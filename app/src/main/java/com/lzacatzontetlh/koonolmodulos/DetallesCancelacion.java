package com.lzacatzontetlh.koonolmodulos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.commons.PrinterConnect;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewDatosDetaCancelacion;
import com.lzacatzontetlh.koonolmodulos.modelo.detallesCancelacionDatos;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// Autor: Laura Zacatzontetl Hernandez
public class DetallesCancelacion extends AppCompatActivity {
    public static final String idDC="idDC";
    public static final String hora="hora";
    public static final String folioVenta="folioVenta";
    public static final String fechav="fechav";
    public static final String estatus="estatus";
    public static final String totalVenta="totalVenta";
    private EditText editTextEmail;
    private EditText editTextPassword;
    int numeero;
    private ProgressDialog progressDialog;
    String email, password;
    String usrStatus, usrid;
    static String str, usrID;
    ArrayList codigoList = new ArrayList();


    ArrayList<detallesCancelacionDatos> productos;
    RecyclerViewDatosDetaCancelacion adaptaVntsLista;
    Button aceptarCancelacion2, regresar;
    final Context context = this;
    ConexionSQLiteHelper conn;
    RecyclerView recyclerView;
    Button aceptarCancelacion;
    TextView folioV,fecha,horaDCq,cajero,subtotal, iva, total;
    final Ingresarsql sq = new Ingresarsql();
    private RecyclerView.LayoutManager layoutManager;
    RecyclerViewDatosDetaCancelacion adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cancelacion);





       productos = new ArrayList<detallesCancelacionDatos>();

        adaptador = new RecyclerViewDatosDetaCancelacion((ArrayList<detallesCancelacionDatos>)  productos);


        aceptarCancelacion2 = (Button)findViewById(R.id.btnConfirmar);
        regresar = (Button)findViewById(R.id.btnCancelar4);
        recyclerView =  findViewById(R.id.recyclerViewDetallesC);
        recyclerView.setHasFixedSize(true);
        folioV=  findViewById(R.id.tVFolio);
        fecha=  findViewById(R.id.fecha);
        horaDCq=  findViewById(R.id.hora);
        cajero=  findViewById(R.id.cajero);
        subtotal=  findViewById(R.id.subtotal2);
        iva=  findViewById(R.id.iva2);
        total=  findViewById(R.id.totall);
        aceptarCancelacion=  findViewById(R.id.btnConfirmar2);
        editTextEmail = findViewById(R.id.usuario);
        editTextPassword = findViewById(R.id.cotraseña);
        progressDialog = new ProgressDialog(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        conn=new ConexionSQLiteHelper(getApplicationContext());
        final String fechav = getIntent().getStringExtra("fechav");
        fecha.setText(fechav);
        final String hora = getIntent().getStringExtra("hora");
        horaDCq.setText(hora);
        final String folioVenta = getIntent().getStringExtra("folioVenta");
        folioV.setText(folioVenta);
        cargarDatos();
        cargarCajero();
        cargarTotalIvaSubtotal();
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.Cancelacion.class));
            }
        });
        aceptarCancelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCancelacion();
            }
        });

    }

    private void cargarDatos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        //Globales.getInstance().listaDetallesCancelacion.clear();
        productos.clear();
        String idDoc = getIntent().getStringExtra("idDC");
        //Cursor cursor2 =db.rawQuery("SELECT prd_nombre,prd_imagen, documento_det.prepro_fk , docd_cantprod, docd_precven,docd_preccom from documento_det INNER JOIN producto ON  producto.prd_id= documento_det.prepro_fk  WHERE doc_fk='"+idDoc+"'", null);
        Cursor cursor2 =db.rawQuery(" SELECT prd_nombre,prd_imagen, documento_det.prepro_fk , docd_cantprod, docd_precven,docd_preccom" +
                " from documento_det  " +
                "INNER JOIN prest_prod ON  prest_prod.prepro_id= documento_det.prepro_fk " +
                "INNER JOIN producto ON  producto.prd_id= prest_prod.prd_fk " +
                " WHERE doc_fk='"+idDoc+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    Bitmap bitmap = null;
                    String prd_nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                    String prepro_precompra= String.valueOf( cursor2.getString(cursor2.getColumnIndex("docd_preccom")));
                    String docd_cantprod= String.valueOf( cursor2.getString(cursor2.getColumnIndex("docd_cantprod")));
                    String docd_precven= String.valueOf( cursor2.getString(cursor2.getColumnIndex("docd_precven")));
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);



                  //  Globales.getInstance().listaDetallesCancelacion.add(new detallesCancelacionDatos(prd_nombre,prepro_precompra,docd_cantprod, docd_precven,bitmap));
                    detallesCancelacionDatos productoList = new detallesCancelacionDatos(prd_nombre,prepro_precompra,docd_cantprod, docd_precven,bitmap);
                    productos.add(productoList);
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {


                  //  detallesCancelacionDatos productoList = new detallesCancelacionDatos(bitmap,producto,cantidadProduct,precioProducto,subTotProduct);



                    //final RecyclerViewDatosDetaCancelacion adaptador = new RecyclerViewDatosDetaCancelacion((ArrayList<detallesCancelacionDatos>)  Globales.getInstance().listaDetallesCancelacion);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  cargarCajero(){

        final String folioVenta = getIntent().getStringExtra("folioVenta");//cajero q hizo la venta

        SQLiteDatabase db = conn.getReadableDatabase();
       // String cajaCancelacion= Globales.getInstance().idCajaCancelacion;
       // String usuarioCancelacion= Globales.getInstance().idUsuarioCancelacion;
        String nombreCajero="";

        //Cursor cursor2 =db.rawQuery("SELECT prs_nombre from  caja_usuario INNER JOIN usuario ON usuario.usr_id=caja_usuario.usr_fk INNER JOIN persona ON  persona.prs_id=usuario.prs_fk  INNER JOIN documento ON  documento.usr_fk=caja_usuario.usr_fk WHERE doc_folio='"+folioVenta+"'", null);
        //cambio con la nueva configuracion
        Cursor cursor2 =db.rawQuery("SELECT  cfg_nomPrs from configuracion INNER JOIN documento ON  configuracion.cfg_idUsr= documento.usr_fk  WHERE doc_folio=='"+folioVenta+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    nombreCajero = String.valueOf(cursor2.getString(cursor2.getColumnIndex("cfg_nomPrs")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    cajero.setText(nombreCajero);
                } else {
                    cajero.setText("");
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  cargarTotalIvaSubtotal(){
        SQLiteDatabase db = conn.getReadableDatabase();
        String idDoc = getIntent().getStringExtra("idDC");
        String sub="";
        String iva2="";
        String total2="";
        Cursor cursor2 =db.rawQuery("SELECT doc_subtotal, doc_iva, doc_total FROM documento WHERE documento.doc_id='"+idDoc+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    sub = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_subtotal")));
                    iva2 = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_iva")));
                    total2 = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    subtotal.setText(sub);
                    iva.setText(iva2);
                    total.setText(total2);


                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().subTCancel=sub;
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ivaCancel=iva2;
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalCancel=total2;



                } else {
                    subtotal.setText("");
                    iva.setText("");
                    total.setText("");

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().subTCancel="";
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ivaCancel="";
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalCancel="";
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void AlertCancela2() {
        LayoutInflater aceptarCancelacion = LayoutInflater.from(context);
        View prompstsCancelacion = aceptarCancelacion.inflate(R.layout.dialog_cancelacion, null);
        final AlertDialog.Builder builderCancel = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
        builderCancel.setView(prompstsCancelacion);
        builderCancel.setCancelable(false);

       // final  Button aceptarCancel = (Button)prompstsCancelacion.findViewById(R.id.btnCancel);
      //  final ImageButton cerrarv = prompstsCancelacion.findViewById(R.id.cerrarCanc);

        final TextView msjc =prompstsCancelacion.findViewById(R.id.mensajeCanelacion);
        msjc.setText("");
        builderCancel.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (msjc.equals("") || msjc.length() == 0) {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this, " Es obligatorio ingresar los motivos para cancelar.", Toast.LENGTH_LONG).show();
                    AlertCancela2();
                }
                else {
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cancelDLV=folioV.getText().toString();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeEnT=msjc.getText().toString();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajeroEnT=cajero.getText().toString();



                msjc.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msjc.getWindowToken(), 0);



                LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
                final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
                alerta.setMessage("                   ¿Esta seguro  de cancelar la venta?")
                        .setCancelable(true)
                        .setCustomTitle(vista)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sq.existeUnDocCancelacion(context);
                                String fol_foli = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().folioCancel;
                                sq.consultartpdCancelaciones(context);
                                String tpd_fk = String.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idTpdCancela);
                                String doc_id = "";
                                String doc_fecha = sq.fecha();
                                String doc_hora = sq.hora();
                                String doc_iva = "";
                                String doc_subtotal = "";
                                String doc_total = "";
                                String doc_descuento = "";
                                String doc_cosind = "";
                                String doc_observ = msjc.getText().toString();
                                String doc_saldo = "";
                                sq.consultarIDVentaPublico(getApplicationContext());//duda
                                String prs_fk = String.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaalPublico);
                                String usr_fk = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idUsuarioCancelacion;
                                String cja_fk = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaCancelacion;
                                sq.consultaestatusCancelado(getApplicationContext());
                                String esta_fk = String.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusCancelado);
                                String est_fk = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoCancelacion;
                                ConexionSQLiteHelper conn;
                                conn = new ConexionSQLiteHelper(context);
                                SQLiteDatabase db = conn.getReadableDatabase();
                                final String folioVenta = getIntent().getStringExtra("folioVenta");
                                Cursor cursor2 = db.rawQuery("SELECT doc_id,doc_iva,doc_subtotal,doc_total,doc_descuento, doc_cosind,doc_saldo,usr_fk, cja_fk,est_fk,esta_fk from documento WHERE doc_folio='" + folioVenta + "'", null);
                                try {
                                    if (cursor2 != null) {
                                        cursor2.moveToFirst();
                                        int index = 0;
                                        while (!cursor2.isAfterLast()) {
                                            doc_id = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_id")));
                                            doc_iva = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_iva")));
                                            doc_subtotal = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_subtotal")));
                                            doc_total = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_total")));
                                            doc_descuento = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_descuento")));
                                            doc_cosind = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_cosind")));
                                            doc_saldo = String.valueOf(cursor2.getString(cursor2.getColumnIndex("doc_saldo")));
                                            //usr_fk=String.valueOf( cursor2.getString(cursor2.getColumnIndex("usr_fk")));
                                            //cja_fk = String.valueOf(cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                                           // est_fk = String.valueOf(cursor2.getString(cursor2.getColumnIndex("est_fk")));
                                            index++;
                                            cursor2.moveToNext();
                                        }
                                        if (index != 0) {
                                            System.out.println(" Datos encontrados ");
                                        } else {
                                            System.out.println(" No encontro un folio ");
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ERROR, "Null16 ", e.getMessage());
                                }
                                sq.registrarCancelacion(doc_fecha, doc_hora, doc_iva, doc_subtotal, doc_total, doc_descuento, doc_cosind, doc_observ, doc_saldo, prs_fk, usr_fk, est_fk, esta_fk, fol_foli, tpd_fk, cja_fk, getApplicationContext());

                                ////////////////////////////
                                String docd_cantprod = "";
                                String docd_precven = "";
                                String docd_preccom = "";
                                String docd_descuento = "";
                                String prepro_fk = "";
                                String ext_fk = "";

                                sq.ultimoidocCancelacion(context,fol_foli );
                                String ultimoId0= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idUltimoDocDCancelacion;
                                String doc_fk = ultimoId0;
                                String imp_fk = "";


                                SQLiteDatabase db2 = conn.getReadableDatabase();
                                Cursor cursDD = db2.rawQuery("SELECT  docd_cantprod, docd_precven, docd_preccom, docd_descuento, prepro_fk, ext_fk, imp_fk FROM documento_det WHERE doc_fk='" + doc_id + "'", null);
                                try {
                                    if (cursDD != null) {
                                        cursDD.moveToFirst();
                                        int index = 0;
                                        while (!cursDD.isAfterLast()) {
                                            docd_cantprod = String.valueOf(cursDD.getString(cursDD.getColumnIndex("docd_cantprod")));
                                            docd_precven = String.valueOf(cursDD.getString(cursDD.getColumnIndex("docd_precven")));
                                            docd_preccom = String.valueOf(cursDD.getString(cursDD.getColumnIndex("docd_preccom")));
                                            docd_descuento = String.valueOf(cursDD.getString(cursDD.getColumnIndex("docd_descuento")));
                                            prepro_fk = String.valueOf(cursDD.getString(cursDD.getColumnIndex("prepro_fk")));
                                            ext_fk = String.valueOf(cursDD.getString(cursDD.getColumnIndex("ext_fk")));
                                            imp_fk = String.valueOf(cursDD.getString(cursDD.getColumnIndex("imp_fk")));


                                            sq.registrarDocumentoDetaCancelacion(docd_cantprod, docd_precven, docd_preccom, docd_descuento, prepro_fk, ext_fk, doc_fk, imp_fk, getApplicationContext());//doc_fk debe ser el q se acaba de registrar anteriormenete

                                            index++;
                                            cursDD.moveToNext();
                                        }
                                        if (index != 0) {
                                            System.out.println(" Datos encontrados ");
                                        } else {
                                            System.out.println(" No encontro un folio ");
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ERROR, "Null ", e.getMessage());
                                }




                                sq.cambiarEstatusAVentaCancelada(context,doc_id);

                                //Toast.makeText(DetallesCancelacion.this, " Registrado...", Toast.LENGTH_LONG).show();
                                LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
                                final View vista = imagenadvertencia_alert.inflate(R.layout.imagenexito, null);
                                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
                                alerta.setMessage("                                Venta cancelada")
                                        .setCustomTitle(vista)
                                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                dialog.cancel();
                                              // finish();///
                                                imprimirTicket taskimp;
                                                taskimp = new imprimirTicket();
                                                taskimp.execute((Void) null);
                                            }
                                        });
                                alerta.show();
                            }
                        });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.Cancelacion.class);
                        startActivity(intencion2);
                    }
                });
                alerta.show();
            }
            }
        });
        builderCancel.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msjc.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msjc.getWindowToken(), 0);
                dialog.cancel();
            }
        });
        AlertDialog alert = builderCancel.create();
        alert.show();
    }

    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.Cancelacion.class);
        startActivity(intencion2);
        finish();
    }
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sq.limpiarVariablesGlobales();
                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
                        }
                    });
            alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alerta.show();
        }
        if (id==R.id.opcionHome) {
            finish();
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
            startActivity(intencion2);
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow, menu);
        Ingresarsql sq = new Ingresarsql();
       // sq.consultarNombreUsuarioEnCancelacion(getApplicationContext());
        sq.consultarNombreUsuario(getApplicationContext());
        //menu.getItem(0).setTitle(Globales.getInstance().nombreUsuarioCancelacion);
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);

        return true;
    }




    //METODO PARA LA IMPRESION
    public class
    imprimirTicket extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;
        imprimirTicket() {
            mContext = com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this;
            impresora = new PrinterConnect(mContext);
        }
        public  void  impresionOriginal(){
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                for (detallesCancelacionDatos temp : productos) {
                    cadenotaDatos = cadenotaDatos + "\n" +temp.productoDC+ "\n $ " + temp.precioDC +"  X " + temp.cantidadDC +" = $ " + temp.importeDC;
                }
                impresora.conecta();
                sq.consultarNombreEstaCancelaciones(getApplicationContext());
                cadenota =  "ORIGINAL\n"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreEstCancel+" \n" + "Venta:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cancelDLV+" \n" + "Folio de cancelacion:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().folioCancel+"\n" +
                        "Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().subTCancel+" \n" +"IVA 16%: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ivaCancel+" \n"+ "Total: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalCancel+" \n" + "Motivos:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeEnT+" \n" + "Cajero:\n"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajeroEnT+" \n"+"*** Venta cancelada ***";

                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }
        public  void  impresionCopia(){
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                for (detallesCancelacionDatos temp : productos) {
                    cadenotaDatos = cadenotaDatos + "\n" +temp.productoDC+ "\n $ " + temp.precioDC +"  X " + temp.cantidadDC +" = $ " + temp.importeDC;
                }
                impresora.conecta();
                sq.consultarNombreEstaCancelaciones(getApplicationContext());
                cadenota =  "COPIA \n"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreEstCancel+" \n" + "Venta:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cancelDLV+" \n" + "Folio de cancelacion:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().folioCancel+"\n" +
                        "Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().subTCancel+" \n" +"IVA 16%: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ivaCancel+" \n"+ "Total: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalCancel+" \n" + "Motivos:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeEnT+" \n" + "Cajero:\n"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajeroEnT+" \n"+"*** Venta cancelada ***";

                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Imprimiendo");
            pd.setMessage("Espere mientras se imprime ");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
            pd.cancel();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            impresionOriginal();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    impresionCopia();
                }
            }, 2400); //espera 2 segundos para imprimir el otro ticket el  del cliente
            //impresionCopia();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.dialog_imprimir, null);
            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this, com.lzacatzontetlh.koonolmodulos.Cancelacion.class));
                    finish();
                }
            });

            builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // SI EL TICKET NO SE IMPRIMIÓ VUELVE A LLAMAR EL MÉTODO PARA VOLVER A IMPRIMIR
                    imprimirTicket taskimp;
                    taskimp = new imprimirTicket();
                    taskimp.execute((Void) null);
                    pd.dismiss();
                }
            });
            android.support.v7.app.AlertDialog alert = builder2.create();
            alert.show();










        }

    }

    private void AlertCancelacion() {
        LayoutInflater autoriza = LayoutInflater.from(context);
        View prompstsAutoriza = autoriza.inflate(R.layout.autoriza, null);
        final AlertDialog.Builder builderAutoriza = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this);
        builderAutoriza.setView(prompstsAutoriza);
        builderAutoriza.setCancelable(false);
        final EditText usua =  prompstsAutoriza.findViewById(R.id.usuario);
        final EditText pasw =  prompstsAutoriza.findViewById(R.id.cotraseña);


        final Button aceptar4 = (Button)prompstsAutoriza.findViewById(R.id.btnAceptar4);
        Button cancela4 = (Button)prompstsAutoriza.findViewById(R.id.btnCancela4);

        final AlertDialog alertAutoriza = builderAutoriza.create();

        aceptar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = usua.getText().toString().trim();
                password = pasw.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this, "Porfavor introduzca su correo", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this, "Porfavor introduzca su contraseña", Toast.LENGTH_LONG).show();
                    return;
                }else{


                    usua.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(usua.getWindowToken(), 0);

                    pasw.requestFocus();
                    InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm2.hideSoftInputFromWindow(pasw.getWindowToken(), 0);

                    int var;
                    var= sq.loginEnCancelacion(getApplicationContext(), email, password);
                    if(var==1){
                        String usu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idUsuarioCancelacion;
                        sq.consultaCaja(getApplicationContext(), usu);
                        AlertCancela2();
                    }
                    else {

                        sq.mensajeParaCajero(context);

                      //  Toast.makeText(DetallesCancelacion.this, "Lo sentimos, usted no tiene acceso", Toast.LENGTH_SHORT).show();
                    }

                    //Validación para iniciar sesión siempre y cuando tenga conexión a internet
                   /* ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        progressDialog.setMessage("Verificando datos ...");
                        progressDialog.show();

                        getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php");

                    } else {
                        // No hay conexión a Internet en este momento
                        Toast.makeText(DetallesCancelacion.this, "No hay conexión a Internet, se podrá iniciar sesión cuando te conectes a una red", Toast.LENGTH_LONG).show();
                        usua.setText("");
                        pasw.setText("");
                    }*/
                }



                //finish();
                alertAutoriza.cancel();
            }
        });
        cancela4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usua.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(usua.getWindowToken(), 0);

                pasw.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(pasw.getWindowToken(), 0);

                alertAutoriza.cancel();
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.DetallesCancelacion.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));


            }
        });

        alertAutoriza.show();
    }
