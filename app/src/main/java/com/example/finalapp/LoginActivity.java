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
import com.example.finalapp.Models.Auth;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText user_email;
    private EditText user_password;;
    private String email;
    private String password;
    private JSONObject UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        user_email=findViewById(R.id.user_email_login);
        user_password=findViewById(R.id.user_password_login);

    }

    public void login_user_btn(View view){
        email=user_email.getText().toString();
        password=user_password.getText().toString();
        if (!email.isEmpty() && email!=null && !password.isEmpty() && password!=null){
            Auth auth=new Auth(""+email,""+password);
            UserInfo=auth.Login();
            if (UserInfo==null){
                Toast.makeText(this, "Invalid Email Or Password Try Again", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    //System.out.println(UserInfo);
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
                    auth.setUSER_TOKEN(UserInfo.getString("access_token"));
                   // System.out.println(auth.getUSER_TOKEN()+"*//////////////*");
                    Intent intent;
                   JSONObject report= Kernal.sendSingleGetRequest(Routes.report+"?token="+UserInfo.getString("access_token"));
                    System.out.println(report.toString());
                   if (UserInfo.getString("role").equalsIgnoreCase("instructor")){
                         intent=new Intent(LoginActivity.this,InstructorDashboardActivity.class);
                       intent.putExtra("classes",report.getString("instructor_rooms"));
                   }else{
                        //redirect to student dashboard activity
                         intent=new Intent(LoginActivity.this,StudentDashboardActivity.class);
                         intent.putExtra("classes",report.getString("student_rooms"));
//                         intent=new Intent(LoginActivity.this,InstructorDashboardActivity.class);
                    }
                    //* redirect to user home activity with token ++ toast message *//
                    intent.putExtra("lectures",report.getString("lectures"));
                    intent.putExtra("userToken",auth.getUSER_TOKEN());
                    startActivity(intent);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    Toast.makeText(this, "Invalid Email Or Password Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Email Or Password Required", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
