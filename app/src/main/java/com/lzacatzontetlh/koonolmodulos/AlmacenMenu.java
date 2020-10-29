package com.lzacatzontetlh.koonolmodulos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapter;

import java.util.ArrayList;

public class AlmacenMenu extends AppCompatActivity {
    private GridView gridView;
    GridAdapter adapter;
    String selectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen_menu);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Entradas");
        arrayList.add("Pedidos");
        arrayList.add("Reportes");
        arrayList.add("Salidas");
        arrayList.add("Traspaso");

        int images[] ={R.drawable.entradas, R.drawable.pedidos,
                R.drawable.reportes, R.drawable.salida, R.drawable.traspasos};

        adapter = new GridAdapter(this,images, arrayList);

        gridView = (GridView) findViewById(R.id.gridviewAlmacen);
        gridView.setAdapter(adapter);
}
}
