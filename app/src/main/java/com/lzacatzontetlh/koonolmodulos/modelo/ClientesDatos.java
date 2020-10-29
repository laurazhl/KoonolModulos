package com.lzacatzontetlh.koonolmodulos.modelo;

public class ClientesDatos {
    String tipoPersona,nombre, telefono;

    public ClientesDatos(){
    }

    public ClientesDatos(String tipoPersona, String nombre, String tel){
        this.tipoPersona=tipoPersona;
        this.nombre=nombre;
        this.telefono=tel;

    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
