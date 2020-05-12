package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button login_view;
    Button register_instructor_view;
    Button register_student_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_view=findViewById(R.id.login_redirect);
        register_instructor_view=findViewById(R.id.register_as_instructor);
        register_student_view=findViewById(R.id.register_as_student);
    }
    public void login_view(View view){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void register_student_view(View view){
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        intent.putExtra("role", "student");
        startActivity(intent);
    }
    public void register_instructor_view(View view){
        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        intent.putExtra("role", "instructor");
        startActivity(intent);
    }
}
