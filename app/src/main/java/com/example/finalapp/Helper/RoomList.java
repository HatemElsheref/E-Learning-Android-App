package com.example.finalapp.Helper;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalapp.EditClass;
import com.example.finalapp.LecturesActivity;
import com.example.finalapp.Models.Room;
import com.example.finalapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
                intent.putExtra("classId",list.get(position).id);
                context.startActivity(intent);
            }
        });


        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button editBtn = (Button)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int classID=list.get(position).id;
                String uri=Routes.delete_class+classID+"?token="+token;
                JSONObject jsonObject=Kernal.sendResourceRequest(uri,"DELETE",null);
                if (jsonObject != null){
                    try {
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

//                try{
//                    URL url=new URL(uri);
//                    BufferedReader bufferedReader;
//                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("POST");
//                    String activeDeleteMethod="_method=DELETE";
//                    connection.setDoOutput(true);
//                    connection.setDoInput(true);
//                    connection.setRequestProperty("Content-Language", "en-US");
//                    connection.setRequestProperty("Accept", "application/json");
//                    connection.setRequestProperty("Content-Length",""+Integer.toString(activeDeleteMethod.getBytes().length));
//                    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
//                    wr.writeBytes(activeDeleteMethod);
//                    wr.flush();
//                    wr.close();
//                    InputStream is = connection.getInputStream();
//                    bufferedReader = new BufferedReader(new InputStreamReader(is));
//                    JSONObject jsonObject=new JSONObject(bufferedReader.readLine());
//                     list.remove(position); //or some other task
//                    notifyDataSetChanged();
//                    Toast.makeText(context, jsonObject.get("result").toString(), Toast.LENGTH_SHORT).show();
//
//                }catch (Exception e){
//                    System.out.println(e.getMessage()+"==============>>exception of connection");
//                }

            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                int classID=list.get(position).id;
                String className=list.get(position).name;
                String classCode=list.get(position).code;
                Intent intent=new Intent(context, EditClass.class);
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

}
