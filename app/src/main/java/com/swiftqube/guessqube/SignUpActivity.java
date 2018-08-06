package com.swiftqube.guessqube;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swiftqube.guessqube.people.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    EditText edtEmail, edtPassword, edtName;
    private FirebaseAuth mfirebaseAuth;
    ProgressDialog progressDialog;
    FirebaseUser user;
    DatabaseReference mDatabase;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ActionBar actionBar;
    UserDetails userDetails = new UserDetails();
    private static final String REQUIRED = "Required";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSubmit = findViewById(R.id.btnSignup);
        btnSubmit.setOnClickListener(this);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        progressDialog = new ProgressDialog(this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnSignup){
            registerUser();
        }
    }

    public void registerUser(){
        final String name = edtName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError(REQUIRED);
            return;}

        if (name.length()<2){
            edtName.setError("Not less than 2 char");
            return;}

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(REQUIRED);
            return;}

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError(REQUIRED);
            return;}

        if (password.length()<5){
            edtPassword.setError("should not be less than 5 char");
            return;}

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        mfirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            editor.putString("PASSWORD", password);
                            editor.putString("EMAIL", email);
                            editor.apply();
                            user = mfirebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            createTable(name);
                            Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Registration error. Check your details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createTable(String name){
        mDatabase = mDatabase.child("UserDatabase");
        String key = user.getUid();

        Map<String, Object> userData = userDetails.getUserData(name);
        Map<String, Object>update = new HashMap<>();
        update.put(key, userData);
        mDatabase.updateChildren(update);
    }

}
