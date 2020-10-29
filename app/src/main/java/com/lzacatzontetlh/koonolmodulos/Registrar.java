package com.lzacatzontetlh.koonolmodulos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Registrar extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText edtName, editTextEmail, editnum;
    private EditText editTextPassword,  editConfirPassword, edtAP,  edtAM;
    private TextView textViewSignip;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    int numeero;
    ArrayList codigoList = new ArrayList();
    static String str;
    static String user_id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //PARA PONER EN FORMA VERTICAL
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        edtName = (EditText) findViewById(R.id.editTextName);
        edtAP = (EditText) findViewById(R.id.editxtAP);
        edtAM = (EditText) findViewById(R.id.editxtAM);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editConfirPassword = (EditText) findViewById(R.id.editTextPassword2);
        editnum = (EditText) findViewById(R.id.editNum);
        textViewSignip = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignip.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(getApplicationContext(), com.lzacatzontetlh.koonolmodulos.Registrar.class));
        }
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmacionContra = editConfirPassword.getText().toString().trim();
        final String nameU = edtName.getText().toString().trim();
        final String apellidoPaterno = edtAP.getText().toString().trim();
        final String apellidoMaterno = edtAM.getText().toString().trim();
        final String Num = editnum.getText().toString().trim();
        final String nombreCompleto = apellidoPaterno+" "+apellidoMaterno+" "+nameU;


        if (TextUtils.isEmpty(nameU)) {
            Toast.makeText(this, "Por favor, introduzca el nombre", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(apellidoPaterno)) {
            Toast.makeText(this, "Por favor, introduzca el apellido paterno", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(apellidoMaterno)) {
            Toast.makeText(this, "Por favor, introduzca el apellido materno", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor, introduzca el email", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(Num)) {
            Toast.makeText(this, "Por favor, introduzca el número de télefono", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, introduzca la contraseña", Toast.LENGTH_LONG).show();
            return;

        }else if (TextUtils.isEmpty(confirmacionContra)) {
            Toast.makeText(this, "Por favor, introduzca la contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmacionContra)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }
        //if validations are ok
        //we will first show a progressbar
        progressDialog.setMessage("Registrando usuario, espere un momento...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    generadorCodigos();
                    firebaseDatabase = firebaseDatabase.getInstance();
                    databaseReference= firebaseDatabase.getReference();
                    //databaseReference.keepSynced(true);
                    user_id = firebaseAuth.getCurrentUser().getUid();

                    databaseReference.child("Usuario/" +user_id).child("nombre").setValue(nombreCompleto);
                    databaseReference.child("Usuario/" +user_id).child("email").setValue(email);
                    databaseReference.child("Usuario/" +user_id).child("password").setValue(password);
                    databaseReference.child("Usuario/" +user_id).child("telefono").setValue(Num);
                    //Toast.makeText(Registro.this, "Registrado correctamente", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    finish();
                    startActivity(new Intent(getApplicationContext(),CodigoVerificacion.class));
                }

            }
        });
        progressDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();

        }
        if(v == textViewSignip){
            //will open login activity here
            finish();
            startActivity(new Intent(this, com.lzacatzontetlh.koonolmodulos.MainActivity.class));
        }
    }


    private void limpiarCampos() {
        edtName.setText("");
        edtAP.setText("");
        edtAM.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editConfirPassword.setText("");
        editnum.setText("");
    }

    private void generadorCodigos() {
        // Genera 6 numeros entre 1 y 10
        for (int i = 1; i <= 6; i++) {
            numeero = (int) (Math.random() * 9 + 1);
            if (codigoList.contains(numeero)) {
                i--;
            } else {
                codigoList.add(numeero);
            }
        }

        str = TextUtils.join("", codigoList);
        Toast.makeText(com.lzacatzontetlh.koonolmodulos.Registrar.this, "El codigo es: "+str, Toast.LENGTH_LONG).show();
    }
}
