package com.lzacatzontetlh.koonolmodulos.modelo;

public class Utilidades {


    //CONSTANTES campos tabla actividades
    /*public static final String TABLA_PRODUCTOS = "productos";
    public static final String CAMPO_ID= "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_PRESENTACION = "presentacion";
    public static final String CAMPO_CALIDAD = "calidad";
    public static final String CAMPO_ABREVIATURA = "abreviatura";
    public static final String CAMPO_TIPO = "tipo_mercado";
    public static final String CAMPO_MODULO = "modulo";
    public static final String CAMPO_MODULOO = "moduloo";

    public static final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE "+TABLA_PRODUCTOS+"("+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_PRESENTACION+" TEXT, "+CAMPO_CALIDAD+" TEXT, "+CAMPO_ABREVIATURA+" TEXT)";*/



    //CONSTANTES campos tabla usuario
    public static final String TABLA_USUARIO = "usuario";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE= "nombre";
    public static final String CAMPO_TELEFONO = "telefono";

    public static final String CREAR_TABLA_USUARIO = "CREATE TABLE "+TABLA_USUARIO+"("+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_TELEFONO+" TEXT)";


    //CONSTANTES campos tabla cp
    public static final String TABLA_CP = "cp";
    public static final String CAMPO_IDCP = "id";
    public static final String CAMPO_IDcp = "numcp";
    public static final String CAMPO_STATE = "nameState";
    public static final String CAMPO_MUNI = "muni";
    public static final String CAMPO_ASENTA = "col";
    public static final String CAMPO_TIPO = "tipo";


    //CONSTANTES campos tabla todos cp PUEBLA
    public static final String TABLA_CPPUE1 = "cppue1";
    public static final String CAMPO_IDCPA = "id";
    public static final String CAMPO_CLAVE = "clave";
    public static final String CAMPO_IDcpA = "numcp";
    public static final String CAMPO_STATEA = "nameState";
    public static final String CAMPO_MUNIA = "muni";
    public static final String CAMPO_ASENTAA = "col";
    public static final String CAMPO_TIPOA = "tipo";
    public static final String CAMPO_PK = "pk";

    public static final String CREAR_TABLA_CP = "CREATE TABLE "+TABLA_CP+"("+CAMPO_ID+" INTEGER, "+CAMPO_CLAVE+" TEXT, "+CAMPO_IDcp+" TEXT, "+CAMPO_STATEA+" TEXT, "+CAMPO_MUNIA+" TEXT, "+CAMPO_ASENTAA+" TEXT, "+CAMPO_TIPOA+" TEXT, "+CAMPO_PK+" INTEGER)";



    //CONSTANTES campos tabla actividades
    public static final String TABLA_ACTIVITY = "actividades";
    public static final String CAMPO_IDACT= "act_id";
    public static final String CAMPO_CODIGO = "act_codigo";
    public static final String CAMPO_TITULO = "act_titulo";
    public static final String CAMPO_DESCRIPCION = "act_descripcion";
    public static final String CAMPO_TIPOACT = "act_tipo";
    public static final String CAMPO_CODIGOUIF = "codigo_uif";
    public static final String CAMPO_VALOR = "valor";
    public static final String CAMPO_PUNTAJE = "puntaje";

    public static final String CREAR_TABLA_ACTIVITY = "CREATE TABLE "+TABLA_ACTIVITY+"("+CAMPO_IDACT+" INTEGER, "+CAMPO_CODIGO+" TEXT, "+CAMPO_TITULO+" TEXT, "+CAMPO_DESCRIPCION+" TEXT, "+CAMPO_TIPOACT+" TEXT, "+CAMPO_CODIGOUIF+" TEXT, "+CAMPO_VALOR+" REAL, "+CAMPO_PUNTAJE+" REAL)";


    //CONSTANTES campos tabla colonias
    public static final String TABLA_COLONIAS = "colonias";
    public static final String CAMPO_ID2= "col_id";
    public static final String CAMPO_CVENTI= "col_cvEnti";
    public static final String CAMPO_NOMBRENTI = "col_nombrEnti";
    public static final String CAMPO_CVEDISTRITO = "col_cveDistrito";
    public static final String CAMPO_CABECERA = "col_cabecera";
    public static final String CAMPO_CVESECCION = "col_cveSeccion";
    public static final String CAMPO_CVEMUNI = "col_cveMuni";
    public static final String CAMPO_NOMBREMUNI = "col_nombreMuni";
    public static final String CAMPO_TIPO2 = "col_tipo";
    public static final String CAMPO_NOMBRE2 = "col_nombre";
    public static final String CAMPO_CP = "col_cp";

    public static final String CREAR_TABLA_COLONIAS= "CREATE TABLE "+TABLA_COLONIAS+"("+CAMPO_ID2+" INTEGER, "+CAMPO_CVENTI+" INTEGER, "+CAMPO_NOMBRENTI+" TEXT, "+CAMPO_CVEDISTRITO+" INTEGER, "+CAMPO_CABECERA+" TEXT, "+CAMPO_CVESECCION+" INTEGER, "+CAMPO_CVEMUNI+" INTEGER, "+CAMPO_NOMBREMUNI+" TEXT, "+CAMPO_TIPO2+" TEXT, "+CAMPO_NOMBRE2+" TEXT, "+CAMPO_CP+" TEXT)";



    //var lau
    public static final int LIST=1;
    public static final int GRID=2;
    public static int visualizacion=LIST;


    //CONSTANTES campos tabla imagen
    public static final String TABLA_IMAGEN = "imagen";
    public static final String CAMPO_IMAGENID = "id_imagen";
    public static final String CAMPO_IMAGENFK_CLIENTE = "fk_cliente";
    public static final String CAMPO_IMAGENTIPO = "imagenTipo";
    public static final String CAMPO_IMAGENARCHIVO = "imagenArchivo";
    public static final String CAMPO_IMAGENRUTA = "imagenRuta";
    public static final String CAMPO_IMAGENSINCRONIZADO ="Sincronizado";


    //CONSTANTES campos tabla Clientes
    public static final String TABLA_CLIENTES = "Cliente";
    public static final String CAMPO_CLIENTEID = "id_cliente";
    public static final String CAMPO_CLIENTENOMBRE = "nombre";
    public static final String CAMPO_CLIENTEDIRECCION = "direccion";
    public static final String CAMPO_CLIENTESINCRONIZADO ="Sincronizado";

}
