package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;
import com.lzacatzontetlh.koonolmodulos.venta_anterior_reimpirme;

import java.util.ArrayList;

public class VentaDetAdapter extends RecyclerView.Adapter<VentaDetAdapter.PaletteViewHolderVenta>{
    ArrayList<Productoss> productos;

    public VentaDetAdapter(venta_anterior_reimpirme venta_anterior_reimpirme, @NonNull ArrayList<Productoss> productos) { this.productos = productos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_vnt_det, viewGroup, false);
        return new PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(productos.get(i));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }


    class PaletteViewHolderVenta extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public TextView nombreProd;
        public EditText cantEdit;
        public TextView precio;
        public TextView subtotal;


        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgProduct);
            nombreProd = (TextView) itemView.findViewById(R.id.txtProducto);
            cantEdit = (EditText) itemView.findViewById(R.id.cant2);
            precio = (TextView) itemView.findViewById(R.id.precio);
            subtotal = (TextView) itemView.findViewById(R.id.subtotal);

        }

        public void bind(final Productoss productos) {
            imageView.setImageBitmap(productos.imagen);
            nombreProd.setText(productos.getNombre());
            precio.setText(Double.toString(productos.getPrecio()));
            cantEdit.setText(Integer.toString(productos.getCantidad()));
            subtotal.setText(Double.toString(productos.getSubtotal()));
        }
    }
}
