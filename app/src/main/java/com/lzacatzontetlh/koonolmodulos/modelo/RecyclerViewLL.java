package com.lzacatzontetlh.koonolmodulos.modelo;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewLL extends RecyclerView.Adapter<RecyclerViewLL.PaletteViewHolderVenta>  {
    ArrayList<ProductosDatos> listClasificacion;
    private View.OnClickListener listener;



    public RecyclerViewLL(@NonNull ArrayList<ProductosDatos> clasificacion) {
        this.listClasificacion = clasificacion;
    }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rowsproductoscp, viewGroup, false);
        // producto.setOnClickListener(this);
        return new  PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(listClasificacion.get(i));
        paletteViewHolderVenta.setOnClickListener();


    }



    @Override
    public int getItemCount() {
        return listClasificacion.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }


    class PaletteViewHolderVenta extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombreClasificacion;
        public ImageView imagenClasificacion;
        public TextView presentacion;
        public TextView precio;
        public TextView id;
        ImageView detallesProducto;
        ImageButton botonAgregarProductoC;
        ImageButton botonQuitarProductoC;


        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            nombreClasificacion = (TextView) itemView.findViewById(R.id.nombreClasificacion2);
            presentacion = (TextView) itemView.findViewById(R.id.presentacionProducto);
            imagenClasificacion =  itemView.findViewById(R.id.imagenClasificacion2);
            id= (TextView) itemView.findViewById(R.id.idid);
            precio = (TextView) itemView.findViewById(R.id.clasi);
            botonAgregarProductoC= itemView.findViewById(R.id.botonMasP);
            botonQuitarProductoC= itemView.findViewById(R.id.botonMenosP);
            detallesProducto=  itemView.findViewById(R.id.imagenClasificacion2);
        }

        void setOnClickListener(){
            botonAgregarProductoC.setOnClickListener(this);
            botonQuitarProductoC.setOnClickListener(this);
            detallesProducto.setOnClickListener(this);
        }

        public void bind(ProductosDatos clasificacion) {
            nombreClasificacion.setText(clasificacion.getNombreProducto());
            //  imagenClasificacion.setText(clasificacion.getImagenProductoe());
            presentacion.setText(clasificacion.getPresentacion());
            //nombreClasificacion.getText();
            imagenClasificacion.setImageBitmap(clasificacion.getImagenProductoe());
            //id= clasificacion.getId();
            id.setText(clasificacion.getId());

            precio.setText(clasificacion.getClasificacion());

        }
        private String fecha() {
            final SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            return fe.format(calendar.getTime());
        }
        private String hora() {
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String datetime = dateFormat.format(c.getTime());
            return datetime;
        }

        private void existe(Context contexto, String query) {
            ConexionSQLiteHelper conn;
            conn=new ConexionSQLiteHelper(contexto);
            SQLiteDatabase db = conn.getReadableDatabase();
            String id="";
            Cursor cursor2 =db.rawQuery("SELECT prepro_fk,docd_cantprod,doc_fk FROM documento_det WHERE prepro_fk ='"+query+"'" , null);
            try {
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int index = 0;
                    while (!cursor2.isAfterLast()) {
                        Globales.getInstance().vNumDoc2= String.valueOf( cursor2.getString(cursor2.getColumnIndex("doc_fk")));
                        Globales.getInstance().vExisteP2 = "existeElProducto";

                        index++;
                        cursor2.moveToNext();
                    }
                    if (index != 0) {
                        System.out.println("F  existeD" );
                    }
                    else
                    {
                        if(id.equals("")) {
                            Globales.getInstance().vExisteP2= "noExisteElProducto";
                        }
                    }
                }
            }catch(Exception e){
                Log.println(Log.ERROR,"",e.getMessage());
            }
        }



        @Override
        public void onClick(View view) {
            final Ingresarsql sq = new Ingresarsql();
            Context contexto= view.getContext();
            String iden=id.getText().toString();
            String precioE= String.valueOf( precio.getText());

            switch (view.getId()){
                case  R.id.botonMasP:

                    String docd_cantprod="";   //cantidad de productos en el carrito
                    sq.existeCanti(contexto,id.getText().toString());
                    if(  Globales.getInstance().vExisteCantidad.equals("existeCantidad")){
                        int v= Integer.parseInt(Globales.getInstance().vCPE);
                        int docd_cantprod1=  v+1;
                        docd_cantprod= String.valueOf(docd_cantprod1);
                    }else {
                        if(  Globales.getInstance().vExisteCantidad.equals("noExisteCantidad")) {
                            docd_cantprod= "1";
                        }
                    }
                    String docd_precven=precioE; // precio unitario del producto agregado.          <
                    String docd_preccom= precioE;  //precio de venta del producto ingresado         <
                    String docd_descuento= "0";  //OFICIAL descuento
                    String prepro_fk=iden; //  id del producto agregado al carrito
                    String ext_fk= "0"; //OFICIAL


                    String imp_fk ="0"; //OFICIAL


                    //  String f1=sq.generarFolio(contexto);
                   /* String f1= "123456789101";
                    System.out.println(" f1 "+ f1);
                    Globales.getInstance().folioVenta= f1;
                    Globales.getInstance().idFolio= f1;*/


                    String doc_fecha=fecha();
                    String doc_hora= hora();
                    String doc_iva="0";//No oficial dato desconocido
                    String docd_cosind ="0"; //No oficial dato desconocido            costo indirecto           <
                    String doc_observ="0";//OFICIAL
                    String doc_saldo="0";//OFICIAL

                    sq.consultarIDVentaPublico(contexto);
                    String prs_fk= String.valueOf(Globales.getInstance().idVentaalPublico);
                    // String prs_fk="545";//"Venta al pÃºblico";//OFICIAL pero preguntar si solo es el id
                    String usr_fk= Globales.getInstance().id_usuario; //modificar valor  llave foranea de la tabla persona
                    String cja_fk= Globales.getInstance().idCajaLau; //modificar valor  llave foranea de la tabla caja
                    String est_fk= Globales.getInstance().idEstablecimientoLau; //modificar valor  llave foranea de la tabla establecimietno
                    sq.consultaestatus(contexto);
                    String esta_fk= String.valueOf(Globales.getInstance().idEstatusLau);//OFICIAL

                    sq.consultartpdVenta(contexto);
                    String tpd_fk= String.valueOf(Globales.getInstance().idTpdVenta);

                    sq.registrarVentaProceso(docd_cantprod,docd_precven,docd_preccom, docd_descuento,prepro_fk,ext_fk, imp_fk ,Globales.getInstance().idFolio, doc_fecha,doc_hora, doc_iva ,docd_cosind ,doc_observ,doc_saldo,prs_fk,usr_fk,cja_fk,est_fk,esta_fk,tpd_fk,contexto);
                    break;
                case  R.id.botonMenosP:
                    String prepro_fk2=iden;
                    String docd_cantprod2="";
                    String docd_precven2=precioE;
                    sq.existeCanti(contexto,id.getText().toString());

                    if(  Globales.getInstance().vExisteCantidad.equals("existeCantidad")){
                        int v = Integer.parseInt(Globales.getInstance().vCPE);
                        if(v==1){
                            existe(contexto,prepro_fk2);
                            sq.elementosExistentesEnDDe(contexto);
                            int elem= Integer.parseInt(Globales.getInstance().elementosExistentesEnDD);

                            if(elem>1){
                                sq.ActualizarVentaEli(contexto,prepro_fk2);//elimino el producto  de la tabla detalle
                            }

                            if(elem==1){
                                String doc_id= Globales.getInstance().idDocUl;
                                //solo elimina el producto que se le esta restanodo
                                sq.ActualizarV(contexto,prepro_fk2,doc_id);//elimino el producto  de la tabla detalle
                            }
                        }
                        else {
                            int docd_cantprod1 = v - 1;
                            docd_cantprod2 = String.valueOf(docd_cantprod1);
                            sq.ActualizarVenta(contexto,prepro_fk2, docd_cantprod2, docd_precven2);
                        }
                    }else {
                        if(  Globales.getInstance().vExisteCantidad.equals("noExisteCantidad")) {

                            System.out.println("El producto no ha sido insertado no hay necesidad de eliminar "+ docd_cantprod2);
                        }
                    }


                    break;
                case R.id.imagenClasificacion2:
                    String idddd=iden;
                    sq.consultaestatusHabilitado(contexto);
                    int idEHabilitado=Globales.getInstance().idEstatusHabilitado;
                    ConexionSQLiteHelper conn;
                    conn=new ConexionSQLiteHelper(contexto);
                    SQLiteDatabase db = conn.getReadableDatabase();
                    String codigo="",nombreProducto="",presentacion="", precio="", segmento="" ;
                    Cursor cursor2 =db.rawQuery("SELECT prepro_id,prst_descripcion,  prd_nombre, lstp_precio, prst_fk, prd_nombre , prd_codigo\n" +
                            "                FROM prest_prod \n" +
                            "                INNER JOIN producto On producto.prd_id= prest_prod.prd_fk \n" +
                            "                INNER JOIN presentacion ON  presentacion.prst_id= prest_prod.prst_fk  \n" +
                            "INNER JOIN listaprecio ON listaprecio.prepro_fk=prest_prod.prepro_id\n" +
                            "WHERE  prepro_id='"+idddd+"' and producto.esta_fk='"+idEHabilitado+"'",null);


                    //			INNER JOIN segmento on segmento.seg_id = listaprecio.seg_fk agrgar ala consulta cuando quieran segmento
                    try {
                        if (cursor2 != null) {
                            cursor2.moveToFirst();
                            int index = 0;
                            while (!cursor2.isAfterLast()) {
                                codigo= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_codigo")));
                                nombreProducto= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prd_nombre")));
                                presentacion= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prst_descripcion")));
                                precio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("lstp_precio")));
                                //segmento= String.valueOf( cursor2.getString(cursor2.getColumnIndex("seg_nombre")));
                                index++;
                                cursor2.moveToNext();
                            }
                            if (index != 0) {
                                LayoutInflater imagenadvertencia_alert = LayoutInflater.from(contexto);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                AlertDialog.Builder alerta = new AlertDialog.Builder(contexto);
                                alerta.setTitle("Información del producto");
                                //alerta.setMessage("CÃ³digo: " + codigo+ "\nNombre del producto: " +nombreProducto+ "\nPresentacion"+ presentacion+ "\nLista de precios: "+ segmento+ "\nPrecio: " + precio).setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar",null) ;alerta.show();
                                alerta.setMessage("Código: " + codigo+ "\nNombre del producto: " +nombreProducto+ "\nPresentación: "+ presentacion+ "\nPrecio venta: $" + precio).setCancelable(true).setPositiveButton("Cerrar",null) ;alerta.show();

                            }
                            else
                            {
                                Toast.makeText(contexto, "No se puede mostrar mÃ¡s informaciÃ³n." , Toast.LENGTH_SHORT).show();

                            }
                        }
                        cursor2.close();
                        db.close();
                    }catch(Exception e){
                        Log.println(Log.ERROR,"Error ",e.getMessage());
                    }
                    break;



            }
        }




    }
}