package com.lzacatzontetlh.koonolmodulos.modelo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzacatzontetlh.koonolmodulos.Globales;
import com.lzacatzontetlh.koonolmodulos.R;

import java.util.ArrayList;

public class GridAdapter2 extends BaseAdapter {


    private Context context;
    private ArrayList<String> arrayList;
    private int imagens[];

    public GridAdapter2(Context context, int imagens[], ArrayList<String> arrayList){
        this.context = context;
        this.imagens = imagens;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    //SE AGREGOO
    @Override
    public boolean isEnabled(int position) {
        // Disable the third item of GridView
        /*if (Globales.getInstance().apertura.equals("SI")){
        }*/
        if (Globales.getInstance().apertura==2) {
            if (position == 1)
            return false;

            if (position == 2)
                return false;

            if (position == 3)
                return false;

        }

        else if (Globales.getInstance().apertura==1){
            if (position == 0)
                return false;
        }
            return true;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_grid, null);
        }

        TextView tituloTv = (TextView) convertView.findViewById(R.id.ig_tv_titulo);
        tituloTv.setText(arrayList.get(position));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
        imageView.setImageResource(imagens[position]);

        //SI AÚN NO SE HA REALIZADO LA APERTURA, DESHABILITARA ALGUNOS BOTONES
        if (Globales.getInstance().apertura==2) {

            if(position == 1){
                // set the GridView disable item color
                tituloTv.setTextColor(Color.parseColor("#F0EFEF"));
            }
            if(position == 2){
                // set the GridView disable item color
                tituloTv.setTextColor(Color.parseColor("#F0EFEF"));
            }
            if(position == 3){
                // set the GridView disable item color
                tituloTv.setTextColor(Color.parseColor("#F0EFEF"));
            }

        }

        // APERTURA HABILITADA
        else if (Globales.getInstance().apertura==1) {
            if(position == 0){
                // set the GridView disable item color
                tituloTv.setTextColor(Color.parseColor("#F0EFEF"));
            }

        } else {
            // set the TextView text color (GridView item color)
            tituloTv.setTextColor(Color.BLACK);
        }


        return convertView;
    }
}
