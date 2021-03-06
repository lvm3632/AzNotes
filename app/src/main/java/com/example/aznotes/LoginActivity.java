package com.example.aznotes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    TextView bienvenidoLabel, continuarLabel, nuevoUsuario, olvidasteContrasena;
    ImageView loginImageView;
    TextInputLayout usuarioTextField, contrasenaTextField;
    MaterialButton inicioSesion;
    TextInputEditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    //protected static boolean changingScene=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginImageView = findViewById(R.id.loginImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioTextField = findViewById(R.id.usuarioTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        olvidasteContrasena = findViewById(R.id.olvidasteContra);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        mAuth = FirebaseAuth.getInstance();


        //LoginActivity.changingScene=false;


        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // La clase en la que estamos, y la clase a la que queremos ir
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                // Arreglo de animaciones
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(loginImageView, "LogoImageTrans");
                pairs[1] = new Pair<View, String>(bienvenidoLabel, "textTrans");
                pairs[2] = new Pair<View, String>(continuarLabel, "iniciaSesionTextTrans");
                pairs[3] = new Pair<View, String>(usuarioTextField, "emailInputTextTrans");
                pairs[4] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
                pairs[5] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");
                pairs[6] = new Pair<View, String>(nuevoUsuario, "newUserTrans");

                // Hace las animaciones si la versi??n es mayor a lollipop o igual
                /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                    finish();
                }*/

            }
        });

        olvidasteContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    public void validate(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        //LoginActivity.changingScene=false;
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Correo inv??lido");
            //LoginActivity.changingScene=false;
            //Log.wtf("LoginActivity", LoginActivity.changingScene+"");
            Toast.makeText(LoginActivity.this, "Correo inv??lido", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //LoginActivity.changingScene=false;
           // Log.wtf("LoginActivity", LoginActivity.changingScene+"");
            //LoginActivity.changingScene=false;
            emailEditText.setError(null);
        }

        if(password.isEmpty() || password.length() < 6){
            //LoginActivity.changingScene=false;
            //Log.wtf("LoginActivity", LoginActivity.changingScene+"");
            passwordEditText.setError("Se necesitan m??s de 6 car??cteres");
            Toast.makeText(LoginActivity.this, "Se necesitan m??s de 6 car??cteres de contrase??a", Toast.LENGTH_SHORT).show();
            return;

        }else if(!Pattern.compile("[0-9]").matcher(password).find()){
            //LoginActivity.changingScene=false;
            // Log.wtf("LoginActivity", LoginActivity.changingScene+"");
            passwordEditText.setError("La contrase??a necesita al menos un n??mero");
            Toast.makeText(LoginActivity.this, "La contrase??a necesita al menos un n??mero", Toast.LENGTH_SHORT).show();

            return;
        }else{
            //LoginActivity.changingScene=false;
            passwordEditText.setError(null);
        }
        //LoginActivity.changingScene=true;
       // Log.wtf("LoginActivity", LoginActivity.changingScene+"");

        iniciarSesion(email, password);
    }
    public void iniciarSesion(String email, String password){
        //LoginActivity.changingScene=false;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if((task.isSuccessful())){
                           // LoginActivity.changingScene=true;
                            //Toast.makeText(LoginActivity.this, "Valor variable boolean" + LoginActivity.changingScene, Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "??Has iniciado sesi??n correctamente!", Toast.LENGTH_SHORT).show();
                            //Log.wtf("LoginActivity", LoginActivity.changingScene+"");
                            Intent intent = new Intent(LoginActivity.this, ProfileInfoActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            //LoginActivity.changingScene=false;
                            //Toast.makeText(LoginActivity.this, "Valor variable boolean" + LoginActivity.changingScene, Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Credenciales equivocadas, intenta de nuevo." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void hola(){
        Log.wtf("hola", "hola");
    }




}