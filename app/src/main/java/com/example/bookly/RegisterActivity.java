package com.example.bookly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookly.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEt, addressEt, emailEt, passwordEt, confirmPasswordEt;
    private Button registerButton;

    private TextView loginTv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_modern);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance(
                "https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        nameEt = findViewById(R.id.nameEt);
        addressEt = findViewById(R.id.addressEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        registerButton = findViewById(R.id.registerBtn);
        loginTv = findViewById(R.id.loginTv);

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register seller
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
//                Toast.makeText(RegisterActivity.this, "Register account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String name, address, email, password, confirmPassword;
    private void inputData() {
        name = nameEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();

        if (address.isEmpty()){
            Toast.makeText(this, "Address must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()){
            Toast.makeText(this, "Email must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be longer than 5 chars", Toast.LENGTH_SHORT).show();
            return;
        }

        if (confirmPassword.length() < 6) {
            Toast.makeText(this, "Confirm password must be longer than 5 chars", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creating user account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE_AUTH", "createUserWithEmail:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            pushUserAccountData(task);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("FIREBASE_AUTH", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        progressDialog.dismiss();
//                        saverFirebaseData();
//                        Toast.makeText(RegisterActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void pushUserAccountData(@NonNull Task<AuthResult> task) {
        User user = new User(name, email, password, address);
        String id = task.getResult().getUser().getUid();
        firebaseDatabase.getReference()
                .child("Users")
                .child(id)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Saving account information.");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        // upload without image
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+firebaseAuth.getUid());
//        hashMap.put("fullname", ""+name);
        hashMap.put("email", ""+email);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("accountType", "User");
        hashMap.put("available", "true");
        hashMap.put("bonus", "0");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
