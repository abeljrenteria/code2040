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
import java.util.*;
import org.json.*;

public class NeedleHaystack extends AppCompatActivity {

    private Button submit, next;
    private TextView title, instrc, needle, collection, position, responce;
    private String TOKEN = "36c6de4f84921a4de705fe11639cd100";
    private String descrip = "In this step the objective is to find the position of a string in an array. " +
            "In simple terms, find a needle in a haystack. \n" + "\n" + "Once the submit button is pressed the API will be connected to " +
            "and provide the string, the 'needle', and array, the 'haystack'. The code then uses a loop to search through the array to locate " +
            "the given string. Once the string is located it's position in the array is then sent to the API to validate. \n" +
            "\n" + "Click submit to view the result.";
    private int location;
    private String txt1 = "<font color='#000000'>Needle: </font>";
    private String txt2 = "<font color='#000000'>Array: </font>";
    private String txt3 = "<font color='#000000'>Location in Array: </font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        Intent nextPage_rev = getIntent();

        title = (TextView) findViewById(R.id.txt_title);
        instrc = (TextView) findViewById(R.id.txt_descrip);
        needle = (TextView) findViewById(R.id.txt1);
        collection = (TextView) findViewById(R.id.txt2);
        position = (TextView) findViewById(R.id.txt3);
        responce = (TextView) findViewById(R.id.txt_responce);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);

        responce.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        title.setText("Step III: Needle in a Haystack");
        instrc.setText(descrip);
        needle.setText(Html.fromHtml(txt1));
        collection.setText(Html.fromHtml(txt2));
        position.setText(Html.fromHtml(txt3));

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new getNeedle().execute("http://challenge.code2040.org/api/haystack");
                new postLocation().execute("http://challenge.code2040.org/api/haystack/validate");
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextPage_needle = new Intent(NeedleHaystack.this, Prefix.class);
                startActivity(nextPage_needle);
            }
        });
    }

    public static BufferedReader postRequest(JSONObject payload, URL endpoint) {

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
        public String needleW;
        public int location_needleW;
        public JSONArray arrayW;
    }

    public class getNeedle extends AsyncTask<String, Void, Wrapper>{

        @Override
        protected Wrapper doInBackground(String... params) {
            try {
                //Created a JSON dictonary to send to api and obtain the needle and array
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                URL url = new URL(params[0]);

                BufferedReader stream = postRequest(dictionary, url);

                JSONObject obj = new JSONObject(stream.readLine());
                String needle = obj.getString("needle");
                JSONArray arr = obj.getJSONArray("haystack");

                //Stores Array in new ArrayList and searches for the Needle
                ArrayList<String> list = new ArrayList<String>();

                int index = 0;
                for(int j=0; j< arr.length(); j++){

                    list.add(arr.get(j).toString());
                    Boolean flag = list.contains(needle);

                    if(flag){
                        index = j;
                        break;
                    }
                }

                Wrapper w = new Wrapper();

                w.needleW = needle;
                w.arrayW = arr;
                w.location_needleW = index;
                return w;

            } catch (MalformedURLException e) {
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

            needle.setText(Html.fromHtml(txt1 + w.needleW));
            needle.setVisibility(View.VISIBLE);

            String str2 = w.arrayW.toString();
            collection.setText(Html.fromHtml(txt2 + str2));
            collection.setVisibility(View.VISIBLE);

            location = w.location_needleW;
            String str1 = Integer.toString(w.location_needleW);
            position.setText(Html.fromHtml(txt3 + str1));
            position.setVisibility(View.VISIBLE);

        }
    }

    public class postLocation extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                //Created a new json dictionary with the answer to send to the api
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                dictionary.put("needle", location);
                URL url = new URL(params[0]);

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
                String answer = "Step 3 complete";
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
