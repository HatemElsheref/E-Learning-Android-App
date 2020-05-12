package com.example.finalapp.Models;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;

import org.json.JSONObject;

public class Auth {
    private String USER_NAME=null;
    private String USER_EMAIL=null;
    private String USER_PASSWORD=null;
    private String USER_TOKEN=null;
    private String USER_ROLE=null;

    public Auth(String USER_NAME, String USER_EMAIL, String USER_PASSWORD, String USER_ROLE) {
        this.USER_NAME = USER_NAME;
        this.USER_EMAIL = USER_EMAIL;
        this.USER_PASSWORD = USER_PASSWORD;
        this.USER_ROLE = USER_ROLE;
    }

    public Auth(String USER_EMAIL, String USER_PASSWORD) {
        this.USER_EMAIL = USER_EMAIL;
        this.USER_PASSWORD = USER_PASSWORD;
    }

    public String getUSER_TOKEN() {
        return USER_TOKEN;
    }

    public void setUSER_TOKEN(String USER_TOKEN) {
        this.USER_TOKEN = USER_TOKEN;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_EMAIL() {
        return USER_EMAIL;
    }

    public void setUSER_EMAIL(String USER_EMAIL) {
        this.USER_EMAIL = USER_EMAIL;
    }

    public String getUSER_PASSWORD() {
        return USER_PASSWORD;
    }

    public void setUSER_PASSWORD(String USER_PASSWORD) {
        this.USER_PASSWORD = USER_PASSWORD;
    }

    public String getUSER_ROLE() {
        return USER_ROLE;
    }

    public void setUSER_ROLE(String USER_ROLE) {
        this.USER_ROLE = USER_ROLE;
    }
    public JSONObject Login(){
        JSONObject jsonObject=new JSONObject();
       try {
           jsonObject.put("email", this.getUSER_EMAIL());
           jsonObject.put("password",this.getUSER_PASSWORD());
           JSONObject userinfo=Kernal.sendPostRequest(Routes.login, jsonObject, null);
           return userinfo;
       }catch (Exception e){
           return  null;
       }
    }
    public JSONObject Register(){
        JSONObject jsonObject=new JSONObject();
       try {
           jsonObject.put("name", this.getUSER_NAME());
           jsonObject.put("email", this.getUSER_EMAIL());
           jsonObject.put("password",this.getUSER_PASSWORD());
           jsonObject.put("role",this.getUSER_ROLE());
           JSONObject userinfo=Kernal.sendPostRequest(Routes.register, jsonObject, null);
           return userinfo;
       }catch (Exception e){
           System.out.println(e.getMessage());
           return  null;
       }
    }
    public JSONObject Me(){
       try {
          String URL=Routes.me+"?token="+this.getUSER_TOKEN();
           JSONObject userinfo=Kernal.sendPostRequest(URL, null, null);
           return userinfo;
       }catch (Exception e){
           return  null;
       }
    }
    public JSONObject Refresh(){
       try {
          String URL=Routes.refresh+"?token="+this.getUSER_TOKEN();
           JSONObject userinfo=Kernal.sendPostRequest(URL, null, null);
           return userinfo;
       }catch (Exception e){
           return  null;
       }
    }
    public boolean Logout(){
       try {
          String URL=Routes.logout+"?token="+this.getUSER_TOKEN();
           JSONObject response=Kernal.sendPostRequest(URL, null, null);
           if (response!=null){
               return true;
           }
           return  false;
       }catch (Exception e){
           return  false;
       }
    }

}
