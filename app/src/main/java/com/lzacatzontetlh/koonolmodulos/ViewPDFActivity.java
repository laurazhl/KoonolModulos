package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;

import java.io.File;

//import androidx.appcompat.app.AppCompatActivity;

// Autor: Laura Zacatzontetl Hernandez
public class ViewPDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    Button aceptarCancelacion;
    private File file;
    final Ingresarsql sq = new Ingresarsql();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView=(PDFView)findViewById(R.id.pdfView);
        aceptarCancelacion = findViewById(R.id.button6);
        ///para poner el icono a lado del nombre de la app
       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        Bundle bundle= getIntent().getExtras();
        if (bundle!=null){
            file =new File(bundle.getString("path", ""));
        }

        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();




        if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().regresarAConultCliente==1){
            aceptarCancelacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().regresarAConultCliente=0;
                    startActivity(new Intent(getApplicationContext(),ConsultaClientes.class));

                }
            });

        }
        else{
            aceptarCancelacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(getApplicationContext(),reporteVentas.class));
                }
            });
        }


    }


    public void onBackPressed() {
        if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().regresarAConultCliente==1){
            finish();
            startActivity(new Intent(getApplicationContext(),ConsultaClientes.class));
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().regresarAConultCliente=0;
        }else {
            Intent intencion2 = new Intent(getApplication(), reporteVentas.class);
            startActivity(intencion2);
            finish();
        }
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.ViewPDFActivity.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.ViewPDFActivity.this);
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
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.ViewPDFActivity.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
// Autor: Laura Zacatzontetl Hernandez