package com.lqstudio.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class SignUp extends AppCompatActivity {

    EditText emailUser, passUser, confPassUser;
    Button submitSignUp;
    ProgressBar progressBar;
    TextView submitToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        emailUser = findViewById(R.id.signup_email);
        passUser = findViewById(R.id.signup_pass);
        confPassUser = findViewById(R.id.signup_conf_pass);

        submitSignUp = findViewById(R.id.signup_button_submit);

        progressBar = findViewById(R.id.signup_progress);
        submitToLogin = findViewById(R.id.signup_login);

        submitSignUp.setOnClickListener(v-> createAccount());
        submitToLogin.setOnClickListener(v-> finish());


    }

    void createAccount(){
        String emailValidation, passValidation, confPassValidation;
        emailValidation = emailUser.getText().toString();
        passValidation = passUser.getText().toString();
        confPassValidation = confPassUser.getText().toString();

        boolean isValidate = validateData(emailValidation,passValidation,confPassValidation);
        if(!isValidate){
            return;
        }

        createAccountInFirebase(emailValidation,passValidation);
    }

    void createAccountInFirebase(String email, String password){
        sedangMenyimpan(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        sedangMenyimpan(false);
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this, "Pendaftaran Berhasil, periksa email Anda untuk verifikasi", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            sedangMenyimpan(false);
                        }else{
                            Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void sedangMenyimpan(boolean menyimpan){
        if(menyimpan){
            progressBar.setVisibility(View.VISIBLE);
            submitToLogin.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            submitToLogin.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String pass, String confPass){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("Email is invalid");
            return false;
        }
        if(pass.length()<8){
            passUser.setError("Password harus lebih dari 7 digit!");
            return false;
        }
        if(!pass.equals(confPass)){
            confPassUser.setError("Password tidak sama!");
            return false;
        }
        return true;
    }

}