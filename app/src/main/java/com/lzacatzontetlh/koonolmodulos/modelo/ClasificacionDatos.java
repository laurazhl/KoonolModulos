package com.lzacatzontetlh.koonolmodulos.modelo;

import android.graphics.Bitmap;

public class ClasificacionDatos {
    String nombreClasificacion,identificador;
    Bitmap imagenClasificacion;

    public ClasificacionDatos() {
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public ClasificacionDatos(Bitmap imagenClasificacion, String nombreClasificacion, String identificador) {
        this.nombreClasificacion = nombreClasificacion;
        this.imagenClasificacion = imagenClasificacion;
        this.identificador = identificador;
    }

    public Bitmap getImagenClasificacion() {
        return imagenClasificacion;
    }

    public void setImagenClasificacion(Bitmap imagenClasificacion) {
        this.imagenClasificacion = imagenClasificacion;
    }

    public String getNombreClasificacion() {
        return nombreClasificacion;
    }

    public void setNombreClasificacion(String nombreClasificacion) {
        this.nombreClasificacion = nombreClasificacion;
    }
}

   
