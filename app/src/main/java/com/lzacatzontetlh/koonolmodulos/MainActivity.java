package com.lzacatzontetlh.koonolmodulos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

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
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    static String str, usrID;
    String email, password;
    int numeero, rolid=0;
    ArrayList codigoList = new ArrayList();
    int idEmp, idEsta, idCja;
    String usrStatus="", prsnom="", usr="", usrClav="", rolnom="",  usrid="",  prsfk="", mac="", NombEmp="", nomEsta="", nomCaja="";
    private final static String[] requestWritePermission = { Manifest.permission.WRITE_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonSignIn = (Button)findViewById(R.id.buttonSignin);

        try {
            deployDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getMacAddr();

        progressDialog = new ProgressDialog(this);
        buttonSignIn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if(v == buttonSignIn){
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();


            if(TextUtils.isEmpty(email)){
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Por favor introduzca su usuario", Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Por favor introduzca su contraseña", Toast.LENGTH_LONG).show();
                return;
            }else{

                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().emailUsr2 = email;
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveUsr2 = password;
                //Validación para iniciar sesión siempre y cuando tenga conexión a internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    progressDialog.setMessage("Iniciando sesión ...");
                    progressDialog.show();

                    getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php");

                } else {
                    // No hay conexión a Internet en este momento
                    Toast.makeText(this, "No hay conexión a Internet, se podrá iniciar sesión cuando te conectes a una red", Toast.LENGTH_LONG).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }
            }

        }
    }


    //METODO QUE GENERA 6 numeros entre 1 y 10 PARA EL MENSAJE DE VERIFICACION
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
        Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "El codigo es: "+str, Toast.LENGTH_LONG).show();
    }


    //IMPLEMENTACION DEL WEB SERVICE
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_usuario", email);
                    postDataParams.put("usr_password", password);
                    postDataParams.put("direcc", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion);

                    return RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_usuarios.php",postDataParams);
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
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Los datos del usuario ingresado no se encuentran registrados", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");

                }/*else if(usrStatus.equals("Habilitado")){
                    progressDialog.dismiss();
                    try {
                        loadIntoListView2(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), MenuGeneral.class));


                }*/else if (usrStatus.equals("Nuevo")){
                    try {
                        loadIntoListView3(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                    generadorCodigos();
                    finish();
                    startActivity(new Intent(getApplicationContext(),CodigoVerificacion.class));

                }else  if (usrStatus.equals("Habilitado")){
                    try {
                        loadIntoListView4(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));

                }else if (s.equals("Deshabilitado")){
                    //else if (usrStatus.equals("Deshabilitado")){
                    progressDialog.dismiss();
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Lo sentimos, usted no tiene acceso", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }

                if (s.equals("Usuario no autorizado")){
                    progressDialog.dismiss();
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Usuario no autorizado", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");

                }

                else if(s.equals("Habilitadox2")){
                    progressDialog.dismiss();
                    try {
                        loadIntoListView2(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));


                }else if (s.equals("Esta logueado")){
                    progressDialog.dismiss();
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.MainActivity.this, "Usuario se encuentra logueado en otro dispositivo", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }

                if (s.equals("Actualizar datos")){
                    progressDialog.dismiss();
                    try {
                        loadIntoListView2(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                }


            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    //ESTATUS
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            usrStatus = obj.getString("esta_estatus");
        }
    }

    //HABILITADO
    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            usrid = obj.getString("usr_id");
            usrStatus = obj.getString("esta_estatus");
            prsfk = obj.getString("prs_fk");
            prsnom = obj.getString("prs_nombre");
            usr = obj.getString("usr_usuario");
            usrClav = obj.getString("usr_password");
            rolid = obj.getInt("rol_id");
            rolnom = obj.getString("rol_desc");
            mac = obj.getString("cjus_mac");
        }

        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario= usrid;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().emailUsr = usr;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveUsr = usrClav;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsFk = prsfk;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsNom = prsnom;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol = rolid;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol2 = rolnom;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion2 = mac;
    }

    //NUEVO
    private void loadIntoListView3(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            usrid = obj.getString("usr_id");
            usrStatus = obj.getString("esta_estatus");
            prsfk = obj.getString("prs_fk");
            prsnom = obj.getString("prs_nombre");
            //prsfk = obj.getInt("prs_fk");
            usr = obj.getString("usr_usuario");
            usrClav = obj.getString("usr_password");
            rolid = obj.getInt("rol_id");
            rolnom = obj.getString("rol_desc");
        }

        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario= usrid;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().emailUsr = usr;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveUsr = usrClav;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsFk = prsfk;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsNom = prsnom;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol = rolid;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol2 = rolnom;
    }

    //CAJERO NUEVO
    private void loadIntoListView4(String json) throws JSONException {
        JSONArray jArray = new JSONArray(json);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            if (i == 0){
            usrid = obj.getString("usr_id");
            prsfk = obj.getString("prs_fk");
            prsnom = obj.getString("prs_nombre");
            usr = obj.getString("usr_usuario");
            usrClav = obj.getString("usr_password");
            rolid = obj.getInt("rol_id");
            rolnom = obj.getString("rol_desc");
            }else {
            //obj = jArray.getJSONObject(i+1);
            idCja = obj.getInt("cja_id");
            nomCaja = obj.getString("cja_nombre");
            idEmp = obj.getInt("emp_id");
            NombEmp = obj.getString("emp_nombre");
            idEsta = obj.getInt("est_id");
            nomEsta = obj.getString("est_nombre");
            }
        }

        int valorUsr2 = Integer.valueOf(usrid);
        int valorprs  = Integer.valueOf(prsfk);
        int valorCja = Integer.valueOf(idCja);

        //INSERCIÓN EN LA TABLA CONFIGURACION en SQLite
        ConsultaSql config = new ConsultaSql();
        config.registrarConfiguracion(valorprs, prsnom, valorUsr2, rolid, rolnom, idEsta, nomEsta, idCja, nomCaja, usr, usrClav, idEmp, NombEmp, getApplicationContext());

        //INSERCIÓN EN LA TABLA CAJA-USUARIO SQLite
        ConsultaSql cja_usr = new ConsultaSql();

        //GENERACION DE CLAVE (VERIFICAR)
        String claveComp = "00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId+"0"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId;
        cja_usr.configuracionInicial(valorCja, valorUsr2,claveComp,getApplicationContext());
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

    //MÉTODO PARA LA OBTENCIÓN DE LA DIRECCIÓN
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion = res1.toString();
                }
                return res1.toString();

            }
        } catch (Exception ex) {
        }
        return "";
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
                    RuntimePermissionUtil.requestPermission(com.lzacatzontetlh.koonolmodulos.MainActivity.this, requestWritePermission, 100);
                    directory.mkdirs();
                }
            }
            RuntimePermissionUtil.requestPermission(com.lzacatzontetlh.koonolmodulos.MainActivity.this, requestWritePermission, 100);
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
        }

    }

}