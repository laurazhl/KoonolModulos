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

public class RecyclerViewProductosCS extends RecyclerView.Adapter<RecyclerViewProductosCS.PaletteViewHolderVenta>{
    ArrayList<ProductosDatos> listProductos;


    public RecyclerViewProductosCS(@NonNull ArrayList<ProductosDatos> productos) { this.listProductos = productos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rowsproductos, viewGroup, false);
        return new  PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(listProductos.get(i));
    }

    @Override
    public int getItemCount() {
        return listProductos.size();
    }


    class PaletteViewHolderVenta extends RecyclerView.ViewHolder {
        public TextView nombreProducto;
        public ImageView imagenProducto;
        public TextView clasificacion;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            nombreProducto = (TextView) itemView.findViewById(R.id.nombre);
            imagenProducto = itemView.findViewById(R.id.imagen);
            clasificacion = (TextView) itemView.findViewById(R.id.clasi);
        }

        public void bind(ProductosDatos productos) {
            nombreProducto.setText(productos.getNombreProducto());
          //  imagenProducto.setText(productos.getImagenProductoe());
            imagenProducto.setImageBitmap(productos.getImagenProductoe());
            clasificacion.setText(productos.getClasificacion());

        }
    }
}
