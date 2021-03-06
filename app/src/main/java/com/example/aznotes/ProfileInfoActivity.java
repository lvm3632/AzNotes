package com.example.aznotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class ProfileInfoActivity extends AppCompatActivity {
    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    private TextView saludo, lblUltimaNota, textNumeroDeNotas, textUltimaNota, tituloUltimaNota;
    private Button editarDatos;

    //Testing
    //protected static boolean ProfileInfoActivityTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        saludo = findViewById(R.id.saludoProfile);
        editarDatos = findViewById(R.id.buttonEditarDatos);
        lblUltimaNota = findViewById(R.id.lblUltimaNota);
        textNumeroDeNotas = findViewById(R.id.textNumeroDeNotas);
        textUltimaNota = findViewById(R.id.textUltimaNota);
        tituloUltimaNota = findViewById(R.id.tituloUltimaNota);

        mAuth = FirebaseAuth.getInstance();

        //ProfileInfoActivityTest=false;
       /* if(LoginActivity.changingScene){
            LoginActivity.changingScene=true;
        }else{
            LoginActivity.changingScene=false;
        }*/
        //cambiarTrue();

        // Menu
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        //Add your action onClick
                        //Toast.makeText(ProfileInfoActivity.this, "ITEM1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.page_2:
                        //Toast.makeText(ProfileInfoActivity.this, "ITEM2", Toast.LENGTH_SHORT).show();
                        escribirNota(findViewById(android.R.id.content).getRootView());

                        break;

                    case R.id.page_3:
                        //Toast.makeText(ProfileInfoActivity.this, "ITEM3", Toast.LENGTH_SHORT).show();
                        cambiarAMisNotas(findViewById(android.R.id.content).getRootView());
                        break;
                }
                return false;
            }
        });
        // End of menu

    }


    /*public void cambiarTrue(){
        LoginActivity.changingScene=true;
    }*/

    /*public void cambiarFalse(){
        LoginActivity.changingScene=false;
    }*/

    @Override
    protected void onStart() {
        super.onStart();

        fStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() == null){
           // cambiarFalse();
            FirebaseAuth.getInstance().signOut();
            FirebaseFirestore.getInstance().terminate();
            Intent i = new Intent(ProfileInfoActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return;
        }else{
            //cambiarTrue();
            userId = mAuth.getCurrentUser().getUid();
            //Log.wtf("Entro changing", LoginActivity.changingScene+"");
            actualizarSaludo();
            actualizarUltimaNota();
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
    public void actualizarUltimaNota() {
        DocumentReference docRef = fStore.collection("notas").document(userId);
        Log.wtf("UserID: ", userId+ "");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() == null || document.getData().size() <= 0){
                        tituloUltimaNota.setText("No tienes notas registradas.");
                        lblUltimaNota.setText("");
                        textUltimaNota.setText("");
                        textNumeroDeNotas.setText("");
                        return;
                    }
                    if(document != null){
                            String index = String.valueOf(document.getData().size()-1);
                            ArrayList<Object> lista_notas = (ArrayList) document.getData().get(index);
                            Log.wtf("Tama??o lista X", lista_notas.size()+"");
                            int indice=0;
                            Nota notita = new Nota();
                            for (Object item: lista_notas) {
                                Log.wtf("Objetos lista X", lista_notas.toString()+"");
                                if(indice == 0){
                                    tituloUltimaNota.setText("T??tulo: " + item.toString());
                                    lblUltimaNota.setText("");
                                }else if(indice == 1){
                                    textUltimaNota.setText("Mensaje: " + item.toString());
                                }else if(indice == 2){
                                    notita.setFecha(item.toString());
                                    lblUltimaNota.setText("\nTu ??ltima nota es del: " + notita.getFecha());
                                }
                                indice++;
                            }
                        textNumeroDeNotas.setText("Actualmente tienes " + (document.getData().size()) + " notas registradas.");
                    }else{



                        Log.wtf("Lista de notas: ", "Est?? vac??a");
                    }
                }else{
                    Log.wtf("Error con firebase: ", task.getException());
                }
            }
        });
    }


    public void logOut(View v){
        //ProfileInfoActivityTest=true;
        FirebaseAuth.getInstance().signOut();
        FirebaseFirestore.getInstance().terminate();
        Intent i = new Intent(ProfileInfoActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void goToMenuGrid(View v){
        //ProfileInfoActivityTest=true;
        Intent irAlMenu =new Intent(this, GridMenuActivity.class);
        startActivity(irAlMenu);
        finish();
    }

    public void escribirNota(View v){
        //ProfileInfoActivityTest=true;
        Intent escribirNota = new Intent(this,EscribirNota.class);
        startActivity(escribirNota);
        finish();
    }

    private void cambiarAMisNotas(View v) {
        Intent i = new Intent(this, RecyclerActivityNotas.class);
        startActivity(i);
        finish();
    }



}