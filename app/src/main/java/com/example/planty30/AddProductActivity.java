package com.example.planty30;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddProductActivity extends AppCompatActivity {

    Button AddProduct;
    ImageButton backinaddproduct;
    ImageView imageinaddproduct;
    EditText nameinaddproduct,Priceinaddproduct,Descinaddproduct,Waterinaddproduct,Tempinaddproduct;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        AddProduct = findViewById(R.id.AddProduct);
        imageinaddproduct = findViewById(R.id.imageinaddproduct);
        nameinaddproduct = findViewById(R.id.nameinaddproduct);
        Priceinaddproduct = findViewById(R.id.Priceinaddproduct);
        Descinaddproduct = findViewById(R.id.Descinaddproduct);
        Waterinaddproduct = findViewById(R.id.Waterinaddproduct);
        Tempinaddproduct = findViewById(R.id.Tempinaddproduct);
        backinaddproduct = findViewById(R.id.backinaddproduct);

        backinaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddProductActivity.this,AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            imageinaddproduct.setImageURI(uri);
                        }else {
                            Toast.makeText(AddProductActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imageinaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void saveData(){

        String name = nameinaddproduct.getText().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Plant")
                .child(name);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();

                uploadData();

                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }

    public void uploadData()
    {

        String name = nameinaddproduct.getText().toString();
        String price = Priceinaddproduct.getText().toString();
        String desc = Descinaddproduct.getText().toString();
        String water = Waterinaddproduct.getText().toString();
        String temp = Tempinaddproduct.getText().toString();

        DataClass5 dataClass5 = new DataClass5(name,desc,price,temp,water,imageURL);

        FirebaseDatabase.getInstance().getReference("Plant").child(name)
                .setValue(dataClass5)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddProductActivity.this, "New Product Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddProductActivity.this,AdminDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
