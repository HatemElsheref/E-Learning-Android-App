package com.example.finalapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapp.Models.ClassActivity;

public class InstructorDashboardActivity extends AppCompatActivity {
    public String token;
    TextView Lectures;
    TextView Rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_dashboard);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        Lectures=findViewById(R.id.lectures_number);
        Rooms=findViewById(R.id.classes_number);
        Lectures.setText("Lectures ( "+intent.getStringExtra("lectures")+" )");
        Rooms.setText("Classes ( "+intent.getStringExtra("classes")+" )");
        //System.out.println("in Instructor Dashboard Activity ********* "+token);

    }
    public void redirect_to_classs_activity(View view){
        Intent intent=new Intent(InstructorDashboardActivity.this, InstructorRoomActivity.class);
        intent.putExtra("userToken",token);
        //System.out.println("in Instructor Dashboard Activity  redirect func ********* "+token);
        startActivity(intent);
    }

}
