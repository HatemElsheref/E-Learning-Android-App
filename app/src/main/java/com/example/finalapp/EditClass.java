package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditClass extends AppCompatActivity {

    String token;
    int classId;
    String className;
    String classCode;
    EditText classname;
    TextView classcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");

        classId=intent.getIntExtra("classId",-1);
        className=intent.getStringExtra("className");
        classCode=intent.getStringExtra("classCode");
        classname=findViewById(R.id.class_name_text);
        classcode=findViewById(R.id.class_code_label);
        classname.setText(className );
        classcode.setText(classCode);

    }

    public void edit_new_class_btn(View view){
        JSONObject requestbody=new JSONObject();
        try {
            requestbody.put("name",classname.getText().toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        String uri= Routes.update_class+classId+"?_method=put&token="+token;
        JSONObject jsonObject= Kernal.sendResourceRequest(uri,"PUT",requestbody);
        if (jsonObject != null){
            try {
                Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, "Class Updated ", Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
            }
            Intent intent=new Intent(EditClass.this,InstructorRoomActivity.class);
            intent.putExtra("userToken", token);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Failed Operation", Toast.LENGTH_SHORT).show();
        }

    }
    public void copy_class_code(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", classcode.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "class code copied now !!", Toast.LENGTH_SHORT).show();
    }

}