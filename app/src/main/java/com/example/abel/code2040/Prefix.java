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
import java.util.*;
import org.json.*;

public class Prefix extends AppCompatActivity {

    private Button submit, next;
    private TextView title, instrc, pre, collection, newCollection, responce;
    private String TOKEN = "36c6de4f84921a4de705fe11639cd100";
    private ArrayList newArray;
    private String descrip = "In this step the objective is to return an array containing only the strings that do not start with a " +
            "prefix that is provided by the API. \n" + "\n" + "Click submit to view the result.";
    private String txt1 = "<font color='#000000'>Array: </font>";
    private String txt2 = "<font color='#000000'>Prefix: </font>";
    private String txt3 = "<font color='#000000'>New Array: </font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefix);

        Intent nextPage_needle = getIntent();

        title = (TextView) findViewById(R.id.txt_title);
        instrc = (TextView) findViewById(R.id.txt_descrip);
        pre = (TextView) findViewById(R.id.txt1);
        collection = (TextView) findViewById(R.id.txt2);
        newCollection = (TextView) findViewById(R.id.txt3);
        responce = (TextView) findViewById(R.id.txt_responce);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);

        title.setText("Step IV: Prefix");
        instrc.setText(descrip);
        collection.setText(Html.fromHtml(txt1));
        pre.setText(Html.fromHtml(txt2));
        newCollection.setText(Html.fromHtml(txt3));
        responce.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new getPrefix().execute("http://challenge.code2040.org/api/prefix");
                new postPrefix().execute("http://challenge.code2040.org/api/prefix/validate");
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextPage_prefix = new Intent(Prefix.this, DatingGame.class);
                startActivity(nextPage_prefix);
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
        public String prefixW;
        public JSONArray array1;
        public ArrayList arrayW;
    }

    public class getPrefix extends AsyncTask<String, Void, Wrapper>{

        @Override
        protected Wrapper doInBackground(String... params) {
            try {
                //Created a JSON dictionary to post to the api and obtain the prefix and array
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                URL url = new URL(params[0]);

                BufferedReader stream = postRequest(dictionary, url);

                JSONObject obj = new JSONObject(stream.readLine());
                String prefix = obj.getString("prefix");
                JSONArray arr = obj.getJSONArray("array");

                ArrayList<String> list = new ArrayList<String>();
                ArrayList<String> sublist = new ArrayList<String>();

			    //Stores the Array in a new arrayList and searches for string in the arraylist
                for(int j=0; j<arr.length(); j++){

                    list.add(arr.get(j).toString());

                    Boolean flag = list.get(j).startsWith(prefix);

                    if(flag == false){
                        sublist.add(list.get(j));
                    }
                }

                Wrapper w = new Wrapper();

                w.prefixW = prefix;
                w.array1 = arr;
                w.arrayW = sublist;
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

            String str2 = w.array1.toString();
            collection.setText(Html.fromHtml(txt1 + str2));

            pre.setText(Html.fromHtml(txt2 + w.prefixW));

            newArray = w.arrayW;
            String location = w.arrayW.toString();
            newCollection.setText(Html.fromHtml(txt3 + location));

            pre.setVisibility(View.VISIBLE);
            collection.setVisibility(View.VISIBLE);
            newCollection.setVisibility(View.VISIBLE);
        }
    }

    public class postPrefix extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                //Created a new json dictionary with the answer to send to the api
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                dictionary.put("array", newArray);
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
                String answer = "Step 4 complete";
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
