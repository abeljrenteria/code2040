package com.example.abel.code2040;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    private TextView txt_view_mainTitle, txt_view_descrip;
    private String mainTitle = "Introduction";
    private String descrip = "The next step in the Code2040 application process is the Technical Assessment. \n" +
            "\n" +
            "The Technical Assessment is an API challenge with the goal of showcasing our coding abilities by completing 5 programming challenges presented by Code2040. \n" +
            "\n" +
            "In order to complete this challenge I chose to use the JAVA programming language and Android Studio to create an android mobile " +
            "application. \n" + "\n" +
            "Click begin to start the challenge.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        Intent about = getIntent();

        Button btn_begin = (Button) findViewById(R.id.btn_begin);
        txt_view_mainTitle = (TextView) findViewById(R.id.txt_mainTitle);
        txt_view_descrip = (TextView) findViewById(R.id.txt_description);

        txt_view_mainTitle.setText(mainTitle);
        txt_view_descrip.setText(descrip);

        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration1 = new Intent(AboutPage.this, Registration.class);
                startActivity(registration1);

            }
        });
    }
}





































