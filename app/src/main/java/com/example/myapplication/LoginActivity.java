package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailFieldText, passwordFieldText;
    Button loginButton;
    ProgressBar progress_bar_;

    TextView signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailFieldText = findViewById(R.id.emailField);
        passwordFieldText = findViewById(R.id.passwordField);
        progress_bar_ = findViewById(R.id.progress_bar);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.SignUp_button);



        //тут теперь метод для кнопки с лямдой
        loginButton.setOnClickListener(v -> LogIn());

        signUpButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));


    }

    private void LogIn() {
        Log.i("createAccount","анжали кнопку");
        String email = emailFieldText.getText().toString();
        String password = passwordFieldText.getText().toString();

        boolean isValidated = validateData(email,password);

        if(!isValidated){return;}

        loginAccountInFirebase(email,password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(false);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //успешный вход
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity (new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Utility.showToast(LoginActivity.this,"Email is not verified");
                    }
                }else{
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }

    boolean validateData(String email, String password){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailFieldText.setError("Email is invalid.");
            return false;
        }
        if(password.length()<6){
            passwordFieldText.setError("Too short password. At least 6 symbols.");
            return false;
        }

        return true;
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progress_bar_.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }else{
            progress_bar_.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }


}