/*
    //IMPLEMENTACION DEL WEB SERVICE
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<String ,String,String> {



            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_usuario", email);
                    postDataParams.put("usr_password", password);

                    return MainActivity.RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php",postDataParams);
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
                //Toast.makeText(getApplicationContext(), "juhbhjbhjbhj"+s, Toast.LENGTH_SHORT).show();


                try {
                    loadIntoListView(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("NO SE ENCONTRARON COINCIDENCIAS")){
                    progressDialog.dismiss();
                    Toast.makeText(DetallesCancelacion.this, "Los datos del usuario ingresado no se encuentran registrados", Toast.LENGTH_SHORT).show();
                    //  editTextEmail.setText("");
                    //  editTextPassword.setText("");

                }else if (usrStatus.equals("Habilitado")){
                    progressDialog.dismiss();
                    try {
                        loadIntoListView2(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Globales.getInstance().idUsuarioCancelacion= usrid;


                    String usu=   usrid;
                    sq.consultaCaja(getApplicationContext(), usu);


                  AlertCancela2();

                }else if (usrStatus.equals("Nuevo")){
                    progressDialog.dismiss();
                    usrID = usrid;
                    generadorCodigos();
                    finish();
                    startActivity(new Intent(getApplicationContext(),CodigoVerificacion.class));
                }
                else if (usrStatus.equals("Deshabilitado")){
                    progressDialog.dismiss();
                    Toast.makeText(DetallesCancelacion.this, "Lo sentimos, usted no tiene acceso", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }



            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }*/

/*
    //HABILITADO
    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            usrid = obj.getString("usr_id");
            usrStatus = obj.getString("esta_estatus");
        }
    }*/

/*
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
          //  usrid = obj.getString("usr_id");
            usrStatus = obj.getString("esta_estatus");

        }
    }*/
/*
    private void generadorCodigos() {
        for (int i = 1; i <= 6; i++) {
            numeero = (int) (Math.random() * 9 + 1);
            if (codigoList.contains(numeero)) {
                i--;
            } else {
                codigoList.add(numeero);
            }
        }

        str = TextUtils.join("", codigoList);
        Toast.makeText(DetallesCancelacion.this, "El codigo es: "+str, Toast.LENGTH_LONG).show();
    }*/



}
// Autor: Laura Zacatzontetl Hernandez

