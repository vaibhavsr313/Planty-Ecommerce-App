package com.example.planty30;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class editprofilepage extends AppCompatActivity {

    ImageButton i2;
    ImageView Profileineditprofile;
    EditText Nameinedit,Addressinedit,phoneinedit;
    Button editinedit;
    public Uri uri;
    String imageUrl;
    DatabaseReference databaseReference;
    private final int GALLERY_REQ_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofilepage);
        i2 = findViewById(R.id.backineditedit);
        Profileineditprofile = findViewById(R.id.Profileineditprofile);
        Nameinedit = findViewById(R.id.Nameinedit);
        Addressinedit = findViewById(R.id.Addressinedit);
        phoneinedit = findViewById(R.id.phoneinedit);
        editinedit = findViewById(R.id.editinedit);

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),mepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            Profileineditprofile.setImageURI(uri);
                        }else{
                            Toast.makeText(editprofilepage.this, "Image not changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Profileineditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        editinedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User Profiles")
                .child(uri.getLastPathSegment());





        AlertDialog.Builder builder = new AlertDialog.Builder(editprofilepage.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
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

    public void uploadData(){

        String name = Nameinedit.getText().toString();
        String address = Addressinedit.getText().toString();
        String phone = phoneinedit.getText().toString();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userId=user.getUid();

        DataClass dataClass = new DataClass(name, address, phone, imageUrl,userId);

        if(TextUtils.isEmpty(Nameinedit.getText().toString()) && TextUtils.isEmpty(Addressinedit.getText().toString()) && TextUtils.isEmpty(phoneinedit.getText().toString()))
        {
            Toast.makeText(this, "Please fill all your Details", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(editprofilepage.this, "Edited....", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), mepage.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editprofilepage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}