package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ClasificacionDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapterProductos;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.ProductosDatos;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

// Autor: Laura Zacatzontetl Hernandez
public class clasificacionSeleccionada extends AppCompatActivity {



    public static final String nombreclasificacion="nombreclasificacion";
    public static final String identificadorClasificacion="identificadorClasificacion";
    private GridView gridView;
    GridAdapterProductos adapter;
    EditText busqueda;
    ImageButton botonTeclado,botonTecladoOculto;
    Spinner spinnerlistaCaracteristica,valor;

    TextView txtCarac,txtIden,cuentaProductos2;
    ImageButton botonBuscar;
    ImageView imageView4;
    View separador;
    ConexionSQLiteHelper conn;
    TextView fol,doc;
    public static TextView cuentaProductos;
    Ingresarsql sq = new Ingresarsql();
    RecyclerView recyclerViewClasificacionProductos;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion_seleccionada);
        busqueda= findViewById(R.id.busqueda3);
        botonBuscar= findViewById(R.id.btnBuscar3cs);
        cuentaProductos= findViewById(R.id.cuentaProductos);
        imageView4= findViewById(R.id.imageView4);
        botonTeclado= findViewById(R.id.btnTeclado);
        botonTecladoOculto= findViewById(R.id.btnTecladoOculto);
        gridView= findViewById(R.id.vistaUsuario2);
        spinnerlistaCaracteristica= (Spinner)findViewById(R.id.caracteristica);
        valor= (Spinner)findViewById(R.id.valor);
        txtCarac= findViewById(R.id.txtCarac);
        txtIden= findViewById(R.id.txtIden);

        cuentaProductos2= findViewById(R.id.cuentaProductos2);
        separador= findViewById(R.id.separador);


        // recyclerViewClasificacionProductos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        // recyclerViewClasificacionProductos.setLayoutManager(layoutManager);
        conn=new ConexionSQLiteHelper(getApplicationContext());

        final String nombreclasificacion = getIntent().getStringExtra("nombreclasificacion");
        txtCarac.setText(nombreclasificacion);
        final String identificadorClasificacion = getIntent().getStringExtra("identificadorClasificacion");
        txtIden.setText(identificadorClasificacion);
        busqueda.setVisibility(View.VISIBLE);
        busqueda.setFocusable(false);

        txtIden.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this, Productos.class));

            }
        });

        busqueda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                busqueda.setFocusable(false);
                busqueda.setEnabled(false);
                Intent intencion2 = new Intent(getApplication(), Productos.class);
                finish();
                startActivity(intencion2);
            }
        });


        botonTeclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion2 = new Intent(getApplication(), Productos.class);
                finish();
                startActivity(intencion2);
            }
        });
       /* botonTecladoOculto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/




        cargarCaracteristicas();
        cargarValor();

    /*    spinnerlistaCaracteristica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarCaracteristicas();
                System.out.print(" 11111111111 ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                  Toast.makeText(clasificacionSeleccionada.this, "No esta funcionando ",Toast.LENGTH_LONG).show();
                System.out.print(" 3333 ");
            }


        });*/

      /*  busqueda.addTextChangedListener(new TextWatcher() {
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
                //cargarTodosLosProductos();
                contabilizaLosProdAgreCarr();
            }
        });*/

        //cargarClasificaciones(clasificacion);

        // mostrarProductos();
        cargarProductosAlI();
        //cargarProductosTodos();

        spinnerlistaCaracteristica.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn, View v, int posicion, long id) {
                        Toast.makeText(spn.getContext(), "Caracteristica: " + spn.getItemAtPosition(posicion).toString(), Toast.LENGTH_LONG).show();
                        cargarProductosConCaracteristica();
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });


        //cargarProductosConCaracteristica
        valor.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn, View v, int posicion, long id) {
                        //Toast.makeText(spn.getContext(), "Has seleccionado el valor: " + spn.getItemAtPosition(posicion).toString(), Toast.LENGTH_LONG).show();
                        cargarProductosConCaracteristicaYValor();
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion2 = new Intent(getApplication(), Productos.class);
                finish();
                startActivity(intencion2);
                /*contabilizaLosProdAgreCarr();
                buscarProducto();
                String query = busqueda.getText().toString();
                if(query.equals("")){
                   // cargarClasificaciones();
                    cargarProductosTodos();
                }
                else {
                    buscarProducto();
                }*/
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this, VentasTacos.class));
            }
        });

        separador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this, VentasTacos.class));
            }
        });


        cuentaProductos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this, VentasTacos.class));
            }
        });


        cuentaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this, VentasTacos.class));
            }
        });


        contabilizaLosProdAgreCarr();

        //  fol= findViewById(R.id.fol);
        //  doc= findViewById(R.id.fol2);

