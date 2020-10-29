package com.lzacatzontetlh.koonolmodulos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SeleccionBodega extends AppCompatActivity {
    Spinner sucursal, caja;
    Button agregar;
    private ProgressDialog progressDialog;
    int valorUsr, valorCja, valorprs, valorEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_bodega);

        sucursal = (Spinner)findViewById(R.id.sucursal);
        caja = (Spinner)findViewById(R.id.caja);
        agregar = (Button)findViewById(R.id.guardar);
        progressDialog = new ProgressDialog(this);

        getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_establecimientos.php");


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sucursal.getSelectedItemPosition() == AdapterView.INVALID_POSITION){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, "No hay sucursales disponibles", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, MainActivity.class));

                }else if (caja.getSelectedItemPosition() == AdapterView.INVALID_POSITION){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, "No hay cajas disponibles", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, MainActivity.class));

                }else {
                    //REGISTRO DE CAJA-USUARIO Y ACTUALIZACIÓN DE CAMPOS EN MySQL
                    RegistraMysql("https://www.nextcom.com.mx/webserviceapp/koonol/Insertar_caja.php");

                    valorUsr = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario);
                    valorCja = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId);
                    valorEst  = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId);
                    valorprs  = Integer.valueOf(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsFk);

                    //INSERCIÓN EN LA TABLA CONFIGURACION en SQLite
                    ConsultaSql config = new ConsultaSql();
                    config.registrarConfiguracion(valorprs, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().prsNom, valorUsr, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().Rol2, valorEst, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo, valorCja, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().emailUsr, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().claveUsr, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaId, com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaNombre, getApplicationContext());

                    //INSERCIÓN EN LA TABLA CAJA-USUARIO SQLite
                    ConsultaSql cja_usr = new ConsultaSql();

                    //GENERACION DE CLAVE (VERIFICAR)
                    String claveComp = "00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"00"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId+"0"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId;
                    cja_usr.configuracionInicial(valorCja, valorUsr,claveComp,getApplicationContext());

                }

            }
        });



    }

    private void getJSON(final String urlWebService) {

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
                    loadEST(s);
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


    //OBTENCION DE LOS ESTABLECIMIENTOS EN MYSQL
    private void loadEST(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        final String[] sucursalSelect = new String[jsonArray.length()];
        final String[] sucursalId = new String[jsonArray.length()];

        final String[] empresaSelect = new String[jsonArray.length()];
        final int[] empresaId = new int[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            sucursalSelect[i] = obj.getString("est_nombre");
            sucursalId[i] = obj.getString("est_id");
            empresaSelect[i] = obj.getString("emp_nombre");
            empresaId[i] = obj.getInt("emp_id");
            //valor = obj.getString("est_id");
        }
        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sucursalSelect);
        sucursal.setAdapter(adapter2);

        sucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String datoSelect=  sucursal.getItemAtPosition(sucursal.getSelectedItemPosition()).toString();
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId = sucursalId[position];
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo = sucursalSelect[position];
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaNombre = empresaSelect[position];
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().empresaId = empresaId[position];

                getJSON_CJA("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_caja.php");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //IMPLEMENTACION DEL WEB SERVICE
    private void getJSON_CJA(final String urlWebService) {

        class GetJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... strings) {
                try {
                    // POST Request
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("est_id", com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoId);

                    return CajaMenu.RequestHandler.sendPost("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_caja.php",postDataParams);
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
                    //progressDialog.dismiss();
                    loadIntoListView(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    //OBTENCION DE LAS CAJAS EN MYSQL
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        final String[] cajaSelect = new String[jsonArray.length()];
        final String[] cajaId = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            cajaSelect[i] = obj.getString("cja_nombre");
            cajaId[i]= obj.getString("cja_id");
        }
        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cajaSelect);
        caja.setAdapter(adapter2);

        caja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaId = cajaId[position];
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cajaa = cajaSelect[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    //MÉTODO PARA REGISTRAR DATOS DE CAJA Y USUARIO
    private  void RegistraMysql(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Se ha insertado correctamente")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, "Configuración inicial, correcta !!!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, MenuGeneral.class));


                }else if (response.equals("No se ha podido insertar el registro")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, "La configuración inicial no se pudo realizar!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this, error.toString() , Toast.LENGTH_SHORT).show();
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

               // parametros.put("usuario_fk", String.valueOf(MainActivity.usrID));
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(com.lzacatzontetlh.koonolmodulos.SeleccionBodega.this);
        requestQueue.add(stringRequest);
    }


}
