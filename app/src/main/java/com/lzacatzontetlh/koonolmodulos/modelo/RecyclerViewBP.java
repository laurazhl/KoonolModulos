package com.lzacatzontetlh.koonolmodulos.modelo;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class RecyclerViewBP extends RecyclerView.Adapter<RecyclerViewBP.PaletteViewHolderVenta> implements View.OnClickListener {
    ArrayList<ProductosDatos> listClasificacion;
    private View.OnClickListener listener;



    public RecyclerViewBP(@NonNull ArrayList<ProductosDatos> clasificacion) {
        this.listClasificacion = clasificacion;
    }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rowsproductoscp, viewGroup, false);
        producto.setOnClickListener(this);
        return new  PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(listClasificacion.get(i));
        paletteViewHolderVenta.botonAgregarProductoC.setOnClickListener(this);
        paletteViewHolderVenta.botonQuitarProductoC.setOnClickListener(this);

    }



    @Override
    public int getItemCount() {
        return listClasificacion.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
        Context context=   view.getContext();
        switch (view.getId()){
            case  R.id.botonMasP:
                System.out.println(" entro a boton  mas P  " +getItemCount());

                Class productos= listClasificacion.getClass();

                System.out.println(" eeeeeeeee  " +productos);


//que mande la posicibion de la listsa osea la posicicon del recycler view poara despues  obtener el dato de esa listya




                view.getContext();
                //  Context context=   view.getContext();

                //Intent intencion = new Intent(context, clasificacionSeleccionada.class);
                // context.startActivity(intencion);



                break;
            case  R.id.botonMenosP:
                //   Intent intencion2 = new Intent(context, MenuGeneral.class);
                //  context.startActivity(intencion2);
                System.out.println(" entro a boton  menos P  " );
                break;

        }
    }

    class PaletteViewHolderVenta extends RecyclerView.ViewHolder {
        public TextView nombreClasificacion;
        public TextView presentacion;
        public ImageView imagenClasificacion;
        public TextView precio;
        ImageButton botonAgregarProductoC;
        ImageButton botonQuitarProductoC;


        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            nombreClasificacion = (TextView) itemView.findViewById(R.id.nombreClasificacion2);
            presentacion = (TextView) itemView.findViewById(R.id.presentacionProducto);
            imagenClasificacion =  itemView.findViewById(R.id.imagenClasificacion2);
            precio = (TextView) itemView.findViewById(R.id.clasi);

            botonAgregarProductoC= itemView.findViewById(R.id.botonMasP);
            botonQuitarProductoC= itemView.findViewById(R.id.botonMenosP);
        }

        public void bind(ProductosDatos clasificacion) {
            nombreClasificacion.setText(clasificacion.getNombreProducto());
            //  imagenClasificacion.setText(clasificacion.getImagenProductoe());
            presentacion.setText(clasificacion.getPresentacion());
            nombreClasificacion.getText();
            imagenClasificacion.setImageBitmap(clasificacion.getImagenProductoe());

            precio.setText(clasificacion.getClasificacion());

        }
    }
}
