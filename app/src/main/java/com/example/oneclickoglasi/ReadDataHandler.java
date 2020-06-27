package com.example.oneclickoglasi;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class ReadDataHandler extends Handler {
    private String text;
    private List<PostModel> list;
    private UserModel user;
    private boolean itemPosted;


    public PostModel getPostModel() {
        return postModel;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }

    private PostModel postModel;

    public boolean isUserRegisted() {
        return userRegisted;
    }

    public void setUserRegisted(boolean userRegisted) {
        this.userRegisted = userRegisted;
    }

    private boolean userRegisted;


    public boolean isItemPosted() {
        return itemPosted;
    }

    public void setItemPosted(boolean itemPosted) {
        this.itemPosted = itemPosted;
    }

    public List<PostModel> getList(){
        return this.list;
    }
    public void setList(List<PostModel> list){
        this.list = list;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }


    public UserModel getUser(){
        return this.user;
    }
    public void setUser(UserModel user){
        this.user = user;
    }




}
