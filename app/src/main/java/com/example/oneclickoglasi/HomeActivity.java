package com.example.oneclickoglasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar    toolbar;
    private DatabaseReference mDatabase;

    private static final String TAG = "EmailPassword";
    Menu menu;
    private FirebaseAuth mAuth;

    AuthModule am = AuthModule.getInstance();

    int LAUNCH_LOGIN_ACTIVITY = 1;
    int LAUNCH_SIGNUP_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //DRAWER
        //getPosts(new PostModel());
        initComponents();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


    }
    @SuppressLint("ResourceType")
    private void initComponents(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView   = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //ENDDRAWER
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        if (user != null) {
            String uid = user.getUid();

            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
            //((TextView)findViewById(R.id.nav_user_email)).setText(user.getEmail());
            //((TextView) findViewById(R.id.navigation_header_user_email)).setText(user.getEmail());
           // navigationView.getMenu().findItem(R.id.nav_profile).setTitle(user.getEmail());
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            //((TextView) findViewById(R.string.user_email)).setText(user.getEmail());

            View headerLayout = navigationView.inflateHeaderView(R.layout.header);
            ((TextView)headerLayout.findViewById(R.id.navigation_header_user_email)).setText(user.getEmail());
           // ((TextView)headerLayout.findViewById(R.id.navigation_header_user_username)).setText(user.getDisplayName());
           // System.out.println();
            navigationView.getMenu().findItem(R.id.nav_settings).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_SignUp).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_addPost).setVisible(true);


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.

        }else{
            View headerLayout = navigationView.inflateHeaderView(R.layout.header);
            ((TextView)headerLayout.findViewById(R.id.navigation_header_user_email)).setText("Please Sign Up to experience aditional features of the 1ClickOglasi");
            navigationView.getMenu().findItem(R.id.nav_settings).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
           // navigationView.findViewById(R.id.nav_group_profile).setBackgroundColor(Color.WHITE);
            //navigationView.findViewById(R.id.nav_group_communicate).setBackgroundColor(Color.WHITE);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_SignUp).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_addPost).setVisible(false);
        }
        //MENU ITEMS



        //Get Posts
        getPosts(new PostModel());


    }

   protected void getPosts(final PostModel pm){

       TextView labelJson = findViewById(R.id.nameLabel);


       final FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference("RECORDS" );

       ValueEventListener postListener = new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               // Get Post object and use the values to update the UI
               LinearLayout mainScrollView = (LinearLayout) findViewById(R.id.mainScrollView);
               mainScrollView.removeAllViews();
               LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              // ArrayList<PostModel> posts = dataSnapshot.getValue( ArrayList.class);

                        for (final DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            final PostModel post = postSnapshot.getValue(PostModel.class);
                            System.out.println(postSnapshot.toString());
                            final RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.post_view,null);

                            final String itemName =  post.getName();
                            ((TextView) item.findViewById(R.id.nameLabel)).setText(post.getName() );

                            /*final String itemDescription = post.getDescription();
                            ((TextView) item.findViewById(R.id.postedBylabel)).setText(post.user_id);*/

                            final String itemPrice = post.getPrice() + " " + post.getValue();
                            ((TextView) item.findViewById(R.id.priceLabel)).setText(  post.getPrice() + " " +  post.getValue());

                            ImageView imageView = (ImageView) findViewById(R.id.postView_image);


                            //System.out.println(dataSnapshot.getKey()+"_"+model.getName()+".jpg");



                                FirebaseStorage storage = FirebaseStorage.getInstance();

                                //System.out.println(dataSnapshot.getKey()+"_"+itemName+".jpg");
                                StorageReference imageRef = storage.getReference().child("images").child(postSnapshot.getKey()+"_"+itemName+".jpg");
                                imageRef.getBytes(1024*1024)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                             Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                                                ((ImageView) item.findViewById(R.id.postView_image)).setImageBitmap(bitmap);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });



                            mainScrollView.addView(item);
                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent ItemPageActivity = new Intent(getApplicationContext(), ItemPageActivity.class);

                                    ItemPageActivity.putExtra("id",postSnapshot.getKey());
                                    startActivity(ItemPageActivity);
                                }
                            });
                    }
                       //System.out.println(post.getDescription());




           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               // Getting Post failed, log a message
               Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
               // ...
           }
       };
       ref.addValueEventListener(postListener);








    }
    //Closes the drawer after pressing back on the device
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
    //

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //Starts a login activity, and returns am.isLogged true if succefull
        if(item.getItemId() == R.id.nav_login){

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);


        }
        if(item.getItemId() == R.id.nav_SignUp){

            Intent signupIntent = new Intent(this, Signup_activity.class);
            startActivity(signupIntent);


        }
        if(item.getItemId() == R.id.nav_addPost){

            Intent addItemActivity = new Intent(this, AddItemActivity.class);
            startActivity(addItemActivity);


        }
        if(item.getItemId() == R.id.nav_logout){

            mAuth.signOut();
            finish();
            startActivity(getIntent());


        }
        if(item.getItemId() == R.id.nav_settings){

            Intent settingsActivity = new Intent(this, settings.class);
            startActivity(settingsActivity);

        }
        return false;
    }

    //Returns LoginActivity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (requestCode == LAUNCH_LOGIN_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                am.isLoged = data.getBooleanExtra("result",am.isLoged);

                //Hide and show some of the menu items after the succesfull login
               /* navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_profile).setTitle(am.getCurrentUser().getUsername());
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_SignUp).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_addPost).setVisible(true);*/
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                navigationView.findViewById(R.id.nav_group_profile);
                navigationView.findViewById(R.id.nav_group_communicate);
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_SignUp).setVisible(true);


            }

        }
       initComponents();
    }




}

