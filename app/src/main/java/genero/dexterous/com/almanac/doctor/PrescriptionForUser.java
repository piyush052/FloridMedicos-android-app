package genero.dexterous.com.almanac.doctor;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.Calendar;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;
import genero.dexterous.com.almanac.discussion.SignUpActivity;

public class PrescriptionForUser extends AppCompatActivity {
  String myData,urlParameters;
    ProgressBar pr;
    TextView tv;
    SharedPreferences sharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_for_user);


        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle("User Prescription");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

        }

       sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);


        String email=sharedPreferences.getString("email", "12");
        final String userName=sharedPreferences.getString("userName","hello");
        if(email.equals("12")  && userName.equals("hello")){
            Intent in=new Intent(PrescriptionForUser.this, SignUpActivity.class);
            in.putExtra("pwd","pref");
            startActivity(in);
            finish();
        }




        pr= (ProgressBar) findViewById(R.id.progressBarPresc);

         tv= (TextView) findViewById(R.id.presList);


         sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);

        String presc=sharedPreferences.getString("presc",null);
        String docName=sharedPreferences.getString("docName",null);
        if(presc==null && docName==null){


        String usrName=sharedPreferences.getString("userName", "hello");

        if(!(usrName.equals("hello"))){
            urlParameters="k1="+usrName;
            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                new  Abc().execute();

            }else{
                showAlertDialog(PrescriptionForUser.this, "No Internet Connection",
                        "You don't have internet connection.", false);
            }        }}else{
            pr.setVisibility(View.INVISIBLE);
            tv.setText("Doctor id-"+docName+"\n"
            +"prescription-"+presc);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(PrescriptionForUser.this,HomeActivity.class);
        startActivity(in);
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



    public class Abc extends AsyncTask<String,String, String> {

        String docName="Not Available",presc="Not Available";
        int time []=new int[3];

        SharedPreferences sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pr.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {




            try {
                URL url=new URL("http://floridmedicos.in/userPrescriptions.php");



                HttpURLConnection connection= (HttpURLConnection) url.openConnection();


                // set connection output to true
                connection.setDoOutput(true);
                connection.setDoInput(true);



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

            if (myData!=null) {
                JSONArray doctors = null;
                try {
                    doctors = new JSONArray(myData);


                    JSONObject c = doctors.getJSONObject(doctors.length() - 1);


                    docName = c.getString("doctor_id");
                    presc = c.getString("prescription");

                    editor.putString("docName",docName);
                    editor.putString("presc",presc);
                    editor.apply();
                    try {
                        time[0] = Integer.parseInt(c.getString("time1"));
                        time[1] = Integer.parseInt(c.getString("time2"));
                        time[2] = Integer.parseInt(c.getString("time3"));

                        Log.e("time",""+time[0]);
                        Log.e("time",""+time[1]);

                        Log.e("time",""+time[2]);

                    }catch (NumberFormatException e){
                        Log.e("error",e.toString());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                presc="there is no any prescription";
                docName="not available";
            }



            return myData;
        }


        int x;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pr.setVisibility(View.INVISIBLE);
            docName=sharedPreferences.getString("docName","tmc");
            presc=sharedPreferences.getString("presc","tmc");

            if (docName.equals("tmc") || presc.equals("tmc")) {
//                docName = "not available";
//                presc = "not available";
                tv.setText("No prescription available for you. " +
                        "Contact to the doctor through doctor details module.");

            }else{
                showNotification();

                for (int i = 0; i < time.length; i++) {

                    x = time[i];
                    Log.e("time in for loop",""+time[i]);

                    if(x!=0) {


                            Log.e("time in for loop", "" + x);
                            Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, presc);
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, 1);
                            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, x);
                            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
                            alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                            PrescriptionForUser.this.startActivity(alarmIntent);



                    }



                }

                tv.setText("Doctor id-" + docName + "\n \n"
                        + "prescription-" + presc);


            }
        }
        public void showNotification(){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setSmallIcon(R.drawable.logo);
            builder.setContentTitle("You have one prescription");
            builder.setContentText(presc);
            Intent intent = new Intent(PrescriptionForUser.this,PrescriptionForUser.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        Log.e("item","selected");

        switch(item.getItemId()){
            case R.id.logoutfor:

                SharedPreferences   sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent in=new Intent(this, HomeActivity.class);
                startActivity(in);
                finish();
                break;




        }
        return true;

    }

}
