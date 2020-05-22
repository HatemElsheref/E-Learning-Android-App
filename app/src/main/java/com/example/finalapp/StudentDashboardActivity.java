package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StudentDashboardActivity extends AppCompatActivity {
    public String token;
    TextView Lectures;
    TextView Rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        Lectures=findViewById(R.id.lec_num);
        Rooms=findViewById(R.id.class_num);
        Lectures.setText("Lectures ( "+intent.getStringExtra("lectures")+" )");
        Rooms.setText("Classes ( "+intent.getStringExtra("classes")+" )");
    }

    public void redirect_to_myclasses(View view){
        Intent intent=new Intent(StudentDashboardActivity.this, StudentRoomActivity.class);
        intent.putExtra("userToken",token);
        //System.out.println("in Instructor Dashboard Activity  redirect func ********* "+token);
        startActivity(intent);
    }

}
