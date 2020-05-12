package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Helper.ClassesList;
import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.RoomList;
import com.example.finalapp.Helper.Routes;
import com.example.finalapp.Models.Instructor;
import com.example.finalapp.Models.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InstructorRoomActivity extends AppCompatActivity {
    public JSONArray Adminrooms;
    public String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_rooms);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        //System.out.println("in Instructor Room Activity ********* "+token);

        ArrayList<Room> list = new ArrayList<Room>();
        String url=Routes.show_all_admin_classes+"?token="+token;
        try {
            Adminrooms=Kernal.sendGetRequest(url);
            if (Adminrooms.length()>0){
                for (int i=0;i<Adminrooms.length();i++){
                    try {
                        int id=Adminrooms.getJSONObject(i).getInt("id");
                        String name=Adminrooms.getJSONObject(i).getString("name");
                        String code=Adminrooms.getJSONObject(i).getString("code");
                        list.add( new Room(id,name,code));
                    }catch (Exception e){
                        System.out.println("++++++++++"+e.getMessage()+"+++++++++++++");
                    }
                }
            }
        }catch (Exception e){
            System.out.println("++++++++++"+e.getMessage()+"+++++++++++++");
        }


        //instantiate custom adapter
        RoomList adapter = new RoomList(list, this,token);


        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.list_of_classes);
        lView.setAdapter(adapter);
    }
    public void add_New_Class(View view){
        Intent intent=new Intent(InstructorRoomActivity.this,AddClassActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }


}
