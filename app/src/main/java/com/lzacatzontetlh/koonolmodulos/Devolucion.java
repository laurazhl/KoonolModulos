package com.lzacatzontetlh.koonolmodulos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Devolucion extends AppCompatActivity {
    Button devolverProducts, cancelarDevolucion;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        devolverProducts = (Button)findViewById(R.id.btnConfirmar);
        cancelarDevolucion = (Button)findViewById(R.id.btnCancelar);

        devolverProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDevolucion();
            }
        });

        cancelarDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.VentaMenu.class));
            }
        });
    }


    private void AlertDevolucion() {
        LayoutInflater devolucion = LayoutInflater.from(context);
        View prompstsDevolucion = devolucion.inflate(R.layout.dialog_mensaje_devolucion, null);
        final android.support.v7.app.AlertDialog.Builder builderDevolucion = new android.support.v7.app.AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Devolucion.this);
        builderDevolucion.setView(prompstsDevolucion);
        builderDevolucion.setCancelable(false);

        final Button aceptarDevolucion = (Button) prompstsDevolucion.findViewById(R.id.btnAceptar);

        aceptarDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ALERT DE CONFIRMACION DEVOLUCION DE PRODUCTO
                AlertDialog.Builder mensajeCancelacion = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Devolucion.this);
                mensajeCancelacion.setMessage("Articulo devuelto");
                mensajeCancelacion.setCancelable(false);

                mensajeCancelacion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.VentaMenu.class));
                    }
                });

                android.support.v7.app.AlertDialog alert = mensajeCancelacion.create();
                alert.show();
            }
        });

        android.support.v7.app.AlertDialog alertDevolucion = builderDevolucion.create();
        alertDevolucion.show();
    }
}
