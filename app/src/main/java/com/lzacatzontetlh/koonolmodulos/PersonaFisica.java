package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonaFisica extends AppCompatActivity {
    Button regresar, registrar, regresar2;
    EditText nombre, apellidoM, apellidoP, curp, rfc, calle, numI, numE, cp, telefono, correo, facebook, insta, twitter,fecham;
    ConexionSQLiteHelper conn;
    ImageButton botonCamaraOsr, buscar;
    TextView eti, subtitulo, titulo;
    SearchableSpinner searchableSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_fisica);
        botonCamaraOsr=findViewById(R.id.botonCamaraOsr);
        regresar = findViewById(R.id.button2);
        registrar = findViewById(R.id.button3);
        nombre =  findViewById(R.id.name);
        apellidoP =  findViewById(R.id.nameAp);
        apellidoM =  findViewById(R.id.nameAm);
        eti= findViewById(R.id.eti);
        fecham =  findViewById(R.id.fechaNacimiento2);
        curp =  findViewById(R.id.curp);
        rfc =  findViewById(R.id.RFC);
        calle =  findViewById(R.id.Address);
        numI =  findViewById(R.id.NI);
        numE =  findViewById(R.id.NE);
        cp =  findViewById(R.id.CP);
        telefono =  findViewById(R.id.telefono);
        correo =  findViewById(R.id.email);
        facebook =  findViewById(R.id.facebook);
        insta =  findViewById(R.id.insta);
        twitter =  findViewById(R.id.twitter);
        regresar2= findViewById(R.id.buttonRegresar2);
        subtitulo=findViewById(R.id.textCopia);
        titulo=findViewById(R.id.text);
        buscar = findViewById(R.id.btnBuscar2);

        searchableSpinner=findViewById(R.id.searchable_spinner);
        conn=new ConexionSQLiteHelper(getApplicationContext());






        regresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this, com.lzacatzontetlh.koonolmodulos.ConsultaClientes.class));
            }
        });


        busquedaCP();



        if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar.length()!=0){
            String nom = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar;
            cargarPersonaFisica(getApplicationContext(),nom );
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar="";
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().botonAtras="1";
            //Toast.makeText(PersonaFisica.this,"...",Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(PersonaFisica.this,"..1.",Toast.LENGTH_SHORT).show();

            buscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    busquedaCP();
                    String query = cp.getText().toString();
                    if(query.equals("") || query.equals(" ")){
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this,"Ingrese un valor a buscar.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        busquedaCP();
                    }
                }
            });



            searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String nombreS = searchableSpinner.getSelectedItem().toString();
                    //Toast.makeText(PersonaFisica.this," nombreS "+ nombreS,Toast.LENGTH_SHORT).show();

                    cp.setText(nombreS);
                    searchableSpinner.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchableSpinner.getWindowToken(), 0);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    searchableSpinner.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchableSpinner.getWindowToken(), 0);
                }
            });



            regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this, com.lzacatzontetlh.koonolmodulos.ClienteMenu.class));
                }
            });


            botonCamaraOsr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this, com.lzacatzontetlh.koonolmodulos.Ocr.class));
                }
            });
            //llenarCamposParaPrueba();
            registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarPersonaFisica();
                }
            });

            String valor = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE;
            if (valor.equals("12")) {
                cagarDatosDelIne();
                com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE = "";
            }





        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow, menu);
        Ingresarsql sq = new Ingresarsql();
        sq.consultarNombreUsuario(getApplicationContext());
        menu.getItem(0).setTitle(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombreUsuario);
        return true;
    }


    public  boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id==R.id.opcion1){
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            alerta.setMessage("Verifiqué guardar cambios, de no ser así sus datos se perderan.  ¿Desea regresar?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.ClienteMenu.class);
                            startActivity(intencion2);
                            finish();
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

        return super.onOptionsItemSelected(item);
    }


    private void registrarPersonaFisica(){
        final String NOMBRE =  nombre.getText().toString().trim();
        final String APELLIDOP = apellidoP.getText().toString().trim();
        final String APELLIDOM = apellidoM.getText().toString().trim();
        final String CURP = curp.getText().toString().trim();
        final String RFC = rfc.getText().toString().trim();
        final String CALLE = calle.getText().toString().trim();
        final String NUME = numE.getText().toString().trim();
        final String NUMI = numI.getText().toString().trim();
        final String CP = cp.getText().toString().trim();
        final String TELEFONO = telefono.getText().toString().trim();
        final String CORREO =  correo.getText().toString().trim();
        final String FACEBOOK = facebook.getText().toString().trim();
        final String TWITTER = twitter.getText().toString().trim();
        final String INSTA = insta.getText().toString().trim();
//        final String FECHAN = fecham.getText().toString().trim();
        //String nombreCompleto = APELLIDOP+APELLIDOM+NOMBRE;
        final String nombreCompleto =NOMBRE+" "+APELLIDOP+" "+APELLIDOM;

        int tipo;
        int diascred;
        float limcred;
        float saldo;
        int esta;
        int tipoPersona;

        if (TextUtils.isEmpty(NOMBRE)) { Toast.makeText(this, "Por favor, introduzca el nombre", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(APELLIDOP)) { Toast.makeText(this, "Por favor, introduzca el apellido paterno", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(APELLIDOM)) { Toast.makeText(this, "Por favor, introduzca el apellido materno", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(CURP)) { Toast.makeText(this, "Por favor, introduzca el CURP", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(RFC)) { Toast.makeText(this, "Por favor, introduzca el RFC", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(RFC)) { Toast.makeText(this, "Por favor, introduzca el RFC", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(CALLE)) {
            Toast.makeText(this, "Por favor, introduzca la calle", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(NUME)) { Toast.makeText(this, "Por favor, introduzca el número exterior", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(CP)) { Toast.makeText(this, "Por favor, introduzca el código postal", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(TELEFONO)) { Toast.makeText(this, "Por favor, introduzca el número de teléfono ", Toast.LENGTH_LONG).show();return;
        }else if (TextUtils.isEmpty(CORREO)) {
            Toast.makeText(this, "Por favor, introduzca su correo eléctronico", Toast.LENGTH_LONG).show();return;
        }

        final Ingresarsql sq = new Ingresarsql();

        if (!validarEmail(CORREO)) {
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            alerta.setMessage("Ingresa un correo electrónico válido.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);
            alerta.show();
        } else {
            if (nombreCompleto.length() >0) {
                if (CALLE.length() >0) {
                    if (NUME.length() >0) {
                            if (TELEFONO.length() == 10) {
                                if (CURP.length() == 18) {
                                    if (RFC.length() == 13) {
                                        if (CP.length() == 5) {
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                                            builder.setCancelable(false);
                                            builder.setMessage("¿Son correctos sus datos?");
                                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int saldo=0;
                                                    int tipo=0;
                                                    int cp=0;//duda
                                                    int esta= 1;
                                                    int tpp=2;
                                                    String prs_fkV=sq.existeLaPersona(getApplicationContext(),rfc.getText().toString().trim());
                                                    if(prs_fkV.equals("noExiste")) {
                                                        sq.registrarPersonaFisica(nombreCompleto, NOMBRE, APELLIDOP, APELLIDOM, rfc.getText().toString().trim(), calle.getText().toString().trim(), numE.getText().toString().trim(), numI.getText().toString().trim(), telefono.getText().toString().trim(), correo.getText().toString().trim(), facebook.getText().toString().trim(), twitter.getText().toString().trim(), insta.getText().toString().trim(), CURP, tipo, 0, 0, saldo, cp, esta, tpp, getApplicationContext());
                                                        finish();
                                                        Intent intencion2 = new Intent(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this, com.lzacatzontetlh.koonolmodulos.ConsultaClientes.class);
                                                        startActivity(intencion2);
                                                        // consultaSQL();
                                                    }
                                                    else {
                                                            Toast.makeText(getApplicationContext(), "Este cliente ya existe.", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //if user select "No", just cancel this dialog and continue with app
                                                    dialog.cancel();
                                                }
                                            });
                                            android.support.v7.app.AlertDialog alert = builder.create();
                                            alert.show();

                                        }
                                        else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("El código postal debe tener 5 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                                        }
                                    }
                                    else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("El RFC debe tener 13 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                                    }
                                }
                                else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("El CURP debe tener 18 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                                }
                            }
                            else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("El número telefónico debe tener 10 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                            }
                    }
                    else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("Introduzca el número exterior.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                    }
                }
                else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("Introduzca la calle.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                }
            }
            else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);alerta.setMessage("Introduzca el nombre completo.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
            }
        }
        //limpiarCampos();

    }


    private void limpiarCampos() {
        nombre.setText("");
        apellidoP.setText("");
        apellidoM.setText("");
        curp.setText("");
        rfc.setText("");
        calle.setText("");
        numI.setText("");
        numE.setText("");
        cp.setText("");
        telefono.setText("");
        correo.setText("");
        facebook.setText("");
        insta.setText("");
        twitter.setText("");
    }



    private void llenarCamposParaPrueba() {
        nombre.setText("Jaqueline ");
        apellidoP.setText("Torrez ");
        apellidoM.setText("Tula");
      //  fecham.setText("24/12/23");
        curp.setText("ESTAESUNACURPDEPRUEBA");
        rfc.setText("1234567890984");
        calle.setText("AV benito juarez");
        numI.setText("34");
        numE.setText("43");
        cp.setText("72760");
        telefono.setText("1234567890");
        correo.setText("CORREO@EJEMPLO.COM");
        facebook.setText("fb");
        insta.setText("ist");
        twitter.setText("tw");
    }


    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public void onBackPressed() {
        if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().botonAtras.equals("1")){
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().botonAtras="";
            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.ConsultaClientes.class);
            startActivity(intencion2);
            finish();
        }
        else {
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaFisica.this);
            alerta.setMessage("Verifiqué guardar cambios, de no ser así sus datos se perderan.  ¿Desea regresar?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.ClienteMenu.class);
                            startActivity(intencion2);
                            finish();
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
    }

    public   void  cagarDatosDelIne(){
        //editTextFN.setText(Ocr.fech);
        try {
            String string = com.lzacatzontetlh.koonolmodulos.Ocr.nombreDeLaPersona;
            String[] parts = string.split(" ");
            int contador=parts.length;
            String part0 = parts[0];//apellido poaterno
            String part1 = parts[1]; //obtiene:apellido mterno
            String part2 = parts[2]; //obtiene nombre

            nombre.setText(part2);

            String part3="", part4="", part5="";
            if(contador==4){
                part3 = parts[3]; //segundo nombre
                nombre.setText(part2 +" "+ part3);
            }
            if(contador==5) {
                part4 = parts[4]; //obtiene: segundo nombre
                nombre.setText(part2 +" "+ part3+" "+part4);
            }

            if(contador==6) {
                part5 = parts[5]; //obtiene: segundo nombre
                nombre.setText(part2 +" "+ part3+" "+part4 + " "+ part5);
            }
            apellidoP.setText(part0);
            apellidoM.setText(part1);
            fecham.setText(com.lzacatzontetlh.koonolmodulos.Ocr.fechaDeNacDeLaPersona);
            curp.setText(com.lzacatzontetlh.koonolmodulos.Ocr.clv);
            calle.setText(com.lzacatzontetlh.koonolmodulos.Ocr.dire);
        }catch (Exception e){
            String string = com.lzacatzontetlh.koonolmodulos.Ocr.nombreDeLaPersona;
            String[] parts = string.split(" ");
            int contador=parts.length;
            String part0 = parts[0];//apellido poaterno
            String part1 = parts[1]; //obtiene:apellido mterno
            String part2 = parts[2]; //obtiene nombre

            nombre.setText(part2);

            String part3="", part4="",  part5="";
            if(contador==4){
                 part3 = parts[3]; //segundo nombre
                nombre.setText(part2 +" "+ part3);
            }
            if(contador==5) {
                 part4 = parts[4]; //obtiene: segundo nombre
                nombre.setText(part2 +" "+ part3+" "+part4);
            }

            if(contador==6) {
                part5 = parts[5]; //obtiene: segundo nombre
                nombre.setText(part2 +" "+ part3+" "+part4 + " "+ part5);
            }
            apellidoP.setText(part0);
            apellidoM.setText(part1);
            fecham.setText(com.lzacatzontetlh.koonolmodulos.Ocr.fechaDeNacDeLaPersona);
            curp.setText(com.lzacatzontetlh.koonolmodulos.Ocr.clv);
            calle.setText(com.lzacatzontetlh.koonolmodulos.Ocr.dire);
            System.out.println("part1" + part1 + " part2 " + part2 + "part3" + part3 + " part4 " + part4 + " part55 " + string + "Ocr.dire" + com.lzacatzontetlh.koonolmodulos.Ocr.dire + "part0"+ part0);
        }
        com.lzacatzontetlh.koonolmodulos.Ocr.nombreDeLaPersona="";
    }



    public  void cargarPersonaFisica(Context context, String prs_nombre){

        //prs_nombre=Globales.getInstance().nombrePersonaACargar;

        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context);
        SQLiteDatabase db = conn.getReadableDatabase();

        String var="", prs_rfc, prs_calle, prs_noint, prs_noext, prs_telefono, prs_email, prs_facebook, prs_twitter,prs_instagram, prs_tipo, cp_fk ;
        int  prs_id=0;
        Cursor cursor2 =db.rawQuery("SELECT prs_id, prs_rfc, prs_calle, prs_noint, prs_noext, prs_telefono, prs_email, prs_facebook, prs_twitter,prs_instagram, prs_tipo, cp_fk  from persona WHERE  prs_nombre='"+prs_nombre+"' " , null);
        try {
            if (cursor2 != null) {
                cursor2.moveToFirst();
                int index = 0;
                while (!cursor2.isAfterLast()) {
                    prs_id= Integer.parseInt(String.valueOf(cursor2.getColumnIndex("prs_id")));
                    prs_rfc= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_rfc")));
                    prs_calle= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_calle")));
                    prs_noint= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_noint")));
                    prs_noext= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_noext")));
                    prs_telefono= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_telefono")));
                    prs_email= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_email")));
                    prs_facebook= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_facebook")));
                    prs_twitter= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_twitter")));
                    prs_instagram= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_instagram")));
                    prs_tipo= String.valueOf( cursor2.getString(cursor2.getColumnIndex("prs_tipo")));
                    cp_fk= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_fk")));
                    rfc.setText( prs_rfc);
                   calle.setText( prs_calle);
                   numI.setText(prs_noint );
                   numE.setText(prs_noext );
                   telefono.setText( prs_telefono);
                   correo.setText( prs_email);
                   facebook.setText(prs_facebook );
                   twitter.setText(prs_twitter );
                   insta.setText( prs_instagram);
                   cp.setText( cp_fk);
                    index++;
                    cursor2.moveToNext();
                    if(prs_tipo.equals("0")){//Tipo fisica
                        Cursor cursorr =db.rawQuery("SELECT fsc_id,fsc_nombre,fsc_apaterno,fsc_amaterno,fsc_curp,prs_fk from fisica WHERE prs_fk="+prs_id , null);
                        try {
                            if (cursorr != null) {
                                cursorr.moveToFirst();
                                int indexx = 0;
                                while (!cursorr.isAfterLast()) {
                                    String fsc_id= String.valueOf( cursorr.getString(cursorr.getColumnIndex("fsc_id")));
                                    String fsc_nombre= String.valueOf( cursorr.getString(cursorr.getColumnIndex("fsc_nombre")));
                                    String fsc_apaterno= String.valueOf( cursorr.getString(cursorr.getColumnIndex("fsc_apaterno")));
                                    String fsc_amaterno= String.valueOf( cursorr.getString(cursorr.getColumnIndex("fsc_amaterno")));
                                    String fsc_curp= String.valueOf( cursorr.getString(cursorr.getColumnIndex("fsc_curp")));
                                    String prs_fk= String.valueOf( cursorr.getString(cursorr.getColumnIndex("prs_fk")));
                                    nombre.setText( fsc_nombre);
                                    apellidoM.setText( fsc_amaterno);
                                    apellidoP.setText( fsc_apaterno);
                                    curp.setText(fsc_curp );
                                    index++;
                                    cursorr.moveToNext();
                                }
                                if (indexx != 0) {
                                    System.out.println("ya existe el registrarTipoDeGarantia ");
                                }
                            }
                            cursor2.close();
                            db.close();
                        }catch(Exception e){
                            System.out.println("Error en validacion Det.G  "+e.getMessage());
                        }
                    }
                }
                if (index != 0) {
                    nombre.setEnabled(false);
                    apellidoM.setEnabled(false);
                    apellidoP.setEnabled(false);
                    curp.setEnabled(false);
                    rfc.setEnabled(false);
                    calle.setEnabled(false);
                    numI.setEnabled(false);
                    numE.setEnabled(false);
                    telefono.setEnabled(false);
                    correo.setEnabled(false);
                    facebook.setEnabled(false);
                    twitter.setEnabled(false);
                    insta.setEnabled(false);
                    cp.setEnabled(false);
                    registrar.setVisibility(View.INVISIBLE);
                    botonCamaraOsr.setVisibility(View.INVISIBLE);
                    eti.setVisibility(View.INVISIBLE);
                    regresar.setVisibility(View.INVISIBLE);
                    regresar2.setVisibility(View.VISIBLE);
                    subtitulo.setVisibility(View.VISIBLE);
                    titulo.setVisibility(View.INVISIBLE);
                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }
    }



    private void busquedaCP() {
        SQLiteDatabase db = conn.getReadableDatabase();
         //List<DatosCP> lista = new ArrayList<DatosCP>();
        List<String> lista = new ArrayList<String>();
        lista.clear();
        searchableSpinner.setTitle("Seleccione un código postal");
        searchableSpinner.setPositiveButton("Cerrar");
        //String query = cp.getText().toString();
        lista.add("72769");
        lista.add("72456");




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        //ArrayAdapter<DatosCP> adapter = new ArrayAdapter<DatosCP>(this, android.R.layout.simple_list_item_1, lista);
        searchableSpinner.setAdapter(adapter);



       /* String query ="1000";
            Cursor cursor2 =db.rawQuery("select cp_id,cp_codigopostal,cp_estado,cp_municipio,cp_asentamiento,cp_ciudad from cp WHERE cp_codigopostal='"+query+"'", null);

            try {
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int index = 0;
                    while (!cursor2.isAfterLast()) {
                        String cp_id= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_id")));
                        String cp_codigopostal= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_codigopostal")));
                        String cp_estado= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_estado")));
                        String cp_municipio= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_municipio")));
                        String cp_asentamiento= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_asentamiento")));
                        String cp_ciudad= String.valueOf( cursor2.getString(cursor2.getColumnIndex("cp_ciudad")));



                        //lista.add(new DatosCP(cp_id,cp_codigopostal,cp_estado,cp_municipio,cp_asentamiento,cp_ciudad));
                    //    lista.add("Hola");
                            lista.add(cp_codigopostal + cp_estado+ cp_municipio + cp_asentamiento+ cp_ciudad);
                            index++;
                            cursor2.moveToNext();
                    }
                    if (index != 0) {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
                        //ArrayAdapter<DatosCP> adapter = new ArrayAdapter<DatosCP>(this, android.R.layout.simple_list_item_1, lista);
                        searchableSpinner.setAdapter(adapter);

                       // RecyclerView recyclerView2;

                      //  funcionClick();
                    //    recyclerView.setAdapter(adaptador);
                        //recyclerView.setAdapter(new RecyclerViewClientes((ArrayList<ClientesDatos>) listclientes));
                        Toast.makeText(PersonaFisica.this,"4545.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(PersonaFisica.this,"No hay concidencias",Toast.LENGTH_SHORT).show();
                       // recyclerView.setAdapter(null);
                    }
                }
            }catch(Exception e){
                Log.println(Log.ERROR,"",e.getMessage());
            }*/
    }







}
