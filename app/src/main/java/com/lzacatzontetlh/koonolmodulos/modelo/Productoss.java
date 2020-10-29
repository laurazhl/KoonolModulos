package com.lzacatzontetlh.koonolmodulos.modelo;

import android.graphics.Bitmap;

public class Productoss {
    public String nombre, idProducto;
    public int cantidad;
    public double precio;
    public double subtotal;
    public double total;
    Bitmap imagen;
    public String presentacionP;

    public String getPresentacionP() {
        return presentacionP;
    }

    public void setPresentacionP(String presentacionP) {
        this.presentacionP = presentacionP;
    }

    public Productoss(){

    }

    public Productoss(Bitmap imagen, String nombre, int cantidad, double precio, double subtotal, String presentacionP){
        //this.id = id;
        this.imagen = imagen;
        this.nombre=nombre;
        this.cantidad=cantidad;
        this.precio=precio;
        this.subtotal=subtotal;
        this.presentacionP=presentacionP;
        //this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}




