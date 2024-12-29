package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cartpage extends AppCompatActivity {

    BottomNavigationView bn1;
    ImageView bag_1;
    ImageButton img1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button buyincart,buy_2;
    TextView Totalpriceincart,bag,subTtextview;
    ImageView buyimg,totalimg;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartpage);
        bn1 = findViewById(R.id.bottomnavincart);
        buyincart = findViewById(R.id.buyincart);
        bag_1 = findViewById(R.id.bag_1);
        buy_2 = findViewById(R.id.buy_2);
        buyimg = findViewById(R.id.buyimg);
        subTtextview = findViewById(R.id.subTtextview);
        totalimg = findViewById(R.id.totalimg);
        bag = findViewById(R.id.bag);
        Totalpriceincart = findViewById(R.id.Totalpriceincart);
        img1 = findViewById(R.id.backincart);
        recyclerView = findViewById(R.id.rcincart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        buy_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cartpage.this,Homepage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        bn1.setSelectedItemId(R.id.Cart);

        bn1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.Home:
                        Intent intent =new Intent(getApplicationContext(),Homepage.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        break;
                    case R.id.Cart:
                        bn1.setSelectedItemId(R.id.Cart);
                        Intent intent2 =new Intent(getApplicationContext(),Cart.class);
                        startActivity(intent2);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        break;
                    case R.id.Prodile:
                        Intent intent1 =new Intent(getApplicationContext(),mepage.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }
                return true;
            }
        });
        buyincart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cartpage.this,Paymentpage.class);
                intent.putExtra("TotalP",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart()
    {
           super.onStart();

           final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

           cartListRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists())
                   {
                       bag.setVisibility(View.INVISIBLE);
                       bag_1.setVisibility(View.INVISIBLE);
                       buy_2.setVisibility(View.INVISIBLE);

                       buyincart.setVisibility(View.VISIBLE);
                       buyimg.setVisibility(View.VISIBLE);
                       Totalpriceincart.setVisibility(View.VISIBLE);
                       totalimg.setVisibility(View.VISIBLE);
                       subTtextview.setVisibility(View.VISIBLE);
                   }
                   else{
                       bag.setVisibility(View.VISIBLE);
                       bag_1.setVisibility(View.VISIBLE);
                       buy_2.setVisibility(View.VISIBLE);

                       buyincart.setVisibility(View.INVISIBLE);
                       buyimg.setVisibility(View.INVISIBLE);
                       Totalpriceincart.setVisibility(View.INVISIBLE);
                       totalimg.setVisibility(View.INVISIBLE);
                       subTtextview.setVisibility(View.INVISIBLE);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("UserView")
                                .child(userId).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText(model.getpQuan());
                holder.txtProductName.setText(model.getpName());
                holder.txtProductPrice.setText("₹"+model.getpPrice());

                int oneTypeProductTPrice = ((Integer.parseInt(model.getpPrice()))) * Integer.parseInt(model.getpQuan());
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;
                Integer.toString(overTotalPrice);
                Totalpriceincart.setText("₹"+ overTotalPrice);

                holder.removebuttonincart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartListRef.child("UserView")
                                .child(userId).child("Products")
                                .child(model.getpName())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(Cartpage.this, "Item Removed.", Toast.LENGTH_SHORT).show();
                                            int oneTypeProductTPrice = ((Integer.parseInt(model.getpPrice()))) * Integer.parseInt(model.getpQuan());
                                            overTotalPrice = overTotalPrice - oneTypeProductTPrice;
                                            Integer.toString(overTotalPrice);
                                            Totalpriceincart.setText("₹"+ overTotalPrice);
                                        }
                                    }
                                });
                            cartListRef.child("AdminView")
                                .child(userId).child("Products")
                                .child(model.getpName())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {

                                        }
                                    }
                                });
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return  holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}