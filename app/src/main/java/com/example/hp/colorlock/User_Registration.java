package com.example.hp.colorlock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_Registration extends AppCompatActivity {
    private Button register;
    private EditText email,password,user_name,uniq_id;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseApp firebaseApp;
    private User userData;
    private String em,pass,name,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__registration);
        register=findViewById(R.id.reg);
        email=findViewById(R.id.email_field);
        password=findViewById(R.id.password);
        user_name=findViewById(R.id.name);
        uniq_id=findViewById(R.id.unique_id);
        firebaseApp=FirebaseApp.getInstance();
        firebaseAuth=FirebaseAuth.getInstance(firebaseApp);
        progressDialog=new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    email.setError("Enter your email!");
                    email.requestFocus();
                }if(password.getText().toString().equals("")){
                    password.setError("Enter password!");
                    password.requestFocus();
                }
                if(user_name.getText().toString().equals("")){
                    user_name.setError("Enter name!");
                    user_name.requestFocus();
                }
                if(uniq_id.getText().toString().equals("")){
                    uniq_id.setError("Enter unique id!");
                    uniq_id.requestFocus();
                }
                else{
                    em=email.getText().toString();
                    pass=password.getText().toString();
                    name=user_name.getText().toString();
                    uid=uniq_id.getText().toString();
                    userData = new User(em,pass,name,uid);
                    progressDialog.show();
                    progressDialog.setMessage("Registering..");
                    databaseReference=FirebaseDatabase.getInstance().getReference("users").child(uid);
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);
                }

            }
        });
    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(!dataSnapshot.exists()){
                //  FirebaseDatabase.getInstance().goOffline();
                createAccount(em,pass);
                Log.i("inside if","yo");
                //dataRef.cl
            }else{
                Log.i("inside else","yo");
                progressDialog.dismiss();
                //createAccount(em,pass);
                Toast.makeText(getApplicationContext(),"User-id taken!",Toast.LENGTH_SHORT).show();
                uniq_id.setText("");
                uniq_id.setError("User id!");
                uniq_id.requestFocus();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public void createAccount(final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor=getSharedPreferences("credentials",MODE_PRIVATE).edit();
                            editor.putString(FirebaseAuth.getInstance().getUid(),uid);
                            editor.putString("user_id",uid);
                            editor.putString("user_name",name);
                            editor.putString("user_email",em);
                            editor.commit();
                            //databaseReference.removeEventListener(valueEventListener);
                            upload_userData(userData);
                            Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(User_Registration.this,Main_menu.class));
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(User_Registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

//
                        // [END_EXCLUDE]
                    }
                });
    }

    private void upload_userData(User user_data) {
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("users/"+uid+"/USER_DATA");
        //databaseReference.removeEventListener(valueEventListener);
        databaseReference.setValue(user_data);
        Log.i("firebase auth==========",FirebaseAuth.getInstance().getUid());
        databaseReference=FirebaseDatabase.getInstance().getReference("user_ids/"+FirebaseAuth.getInstance().getUid());
        databaseReference.setValue(uid);
    }
    public String trim_email(String email) {
        String alphaAndDigits = email.replaceAll("[^a-zA-Z0-9]+","");
        return alphaAndDigits;
    }
}

