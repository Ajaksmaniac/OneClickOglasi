package com.example.oneclickoglasi;

 class AuthModule {
    //Singletone type of class


   public boolean isLoged;
    private static AuthModule single_instance = null;
    public  UserModel currentUser;


    public boolean isLoged(){
        return this.isLoged;
    }
    private AuthModule()
    {
       isLoged = false;
       currentUser = null;
    }
    UserModel getCurrentUser(){
        if(!isLoged){

            return null;
        }
        return currentUser;
    }


    public void setCurrentUser(UserModel u){
       this.currentUser  = u;
       this.isLoged = true;
    }
    public void setLoggedTrue(){this.isLoged = true;};

    public static AuthModule getInstance()
    {
        if (single_instance == null)
            single_instance = new AuthModule();

        return single_instance;
    }

    public void logout(){
        this.isLoged = false;
        currentUser = null;
    }




}
