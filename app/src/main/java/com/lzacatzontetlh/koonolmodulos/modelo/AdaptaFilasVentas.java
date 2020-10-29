package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.List;

//import com.codesapp.karenperez.puntodeventa.R;

public class AdaptaFilasVentas extends RecyclerView.Adapter<AdaptaFilasVentas.PaletteViewHolderFilas> {
    private List<ClientesDatos> data;

    private final MiListener listener;

    public interface MiListener {
        void onItemClick(ClientesDatos item);
    }

    public AdaptaFilasVentas(@NonNull List<ClientesDatos> data, MiListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public PaletteViewHolderFilas onCreateViewHolder(ViewGroup parent, int viewType) {
        View producto = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapta_ventasanteriores, parent, false);
        return new PaletteViewHolderFilas(producto);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolderFilas holder, int position) {
        holder.bind(data.get(position), listener);
    }

    class PaletteViewHolderFilas extends RecyclerView.ViewHolder {

        public TextView fol;
        public TextView fec;
        public TextView hor;
        public TextView din;

        public PaletteViewHolderFilas(View itemView) {
            super(itemView);

            fol = (TextView) itemView.findViewById(R.id.txtFolio);
            fec = (TextView) itemView.findViewById(R.id.txtFecha);
            hor = (TextView) itemView.findViewById(R.id.txtHora);
            din = (TextView) itemView.findViewById(R.id.txtMontoto);
        }

        public void bind(final ClientesDatos item, final MiListener listener) {
            fol.setText(item.getNombre());
            din.setText(item.getTelefono());
            fec.setText(item.getTipoPersona());
            hor.setText(item.getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}

