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

import com.example.finalapp.Models.Room;
import com.example.finalapp.R;
import com.example.finalapp.StudentLectureActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class StudentRoomList extends BaseAdapter implements ListAdapter {
    private ArrayList<Room> list = new ArrayList<Room>();
    public String token;
    private Context context;


    public StudentRoomList(ArrayList<Room> list, Context context,String token) {
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
            view = inflater.inflate(R.layout.component_class_student_list_buttons, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_student_string);
        listItemText.setText(list.get(position).name);
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,StudentLectureActivity.class);
                intent.putExtra("userToken",token);
                intent.putExtra("classId",list.get(position).id);
                context.startActivity(intent);
            }
        });
        ImageView deleteBtn = (ImageView)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int classID=list.get(position).id;
                String uri=Routes.exit_class+classID+"?token="+token;
                JSONObject jsonObject=Kernal.sendPostRequest(uri,null,null);
                if (jsonObject != null){
                    try {
                        String paths=jsonObject.get("message").toString();
                        Toast.makeText(context,paths, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(context, "exited successfully ", Toast.LENGTH_SHORT).show();
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

        return view;
    }
}
