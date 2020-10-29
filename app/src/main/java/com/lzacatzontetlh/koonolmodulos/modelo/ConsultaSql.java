package com.lzacatzontetlh.koonolmodulos.modelo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class ConsultaSql extends AppCompatActivity {

    public int IdPago=0, idApertura=0;
    public int statusFinal=0,  id=0, idPag_det=0, clas_fk, esta_fk, emp_fk, prd_stockmax, prd_stockmin, prd_unimed;
    public  double efectivoVntas=0.0, tarjetaVntas=0.0, totalVnta=0.0;
    public String tipo="", info;

   // public  String tipo="", prd_codigo, prd_nombre, prd_descorta, prd_deslarga, prd_observ;
    //public int[] IdClass = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    //***** VENTAS PREVIAS *******//
   //REGISTO DE CAJA-USUARIO
    public void configuracionInicial(int cja, int usr, String clavee, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sql1="insert into caja_usuario(cja_fk, usr_fk, cjus_clav)" +
                " VALUES ("+cja+", "+usr+", '"+clavee+"')" ;

        try {
            db1.execSQL(sql1);
            db1.close();

            if (sql1 != null){
                Toast.makeText(contexto, "Su configuración inicial, se completo..", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //***** CONFIGURACION INICIAL *******//
    //VENTAS ANTERIORES
    public void registrarPago(double monto, String fcha, int metodo, int user, String codigoVerificacion, Context contexto1) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String sql="insert into pago(pag_monto, pag_fecha, met_fk, usr_fk, pag_noaut)" +
                " VALUES ("+monto+", '"+fcha+"', "+metodo+", "+user+", '"+codigoVerificacion+"')" ;

        try {
            db.execSQL(sql);
            db.close();

            ConexionSQLiteHelper conn2;
            conn2=new ConexionSQLiteHelper(contexto1);

            SQLiteDatabase db2 = conn2.getReadableDatabase();
            Cursor cursor=db2.rawQuery("select MAX(pag_id) as mx from pago", null);

            if (cursor != null) {
                cursor.moveToFirst();
                IdPago = cursor.getInt(cursor.getColumnIndex("mx"));
                registrarPagoDet(IdPago, Globales.getInstance().idDocm, Globales.getInstance().cantidadPago, contexto1);
            }
            cursor.close();
            db2.close();

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
        //Toast.makeText(contexto, "Id Cliente: "+IdCliente , Toast.LENGTH_SHORT).show();
    }


    public void registrarPagoDet(int pagoID, int docID, double montoDet, Context contexto2) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto2);
        SQLiteDatabase db = conn.getWritableDatabase();
        String sqlDet="insert into pago_det(pag_fk, doc_fk, pagd_monto)" +
                " VALUES ("+pagoID+", "+docID+", "+montoDet+")" ;

        try {
            db.execSQL(sqlDet);
            db.close();
            if (sqlDet != null) {
                consultaEstatus(contexto2);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    private void consultaEstatus(Context contexto3) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto3);
        SQLiteDatabase db = conn.getReadableDatabase();
        SQLiteDatabase dbUpdate= conn.getWritableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Pagado'",null);

        try {

            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                statusFinal = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
                dbUpdate.execSQL("UPDATE documento SET esta_fk ="+statusFinal+" WHERE doc_id ="+Globales.getInstance().idDocm);
                dbUpdate.execSQL("UPDATE documento SET esta_pago ='Pagado' WHERE doc_id ="+Globales.getInstance().idDocm);
                //Toast.makeText(contexto3, "su estatus cambio a: "+statusFinal, Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    //***** APERTURA DE CAJA(CAJA MENU)*******//
    //1. CONSULTAR CLAVE DE LA TABLA CAJA-USUARIO
    public void consultaClave(Context contexto04) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto04);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorClave =db.rawQuery("SELECT cjus_clav FROM caja_usuario where usr_fk ='"+Globales.getInstance().id_usuario+"'",null);

        try {

            if (cursorClave != null) {
                cursorClave.moveToFirst();
                String claveComp = cursorClave.getString(cursorClave.getColumnIndex("cjus_clav"));
                Globales.getInstance().claveCU = claveComp;
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //2. REGISTRAR APERTURA O CIERRE DE CAJA EN SQLite
    public void registrarApertura(String fchaa, String hra, int tpm, String cjusClav, Context contexto4) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto4);
        SQLiteDatabase db3 = conn.getWritableDatabase();
        String sql="insert into mov_caja(mcja_fecha, mcja_hora, tpm_fk, cjus_clav)" +
                " VALUES ('"+fchaa+"', '"+hra+"', "+tpm+",'"+cjusClav+"')" ;

        try {
            db3.execSQL(sql);
            db3.close();

            ConexionSQLiteHelper conn2;
            conn2=new ConexionSQLiteHelper(contexto4);

            SQLiteDatabase db2 = conn2.getReadableDatabase();
            Cursor cursorAper=db2.rawQuery("select MAX(mcja_id) as mx2 from mov_caja", null);

            if (cursorAper != null) {
                cursorAper.moveToFirst();
                idApertura = cursorAper.getInt(cursorAper.getColumnIndex("mx2"));

                if (Globales.getInstance().cajaOperacion.equals("Apertura")){
                    double montoo = Double.valueOf(Globales.getInstance().mont);
                    registrarApertDet("Apertura",montoo,idApertura, contexto4);
                }else if (Globales.getInstance().cajaOperacion.equals("Cierre")){
                    registrarApertDet("Cierre",Globales.getInstance().montoTotl,idApertura, contexto4);
                }


            }
            cursorAper.close();
            db3.close();

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }

    //3. MÉTODO PARA REGISTRAR APERTURA DE CAJA DETALLE EN SQLite
    public void registrarApertDet(String concepto, double monto, int movDet, Context contexto5) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto5);
        SQLiteDatabase dbDet = conn.getWritableDatabase();
        String sqlDet2="insert into movcaja_det(mcjad_concepto, mcjad_monto, mcja_fk)" +
                       " VALUES ('"+concepto+"', "+monto+", "+movDet+")" ;

        try {
            dbDet.execSQL(sqlDet2);
            dbDet.close();
            if (sqlDet2 != null) {
                Toast.makeText(contexto5, "Se inserto correctamente los datos", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    //***** CORTE X(CAJA MENU)*******//
    //1. MÉTODO PARA EXTRAER EL MONTO CON EL QUE APERTURO LA CAJA
    public void consultaMontoApert(Context contexto6) {
        Globales.getInstance().apertura = 0;
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto6);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursorMontApert =db.rawQuery("SELECT mcjad_monto FROM  caja_usuario, mov_caja, tipo_movimiento, movcaja_det WHERE  tipo_movimiento.tpm_nombre='Apertura' AND tipo_movimiento.tpm_id = mov_caja.tpm_fk " +
                " AND mov_caja.cjus_clav=caja_usuario.cjus_clav AND mov_caja.mcja_fecha = '"+fecha2()+"' AND mov_caja.mcja_id=movcaja_det.mcja_fk",null);
        try {
            cursorMontApert.getCount();

            if (cursorMontApert != null) {
                if (cursorMontApert.getCount()>0) {
                    cursorMontApert.moveToFirst();
                    double montoTotal = cursorMontApert.getDouble(cursorMontApert.getColumnIndex("mcjad_monto"));
                    Globales.getInstance().montoApertura = montoTotal;
                    Globales.getInstance().apertura = 1;
                }else {
                    Globales.getInstance().apertura = 2;
                }

            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }



    //2. MÉTODO PARA OBTENER LOS TOTALES EN EFECTIVO Y TARJETA DEL DÍA
    public void totales(Context contexto71) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto71);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursorPagados=db.rawQuery("SELECT pag_fk " +
                " FROM documento, pago_det, estatus WHERE estatus.esta_estatus='Pagado' and esta_pago='Pagado' and estatus.esta_id = documento.esta_fk and pago_det.doc_fk=documento.doc_id and documento.doc_fecha='"+fecha()+"' and documento.usr_fk='"+Globales.getInstance().id_usuario+"'",null);


        try {
            if (cursorPagados != null) {
                cursorPagados.moveToFirst();
                int indexPagado= 0;

                while (!cursorPagados.isAfterLast()) {
                    idPag_det = cursorPagados.getInt(cursorPagados.getColumnIndex("pag_fk"));

                    Cursor cursorTotal=db.rawQuery("SELECT pag_monto, met_nombre" +
                            " FROM pago, metodo_pago WHERE pag_id='"+idPag_det+"' AND pago.met_fk=metodo_pago.met_id and pago.usr_fk='"+Globales.getInstance().id_usuario+"'and pago.pag_fecha='"+fecha()+"' and (metodo_pago.met_nombre='Efectivo' OR metodo_pago.met_nombre='Tarjeta')",null);


                    try {
                        if (cursorTotal != null) {
                            cursorTotal.moveToFirst();
                            int indexTotal = 0;

                            while (!cursorTotal.isAfterLast()) {
                                tipo = cursorTotal.getString(cursorTotal.getColumnIndex("met_nombre"));
                                totalVnta = cursorTotal.getDouble(cursorTotal.getColumnIndex("pag_monto"));
                                if (tipo.equals("Efectivo")){
                                    efectivoVntas = efectivoVntas+totalVnta;
                                }else if (tipo.equals("Tarjeta")){
                                    tarjetaVntas = tarjetaVntas+totalVnta;
                                }

                                indexTotal++;
                                cursorTotal.moveToNext();
                            }

                            /*if (indexTotal != 0) {
                                Globales.getInstance().totalEfectivo2 = efectivoVntas;
                                Globales.getInstance().totalTarjeta2 = tarjetaVntas;

                            }*/
                        }

                    }catch(Exception e){
                        Log.println(Log.ERROR,"",e.getMessage());
                    }

                    indexPagado++;
                    cursorPagados.moveToNext();
                }

                if (indexPagado != 0) {
                    Globales.getInstance().totalEfectivo2 = efectivoVntas;
                    Globales.getInstance().totalTarjeta2 = tarjetaVntas;

                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //3. MÉTODO PARA OBTENER LOS TOTALES EN EFECTIVO Y TARJETA DEL DÍA
    /*public void totales(Context contexto7) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto7);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorTotal=db.rawQuery("SELECT pag_monto, met_nombre" +
                                  " FROM pago, metodo_pago WHERE pago.met_fk=metodo_pago.met_id and pago.usr_fk='"+Globales.getInstance().id_usuario+"'  and pago.pag_fecha='"+fecha()+"' and (metodo_pago.met_nombre='Efectivo' OR metodo_pago.met_nombre='Tarjeta')",null);


        try {
            if (cursorTotal != null) {
                cursorTotal.moveToFirst();
                int indexTotal = 0;

                while (!cursorTotal.isAfterLast()) {
                    tipo = cursorTotal.getString(cursorTotal.getColumnIndex("met_nombre"));
                    totalVnta = cursorTotal.getDouble(cursorTotal.getColumnIndex("pag_monto"));
                    if (tipo.equals("Efectivo")){
                        efectivoVntas = efectivoVntas+totalVnta;
                    }else if (tipo.equals("Tarjeta")){
                        tarjetaVntas = tarjetaVntas+totalVnta;
                    }

                    indexTotal++;
                    cursorTotal.moveToNext();
                }

                if (indexTotal != 0) {
                    Globales.getInstance().totalEfectivo2 = efectivoVntas;
                    Globales.getInstance().totalTarjeta2 = tarjetaVntas;

                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }*/


    //***** CONFIGURACION INICIAL*******//
    // REGISTRAR DATOS DE LA CONFIGURACION INICIAL EN SQLite
    public void registrarConfiguracion(int idPrs, String nomPrs, int idUsr, int idRol, String nomRol, int idEsta, String nomEsta, int idCaja, String nomCaja, String correo, String clave, int empId, String nombEmp, Context contexto8) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto8);
        SQLiteDatabase dbConfig = conn.getWritableDatabase();
        String sqlConfi="insert into configuracion(cfg_idPrs, cfg_nomPrs, cfg_idUsr, cfg_idRol, cfg_nomRol, cfg_idEsta, cfg_nomEsta, cfg_idCja, cfg_nomCja, cfg_correo, cfg_clave, cfg_idEmp, cfg_nomEmp)" +
                " VALUES ("+idPrs+", '"+nomPrs+"', "+idUsr+", "+idRol+",'"+nomRol+"', "+idEsta+", '"+nomEsta+"', "+idCaja+", '"+nomCaja+"', '"+correo+"', '"+clave+"', "+empId+", '"+nombEmp+"')" ;

        try {
            dbConfig.execSQL(sqlConfi);
            dbConfig.close();
            if (sqlConfi != null) {
                Toast.makeText(contexto8, "Se insertaron correctamente los datos", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    //OBTENER LOS DATOS GENERALES DEL USUARIO QUE INICIO SESIÓN
    public void datosCompletos(Context contexto9) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto9);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorDatos=db.rawQuery("SELECT * FROM configuracion WHERE cfg_correo='"+Globales.getInstance().emailUsr2+"' AND cfg_clave='"+Globales.getInstance().claveUsr2+"'",null);

        try {
            if (cursorDatos != null) {
                cursorDatos.moveToFirst();
                String nomEstablecimiento = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_nomEsta"));
                String nomUsuario = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_nomPrs"));
                String nombCaja = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_nomCja"));
                String nomRool = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_nomRol"));
                String IDCja = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_idCja"));
                String IDUsr = cursorDatos.getString(cursorDatos.getColumnIndex("cfg_idUsr"));

                Globales.getInstance().cajero2 = nomUsuario;
                Globales.getInstance().cajaa2 = nombCaja;
                Globales.getInstance().establecimientoo2 = nomEstablecimiento;
                Globales.getInstance().cajaId = IDCja;
                Globales.getInstance().id_usuario = IDUsr;
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    public void todo2(Context contexto10) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto10);
        SQLiteDatabase db = conn.getReadableDatabase();
       Cursor cursorTotal=db.rawQuery("SELECT * FROM documento WHERE doc_folio='1010412'",null);
        //Cursor cursorTotal=db.rawQuery("SELECT * FROM documento ",null);

        //try {
            if (cursorTotal != null) {
                cursorTotal.moveToFirst();
                String prueba = cursorTotal.getString(cursorTotal.getColumnIndex("esta_pago"));
                Toast.makeText(contexto10, prueba, Toast.LENGTH_LONG).show();
            }

       /* }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }*/

    }


    /// ***** CIERRE DE SESION ***** /////
    public void getJSONCierre(final String urlWebService, final Context context11) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usuario_fk", Globales.getInstance().id_usuario);
                    postDataParams.put("caja_fk", Globales.getInstance().cajaId);
                    postDataParams.put("direcc", Globales.getInstance().direccion);

                    return RequestHandler.sendPost(urlWebService,postDataParams);
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("No hay registros")){
                    finish();
                    Intent intent = new Intent(context11, MainActivity.class);
                    startActivity(intent);

                }else if (s.equals("SE ENCONTRARON COINCIDENCIAS")){
                    finish();
                    Intent intent = new Intent(context11, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(context11, "Sesión  Cerrada", Toast.LENGTH_LONG).show();

                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    //******* SINCRONIZACION INICIAL *******//
    //1. MÉTODO PARA LA BUSQUEDA DE CLASIFICACIÓN
    public void consultaClasificacion (Context contexto12) {
       ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto12);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorClas =db.rawQuery("SELECT clas_id FROM clasificacion WHERE clas_id ="+Globales.getInstance().categoriaId,null);
        try {

            if (cursorClas != null) {
                cursorClas.moveToFirst();
                if(cursorClas.getCount()==0){
                    categoria(Globales.getInstance().categoriaId, Globales.getInstance().categoriaNom, 4, contexto12);
                    //Globales.getInstance().YaInsertoPrd="NO";

                }else {
                    Globales.getInstance().YaInserto="NO";
                   // consultaProducto(contexto12);
                }
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //2.REGISTO DE CATEGORIA NO EXISTENTE
    public void categoria(int id, String nombreClas, int emp, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sql1="insert into clasificacion(clas_id, clas_nombre, emp_fk)" +
                " VALUES (?, ?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sql1);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,id);
        insertStmt.bindString(2, nombreClas);
        insertStmt.bindLong(3, emp);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sql1 != null){
                Globales.getInstance().YaInserto = "SI";
                //Globales.getInstance().YaInsertoPrd="NO";
                //Toast.makeText(contexto, "Se insertó la categoria correctamente..", Toast.LENGTH_LONG).show();
            }else {
                Globales.getInstance().YaInserto = "NO";
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //3. MÉTODO PARA LA ACTUALIZACIÓN DE LA IMG EN CLASIFICACIÓN
    public void UpdateImg(Context contexto13, byte[] img, int id) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto13);
        SQLiteDatabase dbUpd = conn.getWritableDatabase();

        String sql1 = "UPDATE clasificacion SET clas_imagen = ? WHERE clas_id ="+id;
        SQLiteStatement insertStmt = dbUpd.compileStatement(sql1);
        insertStmt.clearBindings();
        insertStmt.bindBlob(1, img);
        insertStmt.executeUpdateDelete();

        try {
            dbUpd.close();

            if (sql1 != null){
                Toast.makeText(contexto13, "Se insertó la categoria correctamente..", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }

    //4. MÉTODO PARA LA BUSQUEDA DEL PRODUCTO
    public void consultaProducto(Context contexto14) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto14);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorPrd =db.rawQuery("SELECT prd_id FROM producto WHERE prd_id ="+Globales.getInstance().prdId,null);
        try {

            if (cursorPrd != null) {
                cursorPrd.moveToFirst();
                if(cursorPrd.getCount()==0){
                    productoo(Globales.getInstance().prdId, Globales.getInstance().prdCod, Globales.getInstance().prdNom,  Globales.getInstance().prdDescorta, Globales.getInstance().prdDeslarga, Globales.getInstance().stockMax, Globales.getInstance().stockMin, Globales.getInstance().unimed, Globales.getInstance().prdObs, Globales.getInstance().categoriaId,  Globales.getInstance().prdEsta, Globales.getInstance().prdEmp, contexto14);

                }else {
                    Globales.getInstance().YaInsertoPrd="NO";
                }
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //5.REGISTO DEL PRODUCTO NO EXISTENTE
    public void productoo(int idPrd, String cdgPrd, String nomPrd, String descorta, String deslarga, int stockmax, int stockmin, int unimed, String obs, int clas, int esta, int emprs, Context contexto15) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto15);
        SQLiteDatabase db2 = conn.getWritableDatabase();
        String sqlPrd="insert into producto(prd_id, prd_codigo, prd_nombre, prd_descorta, prd_deslarga, prd_stockmax, prd_stockmin, prd_unimed, prd_observ, clas_fk, esta_fk, emp_fk)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement insertStmt2 = db2.compileStatement(sqlPrd);
        insertStmt2.clearBindings();
        insertStmt2.bindLong(1,idPrd);
        insertStmt2.bindString(2, cdgPrd);
        insertStmt2.bindString(3, nomPrd);
        insertStmt2.bindString(4, descorta);
        insertStmt2.bindString(5,deslarga);
        insertStmt2.bindLong(6, stockmax);
        insertStmt2.bindLong(7, stockmin);
        insertStmt2.bindLong(8, unimed);
        //insertStmt2.bindBlob(9,img2);
        insertStmt2.bindString(9, obs);
        insertStmt2.bindLong(10, clas);
        insertStmt2.bindLong(11, esta);
        insertStmt2.bindLong(12, emprs);

        insertStmt2.executeInsert();

        try {
            db2.close();

            if (sqlPrd != null){
                Globales.getInstance().YaInsertoPrd = "SI";
            }else{
                Globales.getInstance().YaInsertoPrd="NO";
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //6. MÉTODO PARA LA ACTUALIZACIÓN DE LA IMG EN PRODUCTO
    public void UpdateImg2(Context contexto16, byte[] img2, int idPrd){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto16);
        SQLiteDatabase dbUpd2 = conn.getWritableDatabase();

        String sql2 = "UPDATE producto SET prd_imagen = ? WHERE prd_id ="+idPrd;
        SQLiteStatement insertStmt2 = dbUpd2.compileStatement(sql2);
        insertStmt2.clearBindings();
        insertStmt2.bindBlob(1, img2);
        insertStmt2.executeUpdateDelete();

        try {
            dbUpd2.close();

            if (sql2 != null){

                Toast.makeText(contexto16, "Se insertó el producto correctamente..", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //7. MÉTODO PARA LA BUSQUEDA DE CARACTERISTICAS
    public void consultaCrts(Context contexto17) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto17);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorClas =db.rawQuery("SELECT crts_id FROM caracteristica WHERE crts_id ="+Globales.getInstance().idCarts,null);
        try {

            if (cursorClas != null) {
                cursorClas.moveToFirst();
                if(cursorClas.getCount()==0){
                    caracteristicas(Globales.getInstance().idCarts, Globales.getInstance().nomCarts, Globales.getInstance().clasCarts, contexto17);

                }/*else {
                }*/
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //8.REGISTO DE CARACTERISTICAS NO EXISTENTE
    public void caracteristicas(int idCrt, String nombreCrt, int clas, Context contexto18) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto18);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlCrts="insert into caracteristica(crts_id, crts_nombre, clas_fk)" +
                " VALUES (?, ?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlCrts);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,idCrt);
        insertStmt.bindString(2, nombreCrt);
        insertStmt.bindLong(3, clas);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlCrts != null){
                Toast.makeText(contexto18, "Se insertó la caracteristica correctamente..", Toast.LENGTH_LONG).show();
                //consultaCrtsDet(contexto18);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //9. MÉTODO PARA LA BUSQUEDA DE CARACTERISTICA DETALLE
    public void consultaCrtsDet(Context contexto19) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto19);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorClas =db.rawQuery("SELECT crtd_id FROM caract_det WHERE crtd_id ="+Globales.getInstance().idCartsD,null);
        try {

            if (cursorClas != null) {
                cursorClas.moveToFirst();
                if(cursorClas.getCount()==0){
                    caracteristicasDet(  Globales.getInstance().idCartsD,  Globales.getInstance().descCartsD,  Globales.getInstance().cartsFk, contexto19);

                }/*else {
                }*/
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //10.REGISTO DE CARACTERISTICA DETALLE NO EXISTENTE
    public void caracteristicasDet(int idCrtDet, String descrip, int caracteristica, Context contexto20) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto20);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlCrtsDet="insert into caract_det(crtd_id, ctrd_descripcion, crts_fk)" +
                " VALUES (?, ?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlCrtsDet);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,idCrtDet);
        insertStmt.bindString(2,  descrip);
        insertStmt.bindLong(3, caracteristica);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlCrtsDet != null){
                Toast.makeText(contexto20, "Se insertó la caracteristica det correctamente..", Toast.LENGTH_LONG).show();
                //consultaPrdCarDet(contexto20);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //11. MÉTODO PARA LA BUSQUEDA DE CARACTERISTICA DETALLE-PRODUCTO
    public void consultaPrdCarDet(Context contexto21) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto21);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorPrdCarDet =db.rawQuery("SELECT crtd_fk, prd_fk FROM prod_cardet WHERE crtd_fk ="+Globales.getInstance().cartsDetFk+" and prd_fk="+Globales.getInstance().prdFk,null);
        try {

            if (cursorPrdCarDet != null) {
                cursorPrdCarDet.moveToFirst();
                if(cursorPrdCarDet.getCount()==0){
                    ProCarDet(Globales.getInstance().cartsDetFk, Globales.getInstance().prdFk, contexto21);
                }
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //12.REGISTO DE CARACTERISTICA DETALLE -PRODUCTO NO EXISTENTE
    public void ProCarDet(int idDet, int idPrd, Context contexto22) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto22);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlCrtsDet="insert into prod_cardet(crtd_fk, prd_fk)" +
                " VALUES (?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlCrtsDet);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,idDet);
        insertStmt.bindLong(2, idPrd);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlCrtsDet != null){
                Toast.makeText(contexto22, "Se insertó caracteristica det del producto correctamente..", Toast.LENGTH_LONG).show();
                //consultaPresentacion(contexto22);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //13. MÉTODO PARA LA BUSQUEDA DE PRESENTACIÓN
    public void consultaPresentacion(Context contexto23) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto23);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorPrst =db.rawQuery("SELECT prst_id FROM presentacion WHERE prst_id ="+Globales.getInstance().prstId,null);
        try {

            if (cursorPrst != null) {
                cursorPrst.moveToFirst();
                if(cursorPrst.getCount()==0){
                    Presentacion(Globales.getInstance().prstId, Globales.getInstance().prstDescr, contexto23);
                }
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //14.REGISTO DE PRESENTACION NO EXISTENTE
    public void Presentacion(int idPrst, String descripcionPrst, Context contexto24) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto24);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlPrst="insert into presentacion(prst_id, prst_descripcion)" +
                " VALUES (?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlPrst);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,idPrst);
        insertStmt.bindString(2, descripcionPrst);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlPrst != null){
                Toast.makeText(contexto24, "Se insertó la presentación correctamente..", Toast.LENGTH_LONG).show();
                //consultaPrestPrd(contexto24);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //15. MÉTODO DE LA BUSQUEDA PRESENTACION PRODUCTO
    public void consultaPrestPrd(Context contexto25) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto25);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorPrestPrd =db.rawQuery("SELECT prepro_id FROM prest_prod WHERE prepro_id="+Globales.getInstance().preproId,null);
        try {

            if (cursorPrestPrd != null) {
                cursorPrestPrd.moveToFirst();
                if(cursorPrestPrd.getCount()==0){
                    PrestPrd(Globales.getInstance().preproId,Globales.getInstance().preproCg, Globales.getInstance().preproCom, Globales.getInstance().preproPrd, Globales.getInstance().preproPrst, contexto25);
                }
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //16.REGISTO DE LA PRESENTACIÓN PRODUCTO NO EXISTENTE
    public void PrestPrd(int preproId, String preproCd, double precompra, int prdFk, int prstFk, Context contexto26) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto26);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlPrestPrd="insert into prest_prod(prepro_id, prepro_codigo, prepro_precompra, prd_fk, prst_fk)" +
                " VALUES (?, ?, ?, ?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlPrestPrd);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,preproId);
        insertStmt.bindString(2, preproCd);
        insertStmt.bindDouble(3, precompra);
        insertStmt.bindLong(4, prdFk);
        insertStmt.bindLong(5,prstFk);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlPrestPrd != null){
                Toast.makeText(contexto26, "Se insertó el pres_prod correctamente..", Toast.LENGTH_LONG).show();
                //consultaListPrd(contexto26);
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    //17. MÉTODO PARA LA BUSQUEDA LISTA PRECIO
    public void consultaListPrd(Context contexto27) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto27);
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursorList=db.rawQuery("SELECT lstp_id FROM listaprecio WHERE lstp_id ="+Globales.getInstance().listId,null);
        try {

            if (cursorList != null) {
                cursorList.moveToFirst();
                if(cursorList.getCount()==0){
                    ListPrd(Globales.getInstance().listId, Globales.getInstance().listPrc, Globales.getInstance().listAct, Globales.getInstance().listSeg, Globales.getInstance().listPrepro, contexto27);
                //SI YA EXISTE EL PRECIO Y SE MODIFICÓ SE REALIZARÁ LA ACTUALIZACIÓN
                }/*else {
                    db.execSQL("UPDATE documento SET esta_fk ="+statusFinal+" WHERE doc_id ="+Globales.getInstance().idDocm);
                    //GFG
                }*/
            }

        }catch(Exception e){

            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    //18.REGISTO DE LA LISTA PRECIO NO EXISTENTE
    public void ListPrd(int listId, double listPrecio, int listActualizado,int segm, int prepro, Context contexto28) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto28);
        SQLiteDatabase db1 = conn.getWritableDatabase();
        String sqlList="insert into listaprecio(lstp_id, lstp_precio, lstp_actualizado, seg_fk, prepro_fk)" +
                " VALUES (?, ?, ?, ?, ?)" ;

        SQLiteStatement insertStmt = db1.compileStatement(sqlList);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,listId);
        insertStmt.bindDouble(2, listPrecio);
        insertStmt.bindLong(3, listActualizado);
        insertStmt.bindLong(4, segm);
        insertStmt.bindLong(5, prepro);
        insertStmt.executeInsert();

        try {
            db1.close();

            if (sqlList != null){
                Toast.makeText(contexto28, "Se insertó lista del producto correctamente..", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());

        }
    }


    public static class RequestHandler {
        public  static String sendPost(String r_url , JSONObject postDataParams) throws Exception {
            URL url = new URL(r_url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(encodeParams(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode(); // To Check for 200
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }
            return null;
        }

        private  static String encodeParams(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while(itr.hasNext()){
                String key= itr.next();
                Object value = params.get(key);
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }

    public void prdts(Context contexto111) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto111);
        SQLiteDatabase db = conn.getReadableDatabase();
        //SELECT * FROM producto
        info="";
        Cursor cursorPagados=db.rawQuery("SELECT prd_id, prd_imagen, prd_nombre, prepro_precompra, crts_id,crts_nombre,  producto.clas_fk,prd_fk  FROM producto INNER JOIN clasificacion ON producto.clas_fk= clasificacion.clas_id INNER JOIN caracteristica ON caracteristica.clas_fk= clasificacion.clas_id INNER JOIN prest_prod ON prest_prod.prd_fk= producto.prd_id WHERE  producto.clas_fk=1",null);

        try {
            if (cursorPagados != null) {
                cursorPagados.moveToFirst();
                int indexPagado= 0;

                while (!cursorPagados.isAfterLast()) {
                    String idPrd = cursorPagados.getString(cursorPagados.getColumnIndex("prd_id"));
                    //String cdPrd = cursorPagados.getString(cursorPagados.getColumnIndex("prd_codigo"));
                    String nomPrd = cursorPagados.getString(cursorPagados.getColumnIndex("prd_nombre"));

                    info = info +idPrd+", "+nomPrd;


                    indexPagado++;
                    cursorPagados.moveToNext();
                }
                Toast.makeText(contexto111, info+" CONTADOR: "+cursorPagados.getCount(), Toast.LENGTH_LONG).show();

            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }



    //METODO PARA OBTENER FECHA ACTUAL
    private String fecha() {
        final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }

    //METODO PARA OBTENER FECHA ACTUAL SQLite
    public String fecha2() {
        final SimpleDateFormat fe = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }
}
