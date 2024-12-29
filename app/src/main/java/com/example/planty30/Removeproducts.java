package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Removeproducts extends AppCompatActivity {
    ImageButton backinaremoveproducts;
    TextView nameadmininorders,phoneadmininorders,addressadmininorders,totalpriceadmininorders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeproducts);

        backinaremoveproducts = findViewById(R.id.backinaremoveproducts);

        nameadmininorders = findViewById(R.id.nameadmininorders);
        phoneadmininorders = findViewById(R.id.phoneadmininorders);
        addressadmininorders = findViewById(R.id.addressadmininorders);
        totalpriceadmininorders = findViewById(R.id.totalpriceadmininorders);

        backinaremoveproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Removeproducts.this,AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });



        final DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("pXdScRXKbMSHS0rchiLZ6EjbX243");

        userListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name2 = snapshot.child("uName").getValue().toString();
                String no2 = snapshot.child("uPhone").getValue().toString();
                String ad2 = snapshot.child("uAddress").getValue().toString();
                String tam2 = snapshot.child("tAmount").getValue().toString();

                nameadmininorders.setText(name2);
                phoneadmininorders.setText(no2);
                addressadmininorders.setText(ad2);
                totalpriceadmininorders.setText("₹"+tam2);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}