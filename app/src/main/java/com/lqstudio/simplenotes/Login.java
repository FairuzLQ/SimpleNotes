package com.lqstudio.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailUser, passUser;
    Button submitLogin;
    ProgressBar progressBar;
    TextView submitToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        emailUser = findViewById(R.id.login_email);
        passUser = findViewById(R.id.login_pass);

        submitLogin = findViewById(R.id.login_button_submit);

        progressBar = findViewById(R.id.login_progress);
        submitToRegister = findViewById(R.id.signin_register);

        submitLogin.setOnClickListener((v)-> loginUser());
        submitToRegister.setOnClickListener((v)-> startActivity(new Intent(Login.this,SignUp.class)));
    }

    void loginUser(){
        String emailValidation, passValidation;
        emailValidation = emailUser.getText().toString();
        passValidation = passUser.getText().toString();


        boolean isValidate = validateData(emailValidation,passValidation);
        if(!isValidate){
            return;
        }

        loginFirebaseValidate(emailValidation,passValidation);

    }

    void loginFirebaseValidate(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        sedangMenyimpan(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                sedangMenyimpan(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(Login.this, "Email belum terverifikasi", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void sedangMenyimpan(boolean menyimpan){
        if(menyimpan){
            progressBar.setVisibility(View.VISIBLE);

        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    boolean validateData(String email, String pass){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("Email is invalid");
            return false;
        }
        if(pass.length()<8){
            passUser.setError("Password harus lebih dari 7 digit!");
            return false;
        }

        return true;
    }
}