package com.example.diaryhihi.diaryhihi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    URL url;
    HttpURLConnection con;
    MaterialCalendarView mcv;
    EditText sevi;
    final String ADD = "http://10.10.17.65";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sevi = findViewById(R.id.searchText);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mcv = findViewById(R.id.calendarView);
        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(1900,1,31))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS);
        mcv.addDecorators(new SundayDecorator(), new SaturdayDecorator());

        calendarHandler ch = new calendarHandler();
        mcv.setOnDateChangedListener(ch);
    }

    class calendarHandler implements OnDateSelectedListener{
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, final boolean selected) {

        AlertDialog.Builder d1 = new AlertDialog.Builder(MainActivity.this);
        d1.setTitle("");
        String list[] = {"일기 쓰기", "글 전체 조회", "일기 읽기"};
        d1.setItems(list, new DialogInterface.OnClickListener() {
            String date = "";
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
                 switch (i){
                    case 0:
                        Intent intent1 = new Intent(MainActivity.this, WriteActivity.class);
                        date += mcv.getSelectedDate().getYear() + "/"
                                + (mcv.getSelectedDate().getMonth() + 1) + "/"
                                + mcv.getSelectedDate().getDay();
                        intent1.putExtra("date", date);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(MainActivity.this, ReadActivity.class);
                        ArrayList<Board> tlist = getList();
                        intent2.putExtra("list", tlist);
                        intent2.putExtra("date", "");
                        intent2.putExtra("result", "");
                        startActivity(intent2);
                        break;
                    case 2: //서버에서 목록 불러와서 연결
                        ArrayList<Board> list = null;
                        date += mcv.getSelectedDate().getYear() + "/"
                                + (mcv.getSelectedDate().getMonth() + 1) + "/"
                                + mcv.getSelectedDate().getDay();

                        list = getList();

                        if(list != null){//리스트 가져와서 뷰로 보냄
                            int re = 0;
                            Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                            for(int a = 0; a < list.size(); a++){
                                if(list.get(a).getWdate().equals(date)){
                                    re = 1;
                                }
                            }
                            if(re != 0){
                                intent.putExtra("list", list);
                                intent.putExtra("date", date);
                                intent.putExtra("result", "");
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "저장된 글이 없습니다", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "저장된 글이 없습니다", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        d1.setNegativeButton("취소", null);
        d1.show();
    }
    }

    public void search(View v) {
        if(v.getId() == R.id.searchButton){//검색 버튼
            String st = sevi.getText().toString();
            if(st == null){
                Toast.makeText(MainActivity.this, "검색할 단어를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            } else {
                ArrayList<Board> list = getList();
                ArrayList<Board> check = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getTitle().contains(st) || list.get(i).getText().contains(st)){
                        check.add(list.get(i));
                    }
                }
                if(check.size() != 0){
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    intent.putExtra("list", check);
                    intent.putExtra("result", st);
                    intent.putExtra("date", "");
                    startActivity(intent);
                    sevi.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "검색된 글이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public ArrayList<Board> getList(){
        ArrayList<Board> list = null;

        try{
            url = new URL(ADD + ":8888/www/selectList");
        } catch (MalformedURLException e){
            Toast.makeText(MainActivity.this,"잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
        }
        try{
            con = (HttpURLConnection) url.openConnection();

            if(con != null){
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line;
                    String page = "";
                    while ((line = reader.readLine()) != null){
                        page += line;
                    }
                    list = jsonParse(page);
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return  list;
    }

    public ArrayList<Board> jsonParse(String page){
        JSONArray jarray = null;
        JSONObject item = null;
        ArrayList<Board> list = new ArrayList<Board>();

        try {
            jarray = new JSONArray(page);

            for (int i = 0; i < jarray.length(); i++) {
                item = jarray.getJSONObject(i);
                list.add(new Board(item.getInt("boardnum"), item.getString("title"), item.getString("text"), item.getString("wdate")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}
