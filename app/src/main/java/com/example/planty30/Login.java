package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    ImageButton imgbut;
    Button b1,AdminLogin;
    EditText e1,p1,passwordadmin,adminphone;
    TextView a1,iamuser,iamadmin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextInputLayout textpass;
    private String parentDbName = "Users";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        a1 = findViewById(R.id.account);//don't have an account button
        iamadmin = findViewById(R.id.iamadmin);//Iamadmin
        iamuser = findViewById(R.id.iamuser);//IamUser
        imgbut = findViewById(R.id.imageButton3);//back button
        e1 = findViewById(R.id.emailinlogin);//email
        p1 = findViewById(R.id.password);//password
        b1 = findViewById(R.id.login);//login
        AdminLogin = findViewById(R.id.AdminLogin);//Adminlogin
        adminphone = findViewById(R.id.adminphone);//emailadmin
        passwordadmin = findViewById(R.id.passwordadmin);//passadmin
        textpass = findViewById(R.id.textpass);//TextInput
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();


        iamadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                b1.setText("Admin Login");
                iamadmin.setVisibility(View.INVISIBLE);
                iamuser.setVisibility(View.VISIBLE);
                AdminLogin.setVisibility(View.VISIBLE);
                b1.setVisibility(View.INVISIBLE);
                adminphone.setVisibility(View.VISIBLE);
                textpass.setVisibility(View.VISIBLE);
                e1.setVisibility(View.INVISIBLE);
                p1.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        AdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name, password;
                name = adminphone.getText().toString();
                password = passwordadmin.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(Login.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (snapshot.child("Admins").child(name).exists())
                        {

                            Users usersData = snapshot.child("Admins").child(name).getValue(Users.class);

                            if (usersData.getName().equals(name))
                            {
                                if (usersData.getPassword().equals(password))
                                {
                                    Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this,AdminDashboard.class);
                                    intent.putExtra("name1",name);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }
                        else {
                            Toast.makeText(Login.this, "Wrong Phone No. or password",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        iamuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setText("Login");
                iamadmin.setVisibility(View.VISIBLE);
                iamuser.setVisibility(View.INVISIBLE);
                AdminLogin.setVisibility(View.INVISIBLE);
                b1.setVisibility(View.VISIBLE);
                adminphone.setVisibility(View.INVISIBLE);
                textpass.setVisibility(View.INVISIBLE);
                e1.setVisibility(View.VISIBLE);
                p1.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });

        imgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            } //back button
        });

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity1();
            }//don't have an account button
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(e1.getText());
                password = String.valueOf(p1.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(getApplicationContext(),Homepage.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Wrong Email or password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    public void openActivity2()
    {
        Intent intent;
        intent = new Intent(this,logsign.class); //back button
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    public void openActivity1()
    {
        Intent intent;
        intent = new Intent(this,Signup.class); //don't have an account button
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}