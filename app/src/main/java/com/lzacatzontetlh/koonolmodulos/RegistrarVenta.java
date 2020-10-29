package com.lzacatzontetlh.koonolmodulos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegistrarVenta extends AppCompatActivity {
    ImageButton presentacion;
    final Context context = this;
    AlertDialog alertPresentacion, alertContado, alertPago, alertCredito;
    Button pagar;
    RadioButton opcion1, opcion2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        presentacion = (ImageButton)findViewById(R.id.btnBuscar2);
        pagar = (Button)findViewById(R.id.btnPagar);

        presentacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertPresentacion();
            }
        });

        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertFornaPago();

            }
        });
    }


    private void AlertPresentacion() {
        LayoutInflater li2 = LayoutInflater.from(context);
        View prompstsPresentacion = li2.inflate(R.layout.dialog_presentacion, null);
        final AlertDialog.Builder  builderPresentacion = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.RegistrarVenta.this);
        builderPresentacion.setView(prompstsPresentacion);
        builderPresentacion.setCancelable(false);
        final Button Aceptar = (Button) prompstsPresentacion.findViewById(R.id.btnConfirmar);

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPresentacion.dismiss();
            }
        });

        alertPresentacion = builderPresentacion.create();
        alertPresentacion.show();
    }


    private void AlertFornaPago() {
        LayoutInflater li = LayoutInflater.from(context);
        View prompstsPago = li.inflate(R.layout.dialog_formapago, null);
        final AlertDialog.Builder  builderPago = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.RegistrarVenta.this);
        builderPago.setView(prompstsPago);
        //builderPago.setCancelable(false);
        opcion1 = (RadioButton)prompstsPago.findViewById(R.id.contado);
        opcion2 = (RadioButton)prompstsPago.findViewById(R.id.credito);
        final Button Aceptar = (Button) prompstsPago.findViewById(R.id.btnConfirmar);

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opcion1.isChecked()==true){
                    AlertContado();
                    //Toast.makeText(RegistrarVenta.this, "Botón seleccionado: "+opcion1.getText(), Toast.LENGTH_LONG).show();

                }else if (opcion2.isChecked()==true){
                    AlertCredito();
                    //Toast.makeText(RegistrarVenta.this, "Botón seleccionado: "+opcion2.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });

        alertPago =  builderPago.create();
        alertPago.show();
    }


    private void AlertContado() {
        LayoutInflater contado = LayoutInflater.from(context);
        View prompstsContado = contado.inflate(R.layout.dialog_pagocontado, null);
        final AlertDialog.Builder  builderContado = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.RegistrarVenta.this);
        builderContado.setView(prompstsContado);
        builderContado.setCancelable(false);

        final Button Confirmar = (Button) prompstsContado.findViewById(R.id.btnConfirmar);

        Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertContado.dismiss();
                alertPago.dismiss();
            }
        });

        alertContado =  builderContado.create();
        alertContado.show();
    }

    private void AlertCredito() {
        LayoutInflater credito = LayoutInflater.from(context);
        View prompstsCredito = credito.inflate(R.layout.dialog_pagocredito, null);
        final AlertDialog.Builder  builderCredito = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.RegistrarVenta.this);
        builderCredito.setView(prompstsCredito);
        builderCredito.setCancelable(false);

        final Button Confirmar = (Button) prompstsCredito.findViewById(R.id.btnConfirmar);
        final Button Cancelar = (Button) prompstsCredito.findViewById(R.id.btnCancelar);
        final TextView fech = (TextView) prompstsCredito.findViewById(R.id.fch);
        fech.setText(fecha());

        Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertCredito.dismiss();
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertar que limpie el cuadro de la firma
                alertCredito.dismiss();
            }
        });

        alertCredito =  builderCredito.create();
        alertCredito.show();
    }

    //METODO PARA OBTENER FECHA ACTUAL
    private String fecha() {
        final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return fe.format(calendar.getTime());
    }


}
