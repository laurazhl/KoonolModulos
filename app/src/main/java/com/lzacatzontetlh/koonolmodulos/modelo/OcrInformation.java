package com.lzacatzontetlh.koonolmodulos.modelo;

public class OcrInformation {
    public String idPF;
    public String namePF;
    public String fnPF;
    public String curpPF;
    public String namecalle;
    public String sexo;
    public String claveElec;
    public String anioR;
    public String estado;
    public String muni;
    public String seccion;
    public String localida;
    public String emision;
    public String vigencia;
    public String origen;
    public String info;
    public String tel;
    public String email;
    public String actividad;


    public OcrInformation(){
    }

    public OcrInformation(String idPF, String namePF, String fnPF, String curpPF, String namecalle, String sexo, String claveElec, String anioR, String estado,
                          String muni, String seccion, String localida, String emision, String vigencia, String origen, String info, String tel, String email, String actividad){
        this.idPF = idPF;
        this.namePF = namePF;
        this.fnPF = fnPF;
        this.curpPF = curpPF;
        this.namecalle = namecalle;
        this.sexo = sexo;
        this.claveElec = claveElec;
        this.anioR = anioR;
        this.estado = estado;
        this.muni = muni;
        this.seccion = seccion;
        this.localida = localida;
        this.emision = emision;
        this.vigencia = vigencia;
        this.origen = origen;
        this.info = info;
        this.tel = tel;
        this.email = email;
        this.actividad = actividad;
    }

    public String getIdPF() {
        return idPF;
    }

    public void setIdPF(String idPF) {
        this.idPF = idPF;
    }

    public String getNamePF() {
        return namePF;
    }

    public void setNamePF(String namePF) {
        this.namePF = namePF;
    }

    public String getFnPF() {
        return fnPF;
    }

    public void setFnPF(String fnPF) {
        this.fnPF = fnPF;
    }

    public String getCurpPF() {
        return curpPF;
    }

    public void setCurpPF(String curpPF) {
        this.curpPF = curpPF;
    }

    public String getNamecalle() {
        return namecalle;
    }

    public void setNamecalle(String namecalle) {
        this.namecalle = namecalle;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getClaveElec() {
        return claveElec;
    }

    public void setClaveElec(String claveElec) {
        this.claveElec = claveElec;
    }

    public String getAnioR() {
        return anioR;
    }

    public void setAnioR(String anioR) {
        this.anioR = anioR;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMuni() {
        return muni;
    }

    public void setMuni(String muni) {
        this.muni = muni;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getLocalida() {
        return localida;
    }

    public void setLocalida(String localida) {
        this.localida = localida;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTel() {
        return tel;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CURP: " +curpPF + "\nNombre: "+namePF ;
        //return "RFC: " +rfcPF + "\nNombre: "+namePF + "\nDirección: " +namecalle + "\nColonia: " +coloniaPF;
    }

}

