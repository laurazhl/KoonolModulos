package com.lzacatzontetlh.koonolmodulos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.commons.PrinterConnect;
import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.ProductoAdapter;
import com.lzacatzontetlh.koonolmodulos.modelo.Productoss;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VentasTacos extends AppCompatActivity implements ProductoAdapter.MiListener {

    RecyclerView recyclerView;
    ConexionSQLiteHelper conn;

    ProductoAdapter adaptaProdsLista;
    ArrayList<Productoss> productos;
    int[] ids= new int[100];
    int[] productsID= new int[100];
    String nomb, folioVenta;
    int indexList, cant=0, cant2=0;
    double precioUnitario=0.0, precioUnitario2=0.0;
    Double TotalVenta=0.0, totalCobrar=0.0, subTot=0.0, toTal=0.0, toTal2=0.0;
    Button btnMontoTotal, agregar, cancelar;
    int bandera=0;
    final Context context = this;
    private ProgressDialog progressDialog;
    Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_tacos);

        conn=new ConexionSQLiteHelper(getApplicationContext());
        productos = new ArrayList<Productoss>();
        //
        // progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnMontoTotal = (Button) findViewById(R.id.btnCobrar);
        agregar = (Button) findViewById(R.id.btnAgregar);
        cancelar = (Button) findViewById(R.id.btnCancelar);

        adaptaProdsLista = new ProductoAdapter(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, productos);
        consultaSQL2();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, Productos.class));
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentasTacos.this);
                builder.setCancelable(false);
                builder.setMessage("¿Está seguro de cancelar la captura?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        SQLiteDatabase db = conn.getReadableDatabase();
                        Cursor cursorC = db.rawQuery("DELETE FROM documento_det WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);
                        Cursor cursorC2 = db.rawQuery("DELETE FROM documento WHERE documento.doc_id="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);

                        cursorC.moveToFirst();
                        cursorC2.moveToFirst();
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, "Captura cancelado...", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //  Botón para imprensión la venta que se realizó
        btnMontoTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reimprimiendo taskimp;
                taskimp = new Reimprimiendo();
                taskimp.execute((Void) null);


            }
        });
    }



    //CONSULTA A BD PARA OBTENER LOS PRODUCTOS DEL X VENTA
    private void consultaSQL2() {
        productos.clear();
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor2 =db.rawQuery("SELECT doc_id, doc_folio,docd_cantprod,prst_descripcion, docd_precven, doc_total, prd_nombre, prd_imagen, lstp_precio, documento_det.prepro_fk" +
                " FROM estatus, documento, documento_det, producto, prest_prod " +
                " INNER JOIN presentacion ON presentacion.prst_id = prest_prod.prst_fk  INNER JOIN listaprecio ON listaprecio.prepro_fk=documento_det.prepro_fk WHERE estatus.esta_estatus='En proceso' AND documento.esta_fk=estatus.esta_id " +
                "AND documento.doc_id = documento_det.doc_fk " +
                "AND documento_det.prepro_fk = prest_prod.prepro_id  " +
                "AND prest_prod.prd_fk = producto.prd_id", null);

        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                Bitmap bitmap = null;
                while (!cursor2.isAfterLast()) {
                    folioVenta = cursor2.getString(cursor2.getColumnIndex("doc_folio"));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia = folioVenta;

                    precioUnitario = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                    nomb= cursor2.getString(cursor2.getColumnIndex("prd_nombre"));
                    cant= cursor2.getInt(cursor2.getColumnIndex("docd_cantprod"));
                    subTot= cursor2.getDouble(cursor2.getColumnIndex("docd_precven"));
                    toTal= cursor2.getDouble(cursor2.getColumnIndex("doc_total"));



                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle = cursor2.getInt(cursor2.getColumnIndex("doc_id"));
                    productsID[index] = cursor2.getInt(cursor2.getColumnIndex("prepro_fk"));

                    //Se obtiene la imagen del producto seleccionado
                    byte[] blob = cursor2.getBlob(cursor2.getColumnIndex("prd_imagen"));
                    ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                    bitmap = BitmapFactory.decodeStream(bais);


                    //var para presentacion
                    String presentacion = cursor2.getString(cursor2.getColumnIndex("prst_descripcion"));


                    //Asignación de la información
                    Productoss producto = new Productoss(bitmap,nomb,cant,precioUnitario,subTot,presentacion);
                    productos.add(producto);
                    btnMontoTotal.setText("Registrar venta \t\n\t $ " + String.format("%.2f", toTal));
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta =  toTal;

                    index++;
                    cursor2.moveToNext();
                }

                if (index != 0) {
                    recyclerView.setAdapter(adaptaProdsLista);
                }else {
                    btnMontoTotal.setEnabled(false);
                    btnMontoTotal.setBackgroundColor(0xFFe6e6e6);
                }

                bandera++;
            }

        }catch(Exception e){
        }
    }


    //Si se retrocede.. este método se encargará de cancelar la venta si se presiona el botón si
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("¿Está seguro de cancelar la captura?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application

                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor cursorC=db.rawQuery("DELETE FROM documento_det WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);
                Cursor cursorC2 =db.rawQuery("DELETE FROM documento WHERE documento.doc_id="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);
                cursorC.moveToFirst();
                cursorC2.moveToFirst();

                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, "Captura cancelada...", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(Productoss productos, EditText cantidadescribir) {
        productos.cantidad=0;
        cantidadescribir.requestFocus();
        cantidadescribir.setCursorVisible(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(com.lzacatzontetlh.koonolmodulos.VentasTacos.this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(cantidadescribir, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onItemClickMenos(Productoss productos) {
        productos.cantidad=0;
    }

    @Override
    public void cambiacant(String cadena, int posicion) {

        if (com.lzacatzontetlh.koonolmodulos.Globales.getInstance().operacion == 0){
            if(cadena.trim().equals("")){
                cadena = "0";

            }
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().cantiOperacion = Integer.valueOf(cadena);
            consultaSQL3("cambia");
        }
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().operacion = 0;
    }


    @Override
    public void elimi( int posicion) {
        if (productos.size() == 1){
        //if (posicion == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentasTacos.this);
            builder.setCancelable(false);
            builder.setMessage("¿Está seguro de cancelar la captura?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    SQLiteDatabase db = conn.getReadableDatabase();
                    Cursor cursorC = db.rawQuery("DELETE FROM documento_det WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);
                    Cursor cursorC2 = db.rawQuery("DELETE FROM documento WHERE documento.doc_id="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);

                    cursorC.moveToFirst();
                    cursorC2.moveToFirst();
                    finish();
                    startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                    Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, "Captura cancelado...", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto = productsID[posicion];
            consultaSQL3("eliminar");
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().operacion = 1;
        }

    }

    @Override
    public void aumentar(int posicion) {
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto = productsID[posicion];
        consultaSQL3("aumentar");
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().operacion = 1;
    }

    @Override
    public void disminuir( int posicion) {

        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto = productsID[posicion];
        consultaSQL3("disminuir");
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().operacion = 1;

    }


    private void consultaSQL3(String tipo) {
        int cantotal=0;
        SQLiteDatabase db = conn.getReadableDatabase();
        SQLiteDatabase db2 = conn.getWritableDatabase();

        Cursor cursor2 =db.rawQuery("SELECT docd_cantprod, lstp_precio FROM documento INNER JOIN documento_det ON "+
                "documento.doc_id = documento_det.doc_fk INNER JOIN producto ON "+
                "documento_det.prepro_fk =  prest_prod.prepro_id INNER JOIN prest_prod ON prest_prod.prd_fk = producto.prd_id INNER JOIN listaprecio ON listaprecio.prepro_fk=documento_det.prepro_fk WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle+" AND documento_det.prepro_fk="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto, null);

        try {
        if (cursor2 != null) {
            cursor2.moveToFirst();
            int index = 0;

            while (!cursor2.isAfterLast()) {
                precioUnitario2 = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                cant2= cursor2.getInt(cursor2.getColumnIndex("docd_cantprod"));

                index++;
                cursor2.moveToNext();
            }

            if (tipo == "aumentar"){
                cantotal = cant2+1;

            }else if (tipo == "disminuir"){
                cantotal = cant2-1;

            }/*else if(tipo == "cambia"){
                    cantotal = Globales.getInstance().cantiOperacion;
                }*/else if (tipo == "eliminar"){
                Cursor cursorE=db.rawQuery("DELETE FROM documento_det WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle+" AND documento_det.prepro_fk="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto, null);
                cursorE.moveToNext();
                Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, "Producto cancelado...", Toast.LENGTH_SHORT).show();

            }
            if (cantotal <= 0){
                cantotal = 0;
            }
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().canTotal = cantotal;

            toTal2 = precioUnitario2*cantotal;

            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().subTotal=toTal2;

            db2.execSQL("UPDATE documento_det SET docd_cantprod = "+cantotal+", docd_precven = "+toTal2+" WHERE doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle+" AND documento_det.prepro_fk="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idProducto);

            Cursor cursor3 = db.rawQuery("SELECT SUM(docd_precven) as suma FROM documento_det WHERE documento_det.doc_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle, null);
            cursor3.moveToFirst();

            TotalVenta = Double.parseDouble(cursor3.getString(cursor3.getColumnIndex("suma")));
            String v =  String.format("%.2f", TotalVenta);
            totalCobrar = Double.valueOf(v);
            btnMontoTotal.setText("Registrar venta \t\n\t $ " + Double.toString(totalCobrar));
            db2.execSQL("UPDATE documento SET doc_subtotal = "+TotalVenta+", doc_total  = "+TotalVenta+" WHERE doc_id ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle);
            // Restore state
            //recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            consultaSQL2();
            //recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


        }

        }catch(Exception e){
        }
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    View view = com.lzacatzontetlh.koonolmodulos.VentasTacos.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(com.lzacatzontetlh.koonolmodulos.VentasTacos.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    break;
            }
        }
    }


    //METODO PARA LA IMPRESION
    public class
    Reimprimiendo extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog pd;
        private Context mContext;
        PrinterConnect impresora;

        Reimprimiendo() {
            mContext = com.lzacatzontetlh.koonolmodulos.VentasTacos.this;
            impresora = new PrinterConnect(mContext);
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Imprimiendo");

            pd.setMessage("Aguarde mientras se imprime ");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String cadenota = "";
            String cadenotaDatos = "";

            try {
                for (Productoss temp : productos) {
                    cadenotaDatos = cadenotaDatos + "\n" +temp.nombre + "\n $ " + temp.precio +"  X " + temp.cantidad +" = $ " + temp.subtotal;
                }

                impresora.conecta();

                cadenota =  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().establecimientoo2+" \n" + "Folio de la venta: "+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idVentaPrevia +" \n" +
                        "Fecha:" + fecha() +"   Hora:"+hora()+"\n-------------------------------\n"+cadenotaDatos+"\n \n -------------------------------\n"+"Subtotal: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta+" \n" +"IVA 16%: $0.00"+" \n"+ "Total: $"+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().TotVenta+" \n"+"*** Cuenta por pagar ***";

                impresora.SendDataString(cadenota);
                impresora.SendDataString("\n----------------------------------------------------------------\n\n");
                cadenota = "";
                impresora.detieneImpresion();
                //finish();
                //startActivity(new Intent(VentasTacos.this, MenuGeneral.class));
            } catch (android.database.SQLException e) {

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            LayoutInflater ticket  = LayoutInflater.from(context);
            View prompstsView2 = ticket.inflate(R.layout.dialog_imprimir, null);
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentasTacos.this);
            builder2.setView(prompstsView2);
            builder2.setCancelable(false);

            final TextView textoo = (TextView) prompstsView2.findViewById(R.id.textView5);
            textoo.setText("¿Se imprimió correctamente su ticket ?");

            builder2.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    consultaEstatus();
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentasTacos.this, com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
                }
            });

            builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // SI EL TICKET NO SE IMPRIMIÓ VUELVE A LLAMAR EL MÉTODO PARA VOLVER A IMPRIMIR
                    Reimprimiendo taskimp;
                    taskimp = new Reimprimiendo();
                    taskimp.execute((Void) null);
                    pd.dismiss();
                }
            });
            AlertDialog alert = builder2.create();
            alert.show();
        }

    }


    //METODO PARA OBTENER FECHA ACTUAL
    private String fecha() {
        final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }

    //METODO PARA OBTENER HORA ACTUAL
    private String hora() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String datetime = dateFormat.format(c.getTime());
        return datetime;
    }


    //CONSULTA A BD PARA OBTENER ID DEL ESTATUS POR PAGAR
    private void consultaEstatus() {
        SQLiteDatabase db = conn.getReadableDatabase();
        SQLiteDatabase dbUpdate= conn.getWritableDatabase();
        Cursor cursorEstatus =db.rawQuery("SELECT * FROM estatus WHERE estatus.esta_estatus='Por pagar'",null);

        try {
            if (cursorEstatus != null) {
                cursorEstatus.moveToFirst();

                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idStatus = cursorEstatus.getInt(cursorEstatus.getColumnIndex("esta_id"));
                String sql="UPDATE documento SET esta_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idStatus+" WHERE doc_id ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle;
                dbUpdate.execSQL("UPDATE documento SET esta_fk ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idStatus+" WHERE doc_id ="+ com.lzacatzontetlh.koonolmodulos.Globales.getInstance().idDetalle);

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

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.VentasTacos.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentasTacos.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentasTacos.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
