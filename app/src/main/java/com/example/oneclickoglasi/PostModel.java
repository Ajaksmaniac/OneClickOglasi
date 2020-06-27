package com.example.oneclickoglasi;

import android.net.Uri;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class PostModel {
    public String id;
    public String name;
    public int price;
    public String user_id;




    public String image;




    public String description;
    public String value;





    //Basic constructor
    public PostModel( String name, int price, String description, String value,String username) throws FirebaseAuthException {
       // this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.value = value;

        this.user_id = username;
      ;


    }


    public PostModel() {

    }

    //Gets PostModel list


    public String getPostImage() {
        return image;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getId() {
        return id;
    }
    public void setPostImage(String postImage) {
        this.image = postImage;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

@Exclude
public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("id", this.id);
    result.put("name", this.name);
    result.put("price", this.price);
    result.put("description", this.description);
    result.put("value", this.value);
    result.put("image", this.image);
    result.put("user_id",this.user_id);

    return result;
}


}
