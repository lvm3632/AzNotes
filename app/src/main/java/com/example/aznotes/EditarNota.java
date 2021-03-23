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
import java.util.Map;

public class EditarNota extends AppCompatActivity {

    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private Button actualizarNota;
    private TextInputEditText titleTextField,
            notaTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nota);

        actualizarNota = findViewById(R.id.actualizarNota);
        titleTextField = findViewById(R.id.titleTextField);
        notaTextField = findViewById(R.id.notaEditText);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        actualizarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date horaInicio = new Date();
                String titulo = titleTextField.getText().toString();
                String nota = notaTextField.getText().toString();

                if(nota.isEmpty() || titulo.isEmpty()){
                    Toast.makeText(EditarNota.this, "Completa los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(EditarNota.this, "Nota actualizada", Toast.LENGTH_SHORT).show();
                editarNota(titulo, nota, horaInicio, RecyclerActivityNotas.posicion);
                cambiarMenu(v);
                finish();
            }
        });
    }

    public void editarNota(String titulo, String texto, Date fecha, String pos) {
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
                    if(document.exists()){
                            Nota notausuario = new Nota();
                            ArrayList<Object> lista_notas = (ArrayList) document.getData().get(pos);
                            Log.wtf("Tamaño lista X", lista_notas.size()+"");
                            int indice=0;
                            notausuario.setId_Nota(Integer.parseInt(pos));
                            for (Object item: lista_notas) {
                                Log.wtf("Objetos lista X", lista_notas.toString()+"");
                                if(indice == 0){
                                    notausuario.setTitle(titulo);
                                }else if(indice == 1){
                                    notausuario.setTexto(texto);
                                }else if(indice == 2){
                                    notausuario.setFecha(fecha.toString());
                                }
                                indice++;

                                Log.wtf("Nota del usuario en editar", "\n Inicio: "
                                        + notausuario.getTitle() + "\n"
                                        + notausuario.getTexto() + "\n"
                                        + notausuario.getFecha() + "\n");
                            }

                            // Agregar HT
                        GridMenuActivity.HT_Notas.put(pos, notausuario);
                        Map<String,Object> notas = document.getData();
                        notas.put(pos, Arrays.asList(titulo,texto,fecha));

                        docRef.update(notas).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.wtf("Nota actualizada - ", "La nota se actualizo posicion: " + pos);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Error la nota no se actualizo:", "onFailure: " + e.toString());
                            }
                        });


                    }else{
                        Log.wtf("Lista de notas: ", "Está vacía");
                    }
                }else{
                    Log.wtf("Error con firebase: ", task.getException());
                }
            }
        });
    }

    public void cargarDatos(String pos) {
        DocumentReference docRef = fStore.collection("notas").document(userId);
        Log.wtf("UserID cargar datos: ", userId+ "");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() == null || document.getData().size() <= 0){
                        return;
                    }
                    if(document.exists()){
                        ArrayList<Object> lista_notas = (ArrayList) document.getData().get(pos);
                        Log.wtf("Tamaño lista X", lista_notas.size()+"");
                        int indice=0;

                        for (Object item: lista_notas) {
                            Log.wtf("Objetos lista X", lista_notas.toString()+"");
                            if(indice == 0){
                                titleTextField.setText(item.toString());
                            }else if(indice == 1){
                                notaTextField.setText(item.toString());
                            }/*else if(indice == 2){
                                notausuario.setFecha(item.toString());
                            }*/
                            indice++;
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




    @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser() == null){
            FirebaseAuth.getInstance().signOut();
            FirebaseFirestore.getInstance().terminate();
            Intent i = new Intent(EditarNota.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }
        userId = mAuth.getCurrentUser().getUid();
        cargarDatos(RecyclerActivityNotas.posicion);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GridMenuActivity.class);
        startActivity(i);
        finish();
        return;
    }

    public void cambiarMenu(View v){
        Intent irMenu = new Intent(this,GridMenuActivity.class);
        startActivity(irMenu);
        finish();
    }
}