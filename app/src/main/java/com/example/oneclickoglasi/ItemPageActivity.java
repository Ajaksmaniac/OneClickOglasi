package com.example.oneclickoglasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import android.app.Activity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;



public class ItemPageActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = "ItemPage";
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();


            }
        });

        //Getting extras from previus intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("RECORDS").child(id);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final PostModel model = dataSnapshot.getValue(PostModel.class);
                user_id = model.user_id;

                TextView itemPriceLabel = (TextView) findViewById(R.id.itemPrice);
                TextView itemDescriptionLabel = (TextView) findViewById(R.id.itemDescription);

                final ImageView imageView = (ImageView) findViewById(R.id.itemImage);
                final ImageView phoneCall = (ImageView) findViewById(R.id.callPosterPhone);


                TextView itemNameLabel = (TextView) findViewById(R.id.itemName);

                itemNameLabel.setText(model.getName());


                itemPriceLabel.setText(model.getPrice() + " " + model.getValue());
                final TextView postedByLabel = (TextView) findViewById(R.id.postedByName);
                final TextView postedByEmail = (TextView) findViewById(R.id.postedByEmail);
                final TextView postedByPhone = (TextView) findViewById(R.id.postedByPhone);
                DatabaseReference ref2 = database.getReference("Users").child(model.user_id);
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final UserModel user = dataSnapshot.getValue(UserModel.class);
                        postedByLabel.setText("Name: " + user.getName());
                        postedByEmail.setText("Email: " + user.getEmail());
                        postedByPhone.setText("Phone: " + user.getPhone());
                        phoneCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("clicked");
                                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                                dialIntent.setData(Uri.parse("tel:" + user.getPhone()));
                                startActivity(dialIntent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ItemPageActivity.this,"Error while geting user info", Toast.LENGTH_LONG).show();
                    }
                };

                itemDescriptionLabel.setText(model.getDescription());
                ref2.addValueEventListener(userListener);

               try{

                   FirebaseStorage storage = FirebaseStorage.getInstance();

                   //System.out.println(dataSnapshot.getKey()+"_"+model.getName()+".jpg");
                   StorageReference imageRef = storage.getReference().child("images").child(dataSnapshot.getKey()+"_"+model.getName()+".jpg");

                   imageRef.getBytes(1024*1024)
                           .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                 @Override
                                 public void onSuccess(byte[] bytes) {
                                     Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                                     imageView.setImageBitmap(bitmap);
                                 }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {

                               }
                           });

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        ref.addValueEventListener(postListener);

    }

}
