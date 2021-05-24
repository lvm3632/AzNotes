package com.example.aznotes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class RecyclerActivityNotas extends AppCompatActivity implements View.OnClickListener {
    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    private Hashtable<String, Nota> HT_Notas;
    private RecyclerView recyclerViewNotas;
    private NotasAdapter adapter;

    protected static boolean nuevoUsarioNotas = false;
    protected static String posicion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_notas);
        recyclerViewNotas = findViewById(R.id.recycler_recorridos);
        mAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        Log.wtf("Hash Size",GridMenuActivity.HT_Notas.size()+"");
        this.adapter = new NotasAdapter(GridMenuActivity.HT_Notas, this);
        Log.wtf("onCreate", GridMenuActivity.HT_Notas.size()+ "");
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNotas.setLayoutManager(llm);
        recyclerViewNotas.setAdapter(this.adapter);


        // Menu
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        //Add your action onClick
                        //Toast.makeText(RecyclerActivityNotas.this, "ITEM1", Toast.LENGTH_SHORT).show();
                        cambiarAMiPerfil(findViewById(android.R.id.content).getRootView());
                        break;
                    case R.id.page_2:
                        //Toast.makeText(RecyclerActivityNotas.this, "ITEM2", Toast.LENGTH_SHORT).show();
                        escribirNota(findViewById(android.R.id.content).getRootView());

                        break;

                    case R.id.page_3:
                        //Toast.makeText(RecyclerActivityNotas.this, "ITEM3", Toast.LENGTH_SHORT).show();
                        cambiarAMisNotas(findViewById(android.R.id.content).getRootView());
                        break;
                }
                return false;
            }
        });
        // End of menu

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RecyclerActivityNotas.this, GridMenuActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser() == null){
            FirebaseAuth.getInstance().signOut();
            FirebaseFirestore.getInstance().terminate();
            Intent i = new Intent(RecyclerActivityNotas.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }

        userId = mAuth.getCurrentUser().getUid();
        actualizarNotas();
    }


    @Override
    public void onClick(View v) {
        Log.wtf("Tamano onclick", GridMenuActivity.HT_Notas.size()+"");
        int position = recyclerViewNotas.getChildLayoutPosition(v);
        String pos = String.valueOf((position));
        Log.wtf("Posicion item: ", pos+"");
       AlertDialog.Builder builder = new AlertDialog.Builder(RecyclerActivityNotas.this);
        builder.setCancelable(true);
        builder.setTitle("Información de la nota");
        /*builder.setMessage("Has recorrido: " + EjerciciosActivity.HT_Recorridos.get(pos).getDistancia()
                + " metros con un total de " + EjerciciosActivity.HT_Recorridos.get(pos).getPasos() + " pasos en "
                + EjerciciosActivity.HT_Recorridos.get(pos).getTiempo_carrera() + "\nHora inicial: "
                + EjerciciosActivity.HT_Recorridos.get(pos).getTiempoInicioTotal() + "\nHora Final: "
                + EjerciciosActivity.HT_Recorridos.get(pos).getTiempoFinalTotal());*/

        builder.setMessage("¿Quieres editar esta nota? \n Título: " + GridMenuActivity.HT_Notas.get(pos).getTitle());

        builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(RecyclerActivityNotas.this, EditarNota.class);
                startActivity(i);
                dialog.cancel();
                RecyclerActivityNotas.posicion = pos;
                finish();
            }
        });

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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