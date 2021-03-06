package com.example.aznotes;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity{

    private TextView nuevoUsuario,
            bienvenidoLabel,
            continuarLabel;

    private TextInputEditText emailEditText,
            passwordEditText,
            confirmPasswordEditText;

    private TextInputLayout usuarioSignUpTextField,
            contrasenaTextField,
            nameTextField;

    private ImageView signUpImageView;
    private MaterialButton inicioSesion;
    /*
     *  Variables bases de datos
     */
    private String userId;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;

    private static boolean isNewUser = false;

    protected static boolean signUpFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpImageView = findViewById(R.id.signUpImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioSignUpTextField = findViewById(R.id.usuarioSignUpTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameTextField = findViewById(R.id.nameTextField);

        // Referencia a la DB de usuarios autorizados
        mAuth = FirebaseAuth.getInstance();

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionBack();
            }
        });
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }
    /** Creamos onStart una sesion a la DBFirestore **/
    // @Override
    protected void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();

    }
    // Cuando das back o en la flecha del dispositivo
    @Override
    public void onBackPressed(){
        transitionBack();
    }
    public void validate(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String nombre = nameTextField.getEditText().getText().toString();

        if(nombre.isEmpty()){
            nameTextField.setError("Ingresa un nombre");
            Toast.makeText(SignUpActivity.this, "Campo vac??o", Toast.LENGTH_SHORT).show();
            SignUpActivity.signUpFlag=false;
            return;
        }


        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Correo inv??lido");
            Toast.makeText(SignUpActivity.this, "Correo inv??lido", Toast.LENGTH_SHORT).show();
            SignUpActivity.signUpFlag=false;
            return;
        }else{
            emailEditText.setError(null);
        }
        if(confirmPassword.isEmpty() && password.isEmpty()){
            SignUpActivity.signUpFlag=false;
            Toast.makeText(SignUpActivity.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty() || confirmPassword.isEmpty() || password.length() < 6 || confirmPassword.length() < 6 ){
            passwordEditText.setError("Se necesitan m??s de 6 car??cteres");
            SignUpActivity.signUpFlag=false;
            Toast.makeText(SignUpActivity.this, "Se necesitan m??s de 6 car??cteres de contrase??a", Toast.LENGTH_SHORT).show();
        }else if(!Pattern.compile("[0-9]").matcher(password).find()){
            passwordEditText.setError("La contrase??a necesita al menos un n??mero");
            SignUpActivity.signUpFlag=false;
            Toast.makeText(SignUpActivity.this, "La contrase??a necesita al menos un n??mero", Toast.LENGTH_SHORT).show();
            return;
        }else{
            passwordEditText.setError(null);
        }

        if(!confirmPassword.equals(password)){
            confirmPasswordEditText.setError("Deben ser iguales");
            SignUpActivity.signUpFlag=false;
            Toast.makeText(SignUpActivity.this, "Las contrase??as deben ser iguales", Toast.LENGTH_SHORT).show();
            return;
        }else{
            SignUpActivity.signUpFlag=true;
            registrar(email, password,nombre);
        }
    }
    public void registrar(String email, String password, String nombre){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore
                                    .collection("usuarios").document(userId);

                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre_completo", nombre);
                            user.put("correo", email);
                            user.put("password", password);
                            user.put("id_usuario", password);


                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.wtf("Usuario creado - ", "onSuccess: user Profile is created for " + user);
                                    Toast.makeText(SignUpActivity.this, "Registro completo", Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Error usuario:", "onFailure: " + e.toString());
                                    Toast.makeText(SignUpActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                                }
                            });

                            isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            SignUpActivity.setIsNewUser(isNewUser);
                            if(isNewUser){
                                Intent intent = new Intent(SignUpActivity.this, ProfileInfoActivity.class);
                                startActivity(intent);
                                Toast.makeText(SignUpActivity.this, "Nuevo usuario", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }else{


                            Toast.makeText(SignUpActivity.this, "Fallo en registrarse"  + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void transitionBack(){
        // La clase en la que estamos, y la clase a la que queremos ir
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        // Arreglo de animaciones
        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(signUpImageView, "LogoImageTrans");
        pairs[1] = new Pair<View, String>(bienvenidoLabel, "textTrans");
        pairs[2] = new Pair<View, String>(continuarLabel, "iniciaSesionTextTrans");
        pairs[3] = new Pair<View, String>(usuarioSignUpTextField, "emailInputTextTrans");
        pairs[4] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
        pairs[5] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");
        pairs[6] = new Pair<View, String>(nuevoUsuario, "newUserTrans");
        // Hace las animaciones si la versi??n es mayor a lollipop o igual
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
            finish();
        }
    }

    public static boolean isNewUser() {
        return isNewUser;
    }

    public static void setIsNewUser(boolean newUser) {
        isNewUser = newUser;
    }
}