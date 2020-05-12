package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.finalapp.Models.Auth;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText user_name;
    private EditText user_email;
    private EditText user_password;
    private String name;
    private String email;
    private String password;
    private String role;
    private JSONObject UserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_password);
        Intent intent=getIntent();
        role=intent.getStringExtra("role");

    }

    public void register_new_account(View view){
        name=user_name.getText().toString();
        email=user_email.getText().toString();
        password=user_password.getText().toString();
        if (!name.isEmpty() && name!=null && !email.isEmpty() && email!=null && !password.isEmpty() && password!=null){
            Auth auth=new Auth(""+name,""+email,""+password,""+role);
            UserInfo=auth.Register();

            if (UserInfo==null){
                Toast.makeText(this, "Invalid Email Or Password Try Again", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
                    auth.setUSER_TOKEN(UserInfo.getString("access_token"));
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    //* redirect to user home activity with token ++ toast message *//
//                    intent.putExtra("userToken",auth.getUSER_TOKEN());
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(this, "Invalid Email Or Password Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Email Or Password Required", Toast.LENGTH_SHORT).show();
            return;
        }

    }



}
