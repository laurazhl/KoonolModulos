package com.lzacatzontetlh.koonolmodulos.modelo;

import android.graphics.Bitmap;

public class detallesCancelacionDatos {
    public String productoDC, precioDC,cantidadDC,importeDC;
    Bitmap imagen;

    public detallesCancelacionDatos(String productoDC, String precioDC, String cantidadDC, String importeDC, Bitmap imagen) {
        this.productoDC = productoDC;
        this.precioDC = precioDC;
        this.cantidadDC = cantidadDC;
        this.importeDC = importeDC;
        this.imagen = imagen;
    }

    public String getProductoDC() {
        return productoDC;
    }

    public void setProductoDC(String productoDC) {
        this.productoDC = productoDC;
    }

    public String getPrecioDC() {
        return precioDC;
    }

    public void setPrecioDC(String precioDC) {
        this.precioDC = precioDC;
    }

    public String getCantidadDC() {
        return cantidadDC;
    }

    public void setCantidadDC(String cantidadDC) {
        this.cantidadDC = cantidadDC;
    }

    public String getImporteDC() {
        return importeDC;
    }

    public void setImporteDC(String importeDC) {
        this.importeDC = importeDC;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
