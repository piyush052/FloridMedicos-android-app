package genero.dexterous.com.almanac.doctor;

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


public class MainActivity extends AppCompatActivity {
    ListView listView;

    String myData="hello";
    CustomAdapter customAdapter;
    HttpURLConnection connection;
    URL url;


    ArrayList<String> first_name;
    ArrayList<String> last_name;
    ArrayList<String> pincode1;
    ArrayList<String> branch ;
    ArrayList<String> resi_address;
    ArrayList<String> id;
    ArrayList<String> phone ;
    ArrayList<String> speciality_1 ;
    ArrayList<String> speciality_2 ;
    ArrayList<String> email;

    String urlParameters;
    static final String url1="http://floridmedicos.in/abcd.php";
    String pincode,speciality;
    TextView tv;
    public void changeStatusBarColor(){

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }






    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Doctor's details");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        tv= (TextView) findViewById(R.id.textView3);
        tv.setVisibility(View.INVISIBLE);

        listView= (ListView) findViewById(R.id.listView);
        pincode=getIntent().getExtras().getString("k2");
        speciality=getIntent().getExtras().getString("k1");
        urlParameters="k1="+speciality+"&k2="+pincode;
        Log.e("URL",urlParameters);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            new  ABC().execute();
        }else{
            showAlertDialog(MainActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in=new Intent(MainActivity.this, MapActivity.class);

                in.putExtra("key",resi_address.get(position)+","+pincode1.get(position));

                Log.e("sendData",resi_address.get(position)+","+pincode1.get(position));
               // in.putExtra("key1",resi_address.get(position) + ","+pincode1.get(position));
                startActivity(in);
            }
        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(MainActivity.this,DoctorActivity.class);
        startActivity(in);
    }
     ProgressBar pr;
    public class ABC extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pr= (ProgressBar) findViewById(R.id.progressBar);
             pr.setVisibility(View.VISIBLE);

            first_name=new ArrayList<>();
             last_name=new ArrayList<>();
             pincode1=new ArrayList<>();
             branch =new ArrayList<>();
             resi_address=new ArrayList<>();
             id=new ArrayList<>();
            phone =new ArrayList<>();
             speciality_1 =new ArrayList<>();
             speciality_2=new ArrayList<>();
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
            //    connection.addRequestProperty("Cache-Control", "only-if-cached");
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
                    Log.e("piyush","Everythingh is allright");
                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    Log.e("piyush","something error ");
                  // new BloodBankActivity().showAlertDialog(MainActivity.this, "No Internet Connection",
                        //    "You don't have an active internet connection.", false);
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
            if (myData != null) {
                try {
                  //  JSONObject jsonObj = new JSONObject(myData);

                    // Getting JSON Array node
                  JSONArray  doctors = new JSONArray(myData);

                    // looping through All Contacts
                    for (int i = 0; i < doctors.length(); i++) {
                        JSONObject c = doctors.getJSONObject(i);

                         id.add(  c.getString("id"));

                        Log.e("id i main Activity",c.getString("id"));

                        if(c.getString("pincode").equals("0")){
                            pincode1.add("NOT AVAILABLE");
                        }else{
                            pincode1.add(c.getString("pincode"));
                        }

                       branch.add(c.getString("branch"));
                        first_name.add("Dr. " + c.getString("first_name"));

                         last_name.add( c.getString("last_name"));
                         speciality_1.add( c.getString("speciality_1"));
                         speciality_2.add( c.getString("speciality_2"));
                         resi_address.add(c.getString("resi_address"));
                        String phone1=c.getString("phone1");
                        if(phone1.equals("0")){
                            phone1="";
                        }
                        String phone2=c.getString("phone2");
                        if(phone2.equals("0")){
                            phone2="";
                        }
                        String phone3=c.getString("phone3");
                        if(phone3.equals("0")){
                            phone3="";
                        }
                        String phone4=c.getString("phone4");
                        if(phone4.equals("0")){
                            phone4="";
                        }
                        String phone5=c.getString("mobile");
                        if(phone5.equals("0")){
                            phone5="";
                        }
                        if(c.getString("email").isEmpty()){
                            email.add("NOT AVAILABLE");
                        }else{
                            email.add(c.getString("email"));
                        }

                        String str =phone1+phone2+phone3+phone4+phone5;
                        if(str.isEmpty()){
                            phone.add("NOT AVAILABLE");
                        }else{
                            phone.add(phone1+" "+phone2+" "+phone3+" "+phone4+" "+phone5);
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error",e.toString());
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.e("error",e.toString());

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
            try {
                if (first_name.get(0) != null) {
                    tv.setVisibility(View.INVISIBLE);

                    customAdapter = new CustomAdapter(MainActivity.this, first_name, last_name, pincode1, speciality_1, speciality_2, branch, phone, id, resi_address, email);
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e) {
                tv.setVisibility(View.VISIBLE);
                tv.setText("THERE IS NO DOCTOR PRESENT IN THIS AREA.");
                tv.setTextColor(Color.parseColor("#000000"));
            }


        }
    }
}
