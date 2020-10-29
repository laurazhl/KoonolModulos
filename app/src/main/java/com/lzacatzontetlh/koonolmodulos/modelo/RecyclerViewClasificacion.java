package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class RecyclerViewClasificacion extends RecyclerView.Adapter<RecyclerViewClasificacion.PaletteViewHolderVenta>  implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<ClasificacionDatos> listClasificacion;


    public RecyclerViewClasificacion(@NonNull ArrayList<ClasificacionDatos> clasificacion) { this.listClasificacion = clasificacion; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rowsproductos, viewGroup, false);
        producto.setOnClickListener(this);
        return new  PaletteViewHolderVenta(producto);

    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(listClasificacion.get(i));

    }

    @Override
    public int getItemCount() {
        return listClasificacion.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    class PaletteViewHolderVenta extends RecyclerView.ViewHolder {
        public TextView nombreClasificacion;
        public ImageView imagenClasificacion;
        public TextView clasificacion;


        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            nombreClasificacion = (TextView) itemView.findViewById(R.id.nombreClasificacion);
            imagenClasificacion =  itemView.findViewById(R.id.imagenClasificacion);
            //  clasificacion = (TextView) itemView.findViewById(R.id.clasi);

        }

        public void bind(ClasificacionDatos clasificacion) {
            nombreClasificacion.setText(clasificacion.getNombreClasificacion());
            //imagenClasificacion.setText(clasificacion.getImagenClasificacion());
            imagenClasificacion.setImageBitmap(clasificacion.imagenClasificacion);
            //imageView.setImageBitmap(productos.imagen);
            //   clasificacion.setText(clasificacion.getClasificacion());

        }
    }
}