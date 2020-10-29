package com.lzacatzontetlh.koonolmodulos;

import com.lzacatzontetlh.koonolmodulos.modelo.CancelacionDatos;
import com.lzacatzontetlh.koonolmodulos.modelo.ProductosDatos;

import java.util.ArrayList;
import java.util.List;

public class Globales {
    //Datos generales
    public String  cajaa2;
    public String  establecimientoo2;
    public String  cajero2;
    public String direccion;

    //MENU GENERAL
    public int apertura;

    //Main Activity
    public String emailUsr;
    public  String claveUsr;
    public String emailUsr2;
    public  String claveUsr2;
    public int Rol;
    public  String Rol2;
    public String id_usuario;
    public String prsFk;
    public String prsNom;
    public String direccion2;

    //Seleccion bodega
    public String  cajaId;
    public String  cajaa;
    public String  establecimientoId;
    public String  establecimientoo;
    public String  empresaNombre;
    public int empresaId;


    //CAJA MENU
    public  int cjusId2;
    public String nombreCjus;
    public String mensajeCorte;
    public double direfe;
    public String cajaOperacion;
    public int categoriaId;
    public String categoriaNom;
    public String YaInserto;
    public String YaInsertoPrd;
    public int prdId;
    public String prdCod;
    public String prdNom;
    public String prdDescorta;
    public String prdDeslarga;
    public int stockMax;
    public int stockMin;
    public int unimed;
    public String prdObs;
    public int prdEsta;
    public int prdEmp;
    public int idCarts;
    public String nomCarts;
    public int clasCarts;
    public int idCartsD;
    public String descCartsD;
    public int cartsFk;
    public int cartsDetFk;
    public int prdFk;
    public int prstId;
    public String prstDescr;
    public int preproId;
    public String preproCg;
    public double preproCom;
    public int preproPrd;
    public int preproPrst;
    public int listId;
    public double listPrc;
    public int listAct;
    public int listSeg;
    public int listPrepro;



    //CORTE DE CAJA
    public double totalEfectivo2;
    public double totalTarjeta2;
    public String claveCU;
    public double montoApertura;
    public double montoTotl;

    public String  cajero;
    public String  caja;
    public String  mont;
    public String  tpmId;
    public String  tpmNom;
    public String  estNom;
    public String cjusId;
    public int canti;

    public int operacion;
    public Double subTotal;
    public Double TotVenta;
    public int idProducto;
    public int cantiOperacion;
    public  int canTotal;
    public int idDetalle;
    public int idStatus;
    public int positionRecycler;
    public String fchaSelect;
    public int actualiSin;
    public String idVentaPrevia;



    public  String idMetodo;
    public int metodoFK;
    //VENTA ANTERIOR
    public double cantidadPago;
    public int idDocm;
    public String statusPago;



    // var lau/
    public String idDocUl;
    public String idFolio;
    public String vDoc;
    public String vFolio;
    public String vExisteP;
    public String vExisteP2;
    public String  elementosExistentesEnDD;
    public String vNumDoc;
    public String vNumDoc2;
    public String vCPE;
    public String vExisteCantidad;
    public String ineFrontal;
    public String ineInversa;
    public String nombreUsuario;
    public String nombreUsuarioCancelacion;
    public String folioVenta;
    public String idEmpresaLau;
    public String idEstablecimientoLau;
    public String idCajaLau;
    public String nomEst;
    public String idCajaCancelacion;
    public String  idUsuarioCancelacion;
    public String  idEstablecimientoCancelacion;
    public int  idEstatusLau;
    public int  idEstatusPorPagar;
    public int  idEstatusPagado;
    public int  idEstatusCancelado;
    public int   idEstatusHabilitado;

    public int  idVentaalPublico;
    public int  idTpdCancela;
    public int  idTpdVenta;
    public String idEmpresaCancel;
    public String idEstablecimientoCancel;
    public String idCajaCancel;
    public String folioCancel;
    public String idUltimoDocDCancelacion;
    public String cancelDLV;
    public String nombreEstCancel;
    public String subTCancel;
    public String ivaCancel;
    public String totalCancel;
    public String mensajeEnT;
    public String cajeroEnT;
    public String existeAD;
    public int idRolCajero;
    public int idRolAdministrador;
    public int idRolSuperAdmi;
    public String tipoDeUsuario;
    public String fechas;
    public String renglon;

    public  String vglobalE="";
    public  String nombrePersonaACargar="";
    public  String botonAtras="";
    public String  fechaNacimientoOCR;

    public int regresarAConultCliente=0;

    public List<ProductosDatos> listclientes = new ArrayList<ProductosDatos>();
    public List<CancelacionDatos> listaDatosCancelacion = new ArrayList<CancelacionDatos>();
    public List<CancelacionDatos> listaRV = new ArrayList<CancelacionDatos>();

    private static Globales instance = new Globales();

    public static Globales getInstance() { return instance; }

    public static void setInstance(Globales  instance) {
        Globales.instance = instance;
    }

}


