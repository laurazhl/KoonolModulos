package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.commons.PrinterConnect;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.ConsultaSql;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.Productoss;
import com.lzacatzontetlh.koonolmodulos.modelo.VentaDetAdapter;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class venta_anterior_reimpirme extends AppCompatActivity {
    Button pagar, cancelar;
    TextView ventaFolio, fech, hr, cajeroNom, totalVnt, subVnta;
    final Context context = this;
    ConexionSQLiteHelper conn;
    String producto="", horaVenta, fchVenta, variableMet;
    double precioProducto=0.0, subTotProduct=0.0, totalVenta=0.0;
    int cantidadProduct=0, iDMetodo=0, idDoc=0;
    RecyclerView recyclerViewDatos;
    ArrayList<Productoss> productosVenta;
    VentaDetAdapter adaptaVntsLista;
    private ProgressDialog progressDialog;
    String email, password,usrid, usrStatus;
    final Ingresarsql sq = new Ingresarsql();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_anterior_reimpirme);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        conn=new ConexionSQLiteHelper(getApplicationContext());
        productosVenta = new ArrayList<Productoss>();
        progressDialog = new ProgressDialog(this);

        pagar = (Button)findViewById(R.id.btnImprime2);
        cancelar = (Button)findViewById(R.id.btnCancela);
        ventaFolio = (TextView)findViewById(R.id.text);
        fech = (TextView)findViewById(R.id.Fecha);
        hr = (TextView)findViewById(R.id.Hora);
        recyclerViewDatos = (RecyclerView)findViewById(R.id.vistalista2);
        cajeroNom = (TextView)findViewById(R.id.Cajero);
        totalVnt = (TextView)findViewById(R.id.Total);
        subVnta = (TextView)findViewById(R.id.SubT);

        ventaFolio.setText("Venta "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia);
        cajeroNom.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero2);


        adaptaVntsLista = new VentaDetAdapter(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, productosVenta);

        if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().statusPago.equals("Pagado")){
                pagar.setEnabled(false);
                pagar.setBackgroundColor(0xFFe6e6e6);

        }
        consultaVentaFolio();
        recyclerViewDatos.setLayoutManager(new LinearLayoutManager(this));


        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater ticket  = LayoutInflater.from(context);
                View prompstsView2 = ticket.inflate(R.layout.dialog_formapago, null);
                final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                builder2.setView(prompstsView2);
                builder2.setCancelable(false);

                Button aceptar = (Button)prompstsView2.findViewById(R.id.btnConfirmar);
                final RadioButton radioButton = (RadioButton) prompstsView2.findViewById(R.id.contado);
                final RadioButton radioButton2 = (RadioButton) prompstsView2.findViewById(R.id.credito);


                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //METODO DE PAGO EN EFECTIVO
                        if(radioButton.isChecked()){
                            variableMet = "Efectivo";
                            LayoutInflater pago  = LayoutInflater.from(context);
                            View datos =  pago.inflate(R.layout.dialog_pagoefectivo, null);
                            final android.support.v7.app.AlertDialog.Builder builderDatos = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                            builderDatos.setView(datos);
                            builderDatos.setCancelable(false);

                            final TextView ventaTotal = (TextView) datos.findViewById(R.id.textView2);
                            final EditText cantiTotal = (EditText) datos.findViewById(R.id.Total);
                            final Button aceptar = (Button) datos.findViewById(R.id.btnConfirmar);
                            ventaTotal.setText(String.format("%.2f", totalVenta));

                            aceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dinero = cantiTotal.getText().toString();
                                    if (TextUtils.isEmpty(dinero)) {
                                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "Por favor, introduzca la cantidad a pagar", Toast.LENGTH_LONG).show();

                                    }else if (!dinero.equals(String.format("%.2f", totalVenta))){
                                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "No coinciden las cantidades, favor de verificar", Toast.LENGTH_LONG).show();
                                        cantiTotal.setText("");
                                    }else {
                                    //CONVERSIONES
                                    double cantiTotl= Double.valueOf(dinero);
                                    int idUsr = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);

                                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cantidadPago = cantiTotl;

                                    //INSERCIÓN EN LA TABLA PAGO
                                    ConsultaSql insertarPago = new ConsultaSql();
                                    insertarPago.registrarPago(cantiTotl,fecha(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().metodoFK, idUsr, "", getApplicationContext());

                                    //IMPRESION DE TICKET
                                    Reimprimiendo taskimp;
                                    taskimp = new Reimprimiendo();
                                    taskimp.execute((Void) null);
                                    }
                                }
                            });

                            android.support.v7.app.AlertDialog alertPago = builderDatos.create();
                            alertPago.show();

                            //METODO PAGO EN TARJETA
                        }else if(radioButton2.isChecked()){
                            variableMet = "Tarjeta";
                            LayoutInflater pago2  = LayoutInflater.from(context);
                            View datos2 =  pago2.inflate(R.layout.dialog_ventanapago, null);
                            final android.support.v7.app.AlertDialog.Builder builderDatos = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                            builderDatos.setView(datos2);
                            builderDatos.setCancelable(false);

                            final TextView ventaTotal = (TextView) datos2.findViewById(R.id.textView2);
                            final EditText numAutoriza = (EditText) datos2.findViewById(R.id.autoriza);
                            final EditText cantiTotal2 = (EditText) datos2.findViewById(R.id.cantidad);
                            final Button confirmar = (Button) datos2.findViewById(R.id.btnConfirmar);
                            ventaTotal.setText(String.format("%.2f", totalVenta));

                            //CONFIRMAR LA VENTA
                            confirmar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dinero2 = cantiTotal2.getText().toString();
                                    String num = numAutoriza.getText().toString();
                                    if (TextUtils.isEmpty(dinero2)) {
                                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "Por favor, introduzca la cantidad a pagar", Toast.LENGTH_LONG).show();

                                    }else if (!dinero2.equals(String.format("%.2f", totalVenta))){
                                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "No coinciden las cantidades, favor de verificar", Toast.LENGTH_LONG).show();
                                        cantiTotal2.setText("");

                                    }else if (TextUtils.isEmpty(num)){
                                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "Por favor, introduzca el numero de autorización", Toast.LENGTH_LONG).show();

                                    }else {
                                        //CONVERSIONES
                                        double cantiTotl= Double.valueOf(dinero2);

                                        int idUsr = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);
                                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cantidadPago = cantiTotl;

                                        //INSERCIÓN EN LA TABLA PAGO
                                        ConsultaSql insertarPago = new ConsultaSql();
                                        insertarPago.registrarPago(cantiTotl,fecha(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().metodoFK, idUsr, num, getApplicationContext());

                                        //IMPRESION DE TICKET
                                        Reimprimiendo taskimp;
                                        taskimp = new Reimprimiendo();
                                        taskimp.execute((Void) null);
                                    }
                                }
                            });

                            android.support.v7.app.AlertDialog alertPago2 = builderDatos.create();
                            alertPago2.show();

                        }

                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idMetodo = variableMet;
                        consultaMetd();
                    }

                });

                android.support.v7.app.AlertDialog alert = builder2.create();
                alert.show();
            }
        });


        //BOTON CANCELAR
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                //startActivity(new Intent(venta_anterior_reimpirme.this, Cancelacion.class));
                AlertCancelacion();
            }
        });

    }


    //CONSULTA A BD PARA OBTENER LOS PRODUCTOS DEL X VENTA
    private void consultaVentaFolio() {
        productosVenta.clear();
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor2 =db.rawQuery("SELECT doc_fecha, doc_hora, doc_id, docd_cantprod, docd_precven, docd_preccom, doc_total, prd_nombre, prd_imagen, prepro_precompra, documento_det.prepro_fk"+
                " FROM estatus, documento, documento_det, producto, prest_prod"+
                " WHERE  documento.esta_fk=estatus.esta_id AND documento.doc_id = documento_det.doc_fk AND documento_det.prepro_fk = producto.prd_id AND prest_prod.prd_fk = producto.prd_id AND documento.doc_folio='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia+"' and (estatus.esta_estatus='Por pagar' OR estatus.esta_estatus='Pagado')", null);

        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;

                while (!cursor2.isAfterLast()) {
                    Bitmap bitmap = null;
                    idDoc = cursor2.getInt(cursor2.getColumnIndex("doc_id"));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDocm = idDoc;
                    producto = cursor2.getString(cursor2.getColumnIndex("prd_nombre"));
                    precioProducto = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("docd_preccom")));
                    cantidadProduct= cursor2.getInt(cursor2.getColumnIndex("docd_cantprod"));
                    subTotProduct = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("docd_precven")));
                    horaVenta= cursor2.getString(cursor2.getColumnIndex("doc_hora"));
                    fchVenta = cursor2.getString(cursor2.getColumnIndex("doc_fecha"));
                    totalVenta = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta = totalVenta;
                    //Se obtiene la imagen del producto seleccionado
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);

                    //var agregada para presentacion
                    String presentacion="";

                    //Asignación de la información
                    Productoss productoList = new Productoss(bitmap,producto,cantidadProduct,precioProducto,subTotProduct, presentacion);
                    productosVenta.add(productoList);


                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    recyclerViewDatos.setAdapter(adaptaVntsLista);
                    fech.setText(fchVenta);
                    hr.setText(horaVenta);
                    totalVnt.setText(String.format("%.2f", totalVenta));
                    subVnta.setText(String.format("%.2f", totalVenta));

                }

            }

        }catch(Exception e){
        }
    }



    //CONSULTA A BD PARA OBTENER ID METODO DE PAGO
    private void consultaMetd() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor3 =db.rawQuery("SELECT * FROM metodo_pago WHERE met_nombre='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idMetodo+"'", null);
        try {
            if (cursor3 != null) {
                cursor3.moveToFirst();
                int index3 = 0;

                while (!cursor3.isAfterLast()) {
                    iDMetodo = cursor3.getInt(cursor3.getColumnIndex("met_id"));
                    index3++;
                    cursor3.moveToNext();
                }

                if (index3 != 0) {
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().metodoFK = iDMetodo;

                }

            }

        }catch(Exception e){
        }
    }

    //OBTIENE FECHA ACTUAL
    public String fecha() {
        final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }


    //METODO PARA OBTENER HORA ACTUAL
    private String hora() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String datetime = dateFormat.format(c.getTime());
        return datetime;
    }


    //METODO PARA LA IMPRESION
    public class
    Reimprimiendo extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;

        Reimprimiendo() {
            mContext = com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this;
            impresora = new PrinterConnect(mContext);
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Imprimiendo");

            pd.setMessage("Aguarde mientras se imprime ");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String cadenota = "";
            String cadenotaDatos = "";

            try {
                for (Productoss temp : productosVenta) {
                    cadenotaDatos = cadenotaDatos + "\n" +temp.nombre + "\n $ " + temp.precio +"  X " + temp.cantidad +" = $ " + temp.subtotal;
                }

                impresora.conecta();

                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo2+" \n" + "Folio de la venta:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia+"\n" +
                        "Fecha:" + fecha() +"   Hora:"+hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta+" \n" +"IVA 16%: $0.00"+" \n"+ "Total: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta+" \n"+"*** Cuenta pagada ***";

                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();

            } catch (android.database.SQLException e) {

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.dialog_imprimir, null);
            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //consultaEstatus();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                }
            });

            builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // SI EL TICKET NO SE IMPRIMIÓ VUELVE A LLAMAR EL MÉTODO PARA VOLVER A IMPRIMIR
                    Reimprimiendo taskimp;
                    taskimp = new Reimprimiendo();
                    taskimp.execute((Void) null);
                    pd.dismiss();
                }
            });
            android.support.v7.app.AlertDialog alert = builder2.create();
            alert.show();
        }

    }


    private void AlertCancelacion(){
        LayoutInflater autoriza = LayoutInflater.from(context);
        View prompstsAutoriza = autoriza.inflate(R.layout.autoriza, null);
        final AlertDialog.Builder builderAutoriza = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
        builderAutoriza.setView(prompstsAutoriza);
        builderAutoriza.setCancelable(false);
        final EditText usua =  prompstsAutoriza.findViewById(R.id.usuario);
        final EditText pasw =  prompstsAutoriza.findViewById(R.id.cotraseña);
        Button aceptar = prompstsAutoriza.findViewById(R.id.btnAceptar4);
        Button cancelaa = prompstsAutoriza.findViewById(R.id.btnCancela4);

       //BOTON ACEPTAR EN LA PANTALLA AUTORIZACIÓN
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = usua.getText().toString().trim();
                password = pasw.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "Porfavor introduzca su correo", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, "Por favor introduzca su contraseña", Toast.LENGTH_LONG).show();
                    return;

                }else{
                    //Validación para iniciar sesión siempre y cuando tenga conexión a internet
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    /*if (networkInfo != null && networkInfo.isConnected()) {
                        progressDialog.setMessage("Iniciando sesión ...");
                        progressDialog.show();*/
                        AlertCancela2();
                        //getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php");

                    /*} else {
                        // No hay conexión a Internet en este momento
                        Toast.makeText(venta_anterior_reimpirme.this, "No hay conexión a Internet, se podrá iniciar sesión cuando te conectes a una red", Toast.LENGTH_LONG).show();
                        usua.setText("");
                        pasw.setText("");
                    }*/
                }
            }
        });


        //BOTON CANCELAR EN LA PANTALLA AUTORIZACION
        cancelaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usua.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(usua.getWindowToken(), 0);

                pasw.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(pasw.getWindowToken(), 0);
            }
        });



        /*builderAutoriza.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/

       /* builderAutoriza.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });*/
        AlertDialog alertAutoriza = builderAutoriza.create();
        alertAutoriza.show();
    }


    /// ******************** POSIBILIDAD DE QUE SE QUITE ESTO *********************///
    //IMPLEMENTACION DEL WEB SERVICE
    /*private void getJSON(final String urlWebService) {

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

                try {
                    loadIntoListView(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("NO SE ENCONTRARON COINCIDENCIAS")){
                    progressDialog.dismiss();
                    Toast.makeText(venta_anterior_reimpirme.this, "Los datos del usuario ingresado no se encuentran registrados", Toast.LENGTH_SHORT).show();

                }else if (usrStatus.equals("Habilitado")){
                    progressDialog.dismiss();
                    Globales.getInstance().idUsuarioCancelacion= usrid;

                    String usu=   usrid;
                    sq.consultaCaja(getApplicationContext(), usu);
                    AlertCancela2();
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            usrid = obj.getString("usr_id");
            usrStatus = obj.getString("esta_estatus");

        }
    }*/


    private void AlertCancela2() {
        LayoutInflater aceptarCancelacion = LayoutInflater.from(context);
        View prompstsCancelacion = aceptarCancelacion.inflate(R.layout.dialog_cancelacion, null);
        final AlertDialog.Builder builderCancel = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
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
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this, " Es obligatorio ingresar los motivos para cancelar.", Toast.LENGTH_LONG).show();
                    AlertCancela2();
                }
                else {
                    //Globales.getInstance().cancelDLV=folioV.getText().toString();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeEnT=msjc.getText().toString();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajeroEnT= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero;



                    msjc.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(msjc.getWindowToken(), 0);



                    LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                    final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
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
                                    sq.consultarIDVentaPublico(getApplicationContext());
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
                                    Cursor cursor2 = db.rawQuery("SELECT doc_id,doc_iva,doc_subtotal,doc_total,doc_descuento, doc_cosind,doc_saldo,usr_fk, cja_fk,est_fk,esta_fk from documento WHERE doc_folio='" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia + "'", null);
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
                                        Log.println(Log.ERROR, "Null16 ", e.getMessage());
                                    }




                                    sq.cambiarEstatusAVentaCancelada(context,doc_id);
                                    LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                                    final View vista = imagenadvertencia_alert.inflate(R.layout.imagenexito, null);
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
                                    alerta.setMessage("                                Venta cancelada")
                                            .setCustomTitle(vista)
                                            .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    dialog.cancel();
                                                    // finish();
                                                    //imprimirTicket taskimp;
                                                    //taskimp = new imprimirTicket();
                                                    //taskimp.execute((Void) null);
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



    //METODO PARA LA IMPRESION
    /*public class
    imprimirTicket extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;
        imprimirTicket() {
            mContext = venta_anterior_reimpirme.this;
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
                cadenota =  "ORIGINAL\n"+Globales.getInstance().nombreEstCancel+" \n" + "Venta:"+Globales.getInstance().cancelDLV+" \n" + "Folio de cancelacion:"+Globales.getInstance().folioCancel+"\n" +
                        "Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+Globales.getInstance().subTCancel+" \n" +"IVA 16%: $"+Globales.getInstance().ivaCancel+" \n"+ "Total: $"+Globales.getInstance().totalCancel+" \n" + "Motivos:"+Globales.getInstance().mensajeEnT+" \n" + "Cajero:\n"+Globales.getInstance().cajeroEnT+" \n"+"*** Venta cancelada ***";

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
                cadenota =  "COPIA \n"+Globales.getInstance().nombreEstCancel+" \n" + "Venta:"+Globales.getInstance().cancelDLV+" \n" + "Folio de cancelacion:"+Globales.getInstance().folioCancel+"\n" +
                        "Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+Globales.getInstance().subTCancel+" \n" +"IVA 16%: $"+Globales.getInstance().ivaCancel+" \n"+ "Total: $"+Globales.getInstance().totalCancel+" \n" + "Motivos:"+Globales.getInstance().mensajeEnT+" \n" + "Cajero:\n"+Globales.getInstance().cajeroEnT+" \n"+"*** Venta cancelada ***";

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
            }, 3000); //espera 4 segundos para imprimir el otro ticket el  del cliente
            //impresionCopia();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.dialog_imprimir, null);
            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(venta_anterior_reimpirme.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(venta_anterior_reimpirme.this, MenuGeneral.class));
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

    }*/



    //IMPLEMENTACION DEL MENÚ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoverflow, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id== R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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

        if (id== R.id.opcionHome) {

            finish();
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.VentasPrevias.class);
            startActivity(intencion2);
        }

        return super.onOptionsItemSelected(item);
    }

}


