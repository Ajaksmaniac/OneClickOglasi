package com.example.oneclickoglasi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.EditText;

public class settingsEmailFragment extends Fragment {
    Button emailSubmit ;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public settingsEmailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vi = inflater.inflate(R.layout.settings_email_fragment, container, false);
        emailSubmit = (Button)vi.findViewById(R.id.changeEmailSubmitBUtton);



        //Updating User email in firebase Auth and in Firebase database
        emailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((EditText) vi.findViewById(R.id.changeEmailInput)).length()<1){
                    ((EditText) vi.findViewById(R.id.changeEmailInput)).setBackgroundColor(Color.RED);
                    return;
                }else{
                    if(user != null){
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), ((EditText)vi.findViewById(R.id.changeEmailPasswordInput)).getText().toString());
                        final String newEmail = ((EditText) vi.findViewById(R.id.changeEmailInput)).getText().toString();
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("user", "User re-authenticated.");
                                        //Now change your email address \\
                                        //----------------Code for Changing Email Address----------\\
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.updateEmail(((EditText) vi.findViewById(R.id.changeEmailInput)).getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("user", "User email address updated.");
                                                            FirebaseDatabase  database = FirebaseDatabase.getInstance();
                                                            DatabaseReference ref = database.getReference();
                                                            ref.child("Users/"+user.getUid()+"/email").setValue(user.getEmail());
                                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    Log.d("User", "Success");
                                                                    System.out.println(dataSnapshot.getValue());
                                                                    mAuth.signOut();
                                                                    Intent homeIntent = new Intent(getContext(),HomeActivity.class);
                                                                    startActivity(homeIntent);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.d("User", databaseError.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                        //----------------------------------------------------------\\
                                    }
                                });



                    }
                }



            }
        });
        return vi;
    }
}
