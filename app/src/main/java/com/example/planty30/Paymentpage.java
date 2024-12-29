package com.example.planty30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Paymentpage extends AppCompatActivity {

    TextView addressinpayment,subtotal,totalpriceinpayment,nameinpayment,phoneinpayment;
    private String totalAmount = "";
    private int price = 0;
    DatabaseReference databaseReference;
    Button buyinpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentpage);
        addressinpayment = findViewById(R.id.addressinpayment);
        phoneinpayment = findViewById(R.id.phoneinpayment);
        nameinpayment = findViewById(R.id.nameinpayment);
        subtotal = findViewById(R.id.subtotal);
        buyinpayment = findViewById(R.id.buyinpayment);
        totalpriceinpayment = findViewById(R.id.totalpriceinpayment);

        totalAmount = getIntent().getStringExtra("TotalP");
        int new1 = Integer.parseInt(totalAmount);
        subtotal.setText(totalAmount);
        price = (5 * new1) / 100;
        int new2 = price+new1;
        totalpriceinpayment.setText(String.valueOf(new2));

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.child("dataAddress").getValue().toString();
                String name = snapshot.child("dataName").getValue().toString();
                String phone = snapshot.child("dataNo").getValue().toString();

                addressinpayment.setText(address);
                nameinpayment.setText(name);
                phoneinpayment.setText(phone);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buyinpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrder();
            }
        });

    }
    private void ConfirmOrder()
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        final String saveCurrentTime,saveCurrenDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrenDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(userId);

        HashMap<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("tAmount", totalpriceinpayment.getText().toString());
        ordersMap.put("uName",nameinpayment.getText().toString());
        ordersMap.put("uPhone", phoneinpayment.getText().toString());
        ordersMap.put("uAddress", addressinpayment.getText().toString());
        ordersMap.put("pDate", saveCurrenDate);
        ordersMap.put("pTime", saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("CartList")
                            .child("UserView")
                            .child(userId)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Paymentpage.this, "Your Order Is Placed successfully it will reach to you in 2 Business Days", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Paymentpage.this,Homepage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            });
                    FirebaseDatabase.getInstance().getReference().child("CartList")
                            .child("AdminView")
                            .child(userId)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {

                                    }
                                }

                            });
                }
            }
        });


    }
}