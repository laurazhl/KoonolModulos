package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.VentaaAdapter;
import com.lzacatzontetlh.koonolmodulos.modelo.VentasInfo;

import java.util.ArrayList;
import java.util.Calendar;

public class VentasPrevias extends AppCompatActivity implements VentaaAdapter.MiListener{

    EditText fechI;
    ImageButton btnFech, buscarFolio;
    private int dia, mes, anio;
    Spinner statuss;
    String[] status = new String[] {"Pagado", "Por pagar"};
    RecyclerView recyclerView;
    VentaaAdapter adaptaVentsLista;
    ArrayList<VentasInfo> productos;
    ConexionSQLiteHelper conn;
    String sCadenaInvertida = "", folioVenta="", statusVenta="", folio="";
    double totalVenta = 0.0;
    final Context context = this;
    String status3;
    SQLiteDatabase db;
    int[] ventaID= new int[100];
    EditText ventaFolio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_previas);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        conn=new ConexionSQLiteHelper(getApplicationContext());
        productos = new ArrayList<VentasInfo>();

        btnFech = (ImageButton) findViewById(R.id.ButtonAgregar);
        fechI = (EditText) findViewById(R.id.editAsk);
        statuss = (Spinner)findViewById(R.id.sta);
        ventaFolio = (EditText)findViewById(R.id.venta);
        recyclerView = (RecyclerView) findViewById(R.id.ventasView);
        buscarFolio = (ImageButton)findViewById(R.id.btnBuscar);

        adaptaVentsLista = new VentaaAdapter(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this, productos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        statuss.setAdapter(adapter);

        TodaslasVentas();


        btnFech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                anio = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i<1960){
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this, "El año debe ser mayor a 1960", Toast.LENGTH_LONG).show();
                        }else {
                            int mesActual = i1 + 1;
                            String diaFormateado = (i2 < 10) ? "0" + String.valueOf(i2) : String.valueOf(i2);
                            String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                            fechI.setText(diaFormateado + "/" + mesFormateado + "/" + i);
                            sCadenaInvertida = "";
                            consultaVentas();
                        }
                    }
                },anio, mes, dia);
                datePickerDialog.show();

            }
        });


        buscarFolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaVentaFolio();
            }
        });



    }


    //CONSULTA A BD PARA OBTENER LAS VENTAS POR FECHA Y ESTATUS
    private void consultaVentas() {
        db = conn.getReadableDatabase();
        final String inicio = fechI.getText().toString();
        status3 = statuss.getSelectedItem().toString();

        productos.clear();
        status3 = statuss.getSelectedItem().toString();
        Cursor cursorVentas =db.rawQuery("SELECT doc_folio, doc_total, doc_saldo, esta_estatus"+
                " FROM estatus, documento"+
                " WHERE estatus.esta_estatus='"+status3+"' AND documento.esta_fk=estatus.esta_id AND documento.doc_fecha ='"+inicio+"' AND documento.usr_fk='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"'", null);

        try {
            if (cursorVentas != null) {
                cursorVentas.moveToFirst();
                int index = 0;

                while (!cursorVentas.isAfterLast()) {
                    folioVenta = cursorVentas.getString(cursorVentas.getColumnIndex("doc_folio"));
                    totalVenta = Double.parseDouble(cursorVentas.getString(cursorVentas.getColumnIndex("doc_total")));
                    statusVenta = cursorVentas.getString(cursorVentas.getColumnIndex("esta_estatus"));

                    //Asignación de la información
                    VentasInfo veenta = new VentasInfo(folioVenta,totalVenta,statusVenta);
                    productos.add(veenta);

                    index++;
                    cursorVentas.moveToNext();
                }

                if (index != 0) {
                    recyclerView.setAdapter(adaptaVentsLista);
                            /*if (Globales.getInstance().idVentaPrevia >= 0){
                                startActivity(new Intent(VentasPrevias.this, MenuGeneral.class));
                            }*/
                }


            }

        }catch(Exception e){
        }

        statuss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productos.clear();
                status3 = statuss.getSelectedItem().toString();
                Cursor cursorVentas =db.rawQuery("SELECT doc_folio, doc_total, doc_saldo, esta_estatus"+
                        " FROM estatus, documento"+
                        " WHERE estatus.esta_estatus='"+status3+"' AND documento.esta_fk=estatus.esta_id AND documento.doc_fecha ='"+inicio+"' AND documento.usr_fk='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"'", null);

                try {
                    if (cursorVentas != null) {
                        cursorVentas.moveToFirst();
                        int index = 0;

                        while (!cursorVentas.isAfterLast()) {
                            folioVenta = cursorVentas.getString(cursorVentas.getColumnIndex("doc_folio"));
                            totalVenta = Double.parseDouble(cursorVentas.getString(cursorVentas.getColumnIndex("doc_total")));
                            statusVenta = cursorVentas.getString(cursorVentas.getColumnIndex("esta_estatus"));

                            //Asignación de la información
                            VentasInfo veenta = new VentasInfo(folioVenta,totalVenta,statusVenta);
                            productos.add(veenta);

                            index++;
                            cursorVentas.moveToNext();
                        }

                        if (index != 0) {
                            recyclerView.setAdapter(adaptaVentsLista);
                            /*if (Globales.getInstance().idVentaPrevia >= 0){
                                startActivity(new Intent(VentasPrevias.this, MenuGeneral.class));
                            }*/
                        }


                    }

                }catch(Exception e){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    //CONSULTA A BD PARA OBTENER LA VENTA POR FOLIO
    private void consultaVentaFolio() {
        db = conn.getReadableDatabase();
                productos.clear();
                folio = ventaFolio.getText().toString();
                Cursor cursorVentas =db.rawQuery("SELECT doc_folio, doc_total, doc_saldo, esta_estatus"+
                " FROM estatus, documento"+
                " WHERE documento.doc_folio ='"+folio+"' AND documento.esta_fk = estatus.esta_id AND documento.usr_fk='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"'", null);

                try {
                    if (cursorVentas != null) {
                        cursorVentas.moveToFirst();
                        int index = 0;

                        while (!cursorVentas.isAfterLast()) {
                            folioVenta = cursorVentas.getString(cursorVentas.getColumnIndex("doc_folio"));
                            totalVenta = Double.parseDouble(cursorVentas.getString(cursorVentas.getColumnIndex("doc_total")));
                            statusVenta = cursorVentas.getString(cursorVentas.getColumnIndex("esta_estatus"));

                            //Asignación de la información
                            VentasInfo veenta = new VentasInfo(folioVenta,totalVenta,statusVenta);
                            productos.add(veenta);

                            index++;
                            cursorVentas.moveToNext();
                        }

                        if (index != 0) {
                            recyclerView.setAdapter(adaptaVentsLista);
                        }else {
                            Toast.makeText(this, "No se encontraron coincidencias del folio "+folio, Toast.LENGTH_LONG).show();
                        }


                    }

                }catch(Exception e){
                }
    }


    @Override
    public void onItemClick(VentasInfo infoV, int posicion) {
        //String ld = infoV.getFolioVenta();
        // Toast.makeText(this, ld+ "posicion: "+foLio, Toast.LENGTH_LONG).show();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia = infoV.getFolioVenta();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().statusPago = infoV.getStatus();
        int foLio = posicion;
        finish();
        startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this, venta_anterior_reimpirme.class));
    }


    //CONSULTA A BD PARA OBTENER TODAS LAS VENTAS
    private void TodaslasVentas() {
        db = conn.getReadableDatabase();
                productos.clear();

                Cursor cursorVentas =db.rawQuery("SELECT doc_folio, doc_total, doc_saldo, esta_estatus"+
                        " FROM estatus, documento WHERE documento.esta_fk=estatus.esta_id and (estatus.esta_estatus='Por pagar' OR estatus.esta_estatus='Pagado') AND documento.usr_fk='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario+"'", null);

                try {
                    if (cursorVentas != null) {
                        cursorVentas.moveToFirst();
                        int index = 0;

                        while (!cursorVentas.isAfterLast()) {
                            folioVenta = cursorVentas.getString(cursorVentas.getColumnIndex("doc_folio"));
                            totalVenta = Double.parseDouble(cursorVentas.getString(cursorVentas.getColumnIndex("doc_total")));
                            statusVenta = cursorVentas.getString(cursorVentas.getColumnIndex("esta_estatus"));

                            //Asignación de la información
                            VentasInfo veenta = new VentasInfo(folioVenta,totalVenta,statusVenta);
                            productos.add(veenta);

                            index++;
                            cursorVentas.moveToNext();
                        }

                        if (index != 0) {
                            recyclerView.setAdapter(adaptaVentsLista);
                        }else {
                            Toast.makeText(this, "Aún no hay ventas registradas", Toast.LENGTH_LONG).show();
                        }

                    }

                }catch(Exception e){
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

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasPrevias.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
