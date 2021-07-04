package com.example.emailverification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText registerFullName,registerEmail,registerPassword,registerConfPass;
    Button registerUserBtn;
    Button gotoLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        registerFullName = findViewById(R.id.regfullname);
        registerEmail = findViewById(R.id.regemail);
        registerPassword = findViewById(R.id.regpassword);
        registerConfPass = findViewById(R.id.regconfirmpassword);
        registerUserBtn = findViewById(R.id.regregisteruserbutton);
        gotoLogin = findViewById(R.id.regloginbtn);

        fAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXTRACT THE DATA FROM THE FORM
                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPassword = registerConfPass.getText().toString();

                if(fullName.isEmpty()){
                    registerFullName.setError("Full Name is Required");
                    return;
                }
                if(email.isEmpty()){
                    registerEmail.setError("Email is Required");
                    return;
                }
                if(password.isEmpty()){
                    registerFullName.setError("Passowrd is Required");
                    return;
                }
                if(confPassword.isEmpty()){
                    registerFullName.setError("Conformation Password is Required");
                    return;
                }
                 if(!password.equals(confPassword)){
                     registerConfPass.setError("Password is not Matched");
                     return;
                 }
                 //data is validated

                //register the user using fire base

                Toast.makeText(RegisterActivity.this,"DataValidated",Toast.LENGTH_SHORT).show();
    //addOnSuccessListener-if data is valid/correct
                 fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                     @Override
                     public void onSuccess(AuthResult authResult) {
                         Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                         startActivity(myIntent);
                         finish();
                     }
                 }).addOnFailureListener(new OnFailureListener() {//if the condition is failed and exception message is displayed.
                     @Override
                     public void onFailure(Exception e) {
                         Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                     }
                 });
            }
        });


    }
}