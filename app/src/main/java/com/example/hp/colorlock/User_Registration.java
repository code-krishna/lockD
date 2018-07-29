package com.example.hp.colorlock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_Registration extends AppCompatActivity {
    private Button register;
    private EditText email,password;
    private TextView already_reg;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__registration);
        register=findViewById(R.id.reg);
        email=findViewById(R.id.email_field);
        password=findViewById(R.id.password);
        already_reg=findViewById(R.id.already);
        already_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(),LoginActivity2.class);
                startActivity(intent);
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    email.setError("Enter the email!");
                }if(password.getText().toString().equals("")){
                    password.setError("Enter password!");
                }else{
                    String em=email.getText().toString();
                    String pass=password.getText().toString();
                    progressDialog.setMessage("Registering..");
                    progressDialog.show();
                    createAccount(em,pass);

                }

            }
        });
    }
    public void createAccount(String email,String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = firebaseAuth.getCurrentUser();
                            // updateUI(user);
                            Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(User_Registration.this,Main_menu.class));
                            progressDialog.hide();
                        } else {
                            progressDialog.hide();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(User_Registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                        // [END_EXCLUDE]
                    }
                });
    }
    // [END create_user_with_email]
}

