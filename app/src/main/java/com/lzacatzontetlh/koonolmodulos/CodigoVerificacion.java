package com.lzacatzontetlh.koonolmodulos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lzacatzontetlh.koonolmodulos.modelo.ConsultaSql;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class CodigoVerificacion extends AppCompatActivity {
    EditText codigo;
    Button sig, reenviar;
    String codigoV;
    final Context context = this;
    int numeero;
    ArrayList codigoList = new ArrayList();
    static String str2;
    int idCja=0, idEsta=0, valorUsr2, valorCja2, valorprs2, valorEst2, idEmpresa=0;
    String nomEsta, nomCaja, nombrEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_verificacion);

        codigo = (EditText) findViewById(R.id.codigo);
        sig = (Button) findViewById(R.id.siguiente);
        reenviar = (Button) findViewById(R.id.mensaje);

        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               codigoV = codigo.getText().toString();

                if(TextUtils.isEmpty(codigoV)){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "Por favor, introduzca el código de verificación", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if (codigoV.equals(com.lzacatzontetlh.koonolmodulos.MainActivity.str) || codigoV.equals(str2)){
                        getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_cajero.php");

                    }else {
                        LayoutInflater li2 = LayoutInflater.from(context);
                        View prompstsView2 = li2.inflate(R.layout.alaert_codigo, null);
                        final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this);
                        builder2.setView(prompstsView2);
                        builder2.setCancelable(false);


                        builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                codigo.setText("");

                            }
                        });

                        android.support.v7.app.AlertDialog alert = builder2.create();
                        alert.show();
                    }
                }
            }
        });

        reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generadorCodigos();
            }
        });
    }


    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);
                    postDataParams.put("direcc", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion);

                    return RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_cajero.php",postDataParams);
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


                if (s.equals("Ya existe caja registrada")){
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));

                }else if (s.equals("Ya existe administrador en esta caja")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "Esta caja ya cuenta con un administrador", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, com.lzacatzontetlh.koonolmodulos.MainActivity.class));

                }else if (s.equals("TIENE PERMISOS")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, com.lzacatzontetlh.koonolmodulos.SeleccionBodega.class));

                }/*else if (s.equals("Ya esta configurada la caja")){

                    Toast.makeText(CodigoVerificacion.this, "Ya esta configurada la caja"+idCja, Toast.LENGTH_LONG).show();*/

                    /*valorUsr2 = Integer.valueOf(Globales.getInstance().id_usuario);
                    valorCja2 = Integer.valueOf(Globales.getInstance().cajaId);
                    valorEst2  = Integer.valueOf(Globales.getInstance().establecimientoId);
                    valorprs2  = Integer.valueOf(Globales.getInstance().prsFk);

                    //INSERCIÓN EN LA TABLA CONFIGURACION en SQLite
                    ConsultaSql config = new ConsultaSql();
                    config.registrarConfiguracion(valorprs2, Globales.getInstance().prsNom, valorUsr2, Globales.getInstance().Rol, Globales.getInstance().Rol2, valorEst2, Globales.getInstance().establecimientoo, valorCja2, Globales.getInstance().cajaa, Globales.getInstance().emailUsr, Globales.getInstance().claveUsr, getApplicationContext());

                    //INSERCIÓN EN LA TABLA CAJA-USUARIO SQLite
                    ConsultaSql cja_usr = new ConsultaSql();

                    //GENERACION DE CLAVE (VERIFICAR)
                    String claveComp = "00"+Globales.getInstance().id_usuario+"00"+Globales.getInstance().cajaId+"0"+Globales.getInstance().establecimientoId;
                    cja_usr.configuracionInicial(valorCja2, valorUsr2,claveComp,getApplicationContext());*/


                //}
                else if (idCja != 0){
                    //REGISTRO DE CAJA-USUARIO Y ACTUALIZACIÓN DE CAMPOS EN MySQL
                    RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/caja_cajero.php");

                    valorUsr2 = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);
                    valorCja2 = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId);
                    valorEst2  = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId);
                    valorprs2  = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsFk);

                    //INSERCIÓN EN LA TABLA CONFIGURACION en SQLite
                    ConsultaSql config = new ConsultaSql();
                    config.registrarConfiguracion(valorprs2, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsNom, valorUsr2, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol2, valorEst2, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo, valorCja2, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().emailUsr, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveUsr, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaId, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaNombre, getApplicationContext());

                    //INSERCIÓN EN LA TABLA CAJA-USUARIO SQLite
                    ConsultaSql cja_usr = new ConsultaSql();

                    //GENERACION DE CLAVE (VERIFICAR)
                    String claveComp = "00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId+"0"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId;
                    cja_usr.configuracionInicial(valorCja2, valorUsr2,claveComp,getApplicationContext());

                }
                else if (s.equals("Aun no se ha registrado configuracion")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "No existe registrada configuración inicial", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, com.lzacatzontetlh.koonolmodulos.MainActivity.class));

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
            idCja = obj.getInt("cja_id");
            idEsta = obj.getInt("est_id");
            nomCaja = obj.getString("cja_nombre");
            nomEsta = obj.getString("est_nombre");
            nombrEmpresa = obj.getString("emp_nombre");
            idEmpresa = obj.getInt("emp_id");
        }

        String val1= String.valueOf(idCja);
        String val2 = String.valueOf(idEsta);
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId = val2;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId = val1;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa = nomCaja;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo = nomEsta;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaNombre = nombrEmpresa;
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaId = idEmpresa;
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
        str2 = TextUtils.join("", codigoList);
        Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "El codigo es: "+str2, Toast.LENGTH_LONG).show();
    }


    //MÉTODO PARA REGISTRAR DATOS DE CAJA Y USUARIO
    private  void RegistraMysql(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Se ha insertado correctamente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "Configuración inicial, correcta !!!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));


                }else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, "La configuración inicial no se pudo realizar!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this, error.toString() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<String, String>();

                parametros.put("caja_fk", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId);
                parametros.put("establecimiento_fk", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId);
                parametros.put("usuario_fk", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);
                parametros.put("persona_fk", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsFk);
                parametros.put("direcc", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().direccion);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.CodigoVerificacion.this);
        requestQueue.add(stringRequest);
    }
}
