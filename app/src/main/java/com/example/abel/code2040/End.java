package com.example.abel.code2040;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class End extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Button btn_home = (Button) findViewById(R.id.btn_home);

        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent about = new Intent(End.this, StartActivity.class);
                startActivity(about);
            }
        });
    }
}
