package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ClasificacionDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.lzacatzontetlh.koonolmodulos.modelo.ProductosDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewClasificacion;
import com.lzacatzontetlh.koonolmodulos.modelo.RecyclerViewLL;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

// Autor: Laura Zacatzontetl Hernandez
public class Productos extends AppCompatActivity {
    //  Button canclar;

    EditText busqueda,thide;
    int[] ids= new int[100];
    ImageButton botonBuscar;
    public static TextView cuentaProductosC;
    View separador;
    ImageView imageView5;
    ImageButton botonTeclado,botonTecladoOculto;
    Button regresar;
    ConexionSQLiteHelper conn;
    Ingresarsql sq = new Ingresarsql();

    TextView fol,doc,cuentaProductos3;
    RecyclerView recyclerViewClasificacionProductos;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        busqueda= findViewById(R.id.busqueda2);
        botonBuscar= findViewById(R.id.btnBuscar2);
        regresar = findViewById(R.id.btnAceptar);
        recyclerViewClasificacionProductos= findViewById(R.id.productos);
        cuentaProductosC= findViewById(R.id.cuentaProductosC);
        cuentaProductos3= findViewById(R.id.cuentaProductos3);

        botonTeclado= findViewById(R.id.btnTeclado);
        botonTecladoOculto= findViewById(R.id.btnTecladoOculto);


        layoutManager = new LinearLayoutManager(this);
        recyclerViewClasificacionProductos.setLayoutManager(layoutManager);


        conn=new ConexionSQLiteHelper(getApplicationContext());
        imageView5= findViewById(R.id.imageView5);
        separador= findViewById(R.id.separador);
        //busqueda.setVisibility(View.INVISIBLE);
        botonTecladoOculto.setVisibility(View.INVISIBLE);
        botonTecladoOculto.setEnabled(false);
        busqueda.setEnabled(false);


        botonTeclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda.setEnabled(true);
                busqueda.setHint("Buscar producto");
                busqueda.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                sq.contabilizaLosProdAgreCarr(getApplicationContext());
                busqueda.setVisibility(View.VISIBLE);
                botonTeclado.setVisibility(View.INVISIBLE);
                botonTecladoOculto.setVisibility(View.VISIBLE);
                botonTecladoOculto.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Teclado activado", Toast.LENGTH_SHORT).show();



                busqueda.requestFocus(); //Asegurar que editText tiene focus
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(busqueda, InputMethodManager.SHOW_IMPLICIT);
//int i=0;
//android:fitsSystemWindows
                // v.setFitsSystemWindows(false);