//        fol.setText(Globales.getInstance().idFolio);
        // doc.setText(Globales.getInstance().idDocUl);


     /*   adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater imagenadvertencia_alert =LayoutInflater.from(clasificacionSeleccionada.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(clasificacionSeleccionada.this);
                alerta.setMessage("Mensaje de prueba.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar",null) ;alerta.show();
            }
        });*/



    }

    public  void  cargarCaracteristicas(){
        contabilizaLosProdAgreCarr();
        String query = txtIden.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        List<String> listaCaracteristica = new ArrayList<String>();
        listaCaracteristica.clear();
        Cursor cursor2 =db.rawQuery("select crts_id, crts_nombre, clas_fk from caracteristica  WHERE clas_fk='"+query+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String id= String.valueOf(cursor2.getColumnIndex("crts_id"));
                    String carac= String.valueOf( cursor2.getString(cursor2.getColumnIndex("crts_nombre")));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("clas_fk")));
                    listaCaracteristica.add(carac);
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCaracteristica);
                    spinnerlistaCaracteristica.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listaCaracteristica.clear();
                    spinnerlistaCaracteristica.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public  void  cargarValor(){
        contabilizaLosProdAgreCarr();
        String query = txtIden.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        List<String> listaValor = new ArrayList<String>();
        listaValor.clear();
        Cursor cursor2 =db.rawQuery("SELECT crtd_id, crts_fk, crts_id,ctrd_descripcion,clas_fk FROM caract_det INNER JOIN caracteristica ON caract_det.crts_fk= caracteristica.crts_id INNER JOIN clasificacion ON clasificacion.clas_id= caracteristica.clas_fk WHERE clas_id='"+query+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String id= String.valueOf(cursor2.getColumnIndex("crtd_id"));
                    String carac= String.valueOf( cursor2.getString(cursor2.getColumnIndex("ctrd_descripcion")));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("crtd_id")));
                    listaValor.add(carac);
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaValor);
                    valor.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listaValor.clear();
                    valor.setAdapter(null);
                }


            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), Productos.class);
        startActivity(intencion2);
        finish();
    }
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this);
            alerta.setMessage("Â¿Desea cerrar las sesiÃ³n?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            Intent intencion2 = new Intent(getApplication(), MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"SesiÃ³n  Cerrada", Toast.LENGTH_LONG).show();
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




    private void cargarProductosAlI() {
        sq.consultaestatusHabilitado(getApplicationContext());
        int idEHabilitado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusHabilitado;
        contabilizaLosProdAgreCarr();
        String clasificacion = txtIden.getText().toString();
        SQLiteDatabase db = conn.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<ProductosDatos> listProductos = new ArrayList<ProductosDatos>();
        listProductos.clear();
        // Cursor cursor2 =db.rawQuery("SELECT prd_id, prd_imagen, prd_nombre, prepro_precompra, crts_id,crts_nombre,  producto.clas_fk,prd_fk  FROM producto INNER JOIN clasificacion ON producto.clas_fk= clasificacion.clas_id " +
        //        "INNER JOIN caracteristica ON caracteristica.clas_fk= clasificacion.clas_id INNER JOIN prest_prod ON prest_prod.prd_fk= producto.prd_id WHERE  producto.clas_fk='"+clasificacion+"'", null);
        Cursor cursor2 =db.rawQuery("SELECT prepro_id, prd_imagen, prd_nombre, lstp_precio, prst_fk, prst_descripcion \n" +
                "FROM prest_prod \n" +
                "INNER JOIN producto On producto.prd_id= prest_prod.prd_fk \n" +
                "INNER JOIN presentacion ON  presentacion.prst_id= prest_prod.prst_fk  INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id\n" +
                "WHERE  producto.clas_fk='"+clasificacion+"' and producto.esta_fk='"+idEHabilitado+"'", null);

        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                Bitmap bitmap = null;
                while (!cursor2.isAfterLast()) {

                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                    String precio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prepro_id")));
                    String presentacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prst_descripcion")));



                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);

                    listProductos.add(new ProductosDatos(id,nombre,bitmap, precio,presentacion));
                    arrayList.add(nombre);
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    adapter = new GridAdapterProductos(this, listProductos);
                    gridView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listProductos.clear();
                    gridView.setAdapter(null);
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarProductosConCaracteristica() {
        sq.consultaestatusHabilitado(getApplicationContext());
        int idEHabilitado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusHabilitado;
        contabilizaLosProdAgreCarr();
        String clasificacion = txtIden.getText().toString();
        String spinnerlistaCaracteristica1;
        spinnerlistaCaracteristica1 = spinnerlistaCaracteristica.getSelectedItem().toString();
        final ArrayList<ProductosDatos> listProductos = new ArrayList<ProductosDatos>();
        listProductos.clear();
        SQLiteDatabase db = conn.getReadableDatabase();
        final List<ClasificacionDatos> listclientes = new ArrayList<ClasificacionDatos>();
        listclientes.clear();
      /*  Cursor cursor2 =db.rawQuery("SELECT prd_id, prd_imagen, prd_nombre, prepro_precompra, crts_id,crts_nombre,  producto.clas_fk,prd_fk  FROM producto INNER JOIN clasificacion ON producto.clas_fk= clasificacion.clas_id " +
                "INNER JOIN caracteristica ON caracteristica.clas_fk= clasificacion.clas_id INNER JOIN prest_prod ON prest_prod.prd_fk= producto.prd_id WHERE  crts_nombre= '"+spinnerlistaCaracteristica1 +"' and producto.clas_fk='"+clasificacion+"' ", null);
*/
        Cursor cursor2 =db.rawQuery("SELECT prepro_id, prd_imagen, prd_nombre, lstp_precio, prst_fk, prst_descripcion \n" +
                "FROM prest_prod \n" +
                "INNER JOIN producto On producto.prd_id= prest_prod.prd_fk \n" +
                "INNER JOIN presentacion ON  presentacion.prst_id= prest_prod.prst_fk \n" +
                "INNER JOIN caracteristica ON caracteristica.clas_fk= producto.clas_fk INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id \n" +
                "WHERE  crts_nombre= '"+spinnerlistaCaracteristica1 +"' and producto.clas_fk='"+clasificacion+"'  and producto.esta_fk='"+idEHabilitado+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                Bitmap bitmap = null;
                while (!cursor2.isAfterLast()) {
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                    String precio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prepro_id")));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);
                    String presentacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prst_descripcion")));
                    listProductos.add(new  ProductosDatos(id,nombre,bitmap, precio,presentacion));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    adapter = new GridAdapterProductos(this, listProductos);
                    gridView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listProductos.clear();
                    gridView.setAdapter(null);
                }



            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    private void cargarProductosConCaracteristicaYValor() {
        sq.consultaestatusHabilitado(getApplicationContext());
        int idEHabilitado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusHabilitado;
        contabilizaLosProdAgreCarr();
        String clasificacion = txtIden.getText().toString();
        String spinnerlistaCaracteristica1,varValor;
        spinnerlistaCaracteristica1 = spinnerlistaCaracteristica.getSelectedItem().toString();
        varValor = valor.getSelectedItem().toString();
        final ArrayList<ProductosDatos> listProductos = new ArrayList<ProductosDatos>();
        listProductos.clear();
        SQLiteDatabase db = conn.getReadableDatabase();
       /* Cursor cursor2 =db.rawQuery("SELECT prd_id, prd_imagen, prd_nombre, prepro_precompra, crts_id,crts_nombre,  producto.clas_fk,prd_fk,clas_nombre  FROM producto INNER JOIN clasificacion ON producto.clas_fk= clasificacion.clas_id " +
                "INNER JOIN caracteristica ON caracteristica.clas_fk= clasificacion.clas_id INNER JOIN prest_prod ON prest_prod.prd_fk= producto.prd_id " +
                "WHERE  crts_nombre= '"+spinnerlistaCaracteristica1 +"' and producto.clas_fk='"+clasificacion+"' and prd_nombre  LIKE '%"+varValor+"%'", null);
*/
        Cursor cursor2 =db.rawQuery("SELECT prepro_id, prd_imagen, prd_nombre, lstp_precio, prst_fk, prst_descripcion \n" +
                "FROM prest_prod \n" +
                "INNER JOIN producto On producto.prd_id= prest_prod.prd_fk \n" +
                "INNER JOIN presentacion ON  presentacion.prst_id= prest_prod.prst_fk \n" +
                "INNER JOIN caracteristica ON caracteristica.clas_fk= producto.clas_fk INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id \n" +
                "WHERE  crts_nombre= '"+spinnerlistaCaracteristica1 +"' and producto.clas_fk='"+clasificacion+"' and prd_nombre  LIKE '%"+varValor+"%' and producto.esta_fk='"+idEHabilitado+"' ", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                Bitmap bitmap = null;
                while (!cursor2.isAfterLast()) {

                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                    String precio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prepro_id")));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);
                    // listProductos.add(new  ProductosDatos(id,nombre,bitmap, precio));

                    String presentacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prst_descripcion")));
                    listProductos.add(new ProductosDatos(id,nombre,bitmap, precio,presentacion));
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    final GridAdapterProductos adaptador = new GridAdapterProductos(this, listProductos);
                    gridView.setAdapter(adaptador);


                }
                else
                {
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.this,"No hay concidencias", Toast.LENGTH_SHORT).show();
                    listProductos.clear();
                    gridView.setAdapter(null);
                }

            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }




    private void contabilizaLosProdAgreCarr() {
        SQLiteDatabase db = conn.getReadableDatabase();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.clear();
        sq.consultaestatus(getApplicationContext());
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;
        // String doc=  Globales.getInstance().idDocUl;
        //Cursor cursor2 =db.rawQuery("select sum(docd_cantprod) as cantidadProductos FROM documento_det" , null);
        ///Cursor cursor2 =db.rawQuery("select sum(docd_cantprod) as cantidadProductos FROM documento_det  INNER JOIN documento ON documento_det.doc_fk=documento.doc_id WHERE  doc_fk ="+doc+" and  esta_fk='4'", null);
        Cursor cursor2 =db.rawQuery("select sum(docd_cantprod) as cantidadProductos FROM documento_det  INNER JOIN documento ON documento_det.doc_fk=documento.doc_id WHERE    esta_fk='"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusLau+"'  and est_fk='"+est+"'", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cantidadProductos")));
                    if(id.equals("null")){ cuentaProductos.setText("0"); }
                    else { cuentaProductos.setText(id); }
                    index++;
                    cursor2.moveToNext();
                }
                if (index != 0) {
                    System.out.println("CLPAC " );
                }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }
}
// Autor: Laura Zacatzontetl Hernandez