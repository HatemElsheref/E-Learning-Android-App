package com.example.finalapp.Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FileHandling {

    Button upload;
    Button browse;
    ImageView imageView;
    Uri uri=null;
    FirebaseStorage storage;
    StorageReference reference;


    public static final int PICK_IMAGE_REQUEST=71;


    public void GetFile(String fileName, String type, final Context context){
        String name=(type=="images")?"images/"+fileName:"lectures/"+fileName;
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference("images/"+name);
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();

                //getWindowManager().getDefaultDisplay().getMetrics(dm);
                imageView.setMinimumHeight(dm.heightPixels);
                imageView.setMinimumWidth(dm.widthPixels);
                imageView.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void UploadFile(Uri filePath,String type){

    }
    public static Intent InitFileType(String type){
        Intent intent=new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
        //startActivityForResult(Intent.createChooser(intent, "select picture"),FileHandling.PICK_IMAGE_REQUEST);
    }


    public void DownloadFile(){

    }
    public void DeleteFile(){

    }
}
