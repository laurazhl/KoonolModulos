package com.lzacatzontetlh.koonolmodulos.modelo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.lzacatzontetlh.koonolmodulos.Productos.cuentaProductosC;
import static com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.cuentaProductos;


public class Ingresarsql extends AppCompatActivity {

    //public Long IdCliente=12345678910L;

    String doc=  Globales.getInstance().idDocUl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void registrarPersonaFisica(String nombreCompleto, String nombre, String aPaterno, String aMaterno, String rfc, String calle, String noExt, String noInt, String telefono, String email, String facebook, String twitter, String instagram, String curp, int tipo, int diascred, float limcred, float saldo, int cp, int esta, int tipoPersona, Context contexto) {
        ConexionSQLiteHelper conn;
        conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        int actualizar = 0;
            try {
                String sql = "insert into persona(prs_nombre, prs_rfc, prs_calle, prs_noint, prs_noext, prs_telefono, prs_email, prs_facebook, prs_twitter,prs_instagram, prs_tipo,prs_diascred,prs_limcred,prs_saldo,prs_actualizar, cp_fk,esta_fk,tpp_fk)" +
                        " VALUES ('" + nombreCompleto + "', '" + rfc + "', '" + calle + "', '" + noInt + "','" + noExt + "', '" + telefono + "', '" + email + "', '" + facebook + "', '" + twitter + "', '" + instagram + "', " + tipo + ", " + diascred + ", " + limcred + ", " + saldo + "," + actualizar + ", " + cp + ", " + esta + ", " + tipoPersona + ")";
                db.execSQL(sql);
                int prs_fk = obtenerPrs_fk(contexto, rfc);
                String sqlTablaFisica = "insert into fisica(fsc_nombre,fsc_apaterno,fsc_amaterno,fsc_curp,fsc_actualizar,prs_fk)" +
                        " VALUES ('" + nombre + "', '" + aPaterno + "','" + aMaterno + "',  '" + curp + "','" + actualizar + "', " + prs_fk + ")";
                db.execSQL(sqlTablaFisica);
                db.close();
            } catch (Exception e) {
                System.out.println(" Error en Funcion registrarPersonaFisica :    " + e.getMessage());
            }
    }




    public void registrarPersonaMoral(String razonSocial, String rfc, String calle, String noExt, String noInt, String telefono, String email, String facebook, String twitter, String instagram, int tipo, int diascred, float limcred, float saldo, int cp, int esta, int tipoPersona , Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
            int actualizar = 0;
            try {
                String sql = "insert into persona(prs_nombre, prs_rfc, prs_calle, prs_noint, prs_noext, prs_telefono, prs_email, prs_facebook, prs_twitter,prs_instagram, prs_tipo,prs_diascred,prs_limcred,prs_saldo,prs_actualizar, cp_fk,esta_fk,tpp_fk)" +
                        " VALUES ('" + razonSocial + "', '" + rfc + "', '" + calle + "', '" + noInt + "','" + noExt + "', '" + telefono + "', '" + email + "', '" + facebook + "', '" + twitter + "', '" + instagram + "', " + tipo + ", " + diascred + ", " + limcred + ", " + saldo + ", " + actualizar + ", " + cp + ", " + esta + ", " + tipoPersona + ")";
                db.execSQL(sql);
                int prs_fk=obtenerPrs_fk(contexto,rfc);
                String sqlTablaMoral = "insert into moral(mrl_razonsocial,mrl_actualizar,prs_fk) VALUES ('" + razonSocial + "', " + actualizar + "," + prs_fk + ")";
                db.execSQL(sqlTablaMoral);
                db.close();
            } catch (Exception e) {
                System.out.println(" Error en Funcion registrarPersonaMoral :    " + e.getMessage());
                // Toast.makeText(getApplicationContext(), "Id Registro:  no Existe!!!", Toast.LENGTH_LONG).show();
            }
    }

    public  int obtenerPrs_fk(Context context, String rfc){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();

        String prs_nombre;
        int var=0, prs_id=0;
        Cursor cursor2 =db.rawQuery("SELECT prs_id, prs_nombre from persona WHERE  prs_rfc='"+rfc+"' " , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    prs_id= Integer.parseInt(String.valueOf(cursor2.getColumnIndex("prs_id")));
                    prs_nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    var=prs_id;

                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }
        return  var;
    }

    public String existeLaPersona(Context context, String rfc){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();

        String prs_nombre;
        String var="";
        int  prs_id=0;
        Cursor cursor2 =db.rawQuery("SELECT prs_id, prs_nombre from persona WHERE  prs_rfc='"+rfc+"' " , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    prs_id= Integer.parseInt(String.valueOf(cursor2.getColumnIndex("prs_id")));
                    prs_nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    var="Existe";
                }
                else {
                    var="noExiste";
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }
        return  var;
    }

    public boolean isTableExists(String nombreTabla, Context contexto) {
        boolean isExist = false;

        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nombreTabla + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        db.close();
        return isExist;
    }

    public void registrarFotos(String mCurrentPhotoPath, String tipo, Context contexto) {

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
      //  values.put("cliente_fk", Globales.getInstance().IdCliente);
        values.put("tipoImagen", tipo);
        values.put("nombreImagen", mCurrentPhotoPath);
        values.put("sincronizado", 0);
        Long idResultante = db.insert("imagen", "idImagen", values);
        Toast.makeText(contexto, "Metodo registrar fotoso: " +idResultante+" con idcliente= ", Toast.LENGTH_SHORT).show();
        db.close();
    }


    public void ActualizarEstadoAP(Context contexto, Integer prs_id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        // db.execSQL("UPDATE imagen SET Sincronizado=0");
        db.execSQL("UPDATE persona SET prs_actualizar=1 WHERE prs_id="+prs_id);
        db.close();
    }
    public void ActualizarEstadoPF(Context contexto, Integer fsc_id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE fisica SET fsc_actualizar=1 WHERE fsc_id="+fsc_id);
        db.close();
    }
    public void ActualizarEstadoPM(Context contexto, Integer mrl_id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE moral SET mrl_actualizar=1 WHERE mrl_id="+mrl_id);
        db.close();
    }

