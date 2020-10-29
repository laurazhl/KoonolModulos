package com.lzacatzontetlh.koonolmodulos.modelo;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class GridAdapterProductos extends BaseAdapter {
    public String id="";

    private Context context;

    private ArrayList<ProductosDatos> arrayList;
    //private  ArrayList<String> arrayList;

    private View.OnClickListener listener;

    public GridAdapterProductos(Context context, ArrayList<ProductosDatos> arrayList){
        this.context = context;

        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_rowsproductoscs, null);
            holder = new ViewHolder();


            ProductosDatos productos= arrayList.get(position);
            final TextView tituloTv = convertView.findViewById(R.id.nombre);
            tituloTv.setText(productos.getNombreProducto());
            String v9= productos.getNombreProducto();
            final TextView precio =  convertView.findViewById(R.id.clasiP);
            precio.setText(productos.getClasificacion());
            String v3= productos.getClasificacion();
            ImageView imageView =  convertView.findViewById(R.id.imagen);
            imageView.setImageBitmap(productos.getImagenProductoe());


            TextView presentacion= convertView.findViewById(R.id.presentacionProducto);
            presentacion.setText(productos.getPresentacion());

            TextView id= convertView.findViewById(R.id.idid);
            id.setText(productos.getId());
            // id= productos.getId();
            String v2= productos.getId();


            holder.botonAgregarProductoC= convertView.findViewById(R.id.botonMasP1);
            holder.botonQuitarProductoC=  convertView.findViewById(R.id.botonMenosP1);
            holder.detallesProducto=  convertView.findViewById(R.id.imagen);
            final View finalConvertView = convertView;


            holder.detallesProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductosDatos productos= arrayList.get(position);
                    TextView id= finalConvertView.findViewById(R.id.idid);
                    id.setText(productos.getId());
                    String idddd= productos.getId();
                    Ingresarsql sq = new Ingresarsql();
                    sq.consultaestatusHabilitado(context);
                    int idEHabilitado=Globales.getInstance().idEstatusHabilitado;
                    ConexionSQLiteHelper conn;
                    conn=new ConexionSQLiteHelper(context);
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
                                LayoutInflater imagenadvertencia_alert = LayoutInflater.from(context);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                                alerta.setTitle("Información del producto");
                                //alerta.setMessage("CÃ³digo: " + codigo+ "\nNombre del producto: " +nombreProducto+ "\nPresentacion"+ presentacion+ "\nLista de precios: "+ segmento+ "\nPrecio: " + precio).setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar",null) ;alerta.show();
                                alerta.setMessage("Código: " + codigo+ "\nNombre del producto: " +nombreProducto+ "\nPresentación: "+ presentacion+ "\nPrecio venta: $" + precio).setCancelable(true).setPositiveButton("Cerrar",null) ;alerta.show();

                            }
                            else
                            {
                                Toast.makeText(context, "No se puede mostrar más información." , Toast.LENGTH_SHORT).show();

                            }
                        }
                        cursor2.close();
                        db.close();
                    }catch(Exception e){
                        Log.println(Log.ERROR,"Error ",e.getMessage());
                    }
                }
            });



            holder.botonAgregarProductoC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    /////////////////
                    ProductosDatos productos= arrayList.get(position);
                    final TextView tituloTv = finalConvertView.findViewById(R.id.nombre);
                    tituloTv.setText(productos.getNombreProducto());
                    String v9= productos.getNombreProducto();
                    final TextView precio =  finalConvertView.findViewById(R.id.clasiP);
                    precio.setText(productos.getClasificacion());
                    String v3= productos.getClasificacion();
                    ImageView imageView =  finalConvertView.findViewById(R.id.imagen);
                    imageView.setImageBitmap(productos.getImagenProductoe());

                    TextView presentacion= finalConvertView.findViewById(R.id.presentacionProducto);
                    presentacion.setText(productos.getPresentacion());
                    String pre=productos.getPresentacion();



                    TextView id= finalConvertView.findViewById(R.id.idid);
                    id.setText(productos.getId());




                    //id= productos.getId();
                    String v2= productos.getId();
                    /////////////////


                    final Ingresarsql sq = new Ingresarsql();
                    Context contexto= view.getContext();
                    final String iden=id.getText().toString();
                    String precioE= String.valueOf( precio.getText());

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
                    String doc_fecha=fecha();
                    String doc_hora= hora();
                    String doc_iva="0";//No oficial dato desconocido


                    String docd_cosind ="0"; //No oficial dato desconocido            costo indirecto           <
                    String doc_observ="0";//OFICIAL
                    String doc_saldo="0";//OFICIAL


                    sq.consultarIDVentaPublico(contexto);
                    String prs_fk= String.valueOf(Globales.getInstance().idVentaalPublico);
                    //  String prs_fk="545";//"Venta al pÃºblico";//OFICIAL pero preguntar si solo es el id
                    String usr_fk= Globales.getInstance().id_usuario;
                    String cja_fk= Globales.getInstance().idCajaLau;
                    String est_fk= Globales.getInstance().idEstablecimientoLau;
                    sq.consultaestatus(contexto);
                    String esta_fk= String.valueOf(Globales.getInstance().idEstatusLau);//OFICIAL

                    sq.consultartpdVenta(contexto);
                    String tpd_fk= String.valueOf(Globales.getInstance().idTpdVenta);

                    sq.registrarVentaProceso(docd_cantprod,docd_precven,docd_preccom, docd_descuento,prepro_fk,ext_fk, imp_fk ,Globales.getInstance().idFolio, doc_fecha,doc_hora, doc_iva ,docd_cosind ,doc_observ,doc_saldo,prs_fk,usr_fk,cja_fk,est_fk,esta_fk,tpd_fk,contexto);

                    sq.contabilizaLosProdAgreCarr(contexto);
                }
            });


            holder.botonQuitarProductoC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    /////////////////
                    ProductosDatos productos= arrayList.get(position);
                    final TextView tituloTv = finalConvertView.findViewById(R.id.nombre);
                    tituloTv.setText(productos.getNombreProducto());
                    final TextView precio =  finalConvertView.findViewById(R.id.clasiP);
                    precio.setText(productos.getClasificacion());
                    ImageView imageView =  finalConvertView.findViewById(R.id.imagen);
                    imageView.setImageBitmap(productos.getImagenProductoe());

                    TextView presentacion= finalConvertView.findViewById(R.id.presentacionProducto);
                    presentacion.setText(productos.getPresentacion());




                    TextView id= finalConvertView.findViewById(R.id.idid);
                    id.setText(productos.getId());



                    // id= productos.getId();
                    /////////////////





                    final Ingresarsql sq = new Ingresarsql();
                    Context contexto= view.getContext();
                    final String iden=id.getText().toString();
                    String precioE= String.valueOf( precio.getText());;
                    String prepro_fk2=iden;
                    String docd_cantprod2="";   //cantidad de productos en el carrito
                    String docd_precven2=precioE;
                    sq.existeCanti(contexto,id.getText().toString());

                    if(  Globales.getInstance().vExisteCantidad.equals("existeCantidad")){
                        int v = Integer.parseInt(Globales.getInstance().vCPE);
                        if(v==1){
                            existe(contexto,prepro_fk2);
                            sq.elementosExistentesEnDDe(contexto);
                            int elem= Integer.parseInt(Globales.getInstance().elementosExistentesEnDD);

                            if  (elem>1){
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


                    sq.contabilizaLosProdAgreCarr(contexto);
                }
            });
            convertView.setTag(holder);
        }
        return convertView;
    }

    class ViewHolder {
        ImageButton botonAgregarProductoC;
        ImageButton botonQuitarProductoC;
        ImageView detallesProducto;
    }


}