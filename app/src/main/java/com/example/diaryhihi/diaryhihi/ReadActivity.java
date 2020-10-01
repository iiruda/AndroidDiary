package com.example.diaryhihi.diaryhihi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

    ArrayList<Board> list = null;
    ListView listview;
    String date, result;
    TextView tv;
    Board b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listform);

        listview = (ListView) findViewById(R.id.list);
        tv = (TextView) findViewById(R.id.res);
        Intent intent = getIntent();
        list = (ArrayList<Board>) intent.getSerializableExtra("list");//받아온 전체 리스트

        if(!intent.getStringExtra("date").equals("")){ //날짜를 선택한 글
            date = intent.getStringExtra("date");
            Toast.makeText(ReadActivity.this, date, Toast.LENGTH_SHORT).show();

            tv.setText(date + "의 글");

            ArrayList<Board> viList = new ArrayList<>();//해당 날짜의 리스트만 받게
            ArrayList<String> tit = new ArrayList<>();//제목 보이게

            for(int i = 0; i < list.size(); i++){
                if(list.get(i).getWdate().equals(date)){
                    viList.add(list.get(i));
                    tit.add(list.get(i).getTitle());
                }
            }
            ArrayAdapter<String> adapter;

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tit);
            listview.setAdapter(adapter);

        } else if(!intent.getStringExtra("result").equals("")) { //단어로 검색된 글
            result = intent.getStringExtra("result");
            ArrayList<String> tit = new ArrayList<>();

            tv.setText(result + "로 검색된 글");

            for(int i = 0; i < list.size(); i++){
                tit.add(list.get(i).getTitle());
            }

            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tit);
            listview.setAdapter(adapter);

        } else { //전체 보기
            ArrayList<String> tit = new ArrayList<>();

            tv.setText("전체 보기");

            for(int i = 0; i < list.size(); i++){
                tit.add(list.get(i).getTitle());
            }

            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tit);
            listview.setAdapter(adapter);

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ReadActivity.this, TextActivity.class);
                intent.putExtra("text", list.get(i));
                startActivity(intent);
            }
        });

    }


}
