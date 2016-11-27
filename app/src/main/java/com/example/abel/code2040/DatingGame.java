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
import java.text.*;

public class DatingGame extends AppCompatActivity {

    private Button submit, next;
    private TextView title, instrc, origDate, intervalTxt, newDate, responce;
    private String TOKEN = "36c6de4f84921a4de705fe11639cd100";
    private String descrip = "In this step the API will provide a datestamp and interval. The datestamp is a string formatted as an" +
            " iso 8601 datestamp and the interval is an integer of seconds. The objective is to add the interval to the datestamp.\n"
            + "\n" + "In order to do this the datestamp had to be reformatted to add the seconds then had to be formatted again " +
            "back to the iso 8601 format then sent to the API to validate.\n" + "\n" + "Click submit to see the results." ;
    private String finDate;
    private String txt1 = "<font color='#000000'>Original ISO8601 DateStamp: </font>";
    private String txt2 = "<font color='#000000'>Seconds Interval: </font>";
    private String txt3 = "<font color='#000000'>Final ISO8601 DateStamp: </font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_game);

        Intent nextPage_needle = getIntent();

        title = (TextView) findViewById(R.id.txt_title);
        instrc = (TextView) findViewById(R.id.txt_instructions);
        origDate = (TextView) findViewById(R.id.text1);
        intervalTxt = (TextView) findViewById(R.id.text2);
        newDate = (TextView) findViewById(R.id.text3);
        responce = (TextView) findViewById(R.id.txt_responce);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);

        responce.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        title.setText("Step V: Dating Game");
        instrc.setText(descrip);
        origDate.setText(Html.fromHtml(txt1));
        intervalTxt.setText(Html.fromHtml(txt2));
        newDate.setText(Html.fromHtml(txt3));


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new getDate().execute("http://challenge.code2040.org/api/dating");
                new postDate().execute("http://challenge.code2040.org/api/dating/validate");
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextPage_dating = new Intent(DatingGame.this, End.class);
                startActivity(nextPage_dating);
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
        public String dateW;
        public int intW;
        public String newDateW;
    }

    public class getDate extends AsyncTask<String, Void, Wrapper>{

        @Override
        protected Wrapper doInBackground(String... params) {
            try {
                //Created a JSON dictionary to post to the api and obtain the datestamp and interval
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                URL url = new URL(params[0]);

                BufferedReader stream = postRequest(dictionary, url);

                JSONObject obj = new JSONObject(stream.readLine());
                String datee = obj.getString("datestamp");
                int interval = obj.getInt("interval");

                //Changed format of the iso datetime then converted the seconds and added the interval to it and converted back
                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date date = date1.parse(datee);
                long seconds = (long)(date.getTime()*(.001));
                long totalSec = (seconds + interval)*(long)(1e3);
                Date newDate = new Date(totalSec);

                String finalDate = date1.format(newDate);

                Wrapper w = new Wrapper();

                w.dateW = datee;
                w.intW = interval;
                w.newDateW = finalDate;
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
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Wrapper w){
            super.onPostExecute(w);

            origDate.setText(Html.fromHtml(txt1 + w.dateW));
            origDate.setVisibility(View.VISIBLE);

            String str = Integer.toString(w.intW);
            intervalTxt.setText(Html.fromHtml(txt2 + str));
            intervalTxt.setVisibility(View.VISIBLE);

            finDate = w.newDateW;
            newDate.setText(Html.fromHtml(txt3 + w.newDateW));
            newDate.setVisibility(View.VISIBLE);

        }
    }

    public class postDate extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                //Created a new json dictionary with the answer to send to the api
                JSONObject dictionary = new JSONObject();
                dictionary.put("token", TOKEN);
                dictionary.put("datestamp", finDate);
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
                String answer = "Step 5 complete";
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
