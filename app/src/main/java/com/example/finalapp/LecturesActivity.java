package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.LectureList;
import com.example.finalapp.Helper.RoomList;
import com.example.finalapp.Helper.Routes;
import com.example.finalapp.Models.Lecture;
import com.example.finalapp.Models.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LecturesActivity extends AppCompatActivity {
    JSONArray ClassLectures;
    public String token;
    public int classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        token = intent.getStringExtra("userToken");
        classId = intent.getIntExtra("classId", -1);

        ArrayList<Lecture> list = new ArrayList<Lecture>();
        String url = Routes.show_class_lectures+classId+"?token="+token;
        try {
            JSONObject ClassLectures_ = Kernal.sendSingleGetRequest(url);
            ClassLectures=ClassLectures_.getJSONArray("data");
            for (int i = 0; i < ClassLectures.length(); i++) {
                try {
                    int id = ClassLectures.getJSONObject(i).getInt("id");
                    String name = ClassLectures.getJSONObject(i).getString("name");
                    String path = ClassLectures.getJSONObject(i).getString("path");
                    String date = ClassLectures.getJSONObject(i).getString("date");
                    list.add(new Lecture(id, name, path,date));
                } catch (Exception e) {
                    System.out.println("++++++++++" + e.getMessage() + "+++++++++++++");
                }
            }
        } catch (Exception e) {
            System.out.println("++++++++++" + e.getMessage() + "+++++++++++++");
        }


        //instantiate custom adapter
        LectureList adapter = new LectureList(list, this,token);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.list_of_lectures);
        lView.setAdapter(adapter);

    }

    public void redirect_to_add_lecture_activity(View view) {
        Intent intent = new Intent(LecturesActivity.this, AddLectureActivity.class);
        intent.putExtra("userToken", token);
        intent.putExtra("classId", classId);
        startActivity(intent);
    }


}

