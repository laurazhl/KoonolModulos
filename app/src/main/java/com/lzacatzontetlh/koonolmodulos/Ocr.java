package com.lzacatzontetlh.koonolmodulos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lzacatzontetlh.koonolmodulos.modelo.Ingresarsql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

public class Ocr extends AppCompatActivity {


    static String cadena = "", cadenaFecha="";
    static String letras = "";
    static String cadenaLetrasCI = "";
    static String nombreDeLaPersona = "";
    static String fechaDeNacDeLaPersona = "";

    static String numeros = "";
    static int numeroFormateado = 0;
    private boolean hayNumeros = false;
    private boolean hayLetras = false;

    private static final String TAG = "OCR";
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage,  mSetImage2;
    private Button button2, button3;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String selectedImagePath = "";
    Uri urlPhoto;
    Uri photoURI;
    String imageurl;
    String nombre, nombrefinal = "", fechaN, fechaNfinal = "", clav, clavfinaL = "", direcc, direccfinal = "", claveE, claveEfinal = "", sxfinal= "", INE, INEFinal="", anioRegistro, anioRegistroFinal="",
    estado, estadoFinal="", muni, muniFinal="", seccion, seccionFinal="", localidad, localFinal="", emision, emisionFinal="", vigencia, vigenciaFinal="", info, infoFinal="";
    static String nom, fech, clv, dire, clavE, sX, anioR, state, mun, secc, locali, emis, vigen, prueba;
    static String nombrePf;
    private Integer bandera=0;

    private Uri filePath;
    String urlImages[] = new String[100];
    int numImages = 0;
    int imageId;
    private Bitmap thumbnail,thumbnail2;

    private String mPath;
    Bitmap bitmap;
    Button regresar,continuar;

    static String idmex="";
    static String fechaNacimiento="";
    static String nombreCompleto="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSetImage = (ImageView) findViewById(R.id.set_picture);
        mSetImage2 = (ImageView) findViewById(R.id.set_picture2);
        button2 = (Button) findViewById(R.id.buttonP1);
        button3 = (Button) findViewById(R.id.buttonP2);
        regresar = (Button)findViewById(R.id.button4);
        continuar=findViewById(R.id.btnConfirmar2);
        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();


