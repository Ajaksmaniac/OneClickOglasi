package com.example.oneclickoglasi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public String email;
    public String password;
    Toolbar toolbar;
    private FirebaseAuth mAuth;


   public AuthModule am = AuthModule.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button LoginButton = (Button) findViewById(R.id.login_button);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED,returnIntent);
            finish();

        }
    });

    findViewById(R.id.login_button).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    boolean checkFields(){
        //RETARDED LOGIC
        EditText emailInput = (EditText) findViewById(R.id.email_input);
        EditText passwordInput = (EditText) findViewById(R.id.password_input);
            Button loginButton = (Button) findViewById(R.id.login_button);

        if(emailInput.length() > 0 && passwordInput.length() > 0){
            return true;
        }
        if(emailInput.length() < 1 ){
            emailInput.setBackgroundColor(Color.RED);
        }else{
            emailInput.setBackgroundColor(Color.GREEN);
        }
        if(passwordInput.length() < 1 ){
            passwordInput.setBackgroundColor(Color.RED);
        }else{
            passwordInput.setBackgroundColor(Color.GREEN);

        }
        return false;
    }

    private static final String TAG = "EmailPassword";
    @Override
    public void onClick(View v) {
        String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        if(v.getId() == R.id.login_button){
            System.out.println("Click");
           if(!checkFields()) return;

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent homeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(homeActivity);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                // ...
                            }

                            // ...
                        }
                    });




//TAKING A BREAK



        }

    }
}

