package com.swiftqube.guessqube;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swiftqube.guessqube.people.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    EditText edtEmail, edtPassword;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mfirebaseAuth;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    FirebaseUser user;
    String name = "";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    UserDetails userDetails = new UserDetails();

    LoginButton loginButton;
    CallbackManager mCallbackManager;
    ActionBar actionBar;
    TextView txtSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        txtSignup = findViewById(R.id.txtSignUp);
        txtSignup.setOnClickListener(this);

        loginButton = findViewById(R.id.btnFacebook);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserDatabase");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        btnSubmit = findViewById(R.id.btnLogin);
        btnSubmit.setOnClickListener(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        progressDialog = new ProgressDialog(this);

        if(!(prefs.getString("PASSWORD", "X%p8kznAA1").equals("X%p8kznAA1"))){
            edtEmail.setText(prefs.getString("EMAIL","myemail@whatever.com"));
            edtPassword.setText(prefs.getString("PASSWORD", "X%p8kznAA1"));
        }

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Dammit", "Hello"+loginResult.getAccessToken().getToken());

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Unknown error occured", Toast.LENGTH_LONG).show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    name = user.getDisplayName();
                    Log.d("Dammit", "Name of user" + user.getDisplayName());
                }else {
                    Log.d("Dammit", "something went wrong");
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Dammit", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mfirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            user = mfirebaseAuth.getCurrentUser();
                            Log.d("Dammit", "Name of user" + user.getDisplayName());
                            Log.w("Dammit", "signInWithCredential", task.getException());
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(!(dataSnapshot.hasChild(user.getUid().toString()))){
                                     createTable(user.getDisplayName().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Authentication error",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }


    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnLogin){
            loginUser();
        }
        if (view.getId()==R.id.txtSignUp){
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

    public void loginUser(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;}

        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        mfirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            editor.putString("PASSWORD", edtPassword.getText().toString().trim());
                            editor.putString("EMAIL", edtEmail.getText().toString().trim());
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                            user = mfirebaseAuth.getCurrentUser();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Sign in failed. Check details or network.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createTable(String name){
        String key = mDatabase.child("UserDatabase").push().getKey();
        key = user.getUid();

        Map<String, Object> userData = userDetails.getUserData(name);
        Map<String, Object>update = new HashMap<>();
        update.put(key, userData);
        mDatabase.updateChildren(update);
    }

}
