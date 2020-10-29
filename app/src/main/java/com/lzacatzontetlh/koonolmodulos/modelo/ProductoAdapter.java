package com.lzacatzontetlh.koonolmodulos.modelo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.PaletteViewHolderVenta>{
    ArrayList<Productoss> productos;
    private MiListener listener;

    public interface MiListener {
        void onItemClick(Productoss productos, EditText cantidadescribir);
        void onItemClickMenos(Productoss productos);
        void cambiacant(String cadena, int posicion);
        void elimi(int posicion);

        void aumentar(int posicion);
        void disminuir(int posicion);
    }

    public ProductoAdapter(MiListener l, ArrayList<Productoss> p) {
        listener = l;
        productos = p;
    }


    public ProductoAdapter(@NonNull ArrayList<Productoss> productos) { this.productos = productos; }

    @NonNull
    @Override
    public PaletteViewHolderVenta onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View producto = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_numero_rows, viewGroup, false);
        return new PaletteViewHolderVenta(producto);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletteViewHolderVenta paletteViewHolderVenta, int i) {
        paletteViewHolderVenta.bind(productos.get(i), listener);
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
        ImageButton eliminarP;
        ImageButton aumentarCant;
        ImageButton disminuirCant;
        public TextView presentacionP;

        public PaletteViewHolderVenta(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgProduct);
            nombreProd = (TextView) itemView.findViewById(R.id.txtProducto);
            cantEdit = (EditText) itemView.findViewById(R.id.cant2);
            cantEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            precio = (TextView) itemView.findViewById(R.id.precio);
            subtotal = (TextView) itemView.findViewById(R.id.subtotal);
            eliminarP = (ImageButton) itemView.findViewById(R.id.eliminar);
            aumentarCant = (ImageButton) itemView.findViewById(R.id.agregarProduct);
            disminuirCant = (ImageButton) itemView.findViewById(R.id.eliminarProduct);
            presentacionP=  itemView.findViewById(R.id.presentacionProducto);

        }

        public void bind(final Productoss productos, final MiListener listener) {
            imageView.setImageBitmap(productos.imagen);
            nombreProd.setText(productos.getNombre());
            precio.setText("$ " + Double.toString(productos.getPrecio()));
            cantEdit.setText(Integer.toString(productos.getCantidad()));
            subtotal.setText(Double.toString(productos.getSubtotal()));
            presentacionP.setText(productos.getPresentacionP());
            if(productos.cantidad>0){

                cantEdit.setText(Integer.toString(productos.cantidad));
                Integer.parseInt(cantEdit.getText().toString());
            }
            else {
                cantEdit.setText("");
            }

            if (productos.cantidad<=0){
                cantEdit.setTextColor(Color.parseColor("#F48D12"));
            }
            else
            {
                cantEdit.setTextColor(Color.parseColor("#F48D12"));
            }


            cantEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                   listener.cambiacant(s.toString(),getAdapterPosition());
                   subtotal.setText(Double.toString(Globales.getInstance().subTotal));
                }
            });



            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(productos, cantEdit);
                    notifyItemChanged(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClickMenos(productos);
                    notifyItemChanged(getAdapterPosition());
                    return false;
                }
            });

            //ELIMINAR
            eliminarP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listener.elimi(getAdapterPosition());
                    //notifyItemRemoved(getAdapterPosition());
                }
            });


            aumentarCant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globales.getInstance().positionRecycler = getAdapterPosition();
                    listener.aumentar(getAdapterPosition());
                    cantEdit.setText(String.valueOf(Globales.getInstance().canTotal));
                    subtotal.setText(Double.toString(Globales.getInstance().subTotal));

                }
            });

            disminuirCant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globales.getInstance().positionRecycler = getAdapterPosition();
                    listener.disminuir(getAdapterPosition());
                    cantEdit.setText(String.valueOf(Globales.getInstance().canTotal));
                    subtotal.setText(Double.toString(Globales.getInstance().subTotal));

                }
            });

        }
    }
}
