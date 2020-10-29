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

public class RecyclerViewDatosDetaCancelacion extends RecyclerView.Adapter<RecyclerViewDatosDetaCancelacion.PaletteViewHolderVenta> implements View.OnClickListener {
    ArrayList<detallesCancelacionDatos> listaDatos;
    private View.OnClickListener listener;


    public RecyclerViewDatosDetaCancelacion(@NonNull ArrayList<detallesCancelacionDatos> listaDatos) { this.listaDatos = listaDatos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_vnt_det, viewGroup, false);
        producto.setOnClickListener(this);
        return new  PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(listaDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
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
        private ImageView imageView;
        public TextView productoDC;
        public TextView precioDC;
        public TextView cantidadDC;
        public TextView importeDC;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgProduct);
            productoDC = (TextView) itemView.findViewById(R.id.txtProducto);
            precioDC = (TextView) itemView.findViewById(R.id.precio);
            cantidadDC = (TextView) itemView.findViewById(R.id.cant2);
            importeDC = (TextView) itemView.findViewById(R.id.subtotal);

        }

        public void bind(final detallesCancelacionDatos datos) {
            imageView.setImageBitmap(datos.imagen);
            productoDC.setText(datos.getProductoDC());
            precioDC.setText(datos.getPrecioDC());
            cantidadDC.setText(datos.getCantidadDC());
            importeDC.setText(datos.getImporteDC());

        }
    }
}
