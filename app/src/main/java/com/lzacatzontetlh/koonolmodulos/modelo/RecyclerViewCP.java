package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class RecyclerViewCP extends RecyclerView.Adapter<RecyclerViewCP.PaletteViewHolderVenta> implements View.OnClickListener {
    ArrayList<DatosCP> listaDatos;
    private View.OnClickListener listener;


    public RecyclerViewCP(@NonNull ArrayList<DatosCP> listaDatos) { this.listaDatos = listaDatos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rows_datos_cp, viewGroup, false);
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
        public TextView cp_codigopostal;
        public TextView cp_estado;
        public TextView cp_municipio;
        public TextView cp_asentamiento;
        public TextView cp_ciudad;
        public TextView cp_id;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            cp_codigopostal = (TextView) itemView.findViewById(R.id.cp_codigopostal);
            cp_estado= (TextView) itemView.findViewById(R.id.cp_estado);
            cp_municipio = (TextView) itemView.findViewById(R.id.cp_municipio);
            cp_asentamiento= (TextView) itemView.findViewById(R.id.cp_asentamiento);
            cp_ciudad = (TextView) itemView.findViewById(R.id.cp_ciudad);
            cp_id = (TextView) itemView.findViewById(R.id.cp_id);
        }

        public void bind(DatosCP datos) {
            cp_codigopostal.setText(datos.getCp_codigopostal());
            cp_estado.setText(datos.getCp_estado());
            cp_municipio.setText(datos.getCp_municipio());
            cp_asentamiento.setText(datos.getCp_asentamiento());
            cp_ciudad.setText(datos.getCp_ciudad());
            cp_id.setText(datos.getCp_id());

        }
    }
}
