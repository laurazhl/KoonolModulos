package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapter;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;

import java.util.ArrayList;

public class VentaMenu extends AppCompatActivity {
    private GridView gridView;
    TextView textoProducto;
    GridAdapter adapter;
    String selectItem;

    final Context context = this;

    Ingresarsql sq = new Ingresarsql();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_menu);

        //PUNTO DE VENTA
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Registrar venta");
        arrayList.add("Ventas previas");
        arrayList.add("Cancelaciones");
        int images[] ={R.drawable.venta,R.drawable.ventasprevias, R.drawable.cancelarventa};

        adapter = new GridAdapter(this,images, arrayList);*/

        //DON CHON
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Registrar venta");
        arrayList.add("Ventas previas");
        arrayList.add("Cancelaciones");
        arrayList.add("Reportes");
        int images[] ={R.drawable.venta,R.drawable.ventasprevias, R.drawable.cancelarventa,R.drawable.reportes1};

        adapter = new GridAdapter(this,images, arrayList);
        gridView =  findViewById(R.id.gridviewVenta);
        textoProducto =  findViewById(R.id.text);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nombreProducto =  view.findViewById(R.id.ig_tv_titulo);
                selectItem = nombreProducto.getText().toString();

                if (selectItem.equals("Registrar venta")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentaMenu.this, com.lzacatzontetlh.koonolmodulos.VentasTacos.class));
                    //startActivity(new Intent(VentaMenu.this, RegistrarVenta.class));

                }if (selectItem.equals("Ventas previas")) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentaMenu.this, com.lzacatzontetlh.koonolmodulos.VentasPrevias.class));

                }if (selectItem.equals("Cancelaciones")) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentaMenu.this, com.lzacatzontetlh.koonolmodulos.Cancelacion.class));

                }
                if (selectItem.equals("Reportes")) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.VentaMenu.this, reporteVentas.class));

                }


            }
        });



        String usu=   com.lzacatzontetlh.koonolmodulos.Globales.getInstance().id_usuario;
       // sq.consultaCaja(getApplicationContext(), usu);
        sq.consultaEsatblecimientoCancelacion(getApplicationContext(), usu);

    }




    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
        startActivity(intencion2);
        finish();
    }
    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.VentaMenu.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.VentaMenu.this);
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
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.VentaMenu.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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



}
