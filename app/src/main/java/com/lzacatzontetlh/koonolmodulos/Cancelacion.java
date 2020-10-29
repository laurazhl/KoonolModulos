package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.CancelacionDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewDatosCancelacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Autor: Laura Zacatzontetl Hernandez
public class Cancelacion extends AppCompatActivity {

    EditText fechI,busqueda;
    Button aceptarCancelacion;
    ImageButton btnFech,buscar;
    private int dia, mes, anio;
    ConexionSQLiteHelper conn;
    int contador=0;
    Spinner spinnerEstatus;
     RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    final Ingresarsql sq = new Ingresarsql();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelacion);
        btnFech =  findViewById(R.id.ButtonAgregar);
        aceptarCancelacion = findViewById(R.id.btnCancelar2);
        fechI =  findViewById(R.id.editAsk);
        busqueda =  findViewById(R.id.busquedaCancelacion);
        buscar = findViewById(R.id.btnBuscar3cs);
        recyclerView =  findViewById(R.id.vistalista1);
        spinnerEstatus= (Spinner)findViewById(R.id.estatus);

        conn=new ConexionSQLiteHelper(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cargarEstatus();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(busqueda.getWindowToken(), 0);



       //cargarTodosLasCancelciones();
       cargarTodosFolios();
        aceptarCancelacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(getApplicationContext(),reporteVentas.class));
               startActivity(new Intent(getApplicationContext(),VentaMenu.class));
            }
        });


        btnFech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                anio = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(com.lzacatzontetlh.koonolmodulos.Cancelacion.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String fec="";
                        if (i<1960){
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this, "El año debe ser mayor a 1960", Toast.LENGTH_LONG).show();
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
                           // Toast.makeText(Cancelacion.this, "La fecha debe ser pasada", Toast.LENGTH_LONG).show();
                            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.Cancelacion.this);
                            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Cancelacion.this);
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

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // recyclerView.setAdapter(null);
                String query = busqueda.getText().toString();
                if(query.equals("")){
                    cargarTodosFolios();
                }
                else {
                    busquedaPorFolio();
                }
            }
        });



        fechI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              //  System.out.println("antes de q cambie el texto");
               // Toast.makeText(Cancelacion.this, "antes de q cambie el texto", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s.toString() + " " + start + " " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
               // Toast.makeText(Cancelacion.this, "despuss de q cambie el texto", Toast.LENGTH_LONG).show();
                busquedaPorFecha();

            }

        });
        busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // System.out.println("antes de q cambie el teto");
               // Toast.makeText(Cancelacion.this, "antes de q cambie el teto", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s.toString() + " " + start + " " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                cargarTodosFolios();
            }

        });


         spinnerEstatus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn, View v, int posicion, long id) {

                        if(contador!=0) {
                            //Toast.makeText(spn.getContext(), "Has seleccionado el valor: " + spn.getItemAtPosition(posicion).toString(), Toast.LENGTH_LONG).show();
                            busquedaEstatus();
                        }
                        if (contador==0){
                            contador++;
                        }

                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });




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




    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), VentaMenu.class);
        startActivity(intencion2);
        finish();
    }
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.Cancelacion.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Cancelacion.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sq.limpiarVariablesGlobales();
                            finish();
                            Intent intencion2 = new Intent(getApplication(), MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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


    private void busquedaPorFolio() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.clear();
        //String caja= Globales.getInstance().idCajaCancelacion;
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        String query = busqueda.getText().toString();


        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;

        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE (esta_fk='"+porPagar+"' OR esta_fk='"+pagado+"') and doc_folio LIKE '%"+query+"%' and est_fk='"+est+"'", null);
        //Cursor cursor2 =db.rawQuery("SELECT doc_folio, doc_fecha, doc_total from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio  WHERE folio.cja_fk='"+caja+"'", null);
        //Cursor cursor2 =db.rawQuery("SELECT doc_folio, doc_fecha, doc_total from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio"), null);

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
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    final RecyclerViewDatosCancelacion adaptador = new RecyclerViewDatosCancelacion((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int elemen=   recyclerView.getChildAdapterPosition(v);
                            String idDC= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getIdDC();
                            String hora= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getHoraDC();
                            String folioVenta= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFolioVenta();
                            String fechav=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFechav();
                            String estatus=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getEstatus();
                            String totalVenta=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getTotalVenta();
                            Intent intencion = new Intent(getApplication(), DetallesCancelacion.class);
                            intencion.putExtra(DetallesCancelacion.idDC, idDC);
                            intencion.putExtra(DetallesCancelacion.hora, hora);
                            intencion.putExtra(DetallesCancelacion.folioVenta, folioVenta);
                            intencion.putExtra(DetallesCancelacion.fechav, fechav);
                            intencion.putExtra(DetallesCancelacion.estatus, estatus);
                            intencion.putExtra(DetallesCancelacion.totalVenta, totalVenta);
                            finish();
                            startActivity(intencion);

                        }
                    });
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarTodosFolios() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.clear();
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;

        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;

        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus FROM estatus, documento WHERE documento.esta_fk=estatus.esta_id and (estatus.esta_id='"+porPagar+"' OR estatus.esta_id='"+pagado+"') and est_fk='"+est+"'", null);
       // Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus FROM estatus, documento WHERE documento.esta_fk=estatus.esta_id and (estatus.esta_id='"+porPagar+"' OR estatus.esta_id='"+pagado+"')", null);
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
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewDatosCancelacion adaptador = new RecyclerViewDatosCancelacion((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int elemen=   recyclerView.getChildAdapterPosition(v);
                            String idDC= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getIdDC();
                            String hora= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getHoraDC();
                            String folioVenta= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFolioVenta();
                            String fechav=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFechav();
                            String estatus=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getEstatus();
                            String totalVenta=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getTotalVenta();
                            Intent intencion = new Intent(getApplication(), DetallesCancelacion.class);
                            intencion.putExtra(DetallesCancelacion.idDC, idDC);
                            intencion.putExtra(DetallesCancelacion.hora, hora);
                            intencion.putExtra(DetallesCancelacion.folioVenta, folioVenta);
                            intencion.putExtra(DetallesCancelacion.fechav, fechav);
                            intencion.putExtra(DetallesCancelacion.estatus, estatus);
                            intencion.putExtra(DetallesCancelacion.totalVenta, totalVenta);
                            finish();
                            startActivity(intencion);
                        }
                    });
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
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
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        Cursor cursor2 =db.rawQuery("SELECT esta_estatus  from estatus  WHERE esta_id='"+porPagar+"' OR esta_id='"+pagado+"'", null);
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
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listaDatos.clear();
                    spinnerEstatus.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaPorFecha() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.clear();
        //String caja= Globales.getInstance().idCajaCancelacion;
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        String query = fechI.getText().toString();


        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;



        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE (esta_fk='"+porPagar+"' OR esta_fk='"+pagado+"') and doc_fecha= '"+query+"' and est_fk='"+est+"'", null);
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
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    final RecyclerViewDatosCancelacion adaptador = new RecyclerViewDatosCancelacion((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int elemen=   recyclerView.getChildAdapterPosition(v);
                            String idDC= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getIdDC();
                            String hora= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getHoraDC();
                            String folioVenta= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFolioVenta();
                            String fechav=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFechav();
                            String estatus=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getEstatus();
                            String totalVenta=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getTotalVenta();
                            Intent intencion = new Intent(getApplication(), DetallesCancelacion.class);
                            intencion.putExtra(DetallesCancelacion.idDC, idDC);
                            intencion.putExtra(DetallesCancelacion.hora, hora);
                            intencion.putExtra(DetallesCancelacion.folioVenta, folioVenta);
                            intencion.putExtra(DetallesCancelacion.fechav, fechav);
                            intencion.putExtra(DetallesCancelacion.estatus, estatus);
                            intencion.putExtra(DetallesCancelacion.totalVenta, totalVenta);
                            finish();
                            startActivity(intencion);

                        }
                    });
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"Datos cargados", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void busquedaEstatus() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.clear();
       // String caja= Globales.getInstance().idCajaCancelacion;
        String estatus = spinnerEstatus.getSelectedItem().toString();
        //String query = fechI.getText().toString();

        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        //Cursor cursor2 =db.rawQuery("SELECT doc_folio, doc_fecha, doc_total, esta_estatus from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE folio.cja_fk='"+caja+"' and esta_estatus='"+estatus+"' and doc_fecha= '"+query+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT doc_id,doc_hora,doc_folio, doc_fecha, doc_total, esta_estatus from documento INNER JOIN  folio ON documento.doc_folio = folio.fol_folio INNER JOIN estatus ON documento.esta_fk= estatus.esta_id  WHERE  esta_estatus='"+estatus+"' and est_fk='"+est+"'", null);

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
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.add(new CancelacionDatos(doc_id,doc_hora,doc_folio,doc_fecha,caja,doc_estatus, doc_total));
                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    final RecyclerViewDatosCancelacion adaptador = new RecyclerViewDatosCancelacion((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int elemen=   recyclerView.getChildAdapterPosition(v);
                            String idDC= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getIdDC();
                            String hora= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getHoraDC();
                            String folioVenta= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFolioVenta();
                            String fechav=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFechav();
                            String estatus=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getEstatus();
                            String totalVenta=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getTotalVenta();
                            Intent intencion = new Intent(getApplication(), DetallesCancelacion.class);
                            intencion.putExtra(DetallesCancelacion.idDC, idDC);
                            intencion.putExtra(DetallesCancelacion.hora, hora);
                            intencion.putExtra(DetallesCancelacion.folioVenta, folioVenta);
                            intencion.putExtra(DetallesCancelacion.fechav, fechav);
                            intencion.putExtra(DetallesCancelacion.estatus, estatus);
                            intencion.putExtra(DetallesCancelacion.totalVenta, totalVenta);
                            finish();
                            startActivity(intencion);

                        }
                    });
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"Datos cargados por estatus", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias.", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }



    private void cargarTodosLasCancelciones() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.clear();
        sq.consultaestatusPorPagar(getApplicationContext());
        sq.consultaestatusPagado(getApplicationContext());
        int porPagar= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPorPagar;
        int pagado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusPagado;
        Cursor cursor2 =db.rawQuery("SELECT * FROM folio where tpd_fk=2", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String doc_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("fol_folio")));
                    String doc_hora= String.valueOf( cursor2.getString(cursor2.getColumnIndex("tpd_fk")));
                    String doc_folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("tpd_fk")));
                    String caja="";
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.add(new CancelacionDatos(doc_id,doc_id,doc_id,doc_id,caja,doc_id, doc_id));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final RecyclerViewDatosCancelacion adaptador = new RecyclerViewDatosCancelacion((ArrayList<CancelacionDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int elemen=   recyclerView.getChildAdapterPosition(v);
                            String idDC= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getIdDC();
                            String hora= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getHoraDC();
                            String folioVenta= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFolioVenta();
                            String fechav=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getFechav();
                            String estatus=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getEstatus();
                            String totalVenta=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listaDatosCancelacion.get(elemen).getTotalVenta();
                            Intent intencion = new Intent(getApplication(), DetallesCancelacion.class);
                            intencion.putExtra(DetallesCancelacion.idDC, idDC);
                            intencion.putExtra(DetallesCancelacion.hora, hora);
                            intencion.putExtra(DetallesCancelacion.folioVenta, folioVenta);
                            intencion.putExtra(DetallesCancelacion.fechav, fechav);
                            intencion.putExtra(DetallesCancelacion.estatus, estatus);
                            intencion.putExtra(DetallesCancelacion.totalVenta, totalVenta);
                            finish();
                            startActivity(intencion);
                        }
                    });
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(adaptador);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.Cancelacion.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


}

// Autor: Laura Zacatzontetl Hernandez




