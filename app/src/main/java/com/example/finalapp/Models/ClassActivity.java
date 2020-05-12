package com.example.finalapp.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.finalapp.Helper.ClassesList;
import com.example.finalapp.R;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        //generate list
        ArrayList<String> list = new ArrayList<String>();
        list.add("item1");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");
        list.add("item2");

        //instantiate custom adapter
        ClassesList adapter = new ClassesList(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.list_of_classes);
        lView.setAdapter(adapter);
    }
}
