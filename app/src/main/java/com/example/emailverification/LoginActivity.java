package com.example.emailverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button createAccountButton,loginbtn,forgetPassword;
    EditText username,password;
    FirebaseAuth fireBaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        fireBaseAuth = FirebaseAuth.getInstance();

       reset_alert = new AlertDialog.Builder(this);
       inflater = this.getLayoutInflater();


        createAccountButton = findViewById(R.id.createaccountbutton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        username = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        loginbtn = findViewById(R.id.loginbutton);
        forgetPassword = findViewById(R.id.forgetpassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start alerdialog

                View view = inflater.inflate(R.layout.reset_pop,null);

                reset_alert.setTitle("Reset Forget Password?")
                        .setMessage("Enter Your Email to get Password Reset Link")
                        .setPositiveButton("Reset ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // validate the email add and  send the reset link
                                EditText email = view.findViewById(R.id.resetpopemail);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Required Field");
                                    return;
                                }

                                //send the reset link
                                fireBaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LoginActivity.this,"Reset Email Sent",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure( Exception e) {
                                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel ",null)
                        .setView(view)
                        .create().show();

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXTRACT DATA and VALIDATE

                if(username.getText().toString().isEmpty()){
                    username.setError("Email is missing");
                    return;
                }
                //getText returns object not string so we use toString()
                if(password.getText().toString().isEmpty()){
                    password.setError("Password is Missing");
                    return;

                }

                fireBaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Login is sucessful
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    //FIRE BASE FEATURE
    //if the user has already loged in then noo need to ask for login.

    @Override
    protected void onStart(){
        super.onStart();
        //IF WE HAVE USER BEFORE THEN IF IS RETURNING TRUE.
        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}