package pt.uma.mediaplayer_ssui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static pt.uma.mediaplayer_ssui.IntelBinView.CONNECTION_TIMEOUT;
import static pt.uma.mediaplayer_ssui.IntelBinView.READ_TIMEOUT;

/**
 * Created by ldin6 on 07/05/2017.
 */

public class IntelBinAdapter extends RecyclerView.Adapter<IntelBinAdapter.ViewHolder>  {


    //Variables
    private List<IntelBin> mIntelBins;
    private Context mContext;
    private int problemCounter=0;
    public String text;

    public void probCounter(){
        problemCounter++;
    }

    public IntelBinAdapter(Context mContext, List<IntelBin> mIntelBins) {
        this.mIntelBins = mIntelBins;
        this.mContext = mContext;

    }


    private Context getContext() {
        return mContext;
    }



    @Override
    public IntelBinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate o layout custom
        View IntelBinView = inflater.inflate(R.layout.layoutrow, parent, false);

        //Retorns a new ViewHolder
        ViewHolder viewHolder = new ViewHolder(IntelBinView);
        return viewHolder;
    }


        /*
        //Searching based on song name or artist
        public void filter(String text){
            mIntelBins.clear();
            if(text.isEmpty())
            {
                mIntelBins.addAll(mMultimediasCopy);// Copy back in case searchtext is empty
            }
            else
            {
                text.toLowerCase();
                for(IntelBin aIntelBin :mMultimediasCopy)
                {
                    String name = aIntelBin.getName();
                    String artist = aIntelBin.getArtist();
                    if(name.equalsIgnoreCase(text)||artist.equalsIgnoreCase(text)){
                        mIntelBins.add(aIntelBin);
                    }
                }
            }
            notifyDataSetChanged();
        }
    */

    @Override
    public void onBindViewHolder(IntelBinAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final IntelBin intelBin = mIntelBins.get(position);

        // Set item views based on the  views and data model

        TextView textView1 = viewHolder.intelBinNameView;
        textView1.setText(intelBin.getName());
        ImageView imageBuilding =viewHolder.intelBinBuilding;
        TextView intelBinFloor =viewHolder.intelBinFloor;
        intelBinFloor.setText(intelBin.getFloor());
        TextView  intelBinLocation =viewHolder.intelBinLocation;
        intelBinLocation.setText(intelBin.getDescription());
        TextView  intelBinLevel =viewHolder.intelBinLevel;
        intelBinLevel.setText(Integer.toString(intelBin.getLevel()));
        Button mSelected = viewHolder.btn_submit_level;

        final EditText editText = viewHolder.text_problem;


        mSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 text = editText.getText().toString();

                AsyncTask newTask =  new AsyncPost().execute();
                editText.setText("");
                probCounter();

            }
        });

    }


    @Override
    public int getItemCount() {
        return mIntelBins.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView intelBinNameView;
        public ImageView intelBinBuilding;
        public TextView  intelBinFloor;
        public TextView intelBinLocation;
        public TextView intelBinLevel;
        public Button btn_submit_level;
        public EditText text_problem;
        public TextView label_problem;
        public CheckBox selectionBox;


        public ViewHolder(View itemView)
        {
            super(itemView);
            intelBinNameView = (TextView) itemView.findViewById(R.id.inteBin_name);
            intelBinBuilding= (ImageView) itemView.findViewById(R.id.intelBin_building);
            intelBinFloor  =    (TextView) itemView.findViewById(R.id.intelBin_floor);
            intelBinLocation =   (TextView) itemView.findViewById(R.id.intelBin_location);
            intelBinLevel = (TextView) itemView.findViewById(R.id.intelBin_level);
            btn_submit_level =  (Button) itemView.findViewById(R.id.btn_prob);
            text_problem =       (EditText) itemView.findViewById(R.id.problem_input);
            label_problem =      (TextView) itemView.findViewById(R.id.problem_detect);


        }


    }

    private class AsyncPost extends AsyncTask<String, String, String> {

        HttpURLConnection conn;

        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://192.168.2.130:8080/intelbins/problem/1/4/1/"+text);

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
                conn.setRequestMethod("POST");


                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

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


        }

    }

}