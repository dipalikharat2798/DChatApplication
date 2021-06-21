package com.example.dchatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    TextView txt_signup,signin_btn;
    EditText login_email,login_password;
    FirebaseAuth auth;
    String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
            + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_signup=findViewById(R.id.txt_signup);
        signin_btn=findViewById(R.id.signin_btn);
        login_email=findViewById(R.id.login_email);
        login_password=findViewById(R.id.login_password);

        auth=FirebaseAuth.getInstance();
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=login_email.getText().toString();
                String pass =login_password.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Enter the vallide details",Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    login_email.setError("invalid password");
                    Toast.makeText(getApplicationContext(),"Invalide password",Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"Error in login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}