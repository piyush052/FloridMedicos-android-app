package genero.dexterous.com.almanac.hospitals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.MapActivity;
import genero.dexterous.com.almanac.R;

public class HospitalDetailsActivity extends AppCompatActivity {
    public void changeStatusBarColor(){

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }

        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }






    }

    String state,city;
    HttpURLConnection connection;
    URL url;
    String myData="hello";
    String url1="http://floridmedicos.in/hospital_datails.php";
    String urlParameters;
    ArrayList<String> id;
    ArrayList<String> zone;
    ArrayList<String> h_name;
    ArrayList<String> address;
    ArrayList<String> city_name;
    ArrayList<String> state1;
    ArrayList<String> uhcp_name;
    ArrayList<String> pincode;
    ArrayList<String> std_code;
    ArrayList<String> phone;
    ArrayList<String> mobile;
    ArrayList<String> fax;
    ArrayList<String> email;

    ListView lv;
    ProgressBar pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       lv= (ListView) findViewById(R.id.listView3);
        pr= (ProgressBar) findViewById(R.id.hospProgressBar4);


                state=getIntent().getStringExtra("state");
        city=getIntent().getStringExtra("city");
        try {
            getSupportActionBar().setTitle("Hospital Details in " + city);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        }catch (NullPointerException e){
            e.printStackTrace();
        }



        urlParameters = "k1=" + state + "&k2=" + city;
        Log.e("url parameters",urlParameters);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
        new ABC().execute();
        }else{
            showAlertDialog(HospitalDetailsActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent in=new Intent(HospitalDetailsActivity.this, MapActivity.class);
            //   in.putExtra("key1",h_name.get(position)+" "+address.get(position));
               in.putExtra("key",address.get(position));

               Log.e("sendData",address.get(position));

               startActivity(in);

           }
       });




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void showAlertDialog(final Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent in=new Intent(context, HomeActivity.class);
                startActivity(in);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



    public class ABC extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pr.setVisibility(View.VISIBLE);

            id=new ArrayList<>();
            zone=new ArrayList<>();
            h_name=new ArrayList<>();
            address=new ArrayList<>();
            city_name=new ArrayList<>();
            state1=new ArrayList<>();
            uhcp_name=new ArrayList<>();
            pincode=new ArrayList<>();
            std_code=new ArrayList<>();
            mobile=new ArrayList<>();
            phone=new ArrayList<>();
            fax=new ArrayList<>();
            email=new ArrayList<>();






        }

        @Override
        protected String doInBackground(String... params) {
            try {


                // instantiate the URL object with the target URL of the resource to
                // request
                url = new URL(url1);


                // instantiate the HttpURLConnection with the URL object - A new
                // connection is opened every time by calling the openConnection
                // method of the protocol handler for this URL.
                // 1. This is the point where the connection is opened.
                connection= (HttpURLConnection) url.openConnection();


                // set connection output to true
                connection.setDoOutput(true);


                connection.setDoInput(true);
                // instead of a GET, we're going to send using method="POST"
                //..................................................................................
                connection.setRequestMethod("POST");

                OutputStreamWriter writer = new OutputStreamWriter(
                        connection.getOutputStream());

                // write data to the connection. This is data that you are sending
                // to the server
                // 3. No. Sending the data is conducted here. We established the
                // connection with getOutputStream
                writer.write(urlParameters);

                writer.close();
                //.....................................................................................
                // if there is a response code AND that response code is 200 OK, do
                // stuff in the first if block
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // OK
                    Log.e("piyush", "Everythingh is allright"+connection.getResponseMessage());
                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    Log.e("piyush","something error "+connection.getResponseMessage());
                    // Server returned HTTP error code.
                }
                InputStream is=connection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuffer strbuilder=new StringBuffer();
                String line=null;
                while((line=bufferedReader.readLine())!=null){///errror hre
                    strbuilder.append(line);
                }
                myData=strbuilder.toString();
                Log.e("myData=",myData);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //....................................................................................
            // JSON PARSING>> :)




            if (myData != null || !myData.isEmpty()) {
                try {
                    //  JSONObject jsonObj = new JSONObject(myData);

                    // Getting JSON Array node
                    JSONArray hospital = new JSONArray(myData);



                    // looping through All Contacts
                    for (int i = 0; i < hospital.length(); i++) {
                        JSONObject c = hospital.getJSONObject(i);
                      //  arrayList.add(c.getString("city_name"));
                        id.add(c.getString("id"));
                        h_name.add(c.getString("h_name"));
                        zone.add(c.getString("zone"));
                        address.add(c.getString("address"));
                        city_name.add(c.getString("city_name"));
                        state1.add(c.getString("state"));
                        pincode.add(c.getString("pincode"));
                        std_code.add(c.getString("std_code"));
                        uhcp_name.add(c.getString("uhcp_name"));
                        phone.add(c.getString("telephone"));
                        mobile.add(c.getString("mobile"));
                        fax.add(c.getString("fax"));
                        email.add(c.getString("email"));


                        Log.e("city_name", c.getString("city_name"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (ArrayIndexOutOfBoundsException e){

                }
            } else {

                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pr.setVisibility(View.INVISIBLE);
            Log.e("myData=", myData);
            try {
                if (h_name.get(0) != null) {
                    HospDetail hd = new HospDetail(HospitalDetailsActivity.this, id, h_name, zone, address, city_name, state1, pincode, std_code, uhcp_name, phone, mobile, fax, email);
                    lv.setAdapter(hd);
                    Log.e("adapter", "going");
                }
            } catch (Exception e) {
                lv.setVisibility(View.INVISIBLE);
                TextView tvq = (TextView) findViewById(R.id.hospTextView);
                tvq.setVisibility(View.VISIBLE);
                tvq.setText("There is no hospital present in this area.");

            }
        }
    }



}
