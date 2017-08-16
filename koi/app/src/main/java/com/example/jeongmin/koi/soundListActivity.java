package com.example.jeongmin.koi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class soundListActivity extends AppCompatActivity {


//    http://recipes4dev.tistory.com/59 참고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_list);

        final ArrayList<String> items = new ArrayList<String>() ;

        items.add("초인종");
        items.add("이름");


        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items) ;

        final ListView listview = (ListView) findViewById(R.id.soundList) ;
        listview.setAdapter(adapter) ;

    }
}
