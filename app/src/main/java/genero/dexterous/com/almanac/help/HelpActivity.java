package genero.dexterous.com.almanac.help;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
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
import genero.dexterous.com.almanac.R;

public class HelpActivity extends AppCompatActivity{
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

    String docEmail,desc;
    String myData,urlParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle("Help");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

        }
        TextView doc= (TextView) findViewById(R.id.helpDoc);
        TextView abtUs= (TextView) findViewById(R.id.aboutUs);
        TextView contUs= (TextView) findViewById(R.id.contactUs);
        TextView abtApp= (TextView) findViewById(R.id.aboutAppl);
        assert contUs != null;
        contUs.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showAlertDialog("Contact us",null);
           }
       });

        assert abtApp != null;
        abtApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("About App",null);

            }
        });

        assert abtUs != null;
        abtUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("About us",null);

            }
        });
        assert doc != null;
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog("If Yes","If you are a doctor then CLICK OK" +
                        " and submit the detail." +
                        "\nWe will contact you soon and " +
                        "after verification " +
                        "you can give prescriptions and valuable " +
                        "advice to patients through this APP.");

            }
        });




    }



    public void showAlertDialog(final String title, String message) {
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(context).create();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HelpActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);


        alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                  if(title.equals("If Yes")){
                      doctorRequest();

                  }else {
                      dialog.cancel();
                  }
                            }
                        });


        alertDialog.show();
    }

    private void doctorRequest() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(HelpActivity.this);
        View promptsView = li.inflate(R.layout.help_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpActivity.this);

        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.helpDocEmail);
        final  EditText docEdit= (EditText) promptsView.findViewById(R.id.docEditHelp);
        docEdit.setVisibility(View.VISIBLE);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                                Boolean isInternetPresent = cd.isConnectingToInternet();

                                if (isInternetPresent) {

                                //   Toast.makeText(HomeActivity.this,userInput.getText().toString(),Toast.LENGTH_LONG).show();
                                if(!(userInput.getText().toString().isEmpty() && docEdit.getText().toString().isEmpty())) {
                                    docEmail = userInput.getText().toString();
                                    desc=docEdit.getText().toString();
                                    urlParameters="k1="+docEmail+"&k2="+desc;
                                   new Abc().execute();
                                }else{
                                    Toast.makeText(HelpActivity.this,"Please fill all entries.",Toast.LENGTH_LONG).show();

                                }}else {
                                    showAlertDialog(HelpActivity.this, "No Internet Connection",
                                            "You don't have internet connection.", false);
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

                Intent in=new Intent(context, HelpActivity.class);
                startActivity(in);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public class Abc extends AsyncTask<String,String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("new", "going for netwrok");

        }

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL("http://floridmedicos.in/doc_desc.php");


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(urlParameters);
                writer.close();


                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuffer strbuilder = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {///errror hre
                    strbuilder.append(line);
                }
                myData = strbuilder.toString();


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
            if(myData.equals("1")){
                Toast.makeText(HelpActivity.this,"Your request is submitted. We will contact you soon.",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(HelpActivity.this,"your request is not submit. Check your Internet Connection.",Toast.LENGTH_LONG).show();

            }


        }
    }
}
