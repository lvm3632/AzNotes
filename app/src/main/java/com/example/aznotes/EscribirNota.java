package com.example.aznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EscribirNota extends AppCompatActivity {
    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private Button guardarNota;
    private TextInputEditText titleTextField,
            notaTextField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escribir_nota);

        guardarNota = findViewById(R.id.guardarNota);
        titleTextField = findViewById(R.id.titleTextField);
        notaTextField = findViewById(R.id.notaEditText);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        guardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date horaInicio = new Date();
                String titulo = titleTextField.getText().toString();
                String nota = notaTextField.getText().toString();

                if(nota.isEmpty() || titulo.isEmpty()){
                    Toast.makeText(EscribirNota.this, "Completa los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }

                actualizarDatos(titulo, nota, horaInicio);
                cambiarAMiPerfil(v);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
            if(mAuth.getCurrentUser() == null){
            FirebaseAuth.getInstance().signOut();
            FirebaseFirestore.getInstance().terminate();
            Intent i = new Intent(EscribirNota.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }
        userId = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GridMenuActivity.class);
        startActivity(i);
        finish();
        return;
    }

    public void cambiarAMiPerfil(View v){
        Intent i = new Intent(this, GridMenuActivity.class);
        actualizarNotas();
        startActivity(i);
        finish();
    }

    public void actualizarDatos(String titulo_nota, String texto_nota, Date fecha_nota) {
        DocumentReference docRef = fStore.collection("notas").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    if(document.getData() == null){
                        Map<String,Object> notas = new HashMap<>();
                        notas.put("0", Arrays.asList(titulo_nota,texto_nota,fecha_nota));
                        fStore.collection("notas").document(userId)
                                .set(notas)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Nuevo documento", "DocumentSnapshot successfully written!");
                                        Toast.makeText(EscribirNota.this, "Se guardó la primera nota exitosamente", Toast.LENGTH_SHORT).show();
                                        GridMenuActivity.nuevoUsarioNotas = false;
                                        actualizarNotas();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("No se pudo crear el documento", "Error writing document", e);
                                        Toast.makeText(EscribirNota.this, "No se pudo guardar la nota", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        if(document.exists()){
                            Map<String,Object> notas = document.getData();
                            notas.put(notas.size()+"", Arrays.asList(titulo_nota,texto_nota,fecha_nota));
                            docRef.update(notas).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.wtf("Usuario creado - ", "onSuccess: user Profile is created for " + userId);
                                    actualizarNotas();
                                    Toast.makeText(EscribirNota.this, "Se guardó la  nota exitosamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Error usuario:", "onFailure: " + e.toString());
                                    Toast.makeText(EscribirNota.this, "No se pudo guardar la nota", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                }
            }
        });
    }

    public void actualizarNotas() {
        DocumentReference docRef = fStore.collection("notas").document(userId);
        Log.wtf("UserID: ", userId+ "");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() == null || document.getData().size() <= 0){

                        return;
                    }
                    if(document != null){

                        for(int i=0;i<document.getData().size();i++){
                            String index = String.valueOf(i);
                            Nota notausuario = new Nota();

                            ArrayList<Object> lista_notas = (ArrayList) document.getData().get(index);
                            Log.wtf("Tamaño lista X", lista_notas.size()+"");

                            int indice=0;
                            notausuario.setId_Nota(Integer.parseInt(index));
                            for (Object item: lista_notas) {
                                Log.wtf("Objetos lista X", lista_notas.toString()+"");
                                if(indice == 0){
                                    notausuario.setTitle(item.toString());
                                }else if(indice == 1){
                                    notausuario.setTexto(item.toString());
                                }else if(indice == 2){
                                    notausuario.setFecha(item.toString());
                                }
                                indice++;

                                Log.wtf("Nota del usuario", "\n Inicio: "
                                        + notausuario.getTitle() + "\n"
                                        + notausuario.getTexto() + "\n"
                                        + notausuario.getFecha() + "\n");
                            }

                            // Agregar HT
                            GridMenuActivity.HT_Notas.put(index, notausuario);
                        }
                    }else{
                        Log.wtf("Lista de notas: ", "Está vacía");
                    }
                }else{
                    Log.wtf("Error con firebase: ", task.getException());
                }
            }
        });
    }


}