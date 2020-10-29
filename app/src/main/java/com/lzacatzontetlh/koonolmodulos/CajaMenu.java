package com.lzacatzontetlh.koonolmodulos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.ConsultaSql;
import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapter2;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CajaMenu extends AppCompatActivity {
    private GridView gridView;
    TextView textoProducto;
    GridAdapter2 adapter;
    String selectItem;
    final Context context = this;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String cja, sucur, est_nomb, usu, ps, cjaId, tot, TpmId, TpmNom, CjusId;
    TextView CaJero;
    double precioProduct=0.0;
    int actuali, idProduct, IdCjus;
    private ProgressDialog progressDialog;
    public ConexionSQLiteHelper conn;
    String email, password, suma3, nombreLgn;
    EditText usua, pasw;
    double e = 0.0;
    double t = 0.0, suma, totalFinal=0.0,  ToTal2, diferenciaTotal=0, preproComp, listPrc;
    ConsultaSql consultaa;

    int clas_fk, esta_fk, emp_fk, prd_stockmax, prd_stockmin, prd_unimed, prd_id, totalimg=0,  totalimg2=0, clasCrts, idCrts, idCrtDet, cartsfk, crtsDet, prdFk, IdPrst, idPrepro, IdProdu, prstFk, idList, listAct,listSeg,
    listPrepro;
    String prd_codigo, prd_nombre, prd_descorta, prd_deslarga, prd_observ, clas_nombre, nomCrts, dscrpCarts, descriPrst, prepoCod;
    public int[] IdClass = new int[100];
    public String[] NomClass = new String[100];
    public int[] IdPrd = new int[100];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caja_menu);

        //PUNTO DE VENTA
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Apertura");
        arrayList.add("Retiro de dinero");
        arrayList.add("Corte X");
        arrayList.add("Cierre");

        int images[] ={R.drawable.apertura,  R.drawable.retirodinero, R.drawable.cortex, R.drawable.cierre};

        adapter = new GridAdapter(this,images, arrayList);*/

        //DON CHON
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Apertura");
        arrayList.add("Corte X");
        arrayList.add("Cierre");
        arrayList.add("Cobrar");

        int images[] ={R.drawable.apertura, R.drawable.cortex, R.drawable.cierre, R.drawable.cobrar};

        adapter = new GridAdapter2(this,images, arrayList);

        gridView = (GridView) findViewById(R.id.am_gv_gridview2);
        textoProducto = (TextView) findViewById(R.id.text);
        gridView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        conn=new ConexionSQLiteHelper(getApplicationContext());
        consultaa =  new ConsultaSql();

       getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Datos_apertura.php");



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nombreProducto = (TextView) view.findViewById(R.id.ig_tv_titulo);
                selectItem = nombreProducto.getText().toString();

                if (selectItem.equals("Apertura")){
                    getJSONVali("https://www.nextcom.com.mx/webserviceapp/koonol/ValidacionCaja.php");
                   //AlertApertura();

                }if (selectItem.equals("Corte X")) {
                    ConsultaSql clavex3 = new ConsultaSql();
                    clavex3.consultaClave(getApplicationContext());
                    AlertCorteX();


                }if (selectItem.equals("Cierre")){
                    ConsultaSql clavex4 = new ConsultaSql();
                    clavex4.consultaClave(getApplicationContext());
                    getJSONValiCierre("https://www.nextcom.com.mx/webserviceapp/koonol/ValidacionCierre.php");
                    /*ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {*/
                       // AlertAutoriza();
                        //getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php");

                    /*} else {
                        // No hay conexión a Internet en este momento
                        Toast.makeText(CajaMenu.this, "No hay conexión a Internet, se podrá iniciar sesión cuando te conectes a una red", Toast.LENGTH_LONG).show();
                    }*/

                }if (selectItem.equals("Cobrar")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, VentasPrevias.class));
                }

            }
        });

    }

    //METODO PARA OBTENER FECHA ACTUAL
    private String fecha() {
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


    //ALERT APERTURA DE CAJA
    private void AlertApertura() {
        LayoutInflater li2 = LayoutInflater.from(context);
        View prompstsView2 = li2.inflate(R.layout.abrircaja, null);
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        builder2.setView(prompstsView2);
        builder2.setCancelable(false);

        TextView CaJA = (TextView)prompstsView2.findViewById(R.id.textView3);
        TextView SuCursal = (TextView)prompstsView2.findViewById(R.id.Sucursal2);
        CaJero = (TextView)prompstsView2.findViewById(R.id.Cajero2);
        TextView hr = (TextView)prompstsView2.findViewById(R.id.editHora);
        TextView fch = (TextView)prompstsView2.findViewById(R.id.editFecha);
        final EditText monto = (EditText)prompstsView2.findViewById(R.id.Monto2);

        CaJA.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa);
        SuCursal.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().estNom);
        CaJero.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero);
        hr.setText(hora());
        fch.setText(fecha());

        builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tot = monto.getText().toString().trim();
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mont = tot;
                if(TextUtils.isEmpty(tot)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Por favor, inserte el monto", Toast.LENGTH_LONG).show();
                    return;
                } else{
                    /*progressDialog.setMessage("Iniciando sesión ...");
                    progressDialog.show();*/
                    ConsultaSql clavex2 = new ConsultaSql();
                    clavex2.consultaClave(getApplicationContext());

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaOperacion = "Apertura";
                    RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_movCaja.php");

                }

            }
        });

        builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder2.create();
        alert.show();
    }



    //*** CIERRE DE CAJA ****//
    //ALERT PARA AUTORIZACION DEL ADMINISTRADOR
    private void AlertAutoriza() {
        LayoutInflater autoriza = LayoutInflater.from(context);
        View prompstsAutoriza = autoriza.inflate(R.layout.autoriza, null);
        final AlertDialog.Builder builderAutoriza = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        builderAutoriza.setView(prompstsAutoriza);
        builderAutoriza.setCancelable(false);
        usua = (EditText) prompstsAutoriza.findViewById(R.id.usuario);
        pasw = (EditText) prompstsAutoriza.findViewById(R.id.cotraseña);
        Button aceptar4 = (Button)prompstsAutoriza.findViewById(R.id.btnAceptar4);
        Button cancela4 = (Button)prompstsAutoriza.findViewById(R.id.btnCancela4);

        //ACCIÓN AL PRESIONAR EL BOTÓN ACEPTAR
        aceptar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = usua.getText().toString().trim();
                password = pasw.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Por favor, introduzca su correo", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Por favor, introduzca su contraseña", Toast.LENGTH_LONG).show();
                    return;
                }else{

                    //Validación para iniciar sesión siempre y cuando tenga conexión a internet
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        progressDialog.setMessage("Verificando información ...");
                        progressDialog.show();

                        getJSONUSUARIOS("https://www.nextcom.com.mx/webserviceapp/koonol/autoriza.php");

                    } else {
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No hay conexión a Internet, se podrá iniciar sesión cuando te conectes a una red", Toast.LENGTH_LONG).show();
                        usua.setText("");
                        pasw.setText("");
                    }
                }
            }
        });

        //ACCION AL PRESIONAR EL BOTÓN CANCELAR
        cancela4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.CajaMenu.class));

            }
        });

        AlertDialog alertAutoriza = builderAutoriza.create();
        alertAutoriza.show();
    }

    //ALERT CIERRE
    private void AlertCierre() {
        //CONSULTA EL MONTO DE LA APERTURA DE CAJA
        ConsultaSql aperturaCaja = new ConsultaSql();
        aperturaCaja.consultaMontoApert(getApplicationContext());

        LayoutInflater cierre = LayoutInflater.from(context);
        View prompstsCierre = cierre.inflate(R.layout.cierrecaja, null);
        final AlertDialog.Builder builderCierre = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        builderCierre.setView(prompstsCierre);
        builderCierre.setCancelable(false);

        TextView CaJA = (TextView)prompstsCierre.findViewById(R.id.textView3);
        TextView SuCursal = (TextView)prompstsCierre.findViewById(R.id.Sucursal2);
        TextView CaJero = (TextView)prompstsCierre.findViewById(R.id.Cajero2);
        TextView hrCierre = (TextView) prompstsCierre.findViewById(R.id.editHora);
        TextView fchCierre = (TextView) prompstsCierre.findViewById(R.id.editFecha);
        TextView cantiApertura = (TextView) prompstsCierre.findViewById(R.id.Monto2);
        Button aceptar = (Button)prompstsCierre.findViewById(R.id.btnAceptar2);
        Button cancela = (Button)prompstsCierre.findViewById(R.id.btnCancela2);

        final TextView totalxDia = (TextView) prompstsCierre.findViewById(R.id.total2);
        final EditText efectivo = (EditText) prompstsCierre.findViewById(R.id.efectivo2);
        final EditText tarjetas = (EditText) prompstsCierre.findViewById(R.id.transferencia2);

        CaJA.setText(cja);
        SuCursal.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().estNom);
        CaJero.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreCjus);
        cantiApertura.setText(String.format("%.2f", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura));

        hrCierre.setText(hora());
        fchCierre.setText(fecha());


        efectivo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (efectivo.getText().toString().equals("")) {
                    e = 0.0;

                }else {
                String efect = efectivo.getText().toString();
                e  = Double.valueOf(efect);

                suma = e  + t + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura;
                suma3 = String.format("%.2f", suma);
                totalxDia.setText(suma3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        tarjetas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tarjetas.getText().toString().equals("")) {
                    t = 0.0;

                }else {
                    final String tarjetaa = tarjetas.getText().toString();
                    t  = Double.valueOf(tarjetaa);

                    suma = e  + t + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura;
                    String suma3 = String.format("%.2f", suma);
                    totalxDia.setText(suma3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ACCION AL PRESIONAR EL BOTÓN ACEPTAR
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String efect = efectivo.getText().toString();
                String tarjetaa = tarjetas.getText().toString();

                if (TextUtils.isEmpty(efect)) {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Por favor, introduzca el monto en efectivo", Toast.LENGTH_LONG).show();

                }else if (TextUtils.isEmpty(tarjetaa)) {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Por favor, introduzca el monto en tarjetas", Toast.LENGTH_LONG).show();

                }else{

                   // ToTal2= Globales.getInstance().totalEfectivo2+Globales.getInstance().totalTarjeta2+Globales.getInstance().montoApertura;
                    ToTal2= t+e+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura;

                    //CONSULTA LOS MONTOS EN EFECTIVO Y TARJETA
                    ConsultaSql totales = new ConsultaSql();
                    totales.totales(getApplication());
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalEfectivo2+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalTarjeta2+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura;

                    if (ToTal2 == com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl){
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeCorte = "***Todo correcto***";
                    }//else if (ToTal2 != Globales.getInstance().montoTotl){
                        else if (ToTal2 < com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl){
                            diferenciaTotal = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl-ToTal2;
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direfe =  diferenciaTotal;
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeCorte = "***Diferencia por $";
                        }else if(ToTal2 > com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl){
                            diferenciaTotal = ToTal2- com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl;
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direfe =  diferenciaTotal;
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeCorte = "***Diferencia por $";
                        }

                    //}


                    //IMPRIMIR TICKET DE COMPROBACIÓN
                    Reimprimiendo taskimp;
                    taskimp = new Reimprimiendo();
                    taskimp.execute((Void) null);
                }
            }
        });

        //ACCIÓN AL PRESIONAR EL BOTÓN CANCELAR
        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.CajaMenu.class));
            }
        });

        AlertDialog alertCierre = builderCierre.create();
        alertCierre.show();
    }


    //ALERT CORTE X
    private void AlertCorteX() {
        //CONSULTA EL MONTO DE LA APERTURA DE CAJA
        ConsultaSql aperturaCaja = new ConsultaSql();
        aperturaCaja.consultaMontoApert(getApplicationContext());

        LayoutInflater corteX = LayoutInflater.from(context);
        View prompstsCorteX = corteX.inflate(R.layout.cortex, null);
        final AlertDialog.Builder builderCorteX = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        builderCorteX.setView(prompstsCorteX);
        builderCorteX.setCancelable(false);

        TextView CaJA = (TextView)prompstsCorteX.findViewById(R.id.textView3);
        TextView fchCierre = (TextView) prompstsCorteX.findViewById(R.id.editFecha);
        TextView establecimiento = (TextView) prompstsCorteX.findViewById(R.id.Sucursal2);
        TextView apertura = (TextView) prompstsCorteX.findViewById(R.id.Monto2);
        TextView horaa = (TextView) prompstsCorteX.findViewById(R.id.editHora);
        TextView CaJeroX = (TextView)prompstsCorteX.findViewById(R.id.Cajero2);
        final TextView totalDia = (TextView)prompstsCorteX.findViewById(R.id.efectivo2);
        final TextView totalDia2 = (TextView)prompstsCorteX.findViewById(R.id.transferencia2);
        TextView totalCajaDia = (TextView)prompstsCorteX.findViewById(R.id.total2);
        Button aceptar2 = (Button)prompstsCorteX.findViewById(R.id.btnAcepta3);
        Button cancela2 = (Button)prompstsCorteX.findViewById(R.id.btnCancela3);

        CaJA.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa2);
        establecimiento.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo2);
        CaJeroX.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero2);
        apertura.setText(String.format("%.2f", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura));

        horaa.setText(hora());
        fchCierre.setText(fecha());

        ConsultaSql totales = new ConsultaSql();
        totales.totales(getApplicationContext());
        totalDia.setText(String.format("%.2f", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalEfectivo2));
        totalDia2.setText(String.format("%.2f", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalTarjeta2));
        totalFinal = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalEfectivo2+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalTarjeta2+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura;
        totalCajaDia.setText(String.format("%.2f", totalFinal));

        //ACCIONES EL BOTÓN aCPETAR
        aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.CajaMenu.class));
            }
        });

        //ACCIONES EL BOTÓN CANCELAR
        cancela2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.CajaMenu.class));
            }
        });

        AlertDialog  alertCorteX = builderCorteX.create();
        alertCorteX.show();
    }


    //PASO #1
    //IMPLEMENTACION DEL WEB SERVICE (VALIDACIONES SOBRE LA APERTURA DE CAJA)
    private void getJSONVali(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject Params2 = new JSONObject();
                    Params2.put("cjus_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId);
                    Params2.put("fechaa", String.valueOf(fechaSQL()));

                    return RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/ValidacionCaja.php",Params2);
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
                    loadValidacion(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("Ya se aperturo la caja anteriormente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se puede aperturar la caja debido a que ya se realizo este proceso..", Toast.LENGTH_LONG).show();

                }else if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmNom.equals("Apertura")){
                    //Toast.makeText(CajaMenu.this, "", Toast.LENGTH_LONG).show();
                    AlertApertura();

                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadValidacion(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            TpmId = obj.getString("tpm_id");
            TpmNom = obj.getString("tpm_nombre");

        }
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId = TpmId;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmNom = TpmNom;
    }


    //PASO #2
    //IMPLEMENTACION DEL WEB SERVICE (METODO PARA EXTRAER LOS DATOS DE LA CAJA Y USUARIO VINCULADOS)
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);

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
                try {
                    loadIntoListView(s);

                } catch (JSONException e) {
                    e.printStackTrace();
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
            cja = obj.getString("cja_nombre");
            usu = obj.getString("prs_nombre");
            cjaId = obj.getString("cja_id");
            est_nomb =  obj.getString("est_nombre");
            CjusId =  obj.getString("cjus_id");
        }
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().caja = cja;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero = usu;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().estNom = est_nomb;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId = cjaId;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId = CjusId;
        /*progressDialog.dismiss();
        AlertApertura();*/
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


    //METODO PARA INSERTAR DATOS EN MYSQL
    private  void RegistraMysql(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 if (response.equals("Se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Apertura de caja realizada!!!", Toast.LENGTH_SHORT).show();

                    getJSONSincro("https://www.nextcom.com.mx/webserviceapp/koonol/ValidacionSincro.php");
                    ConsultaSql insertarCaja = new ConsultaSql();

                    //int varId = Integer.valueOf(Globales.getInstance().cjusId);
                    int varTemp = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId);
                    insertarCaja.registrarApertura(fechaSQL(), Horaa(), varTemp, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveCU, getApplicationContext());

                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));

                }else if(response.equals("No se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();

                } else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
               parametros.put("fechaa", fechaSQL());
               parametros.put("hora", Horaa());
               parametros.put("caja", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId);
               parametros.put("tpm", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId);
               parametros.put("concepto", "Apertura");
               parametros.put("monto", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mont);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        requestQueue.add(stringRequest);
    }


    //Método para obtener la fecha actual
    private String fechaSQL() {
        final SimpleDateFormat fe2 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return fe2.format(calendar.getTime());
    }

    //Método para obtener la hora actual
    private String Horaa() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm");
        return formateador.format(ahora);
    }


    //IMPLEMENTACION DEL WEB SERVICE (VALIDACIONES DE LA SINCRONIZACION)
    private void getJSONSincro(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject Params2 = new JSONObject();
                    Params2.put("cjus_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId);
                    Params2.put("fechaa", String.valueOf(fechaSQL()));

                    return RequestHandler.sendPost(urlWebService,Params2);
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
                    loadSincro(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("No hay datos")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No hay datos para sincronizar", Toast.LENGTH_LONG).show();

                }else if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().actualiSin==0){
                    //Toast.makeText(CajaMenu.this, "No se tiene que actualizar datos", Toast.LENGTH_LONG).show();
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Datos registrados correctamente !!!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));

                }
                else if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().actualiSin==1){
                    //Toast.makeText(CajaMenu.this, "Se deben actualizar datos por favor", Toast.LENGTH_LONG).show();
                    //getJSONDatosxSincro("https://www.nextcom.com.mx/webserviceapp/koonol/DatosxSincro.php");
                    getJSONSincro2("https://www.nextcom.com.mx/webserviceapp/koonol/Sincro_apertura.php");
                    getJSONSincro3("https://www.nextcom.com.mx/webserviceapp/koonol/Caracteristicas_sincro.php");
                    getJSONSincro4("https://www.nextcom.com.mx/webserviceapp/koonol/Prst_sincro.php");

                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadSincro(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            actuali = obj.getInt("cja_actualizado");
        }
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().actualiSin = actuali;

    }



    // ****** S I N C R O N I Z A C I Ó N  A P E R T U R A *******////
    //1. DATOS DE CLASIFICACIÓN Y PRODUCTOS
    public void getJSONSincro2(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadDatosxSincro(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadDatosxSincro(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int k=0, p=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            prd_id = obj.getInt("prd_id");
            prd_codigo = obj.getString("prd_codigo");
            prd_nombre = obj.getString("prd_nombre");
            prd_descorta = obj.getString("prd_descorta");
            prd_deslarga = obj.getString("prd_deslarga");
            prd_stockmax = obj.getInt("prd_stockmax");
            prd_stockmin = obj.getInt("prd_stockmin");
            prd_unimed = obj.getInt("prd_unimed");
            prd_observ = obj.getString("prd_observ");
            clas_fk = obj.getInt("clas_fk");
            esta_fk = obj.getInt("esta_fk");
            emp_fk = obj.getInt("emp_fk");
            clas_nombre = obj.getString("clas_nombre");

            //CLASIFICACION
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().categoriaId = clas_fk;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().categoriaNom = clas_nombre;

            //DATOS DEL PRODUCTO
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdId = prd_id;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdCod = prd_codigo;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdNom = prd_nombre;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdDescorta = prd_descorta;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdDeslarga =  prd_deslarga;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().stockMax = prd_stockmax;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().stockMin = prd_stockmin;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().unimed = prd_unimed;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdObs = prd_observ;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdEsta = esta_fk;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdEmp = emp_fk;

            //BUSQUEDA DE LA CLASIFICACION
            consultaa.consultaClasificacion(getApplicationContext());
            //BUSQUEDA DEL PRODUCTO
            consultaa.consultaProducto(getApplicationContext());

            //ACTUALIZACIÓN DE LA IMAGEN SEGÚN SEA LA CLASIFICACION
            if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().YaInserto.equals("SI")){
                IdClass[k]=clas_fk;
                new LoadImage().execute("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_imagen.php?ID_PRODUCTO="+IdClass[k]);
                k++;
            }

            //ACTUALIZACIÓN DE LA IMAGEN SEGÚN SEA EL PRODUCTO
            if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().YaInsertoPrd.equals("SI")){
                IdPrd[p] = prd_id;
                new LoadImage2().execute("https://www.nextcom.com.mx/webserviceapp/koonol/Imagen_producto.php?ID_PRODUCTO="+IdPrd[p]);
                p++;
            }

        }



    }

    //EXTRAE LA IMAGEN DE LA CATEGORIA CORRESPONDIENTE Y ACTUALIZA
    class LoadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return downloadBitmap(params[0]);
            } catch (Exception e) {
                Log.e("LoadImage class", "doInBackground() " + e.getMessage());
            }
            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (bitmap != null) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                //bitmap.recycle();

                //if (Globales.getInstance().tipoImg.equals("Clasificacion")){
                    consultaa.UpdateImg(getApplicationContext(),byteArray, IdClass[totalimg]);
                    totalimg++;
                //}
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.e("LoadImage class", "Descargando imagen desde url: " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }


    //EXTRAER IMAGEN DEL PRODUCTO CORRESPONDIENTE Y ACTUALIZA
    class LoadImage2 extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return downloadBitmap2(params[0]);
            } catch (Exception e) {
                Log.e("LoadImage class", "doInBackground() " + e.getMessage());
            }
            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bitmap2) {
            if (isCancelled()) {
                bitmap2 = null;
            }

            if (bitmap2 != null) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray2 = stream.toByteArray();
                //bitmap.recycle();

                //if (Globales.getInstance().tipoImg.equals("Producto")){
                    //consultaa.productoo(IdPrd[totalimg2], CgPrd[totalimg2], NomPrd[totalimg2], DescortaPrd[totalimg2], DeslrgPrd[totalimg2], StockmaxPrd[totalimg2], StockminPrd[totalimg2], UnimedPrd[totalimg2], byteArray, ObsPrd[totalimg2], clas[totalimg2], estafk[totalimg2], empfk[totalimg2],getApplicationContext());
                    consultaa.UpdateImg2(getApplicationContext(), byteArray2, IdPrd[totalimg2]);
                    totalimg2++;
                //}
            }
        }


        private Bitmap downloadBitmap2(String url2) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri2 = new URL(url2);
                urlConnection = (HttpURLConnection) uri2.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream2 = urlConnection.getInputStream();
                if (inputStream2 != null) {
                    Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
                    return bitmap2;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.e("LoadImage class", "Descargando imagen desde url: " + url2);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }


    //2. DATOS DE LAS CARACTERISTICAS, CARACTERISTICA DETALLE, PRODUCTO CARACTERISTICA DETALLE
    public void getJSONSincro3(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadDatosCrts(s);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadDatosCrts(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            idCrts = obj.getInt("crts_id");
            nomCrts = obj.getString("crts_nombre");
            clasCrts = obj.getInt("clas_fk");
            idCrtDet = obj.getInt("crtd_id");
            dscrpCarts = obj.getString("ctrd_descripcion");;
            cartsfk = obj.getInt("crts_fk");
            crtsDet = obj.getInt("crtd_fk");
            prdFk = obj.getInt("prd_fk");

            //CARACTERISTICAS
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCarts = idCrts;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomCarts = nomCrts;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().clasCarts = clasCrts;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCartsD = idCrtDet;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().descCartsD= dscrpCarts;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cartsFk = cartsfk;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cartsDetFk= crtsDet;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prdFk=prdFk;

            //VALIDACION DE LAS CARACTERISTICAS
            consultaa.consultaCrts(getApplicationContext());
            consultaa.consultaCrtsDet(getApplicationContext());
            consultaa.consultaPrdCarDet(getApplicationContext());

        }

    }


    //3. DATOS DE PRESENTACION Y PRESENTACION PRODUCTOS
    public void getJSONSincro4(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadDatosPrst(s);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadDatosPrst(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            IdPrst = obj.getInt("prst_id");
            descriPrst = obj.getString("prst_descripcion");
            idPrepro = obj.getInt("prepro_id");
            prepoCod = obj.getString("prepro_codigo");
            preproComp = obj.getDouble("prepro_precompra");
            IdProdu = obj.getInt("prd_fk");
            prstFk = obj.getInt("prst_fk");
            idList = obj.getInt("lstp_id");
            listPrc = obj.getDouble("lstp_precio");
            listAct = obj.getInt("lstp_actualizado");
            listSeg = obj.getInt("seg_fk");
            listPrepro = obj.getInt("prepro_fk");

            //Presentación
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prstId = IdPrst;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prstDescr = descriPrst;

            //Presentación - Producto
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().preproId = idPrepro;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().preproCg = prepoCod;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().preproCom = preproComp;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().preproPrd = IdProdu;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().preproPrst = prstFk;

            //Lista precio
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listId = idList;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listPrc = listPrc;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listAct = listAct;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listSeg = listSeg;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listPrepro=listPrepro;

            //VALIDACION DE LAS CARACTERISTICAS
            consultaa.consultaPresentacion(getApplicationContext());
            consultaa.consultaPrestPrd(getApplicationContext());
            consultaa.consultaListPrd(getApplicationContext());
        }

    }

    /*private void getJSONDatosxSincro(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadDatosxSincro(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadDatosxSincro(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        SQLiteDatabase db2 = conn.getWritableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            idProduct = obj.getInt("prepro_fk");
            precioProduct = obj.getDouble("lstp_precio");

            //Toast.makeText(this, "Dato actualizado ", Toast.LENGTH_LONG).show();
            db2.execSQL("UPDATE prest_prod SET prepro_precompra = "+precioProduct+" WHERE prepro_id ="+idProduct);
        }
    }*/



    //IMPLEMENTACION DEL WEB SERVICE
    private void getJSONUSUARIOS(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_usuario", email);
                    postDataParams.put("usr_password", password);
                    postDataParams.put("direc", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion);


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

                try {
                    loadIntoListUSR(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("NO SE ENCONTRARON COINCIDENCIAS")){
                    progressDialog.dismiss();
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Los datos del usuario ingresado no se encuentran registrados", Toast.LENGTH_SHORT).show();
                    usua.setText("");
                    pasw.setText("");


                }else if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreCjus != null){
                    progressDialog.dismiss();
                    AlertCierre();

                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }



    private void loadIntoListUSR(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            IdCjus = obj.getInt("cjus_id");
            nombreLgn = obj.getString("prs_nombre");
        }

        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId2 = IdCjus;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreCjus = nombreLgn;
    }


    //***** CIERRE DE CAJA*******//
    //IMPLEMENTACION DEL WEB SERVICE (VALIDACIONES SOBRE EL CIERRE DE CAJA)
    private void getJSONValiCierre(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject Params2 = new JSONObject();
                    Params2.put("cjus_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId);
                    Params2.put("fechaa", String.valueOf(fechaSQL()));

                    return RequestHandler.sendPost(urlWebService,Params2);
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
                    loadValidacionCierre(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (s.equals("Ya se cerro la caja anteriormente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se puede cerrar la caja debido a que ya se realizo este proceso..", Toast.LENGTH_LONG).show();

                }else if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmNom.equals("Cierre")){
                    AlertAutoriza();

                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadValidacionCierre(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            TpmId = obj.getString("tpm_id");
            TpmNom = obj.getString("tpm_nombre");

        }
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId = TpmId;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmNom = TpmNom;
    }


    //METODO PARA INSERTAR DATOS EN MYSQL
    private  void RegistraCierre(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "Cierre de caja realizada!!!", Toast.LENGTH_SHORT).show();

                    ConsultaSql cierreCaja = new ConsultaSql();
                    int varTemp = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId);
                    cierreCaja.registrarApertura(fechaSQL(), Horaa(), varTemp, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveCU, getApplicationContext());

                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));

                }else if (response.equals("No se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();

                }
                else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this, error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
                String cjus = String.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cjusId2);
                String montoChar = String.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl);

                parametros.put("fechaa", fechaSQL());
                parametros.put("hora", Horaa());
                //parametros.put("caja", Globales.getInstance().cjusId);
                parametros.put("caja", cjus);
                parametros.put("tpm", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tpmId);
                parametros.put("concepto", "Cierre");
                parametros.put("monto", montoChar);//montoTotl
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
        requestQueue.add(stringRequest);
    }


    //METODO PARA LA IMPRESION
    public class
    Reimprimiendo extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;

        Reimprimiendo() {
            mContext = com.lzacatzontetlh.koonolmodulos.CajaMenu.this;
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

            try {

                impresora.conecta();

                cadenota =  "Cierre de caja: "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId+" \n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().estNom +" \n"+
                        "Fecha:" + fecha() +"   Hora:"+hora()+"\n-------------------------------\n"+"Informacion ingresada por el "+" \n"+
                        "Cajero:"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajero +" \n"+"Saldo inicial: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura+"\n"+"Efectivo: $"+ e +" \n"+
                        "Tarjetas: $"+t+" \n"+ "Total en caja: $"+ToTal2+" \n -------------------------------\n"+
                        "Informacion sistema: "+" \n"+ "Cajero: "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreCjus +" \n"+"Saldo inicial: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoApertura+"\n"+"Efectivo: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalEfectivo2 +" \n"+
                        "Tarjetas: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().totalTarjeta2+" \n"+ "Total en caja: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().montoTotl+" \n"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().mensajeCorte+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direfe+" ***";

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
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //consultaEstatus();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaOperacion = "Cierre";
                    RegistraCierre("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_movCaja.php");
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
            AlertDialog alert = builder2.create();
            alert.show();
        }

    }


    //IMPLEMENTACION DEL MENÚ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoverflow, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id== R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CajaMenu.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.CajaMenu.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
            startActivity(intencion2);
        }

        return super.onOptionsItemSelected(item);
    }

}
