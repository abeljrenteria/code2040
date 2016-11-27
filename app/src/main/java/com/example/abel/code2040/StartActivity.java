package com.example.abel.code2040;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btn_about = (Button) findViewById(R.id.btn_about);
        Button btn_begin = (Button) findViewById(R.id.btn_begin);

        btn_about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent about = new Intent(StartActivity.this, AboutPage.class);
                startActivity(about);
            }
        });

        btn_begin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent begin = new Intent(StartActivity.this, Registration.class);
                startActivity(begin);
            }
        });


    }
}
