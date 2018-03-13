package com.example.aditya.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

private EditText Email;
private EditText Password;
private Button Login;
private TextView RegisterUser;
private ProgressDialog progressDialog;
private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.emailText);
        Password = findViewById(R.id.passwordText);
        RegisterUser = findViewById(R.id.registerView);
        Login = findViewById(R.id.loginButton);
        Login.setOnClickListener(this);
        RegisterUser.setOnClickListener(this);
    }

    private void loginUser(){
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Authenticating User ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"User Authenticated...",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this,"User Not Authenticated...",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == Login){
            loginUser();
        }
        if(view == RegisterUser){
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        }

    }
}
