package com.example.hp.colorlock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity2 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
   // private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton mSignInButton;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private String email,password;
    private TextView already_reg;
    private String user_name,user_email,user_id;
    private User userdata;


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
   // private FirebaseAuth firebaseAuth;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private Button sign_in;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        progressDialog=new ProgressDialog(this);
        FirebaseApp firebaseApp=FirebaseApp.getInstance();
        firebaseAuth=FirebaseAuth.getInstance(firebaseApp);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        sign_in= findViewById(R.id.email_sign_in_button);
        sign_in.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressDialog.show();
                attemptLogin();
            }
        });
        already_reg=findViewById(R.id.already);
        already_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(),User_Registration.class);
                startActivity(intent);
            }
        });
        mSignInButton = findViewById(R.id.sign_in_button);
        // Set click listeners
        mSignInButton.setOnClickListener(this);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if(email.equals(""))
            mEmailView.setError("Enter mail id!");
        else if(!isEmailValid(email)){
            mEmailView.setError("Enter valid email!");
        }
        if(password.equals(""))
            mPasswordView.setError("Enter password!");
        else if(!isPasswordValid(password)){
            mPasswordView.setError("Password too short!");
        }
        else{
            progressDialog.show();
            progressDialog.setMessage("Signing in...");
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

           // mAuthTask = new UserLoginTask(email, password);
           // mAuthTask.execute((Void) null);
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity2.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //showProgress(false);
                        //SharedPreferences.Editor editor=getSharedPreferences("credentials",0).edit();
                        //editor.putString(FirebaseAuth.getInstance().getUid(),email);
                        //editor.commit();
                        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("user_ids").child(FirebaseAuth.getInstance().getUid());
                        Log.i("firebase_auth=====",FirebaseAuth.getInstance().getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    user_id= String.valueOf(dataSnapshot.getValue());
                                    Log.i("user_id Loginac2",user_id);
                                    Toast.makeText(LoginActivity2.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    saveSP(user_id);
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivity2.this,Main_menu.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity2.this,"Please try again with valid credentials!",Toast.LENGTH_SHORT).show();
                       // showProgress(false);
                    }
                }
                //to save the extracted user credentials in a shared preference
                private void saveSP(String uid) {
                    Log.i("savSP---------","HII");
                    Log.i("user_id=======",uid);
                    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users/"+uid+"/USER_DATA");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                userdata=dataSnapshot.getValue(User.class);
                                user_id=userdata.getUid();
                                user_email=userdata.getEmail();
                                user_name=userdata.getName();
                                SharedPreferences.Editor editor=getSharedPreferences("credentials",0).edit();
                                editor.putString("user_id",userdata.getUid());
                                editor.putString("user_name",userdata.getName());
                                editor.putString("user_email",userdata.getEmail());
                                editor.commit();
                                Log.i("user_id===========",user_id);
                                Log.i("user_name=========",user_name);
                                Log.i("user_email=========",user_email);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    @Override
    public void onClick(View v) {
        progressDialog.setMessage("Signing in..");
        progressDialog.show();
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                progressDialog.dismiss();
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                progressDialog.dismiss();
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginActivity2.this, Main_menu.class));
                            finish();
                        }
                    }
                });

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

