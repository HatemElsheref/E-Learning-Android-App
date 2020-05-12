package com.example.finalapp.Helper;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalapp.EditClass;
import com.example.finalapp.LecturesActivity;
import com.example.finalapp.Models.Lecture;
import com.example.finalapp.Models.Room;
import com.example.finalapp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;

public class LectureList extends BaseAdapter implements ListAdapter {
    private ArrayList<Lecture> list = new ArrayList<Lecture>();
    public String token;
     Context context;


    public LectureList(ArrayList<Lecture> list, Context context,String token) {
        this.list = list;
        this.context = context;
        this.token = token;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public long getItemId(int pos) {
        return pos;
//        return  list.get(pos).getById();
        //just return 0 if your list items do not have an Id variable.
    }
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.component_lecture_list_buttons, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.lectures_item_string);
        listItemText.setText(list.get(position).name);
        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_lecture);
        Button downloadBtn = (Button)view.findViewById(R.id.download_lecture);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int classID=list.get(position).id;
                String uri=Routes.delete_lecture+classID+"?token="+token;
                RemoveLecture(list.get(position).path);
                JSONObject jsonObject=Kernal.sendResourceRequest(uri,"DELETE",null);
                if (jsonObject != null){
                    try {
                        Toast.makeText(context, jsonObject.get("result").toString(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(context, "Lecture Deleted ", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Failed Operation", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
        downloadBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final ProgressDialog loader=new ProgressDialog(context);
                loader.setMessage("downloading");
                loader.show();
                final String path = list.get(position).path;
                final String name = list.get(position).name;
                String fileToken = downloadFile(path);
                String pdfToken = fileToken.split(":")[1].replace("\"", "");
                pdfToken = pdfToken.trim();


                String Url = "https://firebasestorage.googleapis.com/v0/b/covid-19-d88e9.appspot.com/o/Lectures%2F";
                String params = "?alt=media&token=" + pdfToken;


                try {
                    URL url = new URL(Url + path + params);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    FirebaseStorage fbs=FirebaseStorage.getInstance();
                    StorageReference sr=fbs.getReference();
                    sr=sr.child("Lectures/"+path);
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            download(context,uri.toString(),"Lectures/"+path,".pdf",Environment.DIRECTORY_DOWNLOADS+"/Lectures",name);
                            loader.dismiss();
                            Toast.makeText(context, "Lecture Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           loader.dismiss();
                            Toast.makeText(context, "Failed To Upload Lecture", Toast.LENGTH_SHORT).show();
                        }
                    });
                    } catch (IOException e) {
                    }
                    notifyDataSetChanged();
            }
        });

        return view;
    }
    private String downloadFile(String path) {
        try {

            URL url = new URL(" https://firebasestorage.googleapis.com/v0/b/covid-19-d88e9.appspot.com/o/Lectures%2F"+path);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader fileOutputStream = new BufferedReader(new InputStreamReader(inputStream));
            String data="";
            for (int i=0;i<15;i++){
               fileOutputStream.readLine();
            }
            data=fileOutputStream.readLine();
            fileOutputStream.close();
            return data;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void download(Context context,String url,String filename,String extension,String destination,String name) {
        DownloadManager dm=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request r =new DownloadManager.Request(uri);
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        r.setDestinationInExternalFilesDir(context, destination, name+extension);
        dm.enqueue(r);
    }
    private void RemoveLecture(String path)
    {
        FirebaseStorage fbs=FirebaseStorage.getInstance();
        StorageReference sr=fbs.getReference();
        sr=sr.child("Lectures/"+path);
        sr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Lecture Removed With Attached File", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                System.out.println(exception.getMessage());
                Toast.makeText(context, "Failed To Remove Attached File", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
