package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.RoomList;
import com.example.finalapp.Helper.Routes;
import com.example.finalapp.Helper.StudentRoomList;
import com.example.finalapp.Models.ClassActivity;
import com.example.finalapp.Models.Room;

import org.json.JSONArray;

import java.util.ArrayList;

public class StudentRoomActivity extends AppCompatActivity {
    public JSONArray studentRooms;
    public String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_room);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        //System.out.println("in Instructor Room Activity ********* "+token);

        ArrayList<Room> list = new ArrayList<Room>();
        String url= Routes.show_all_student_classes+"?token="+token;
        try {
            studentRooms= Kernal.sendGetRequest(url);
            if (studentRooms.length()>0){
                for (int i=0;i<studentRooms.length();i++){
                    try {
                        int id=studentRooms.getJSONObject(i).getInt("id");
                        String name=studentRooms.getJSONObject(i).getString("name");
                        String code=studentRooms.getJSONObject(i).getString("code");
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
        StudentRoomList adapter = new StudentRoomList(list, this,token);


        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.list_of_student_classes);
        lView.setAdapter(adapter);
    }
    public void join_class(View view){
        Intent intent=new Intent(StudentRoomActivity.this,JoinClassActivity.class);
        intent.putExtra("userToken", token);
        startActivity(intent);
    }
}