       button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntent(1);
                //processImage();
                //vglobalE=1;

            }
        });

       button3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               llamarIntent(2);



           }
       });


        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Ocr.this, PersonaFisica.class));
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE;
                if(valor.equals("12")){
                    finish();
                    startActivity(new Intent(com.lzacatzontetlh.koonolmodulos.Ocr.this, PersonaFisica.class));
                }
                else {
                    if(valor.equals("1")){
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.Ocr.this, "Tome una foto de la parte posterior del INE", Toast.LENGTH_LONG).show();
                    }

                    if(valor.equals("2")){
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.Ocr.this, "Tome una foto de la parte frontal del INE", Toast.LENGTH_LONG).show();
                    }

                    if(valor.equals("")) {
                        Toast.makeText(com.lzacatzontetlh.koonolmodulos.Ocr.this, "Por favor, tome foto del INE", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    static final int REQUEST_TAKE_PHOTO = 1;
    private void llamarIntent(Integer tipo) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(this,
                        "com.lzacatzontetlh.koonolmodulos.fileprovider", photoFile);
                 bandera=tipo;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                if(tipo==1){
                    String v= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE;
                    if(v.equals("2")){

                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE="12";
                        System.out.println(" wewee2 " +  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE);
                    }
                    else {
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE = "1";
                        System.out.println(" wewee3  " +  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE);
                    }


                }
                if(tipo==2){


                    String v= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE;
                    if(v.equals("1")){
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE=v+"2";
                        System.out.println(" wewee4  " +  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE);
                    }

                    if(v.equals("")){
                        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE="2";
                        System.out.println(" wewee  " +  com.lzacatzontetlh.koonolmodulos.Globales.getInstance().vglobalE);
                    }


                }
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            selectedImagePath = mCurrentPhotoPath;
            //uploadImage();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                if (bandera==1){
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ineFrontal = mCurrentPhotoPath;
                    Ingresarsql sqlclass=new Ingresarsql();
                  //  sqlclass.registrarFotos(mCurrentPhotoPath, "INEFRONTAL.jpg", getApplicationContext());
                    mSetImage.setImageBitmap(bitmap);
                    if (mSetImage != null){
                    processImage();
                    }else{
                        LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.Ocr.this);final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Ocr.this);
                        alerta.setMessage("Ingrese una fotografía.").setCancelable(true).setCustomTitle(vista).setPositiveButton("Aceptar",null) ;alerta.show();

                    }
                }
                if (bandera==2){
                    com.lzacatzontetlh.koonolmodulos.Globales.getInstance().ineInversa = mCurrentPhotoPath;
                    Ingresarsql sqlclass=new Ingresarsql();
               //     sqlclass.registrarFotos(mCurrentPhotoPath, "INEINVERSA.jpg", getApplicationContext());
                    mSetImage2.setImageBitmap(bitmap);
                    processImage2();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        filePath = Uri.fromFile(image);
        // //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }


    private void processImage() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (textRecognizer.isOperational()) {
            //Log.d(TAG, "processImage: started");
            //Toast.makeText(this, "processImage: started", Toast.LENGTH_SHORT).show();
            nombrefinal="";
            fechaNfinal="";
            claveEfinal = "";
            clavfinaL="";
            sxfinal="";
            direccfinal = "";
            anioRegistroFinal = "";
            estadoFinal = "";
            muniFinal= "";
            seccionFinal = "";
            localFinal = "";
            emisionFinal = "";
            vigenciaFinal = "";
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < items.size(); i++) {
                TextBlock textBlock = items.valueAt(i);
                //nombre = textBlock.getValue();
                claveE = textBlock.getValue();
                direcc = textBlock.getValue();
                anioRegistro  = textBlock.getValue();
                estado = textBlock.getValue();
                muni = textBlock.getValue();
                seccion = textBlock.getValue();
                localidad  = textBlock.getValue();
                emision = textBlock.getValue();
                vigencia = textBlock.getValue();
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");

                int resultadoIne= textBlock.getValue().indexOf("ELECTORAL");
                INE = "";
                if (resultadoIne != -1){
                    for (int x=0;x<textBlock.getValue().length();x++){
                        if (textBlock.getValue().charAt(x) != '\n'){
                            INE = INE+textBlock.getValue().charAt(x);
                        }else {
                            if (INE.indexOf("ELECTORAL") !=-1) {
                                INE = "";
                            }else{
                                INEFinal = INE;
                            }
                        }
                        if (x==textBlock.getValue().length()-1){
                            INEFinal = INE;
                        }
                    }
                }


                int resultadoFN= textBlock.getValue().indexOf("FECHA");
                fechaN = "";
                if (resultadoFN != -1){
                    for (int x=0;x<textBlock.getValue().length();x++){
                        if (textBlock.getValue().charAt(x) != '\n'){
                            fechaN = fechaN+textBlock.getValue().charAt(x);
                        }else {
                            if (fechaN.indexOf("FECHA") !=-1) {
                                fechaN = "";
                            }else{
                                fechaNfinal = fechaN;
                            }
                        }
                        if (x==textBlock.getValue().length()-1){
                            fechaNfinal = fechaN;
                        }
                    }
                }

                int resultado= textBlock.getValue().indexOf("NOMBRE");
                nombre = "";
                if (resultado != -1){
                    for (int x=0;x<textBlock.getValue().length();x++){
                        if (textBlock.getValue().charAt(x) != '\n'){
                            nombre = nombre+textBlock.getValue().charAt(x);
                        }else {
                            if (nombre.trim().equals("DOMICILIO") == false){
                                if (nombre.trim().equals("NOMBRE") == false) {
                                    nombrefinal = nombrefinal + " " + nombre;
                                }
                                nombre = "";
                            }else
                                break;
                        }
                    }
                }

                int resultado2= textBlock.getValue().indexOf("DOMICILIO");
                direcc = "";
                if (resultado2 != -1){
                    if(nombre==""){

                        //nombre=textBlock.getValue();
                        for (int k=0;k<textBlock.getValue().length();k++) {
                            if (textBlock.getValue().charAt(k) != '\n'){
                                nombre = nombre+textBlock.getValue().charAt(k);
                                if (nombre.indexOf("DOMICILIO")!=-1){
                                    nombrefinal=nombre.substring(0, nombre.length()-10);
                                    if (nombrefinal.indexOf("NOMBRE")!=-1)
                                    {
                                        nombrefinal=nombrefinal.substring(5, nombrefinal.length());
                                    }
                                    break;
                                }
                            }else {
                                nombre=nombre+" ";
                                /*if (nombre.indexOf("DOMICILIO")!=-1){
                                    nombrefinal=nombre.substring(0, nombre.length()-10);
                                    if (nombrefinal.indexOf("NOMBRE")!=-1)
                                    {
                                        nombrefinal=nombrefinal.substring(5, nombrefinal.length());
                                    }
                                    break;
                                }*/
                            }
                        }
                    }


                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(direcc.indexOf("CLAVE") != -1){
                            direccfinal = direcc.substring(0, direcc.length()-5);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            direcc = direcc+textBlock.getValue().charAt(k);
                            if (direcc.indexOf("DOMICILIO") != -1) {
                                direcc = "";
                            }
                        }else {
                            if (direcc.indexOf("DOMICILIO") != -1) {
                                direcc = "";
                            }
                        }
                        if (k==textBlock.getValue().length()-1){
                            direccfinal = direcc;
                            Toast.makeText(this, direcc, Toast.LENGTH_LONG).show();
                        }

                    }
                }

                int resultadoCE= textBlock.getValue().toUpperCase().indexOf("CURP");
                claveE = "";
                String aniosS;
                if (resultadoCE != -1){
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(claveE.toUpperCase().indexOf("CURP") != -1){
                            claveEfinal = textBlock.getValue().substring(k+1, k+19);
                            sxfinal = claveEfinal.substring(10,11);
                            int anios= Integer.valueOf(claveEfinal.substring(4,6));
                            if (anios<=25){
                                aniosS="20";
                            }
                            else{
                                aniosS="19";
                            }
                            String prueba = claveEfinal.substring(4,6);
                            prueba.replace("T","7");
                            fechaNfinal= claveEfinal.substring(8,10)+"/"+claveEfinal.substring(6,8)+"/"+aniosS+prueba;
                            //fechaNfinal= claveEfinal.substring(8,10)+"/"+claveEfinal.substring(6,8)+"/"+aniosS+claveEfinal.substring(4,6);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            claveE = claveE+textBlock.getValue().charAt(k);
                        }

                    }
                }


                int  resultadoC= textBlock.getValue().toUpperCase().indexOf("CLAVE DE ELECTOR");
                clav = "";
                if (resultadoC != -1){
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(clav.toUpperCase().indexOf("CLAVE DE ELECTOR") != -1){
                            clavfinaL = textBlock.getValue().substring(k+1, k+19);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            clav = clav+textBlock.getValue().charAt(k);
                        }

                    }
                }


                int  resultadoAR= textBlock.getValue().toUpperCase().indexOf("ANO DE REGISTRO");
                anioRegistro = "";
                if (resultadoAR != -1){
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(anioRegistro.toUpperCase().indexOf("ANO DE REGISTRO") != -1){
                            anioRegistroFinal = textBlock.getValue().substring(k+1, k+8);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            anioRegistro = anioRegistro+textBlock.getValue().charAt(k);
                        }

                    }
                }

                int  resultadoE= textBlock.getValue().toUpperCase().indexOf("ESTADO");
                estado = "";
                if (resultadoE != -1){
                    for (int e=0;e<textBlock.getValue().length();e++){
                        if(estado.toUpperCase().indexOf("ESTADO") != -1){
                            estadoFinal = textBlock.getValue().substring(e+1, e+3);
                            break;
                        }
                        if (textBlock.getValue().charAt(e) != '\n'){
                            estado = estado+textBlock.getValue().charAt(e);
                        }
                    }
                }


                int  resultadoMuni= textBlock.getValue().toUpperCase().indexOf("MUNICIPIO");
                muni = "";
                if (resultadoMuni != -1){
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(muni.toUpperCase().indexOf("MUNICIPIO") != -1){
                            muniFinal = textBlock.getValue().substring(k+1, k+4);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            muni = muni+textBlock.getValue().charAt(k);
                        }
                    }
                }


                int  resultadoSeccion= textBlock.getValue().toUpperCase().indexOf("SECCION");
                seccion = "";
                if (resultadoSeccion != -1){
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(seccion.toUpperCase().indexOf("SECCION") != -1 || seccion.toUpperCase().indexOf("SECCIÓN") != -1){
                            seccionFinal = textBlock.getValue().substring(k+1, k+5);
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            seccion = seccion+textBlock.getValue().charAt(k);
                        }
                    }
                }


                int  resultadoLocal= textBlock.getValue().toUpperCase().indexOf("LOCALIDAD");
                localidad = "";
                if (resultadoLocal != -1){
                    for (int l=0;l<textBlock.getValue().length();l++){
                        if(localidad.toUpperCase().indexOf("LOCALIDAD") != -1){
                            localFinal = textBlock.getValue().substring(l+1, l+5);
                            break;
                        }
                        if (textBlock.getValue().charAt(l) != '\n'){
                            localidad = localidad+textBlock.getValue().charAt(l);
                        }
                    }
                }


                int  resultadoEmision= textBlock.getValue().toUpperCase().indexOf("EMISION");
                emision = "";
                if (resultadoEmision != -1){
                    for (int e=0;e<textBlock.getValue().length();e++){
                        if(emision.toUpperCase().indexOf("EMISION") != -1 || emision.toUpperCase().indexOf("EMISIÓN") != -1){
                            emisionFinal = textBlock.getValue().substring(e+1, e+5);
                            //emis = emisionFinal;
                            break;
                        }
                        if (textBlock.getValue().charAt(e) != '\n'){
                            emision = emision+textBlock.getValue().charAt(e);
                        }
                    }
                }

                int  resultadoVigen= textBlock.getValue().toUpperCase().indexOf("VIGENCIA");
                vigencia = "";
                if (resultadoVigen != -1){
                    for (int v=0;v<textBlock.getValue().length();v++){
                        if(vigencia.toUpperCase().indexOf("VIGENCIA") != -1){
                            vigenciaFinal = textBlock.getValue().substring(v+1, v+5);
                            //vigen = vigenciaFinal;
                            break;
                        }
                        if (textBlock.getValue().charAt(v) != '\n'){
                            vigencia = vigencia+textBlock.getValue().charAt(v);
                        }
                    }
                }
            }
            fech = fechaNfinal;
            nom = nombrefinal;
            clv = claveEfinal;
            dire = direccfinal;
            clavE = clavfinaL;
            sX = sxfinal;
            anioR = anioRegistroFinal;
            state = estadoFinal;
            mun = muniFinal;
            secc = seccionFinal;
            locali = localFinal;
            emis = emisionFinal;
            vigen = vigenciaFinal;
        } else {
            //Log.d(TAG, "processImage: not operational");
            Toast.makeText(this, "processImage: not operational" , Toast.LENGTH_LONG).show();
        }
    }

    private void processImage2() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (textRecognizer.isOperational()) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < items.size(); i++) {
                TextBlock textBlock = items.valueAt(i);
                info = textBlock.getValue();
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");

                int  resultado= textBlock.getValue().toUpperCase().indexOf("<");
                info = "";
                if (resultado != -1){
                    infoFinal=textBlock.getValue();
                    for (int k=0;k<textBlock.getValue().length();k++){
                        if(info.toUpperCase().indexOf("IDMEX") != -1){
                            infoFinal = textBlock.getValue();
                            break;
                        }
                        if (textBlock.getValue().charAt(k) != '\n'){
                            info = info+textBlock.getValue().charAt(k);
                        }

                    }
                }
            }
            prueba = infoFinal;
            cadena=infoFinal;
            cadenaFecha=infoFinal;
            System.out.println("  infoFinal  " +infoFinal+ " info"+ info+ "cadena"+ cadena);

            traerLetras();
        } else {
            Toast.makeText(this, "processImage: not operational" , Toast.LENGTH_LONG).show();
        }
    }

    /*private void limpiarCampos() {
        editTextname.setText("");
        editTextFN.setText("");
        editTextCURP.setText("");
        editTextDirec.setText("");
        editTextCE.setText("");
        editTextS.setText("");
    }*/

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sig, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            if (id == R.id.menu_sig) {
                ////if ((null!=mSetImage.getDrawable()) || (null!=mSetImage2.getDrawable())){
                //  startActivity(new Intent(Ocr.this, PersonaFisica.class));
                if(vglobalE==1){
                    finish();
                    startActivity(new Intent(Ocr.this, ObtencionDatosActivity.class));
                }

            else {

                Toast.makeText(this, "Por favor, tome foto del INE", Toast.LENGTH_LONG).show();
            }
            }

            return super.onOptionsItemSelected(item);
    }*/





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

            LayoutInflater imagenadvertencia_alert = LayoutInflater.from(com.lzacatzontetlh.koonolmodulos.Ocr.this);
            final View vista = imagenadvertencia_alert.inflate(R.layout.imagenadvertencia, null);
            AlertDialog.Builder alerta = new AlertDialog.Builder(com.lzacatzontetlh.koonolmodulos.Ocr.this);
            alerta.setMessage("¿Desea cerrar las sesión?")
                    .setCancelable(true)
                    .setCustomTitle(vista)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //   Intent intencion2 = new Intent(getApplication(), login.class);
                            // startActivity(intencion2);
                            //  Toast.makeText(this,"Sesión  Cerrada",Toast.LENGTH_LONG).show();

                            finish();
                            Intent intencion2 = new Intent(getApplication(), com.lzacatzontetlh.koonolmodulos.MainActivity.class);
                            startActivity(intencion2);
                            Toast.makeText(com.lzacatzontetlh.koonolmodulos.Ocr.this,"Sesión  Cerrada", Toast.LENGTH_LONG).show();
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
            Intent intencion2 = new Intent(getApplication(), PersonaFisica.class);
            startActivity(intencion2);
        }

        return super.onOptionsItemSelected(item);
    }



    public void onBackPressed() {
                        Intent intencion2 = new Intent(getApplication(), PersonaFisica.class);
                        startActivity(intencion2);
                        finish();
    }


    private static boolean isNumeric(char caracter){
        try {
            Integer.parseInt(String.valueOf(caracter));
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    private void imprimir() {
        //if(numeros.isEmpty() != true)
        /*if(numeros.length()==0)   {
            numeroFormateado = Integer.parseInt(numeros);
        }*/
        if(hayNumeros == true && hayLetras == false) {
            System.out.println("Solo hay numeros.\nNumeros:\t"+numeros);
        }
        else
            if (hayLetras = true && hayNumeros == false){
                    System.out.println("Solo hay letras.\nLetras:\t"+letras);
                     cadenaLetrasCI=letras;
        }
        else {
                System.out.println("Numeros:\t"+numeros);
                System.out.println("Letras:\t"+letras);
        }
    }

    public  void  traerLetras(){
         for(short indice = 0; indice < cadena.length(); indice++) {
            char caracteraux = cadena.charAt(indice);
            if(isNumeric(caracteraux)) {
                numeros += caracteraux;
            }
            else {
                letras += caracteraux;
            }
        }
        imprimir();
        obtenerNombre();
        obtenerFechaNacimiento();
    }



    public  void obtenerNombre() {
        letras = "";
        numeros = "";
        cadena="";
        String cad=cadenaLetrasCI;
        cadenaLetrasCI="";
        String cad2=cad.replace(" ", "");
        String[] c=cad2.split("\n");
        String c3=c[2];
        String quitarEspac=c3.replace("<", " ");
        nombreDeLaPersona = quitarEspac.replace("  ", " ");
    }


    public  void obtenerFechaNacimiento() {
        String cad=cadenaFecha;
        cadenaFecha="";
        String[] c=cad.split("\n");

        String c2=c[1];
        String c3 =c2.substring(0,6);
        com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechaNacimientoOCR=c3;//fecha nac año, mes y dia
        String k= com.lzacatzontetlh.koonolmodulos.Globales.getInstance().fechaNacimientoOCR;
        String c4= c3.substring(0,2);//año
        String c5= c3.substring(2,4);//mes
        String c6= c3.substring(4,6);//dia

        String fechana= c4+"/"+c5+"/"+c6;
        fechaDeNacDeLaPersona=fechana;

    }



}