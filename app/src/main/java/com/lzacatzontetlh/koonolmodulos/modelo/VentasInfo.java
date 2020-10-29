package com.lzacatzontetlh.koonolmodulos.modelo;

public class VentasInfo {
    public String folioVenta, status;
    public double total;


    public VentasInfo(){
    }

    public VentasInfo(String folioVenta, double total, String status){
        this.folioVenta=folioVenta;
        this.total=total;
        this.status=status;
    }

    public String getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(String folioVenta) {
        this.folioVenta = folioVenta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}




