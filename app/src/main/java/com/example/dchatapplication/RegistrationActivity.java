package com.example.dchatapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dchatapplication.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    CircleImageView profile_image;
    EditText reg_name,reg_email,reg_pass,reg_cPass;
    TextView txt_signin,btn_SignUp;
    Uri imageUri;
    String imageURI;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        profile_image=findViewById(R.id.profile_image);
        reg_name=findViewById(R.id.reg_name);
        reg_email=findViewById(R.id.reg_email);
        reg_pass=findViewById(R.id.reg_pass);
        reg_cPass=findViewById(R.id.reg_cPass);
        txt_signin=findViewById(R.id.txt_signin);
        btn_SignUp=findViewById(R.id.btn_SignUp);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=reg_name.getText().toString();
                String email=reg_email.getText().toString();
                String cpass=reg_cPass.getText().toString();
                String pass=reg_pass.getText().toString();
                String status="Hey there I am using this app!";

                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(cpass)){
                    Toast.makeText(getApplicationContext(),"Enter the Vallide credentials",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else if(!pass.equals(cpass)){
                    Toast.makeText(getApplicationContext(),"Password does not matches",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else if(pass.length()<6){
                    Toast.makeText(getApplicationContext(),"Length of Password should be greater than 6 characters",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("uplod").child(auth.getUid());

                                if(imageUri!=null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(RegistrationActivity.this, "Error in Creating a New user", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String staus="Hey There I'm Using This Application";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/quickchatapp-a181b.appspot.com/o/profile_image.png?alt=media&token=a6748395-5c27-492d-b73b-d6859d4b5d15";
                                    Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                finish();
                                            }else {
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating a New user", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}