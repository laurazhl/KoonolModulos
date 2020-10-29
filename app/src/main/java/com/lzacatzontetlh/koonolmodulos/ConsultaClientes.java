package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ClientesDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewClientes;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ConsultaClientes extends AppCompatActivity {

    ImageButton buscar;
    private com.lzacatzontetlh.koonolmodulos.TemplatePDF templatePDF;
    EditText busqueda;
    Spinner select;
    Button regresar, botonImprimirPDF;
    ConexionSQLiteHelper conn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<ClientesDatos> listclientes = new ArrayList<ClientesDatos>();
    final Ingresarsql sq = new Ingresarsql();
    int contador=0;

    final RecyclerViewClientes adaptador = new RecyclerViewClientes((ArrayList<ClientesDatos>) listclientes);
    private final static String[] opciones = { "Nombre", "Correo electrónico","RFC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_clientes);
        conn=new ConexionSQLiteHelper(getApplicationContext());
        recyclerView =  findViewById(R.id.vistaUsuario);
        busqueda =  findViewById(R.id.busqueda);
        buscar = findViewById(R.id.btnBuscar);
        select = findViewById(R.id.select);
        regresar = findViewById(R.id.btnAceptar);
        botonImprimirPDF= findViewById(R.id.btnCancelar4);
        cargarTodosLosClientes();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        select.setAdapter(adapter);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = busqueda.getText().toString();
                if(query.equals("") || query.equals(" ")){
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"Ingrese un valor a buscar.", Toast.LENGTH_SHORT).show();
                }
                else {
                    busquedaClientes();
                }
            }
        });


        busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println(s.toString() + " " + start + " " + count + " " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s.toString() + " " + start + " " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                cargarTodosLosClientes();
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this, com.lzacatzontetlh.koonolmodulos.ClienteMenu.class));
            }
        });
        botonImprimirPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF1();
            }
        });
    }



    private void busquedaClientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        listclientes.clear();
        String seleccion = select.getSelectedItem().toString().trim();
        String query = busqueda.getText().toString();
        contador=1;
        if(seleccion.equals("Nombre")){
            Cursor cursor2 =db.rawQuery("select prs_tipo,prs_nombre, prs_telefono from persona WHERE prs_nombre LIKE '%"+query+"%'", null);
            try {
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int index = 0;
                    while (!cursor2.isAfterLast()) {
                        String tipoPersona= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_tipo")));
                        System.out.println("  tipoPersona   "+tipoPersona );
                        String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                        String telefono= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_telefono")));
                        if(tipoPersona.equals("1")){
                            tipoPersona="Moral";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                        if(tipoPersona.equals("0")){
                            tipoPersona="Física";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                    }
                    if (index != 0) {
                        funcionClick();
                        recyclerView.setAdapter(adaptador);
                        //recyclerView.setAdapter(new RecyclerViewClientes((ArrayList<ClientesDatos>) listclientes));
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"Datos cargados.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }
                }
            }catch(Exception e){
                Log.println(Log.ERROR,"",e.getMessage());
            }
        }
        if(seleccion.equals("Correo electrónico")){
            Cursor cursor2 =db.rawQuery("select prs_tipo,prs_nombre, prs_telefono from persona WHERE prs_email LIKE '%"+query+"%'", null);
            try {
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int index = 0;
                    while (!cursor2.isAfterLast()) {
                        String tipoPersona= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_tipo")));
                        String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                        String telefono= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_telefono")));
                        if(tipoPersona.equals("1")){
                            tipoPersona="Moral";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                        if(tipoPersona.equals("0")){
                            tipoPersona="Física";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                    }
                    if (index != 0) {
                        funcionClick();
                        recyclerView.setAdapter(adaptador);
                        //recyclerView.setAdapter(new RecyclerViewClientes((ArrayList<ClientesDatos>) listclientes));
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"Datos cargados.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }

                }
            }catch(Exception e){
                Log.println(Log.ERROR,"",e.getMessage());
            }
        }
        if(seleccion.equals("RFC")){
            System.out.println(" query RFC "+query);
            Cursor cursor2 =db.rawQuery("select prs_tipo,prs_nombre, prs_telefono from persona WHERE prs_rfc LIKE '%"+query+"%'", null);
            try {
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int index = 0;
                    while (!cursor2.isAfterLast()) {
                        String tipoPersona= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_tipo")));
                        String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                        String telefono= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_telefono")));
                        if(tipoPersona.equals("1")){
                            tipoPersona="Moral";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                        if(tipoPersona.equals("0")){
                            tipoPersona="Física";
                            listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                            index++;
                            cursor2.moveToNext();
                        }
                    }
                    if (index != 0) {
                        funcionClick();
                        recyclerView.setAdapter(adaptador);
                        //recyclerView.setAdapter(new RecyclerViewClientes((ArrayList<ClientesDatos>) listclientes));
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"Datos cargados.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }
                }
            }catch(Exception e){
                Log.println(Log.ERROR,"",e.getMessage());
            }
        }
    }

    private void cargarTodosLosClientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        listclientes.clear();
        Cursor cursor2 =db.rawQuery("select * from persona", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String tipoPersona= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_tipo")));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_nombre")));
                    String telefono= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_telefono")));
                    if(tipoPersona.equals("1")){
                        tipoPersona="Moral";
                        listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                        index++;
                        cursor2.moveToNext();
                    }
                    if(tipoPersona.equals("0")){
                        tipoPersona="Física";
                        listclientes.add(new ClientesDatos(tipoPersona,nombre, telefono));
                        index++;
                        cursor2.moveToNext();
                    }
                }
                if (index != 0) {
                    funcionClick();
                    recyclerView.setAdapter(adaptador);
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }


    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.ClienteMenu.class);
        startActivity(intencion2);
        finish();

       /* LayoutInflater imagenadvertencia_alert =LayoutInflater.from(ConsultaClientes.this);
        final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
        AlertDialog.Builder alerta = new AlertDialog.Builder(ConsultaClientes.this);
        alerta.setMessage("¿Desea regresar al menú cliente?")
                .setCancelable(true)
                .setCustomTitle(vista)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intencion2 = new Intent(getApplication(), ClienteMenu.class);
                        startActivity(intencion2);
                        finish();
                    }
                });
        alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alerta.show();
        */
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.ClienteMenu.class);
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

    public  void  funcionClick(){
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int elemen=   recyclerView.getChildAdapterPosition(v);
                        System.out.println(" Pulsado el elemento    " + listclientes.get(elemen).getNombre());
                        //Toast.makeText(ConsultaClientes.this,"  1111 ",Toast.LENGTH_SHORT).show();
                        String tipo=listclientes.get(elemen).getTipoPersona();
                        if(tipo.equals("Moral")){
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar=listclientes.get(elemen).getNombre();
                            Intent intencion = new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.PersonaMoral.class);
                            startActivity(intencion);
                            finish();
                        }
                        if(tipo.equals("Física") || tipo.equals("Fisica")){
                            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar=listclientes.get(elemen).getNombre();
                            Intent intencion = new Intent(getApplicationContext(), PersonaFisica.class);
                            startActivity(intencion);
                            finish();
                        }
                    }
                });
    }

    public  void generarPDF1() {
        if(recyclerView.getAdapter()!=null) {
            ActivityCompat.requestPermissions(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            templatePDF = new com.lzacatzontetlh.koonolmodulos.TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            templatePDF.addTitles("koonolmodulos", "Lista de clientes", sq.fecha());
            if(contador==1){
                String filtro= select.getSelectedItem().toString().trim();
                String estado = "  Filtrado por: " + filtro ;
                templatePDF.addParagraph(estado);
            }
                 templatePDF.addParagraph(" ");
                ArrayList<String[]> rows = new ArrayList();
                int co=  listclientes.size();
                for (int i=0; i < co; i++ ) {
                    rows.add(new String[]{  listclientes.get(i).getTipoPersona(),  listclientes.get(i).getNombre(), listclientes.get(i).getTelefono()});
                }
                String[] header = {"Tipo de persona"," Nombre", "Teléfono" };
                templatePDF.createTable(header, rows);
            templatePDF.addParagraph(" ");
            templatePDF.closeDocument();
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().regresarAConultCliente=1;
            templatePDF.viewPDF();
        }
        else {
            Toast.makeText(com.lzacatzontetlh.koonolmodulos.ConsultaClientes.this, "Seleccione al menos un filtro para generar un reporte.", Toast.LENGTH_SHORT).show();
        }

    }




}
