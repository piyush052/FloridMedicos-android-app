package genero.dexterous.com.almanac.discussion;

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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
import genero.dexterous.com.almanac.doctor.PrescriptionForUser;


public class SignUpActivity extends ActionBarActivity {

    EditText name1,password1,email1;
    ImageView mProfileImage;
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
    Boolean b=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(R.string.app_name);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        }

        name1 = (EditText) findViewById(R.id.signUpEditText);
        password1 = (EditText) findViewById(R.id.editText2);
        email1 = (EditText) findViewById(R.id.editText3);
        Button SignUp = (Button) findViewById(R.id.button);


        sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        String str=null;
try {
    str = (getIntent().getExtras().getString("pwd"));
   b= str.equals("pref");
    Log.e("string pwd=", str);
}catch (NullPointerException e){
    e.printStackTrace();
}


        assert str != null;
        if (b) {
            Log.e("going for presLogin", "yep");

            prescriptionSignup();

        } else {

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

            Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
            if (isInternetPresent) {

                mProfileImage = (ImageView) findViewById(R.id.imageView);
                Button facebook = (Button) findViewById(R.id.button2);


                if (ParseUser.getCurrentUser() != null) {

                    Log.e("piyush", ParseUser.getCurrentUser().toString());
                    Intent in = new Intent(SignUpActivity.this, ChatActivity.class);
                    startActivity(in);
                    finish();
                }



                SignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveNewUser();

                    }


                });


            } else {

                showAlertDialog(SignUpActivity.this, "No Internet Connection",
                        "You don't have internet connection.", false);
            }
        }
    }

    private void saveNewUser() {


        if (!((name1.getText().toString().isEmpty()) || password1.getText().toString().isEmpty() || email1.getText().toString().isEmpty())){


            ParseUser user = new ParseUser();
            user.setUsername(name1.getText().toString());
            user.setPassword(password1.getText().toString());
            user.setEmail(email1.getText().toString());

// other fields can be set just like with ParseObject
            user.put("phone", "650-253-0000");

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {

                        Intent in = new Intent(SignUpActivity.this, ChatActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(in);
                        // Hooray! Let them use the app now.

                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong

                        Toast.makeText(SignUpActivity.this, " Sign up didn't succeed. Please check your internet connection.",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        Log.e("piyush", e.toString());
                    }
                }
            });

        }else{
            Toast.makeText(SignUpActivity.this, " Please fill all fields.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
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

                Intent in = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(in);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    String usrName,password,email3;
    String urlParameters;

    public void prescriptionSignup(){

        Log.e("i m in presLogin", "yep");


        Button SignUp = (Button) findViewById(R.id.button);
        assert SignUp != null;
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 usrName= name1.getText().toString();
                 password= password1.getText().toString();
                email3= email1.getText().toString();

                if(!(usrName.isEmpty() || password.isEmpty() || email3.isEmpty())) {

                    urlParameters = "k1=" + usrName + "&" + "k2=" + password + "&k3=" + email3;
                    Log.e("urlparameters", urlParameters);

                    new SignUpPres().execute();
                }else {
                    Toast.makeText(SignUpActivity.this,"Plase fill all the entries",Toast.LENGTH_LONG).show();
                }

            }



        });
    }



    private class SignUpPres extends AsyncTask<String,String,String> {

        String myData="123";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
           URL url=new URL("http://floridmedicos.in/user_details.php");



          HttpURLConnection connection= (HttpURLConnection) url.openConnection();


                // set connection output to true
                connection.setDoOutput(true);
                connection.setDoInput(true);



                connection.setRequestMethod("POST");
                //    connection.addRequestProperty("Cache-Control", "only-if-cached");
                OutputStreamWriter writer = new OutputStreamWriter(
                        connection.getOutputStream());


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
            if((s.equals("1"))) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName",usrName);
                editor.putString("Email", email3);
                editor.putString("type","user");
                editor.apply();

                Intent in = new Intent(SignUpActivity.this, PrescriptionForUser.class);
                startActivity(in);
                finish();
            }else{
                Toast.makeText(SignUpActivity.this,"Signup did not complete TRY again And choose unique user name.",Toast.LENGTH_LONG).show();
            }


        }
    }





    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        //   AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_doctor, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        Log.e("item","selected");

        switch(item.getItemId()){
            case R.id.signUpMenu:

                signIn(b);

                break;




        }
        return true;

    }

    String SignInUrlParameters;

    String signUserName;

    private void signIn(Boolean t) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(SignUpActivity.this);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        final EditText pwd = (EditText) promptsView.findViewById(R.id.pwdHelp);
        userInput.setHint("User name");
        pwd.setHint("password");


        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                                Boolean isInternetPresent = cd.isConnectingToInternet();

                                if (isInternetPresent) {

                                    if (!(userInput.getText().toString().isEmpty() && pwd.getText().toString().isEmpty())) {
                                       signUserName = userInput.getText().toString();
                                        String passwd=pwd.getText().toString();

                                        SignInUrlParameters="k1="+signUserName+"&k2="+passwd;


                                        if(b){

                                            new Abc().execute();


                                        }else{


                                            ParseUser.logInInBackground(signUserName, passwd,
                                                    new LogInCallback() {
                                                        public void done(ParseUser user, ParseException e) {
                                                            if (user != null) {
                                                                // If user exist and authenticated, send user to Welcome.class
                                                                Intent intent = new Intent(SignUpActivity.this, ChatActivity.class);
                                                                startActivity(intent);
                                                                Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                                                                finish();
                                                            } else {
                                                                Toast.makeText(
                                                                        getApplicationContext(),
                                                                        "No such user exist, please signup",
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Please fill all the entries.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    showAlertDialog(SignUpActivity.this, "No Internet Connection",
                                            "You don't have internet connection.", false);
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public class Abc extends AsyncTask<String,String, String> {
        String myData1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("new", "going for netwrok");

        }

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL("http://floridmedicos.in/sign_in.php");


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(SignInUrlParameters);
                Log.e("SignInUrlParameters",SignInUrlParameters);
                writer.close();


                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuffer strbuilder = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    strbuilder.append(line);
                }
                myData1 = strbuilder.toString();


                Log.e("piyush", myData1);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return myData1;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(myData1.equals("doctor is available")){

                SharedPreferences   sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("userName",signUserName);
                editor.putString("Email", "Someoe@gmail.com");
                editor.putString("type","user");
                editor.apply();

                Toast.makeText(SignUpActivity.this,"Login Successful.",Toast.LENGTH_LONG).show();
                Intent in=new Intent(SignUpActivity.this,PrescriptionForUser.class);
                startActivity(in);
                finish();

            }else{
                Toast.makeText(SignUpActivity.this,"Please enter correct user name and password.",Toast.LENGTH_LONG).show();

            }


        }
    }


}
