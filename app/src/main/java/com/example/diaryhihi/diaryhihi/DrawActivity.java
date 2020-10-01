package com.example.diaryhihi.diaryhihi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DrawActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawform);
    }

    public void click(View v){
        switch(v.getId()){
            case R.id.save:
                break;
            case R.id.reset:
                break;
            case R.id.write:
                Intent intent = new Intent(DrawActivity.this, WriteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.main:
                finish();
                break;
        }
    }
}
