package com.lzacatzontetlh.koonolmodulos.modelo;

import android.graphics.Bitmap;

public class ProductosDatos {
    String id,nombreProducto,presentacion, clasificacion;
    Bitmap imagenProducto;

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public ProductosDatos(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductosDatos(String id, String nombreProducto, Bitmap imagenProducto, String clasificacion, String presentacion){
        this.id= id;
        this.nombreProducto=nombreProducto;
        this.imagenProducto=imagenProducto;
        this.clasificacion=clasificacion;
        this.presentacion=presentacion;

    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Bitmap getImagenProductoe() {
        return imagenProducto;
    }

    public void setImagenProducto(Bitmap imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
