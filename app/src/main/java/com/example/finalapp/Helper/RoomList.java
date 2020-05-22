package com.example.finalapp.Helper;


import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.finalapp.EditClassActivity;
import com.example.finalapp.LecturesActivity;
import com.example.finalapp.Models.Room;
import com.example.finalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.util.ArrayList;
public class RoomList extends BaseAdapter implements ListAdapter {
    private ArrayList<Room> list = new ArrayList<Room>();
    public String token;
    private Context context;


    public RoomList(ArrayList<Room> list, Context context,String token) {
        this.list = list;
        this.context = context;
        this.token = token;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.component_class_list_buttons, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).name);
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LecturesActivity.class);
                intent.putExtra("userToken",token);
                intent.putExtra("role","instructor");
                intent.putExtra("classId",list.get(position).id);
                context.startActivity(intent);
            }
        });


        //Handle buttons and add onClickListeners
        ImageView deleteBtn = (ImageView)view.findViewById(R.id.delete_btn);
        ImageView editBtn = (ImageView)view.findViewById(R.id.edit_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int classID=list.get(position).id;
                String uri=Routes.delete_class+classID+"?token="+token;
                JSONObject jsonObject=Kernal.sendResourceRequest(uri,"DELETE",null);
                if (jsonObject != null){
                    try {
                        String paths=jsonObject.get("paths").toString();
                       if (!paths.isEmpty() && paths.length()>10){
                           String pathsArray[];
                           paths=paths.replace("[", "");
                           paths=paths.replace("]", "");
                           paths=paths.replace("\"", "");
                           pathsArray=paths.split(",");

                           for (int i=0;i<pathsArray.length;i++){
                               remove_attached_lectures(classID,pathsArray[i]);
                               System.out.println(pathsArray[i]);
                           }
                       }
                        Toast.makeText(context, jsonObject.get("result").toString(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(context, "Class Deleted ", Toast.LENGTH_SHORT).show();
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
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                int classID=list.get(position).id;
                String className=list.get(position).name;
                String classCode=list.get(position).code;
                Intent intent=new Intent(context, EditClassActivity.class);
                intent.putExtra("userToken", token);
                intent.putExtra("classId", classID);
                intent.putExtra("className", className);
                intent.putExtra("classCode", classCode);
                context.startActivity(intent);

                notifyDataSetChanged();
            }

        });

        return view;
    }
    public void remove_attached_lectures(int classID, String path){
        String uri=Routes.delete_lecture+classID+"?token="+token;
        RemoveLecture(path);
        JSONObject jsonObject=Kernal.sendResourceRequest(uri,"DELETE",null);
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
