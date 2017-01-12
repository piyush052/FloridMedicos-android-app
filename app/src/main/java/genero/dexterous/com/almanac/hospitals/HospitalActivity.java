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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;

public class HospitalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public void changeStatusBarColor(){

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_green));
        }






    }

    Spinner spinner;
    AutoCompleteTextView autoCompleteTextView;
    String stateName=null;
    String district=null;
    HttpURLConnection connection;
    URL url;

    ArrayList<String> arrayList;
    static final String url1="http://floridmedicos.in/hospitals.php";


    String urlParameters=null;
    String myData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle("Fill the details");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

        }


        spinner= (Spinner) findViewById(R.id.spinner2);

        autoCompleteTextView= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        arrayList=new ArrayList<String>();
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!autoCompleteTextView.getText().toString().isEmpty()){
                    Intent in=new Intent(HospitalActivity.this,HospitalDetailsActivity.class);
                    in.putExtra("state",stateName);
                    in.putExtra("city",district);
                    startActivity(in);
                    return true;

                }else {
                    Toast.makeText(HospitalActivity.this,"Enter the city name",Toast.LENGTH_LONG).show();
                    return false;

                }
            }
        });


        Button search1= (Button) findViewById(R.id.button3);

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!autoCompleteTextView.getText().toString().isEmpty()){
                    Intent in=new Intent(HospitalActivity.this,HospitalDetailsActivity.class);
                    in.putExtra("state",stateName);
                    in.putExtra("city",district);
                    startActivity(in);
                }else {
                    Toast.makeText(HospitalActivity.this,"Enter the city name",Toast.LENGTH_LONG).show();
                }
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            String afterTextChanged = "";
            String beforeTextChanged = "";
            String onTextChanged = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onTextChanged = autoCompleteTextView.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                beforeTextChanged = autoCompleteTextView.getText().toString();

                if (beforeTextChanged.length() >= 2) {
                    district = beforeTextChanged;
                    urlParameters = "k1=" + stateName + "&k2=" + district;
                //    Toast.makeText(HospitalActivity.this, urlParameters, Toast.LENGTH_LONG).show();
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                    Boolean isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent ) {

                        new ABC().execute();
                    }else{
                        showAlertDialog(HospitalActivity.this, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }
                   // arrayList.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //   Toast.makeText(HospitalActivity.this, "before: " + beforeTextChanged
                //         + '\n' + "on:  urlParameters="k1="+stateName+"&k2="+district;" + onTextChanged
                //       + '\n' + "after: " + afterTextChanged
                //     ,Toast.LENGTH_SHORT).show();

            }
        });



        Button search= (Button) findViewById(R.id.button3);

        String state []= getResources().getStringArray(R.array.state);
        Arrays.sort(state);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HospitalActivity.this,
                android.R.layout.simple_spinner_item, state);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(HospitalActivity.this);


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             stateName= (String) parent.getItemAtPosition(position);
       // Toast.makeText(HospitalActivity.this,stateName,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class ABC extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           // Toast.makeText(HospitalActivity.this,"I M In Async task",Toast.LENGTH_LONG).show();



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
                    Log.e("piyush", "Everythingh is allright");
                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    Log.e("piyush","something error ");
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


            if (myData != null) {
                try {
                    //  JSONObject jsonObj = new JSONObject(myData);

                    // Getting JSON Array node
                    JSONArray hospital = new JSONArray(myData);

                    arrayList.clear();

                    // looping through All Contacts
                    for (int i = 0; i < hospital.length(); i++) {
                        JSONObject c = hospital.getJSONObject(i);
                  arrayList.add(c.getString("city_name"));
                        Log.e("city_name",c.getString("city_name"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (ArrayIndexOutOfBoundsException e){

                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }






            //.........................................................................................

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //.................


//..............................................................................................
            // AUTOCOMPLETE TEXT VIEW
          // String[] countries = getResources().getStringArray(R.array.state);


            ArrayAdapter<String>tAadapter = new ArrayAdapter<String>(HospitalActivity.this,android.R.layout.simple_list_item_1,arrayList);

           // tAadapter.notifyDataSetChanged();
            autoCompleteTextView.setAdapter(tAadapter);


            Log.e("piyush","adapter is set");

//..............................................................................................


        }
    }



}