    public void registrarClasificacion(String nombreClasificacion , String imagen, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        int actualizar=0;
        String sqlTablaMoral="insert into clasificacion(clas_nombre, clas_imagen) VALUES ('"+nombreClasificacion+"', '"+ imagen+"')" ;

        try {
            System.out.println(" sqlTablaMoral " +sqlTablaMoral);
            db.execSQL(sqlTablaMoral);
            db.close();

        }catch(Exception e){
           // Log.println(Log.ERROR,"",e.getMessage());
            System.out.println("Fallo_:    ");
        }
    }

    public void registrarVentaProceso(String docd_cantprod , String docd_precven, String docd_preccom, String docd_descuento, String prepro_fk, String ext_fk, String imp_fk, String doc_folio, String doc_fecha, String doc_hora, String doc_iva, String docd_cosind, String doc_observ, String doc_saldo , String prs_fk, String usr_fk, String cja_fk, String est_fk, String esta_fk, String tpd_fk, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        try {

            validaSiExisteFolio(contexto);
            if(  Globales.getInstance().vFolio.equals("noExisteElFol")){
                String sqlTablaFolio ="insert into folio( fol_folio,tpd_fk,cja_fk) VALUES ('"+doc_folio+"','"+tpd_fk+"','"+cja_fk+"')" ;
                db.execSQL(sqlTablaFolio);
                System.out.println("  validaSiExisteFolio  "+sqlTablaFolio);

                //Toast.makeText(contexto, "  validaSiExisteFolio1  " , Toast.LENGTH_SHORT).show();
            }

            validaSiExisteDoc(contexto);
            if(  Globales.getInstance().vDoc.equals("noExisteElDoc")) {
                String doc_subtotal= docd_precven;
                String sqlTablaDocumento ="insert into documento(doc_folio, doc_fecha,doc_hora,doc_iva,doc_subtotal, doc_total, doc_descuento,doc_cosind, doc_observ,doc_saldo, prs_fk, usr_fk, cja_fk, est_fk, esta_fk ) VALUES ('"+doc_folio+"','"+doc_fecha+"','"+doc_hora+"','"+doc_iva+"','"+doc_subtotal+"','"+doc_subtotal+"','"+docd_descuento+"','"+docd_cosind+"','"+doc_observ+"','"+doc_saldo+"','"+prs_fk+"', '"+usr_fk+"','"+cja_fk+"','"+est_fk+"','"+esta_fk+"')" ;
                db.execSQL(sqlTablaDocumento);

                System.out.println("  noExisteElDoc  "+sqlTablaDocumento);

                //Toast.makeText(contexto, "  noExisteElDoc  " , Toast.LENGTH_SHORT).show();
            }

            existeD(contexto, prepro_fk);
            if(  Globales.getInstance().vExisteP.equals("existeElProducto")){
                Double cantidad= Double.valueOf(docd_cantprod);
                Double precioUnitario= Double.valueOf(docd_precven);
                Double totalPrevio= cantidad* precioUnitario;

                db.execSQL("UPDATE documento_det SET docd_cantprod="+docd_cantprod+",docd_precven="+totalPrevio+" WHERE prepro_fk="+prepro_fk+" and doc_fk ="+doc );

                Double TotalVenta=0.0;
                Cursor cursor3 = db.rawQuery("SELECT SUM(docd_precven) as suma FROM documento_det WHERE documento_det.doc_fk = "+doc, null);
                cursor3.moveToFirst();
                TotalVenta = Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("suma")));

                db.execSQL("UPDATE documento SET doc_subtotal="+TotalVenta+",doc_total="+TotalVenta+" WHERE doc_id="+doc);


                Toast.makeText(contexto, "Producto agregado al carrito." , Toast.LENGTH_SHORT).show();
            }else {
                if(  Globales.getInstance().vExisteP.equals("noExisteElProducto")){
                    ultimoidoc(contexto,doc_folio );
                    String ultimoId0= Globales.getInstance().idDocUl;
                    String sqlTablaDocumentoDetalle="insert into documento_det(docd_cantprod,docd_precven, docd_preccom , docd_descuento,prepro_fk, ext_fk, doc_fk, imp_fk) VALUES ('"+docd_cantprod+"','"+docd_precven+"','"+docd_preccom+"', '"+docd_descuento+"','"+prepro_fk+"','"+ext_fk+"','"+ultimoId0+"', '"+imp_fk+"')" ;
                    db.execSQL(sqlTablaDocumentoDetalle);

                    //si ya existen otros productos diferente al que se ingresara por primera vez tambien se debe actualizar el total
                    elementosExistentesEnDDe(contexto);
                    int elem= Integer.parseInt(Globales.getInstance().elementosExistentesEnDD);
                    if(elem>1){

                        Double TotalVenta=0.0;
                        Cursor cursor3 = db.rawQuery("SELECT SUM(docd_precven) as suma FROM documento_det WHERE documento_det.doc_fk = "+doc, null);
                        cursor3.moveToFirst();
                        TotalVenta = Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("suma")));
                        db.execSQL("UPDATE documento SET doc_subtotal="+TotalVenta+",doc_total="+TotalVenta+" WHERE doc_id="+doc);
                    }

