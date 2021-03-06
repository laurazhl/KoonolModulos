package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class RecyclerViewDatosCancelacion extends RecyclerView.Adapter<RecyclerViewDatosCancelacion.PaletteViewHolderVenta> implements View.OnClickListener {
    ArrayList<CancelacionDatos> listaDatos;
    private View.OnClickListener listener;


    public RecyclerViewDatosCancelacion(@NonNull ArrayList<CancelacionDatos> listaDatos) { this.listaDatos = listaDatos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rows_datos_cancelacion, viewGroup, false);
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
        public TextView folioVenta;
        public TextView fechav;
        public TextView estatus;
        public TextView totalVenta;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            folioVenta = (TextView) itemView.findViewById(R.id.txtFolio1);
            fechav = (TextView) itemView.findViewById(R.id.txtfecha1);
            estatus = (TextView) itemView.findViewById(R.id.txtestatus1);
            totalVenta = (TextView) itemView.findViewById(R.id.txtTotal1);
        }

        public void bind(CancelacionDatos datos) {
            folioVenta.setText(datos.getFolioVenta());
            fechav.setText(datos.getFechav());
            estatus.setText(datos.getEstatus());
            totalVenta.setText(datos.getTotalVenta());

        }
    }
}
