package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UsersInAdmin extends AppCompatActivity {

    ImageButton backinausersadmin;
    public TextView nameadmininusers,phoneadmininusers,addressadmininusers,emailadmininusers;
    public ImageView photoadmininusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_admin);
        backinausersadmin = findViewById(R.id.backinausersadmin);

        nameadmininusers = findViewById(R.id.nameadmininusers);
        phoneadmininusers = findViewById(R.id.phoneadmininusers);
        addressadmininusers = findViewById(R.id.addressadmininusers);
        emailadmininusers = findViewById(R.id.emailadmininusers);
        photoadmininusers = findViewById(R.id.photoadmininusers);

        backinausersadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UsersInAdmin.this,AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("Users").child("pXdScRXKbMSHS0rchiLZ6EjbX243");

        userListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1 = snapshot.child("dataName").getValue().toString();
                String add1 = snapshot.child("dataAddress").getValue().toString();
                String no1 = snapshot.child("dataNo").getValue().toString();
                String img1 = snapshot.child("dataImage").getValue().toString();

                nameadmininusers.setText(name1);
                addressadmininusers.setText(add1);
                phoneadmininusers.setText(no1);
                Picasso.get().load(img1).into(photoadmininusers);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}