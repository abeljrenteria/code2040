package com.example.abel.code2040;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.io.*;
import java.net.*;
import org.json.*;

public class ReverseString extends AppCompatActivity {

    private Button submit, next;
    private TextView title, responce, ogString, reverseString, instrc;
    private String TOKEN = "36c6de4f84921a4de705fe11639cd100";
    private String originalString, reversedString;
    private String descrip = "In this step the objective is to reverse a string provided by the API. For example if the API says 'cupcake' " +
            "I would send back 'ekacpuc'.\n" + "\n" +
            "Once the submit button is pressed the api will be connected and provide a given string. I then created a program that " +
            "will take the string and reverse it using the JAVA reverse() method. After the string is reversed I then store it in a " +
            "JSON dictonary along with my token and send it to the API to valiate the response. \n" + "\n" +
            "Click submit to obtain the String and the String Reversed.\n";
    String txt1 = "<font color='#000000'>String: </font>";
    String txt2 = "<font color='#000000'>String Reversed: </font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        Intent nextPage = getIntent();

        title = (TextView) findViewById(R.id.txt_title);
        instrc = (TextView) findViewById(R.id.txt_instructions);
        ogString = (TextView) findViewById(R.id.text1);
        reverseString = (TextView) findViewById(R.id.text2);
        responce = (TextView) findViewById(R.id.txt_responce);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);

        title.setText("Step II: Reverse String");
        ogString.setText(Html.fromHtml(txt1));
        reverseString.setText(Html.fromHtml(txt2));
        instrc.setText(descrip);
        responce.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new getString().execute("http://challenge.code2040.org/api/reverse");
                new reverseString().execute("http://challenge.code2040.org/api/reverse/validate");
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextPage_rev = new Intent(ReverseString.this, NeedleHaystack.class);
                startActivity(nextPage_rev);
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

    public class Wrapper
    {
        public String theString;
        public String theReversedString;
    }


    public class getString extends AsyncTask<String, Void, Wrapper>{
        @Override
        protected Wrapper doInBackground(String... params) {
            try{
                //Created a JSON dictionary to send to api and obtain the String needed to reverse
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                URL url = new URL(params[0]);

                BufferedReader stream = postRequest(dictionary, url);
                String originalString = stream.readLine();

                //Reverses the string
                String reversedString = new StringBuffer(originalString).reverse().toString();

                Wrapper w = new Wrapper();

                w.theString = originalString;
                w.theReversedString = reversedString;

                return w;

            } catch (MalformedURLException e){
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Wrapper w){
            super.onPostExecute(w);

            ogString.setText(Html.fromHtml(txt1 + w.theString));
            reverseString.setText(Html.fromHtml(txt2 + w.theReversedString));

            ogString.setVisibility(View.VISIBLE);
            reverseString.setVisibility(View.VISIBLE);

            reversedString = w.theReversedString;
        }
    }

    public class reverseString extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                //Created a new json dictionary with the answer to send to the api and get response
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                dictionary.put("string", reversedString);
                URL url = new URL(params[0]);

                BufferedReader stream = postRequest(dictionary, url);
                return stream.readLine();

            } catch(MalformedURLException e){
                e.printStackTrace();
                return null;
            } catch(JSONException e) {
                e.printStackTrace();
                return null;
            } catch(IOException e) {
                e.printStackTrace();
                return null;
            } catch(NullPointerException e) {
                e.printStackTrace();
                String answer = "Step 2 complete";
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
