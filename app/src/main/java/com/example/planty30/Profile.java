package
        com.example.planty30;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    ImageView pp;//profile pic
    public EditText Name,Address,Phone;//userdetails
    Button Continue;
    public Uri uri;
    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pp = findViewById(R.id.Profileinme);
        Name = findViewById(R.id.Name);
        Address = findViewById(R.id.Address);
        Phone = findViewById(R.id.phone);
        Continue = findViewById(R.id.Continue);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            pp.setImageURI(uri);
                        }else{
                            Toast.makeText(Profile.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });



        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    saveData();
            }
        });
    }

    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User Profiles")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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

        String name = Name.getText().toString();
        String address = Address.getText().toString();
        String phone = Phone.getText().toString();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userId=user.getUid();

        DataClass dataClass = new DataClass(name, address, phone, imageUrl,userId);

        FirebaseDatabase.getInstance().getReference("Users").child(userId)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Profile.this, "Saved....", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),Homepage.class);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public   void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}