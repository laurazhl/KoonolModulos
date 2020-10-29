package com.lzacatzontetlh.koonolmodulos.modelo;

public class CancelacionDatos {
    String idDC, horaDC, folioVenta,fechav,caja, estatus,totalVenta;

    public CancelacionDatos(String idDC, String horaDC, String folioVenta, String fechav, String caja, String estatus, String totalVenta) {
        this.idDC = idDC;
        this.horaDC = horaDC;
        this.folioVenta = folioVenta;
        this.fechav = fechav;
        this.caja = caja;
        this.estatus = estatus;
        this.totalVenta = totalVenta;
    }

    public String getIdDC() {
        return idDC;
    }

    public void setIdDC(String idDC) {
        this.idDC = idDC;
    }

    public String getHoraDC() {
        return horaDC;
    }

    public void setHoraDC(String horaDC) {
        this.horaDC = horaDC;
    }

    public String getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(String folioVenta) {
        this.folioVenta = folioVenta;
    }

    public String getFechav() {
        return fechav;
    }

    public void setFechav(String fechav) {
        this.fechav = fechav;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(String totalVenta) {
        this.totalVenta = totalVenta;
    }
}
