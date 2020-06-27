package com.example.oneclickoglasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private Bitmap mImageUri = null;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2 ;
    Toolbar toolbar;
    ImageButton selectItemButton;
    ImageButton cameraCaptureButton;
    int CHOOSE_FILE_FROM_LOCAL_STOARE = 1;
    ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;


    private static final String TAG = "addPostDatabase";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    AuthModule am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);



        //On arrow press back
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
               // setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();


            }
        });


        ImageButton selectItemButton = (ImageButton) findViewById(R.id.add_item_from_storage_button);
        selectItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        ImageButton cameraCaptureButton = (ImageButton) findViewById(R.id.add_item_from_camera_button);
        cameraCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        String[] arraySpinner = new String[] {
                "RSD", "EUR", "YEN","USD"
        };

        Spinner s = (Spinner) findViewById(R.id.value_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        final Button addItemButton = (Button) findViewById(R.id.post_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addPost();
                } catch (FirebaseAuthException | UnsupportedEncodingException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void uploadFile(String ref){
        if(mImageUri != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImageUri.compress(Bitmap.CompressFormat.JPEG,100,baos);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            //System.out.println(dataSnapshot.getKey()+"_"+model.getName()+".jpg");
            final StorageReference reference = FirebaseStorage.getInstance().getReference().child("images").child(ref);

            progressDialog.show();

            reference.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(reference);
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,"onfailure: ",e.getCause());
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setTitle(progress + " % Uploaded");
                        }
                    });
            progressDialog.dismiss();
        }else{

        }

    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: "+ uri);
            }
        });
    }
    private void addPost() throws FirebaseAuthException, UnsupportedEncodingException, FileNotFoundException {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference ref = database.getReference("server/saving-data/fireblog");

        DatabaseReference ref = database.getReference();

        BitmapDrawable drawable = (BitmapDrawable) ((ImageView)findViewById(R.id.selectedImage_image)).getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        final byte[] blobBytes = bos.toByteArray();
        //String s = new String(blobBytes,"UTF-8");
        Drawable Image = ((ImageView)findViewById(R.id.selectedImage_image)).getDrawable();
         final String name =((EditText) findViewById(R.id.post_name_input)).getText().toString();
        int price = Integer.parseInt(((EditText)findViewById(R.id.item_price_input)).getText().toString());
        String  description =((EditText) findViewById(R.id.item_description_input)).getText().toString();
        String  value =((Spinner) findViewById(R.id.value_spinner)).getSelectedItem().toString()
                ;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String key = UUID.randomUUID().toString();
        PostModel post = new PostModel(name,price,description,value,user.getUid());
        //DatabaseReference postsRef = database.child("RECORDS");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference keyRef = rootRef.child("RECORDS").push();
        String key = keyRef.getKey();
        keyRef.setValue(post);



        uploadFile(key+"_"+name+".jpg");
            finish();
    }

    //Open camera Intent
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
     /*   if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(pictureIntent, CAMERA_REQUEST);
        }*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(pictureIntent, CAMERA_REQUEST);}
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            startActivityForResult(pictureIntent, CAMERA_REQUEST);
        }

        /*if (pictureIntent.resolveActivity(getPackageManager()) != null) {

        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CHOOSE_FILE_FROM_LOCAL_STOARE){
            ImageView imageView = (ImageView) findViewById(R.id.selectedImage_image);
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            Drawable drawable =  imageView.getDrawable();
           Bitmap  bm = ((BitmapDrawable) drawable).getBitmap();
            imageView.setImageDrawable(drawable);

            mImageUri = bm;
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if (data != null && data.getExtras() != null) {
                ImageView imageView = (ImageView) findViewById(R.id.selectedImage_image);
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);
                mImageUri = imageBitmap;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);


    }




    //Chooses File from internal storage
    private void showFileChooser() {
        boolean isKitKat = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            isKitKat = true;
            startActivityForResult(Intent.createChooser(intent, "Select file"), CHOOSE_FILE_FROM_LOCAL_STOARE);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), CHOOSE_FILE_FROM_LOCAL_STOARE);
        }
    }




}
