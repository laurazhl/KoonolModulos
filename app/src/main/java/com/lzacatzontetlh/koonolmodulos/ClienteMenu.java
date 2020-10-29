package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.GridAdapterCM;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;

import java.util.ArrayList;

public class ClienteMenu extends AppCompatActivity {
    private GridView gridView;
    GridAdapterCM adapter;
    String selectItem;
    Button botonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_menu);
        botonRegresar = findViewById(R.id.btnCancelar2);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Consultar");
        arrayList.add("Agregar persona física");
        arrayList.add("Agregar persona moral");


        int images[] = { R.drawable.consultarclientes,  R.drawable.registrapersonafisica, R.drawable.registrapersonamoral};

        adapter = new GridAdapterCM(this, images, arrayList);

        gridView = (GridView) findViewById(R.id.gridviewClientes);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nombreProducto = (TextView) view.findViewById(R.id.ig_tv_titulo);
                selectItem = nombreProducto.getText().toString();

                if (selectItem.equals("Agregar persona física")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this, PersonaFisica.class));

                }if (selectItem.equals("Agregar persona moral")) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this, com.lzacatzontetlh.koonolmodulos.PersonaMoral.class));

                }if (selectItem.equals("Consultar")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this, ConsultaClientes.class));

                }

            }
        });


        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class));
            }
        });



    }

    public void onBackPressed() {
        Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
        startActivity(intencion2);
        finish();
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.ClienteMenu.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
                        }
                    });
            alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alerta.show();

        }

        if (id==R.id.opcionHome) {

            finish();
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MenuGeneral.class);
            startActivity(intencion2);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow, menu);
        Ingresarsql sq = new Ingresarsql();
        sq.consultarNombreUsuario(getApplicationContext());
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);
        return true;
    }

}