                    Toast.makeText(contexto, "Producto agregado al carrito." , Toast.LENGTH_SHORT).show();
                }
            }

            contabilizaLosProdAgreCarr(contexto);
            db.close();
        }catch(Exception e){
            System.out.println("Fallo_:    " + e.getMessage());
            Toast.makeText(contexto, "Error dato no agregado al carrito." , Toast.LENGTH_SHORT).show();
            contabilizaLosProdAgreCarr(contexto);
        }
    }

    public void ultimoidoc(Context context, String df) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        consultaestatus(context);
        Cursor cursor2 =db.rawQuery("select * from documento where doc_folio='"+df+"' and  esta_fk='"+Globales.getInstance().idEstatusLau+"'" , null);
        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    Globales.getInstance().idDocUl= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                }
                else
                {
                    if(id.equals("")) {
                        // Globales.getInstance().idDocUl="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    public void ultimoidoc2(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        //Cursor cursor2 =db.rawQuery("select * from documento where doc_folio='"+df+"'" , null);

        consultaestatusPorPagar(context);
        consultaestatusPagado(context);
        int porPagar= Globales.getInstance().idEstatusPorPagar;
        int pagado= Globales.getInstance().idEstatusPagado;

        //Cursor cursor2 =db.rawQuery("select doc_folio,doc_id from documento where esta_fk="+porPagar+" or esta_fk="+pagado+" ORDER BY doc_folio ASC" , null);
        //Cursor cursor2 =db.rawQuery("select doc_folio,doc_id from documento ORDER BY doc_folio ASC" , null);
        Cursor cursor2 =db.rawQuery("select doc_folio,doc_id from documento " , null);
        String id = "";
        try {
            //
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    // int v= Integer.parseInt(Globales.getInstance().idDocUl);
                    int v= Integer.parseInt(id);
                    int oper= v+1;
                    Globales.getInstance().idDocUl= String.valueOf(oper);

                }
                //
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idDocUl="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    private void validaSiExisteDoc(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor2 =db.rawQuery("SELECT *  FROM documento" , null);
        String id = "";
        try {
            if (cursor2 != null) {

                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));

                    if(id!=null){
                        if( Globales.getInstance().idDocUl.equals(id)){
                            Globales.getInstance().vDoc= "existeElDoc";
                            break;
                        }
                        else {
                            Globales.getInstance().vDoc= "noExisteElDoc";
                        }
                        index++;
                        cursor2.moveToNext();
                    }
                }
                if (index != 0) {

                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().vDoc= "noExisteElDoc";
                    }
                    //Globales.getInstance().vDoc= "noExisteElDoc";
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null167 ",e.getMessage());
        }
    }

    private void validaSiExisteFolio(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";
        Cursor cursor2 =db.rawQuery("SELECT *  FROM folio" , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("fol_folio")));
                    if(id!=null) {
                        if (Globales.getInstance().idFolio.equals(id)) {
                            Globales.getInstance().vFolio = "existeElFol";
                            break;
                        }
                        else {
                            Globales.getInstance().vFolio= "noExisteElFol";
                        }
                        index++;
                        cursor2.moveToNext();
                    }
                }
                if (index != 0) {
                    // Globales.getInstance().vFolio= "existeElFol";
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().vFolio= "noExisteElFol";
                    }
                }
            }
            cursor2.close();
            db.close();

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void contabilizaLosProdAgreCarr(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        consultaestatus(context);
        String idu=  Globales.getInstance().id_usuario;
        consultaEmpresaEstableCaja(context,idu);
        String est= Globales.getInstance().idEstablecimientoLau;
        ///Cursor cursor2 =db.rawQuery("select sum(docd_cantprod) as cantidadProductos FROM documento_det  INNER JOIN documento ON documento_det.doc_fk=documento.doc_id WHERE  doc_fk ="+doc+" and  esta_fk='4'", null);
        Cursor cursor2 =db.rawQuery("select sum(docd_cantprod) as cantidadProductos FROM documento_det  INNER JOIN documento ON documento_det.doc_fk=documento.doc_id WHERE    esta_fk='"+Globales.getInstance().idEstatusLau+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cantidadProductos")));
                    if(id.equals("null")){
                        cuentaProductosC.setText("0");
                        cuentaProductos.setText("0");
                    }
                    else {
                        cuentaProductosC.setText(id);
                        cuentaProductos.setText(id);
                    }
                    if(id.equals("0")){
                        cuentaProductosC.setText("0");
                        cuentaProductos.setText("0");
                    }
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    System.out.println(" Termino " );
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }
    private void existeD(Context contexto, String query) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";

        Cursor cursor2 =db.rawQuery("SELECT prepro_fk,docd_cantprod,doc_fk FROM documento_det WHERE prepro_fk ='"+query+"' and doc_fk ="+doc , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().vNumDoc= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fk")));
                    Globales.getInstance().vExisteP = "existeElProducto";
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    System.out.println("F  existeD" );
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().vExisteP= "noExisteElProducto";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void ActualizarVenta(Context contexto, String prepro_fk, String docd_cantprod, String docd_precven) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        existeD(contexto, prepro_fk);
        if(  Globales.getInstance().vExisteP.equals("existeElProducto")){
            Double cantidad= Double.valueOf(docd_cantprod);
            Double precioUnitario= Double.valueOf(docd_precven);
            Double totalPrevio= cantidad* precioUnitario;
            db.execSQL("UPDATE documento_det SET docd_cantprod="+docd_cantprod+",docd_precven="+totalPrevio+" WHERE prepro_fk="+prepro_fk+" and doc_fk ="+doc );
            Double TotalVenta=0.0;
            Cursor cursor3 = db.rawQuery("SELECT SUM(docd_precven) as suma FROM documento_det WHERE documento_det.doc_fk = "+doc, null);
            cursor3.moveToFirst();
            TotalVenta = Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("suma")));
            db.execSQL("UPDATE documento SET doc_subtotal="+TotalVenta+",doc_total="+TotalVenta+" WHERE doc_id="+doc);
            db.close();
            cursor3.close();
            contabilizaLosProdAgreCarr(contexto);
        }else {
            System.out.println("No Existe el producto  ");
        }
        contabilizaLosProdAgreCarr(contexto);
    }

    public void ActualizarVentaEli(Context contexto, String prepro_fk) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        existeD(contexto, prepro_fk);
        if(  Globales.getInstance().vExisteP.equals("existeElProducto")){

            db.execSQL("DELETE FROM documento_det WHERE prepro_fk="+prepro_fk+" and doc_fk ="+doc);
            Double TotalVenta=0.0;
            Cursor cursor3 = db.rawQuery("SELECT SUM(docd_precven) as suma FROM documento_det WHERE documento_det.doc_fk = "+doc, null);
            cursor3.moveToFirst();
            TotalVenta = Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("suma")));
            db.execSQL("UPDATE documento SET doc_subtotal="+TotalVenta+",doc_total="+TotalVenta+" WHERE doc_id="+doc);
            cursor3.close();
            db.close();
            contabilizaLosProdAgreCarr(contexto);
        }else {
            if(  Globales.getInstance().vExisteP.equals("noExisteElProducto")){
                System.out.println("No Existe el producto  ");
            }
        }
        contabilizaLosProdAgreCarr(contexto);
    }

    public void ActualizarV(Context contexto, String prepro_fk, String doc_id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        existeD(contexto, prepro_fk);
        if(  Globales.getInstance().vExisteP.equals("existeElProducto")){
            Cursor cursorC=db.rawQuery("DELETE FROM documento_det WHERE documento_det.doc_fk = "+doc_id, null);
            Cursor cursorC2 =db.rawQuery("DELETE FROM folio WHERE fol_folio IN (SELECT fol_folio FROM folio fol INNER JOIN documento doc ON (fol.fol_folio=doc.doc_folio ) WHERE doc.doc_id ="+doc_id+")", null);
            Cursor cursorC3 =db.rawQuery("DELETE FROM documento WHERE documento.doc_id= "+doc_id, null);
            cursorC.moveToFirst();
            cursorC2.moveToFirst();
            cursorC3.moveToFirst();
            db.close();
            cursorC.close();
            cursorC2.close();
            cursorC3.close();
            contabilizaLosProdAgreCarr(contexto);

        }else {
            if(  Globales.getInstance().vExisteP.equals("noExisteElProducto")){
                System.out.println("No Existe el producto  ");
            }
        }
        contabilizaLosProdAgreCarr(contexto);
    }


    public void elementosExistentesEnDDe(Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";

        Cursor cursor2 =db.rawQuery("SELECT COUNT (*)  FROM documento_det WHERE doc_fk ="+doc , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().elementosExistentesEnDD= String.valueOf( cursor2.getString(cursor2.getColumnIndex("COUNT (*)")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //System.out.println("F  existeD" );
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().elementosExistentesEnDD= "0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void existeCanti(Context contexto, String query) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";

        //Cursor cursor2 =db.rawQuery("SELECT prd_fk,docd_cantprod,doc_fk FROM documento_det WHERE prd_fk ='"+query+"' and doc_fk ="+doc , null);
        Cursor cursor2 =db.rawQuery("SELECT prepro_fk,docd_cantprod,doc_fk FROM documento_det WHERE prepro_fk ='"+query+"' and doc_fk ="+doc , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().vCPE= String.valueOf( cursor2.getString(cursor2.getColumnIndex("docd_cantprod")));
                    Globales.getInstance().vExisteCantidad = "existeCantidad";

                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    System.out.println("F  existeD" );
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().vExisteCantidad= "noExisteCantidad";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    public void consultarNombreUsuario(Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";
        String idu=  Globales.getInstance().id_usuario;

        //cambio por la nueva configuracion
        //Cursor cursor2 =db.rawQuery("SELECT usr_usuario, prs_nombre from usuario INNER JOIN  persona ON usuario.prs_fk = persona.prs_id WHERE usr_id="+idu , null);
        Cursor cursor2 =db.rawQuery("SELECT  cfg_nomPrs from configuracion WHERE cfg_idUsr="+idu , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_nomPrs")));


                    String[] parts = id.split(" ");
                    final String part1 = parts[0];

                    Globales.getInstance().nombreUsuario=part1;
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                }
                else { if(id.equals("")) { Globales.getInstance().nombreUsuario= ""; } }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


  /*  public void consultarNombreUsuarioEnCancelacion (Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";
        String idu=  Globales.getInstance().idUsuarioCancelacion;
        //Cursor cursor2 =db.rawQuery("SELECT usr_usuario from usuario WHERE usr_id="+idu , null);
        Cursor cursor2 =db.rawQuery("SELECT usr_usuario, prs_nombre from usuario INNER JOIN  persona ON usuario.prs_fk = persona.prs_id WHERE usr_id="+idu , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));


                    String[] parts = id.split(" ");
                    final String part1 = parts[0];

                    Globales.getInstance().nombreUsuarioCancelacion=part1;
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                }
                else { if(id.equals("")) { Globales.getInstance().nombreUsuarioCancelacion= ""; } }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }*/

    public String generarFolio(Context context){
        String usu =Globales.getInstance().id_usuario;
        consultaEmpresaEstableCaja(context,usu);

        /////generar folio//////////////////////////////////////////////////////////////////////////
        String empresaF="", establecimientoF="", cajaF="";
        String empresa =Globales.getInstance().idEmpresaLau;//cambiar por idemprese

        int e1= Integer.parseInt(empresa);
        if(empresa!=null && e1!=0){
            int var = empresa.length();
            if(var==1){
                //empresaF= "0"+ empresa;
                empresaF=  empresa;
            }else
            if(var==2){
                empresaF = empresa.substring(0, 2);
            }
            else {
                empresaF = empresa.substring(0, 2);
            }
        }else
        {
            empresaF = "1";
            // empresaF = "00";
        }


        String establecimiento =Globales.getInstance().idEstablecimientoLau;
        int e2= Integer.parseInt(establecimiento);
        if(establecimiento!=null && e2!=0){
            int var = establecimiento.length();
            if(var==1){
                establecimientoF= "0"+ establecimiento;
            }else
            {
                establecimientoF = establecimiento.substring(0, 2);
            }
        }else
        {
            establecimientoF = "00";
        }
        String caja =Globales.getInstance().idCajaLau;
        int e3= Integer.parseInt(caja);
        if(caja!=null && e3!=0){
            int var = caja.length();
            if(var==1){
                cajaF= "0"+ caja;
            }else
            {
                cajaF = caja.substring(0, 2);
            }
        }else
        {
            cajaF = "00";
        }
        String fo="";


        ultimoidoc2(context);
        String t1= Globales.getInstance().idDocUl;


        int t= Integer.parseInt(t1);

        consultartpdVenta(context);
        String tpd= String.valueOf(Globales.getInstance().idTpdVenta);



        if(t!=0  ) {//comprueba si ya existe un doc aunque no este en estado en proceso tal vez ya este en estadpo por pagar o  pagado

            ///hacer una consulta del ultimo folio  existente  q teng en proceso o  por pafar

            String fo2 = consultarUltimoFolioPV(context);
            String folio3= empresaF+establecimientoF+cajaF+tpd+fo2;
            fo=folio3;
            // fo= veriFolio(context,folio3);

        }
        else {

            //Globales.getInstance().idDocUl="1";
            //
            String fo1="1";
            String folio3= empresaF+establecimientoF+cajaF+tpd+fo1;
            String folio= veriFolio(context,folio3);
            fo=empresaF+establecimientoF+cajaF+tpd+folio;


        }


        String folio= fo;

        return folio;
    }

    //
    public String veriFolio(Context context, String folio) {
        String fo="";
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";
        Cursor cursor2 =db.rawQuery("SELECT *  FROM folio" , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("fol_folio")));
                    if(id!=null) {
                        if (folio.equals(id)) {
                            index++;

                            break;
                        }
                        cursor2.moveToNext();
                    }
                }
                if (index != 0) {

                    int tamaño=id.length();
                    if(tamaño<=12){
                        String f1;
                        f1=id.substring(6);
                        int  s= Integer.parseInt(f1);
                        int s2= s+1;
                        String f5= String.valueOf(s2);
                        fo=f5;
                    }
                    if(tamaño==13){
                        String f1;
                        f1=id.substring(7);
                        int  s= Integer.parseInt(f1);
                        int s2= s+1;
                        String f5= String.valueOf(s2);
                        fo=f5;
                    }
                }
                else
                {
                    fo="1";
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
        return fo;
    }




    private String consultarUltimoFolioPV(Context context) {
        consultartpdVenta(context);
        int idTpdVenta= Globales.getInstance().idTpdVenta;
        String fo="";
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String id="";
        //cambio por la nueva configuracion
        //Cursor cursor2 =db.rawQuery("select doc_folio from documento where esta_fk="+porPagar+" or esta_fk="+pagado, null);
        Cursor cursor2 =db.rawQuery("SELECT fol_folio from  folio where tpd_fk="+idTpdVenta, null);

        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("fol_folio")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    int tamaño=id.length();
                    if(tamaño<=12){
                        String f1;
                        f1=id.substring(6);



                        System.out.println(" f1f1 empresa "+f1);




                        int  s= Integer.parseInt(f1);
                        int s2= s+1;
                        String f5= String.valueOf(s2);
                        fo=f5;
                    }
                    if(tamaño==13){
                        String f1;
                        f1=id.substring(7);
                        int  s= Integer.parseInt(f1);
                        int s2= s+1;
                        String f5= String.valueOf(s2);
                        fo=f5;
                    }
                }
                else
                {
                    //fo="1";
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
        return fo;
    }

    public void consultaEmpresaEstableCaja(Context context, String usu) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor2 =db.rawQuery("SELECT cfg_idEsta,cfg_idCja,cfg_nomEsta,cfg_idEmp FROM configuracion WHERE  cfg_idUsr='"+usu+"'" , null);

        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().idEmpresaLau= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idEmp")));
                    Globales.getInstance().idEstablecimientoLau= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idEsta")));
                    Globales.getInstance().idCajaLau= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idCja")));
                    Globales.getInstance().nomEst= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_nomEsta")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //tiene datos
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idEmpresaLau="0";
                        Globales.getInstance().idEstablecimientoLau="0";
                        Globales.getInstance().idCajaLau="0";
                        Globales.getInstance().nomEst="";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }


    public void consultaCaja(Context context, String usu) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor2 =db.rawQuery("SELECT cfg_idCja from configuracion where cfg_idUsr='"+usu+"'" , null);
        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().idCajaCancelacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idCja")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //tiene datos
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idCajaCancelacion="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    public void consultaEsatblecimientoCancelacion(Context context, String usu) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();

        //Cursor cursor2 =db.rawQuery("SELECT cja_fk,est_nombre, caja_usuario.usr_fk, est_fk, emp_fk from caja_usuario INNER JOIN caja ON caja_usuario.cja_fk= caja.cja_id INNER JOIN establecimiento  ON caja.est_fk= establecimiento.est_id  INNER JOIN empresa ON  establecimiento.emp_fk= empresa.emp_id  WHERE caja_usuario.usr_fk='"+usu+"'" , null);
        //cambio por configuracion
        Cursor cursor2 =db.rawQuery("SELECT  cfg_idEsta from configuracion WHERE cfg_idUsr='"+usu+"'" , null);
        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().idEstablecimientoCancelacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idEsta")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //tiene datos
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idEstablecimientoCancelacion="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    public void consultaestatus(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='En proceso'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idEstatusLau = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));

            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }

    public void consultaestatusPorPagar(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Por pagar'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idEstatusPorPagar = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }


    public void consultaestatusPagado(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Pagado'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idEstatusPagado = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }

    public void consultaestatusCancelado(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Cancelada'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idEstatusCancelado = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }


    public void consultarIDVentaPublico(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM persona WHERE persona.prs_nombre='VENTA AL PUBLICO'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idVentaalPublico = cursorEstatus.getInt(cursorEstatus.getColumnIndex("prs_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }



    public void consultartpdCancelaciones(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM tipo_documento WHERE tipo_documento.tpd_documento='Cancelaciones'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idTpdCancela = cursorEstatus.getInt(cursorEstatus.getColumnIndex("tpd_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }


    public void consultartpdVenta(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM tipo_documento WHERE tipo_documento.tpd_documento='Venta'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idTpdVenta = cursorEstatus.getInt(cursorEstatus.getColumnIndex("tpd_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }

    public String fecha() {
        final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }

    public String hora() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String datetime = dateFormat.format(c.getTime());
        return datetime;
    }


    public void registrarCancelacion(String doc_fecha, String doc_hora, String doc_iva, String doc_subtotal, String doc_total, String doc_descuento, String docd_cosind, String doc_observ, String doc_saldo , String prs_fk, String usr_fk, String est_fk, String esta_fk, String fol_folio, String tpd_fk, String cja_fk, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        String sqlTablaFolio ="insert into folio( fol_folio,tpd_fk,cja_fk) VALUES ('"+fol_folio+"','"+tpd_fk+"','"+cja_fk+"')" ;
        String consultaInsertarCancelacion ="insert into documento(doc_folio, doc_fecha,doc_hora,doc_iva,doc_subtotal, doc_total, doc_descuento,doc_cosind, doc_observ,doc_saldo, prs_fk, usr_fk, cja_fk, est_fk, esta_fk ) VALUES ('"+fol_folio+"','"+doc_fecha+"','"+doc_hora+"','"+doc_iva+"','"+doc_subtotal+"','"+doc_total+"','"+doc_descuento+"','"+docd_cosind+"','"+doc_observ+"','"+doc_saldo+"','"+prs_fk+"', '"+usr_fk+"','"+cja_fk+"','"+est_fk+"','"+esta_fk+"')" ;
        try {
            db.execSQL(sqlTablaFolio);
            db.execSQL(consultaInsertarCancelacion);
            // Toast.makeText(contexto, "Documento registrado. " , Toast.LENGTH_LONG).show();
            db.close();
        }catch(Exception e){
            System.out.println("  Error  " + e.getMessage());
        }
    }


    public void registrarDocumentoDetaCancelacion(String docd_cantprod , String docd_precven, String docd_preccom, String docd_descuento, String prepro_fk, String ext_fk, String doc_fk , String imp_fk, Context contexto) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();

        String sqlTablaDocumentoDetalle="insert into documento_det(docd_cantprod,docd_precven, docd_preccom , docd_descuento,prepro_fk, ext_fk, doc_fk, imp_fk) VALUES ('"+docd_cantprod+"','"+docd_precven+"','"+docd_preccom+"', '"+docd_descuento+"','"+prepro_fk+"','"+ext_fk+"','"+doc_fk+"', '"+imp_fk+"')" ;

        try {
            db.execSQL(sqlTablaDocumentoDetalle);
            // Toast.makeText(contexto, "Documento registrado. " , Toast.LENGTH_LONG).show();
            db.close();
        }catch(Exception e){
            System.out.println("Fallo_:    " + e.getMessage());
            Toast.makeText(contexto, "Error dato no agregado." , Toast.LENGTH_SHORT).show();
            contabilizaLosProdAgreCarr(contexto);
        }
    }
    public void consultaEmpresaEstableCajaCancel(Context context, String usu) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        //SELECT  emp_fk, cja_id, est_id from establecimiento INNER JOIN caja ON  establecimiento.est_id= caja.est_fk   WHERE usr_fk=9
        //Cursor cursor2 =db.rawQuery("SELECT cja_fk, caja_usuario.usr_fk, est_fk, emp_fk from caja_usuario INNER JOIN caja ON caja_usuario.cja_fk= caja.cja_id INNER JOIN establecimiento  ON caja.est_fk= establecimiento.est_id  INNER JOIN empresa ON  establecimiento.emp_fk= empresa.emp_id  WHERE caja_usuario.usr_fk='"+usu+"'" , null);
        Cursor cursor2 =db.rawQuery("SELECT  cfg_idEsta,cfg_idEsta, cfg_idCja from configuracion WHERE cfg_idUsr='"+usu+"'" , null);
        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Globales.getInstance().idEmpresaCancel= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idEsta")));
                    Globales.getInstance().idEstablecimientoCancel= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idEsta")));
                    Globales.getInstance().idCajaCancel= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idCja")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //tiene datos
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idEmpresaCancel="0";
                        Globales.getInstance().idEstablecimientoCancel="0";
                        Globales.getInstance().idCajaCancel="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    public void existeUnDocCancelacion(Context context) {
        consultartpdCancelaciones(context);
        int Cancelaciones=Globales.getInstance().idTpdCancela;
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        consultaestatus(context);
        Cursor cursor2 =db.rawQuery("SELECT * FROM folio INNER JOIN tipo_documento ON folio.tpd_fk=tipo_documento.tpd_id WHERE tipo_documento.tpd_id='"+Cancelaciones+"'" , null);
        String folioAuxCancel = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    folioAuxCancel=  cursor2.getString(cursor2.getColumnIndex("fol_folio"));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //si ya existe entonces solo le sumamos un uno al ultimo numero
                    Globales.getInstance().folioCancel=    generarFolioCCYaExisteUnFECancel(context,folioAuxCancel);
                }
                else
                {
                    if(folioAuxCancel.equals("")) {
                        //si no existe un folio entonces se genera por primera vez
                        Globales.getInstance().folioCancel= generarFolioCancelacion(context);
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }

    public String generarFolioCancelacion(Context context){
        String usu =Globales.getInstance().idUsuarioCancelacion;
        consultaEmpresaEstableCajaCancel(context,usu);
        String empresaF="", establecimientoF="", cajaF="";
        String empresa =Globales.getInstance().idEmpresaCancel;
        int e1= Integer.parseInt(empresa);
        if(empresa!=null && e1!=0){

            int var = empresa.length();
            if(var==1){
                empresaF=  empresa;
            }else
            if(var==2){
                empresaF = empresa.substring(0, 2);
            }
            else {
                empresaF = empresa.substring(0, 2);
            }
        }else
        {
            empresaF = "1";
            //empresaF = "00";
        }
        String establecimiento =Globales.getInstance().idEstablecimientoCancel;
        int e2= Integer.parseInt(establecimiento);
        if(establecimiento!=null && e2!=0){
            int var = establecimiento.length();
            if(var==1){
                establecimientoF= "0"+ establecimiento;
            }else
            {
                establecimientoF = establecimiento.substring(0, 2);
            }
        }else
        {
            establecimientoF = "00";
        }
        String caja =Globales.getInstance().idCajaCancel;
        int e3= Integer.parseInt(caja);
        if(caja!=null && e3!=0){
            int var = caja.length();
            if(var==1){
                cajaF= "0"+ caja;
            }else
            {
                cajaF = caja.substring(0, 2);
            }
        }else
        {
            cajaF = "00";
        }


        consultartpdCancelaciones(context);
        int tpdC=Globales.getInstance().idTpdCancela;
        String fo="1";

        String folio= empresaF+establecimientoF+cajaF+tpdC+fo;

        return folio;
    }

    public String generarFolioCCYaExisteUnFECancel(Context context, String folioAuxCancel ) {
        String usu =Globales.getInstance().idUsuarioCancelacion;
        consultaEmpresaEstableCajaCancel(context,usu);
        String empresaF="", establecimientoF="", cajaF="";
        String empresa =Globales.getInstance().idEmpresaCancel;
        int e1= Integer.parseInt(empresa);
        if(empresa!=null && e1!=0){


            int var = empresa.length();
            if(var==1){
                empresaF=  empresa;
            }else
            if(var==2){
                empresaF = empresa.substring(0, 2);
            }
            else {
                empresaF = empresa.substring(0, 2);
            }
        }else
        {
            empresaF = "1";
            //empresaF = "0";
        }
        String establecimiento =Globales.getInstance().idEstablecimientoCancel;
        int e2= Integer.parseInt(establecimiento);
        if(establecimiento!=null && e2!=0){
            int var = establecimiento.length();
            if(var==1){
                establecimientoF= "0"+ establecimiento;
            }else
            {
                establecimientoF = establecimiento.substring(0, 2);
            }
        }else
        {
            establecimientoF = "00";
        }
        String caja =Globales.getInstance().idCajaCancel;
        int e3= Integer.parseInt(caja);
        if(caja!=null && e3!=0){
            int var = caja.length();
            if(var==1){
                cajaF= "0"+ caja;
            }else
            {
                cajaF = caja.substring(0, 2);
            }
        }else
        {
            cajaF = "00";
        }
        String fo="";
        int tamaño=folioAuxCancel.length();
        if(tamaño<=12){
            String f1;
            f1=folioAuxCancel.substring(6);
            int  s= Integer.parseInt(f1);
            int s2= s+1;
            String f5= String.valueOf(s2);
            fo=f5;
        }
        if(tamaño==13){
            String f1;
            f1=folioAuxCancel.substring(7);
            int  s= Integer.parseInt(f1);
            int s2= s+1;
            String f5= String.valueOf(s2);
            fo=f5;
        }
        consultartpdCancelaciones(context);
        int tpdC=Globales.getInstance().idTpdCancela;
        String folio= empresaF+establecimientoF+cajaF+tpdC+fo;
        return  folio;
    }



    public void ultimoidocCancelacion(Context context, String df) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        consultaestatus(context);
        Cursor cursor2 =db.rawQuery("select * from documento where doc_folio='"+df+"' and  esta_fk='"+Globales.getInstance().idEstatusCancelado+"'" , null);
        String id = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    Globales.getInstance().idUltimoDocDCancelacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                }
                else
                {
                    if(id.equals("")) {
                        Globales.getInstance().idUltimoDocDCancelacion="0";
                    }
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Null16 ",e.getMessage());
        }
    }



    public void cambiarEstatusAVentaCancelada(Context contexto, String doc_id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        consultaestatusCancelado(contexto);
        int estatus = Globales.getInstance().idEstatusCancelado;
        db.execSQL("UPDATE documento SET esta_fk="+estatus+" WHERE doc_id="+doc_id);
        db.close();
    }

    public void consultarNombreEstaCancelaciones(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String idEsta= Globales.getInstance().idEstablecimientoCancel;
        Cursor cursor =db.rawQuery("select  est_nombre from establecimiento where est_id='"+idEsta+"'",null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                Globales.getInstance().nombreEstCancel = cursor.getString(cursor.getColumnIndex("est_nombre"));
            }
            cursor.close();
            db.close();
        }catch(Exception e){
        }
    }



    public  void limpiarVariablesGlobales(){
        Globales.getInstance().idDocUl="";
        Globales.getInstance().idFolio="";
        Globales.getInstance().vDoc="";
        Globales.getInstance().vFolio="";
        Globales.getInstance().vExisteP="";
        Globales.getInstance().vExisteP2="";
        Globales.getInstance(). elementosExistentesEnDD="";
        Globales.getInstance().vNumDoc="";
        Globales.getInstance().vNumDoc2="";
        Globales.getInstance().vCPE="";
        Globales.getInstance().vExisteCantidad="";
        Globales.getInstance().ineFrontal="";
        Globales.getInstance().ineInversa="";
        Globales.getInstance().nombreUsuario="";
        Globales.getInstance().nombreUsuarioCancelacion="";
        Globales.getInstance().folioVenta="";
        Globales.getInstance().idEmpresaLau="";
        Globales.getInstance().idEstablecimientoLau="";
        Globales.getInstance().idCajaLau="";
        Globales.getInstance().idCajaCancelacion="";
        Globales.getInstance(). idUsuarioCancelacion="";
        Globales.getInstance(). idEstablecimientoCancelacion="";
        Globales.getInstance().idEstatusLau=0;
        Globales.getInstance().idEstatusPorPagar=0;
        Globales.getInstance().idEstatusPagado=0;
        Globales.getInstance().idEstatusCancelado=0;
        Globales.getInstance().idVentaalPublico=0;
        Globales.getInstance().idTpdCancela=0;
        Globales.getInstance().idTpdVenta=0;
        Globales.getInstance().idEmpresaCancel="";
        Globales.getInstance().idEstablecimientoCancel="";
        Globales.getInstance().idCajaCancel="";
        Globales.getInstance().folioCancel="";
        Globales.getInstance().idUltimoDocDCancelacion="";
        Globales.getInstance().cancelDLV="";
        Globales.getInstance().nombreEstCancel="";
        Globales.getInstance().subTCancel="";
        Globales.getInstance().ivaCancel="";
        Globales.getInstance().totalCancel="";
        Globales.getInstance().mensajeEnT="";
        Globales.getInstance().cajeroEnT="";
    }



    public void consultaridCajero(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT DISTINCT cfg_idRol FROM configuracion WHERE cfg_nomRol='Cajero'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idRolCajero = cursorEstatus.getInt(cursorEstatus.getColumnIndex("cfg_idRol"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }

    public void consultarIdAdministrador(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT DISTINCT cfg_idRol FROM configuracion WHERE cfg_nomRol='Administrador'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idRolAdministrador = cursorEstatus.getInt(cursorEstatus.getColumnIndex("cfg_idRol"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }

    public void consultarIdSuperAdmin(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT DISTINCT cfg_idRol FROM configuracion WHERE cfg_nomRol='Super Administrador'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idRolSuperAdmi = cursorEstatus.getInt(cursorEstatus.getColumnIndex("cfg_idRol"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }


    public  int  loginEnCancelacion(Context context, String usuario, String contraseña){
        consultarIdAdministrador(context);
        int admi= Globales.getInstance().idRolAdministrador;
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String usu, pass;
        int var=0, idUsu;
        Cursor cursor2 =db.rawQuery("SELECT cfg_correo ,cfg_idUsr, cfg_clave from  configuracion WHERE cfg_correo='"+usuario+"'  and cfg_clave='"+contraseña+"' and cfg_idRol='"+admi+"' " , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    idUsu= Integer.parseInt(String.valueOf(cursor2.getColumnIndex("cfg_idUsr")));
                    Globales.getInstance().idUsuarioCancelacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idUsr")));
                    usu= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_correo")));
                    pass= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_clave")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //Toast.makeText(context, "Exito" , Toast.LENGTH_SHORT).show();123456
                    var=1;
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }
        return  var;

    }

    public  void   mensajeParaCajero(Context context){
        consultarIdAdministrador(context);
        int admi= Globales.getInstance().idRolAdministrador;
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        String nombreAdmi="";
        Cursor cursor2 =db.rawQuery("SELECT cfg_nomPrs  from configuracion WHERE cfg_idRol='"+admi+"'" , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    nombreAdmi= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_nomPrs")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    LayoutInflater imagenadvertencia_alert = LayoutInflater.from(context);
                    final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                    alerta.setMessage("SÃ³lo un usuario administrador puede cancelar, nombre del administrador de esta tableta: "+ nombreAdmi).setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);
                    alerta.show();

                }
                else
                {
                    //tener la mac y compararala con la persona que se intenta logear y decirle quien es el administradoir
                    Toast.makeText(context, "No se puede cancelar " , Toast.LENGTH_SHORT).show();

                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }

    }




    public void consultaestatusHabilitado(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Habilitado'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                Globales.getInstance().idEstatusHabilitado = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }




    public void mostrarDetalles(String idClasificacion, Context context) {
        consultaestatusHabilitado(getApplicationContext());
        int idEHabilitado=Globales.getInstance().idEstatusHabilitado;
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT prepro_id,  prd_nombre, lstp_precio, prst_fk, prst_descripcion , prd_codigo, seg_nombre\n" +
                "                FROM prest_prod \n" +
                "                INNER JOIN producto On producto.prd_id= prest_prod.prd_fk \n" +
                "                INNER JOIN presentacion ON  presentacion.prst_id= prest_prod.prst_fk  \n" +
                "INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id\n" +
                "INNER JOIN  segmento ON segmento.seg_id=listaprecio.seg_fk                \n" +
                "WHERE  producto.clas_fk='"+idClasificacion+"' and producto.esta_fk='"+idEHabilitado+"'",null);
        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();
                //Globales.getInstance(). = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
            }
            cursorEstatus.close();
            db.close();
        }catch(Exception e){
        }
    }



    public  void borrardatos(Context contexto){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(contexto);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursorC=db.rawQuery("DELETE FROM  persona;" , null);
        Cursor cursorC2 =db.rawQuery("DELETE FROM fisica;" , null);
        Cursor cursorC3 =db.rawQuery("DELETE FROM moral;" , null);

        cursorC.moveToFirst();
        cursorC2.moveToFirst();
        cursorC3.moveToFirst();

        db.close();
        cursorC.close();
        cursorC2.close();
        cursorC3.close();


    }



  /*
if(sq.isTableExists("persona",getApplicationContext())==true){
        System.out.println(" la tabla exisate   " );
    }else {
        System.out.println(" la tabla no exisate   " );
    }*/




}