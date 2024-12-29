package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    Button b1;
    EditText e1,p1;
    ImageButton imgbut;
    TextView t1;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        imgbut = findViewById(R.id.imageButton3);//back button
        t1 = findViewById(R.id.t1);//don't have an account button
        e1 = findViewById(R.id.emailinme);//email
        p1 = findViewById(R.id.password);//password
        b1 = findViewById(R.id.signup);//signup
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(e1.getText());
                password = String.valueOf(p1.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Authentication Success.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(getApplicationContext(),Profile.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    finish();
                                } else {
                                    Toast.makeText(Signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        imgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }//back button
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity1();
            }//don't have an account button
        });
    }

    public void openActivity2()
    {
        Intent intent;
        intent = new Intent(this,logsign.class); // back button
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    public void openActivity1()
    {
        Intent intent;
        intent = new Intent(this,Login.class); //don't have an account button
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}