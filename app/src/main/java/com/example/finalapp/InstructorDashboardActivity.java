package com.example.finalapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapp.Models.ClassActivity;

public class InstructorDashboardActivity extends AppCompatActivity {
    public String token;
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
        //System.out.println("in Instructor Dashboard Activity ********* "+token);

    }
    public void redirect_to_classs_activity(View view){
        Intent intent=new Intent(InstructorDashboardActivity.this, InstructorRoomActivity.class);
        intent.putExtra("userToken",token);
        //System.out.println("in Instructor Dashboard Activity  redirect func ********* "+token);
        startActivity(intent);
    }

}
