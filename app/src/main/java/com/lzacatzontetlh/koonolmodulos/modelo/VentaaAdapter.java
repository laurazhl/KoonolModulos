package com.lzacatzontetlh.koonolmodulos.modelo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class VentaaAdapter extends RecyclerView.Adapter<VentaaAdapter.PaletteViewHolderVenta>{
    ArrayList<VentasInfo> vnts;
    private MiListener listener;

    public interface MiListener {
        void onItemClick(VentasInfo vtsInfo, int position);
        /*void cambiacant(String cadena, int posicion);
        void aumentar(int posicion);*/
    }


    public VentaaAdapter(MiListener l, ArrayList<VentasInfo> vnts) {
        listener = l;
        this. vnts =  vnts;
    }

    /*public VentaaAdapter(VentasPrevias ventasPrevias, @NonNull ArrayList<VentasInfo> vnts) {
       this. vnts =  vnts;
    }*/

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vnta = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_ventas_previas, viewGroup, false);
        return new PaletteViewHolderVenta(vnta);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(vnts.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return vnts.size();
    }



    class PaletteViewHolderVenta extends RecyclerView.ViewHolder {

        public TextView Folioo;
        public TextView cant;
        public TextView Statuss;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            Folioo = (TextView) itemView.findViewById(R.id.txtFolio);
            cant = (TextView) itemView.findViewById(R.id.txtTotal);
            Statuss = (TextView) itemView.findViewById(R.id.txtStatus);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Globales.getInstance().idVentaPrevia= getAdapterPosition();
                        Log.d("LogDiaAdapter", "Clicled:" +position);
                    }
                }
            });*/
        }


        public void bind(final VentasInfo vnTas, final MiListener miListener) {
            Folioo.setText("Venta "+vnTas.getFolioVenta());
            cant.setText("$ " + Double.toString(vnTas.getTotal()));
            Statuss.setText(vnTas.getStatus());



            /*itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    miListener.onItemClick(getAdapterPosition());
                    //notifyItemChanged(getAdapterPosition());
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(vnTas, getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
