package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.commons.PrinterConnect;
import com.lzacatzontetlh.koonolmodulos.modelo.CancelacionDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewReporteVentas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

// Autor: Laura Zacatzontetl Hernandez
public class reporteVentas extends AppCompatActivity {

    private com.lzacatzontetlh.koonolmodulos.TemplatePDF templatePDF;
    EditText fechI,busqueda ,fechaFin;
    Button aceptarCancelacion , botonImprimirPDF,botonImprimirTicket;
    ImageButton botonFiltrar, botonBorrar;
    ImageButton btnFech,buscar,btnFechaFin;
    private int dia, mes, anio;
    ConexionSQLiteHelper conn;
    Spinner spinnerEstatus,spinnnerCaja;
    RecyclerView recyclerView;
    final Context context = this;
    TextView txte,caja,txtCajaRV, txtFecha,cajaCajero;
    private RecyclerView.LayoutManager layoutManager;
    final Ingresarsql sq = new Ingresarsql();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_ventas);




        btnFech = findViewById(R.id.ButtonAgregar);
        btnFechaFin = findViewById(R.id.ButtonAgregar3);
        aceptarCancelacion = findViewById(R.id.btnCancelar2);
        fechI = findViewById(R.id.editAsk);
        fechaFin = findViewById(R.id.editAsk3);
        busqueda = findViewById(R.id.busquedaCancelacion);
        buscar = findViewById(R.id.btnBuscar3cs);
        recyclerView = findViewById(R.id.vistalista1);
        spinnerEstatus = (Spinner) findViewById(R.id.estatus);
        txte = findViewById(R.id.txt2);
        txtCajaRV = findViewById(R.id.txt11);
        txtFecha = findViewById(R.id.txt3);
        caja = findViewById(R.id.Caja);
        spinnnerCaja = findViewById(R.id.caja);
        cajaCajero= findViewById(R.id.caja2);
        conn=new ConexionSQLiteHelper(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        botonImprimirPDF= findViewById(R.id.btnCancelar4);
        botonImprimirTicket= findViewById(R.id.btnConfirmar2);
        botonFiltrar= findViewById(R.id.filtrar);
        botonBorrar= findViewById(R.id.limpiarf);

        txte.setVisibility(View.INVISIBLE);
         caja.setVisibility(View.INVISIBLE);
        spinnnerCaja.setVisibility(View.INVISIBLE);
        txtCajaRV.setVisibility(View.INVISIBLE);
        txtFecha.setVisibility(View.VISIBLE);
        cajaCajero.setVisibility(View.INVISIBLE);

        //Globales.getInstance().id_usuario="29";

        //Globales.getInstance().fechas="Fecha:"+sq.fecha();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas="";
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon= "Estatus: Todos Caja: Todas";



        botonImprimirPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF1();
                }
            });


        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    botonFiltrar();
                    //
            }
        });

        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    limpiarPantalla();
            }
        });

        botonImprimirTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getAdapter()!=null) {
                    botonImprimirTicket.setEnabled(false);
                    imprimirTicket taskimp;
                    taskimp = new imprimirTicket();
                    taskimp.execute((Void) null);
                }
                else {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Seleccione al menos un filtro para generar un reporte.", Toast.LENGTH_SHORT).show();
                }
            }
        });
