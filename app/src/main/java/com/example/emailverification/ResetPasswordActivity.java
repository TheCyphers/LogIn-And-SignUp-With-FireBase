package com.example.emailverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText userPassword,userConfPassword;
    Button savePasswordbtn;

    FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userConfPassword = findViewById(R.id.resetconfirmpassword);
        userPassword = findViewById(R.id.resetnewpassword);

        savePasswordbtn = findViewById(R.id.resetpasswordbtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        savePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(userPassword.getText().toString().isEmpty()){
                    userPassword.setError("Required Field");
                    return;
                }

                if(userConfPassword.getText().toString().isEmpty()){
                    userConfPassword.setError("Required Field");
                    return;
                }

                if(!userPassword.getText().toString().equals(userConfPassword.getText().toString())){
                    userConfPassword.setError("password Do Not Match");
                    return;
                }

                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPasswordActivity.this,"Password Updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(ResetPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}