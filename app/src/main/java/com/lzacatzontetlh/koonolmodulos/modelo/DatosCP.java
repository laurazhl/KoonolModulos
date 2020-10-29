package com.lzacatzontetlh.koonolmodulos.modelo;

public class DatosCP {


    String cp_id, cp_codigopostal;
    String cp_estado;
    String cp_municipio;
    String cp_asentamiento;
    String cp_ciudad;


    public DatosCP(String cp_id, String cp_codigopostal, String cp_estado, String cp_municipio, String cp_asentamiento, String cp_ciudad) {
        this.cp_id = cp_id;
        this.cp_codigopostal = cp_codigopostal;
        this.cp_estado = cp_estado;
        this.cp_municipio = cp_municipio;
        this.cp_asentamiento = cp_asentamiento;
        this.cp_ciudad = cp_ciudad;
    }

    public String getCp_codigopostal() {
        return cp_codigopostal;
    }

    public void setCp_codigopostal(String cp_codigopostal) {
        this.cp_codigopostal = cp_codigopostal;
    }

    public String getCp_id() {
        return cp_id;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public String getCp_estado() {
        return cp_estado;
    }

    public void setCp_estado(String cp_estado) {
        this.cp_estado = cp_estado;
    }

    public String getCp_municipio() {
        return cp_municipio;
    }

    public void setCp_municipio(String cp_municipio) {
        this.cp_municipio = cp_municipio;
    }

    public String getCp_asentamiento() {
        return cp_asentamiento;
    }

    public void setCp_asentamiento(String cp_asentamiento) {
        this.cp_asentamiento = cp_asentamiento;
    }

    public String getCp_ciudad() {
        return cp_ciudad;
    }

    public void setCp_ciudad(String cp_ciudad) {
        this.cp_ciudad = cp_ciudad;
    }
}
