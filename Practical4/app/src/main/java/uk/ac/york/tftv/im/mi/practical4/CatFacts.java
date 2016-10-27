package uk.ac.york.tftv.im.mi.practical4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatFacts extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_facts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat_facts, menu);
        return true;
    }

    public void getFact(View v) {
        new getData().execute("http://catfacts-api.appspot.com/api/facts?number=1"); //starts thread to get fact
    }

    public void setFact(String fact_data) {
        String fact;
        try {
            JSONObject json = new JSONObject(fact_data);
            fact = json.getJSONArray("facts").getString(0); //get the array "facts", and then get the 0th element of the array
        }
        catch(JSONException e) {
            fact = e.getLocalizedMessage();
        }
        ((TextView)findViewById(R.id.catFactTextView)).setText(fact); //CHANGE THIS TO MATCH YOUR LAYOUT
    }

    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                StringBuilder sb = new StringBuilder();
                String line;

                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = bf.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                bf.close();
                conn.getInputStream().close(); //close connection

                return(sb.toString());
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            setFact(result);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
