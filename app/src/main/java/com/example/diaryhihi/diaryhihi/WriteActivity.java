package com.example.diaryhihi.diaryhihi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends Activity {
    URL url;
    HttpURLConnection con;

    EditText edit;
    EditText ed;
    TextView tv;
    String s;
    final String ADD = "http://10.10.17.65";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeform);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        edit = findViewById(R.id.text);
        ed = findViewById(R.id.title);
        tv = findViewById(R.id.date);

        Intent intent = getIntent();
        s = intent.getStringExtra("date");
        tv.setText(s);
    }

    public void click(View v){
        switch(v.getId()){
            case R.id.save:
                String title = ed.getText().toString();
                String text = edit.getText().toString();

                if(title.isEmpty() || text.isEmpty()){
                    Toast.makeText(WriteActivity.this, "빈 칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("text", text);
                params.put("wdate", s);

                String param = makeParams(params);

                try{
                    url = new URL(ADD + ":8888/www/insertBoard");
                } catch (MalformedURLException e){
                    Toast.makeText(this,"잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
                }

                try{
                    con = (HttpURLConnection) url.openConnection();

                    if(con != null){
                        con.setConnectTimeout(10000);
                        con.setUseCaches(false);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Accept-Charset", "UTF-8");
                        con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                        OutputStream os = con.getOutputStream();
                        os.write(param.getBytes("UTF-8"));
                        os.flush();
                        os.close();

                        if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                            String line;
                            String page = "";

                            while ((line = reader.readLine()) != null){
                                page += line;
                            }
                            Toast.makeText(this, page, Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
                break;

            case R.id.reset:
                reset();
                break;

            case R.id.draw:
                Intent intent = new Intent(WriteActivity.this, DrawActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.main:
                Intent main = new Intent(WriteActivity.this, MainActivity.class);
                startActivity(main);
                finish();
                break;
        }
    }

    public void reset(){
        ed.setText("");
        edit.setText("");
    }

    public String makeParams(HashMap<String,String> params){
        StringBuffer sbParam = new StringBuffer();
        String key = "";
        String value = "";
        boolean isAnd = false;

        for(Map.Entry<String,String> elem : params.entrySet()){
            key = elem.getKey();
            value = elem.getValue();
            if(isAnd){
                sbParam.append("&");
            }
            sbParam.append(key).append("=").append(value);
            if(!isAnd){
                if(params.size() >= 2){
                    isAnd = true;
                }
            }
        }
        return sbParam.toString();
    }

}
