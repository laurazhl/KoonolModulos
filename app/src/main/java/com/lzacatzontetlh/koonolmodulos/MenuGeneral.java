package com.lzacatzontetlh.koonolmodulos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.ConfiguraImpresora;
import com.lzacatzontetlh.koonolmodulos.modelo.ConsultaSql;
import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapter;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.RuntimePermissionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MenuGeneral extends AppCompatActivity {
    final Context context = this;
    private GridView gridView;
    TextView textoProducto;
    GridAdapter adapter;
    Cursor cursor, cursorFisica, cursorMoral;
    String ImagenID="",prs_nombre,prs_rfc,prs_calle,prs_noint,prs_noext,prs_telefono,prs_email,prs_facebook,prs_twitter,prs_instagram,prs_tipo,prs_diascred,prs_limcred,prs_saldo,prs_actualizar,cp_fk,esta_fk,tpp_fk;
    String fsc_nombre, fsc_apaterno, fsc_amaterno, fsc_curp, fsc_actualizar, prs_fkfsc ;
    String mrl_razonsocial,mrl_actualizar,prs_fkmrl;
    String valor, nombreTabla,nombreDelCampo;
    String varDecision;


    Integer prs_id=0, fsc_id=0,mrl_id=0 ;
    private Uri filePath;
    String selectItem, concatenar;
    private final static String[] requestWritePermission = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_general);


        //PUNTO DE VENTA ORIGINAL
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Caja");
        arrayList.add("Clientes");
        arrayList.add("Ventas");
        arrayList.add("Productos");
        arrayList.add("Almacén");
        arrayList.add("Configuración Impresora");

        int images[] ={R.drawable.caja, R.drawable.clientes, R.drawable.venta,  R.drawable.producto,R.drawable.almacen, R.drawable.impresora};

        adapter = new GridAdapter(this,images, arrayList);*/


        //PUNTO DE VENTA DON CHON
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Caja");
        arrayList.add("Ventas");
        arrayList.add("Productos");
        arrayList.add("Configuración Impresora");
        arrayList.add("Clientes");



        int images[] ={R.drawable.caja, R.drawable.venta,  R.drawable.producto, R.drawable.impresora,R.drawable.clientess,};

        adapter = new GridAdapter(this,images, arrayList);
        gridView = (GridView) findViewById(R.id.gridviewMenu);
        textoProducto = (TextView) findViewById(R.id.text);


        gridView.setAdapter(adapter);

        /*try {
            deployDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //DATOS GENERALES DEL USUARIO
        ConsultaSql datos = new ConsultaSql();
        datos.datosCompletos(getApplication());

        ConsultaSql statusApert = new ConsultaSql();
        statusApert.consultaMontoApert(getApplicationContext());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nombreProducto = (TextView) view.findViewById(R.id.ig_tv_titulo);
                selectItem = nombreProducto.getText().toString();
                //concatenar = selectItem+"Menu";
                //Toast.makeText(MenuGeneral.this, selectItem, Toast.LENGTH_SHORT).show();
                if (selectItem.equals("Clientes")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, ClienteMenu.class));

                }if (selectItem.equals("Caja")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, CajaMenu.class));

                }if (selectItem.equals("Productos")) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, Productos.class));
                }
                if (selectItem.equals("Ventas")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, VentaMenu.class));

                }if (selectItem.equals("Almacén")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, com.lzacatzontetlh.koonolmodulos.AlmacenMenu.class));
                }
                if (selectItem.equals("Configuración Impresora")){

                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String impresora = prefs.getString("impresora", "xx");

                    ConfiguraImpresora imp = ConfiguraImpresora.newInstance(0);
                    imp.show(getSupportFragmentManager(), "imp");
                }

            }
        });


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            /*firebaseDatabase = firebaseDatabase.getInstance();
            databaseReference= firebaseDatabase.getReference();
            databaseReference.child("Imagen").child(Globales.getInstance().nombreUsuario).child("Sincronizado").setValue(1);*/
            Toast.makeText(getApplicationContext(), "Sincronizando...", Toast.LENGTH_LONG).show();
         //   consultarDatosPersona();
          //  insertarEnMysqlPersonaFisica();
         //   insertarEnMysqlPersonaMoral();

          //  getJSONValidarP("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php");

        } else {
            // No hay conexión a Internet en este momento
            Toast.makeText(this, "No hay conexión a Internet en este momento", Toast.LENGTH_LONG).show();
        }
    }


    private void deployDatabase() throws IOException {
        //Open your local db as the input stream

        final boolean hasWritePermission = RuntimePermissionUtil.checkPermissonGranted(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String packageName = getApplicationContext().getPackageName();
        String DB_PATH = "/data/data/" + packageName + "/databases/";

        //Create the directory if it does not exist
        File directory = new File(DB_PATH);
        File archivo = new File(DB_PATH+"/BDKoonol.db");
        if(!archivo.exists()) {
            if (!directory.exists()) {
                if (hasWritePermission) {
                    directory.mkdirs();
                } else {
                    RuntimePermissionUtil.requestPermission(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, requestWritePermission, 100);
                    directory.mkdirs();
                }
            }
            RuntimePermissionUtil.requestPermission(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, requestWritePermission, 100);
            String DB_NAME = "BDKoonol.db"; //The name of the source sqlite file

            InputStream myInput = getAssets().open(
                    "BDKoonol.db");

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }/*else {
            //Toast.makeText(MenuGeneral.this, "Base de datos encontrada", Toast.LENGTH_LONG).show();
        //}*/

    }





    private void consultarDatosPersona() {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();

        cursor =db.rawQuery("SELECT * FROM persona WHERE prs_actualizar=0", null); //db.query(Utilidades.TABLA_IMAGEN, campos, Utilidades.CAMPO_IMAGENSINCRONIZADO+"=?", parametros, null, null, null);
        Integer cuenta=cursor.getCount();
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                prs_id = cursor.getInt(cursor.getColumnIndex("prs_id"));
                 prs_nombre= cursor.getString(cursor.getColumnIndex("prs_nombre"));
                prs_rfc  = cursor.getString(cursor.getColumnIndex("prs_rfc"));
                prs_calle= cursor.getString(cursor.getColumnIndex("prs_calle"));
                prs_noint= cursor.getString(cursor.getColumnIndex("prs_noint"));
                prs_noext= cursor.getString(cursor.getColumnIndex("prs_noext"));
                prs_telefono= cursor.getString(cursor.getColumnIndex("prs_telefono"));
                prs_email= cursor.getString(cursor.getColumnIndex("prs_email"));
                prs_facebook= cursor.getString(cursor.getColumnIndex("prs_facebook"));
                prs_twitter= cursor.getString(cursor.getColumnIndex("prs_twitter"));
                prs_instagram= cursor.getString(cursor.getColumnIndex("prs_instagram"));
                prs_tipo= cursor.getString(cursor.getColumnIndex("prs_tipo"));
                prs_diascred= cursor.getString(cursor.getColumnIndex("prs_diascred"));
                prs_limcred= cursor.getString(cursor.getColumnIndex("prs_limcred"));
                prs_saldo= cursor.getString(cursor.getColumnIndex("prs_saldo"));
                prs_actualizar= cursor.getString(cursor.getColumnIndex("prs_actualizar"));
                cp_fk= cursor.getString(cursor.getColumnIndex("cp_fk"));
                esta_fk= cursor.getString(cursor.getColumnIndex("esta_fk"));
                tpp_fk= cursor.getString(cursor.getColumnIndex("tpp_fk"));
                System.out.println(" 333 ");
                if (prs_id>0) {

                    System.out.println(" 1111 ");
                    //si el rfc existe no insertara esa persona
                    valor = cursor.getString(cursor.getColumnIndex("prs_rfc"));
                    nombreTabla="persona";
                    nombreDelCampo="prs_rfc";
                 //   String res= consultarP("https://www.nextcom.com.mx/webserviceapp/koonol/Consultar_existenciaPV.php");
                    //getJSONgetJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consultar_existenciaPV.php");

                    RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_personas.php");
                      //  RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_personas.php");

                }else {
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoAP(getApplicationContext(),prs_id);
                    consultarDatosPersona();
                }
            }
        }catch(Exception e){
           // Toast.makeText(getApplicationContext(), "Sincronización exitosa!!", Toast.LENGTH_LONG).show();

          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Sincronización exitosa!!");
            builder.setTitle("EXITO");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();*/
        }
        //cursor.close();
    }


    private  void RegistraMysql(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Se pudo insertar correctamente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "Datos registrados correctamente !!!", Toast.LENGTH_SHORT).show();
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoAP(getApplicationContext(),prs_id);
                    consultarDatosPersona();
                }
                else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this,"Error en registrar "+ error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
                parametros.put("prs_nombre", prs_nombre);
                parametros.put("prs_rfc",prs_rfc );
                parametros.put("prs_calle",prs_calle );
                parametros.put("prs_noint",prs_noint );
                parametros.put("prs_noext",prs_noext );
                parametros.put("prs_telefono", prs_telefono);
                parametros.put("prs_email", prs_email);
                parametros.put("prs_facebook", prs_facebook);
                parametros.put("prs_twitter", prs_twitter);
                parametros.put("prs_instagram", prs_instagram);
                parametros.put("prs_tipo",prs_tipo );
                parametros.put("prs_diascred", prs_diascred);
                parametros.put("prs_limcred", prs_limcred);
                parametros.put("prs_saldo", prs_saldo);
                parametros.put("cp_fk", cp_fk);
                parametros.put("esta_fk", esta_fk);
                parametros.put("tpp_fk", tpp_fk);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
        requestQueue.add(stringRequest);
        System.out.println(" stringRequest "+stringRequest);
    }



    private void insertarEnMysqlPersonaFisica() {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        cursorFisica =db.rawQuery("SELECT * FROM fisica WHERE fsc_actualizar=0", null); //db.query(Utilidades.TABLA_IMAGEN, campos, Utilidades.CAMPO_IMAGENSINCRONIZADO+"=?", parametros, null, null, null);
        Integer cuenta=cursorFisica.getCount();
        try {
            if (cursorFisica != null) {
                cursorFisica.moveToFirst();
                fsc_id= cursorFisica.getInt(cursorFisica.getColumnIndex("fsc_id"));
                fsc_nombre= cursorFisica.getString(cursorFisica.getColumnIndex("fsc_nombre"));
                fsc_apaterno  = cursorFisica.getString(cursorFisica.getColumnIndex("fsc_apaterno"));
                fsc_amaterno= cursorFisica.getString(cursorFisica.getColumnIndex("fsc_amaterno"));
                fsc_curp= cursorFisica.getString(cursorFisica.getColumnIndex("fsc_curp"));
                fsc_actualizar= cursorFisica.getString(cursorFisica.getColumnIndex("fsc_actualizar"));
                prs_fkfsc= cursorFisica.getString(cursorFisica.getColumnIndex("prs_fk"));
                if (fsc_id>0) {
                    RegistraMysqlPF("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_personaFisica.php");
                }else {
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoPF(getApplicationContext(),fsc_id);
                    insertarEnMysqlPersonaFisica();
                }
            }
        }catch(Exception e){
           // Toast.makeText(getApplicationContext(), "Sincronización exitosa!!", Toast.LENGTH_LONG).show();
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Sincronización exitosa!!");
            builder.setTitle("EXITO");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();*/
        }
    }

    private  void RegistraMysqlPF(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Se pudo insertar correctamente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "Datos registrados correctamente !!!", Toast.LENGTH_SHORT).show();
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoPF(getApplicationContext(),fsc_id);
                    insertarEnMysqlPersonaFisica();
                    //  finish();
                    //  startActivity(new Intent(MenuGeneral.this, MenuGeneral.class));
                }else if (response.equals("Se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }
                else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this,"Error en registrar "+ error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
                parametros.put("fsc_nombre", fsc_nombre);
                parametros.put("fsc_apaterno",fsc_apaterno );
                parametros.put("fsc_amaterno",fsc_amaterno );
                parametros.put("fsc_curp",fsc_curp );
                parametros.put("prs_fk",prs_fkfsc );
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
        requestQueue.add(stringRequest);
        System.out.println(" stringRequest5 "+stringRequest);
    }



    private void insertarEnMysqlPersonaMoral() {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        cursorMoral =db.rawQuery("SELECT * FROM moral WHERE mrl_actualizar=0", null); //db.query(Utilidades.TABLA_IMAGEN, campos, Utilidades.CAMPO_IMAGENSINCRONIZADO+"=?", parametros, null, null, null);
        Integer cuenta=cursorMoral.getCount();
        try {
            if (cursorMoral != null) {
                cursorMoral.moveToFirst();
                mrl_id= cursorMoral.getInt(cursorMoral.getColumnIndex("mrl_id"));
                mrl_razonsocial= cursorMoral.getString(cursorMoral.getColumnIndex("mrl_razonsocial"));

                System.out.println(" mrl_razonsocial58 "+mrl_razonsocial);

                prs_fkmrl= cursorMoral.getString(cursorMoral.getColumnIndex("prs_fk"));
                if (mrl_id>0) {
                    RegistraMysqlPM("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_personaMoral.php");
                }else {
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoPM(getApplicationContext(),mrl_id);
                    insertarEnMysqlPersonaMoral();
                }
            }
        }catch(Exception e){
           // Toast.makeText(getApplicationContext(), "Sincronización exitosa!!", Toast.LENGTH_LONG).show();

          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Sincronización exitosa!!");
            builder.setTitle("EXITO");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();*/
        }
        //cursorMoral.close();
    }

    private  void RegistraMysqlPM(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Se pudo insertar correctamente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "Datos registrados correctamente !!!", Toast.LENGTH_SHORT).show();
                    Ingresarsql sq = new Ingresarsql();
                    sq.ActualizarEstadoPM(getApplicationContext(),mrl_id);
                    insertarEnMysqlPersonaMoral();
                    //  finish();
                    //  startActivity(new Intent(MenuGeneral.this, MenuGeneral.class));

                }else if (response.equals("Se pudo insertar correctamente x2")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }
                else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, "No se pudo realizar esta operación!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this,"Error en registrar "+ error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
                System.out.println(" mrl_razonsocial2 "+mrl_razonsocial);
                parametros.put("mrl_razonsocial", mrl_razonsocial);
                parametros.put("prs_fk",prs_fkmrl );

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
        requestQueue.add(stringRequest);
        System.out.println(" stringRequest2 "+stringRequest);
    }

    private String consultarP(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("El dato existe")){
                 //   varDecision ="El dato existe";
                    System.out.println(" El dato existe varDecision "+ varDecision);

                }else if (response.equals("El dato no existe")){
                 //   varDecision ="El dato no existe";
                    System.out.println(" El dato no existe  varDecision"+  varDecision);
                }
               // System.out.println(" noentro  ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this,"Error en registrar "+ error.toString() , Toast.LENGTH_SHORT).show();
                System.out.println("onErrorResponse");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();
                System.out.println(" valor "+ valor);
                parametros.put("valor",valor);

                System.out.println(" nombreTabla "+ nombreTabla);
                parametros.put("nombreTabla",nombreTabla);

                System.out.println(" nombreDelCampo "+ nombreDelCampo);
                parametros.put("nombreDelCampo",nombreDelCampo);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
        requestQueue.add(stringRequest);
        System.out.println(" stringRequest "+stringRequest);
        System.out.println("  varDecision  "+varDecision);

        return varDecision;


    }


    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    //postDataParams.put("usr_id", MainActivity.usrID);
                    postDataParams.put("valor", valor);
                    System.out.println("  valor  "+valor);
                    return RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/Consultar_existenciaPV.php",postDataParams);
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
                if (s.equals("El dato existe")){
                    System.out.println(" La persona ya existe  "+ s+ valor);
                    valor=null;

                }else if (s.equals("El dato no existe")){
                    System.out.println("  El dato no existe  "+s + valor);
                    RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_personas.php");
                    valor=null;
                }

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



    //IMPLEMENTACION DEL MENÚ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusalir, menu);
        //
        Ingresarsql sq = new Ingresarsql();
        sq.consultarNombreUsuario(getApplicationContext());
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);
        return true;
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){

           /* LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.cierre_sesion, null);
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(MenuGeneral.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final Button aceptar = (Button) prompstsView2.findViewById(R.id.btnAceptar4);
            final Button cancelaar = (Button) prompstsView2.findViewById(R.id.btnCancela4);

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConsultaSql cierre = new ConsultaSql();
                    cierre.getJSONCierre("https://www.nextcom.com.mx/webserviceapp/koonol/cierre_usuario.php", getApplicationContext());
                }
            });

            cancelaar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(MenuGeneral.this, MenuGeneral.class);
                    startActivity(intent);
                }
            });

            AlertDialog alert = builder2.create();
            alert.show();*/

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intent = new Intent(com.lzacatzontetlh.koonolmodulos.MenuGeneral.this, MainActivity.class);
                            startActivity(intent);
                            //ConsultaSql cierre = new ConsultaSql();
                           //cierre.getJSONCierre("https://www.nextcom.com.mx/webserviceapp/koonol/cierre_usuario.php", getApplicationContext());

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

        return super.onOptionsItemSelected(item);
    }




}


