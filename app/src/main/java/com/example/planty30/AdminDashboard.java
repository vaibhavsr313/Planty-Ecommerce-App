package com.example.planty30;

import static com.example.planty30.R.id.logoutinadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminDashboard extends AppCompatActivity {

    TextView userinadminhome;
    CardView logoutinadmin,Users,products,removeproducts;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        userinadminhome = findViewById(R.id.userinadminhome);
        logoutinadmin = findViewById(R.id.logoutinadmin);
        Users = findViewById(R.id.Users);
        products = findViewById(R.id.products);
        removeproducts = findViewById(R.id.removeproducts);

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,AddProductActivity.class);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
                finish();
            }
        });

        removeproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,Removeproducts.class);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
                finish();
            }
        });

        logoutinadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,Login.class);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                startActivity(intent);
                finish();
            }
        });

        Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this,UsersInAdmin.class);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
                finish();
            }
        });


    }
}