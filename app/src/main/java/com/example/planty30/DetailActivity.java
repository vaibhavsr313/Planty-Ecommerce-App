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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ImageView photoindetail;
    Button addcartindetail;
    ImageButton backindetail,cartButtoninetail;
    EditText quantityindetail;
    TextView nameindetail,descindetail,tempindetail,waterindetail,Priceindetail;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        photoindetail = findViewById(R.id.photoindetail);
        backindetail = findViewById(R.id.backindetail);
        cartButtoninetail = findViewById(R.id.cartButtoninetail);
        nameindetail = findViewById(R.id.nameindetail);
        descindetail = findViewById(R.id.descindetail);
        tempindetail = findViewById(R.id.tempindetail);
        waterindetail = findViewById(R.id.waterindetail);
        Priceindetail = findViewById(R.id.Priceindetail);
        addcartindetail = findViewById(R.id.addcartindetail);
        quantityindetail = findViewById(R.id.quantityindetail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            nameindetail.setText(bundle.getString("Name"));
            Priceindetail.setText(bundle.getString("Price"));
            tempindetail.setText(bundle.getString("Temp"));
            waterindetail.setText(bundle.getString("Water"));
            descindetail.setText(bundle.getString("Desc"));

            Glide.with(this).load(bundle.getString("Image")).into(photoindetail);
        }

        backindetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        cartButtoninetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Cartpage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        addcartindetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                addingToCartList();
            }
        });
    }

    private void addingToCartList()
    {
        String saveCurrentTime,saveCurrenDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrenDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        final HashMap<String, Object> cartMap = new HashMap<>();
        final String named = nameindetail.getText().toString();
        cartMap.put("pName", named);
        cartMap.put("pQuan", quantityindetail.getText().toString());
        cartMap.put("pPrice", Priceindetail.getText().toString());
        cartMap.put("pDate", saveCurrenDate);
        cartMap.put("pTime", saveCurrentTime);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        String quantityString = quantityindetail.getText().toString();

        if(TextUtils.isEmpty(quantityString))
        {
            Toast.makeText(this, "Please Enter Quantity", Toast.LENGTH_LONG).show();

        } else if (Integer.parseInt(quantityString) > 10) {

            Toast.makeText(this, "Please Enter Quantity Less Than 10", Toast.LENGTH_LONG).show();

        } else {
            cartListRef.child("UserView").child(userId)
                    .child("Products").child(named)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                cartListRef.child("AdminView").child(userId)
                                        .child("Products").child(named)
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DetailActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

        }
    }
}
