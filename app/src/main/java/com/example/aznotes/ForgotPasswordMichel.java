package com.example.aznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordMichel extends AppCompatActivity {
    MaterialButton recuperarBoton;
    TextInputEditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_michel);

        recuperarBoton = findViewById(R.id.recuperarBoton);
        emailEditText = findViewById(R.id.emailEditText);

        recuperarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    public void validate(){
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Correo inválido");
            Toast.makeText(this, "Por favor, ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        sendEmail(email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ForgotPasswordMichel.this, LoginActivityMichel.class);
        startActivity(intent);
        finish();
    }

    public void sendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordMichel.this, "Correo enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordMichel.this, LoginActivityMichel.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ForgotPasswordMichel.this, "Correo no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}