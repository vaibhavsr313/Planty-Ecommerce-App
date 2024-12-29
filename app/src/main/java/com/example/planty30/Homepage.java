package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    BottomNavigationView bn1;
    FirebaseAuth auth;
    TextView userinhome;
    DatabaseReference databaseReference;
    ImageView Profileinhome;
    RecyclerView recyclerView;
    List<DataClass5> datalist;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        bn1 = findViewById(R.id.bottomnav);
        userinhome = findViewById(R.id.userinhome);
        Profileinhome = findViewById(R.id.Profileinhome);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();



        recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));

        AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        datalist = new ArrayList<>();
        adapter = new MyAdapter(Homepage.this,datalist);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Plant");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for (DataSnapshot itemSnapshot:snapshot.getChildren()){
                    DataClass5 dataClass5 = itemSnapshot.getValue(DataClass5.class);
                    datalist.add(dataClass5);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("dataName").getValue().toString();
                String image = snapshot.child("dataImage").getValue().toString();

                userinhome.setText("Hello "+name+"!");
                Picasso.get().load(image).into(Profileinhome);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Profileinhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 =new Intent(getApplicationContext(),mepage.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent =new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        bn1.setSelectedItemId(R.id.Home);
        bn1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.Home:
                        bn1.setSelectedItemId(R.id.Home);
                        Intent i =new Intent(getApplicationContext(),Homepage.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        break;
                    case R.id.Cart:
                        Intent intent1 =new Intent(getApplicationContext(),Cartpage.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case R.id.Prodile:
                        Intent intent =new Intent(getApplicationContext(),mepage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        break;
                }
                return true;
            }
        });
    }
    public void searchList(String text){
        ArrayList<DataClass5> searchList = new ArrayList<>();
        for(DataClass5 dataClass5: datalist){
            if(dataClass5.getDataName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass5);
            }
        }
        adapter.searchDataList(searchList);

    }
}