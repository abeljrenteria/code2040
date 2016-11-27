package com.example.abel.code2040;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.text.Html;
import java.io.*;
import java.net.*;
import org.json.*;

public class Registration extends AppCompatActivity {

    private Button next, submit;
    private TextView title, instructions, token, git, responce;
    private String TOKEN = "36c6de4f84921a4de705fe11639cd100";
    private String GITHUB = "http://github.com/abeljrenteria/code2040.git";
    private String descrip = "In this first challenge the objective was to connect to the registration endpoint, http://challenge.code2040.org/api/register. \n" +
            "\n" +
            "A JSON dictionary with two keys, token and Github, were sent to the registration endpoint using POST." +
            "The token key is a string with my assigned token and in the Github key I passed the url of the Github repository I created for this challenge. \n" +
            "\n" +
            "Both my token and Github url are represented below and ready to be submited to the API.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        Intent begin = getIntent();
        Intent registration1 = getIntent();

        title = (TextView) findViewById(R.id.txt_title);
        token = (TextView) findViewById(R.id.text1);
        git = (TextView) findViewById(R.id.text2);
        responce = (TextView) findViewById(R.id.txt_responce);
        instructions = (TextView) findViewById(R.id.txt_instructions);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);

        title.setText("Step I: Registration");
        String colorTxt = "<font color='#000000'>Token: </font>";
        token.setText(Html.fromHtml(colorTxt + TOKEN));
        String colorTxt1 = "<font color = '#000000'>Github: </font>";
        git.setText(Html.fromHtml(colorTxt1 + GITHUB));
        instructions.setText(descrip);
        next.setVisibility(View.INVISIBLE);
        responce.setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new register().execute("http://challenge.code2040.org/api/register");
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextPage = new Intent(Registration.this, ReverseString.class);
                startActivity(nextPage);
            }
        });
    }

    public static BufferedReader postRequest(JSONObject payload, URL endpoint){

        BufferedReader reader = null;

        //POST request
        try {
            HttpURLConnection connect = (HttpURLConnection) endpoint.openConnection();
            connect.setDoOutput(true);
            connect.setDoInput(true);
            connect.setRequestProperty("Content-Type", "application/json");
            connect.setRequestProperty("Accept", "applicaton/json");
            connect.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connect.getOutputStream(), "UTF-8");
            writer.write(payload.toString());
            writer.flush();
            writer.close();

            reader = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reader;
    }

    public class register extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                //created a JSON dictionary to send to api
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                dictionary.put("github", GITHUB);
                URL url = new URL(params[0]);

                //sends dictionary to API and retrieves response
                BufferedReader stream = postRequest(dictionary, url);
                return stream.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch(NullPointerException e) {
                e.printStackTrace();
                String answer = "Step 1 Complete";
                return answer;
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            responce.setText(result);
            responce.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);

        }
    }
}
















































