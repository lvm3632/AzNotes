package com.example.aznotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.provider.MediaStore;
import com.squareup.picasso.Picasso;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

public class GridMenuActivity extends AppCompatActivity {

    private ImageView mi_perfil, mis_notas, escribir_nota, salir;

    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    /** Firebase Storage **/
    private FirebaseStorage mStorage;
    private ImageView imageProfilePic;
    private Button btnOpenGallery;
    private Button subirImagen, cambiarImagen;
    private Uri currentImageURl = null;
    /** End of storage **/

    
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

        imageProfilePic = findViewById(R.id.profileImage);
        subirImagen = findViewById(R.id.btnSubirImagen);
        cambiarImagen = findViewById(R.id.btnActualizarImagen);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance();

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
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        //Add your action onClick
                        //Toast.makeText(GridMenuActivity.this, "ITEM1", Toast.LENGTH_SHORT).show();
                        cambiarAMiPerfil(findViewById(android.R.id.content).getRootView());
                        break;
                    case R.id.page_2:
                        //Toast.makeText(GridMenuActivity.this, "ITEM2", Toast.LENGTH_SHORT).show();
                        escribirNota(findViewById(android.R.id.content).getRootView());

                        break;

                    case R.id.page_3:
                        //Toast.makeText(GridMenuActivity.this, "ITEM3", Toast.LENGTH_SHORT).show();
                        cambiarAMisNotas(findViewById(android.R.id.content).getRootView());
                        break;
                }
                return false;
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
        loadDefaults();
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
    /** Upload Image **/
    private void loadDefaults() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser.getPhotoUrl() != null) {
            Picasso.get().load(currentUser.getPhotoUrl()).into(imageProfilePic);
            currentImageURl = currentUser.getPhotoUrl();
        }
    }
    /** End of Upload Image **/
    public void openGallery(View view) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 42);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 42) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUrl = data.getData();
                imageProfilePic.setImageURI(imageUrl);
                currentImageURl = imageUrl;
            }
        }
    }
    private void uploadImage(UserProfileChangeRequest.Builder profileChangeRequest) {
        StorageReference storage = mStorage.getReference();
        StorageReference ref = storage.child("profile_photos/"+ mAuth.getCurrentUser().getUid()); //TODO file extension
        ref.putFile(currentImageURl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            updateProfile(profileChangeRequest.setPhotoUri(task.getResult()));
                            Toast.makeText(GridMenuActivity.this, "IMAGEN SUCCESS", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(GridMenuActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GridMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateProfile(UserProfileChangeRequest.Builder profileChangeRequest) {
        mAuth.getCurrentUser().updateProfile(profileChangeRequest.build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(GridMenuActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(GridMenuActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void submit(View view) {
        Toast.makeText(GridMenuActivity.this, "SUBMIT???", Toast.LENGTH_SHORT).show();
        UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder();
        if(currentImageURl != null) {
            uploadImage(profileUpdates);
        } else {
            profileUpdates.setPhotoUri(null);
            updateProfile(profileUpdates);
        }
    }
}