/*
        spinnerEstatus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn, android.view.View v, int posicion, long id) {
                        if(contador!=0) {
                            if(  Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                                busquedaEstatusPA();
                            }
                            else {
                                busquedaEstatusPC();
                            }
                        }
                        if (contador==0){
                            contador++;
                        }
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });
        spinnnerCaja.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn, android.view.View v, int posicion, long id) {
                        if(contador2!=0) {
                            consultarPorCaja();
                        }
                        if (contador2==0){
                            contador2++;
                        }
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        */

        cargarEstatus();
        //cargarTodos();
        cargarPD();

        btnFech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                anio = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fec="";
                        if (i<1960){
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "El año debe ser mayor a 1960", Toast.LENGTH_LONG).show();
                            fechI.setText("DD/MM/AAAA");
                        }else {
                            int mesActual = i1 + 1;
                            String diaFormateado = (i2 < 10) ? "0" + String.valueOf(i2) : String.valueOf(i2);
                            String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                            fechI.setText(diaFormateado + "/" + mesFormateado + "/" + i);
                            fec=diaFormateado+"/"+mesFormateado+"/"+i;
                        }
                        String fecha = sq.fecha();
                        String fechaa= compararFechasConDate(fec,fecha);
                        if(fechaa.equals("La Fecha 1 es Mayor")){
                            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
                            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
                            alerta.setMessage("La fecha debe ser pasada.")
                                    .setCancelable(true)
                                    .setCustomTitle(vista)
                                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alerta.show();
                            fechI.setText("DD/MM/AAAA");
                        }
                    }
                },anio, mes, dia);
                datePickerDialog.show();
            }
        });

        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                anio = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fec="";
                        if (i<1960){
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "El año debe ser mayor a 1960", Toast.LENGTH_LONG).show();
                            fechaFin.setText("DD/MM/AAAA");
                        }else {
                            int mesActual = i1 + 1;
                            String diaFormateado = (i2 < 10) ? "0" + String.valueOf(i2) : String.valueOf(i2);
                            String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                            fechaFin.setText(diaFormateado + "/" + mesFormateado + "/" + i);
                            fec=diaFormateado+"/"+mesFormateado+"/"+i;
                        }
                        String fecha = sq.fecha();
                        String fechaa= compararFechasConDate(fec,fecha);
                        if(fechaa.equals("La Fecha 1 es Mayor")){
                            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
                            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
                            alerta.setMessage("La fecha debe ser pasada.")
                                    .setCancelable(true)
                                    .setCustomTitle(vista)
                                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alerta.show();
                            fechaFin.setText("DD/MM/AAAA");
                        }
                    }
                },anio, mes, dia);
                datePickerDialog.show();
            }
        });
    }


    private void busquedaPorFechaPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String query = fechI.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha= '"+query+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaPorFechaFinPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String query = fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha= '"+query+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaPorFechaPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String query = fechI.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha= '"+query+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                     //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaPorFechaFinPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String query = fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha= '"+query+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  cargarEstatus(){
        SQLiteDatabase db = conn.getReadableDatabase();
        List<String> listaDatos = new ArrayList<String>();
        listaDatos.clear();
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        sq.consultaestatus(getApplicationContext());
        sq.consultaestatusCancelado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        int cancelada= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusCancelado;
        int enProceso= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusLau;
        listaDatos.add("Seleccione");
        listaDatos.add("Todos");
        Cursor cursor2 =db.rawQuery("SELECT esta_estatus  from estatus  WHERE esta_id='"+porPagar+"' OR esta_id='"+pagado+"' OR esta_id='"+cancelada+"' OR esta_id='"+enProceso+"'", null);
        //Cursor cursor2 =db.rawQuery("SELECT esta_estatus  from estatus", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    listaDatos.add(nombre);
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaDatos);
                    spinnerEstatus.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    listaDatos.clear();
                    spinnerEstatus.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarTodos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk FROM estatus, documento WHERE documento.esta_fk=estatus.esta_id", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                     //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarTodosParaAdminsitrador() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        sq.consultarIdAdministrador(getApplicationContext());
        int rol = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolAdministrador;
        String fecha=sq.fecha();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk, usr_fk ,rol_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id INNER JOIN usuario ON documento.usr_fk=usuario.usr_id INNER JOIN rol ON usuario.rol_fk=rol.rol_id where usuario.rol_fk="+rol+" and doc_fecha='"+fecha+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id  where doc_fecha='"+fecha+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                  //  String caja= "h";ree
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    txtCajaRV.setVisibility(View.VISIBLE);


                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarTodosParaCajero() {
        txte.setVisibility(View.VISIBLE);
        txtCajaRV.setVisibility(View.INVISIBLE);

        cajaCajero.setVisibility(View.VISIBLE);
        caja.setVisibility(View.VISIBLE);

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        sq.consultarIdAdministrador(getApplicationContext());
        int rol = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolCajero;
        String fecha=sq.fecha();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String caja2= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;

        cajaCajero.setText(caja2);

        //Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk, usr_fk ,rol_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id INNER JOIN usuario ON documento.usr_fk=usuario.usr_id INNER JOIN rol ON usuario.rol_fk=rol.rol_id where usuario.rol_fk="+rol+" and doc_fecha='"+fecha+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total,esta_estatus,documento.cja_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id where  doc_fecha='"+fecha+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    //String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  cargarPD(){
        SQLiteDatabase db = conn.getReadableDatabase();
        int nombre=0;
        String usu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
     //   Cursor cursor2 =db.rawQuery("SELECT rol_fk from usuario inner join rol ON usuario.rol_fk=rol.rol_id WHERE usr_id='"+usu+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT cfg_idRol from configuracion  WHERE cfg_idUsr='"+usu+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    nombre = cursor2.getInt(cursor2.getColumnIndex("cfg_idRol"));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    sq.consultarIdAdministrador(getApplicationContext());
                    int rol = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolAdministrador;
                    if(nombre==rol){

                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario="Administrador";
                        txte.setVisibility(View.VISIBLE);
                        caja.setVisibility(View.VISIBLE);
                        spinnnerCaja.setVisibility(View.VISIBLE);
                        cargarCajas();
                        cargarTodosParaAdminsitrador();
                    }
                    sq.consultarIdSuperAdmin(getApplicationContext());
                    int rol2 = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolSuperAdmi;
                    if(nombre==rol2){
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario="Administrador";
                        txte.setVisibility(View.VISIBLE);
                        caja.setVisibility(View.VISIBLE);
                        spinnnerCaja.setVisibility(View.VISIBLE);
                        cargarCajas();
                        cargarTodosParaAdminsitrador();
                    }
                    sq.consultaridCajero(getApplicationContext());
                    int rol3 = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolCajero;
                    if(nombre==rol3) {
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario="Cajero";
                        cargarTodosParaCajero();
                    }


                }
                else
                {
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario="Cajero";
                    cargarTodosParaCajero();
                    //Toast.makeText(reporteVentas.this,"No hay concidencias2",Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.VentaMenu.class);
        startActivity(intencion2);
        finish();
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
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
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
        sq.consultarNombreUsuario(getApplicationContext());
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);
        return true;
    }


    private void busquedaEstatusPA() {

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //String query = fechI.getText().toString();
        //Cursor cursor2 =db.rawQuery("SELECT doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE folio.cja_fk='"+caja+"' and esta_estatus='"+estatus+"' and doc_fecha= '"+query+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  esta_estatus='"+estatus+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    private void busquedaEstatusPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //String query = fechI.getText().toString();
        //Cursor cursor2 =db.rawQuery("SELECT doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE folio.cja_fk='"+caja+"' and esta_estatus='"+estatus+"' and doc_fecha= '"+query+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  esta_estatus='"+estatus+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("documento.cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(estatus.equals("Seleccione")){
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"Seleccione una opción válida22.", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }
                    else {
                        if(estatus.equals("Todos")){

                            // cargarPD();
                            //cargarEstatusTodosPC();
                        }
                        else {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    private void cargarEstatusTodosPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        sq.consultarIdAdministrador(getApplicationContext());
        int rol = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolAdministrador;
        String fecha=sq.fecha();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk, usr_fk ,rol_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id INNER JOIN usuario ON documento.usr_fk=usuario.usr_id INNER JOIN rol ON usuario.rol_fk=rol.rol_id where usuario.rol_fk="+rol+" and doc_fecha='"+fecha+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id  where  est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    //  String caja= "h";ree
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    txtCajaRV.setVisibility(View.VISIBLE);


                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarEstatusTodosPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        sq.consultarIdAdministrador(getApplicationContext());
        int rol = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idRolAdministrador;
        String fecha=sq.fecha();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total esta_estatus,documento.cja_fk, usr_fk ,rol_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id INNER JOIN usuario ON documento.usr_fk=usuario.usr_id INNER JOIN rol ON usuario.rol_fk=rol.rol_id where usuario.rol_fk="+rol+" and doc_fecha='"+fecha+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus,documento.cja_fk FROM documento INNER JOIN estatus ON  documento.esta_fk=estatus.esta_id  where  est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    String caja= "";
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {



                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public String compararFechasConDate(String fecha1, String fechaActual) {
        System.out.println("Parametro String Fecha 1 = "+fecha1 + "Parametro String fechaActual = "+fechaActual);
        String resultado="";
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            if ( fechaDate1.before(fechaDate2) ){
                resultado= "La Fecha 1 es menor";
            }else{
                if ( fechaDate2.before(fechaDate1) ){
                    resultado= "La Fecha 1 es Mayor";
                }else{
                    resultado= "Las Fechas Son iguales";
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  "+e.getMessage());
        }
        return resultado;
    }



    private void busquedaPorFechaPADeHasta() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                     //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaPorFechaPCDeHasta() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }



    public  void  cargarCajas(){
        SQLiteDatabase db = conn.getReadableDatabase();
        List<String> listaDatos = new ArrayList<String>();
        listaDatos.clear();
        listaDatos.add("Seleccione");
        listaDatos.add("Todas");
        /*listaDatos.add("0");
        listaDatos.add("1");
        listaDatos.add("2");*/
        //listaDatos.add("Todos");
       // String usu=  Globales.getInstance().id_usuario;
       Cursor cursor2 =db.rawQuery("SELECT DISTINCT cfg_nomCja,cfg_idCja from  configuracion", null);
     //   Cursor cursor2 =db.rawQuery("SELECT cfg_id from  configuracion where cfg_idUsr="+usu, null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cfg_idCja")));
                    listaDatos.add(nombre);
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaDatos);
                    spinnnerCaja.setAdapter(adapter);

                }
                else
                {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaDatos);
                    spinnnerCaja.setAdapter(adapter);

                   /* Toast.makeText(reporteVentas.this,"No hay concidencias.",Toast.LENGTH_SHORT).show();
                    listaDatos.clear();
                    spinnnerCaja.setAdapter(null);*/
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void consultarPorCaja() {
        String caja="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
     //   String caja = spinnnerCaja.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
       Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  documento.cja_fk='"+caja+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                   // String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja2="";
                  //  String caja2= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja2,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtCajaRV.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    txte.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(caja.equals("Seleccione")){
                        txtCajaRV.setVisibility(View.INVISIBLE);
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"Seleccione una opción válida666.", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }
                    else {
                            txtCajaRV.setVisibility(View.INVISIBLE);
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void consultarTodasCajas() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                  //  String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                     //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    txte.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    public void botonFiltrar(){


        String f=fechaFin.getText().toString();
        String fi=fechI.getText().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String caja="";// = spinnnerCaja.getSelectedItem().toString();
        int fechaInicio=0,fechaFin=0, estado=0,cajaContador=0;

        /////////////variables para el ticket
        String estadoT = "", estatusT = spinnerEstatus.getSelectedItem().toString();
        if (estatusT.equals("Seleccione")) {

            estadoT = "  Estatus: Todos";
        } else {
            estadoT = "  Estatus: " + spinnerEstatus.getSelectedItem().toString();
        }


        String cajT = "";
        //String ca = spinnnerCaja.getSelectedItem().toString();
        String caT = ""; //= spinnnerCaja.getSelectedItem().toString();
        String cT = cajaCajero.getText().toString();

        if (cT.equals("")) {
            caT = spinnnerCaja.getSelectedItem().toString();
        } else {
            cajT = "  Caja: " + cajaCajero.getText().toString();
            spinnnerCaja.setSelection(0);
        }

        if (caT.equals("Seleccione") || caT.equals("")) {
            //no ha seleccionado ningun estadoT
            //cajT = "  Caja: Todas";


            if(cT.equals("")){
                cajT = "       Caja: Todas";
            }
            else {
                cajT = "       Caja: " + cajaCajero.getText().toString();
            }
        } else {
            cajT = "  Caja: " + spinnnerCaja.getSelectedItem().toString();
        }

        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon= estadoT+ cajT;
        ///////////////











        String caj="";
        //String ca = spinnnerCaja.getSelectedItem().toString();
        String ca=""; //= spinnnerCaja.getSelectedItem().toString();
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja=cajaCajero.getText().toString();
            //spinnnerCaja.setSelection(0);
        }


        if(fi.equals("") || fi.equals("DD/MM/AAAA")){
             fechaInicio=0;//cero vacio
        }
        else {
             fechaInicio=1;
        }

        if(f.equals("") || f.equals("DD/MM/AAAA")){
            fechaFin=0;
        }
        else{
            fechaFin=1;
        }
        if(estatus.equals("Seleccione")){
            estado=0;
        }
        else {
            estado=1;
            if(estatus.equals("Todos")){
                estado=2;
            }
        }

        if (caja.equals("Seleccione")) {
            cajaContador=0;
        } else
        {
            cajaContador=1;
            if (caja.equals("Todas")) {
                cajaContador=2;
            }
            //tiene todos los filtros llenos
           // todosLosFiltrosPA();
        }





        txtFecha.setVisibility(View.VISIBLE);
        String variable= fechaInicio+""+fechaFin+""+estado+""+cajaContador+"";
        int var= Integer.parseInt(variable);
        switch (variable){
            case "1111":
                //lleno todos los campos
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    todosLosFiltrosPA();
                }
                else {
                    todosLosFiltrosPC();
                }
                break;
            case "0111":
                //solo falta seleccionar la fecha inicio
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    faltoSFIPA();
                }
                else {
                    faltoSFIPC();
                }
             break;
            case "0011":
                //falta seleccionar ambas fechas
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    estatusYCajaPA();
                }
                else {
                    estatusYCajaPC();
                }
                break;
            case  "0001":
                //no selecciono las fechas ni el estado solo la caja
                consultarPorCaja();
                break;
            case "0000":
                //todos los campos vacios
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"Seleccione al menos un filtro.", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(null);
                break;
            case "1121":
                //todos llenos pero estatus esta en todos

                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){

                    todosLosFiltrosSeleccionoEstaTodos();
                }
                else {
                    todosLosFiltrosSeleccionoEstaTodosPC();
                }

                break;
            case "1120":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    ETSCPA();
                }
                else {
                    ETSCPC();
                }
                break;
            case "1112":

                todosLosFiltrosSeleccionoCajaTodos();
                break;
            case "1122":
                //todos llenos pero estatus y  caja esta en todos
                todosLosFiltrosSeleccionoCajaestatusTodos();
                break;
            case "0020":
                //filtro  solo por estatus en todos
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    cargarEstatusTodosPA();
                }
                else {
                    cargarEstatusTodosPC();
                }
                break;
            case "0002":
                //filtro  por caja en todos
                consultarTodasCajas();
                break;
            case "0022":
                //selecciono estatus y caja en todos
                dosdosPÄ();
                break;
            case "1110":
                //lleno fecha inicio fecha fin y esatus  la caja esta vacia
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    unoUnoUnoCero();
                }
                else {
                    unoUnoUnoCeroPC();
                }
                break;
            case "1100":
                //lleno ambas fechas y  estado y  caja estan vacias
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    busquedaPorFechaPADeHasta();
                }
                else {
                    busquedaPorFechaPCDeHasta();
                }
                break;
            case "1000":
                //solo filtro por la fecha de hoy
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    busquedaPorFechaPA();
                }
                else {
                    busquedaPorFechaPC();
                }
                break;
            case "0100":
                //seleeciono solo fecha fin
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    busquedaPorFechaFinPA();
                }
                else {
                    busquedaPorFechaFinPC();
                }
                break;
            case "0010":
                //selecciono solo  el estatus
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    busquedaEstatusPA();
                }
                else {
                    busquedaEstatusPC();
                }
                break;
            case "1010":
                //selecciono fecha y estado
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    fechayEsPA();
                }
                else {
                    fechayEsPC();
                }

                break;

            case "0101":
                //selecciono fecha fimn y caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    fechaFinyCajaPA();
                }
                else {
                    fechaFinyCajaPC();
                }
                break;
            case "1001":
                //selecciono fecha fimn y caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    fechaycaja();
                }
                else {
                    fechaycajaPC();
                }
                break;
            case "0012":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    estatCjaTPA();
                }
                else {
                    estatCjaTPC();
                }
                break;
            case "0021":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    todosEyCPA();
                }
                else {
                    todosEyCPC();
                }
                break;
            case "0121":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    ceroUnoDosUno();
                }
                else {
                    ceroUnoDosUnoPC();
                }

                break;
            case "0112":
                ///no tiene fecha inicio  solo fin  estatus y caja en todos
                FFEcajaEnTodos();
                break;

            case "0120":
           //tiene fecha fin estado en todos y sin caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    estadTSCPA();
                }
                else {
                    estadTSCPC();
                }
                break;
            case "0110":
                //tiene fecha fin estado en todos y sin caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    EYFFPA();
                }

                else {
                    EYFFPC();
                }
                break;
            case "0122":
                ///no tiene fecha inicio  solo fin  estatus y caja en todos
                FFETcajaEnTodos();
                break;
            case "1011":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    unoCeroUnoUnoPA();
                }

                else {
                    unoCeroUnoUnoPC();
                }
                break;
            case "1012":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    unoCeroUnoDosPA();
                }

                else {
                    unoCeroUnoDosPC();
                }
                break;

            case "1021":
                //fecha estado en todos y caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    uCUUPA();
                }

                else {
                    uCUUPC();
                }
                break;

            case "1022":
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    busquedaPorFechaPA();
                }

                break;
            case "1101":
                //fecha estado en todos y caja
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    todosLosFiltrosSEPA();
                }

                else {
                    todosLosFiltrosSEPC();
                }
                break;
            case "1020":
                //fecha estado en todos
                if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
                    uCUUPA2();
                }

                else {
                   uCUUPC2();
                }
                break;
        }

    }

    private void FFETcajaEnTodos() {
        ///no tiene fecha inicio  solo fin  estatus y caja en todos 0112
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaFin2= fechaFin.getText().toString();
       // String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                   // String doc_estatus="";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }
    private void todosLosFiltrosSEPA() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
       // String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosSEPC() {

        String c=cajaCajero.getText().toString();


        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
       // String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosPA() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    /*Toast.makeText(reporteVentas.this,"No hay concidencias.",Toast.LENGTH_SHORT).show();
                    Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);*/

                    if(caja3.equals("Todas")){
                        //si todas pero  estatus es diferente de todos
                        if(estatus.equals("Todos")){
                            //todosLosFiltrosSeleccionoCajaestatusTodos();
                        }
                        else{
                            //todosLosFiltrosSeleccionoCajaTodos();
                        }
                        //si todaas y  estatus   todos


                    }
                    else {
                        if (estatus.equals("Todos")) {
                           // txtFecha.setVisibility(View.VISIBLE);
                           // todosLosFiltrosSeleccionoEstaTodos();
                        } else {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosPC() {

        String c=cajaCajero.getText().toString();


        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {


                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoCeroUnoDosPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoCeroUnoDosPC() {

        String c=cajaCajero.getText().toString();

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+c+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();


                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void uCUUPA2() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void uCUUPC2() {
        String c=cajaCajero.getText().toString();

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }



    private void uCUUPA() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+caja3+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void uCUUPC() {
        String c=cajaCajero.getText().toString();

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoCeroUnoUnoPA() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoCeroUnoUnoPC() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }

        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"'  and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void faltoSFIPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        ///String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus= "";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void faltoSFIPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaFin2= fechaFin.getText().toString();
        //String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("" +
                "SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus="";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosSeleccionoEstaTodos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
        //String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosSeleccionoEstaTodosPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();

        //String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String caja3 = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    //txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void ETSCPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void ETSCPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
         //String caja3 = spinnnerCaja.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;

        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                   // String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosSeleccionoCajaTodos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosLosFiltrosSeleccionoCajaestatusTodos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        //String caja3 = spinnnerCaja.getSelectedItem().toString();
       // String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void dosdosPÄ() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
     /*   String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();*/
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txte.setVisibility(View.VISIBLE);
                    txtCajaRV.setVisibility(View.VISIBLE);

                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
                    recyclerView.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void estatusYCajaPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        //String fechaInicio = fechI.getText().toString();
        //String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                   // String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus="";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                   txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void estatusYCajaPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"' and documento.cja_fk='"+c+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String doc_estatus="";
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                     txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoUnoUnoCero() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        //String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void unoUnoUnoCeroPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        //String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha>='"+fechaInicio+"' and doc_fecha<='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"' and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechayEsPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();

        //String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"' and est_fk='"+est+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= "";//String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtFecha.setVisibility(View.VISIBLE);
                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechayEsPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"' and est_fk='"+est+"'  and esta_estatus='"+estatus+"'  and documento.cja_fk='"+c+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= "";//String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {


                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechaFinyCajaPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
       // String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
       /// String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechaFinyCajaPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaFin2= fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                   // String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechaycaja() {
        String caja3="";
        String c=cajaCajero.getText().toString();
        if(c.equals("")){
            caja3 = spinnnerCaja.getSelectedItem().toString();
        }
        else {
            caja3=cajaCajero.getText().toString();
        }


        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
       // String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
     //   String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void fechaycajaPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaInicio = fechI.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c=cajaCajero.getText().toString();
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE doc_fecha='"+fechaInicio+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtFecha.setVisibility(View.INVISIBLE);
                    txtCajaRV.setVisibility(View.INVISIBLE);

                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  limpiarPantalla(){
        fechaFin.setText("DD/MM/AAAA");
        fechI.setText("DD/MM/AAAA");
        String ca=""; //= spinnnerCaja.getSelectedItem().toString();
        cargarEstatus();
        String c=cajaCajero.getText().toString();

        if(  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().tipoDeUsuario.equals("Administrador")){
            cargarCajas();
        }
        recyclerView.setAdapter(null);
       /* else {
            String idu=  Globales.getInstance().id_usuario;
            sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
            String caja2= Globales.getInstance().idCajaLau;
            cajaCajero.setText(caja2);
        }*/

    }

    private void estatCjaTPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
      //  String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void estatCjaTPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  est_fk='"+est+"'   and esta_estatus='"+estatus+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus="";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosEyCPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
       // String fechaInicio = fechI.getText().toString();
       // String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
      //  String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE est_fk='"+est+"' and documento.cja_fk='"+caja3+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    //txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void todosEyCPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE est_fk='"+est+"' and documento.cja_fk='"+c+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void ceroUnoDosUno() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
      //  String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
        String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+caja3+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                        }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void ceroUnoDosUnoPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaFin2= fechaFin.getText().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                    //String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    txtCajaRV.setVisibility(View.INVISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txte.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(null);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void FFEcajaEnTodos() {
        ///no tiene fecha inicio  solo fin  estatus y caja en todos 0112
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        String fechaFin2= fechaFin.getText().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    //String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_estatus="";
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void estadTSCPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
       // String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
      //  String caja3 = spinnnerCaja.getSelectedItem().toString();
      //  String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();

                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void estadTSCPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        //String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
     //   String caja3 = spinnnerCaja.getSelectedItem().toString();
      //  String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txte.setVisibility(View.VISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void EYFFPA() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
       // String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
      //  String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"'   and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus="";// String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    //String caja="";
                    String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                    txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void EYFFPC() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.clear();
        //String fechaInicio = fechI.getText().toString();
        String fechaFin2= fechaFin.getText().toString();
       // String caja3 = spinnnerCaja.getSelectedItem().toString();
        String estatus = spinnerEstatus.getSelectedItem().toString();
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        String c= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idCajaLau;

        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total ,esta_estatus,documento.cja_fk from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  doc_fecha='"+fechaFin2+"' and est_fk='"+est+"' and documento.cja_fk='"+c+"'  and esta_estatus='"+estatus+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_hora")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_folio")));
                    String doc_fecha= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fecha")));
                    String doc_estatus= "";//String.valueOf( cursor2.getString(cursor2.getColumnIndex("esta_estatus")));
                    String doc_total= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_total")));
                    String caja="";
                 //   String caja= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cja_fk")));

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {

                   // txtCajaRV.setVisibility(View.VISIBLE);
                    txte.setVisibility(View.INVISIBLE);
                    final RecyclerViewReporteVentas adaptador = new RecyclerViewReporteVentas((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV);
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Datos cargados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                            txte.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(null);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "No hay concidencias.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void generarPDF1() {
        if(recyclerView.getAdapter()!=null){
            /////////////////////////////////////////////////genera el pdf
            ActivityCompat.requestPermissions(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            templatePDF = new com.lzacatzontetlh.koonolmodulos.TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            templatePDF.addTitles("koonolmodulos", "Reporte de Ventas", sq.fecha());
            // String longText = "El propósito de la visita de campo es confimar y corroborar las caracteristicas y estado de la garantía o el inmueble.";
            //  templatePDF.addParagraph(longText);

            String encabezado = "";
            String f = fechaFin.getText().toString();
            String fi = fechI.getText().toString();
            if (fi.equals("") || fi.equals("DD/MM/AAAA")) {
                //esta vacia
                encabezado = "  Fecha: " + sq.fecha().toString();
            } else {
                if (f.equals("") || f.equals("DD/MM/AAAA")) {
                    //no tiene fecha fin
                    encabezado = "  Fecha: " + fechI.getText();
                } else {
                    encabezado = "  Fecha inicio: " + fechI.getText().toString() + "     Fecha fin: " + fechaFin.getText().toString();
                }

            }


            templatePDF.addParagraph(encabezado);
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas=encabezado;

            String estado = "", estatus = spinnerEstatus.getSelectedItem().toString();
            if (estatus.equals("Seleccione")) {
                //no ha seleccionado ningun estado
                estado = "  Estatus: Todos";
            } else {
                estado = "  Estatus: " + spinnerEstatus.getSelectedItem().toString();
            }


            String caj = "";
            //String ca = spinnnerCaja.getSelectedItem().toString();
            String ca = ""; //= spinnnerCaja.getSelectedItem().toString();
            String c = cajaCajero.getText().toString();

            if (c.equals("")) {
                ca = spinnnerCaja.getSelectedItem().toString();
            } else {
                caj = "       Caja: " + cajaCajero.getText().toString();
                spinnnerCaja.setSelection(0);
            }

            if (ca.equals("Seleccione") || ca.equals("")) {
                //no ha seleccionado ningun estado
                if(c.equals("")){
                    caj = "       Caja: Todas";
                }
                else {
                    caj = "       Caja: " + cajaCajero.getText().toString();
                }

            } else {
                caj = "       Caja: " + spinnnerCaja.getSelectedItem().toString();
            }


            String renglon = estado + caj;
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon=renglon;

            templatePDF.addParagraph(renglon);
            templatePDF.addParagraph(" ");


            int fun=0;

            //cadena.trim().length()==0

            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().equals("") && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().trim().length()!=0){
                //////////////////////////////////////////si en el rV nose muestrra la caja
                ArrayList<String[]> rows = new ArrayList();
                int co=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.size();
                for (int i=0; i < co; i++ ) {
                    rows.add(new String[]{ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFolioVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFechav(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getHoraDC(),"$ "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getTotalVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getEstatus()});
                }
                String[] header = {"Folio"," Fecha", "Hora","Total" , "Estatus"};
                templatePDF.createTable(header, rows);
                fun=1;
            }

            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().equals("")  && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().trim().length()!=0) {
                ArrayList<String[]> rows = new ArrayList();
                int co=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.size();
                for (int i=0; i < co; i++ ) {
                    rows.add(new String[]{ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFolioVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFechav(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getHoraDC(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getCaja(),"$ "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getTotalVenta()});
                }
                String[] header = {"Folio"," Fecha", "Hora", "Caja","Total" };
                templatePDF.createTable(header, rows);
                fun=1;
            }

            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().equals("") && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().equals("")) {
                ArrayList<String[]> rows = new ArrayList();
                int co=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.size();
                for (int i=0; i < co; i++ ) {
                    rows.add(new String[]{ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFolioVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFechav(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getHoraDC(),"$ "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getTotalVenta()});
                }
                String[] header = {"Folio"," Fecha", "Hora","Total" };
                templatePDF.createTable(header, rows);
                fun=1;
            }

            if(fun==0){
                //si estan todos los campós visibles
                //////////////////////////////////////////cre la lista que agrega los datos a la table
                ArrayList<String[]> rows = new ArrayList();
                int co = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.size();
                for (int i = 0; i < co; i++) {
                    // String fila = Globales.getInstance().listaRV.get(i).getFolioVenta()+ " "+ Globales.getInstance().listaRV.get(i).getFechav()+" "+Globales.getInstance().listaRV.get(i).getHoraDC()+ " "+ Globales.getInstance().listaRV.get(i).getCaja()+" "+Globales.getInstance().listaRV.get(i).getTotalVenta()+ " "+ Globales.getInstance().listaRV.get(i).getEstatus();
                    //templatePDF.addParagraph(fila);
                    rows.add(new String[]{com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFolioVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getFechav(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getHoraDC(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getCaja(), "$ " + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getTotalVenta(), com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(i).getEstatus()});
                }
                String[] header = {"Folio", " Fecha", "Hora", "Caja", "Total", "Estatus"};
                templatePDF.createTable(header, rows);
                //////////////////////////////////////// ////////////////////////////////////////

            }

            templatePDF.addParagraph(" ");

            templatePDF.closeDocument();
            templatePDF.viewPDF();


        }
        else {
            Toast.makeText(com.lzacatzontetlh.koonolmodulos.reporteVentas.this, "Seleccione al menos un filtro para generar un reporte.", Toast.LENGTH_SHORT).show();
        }

    }



    //METODO PARA LA IMPRESION
    public class
    imprimirTicket extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;
        imprimirTicket() {
            mContext =  com.lzacatzontetlh.koonolmodulos.reporteVentas.this;
            impresora = new PrinterConnect(mContext);
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
            int fun=0;

            if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst.trim().length()==0){
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst="";
            }

            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().trim().equals("") && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().trim().length()!=0){
                //sin caja
                ticketSinCaja();
                fun=1;

            }
            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().equals("")  && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().trim().length()!=0) {
                //sin estatus
                ticketSinEstado();
                fun=1;
            }
            if( com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getEstatus().equals("") && com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV.get(0).getCaja().equals("")) {
                //sin estatus y sin caja
                ticketSinCajaYSinEstado();
                fun=1;
            }
            if(fun==0){
                ticketConTodosDatos();
                fun=1;
            }


            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.dialog_imprimir, null);
            final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder( com.lzacatzontetlh.koonolmodulos.reporteVentas.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //habilitar boton imprimir ticket
                    botonImprimirTicket.setEnabled(true);
                    limpiarPantalla();
                   //finish();
                    //startActivity(new Intent( reporteVentas.this, reporteVentas.class));
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
                    botonFiltrar.setEnabled(true);
                }
            });
            android.support.v7.app.AlertDialog alert = builder2.create();
            alert.show();

        }


        public  void  ticketConTodosDatos(){
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                String encabezado="\n Caja       Folio      Estatus\n ";
                String encabezado2="  Fecha        Hora    Total\n";
                for (CancelacionDatos temp :  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV) {
                    cadenotaDatos = cadenotaDatos + "\nCaja " +temp.getCaja()+ "     " + temp.getFolioVenta() +"     " + temp.getEstatus() +"\n" + temp.getFechav() + "    "+temp.getHoraDC() +"   $"+ temp.getTotalVenta() ;
                }
                impresora.conecta();
                String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
                sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst+" \n" + "Reporte de Ventas \n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas+"\n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon+"\n"+
                        encabezado+encabezado2+ cadenotaDatos+"\n \n -------------------------------\n"+"Generado  \n"+"Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+" \n" + " \n"+"********************************";


                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }
        public void  ticketSinCaja(){
            //FolioFechaHoraTotalEstatus
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                String encabezado="\n  Folio      Estatus\n ";
                String encabezado2="  Fecha        Hora    Total\n";
                for (CancelacionDatos temp :  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV) {
                    cadenotaDatos = cadenotaDatos + "\n " + temp.getFolioVenta() +"     " + temp.getEstatus() +"\n" + temp.getFechav() + "    "+temp.getHoraDC() +"   $"+ temp.getTotalVenta() ;
                }
                impresora.conecta();
                String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
                sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst+" \n" + "Reporte de Ventas \n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas+"\n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon+"\n"+
                        encabezado+encabezado2+ cadenotaDatos+"\n \n -------------------------------\n"+"Generado  \n"+"Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+" \n" + " \n"+"********************************";


                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }

        public void  ticketSinCajaYSinEstado(){
            //FolioFechaHoraTotalEstatus
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                String encabezado="\n Folio\n ";
                String encabezado2="  Fecha        Hora    Total\n";
                for (CancelacionDatos temp :  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV) {
                    cadenotaDatos = cadenotaDatos + "\n " + temp.getFolioVenta() +"     " +"\n" + temp.getFechav() + "    "+temp.getHoraDC() +"   $"+ temp.getTotalVenta() ;
                }
                impresora.conecta();
                String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
                sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst+" \n" + "Reporte de Ventas \n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas+"\n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon+"\n"+
                        encabezado+encabezado2+ cadenotaDatos+"\n \n -------------------------------\n"+"Generado  \n"+"Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+" \n" + " \n"+"********************************";


                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }

        public  void  ticketSinEstado(){
            String cadenota = "";
            String cadenotaDatos = "";
            try {
                String encabezado="\n Caja       Folio\n ";
                String encabezado2="  Fecha        Hora    Total\n";
                for (CancelacionDatos temp :  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaRV) {
                    cadenotaDatos = cadenotaDatos + "\nCaja " +temp.getCaja()+ "     " + temp.getFolioVenta() +"     " + "\n" + temp.getFechav() + "    "+temp.getHoraDC() +"   $"+ temp.getTotalVenta() ;
                }
                impresora.conecta();
                String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
                sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nomEst+" \n" + "Reporte de Ventas \n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechas+"\n" + com.lzacatzontetlh.koonolmodulos.Globales.getInstance().renglon+"\n"+
                        encabezado+encabezado2+ cadenotaDatos+"\n \n -------------------------------\n"+"Generado  \n"+"Fecha:" + sq.fecha() +"   Hora:"+sq.hora()+" \n" + " \n"+"********************************";


                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
            } catch (android.database.SQLException e) {

            }
        }

    }



}

// Autor: Laura Zacatzontetl Hernandez



