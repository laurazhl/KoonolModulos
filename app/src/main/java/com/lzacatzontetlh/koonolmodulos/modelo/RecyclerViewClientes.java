package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class RecyclerViewClientes extends RecyclerView.Adapter<RecyclerViewClientes.PaletteViewHolderVenta> implements View.OnClickListener{
    private View.OnClickListener listener;
    ArrayList<ClientesDatos> Listclientes;


    public RecyclerViewClientes(@NonNull ArrayList<ClientesDatos> clientes) { this.Listclientes = clientes; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rows, viewGroup, false);
        producto.setOnClickListener(this);
        return new  PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(Listclientes.get(i));

    }

    @Override
    public int getItemCount() {
        return Listclientes.size();
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
        public TextView tipoPersona;
        public TextView nombre;
        public TextView tel;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            tipoPersona = (TextView) itemView.findViewById(R.id.txt0);
            nombre = (TextView) itemView.findViewById(R.id.txt1);
            tel = (TextView) itemView.findViewById(R.id.txt);
        }

        public void bind(ClientesDatos clienTes) {
            tipoPersona.setText(clienTes.getTipoPersona());
            nombre.setText(clienTes.getNombre());
            tel.setText(clienTes.getTelefono());

        }
    }
}
