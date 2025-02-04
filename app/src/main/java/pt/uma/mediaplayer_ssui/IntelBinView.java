package pt.uma.mediaplayer_ssui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static pt.uma.mediaplayer_ssui.IntelBin.createIntelBinList;


public class IntelBinView extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mIntelBinList;
    private IntelBinAdapter mAdapter;

    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new AsyncFetch().execute();
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 20*1000);  // interval of one minute

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia_list);
        //RecyclerView

        //Make call to AsyncTask
       setRepeatingAsyncTask();

    }

    private  class AsyncFetch extends AsyncTask<String, String, String> {
            ProgressDialog pdLoading = new ProgressDialog(IntelBinView.this);
            HttpURLConnection conn;

            URL url = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();

            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    // Enter URL address where your json file resides
                    // Even you can make call to php file which returns json data
                    url = new URL("http://192.168.2.130:8080/intelbinsuser/4");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return e.toString();
                }
                try {

                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("GET");


                    // setDoOutput to true as we recieve data from json file
                    //conn.setDoOutput(true);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return e1.toString();
                }

                try {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                   if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        return (result.toString());

                    } else {

                       return ("unsuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                } finally {
                    conn.disconnect();
                }


            }

            @Override
            protected void onPostExecute(String result) {
                //this method will be running on UI thread
                pdLoading.dismiss();
                List<IntelBin> intelBinList = IntelBin.createIntelBinList();
                pdLoading.dismiss();
                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        int fetchedBinId= json_data.getInt("id");
                        String fetchedBinName= json_data.getString("nome");
                        String fetchedBinDesc= json_data.getString("descricao");
                        int fetchedBinLevel = json_data.getInt("level");
                        String fetchedBinFloor= json_data.getString("andar");
                        //int fetchedBinFloor_id= json_data.getInt("intelBinFloor_id");
                        String fetchedBinBuilding=json_data.getString("edificio");
                        String fetchedBinLocation=json_data.getString("local");
                        intelBinList.add(new IntelBin(fetchedBinId,fetchedBinName,fetchedBinDesc,fetchedBinLevel,fetchedBinFloor,1,fetchedBinBuilding,fetchedBinLocation));
                    }

                    // Setup and Handover data to recyclerview
                    mIntelBinList = (RecyclerView)findViewById(R.id.rvIntelBinList);
                    mAdapter = new IntelBinAdapter(IntelBinView.this, intelBinList);
                    mIntelBinList.setAdapter(mAdapter);
                    mIntelBinList.setLayoutManager(new LinearLayoutManager(IntelBinView.this));

                } catch (JSONException e) {
                    Toast.makeText(IntelBinView.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }




}





