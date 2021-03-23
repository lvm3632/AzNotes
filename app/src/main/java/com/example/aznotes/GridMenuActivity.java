package com.example.aznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Hashtable;

public class GridMenuActivity extends AppCompatActivity {

    private ImageView mi_perfil, mis_notas, escribir_nota, salir;
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    
    protected static Hashtable<String, Nota> HT_Notas;
    protected static boolean nuevoUsarioNotas = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_menu);
        mi_perfil = findViewById(R.id.mi_perfil);
        mis_notas = findViewById(R.id.mis_notas);
        salir = findViewById(R.id.salir);
        escribir_nota = findViewById(R.id.escribir_nota);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        HT_Notas = new Hashtable<>();

        mi_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarAMiPerfil(v);
            }
        });

        escribir_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribirNota(v);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(v);
            }
        });

        mis_notas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nuevoUsarioNotas == true){
                    AlertDialog.Builder builder = new AlertDialog.Builder(GridMenuActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Usuario nuevo");
                    builder.setMessage("No tienes notas que mostrar.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return;
                }
                actualizarNotas();
                cambiarAMisNotas(v);
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
            Intent i = new Intent(GridMenuActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }
        userId = mAuth.getCurrentUser().getUid();
        actualizarNotas();
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
                        nuevoUsarioNotas = true;
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
                        nuevoUsarioNotas = false;
                    }else{
                        Log.wtf("Lista de notas: ", "Está vacía");
                    }
                }else{
                    Log.wtf("Error con firebase: ", task.getException());
                }
            }
        });
    }


    public void logOut(View v){
        FirebaseAuth.getInstance().signOut();
        FirebaseFirestore.getInstance().terminate();
        Intent i = new Intent(GridMenuActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void escribirNota(View v){
        Intent escribirNota = new Intent(this,EscribirNota.class);
        startActivity(escribirNota);
        finish();
    }

    public void cambiarAMiPerfil(View v){
        Intent irPerfil = new Intent(this,ProfileInfoActivity.class);
        startActivity(irPerfil);
        finish();
    }

    private void cambiarAMisNotas(View v) {
        Intent i = new Intent(this, RecyclerActivityNotas.class);
        startActivity(i);
        finish();
    }

}