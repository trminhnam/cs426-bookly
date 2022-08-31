package com.example.bookly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText resetEmailEt, currentPasswordEt;
    private Button resetBtn;
    private TextView backToLoginTv;
    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmailEt = (TextInputEditText) findViewById(R.id.resetEmailEt);
        currentPasswordEt = (TextInputEditText) findViewById(R.id.currentPasswordEt);
        backToLoginTv = (TextView) findViewById(R.id.backLoginTv);
        
        resetBtn = (Button) findViewById(R.id.resetBtn);

        progressBar = (ProgressBar) findViewById(R.id.resetProgress);

        auth = FirebaseAuth.getInstance();

        backToLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
        
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = resetEmailEt.getText().toString().trim();
        String password = currentPasswordEt.getText().toString().trim();

        if (email.isEmpty()){
            resetEmailEt.setError("Email is required!");
            resetEmailEt.requestFocus();
            return;
        }

        if (password.isEmpty()){
            currentPasswordEt.setError("Password is required!");
            currentPasswordEt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetEmailEt.setError("Please provide valid email");
            resetEmailEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Try again! Something wrong happened", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}