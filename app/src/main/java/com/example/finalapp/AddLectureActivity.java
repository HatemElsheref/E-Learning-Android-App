package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalapp.Helper.FileHandling;
import com.example.finalapp.Helper.Kernal;
import com.example.finalapp.Helper.Routes;
import com.example.finalapp.Models.Lecture;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.UUID;

public class AddLectureActivity extends AppCompatActivity {
    public String token;
    public int classId;
    public String LectureName;
    public String LecturePath;
    EditText editText;
    Button browse;
    Button add;
    JSONObject result;
    Uri uri=null;
    FirebaseStorage storage;
    StorageReference reference;
    public static final int PICK_IMAGE_REQUEST=71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        token=intent.getStringExtra("userToken");
        classId=intent.getIntExtra("classId", -1);
        editText=findViewById(R.id.add_new_lecture_text);
        browse=findViewById(R.id.browse);
        add=findViewById(R.id.store_lecture);
        storage=FirebaseStorage.getInstance();
        reference=storage.getReference();
    }


    public void add_new_lecture_btn(View view){
        if (uri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading ..");
            progressDialog.show();
            LecturePath=UUID.randomUUID().toString();
            StorageReference ref=reference.child("Lectures/"+LecturePath);

            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    LectureName=editText.getText().toString();
                    String Link= Routes.store_lecture+"?token="+token;
                    try{
                        JSONObject lectureInfo=new JSONObject();
                        lectureInfo.put("name", LectureName);
                        lectureInfo.put("path", LecturePath);
                        lectureInfo.put("room_id", classId);
                        result=Kernal.sendPostRequest(Link,lectureInfo,null);
                        if (result!=null){
                            Intent intent=new Intent(AddLectureActivity.this,LecturesActivity.class);
                            intent.putExtra("userToken", token);
                            intent.putExtra("classId", classId);
                            Toast.makeText(AddLectureActivity.this, "Lecture Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else{
                            Toast.makeText(AddLectureActivity.this, "Failed To Upload Lecture", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){
                        Toast.makeText(AddLectureActivity.this, "Failed To Lecture", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage()+"//////////////////////");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddLectureActivity.this, "Failed To Upload Lecture File", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress=(100.0*taskSnapshot.getBytesTransferred())/(taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage("uploaded "+(int)progress+"%");
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    //displaying percentage in progress dialog
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        }else{
            Toast.makeText(this, "Please , Select PDF FILE First...", Toast.LENGTH_SHORT).show();
        }
    }
    public void select_lecture_pdf_file(View view){
        choiceImage("application/pdf");
    }
    public void choiceImage(String Type){
        Intent intent=new Intent();
        intent.setType(Type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            uri=data.getData();
        }
    }

}
