package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.logging.Logger;
import android.util.Log;
public class CreateAccountActivity extends AppCompatActivity {

    EditText emailFieldText, passwordField1Text, passwordField2Text;
    Button create_account_button_;
    TextView LoginButton_;
    ProgressBar progress_bar_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailFieldText = findViewById(R.id.emailField);
        passwordField1Text = findViewById(R.id.passwordField1);
        passwordField2Text = findViewById(R.id.passwordField2);

        create_account_button_ = findViewById(R.id.create_account_button);
        LoginButton_ = findViewById(R.id.LoginButton);
        progress_bar_ = findViewById(R.id.progress_bar);



        //тут теперь метод для кнопки с лямдой
        create_account_button_.setOnClickListener(v -> CreateAccount());

        //тут для завершения активности потому что кликаем на логин и идем на другой экран
        LoginButton_.setOnClickListener(v -> finish());

    }

    void CreateAccount(){

        Log.i("createAccount","анжали кнопку");
        String email = emailFieldText.getText().toString();
        String password = passwordField1Text.getText().toString();
        String confirmPassword = passwordField2Text.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);

        if(!isValidated){return;}

        createAccountInFirebase(email,password);
    }

    void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            //создание аккаунта успешно
                            Toast.makeText(CreateAccountActivity.this,"Account was created successfully, check email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            Log.i("createAccountInFirebase","зарегали");
                            finish();
                        }else{
                            //неудача
                            Log.i("createAccountInFirebase","Неудача");
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }


    void changeInProgress(boolean inProgress){
        if(inProgress){
            progress_bar_.setVisibility(View.VISIBLE);
            create_account_button_.setVisibility(View.GONE);
        }else{
            progress_bar_.setVisibility(View.GONE);
            create_account_button_.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email, String password, String confirmPassword){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailFieldText.setError("Email is invalid.");
            return false;
        }
        if(password.length()<6){
            passwordField1Text.setError("Too short password. At least 6 symbols.");
            return false;
        }

        if(!password.equals(confirmPassword)){
            passwordField2Text.setError("Passwords are different.");
            return false;
        }

        return true;
    }

}