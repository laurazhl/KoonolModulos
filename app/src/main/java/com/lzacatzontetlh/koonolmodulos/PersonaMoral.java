package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacatzontetlh.koonolmodulos.modelo.ConexionSQLiteHelper;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;

import java.util.regex.Pattern;


public class PersonaMoral extends AppCompatActivity {
    Button regresar, registrar,regresar2;
    EditText nombre, rfc, calle, numI, numE, cp, telefono, correo, facebook, insta, twitter;
    TextView subtitulo, titulo;
    Ingresarsql sq = new Ingresarsql();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona_moral);

        regresar =  findViewById(R.id.button2);
        registrar =  findViewById(R.id.button3);
        nombre =  findViewById(R.id.razonSocial);
        rfc =  findViewById(R.id.RFC);
        calle =  findViewById(R.id.Address0);
        numI =  findViewById(R.id.NI);
        numE =  findViewById(R.id.NE);
        cp =  findViewById(R.id.CP);
        telefono =  findViewById(R.id.telefono0);
        correo =  findViewById(R.id.email0);
        facebook =  findViewById(R.id.facebook);
        insta =  findViewById(R.id.insta);
        twitter =  findViewById(R.id.twitter);
        regresar2= findViewById(R.id.buttonRegresar2);
        subtitulo=findViewById(R.id.textCopia);
        titulo=findViewById(R.id.text);

        regresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this, ConsultaClientes.class));
            }
        });

        if(com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar.length()!=0){
            String nom = com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar;
            cargarPersonaMoral(getApplicationContext(),nom );
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().nombrePersonaACargar="";
            com.lzacatzontetlh.koonolmodulos.Globales.getInstance().botonAtras="1";
        }
        else {


            regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this, ClienteMenu.class));
                    // sq.borrardatos(getApplicationContext());
                }
            });

            // llenarCamposParaPrueba();
            registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarPersonaMoral();
                }
            });
        }

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
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intencion2 = new Intent(getApplication(), MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);

            alerta.setMessage("Verifiqué guardar cambios, de no ser así sus datos se perderan.  ¿Desea regresar?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intencion2 = new Intent(getApplication(), ClienteMenu.class);
                            startActivity(intencion2);

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


    private void registrarPersonaMoral(){
        final String RAZONSOCIAL =  nombre.getText().toString().trim();
        final String RFC = rfc.getText().toString().trim();
        final String CALLE = calle.getText().toString().trim();
        final String NUMI = numI.getText().toString().trim();
        final String NUME = numE.getText().toString().trim();
        final String CP = cp.getText().toString().trim();
        final String TELEFONO = telefono.getText().toString().trim();
        final String CORREO =  correo.getText().toString().trim();
        final String FACEBOOK = facebook.getText().toString().trim();
        final String INSTA = insta.getText().toString().trim();
        final String TWITTER = twitter.getText().toString().trim();


        if (TextUtils.isEmpty(RAZONSOCIAL)) { Toast.makeText(this, "Por favor, introduzca la razón social", Toast.LENGTH_LONG).show();return;
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
            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            alerta.setMessage("Ingresa un correo electrónico válido.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);
            alerta.show();
        } else {
            if (RAZONSOCIAL.length() >0) {
                if (CALLE.length() >0) {
                    if (NUME.length() >0) {
                            if (TELEFONO.length() == 10) {
                                    if (RFC.length() == 12) {
                                        if (CP.length() == 5) {


                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                                            builder.setCancelable(false);
                                            builder.setMessage("¿Son correctos sus datos?");
                                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int saldo=0;
                                                    int tipo=1;
                                                    int cp=1;
                                                    int esta= 1;
                                                    int tpp=2;
                                                    String prs_fkV=sq.existeLaPersona(getApplicationContext(),RFC);
                                                    if(prs_fkV.equals("noExiste")) {
                                                        sq.registrarPersonaMoral(RAZONSOCIAL, RFC, CALLE, NUME, NUMI, TELEFONO, CORREO, FACEBOOK, TWITTER, INSTA, tipo, 0, 0, saldo, cp, esta, tpp, getApplicationContext());

                                                        finish();
                                                        Intent intencion2 = new Intent(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this, ConsultaClientes.class);
                                                        startActivity(intencion2);
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
                                        else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("El código postal debe tener 5 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                                        }
                                    }
                                    else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("El RFC debe tener 12 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                                    }

                            }
                            else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                                AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("El número telefónico debe tener 10 digitos.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                            }

                    }
                    else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("Introduzca el número exterior.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                    }
                }
                else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("Introduzca la calle.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
                }
            }
            else { LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);alerta.setMessage("Introduzca el nombre completo.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar", null);alerta.show();
            }
        }
    }



    private void limpiarCampos() {
        nombre.setText("");
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
        nombre.setText("Matisse Persona Moral ");
       // apellidoP.setText("zaca");
        //apellidoM.setText("hernan");
      //  curp.setText("ZJKASIAJSLAKSUHI97821");
        rfc.setText("123456789876");
        calle.setText("AV benito juarez");
        numI.setText("34");
        numE.setText("43");
        cp.setText("23345");
        telefono.setText("1212121313");
        correo.setText("gtht@hotmail.com");
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
            Intent intencion2 = new Intent(getApplication(), ConsultaClientes.class);
            startActivity(intencion2);
            finish();
        }
        else {

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.PersonaMoral.this);

            alerta.setMessage("Verifiqué guardar cambios, de no ser así sus datos se perderan.  ¿Desea regresar?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intencion2 = new Intent(getApplication(), ClienteMenu.class);
                            startActivity(intencion2);
                            finish();
                            // Toast.makeText(verificacionFinal.this,"Sesión  Cerrada",Toast.LENGTH_LONG).show();
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



    public  void cargarPersonaMoral(Context context, String prs_nombre){

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
                    if(prs_tipo.equals("1")){
                        Cursor cursorr =db.rawQuery("SELECT mrl_id,mrl_razonsocial,prs_fk from moral WHERE  prs_fk="+prs_id , null);
                        try {
                            if (cursorr != null) {
                                cursorr.moveToFirst();
                                int indexx = 0;
                                while (!cursorr.isAfterLast()) {
                                    String mrl_razonsocial= String.valueOf( cursorr.getString(cursorr.getColumnIndex("mrl_razonsocial")));
                                    String mrl_id= String.valueOf( cursorr.getString(cursorr.getColumnIndex("mrl_id")));
                                    String prs_fk= String.valueOf( cursorr.getString(cursorr.getColumnIndex("prs_fk")));
                                    nombre.setText( mrl_razonsocial);
                                    index++;
                                    cursorr.moveToNext();
                                }
                                if (indexx != 0) {
                                }
                                else
                                {

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
                   //botonCamaraOsr.setVisibility(View.INVISIBLE);
                    //eti.setVisibility(View.INVISIBLE);
                    regresar.setVisibility(View.INVISIBLE);
                    regresar2.setVisibility(View.VISIBLE);
                    subtitulo.setVisibility(View.VISIBLE);
                    titulo.setVisibility(View.INVISIBLE);
                }
                else {



                }
            }
            cursor2.close();
            db.close();
        }catch(Exception e){
            Log.println(Log.ERROR,"Error ",e.getMessage());
        }
    }





}
