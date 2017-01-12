package genero.dexterous.com.almanac.doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;

public class PrefActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String myData, urlParameters;
    String real1="0",real2="0",real3="0";

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
        setContentView(R.layout.activity_pref);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
            actionBar.setTitle("Doctor's prescription");
        }

        sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);


     final   String email=sharedPreferences.getString("email", "12");
        if(email.equals("12") ){
//            Intent in=new Intent(PrefActivity.this, SignUpActivity.class);
//            in.putExtra("pwd","pref");
//            startActivity(in);
//            finish();
            new HomeActivity().showAlertDialog();
        }

        final EditText prefToEmil= (EditText) findViewById(R.id.prefToMail);
        final EditText edit= (EditText) findViewById(R.id.prefEdit);
        Button btnSend= (Button) findViewById(R.id.sendPref);
        final EditText time1= (EditText) findViewById(R.id.time1);
        final EditText time2= (EditText) findViewById(R.id.time2);
        final EditText time3= (EditText) findViewById(R.id.time3);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


try {
    char[] str1 = time1.getText().toString().toCharArray();
    if(str1.length>1 && Integer.parseInt(time1.getText().toString())<24) {
        if (str1[1] == ':') {
            real1 = String.valueOf(str1[0]);
        } else {
            real1 = String.valueOf(str1[0]) + String.valueOf(str1[1]);
        }
    }else{
        real1= String.valueOf(str1[0]);
    }
}catch (ArrayIndexOutOfBoundsException e){
    e.printStackTrace();
}
               try{ char[] str2=time2.getText().toString().toCharArray();
                   if(str2.length>1 && Integer.parseInt(time2.getText().toString())<24) {

                       if(str2[1]==':'){
                    real2= String.valueOf(str2[0]);
                }else{
                    real2=String.valueOf(str2[0])+String.valueOf(str2[1]);
                }}else{
                       real2= String.valueOf(str2[0]);
                   }}catch (ArrayIndexOutOfBoundsException e){
                   e.printStackTrace();
               }
                try{char[] str3=time3.getText().toString().toCharArray();
                    if(str3.length>1 && Integer.parseInt(time3.getText().toString())<24) {

                        if(str3[1]==':'){
                    real3= String.valueOf(str3[0]);
                }else{
                    real3=String.valueOf(str3[0])+String.valueOf(str3[1]);
                }}else{
                        real3= String.valueOf(str3[0]);
                    }}catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                if(!prefToEmil.getText().toString().isEmpty() ) {
                    urlParameters = "k1=" + email +
                            "&k2=" + prefToEmil.getText().toString() +
                            "&k3=" + edit.getText().toString() +
                            "&k4=" + real1 +
                            "&k5=" + real2 +
                            "&k6=" + real3;
                    Log.e("urlParameter", urlParameters);

                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                    Boolean isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent) {
                        new Abc().execute();

                    } else {
                        showAlertDialog(PrefActivity.this, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }
                }else {
                    Toast.makeText(PrefActivity.this,"Please Enter all the entries"+"\n"+" And time must be less than 24",Toast.LENGTH_LONG).show();
                }
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



    public class Abc extends AsyncTask<String,String, String>{


        @Override
        protected String doInBackground(String... params) {

            try {
                URL url=new URL("http://floridmedicos.in/prescriptions.php");



                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
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


            return myData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(myData.equals("msg:done")){
                Toast.makeText(PrefActivity.this,"your prescription has been send ",Toast.LENGTH_LONG).show();
                Intent in=new Intent(PrefActivity.this, HomeActivity.class);
                startActivity(in);
            }else {
                Toast.makeText(PrefActivity.this,"your prescription is not sent. Please Try again.",Toast.LENGTH_LONG).show();

            }
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
