package genero.dexterous.com.almanac.medical;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.MapActivity;
import genero.dexterous.com.almanac.R;

public class medicalActivity extends ActionBarActivity {
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

    String myData;
ProgressBar pr;
    MedicalList md;
    static final String url1="http://floridmedicos.in/medical.php";
    ArrayList<String> name;
    ArrayList<String> address;
    ArrayList<String> contact;
    HttpURLConnection connection;
    URL url;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
      lv= (ListView) findViewById(R.id.listView2);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Medical's details in DELHI NCR");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

        }


        name=new ArrayList<>();
        address=new ArrayList<>();
        contact=new ArrayList<>();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
        new ABC().execute();
        }else{
            showAlertDialog(medicalActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in=new Intent(medicalActivity.this, MapActivity.class);
             //   in.putExtra("key1",name.get(position)+" "+address.get(position));
                in.putExtra("key",address.get(position));
                Log.e("sendData",address.get(position));

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


    public class ABC extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pr= (ProgressBar) findViewById(R.id.progressBar2);
            pr.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url=new URL(url1);



            connection= (HttpURLConnection) url.openConnection();


            // set connection output to true
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.getUseCaches();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.e("piyush", "Everythingh is allright");

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
                Log.e("piyush", myData);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //JsonPArsing..........
            if(myData!=null){
                try {
                    JSONArray blood=new JSONArray(myData);





                    for(int i=0; i<blood.length();i++){
                        JSONObject c = blood.getJSONObject(i);
                        name.add(c.getString("COL 1"));
                        Log.e("piyush",c.getString("COL 2"));
                        // fatch other attribute from here
                        address.add(c.getString("COL 2"));

                        contact.add(c.getString("COL 3"));



                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return myData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pr.setVisibility(View.INVISIBLE);

            md=new MedicalList(medicalActivity.this,contact,address,name);
            lv.setAdapter(md);

        }
    }



    }
