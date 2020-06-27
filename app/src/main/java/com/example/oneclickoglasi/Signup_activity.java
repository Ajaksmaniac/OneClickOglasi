package com.example.oneclickoglasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_activity extends AppCompatActivity implements View.OnClickListener {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.singup_submit_button).setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();

            }
        });
        mAuth = FirebaseAuth.getInstance();
    }
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.singup_submit_button){

            String username = ((TextView) findViewById(R.id.signup_username)).getText().toString();
            final String email = ((TextView) findViewById(R.id.signup_email)).getText().toString();
            final String name = ((TextView) findViewById(R.id.signup_name)).getText().toString();
            final String phone = ((TextView) findViewById(R.id.signup_phone)).getText().toString();
            String password = ((TextView) findViewById(R.id.signup_password)).getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                UserModel u = new UserModel(
                                        name,
                                        email,
                                        phone
                                );
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(u)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Signup_activity.this,"User registrated successfuly", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(Signup_activity.this,"failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });



                                Intent homeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(homeActivity);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }
    }
}