                //  busqueda.setFitsSystemWindows(false);

            }
        });




        botonTecladoOculto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda.setEnabled(false);
                busqueda.setHint("Activar Teclado  â†’");
                busqueda.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                sq.contabilizaLosProdAgreCarr(getApplicationContext());
                busqueda.setVisibility(View.VISIBLE);

                botonTeclado.setVisibility(View.VISIBLE);
                botonTecladoOculto.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Teclado desactivado", Toast.LENGTH_SHORT).show();
                botonTecladoOculto.setEnabled(false);


                busqueda.requestFocus(); //Asegurar que editText tiene focus
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(busqueda.getWindowToken(), 0);

            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Productos.this, com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.class));
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
                //cargarTodosLosProductos();

                sq.contabilizaLosProdAgreCarr(getApplicationContext());
                cargarClasificaciones();
            }
        });


        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sq.contabilizaLosProdAgreCarr(getApplicationContext());
                buscarProducto();
                String query = busqueda.getText().toString();
                if(query.equals("")){
                    cargarClasificaciones();
                }
                else {
                    buscarProducto();
                    busqueda.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(busqueda.getWindowToken(), 0);

                }
            }
        });


        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Productos.this, com.lzacatzontetlh.koonolmodulos.VentasTacos.class));
            }
        });


        separador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Productos.this, com.lzacatzontetlh.koonolmodulos.VentasTacos.class));
            }
        });


        cuentaProductosC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Productos.this, com.lzacatzontetlh.koonolmodulos.VentasTacos.class));
            }
        });


        cuentaProductos3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Productos.this, com.lzacatzontetlh.koonolmodulos.VentasTacos.class));
            }
        });

        cargarClasificaciones();
        // getJSON("https://www.nextcom.com.mx/webserviceapp/koonol/Consulta_clasificacion.php");


        sq.contabilizaLosProdAgreCarr(getApplicationContext());
        recuperarFolio(getApplicationContext());


        fol= findViewById(R.id.fol);
        doc= findViewById(R.id.fol2);

        fol.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idFolio);
        doc.setText(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDocUl);




    }



    private void cargarClasificaciones() {

        sq.contabilizaLosProdAgreCarr(getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        List<String> list3 = new ArrayList<String>();
        final List<ClasificacionDatos> listclientes = new ArrayList<ClasificacionDatos>();
        listclientes.clear();

        Cursor cursor2 =db.rawQuery("select * from clasificacion", null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                Bitmap bitmap = null;
                while (!cursor2.isAfterLast()) {
                    String idClas= String.valueOf( cursor2.getString(cursor2.getColumnIndex("clas_id")));
                    String nombreClas= String.valueOf( cursor2.getString(cursor2.getColumnIndex("clas_nombre")));
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("clas_imagen"));


                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);

                    listclientes.add(new ClasificacionDatos(bitmap, nombreClas,idClas));
                    index++;
                    cursor2.moveToNext();



                }
                if (index != 0) {

                    final RecyclerViewClasificacion adaptador = new RecyclerViewClasificacion((ArrayList<ClasificacionDatos>) listclientes);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("DemoRecView", "Pulsado el elemento " + recyclerViewClasificacionProductos.getChildAdapterPosition(v));


                            int elemen=   recyclerViewClasificacionProductos.getChildAdapterPosition(v);
                            System.out.println(" Pulsado el elemento    " + listclientes.get(elemen).getNombreClasificacion());
                            String nombreclasificacion= listclientes.get(elemen).getNombreClasificacion();
                            String identificadorClasificacion= listclientes.get(elemen).getIdentificador();
                            Intent intencion = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.class);
                            intencion.putExtra(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.nombreclasificacion, nombreclasificacion);
                            intencion.putExtra(com.lzacatzontetlh.koonolmodulos.clasificacionSeleccionada.identificadorClasificacion, identificadorClasificacion);

                            startActivity(intencion);
                            finish();
                        }
                    });

                    recyclerViewClasificacionProductos.setAdapter(adaptador);
                }

            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage());
        }
    }

    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
        startActivity(intencion2);
        finish();
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.Productos.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Productos.this);
            alerta.setMessage("Â¿Desea cerrar las sesiÃ³n?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sq.limpiarVariablesGlobales();
                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.Productos.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
        sq.consultarNombreUsuario(getApplicationContext());
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);
        return true;
    }


    private void buscarProducto() {
        sq.consultaestatusHabilitado(getApplicationContext());
        int idEHabilitado= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusHabilitado;
        sq.contabilizaLosProdAgreCarr(getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();
        List<ProductosDatos> listclientes = new ArrayList<ProductosDatos>();
        String query = busqueda.getText().toString();
        listclientes.clear();
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.clear();
        Cursor cursor2 =db.rawQuery("SELECT prepro_id, lstp_precio, prst_descripcion, prd_nombre,prd_imagen, prd_id FROM producto INNER JOIN prest_prod ON producto.prd_id = prest_prod.prd_fk\n" +
                "               INNER JOIN presentacion ON presentacion.prst_id = prest_prod.prst_fk  INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id  WHERE prd_nombre LIKE '%"+query+"%' and producto.esta_fk='"+idEHabilitado+"'" , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                Bitmap bitmap = null;
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    String id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prepro_id")));
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    String nombre= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                    String precio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);
                    String presentacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prst_descripcion")));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.add(new ProductosDatos(id,nombre,bitmap,precio,presentacion));
                    listclientes.add(new ProductosDatos(id,nombre,bitmap,precio,presentacion));


                    index++;
                    cursor2.moveToNext();
                }


                if (index != 0) {
                    final RecyclerViewLL adaptador = new RecyclerViewLL((ArrayList<ProductosDatos>)  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Log.i("DemoRecView", "Pulsado el elemento " + recyclerViewClasificacionProductos.getChildAdapterPosition(v));
                            Log.i("DemoRecView", "Pulsado el elemento " + recyclerViewClasificacionProductos.getChildAdapterPosition(v));


                            int elemen=   recyclerViewClasificacionProductos.getChildAdapterPosition(v);
                            System.out.println(" Pulsado el elemento    " +  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.get(elemen).getNombreProducto());
                            String nombreclasificacion=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.get(elemen).getNombreProducto();
                            String identificadorClasificacion=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().listclientes.get(elemen).getClasificacion();
                        }
                    });

                    recyclerViewClasificacionProductos.setAdapter(adaptador);
                    recyclerViewClasificacionProductos.setLayoutManager(
                            new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                    recyclerViewClasificacionProductos.addItemDecoration(
                            new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                }
                else { Toast.makeText(com.lzacatzontetlh.koonolmodulos.Productos.this,"No hay concidencias", Toast.LENGTH_SHORT).show(); }
            }
        }catch(Exception e){
            Log.println(Log.ERROR,"",e.getMessage()); }
    }





    public  void  recuperarFolio(Context context){
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        sq.consultaestatus(context);
        int estatus= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstatusLau;
        String idu=  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
        sq.consultaEmpresaEstableCaja(getApplicationContext(),idu);
        String est= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idEstablecimientoLau;

        Cursor cursor2 =db.rawQuery("SELECT doc_id,fol_folio from folio  inner JOIN documento ON documento.doc_folio= folio.fol_folio WHERE  esta_fk='"+estatus+"' and est_fk='"+est+"'" , null);
        String folio = "";
        String documento = "";
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {

                    folio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("fol_folio")));
                    documento= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_id")));
                    index++;
                    cursor2.moveToNext();
                    break;
                }
                if (index != 0) {

                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idFolio = folio;
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDocUl = documento;


                }
                else
                {
                    String f1=sq.generarFolio(context);
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idFolio= f1;
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR,"Null167 ",e.getMessage());
        }


    }


}
// Autor: Laura Zacatzontetl Hernandez