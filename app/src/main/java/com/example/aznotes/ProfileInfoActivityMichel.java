package com.example.aznotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class ProfileInfoActivityMichel extends AppCompatActivity {
    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    TextView saludo, Fnacimiento, genero, altura, peso, imc;
    Button editarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info_michel);
        saludo = findViewById(R.id.saludoProfile);
       /* Fnacimiento = findViewById(R.id.fechaNacTextView);
        genero = findViewById(R.id.generoTextView);
        altura = findViewById(R.id.alturaTextView);
        peso = findViewById(R.id.pesoTextView);
        imc = findViewById(R.id.IMCTextView);*/
        editarDatos = findViewById(R.id.buttonEditarDatos);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() == null){
            FirebaseAuth.getInstance().signOut();
            FirebaseFirestore.getInstance().terminate();
            Intent i = new Intent(ProfileInfoActivityMichel.this, LoginActivityMichel.class);
            startActivity(i);
            finish();
            return;
        }else{
            userId = mAuth.getCurrentUser().getUid();
            actualizarSaludo();
            //actualizarDatosPersonales();
        }

    }

    public void actualizarSaludo(){
        DocumentReference documentReference = fStore.collection("usuarios").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String nombre = documentSnapshot.getString("nombre_completo");
                saludo.setText("Hola, " + nombre);
            }
        });
    }

   /* public void actualizarDatosPersonales(){
        DocumentReference documentReference = fStore.collection("atributos_fisicos").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String altura1 = documentSnapshot.getString("altura");
                String peso1 = documentSnapshot.getString("peso");
                String fNacimiento1 = documentSnapshot.getString("fecha_de_nacimiento");
                String genero1 = documentSnapshot.getString("genero");


                altura.setText(altura1);
                Fnacimiento.setText(fNacimiento1);
                peso.setText(peso1);
                genero.setText(genero1);
                String IMC = calcularIMC(peso1, altura1);
                imc.setText(IMC);
            }
        });
    }*/
    /*public String calcularIMC(String peso_kg, String altura_m){
        String altura_sin_m[] = altura_m.split(" ");
        String peso_sin_kg[] = peso_kg.split(" ");
        float height = Float.parseFloat(altura_sin_m[0]);
        float weight = Float.parseFloat(peso_sin_kg[0]);
        float imc_total = weight/(height*height);

        Log.wtf("PESO", height+","+weight+","+imc_total);

        String imc_description = "";

        if (imc_total < 18.5)
            imc_description = "Bajo peso";
        else
        if ((imc_total > 18.5) &&(imc_total <24.9))
            imc_description = "Peso normal";
        else
        if ((imc_total > 25.0)&& (imc_total <29.9))
            imc_description = "Sobrepeso";
        else
        if ((imc_total > 30.0) && (imc_total <34.9))
            imc_description = "Obesidad grado I";
        else
        if ((imc_total > 35.0) && (imc_total <39.9))
            imc_description = "Obesidad grado II";
        else
        if (imc_total >= 40)
            imc_description = "Obesidad grado III";

        String imc_number =  String.format("%.2f", imc_total);
        String res =  imc_number + ", " + imc_description;
        return res;
    }

    /*public void goToMenuGrid(View v){
        Intent irAlMenu =new Intent(this, EjerciciosActivity.class);
        startActivity(irAlMenu);
        finish();
    }*/

    public void logOut(View v){
        FirebaseAuth.getInstance().signOut();
        FirebaseFirestore.getInstance().terminate();
        Intent i = new Intent(ProfileInfoActivityMichel.this, LoginActivityMichel.class);
        startActivity(i);
        finish();
    }


    public void verNotas(View v){
        Intent verNotas = new Intent(this,EditorNotaActivity.class);
        startActivity(verNotas);
    }

}