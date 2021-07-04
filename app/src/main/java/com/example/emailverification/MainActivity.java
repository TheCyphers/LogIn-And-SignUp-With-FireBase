package com.example.emailverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button logout,verifyEmailbtn;
    TextView verifyMsg;
    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth  = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        verifyMsg = findViewById(R.id.verifyemailmessage);
        verifyEmailbtn = findViewById(R.id.verifyemailbtn);

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


        //IF THE EMAIL IS NOT VERIFIED THEN THE TEXT VIEW AND BUTTON IS SET TO VISIBLE
        if(!auth.getCurrentUser().isEmailVerified()){
            verifyEmailbtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        //WHEN THE VERIFY BUTTON IS CLICKED THEN THE VERIFICATION EMAIL IS SENT TO THE USER.
        verifyEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send the verification email
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this,"verification is email is sent",Toast.LENGTH_SHORT).show();
                        verifyEmailbtn.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);
                    }
                });
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.resetpasswordmenu){
            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        }

        if(item.getItemId() == R.id.updateemailmenu){
            View view = inflater.inflate(R.layout.reset_pop,null);
            reset_alert.setTitle("Update Email")
                    .setMessage("Enter Your New Email Address")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // validate the email add and  send the reset link
                            EditText email = view.findViewById(R.id.resetpopemail);
                            if(email.getText().toString().isEmpty()){
                                email.setError("Required Field");
                                return;
                            }

                            //send the reset link
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this,"Email Updated.",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel ",null)
                    .setView(view)
                    .create().show();
        }

        if(item.getItemId() == R.id.deletaccmenu){
            reset_alert.setTitle("Delete Account Permantly")
                    .setMessage("Are YouSure")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this,"Account Deleted",Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }

}