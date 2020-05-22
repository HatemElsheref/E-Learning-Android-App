package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;

import org.json.JSONArray;
import org.json.JSONObject;

public class JoinClassActivity extends AppCompatActivity {
    public JSONObject result;
    public String token;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        editText=findViewById(R.id.join_class_text);
    }

    public void join_class_btn(View view){
        String code=editText.getText().toString();
        String url= Routes.join_class+code+"?token="+token;
        result= Kernal.sendPostRequest(url,null,null);
        if (result != null){
           try {
               Toast.makeText(this, result.get("message").toString(), Toast.LENGTH_SHORT).show();

           }catch (Exception e){
               Toast.makeText(this,"failed to join", Toast.LENGTH_SHORT).show();

               System.out.println(e.getMessage());
           }
           Intent intent=new Intent(JoinClassActivity.this,StudentRoomActivity.class);
           intent.putExtra("userToken", token);
           startActivity(intent);
        }

    }
}
