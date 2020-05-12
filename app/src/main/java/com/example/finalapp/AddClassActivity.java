package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;

import org.json.JSONObject;

public class AddClassActivity extends AppCompatActivity {
    public String token;
    public EditText className;
    public JSONObject result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        className=findViewById(R.id.add_new_class_text);
    }
    public void add_new_class_btn(View view){
        String Classname=className.getText().toString();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("name", Classname);
            result= Kernal.sendPostRequest(Routes.store_class+"?token="+token, jsonObject, null);
            Toast.makeText(this,"Congratulations "+result.get("message").toString(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(AddClassActivity.this,InstructorRoomActivity.class);
            intent.putExtra("userToken", token);
            startActivity(intent);
        }catch (Exception e){
            System.out.println("++++++++++"+e.getMessage()+"+++++++++++++");
        }

    }


}
