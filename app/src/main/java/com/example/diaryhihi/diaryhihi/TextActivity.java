package com.example.diaryhihi.diaryhihi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class TextActivity extends AppCompatActivity {

    TextView title, text, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readform);

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        date = findViewById(R.id.date);

        Intent intent = getIntent();

        Board b = (Board) intent.getSerializableExtra("text");

        title.setText("Title : " + b.getTitle());
        text.setText("Text : " + b.getText());
        date.setText("Date : " + b.getWdate());
    }

    public void but(View v){
        Intent intent = new Intent(TextActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
