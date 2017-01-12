package genero.dexterous.com.almanac.Home;


import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

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
import java.util.HashMap;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.R;
import genero.dexterous.com.almanac.bloodBank.BloodBankActivity;
import genero.dexterous.com.almanac.discussion.SignUpActivity;
import genero.dexterous.com.almanac.doctor.DoctorActivity;
import genero.dexterous.com.almanac.doctor.PrefActivity;
import genero.dexterous.com.almanac.doctor.PrescriptionForUser;
import genero.dexterous.com.almanac.graph.GraphActivity;
import genero.dexterous.com.almanac.help.HelpActivity;
import genero.dexterous.com.almanac.hospitals.HospitalActivity;
import genero.dexterous.com.almanac.medical.medicalActivity;


public class HomeActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    public void permissionCheck() {

        Log.e("permission check", "i m in");


        if ((int) Build.VERSION.SDK_INT > 22) {


            // Assume thisActivity is the current activity
            int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            // Assume thisActivity is the current activity
            int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }


        }
    }

    public void changeStatusBarColor() {

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    Context context;
    SliderLayout mDemoSlider;
    GridAdapter gridAdapter;

    String str[] = {"Doctors", "Medicals", "Hospitals", "BloodBanks", "Discussion Forum", "Prescriptions", "Analysis", "Help"};
    int images[] = {R.drawable.med_doc, R.drawable.medical_case, R.drawable.hospital, R.drawable.bloodlogo, R.drawable.jabber_group, R.drawable.note, R.drawable.bars, R.drawable.help};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        permissionCheck();
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        context = this;
        setUpCarousel(this);
        GridView gv = (GridView) findViewById(R.id.gridView);

        gridAdapter = new GridAdapter(HomeActivity.this, str, images);
        gv.setAdapter(gridAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent in = new Intent(HomeActivity.this, DoctorActivity.class);
                        startActivity(in);
                        break;
                    case 1:
                        Intent in1 = new Intent(HomeActivity.this, medicalActivity.class);
                        startActivity(in1);
                        ;
                        break;

                    case 2:
                        Intent in2 = new Intent(HomeActivity.this, HospitalActivity.class);
                        startActivity(in2);
                        ;
                        break;

                    case 3:
                        Intent i3n = new Intent(HomeActivity.this, BloodBankActivity.class);
                        startActivity(i3n);
                        break;

                    case 4:
                        Intent in3 = new Intent(HomeActivity.this, SignUpActivity.class);
                        startActivity(in3);
                        break;

                    case 5:
                        SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);


                        String email = sharedPreferences.getString("email", "12");
                        String type = sharedPreferences.getString("type", "tmc");
                        if (email.equals("12")) {
                            showAlertDialog();
                        } else {

                            if (type.equals("user")) {
                                Intent in4 = new Intent(HomeActivity.this, PrescriptionForUser.class);
                                startActivity(in4);
                            } else if (type.equals("doctor")) {
                                Intent in4 = new Intent(HomeActivity.this, PrefActivity.class);
                                startActivity(in4);
                            }

                        }
                        break;
                    case 6:
                        Intent in5 = new Intent(HomeActivity.this, GraphActivity.class);
                        startActivity(in5);
                        break;

                    case 7: //Toast.makeText(HomeActivity.this,"Under processing",Toast.LENGTH_LONG).show();
                        Intent in6 = new Intent(HomeActivity.this, HelpActivity.class);
                        startActivity(in6);
                        break;

                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        String type = sharedPreferences.getString("type", "tmc");
        if (type.equals("user")) {
            String usrName = sharedPreferences.getString("userName", "hello");

            if (!(usrName.equals("hello"))) {
                urlParameters = "k1=" + usrName;
                Log.e("urlParameters", urlParameters);
                new Abc().execute();
            }
        }


    }

    private static HomeActivity inst;

    public static HomeActivity instance() {
        return inst;
    }

    private void setUpCarousel(Context context) {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Doctors Details", R.drawable.doctordetails);
        file_maps.put("Blood Banks", R.drawable.bloodbank);
        file_maps.put("Join the discussion", R.drawable.discussionforum);
        file_maps.put("Medical details", R.drawable.medicaldetails);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        try {

            if (slider.getBundle().get("extra").equals("Doctors Details")) {
                Intent in = new Intent(HomeActivity.this, DoctorActivity.class);
                startActivity(in);
            }

            if (slider.getBundle().get("extra").equals("Blood Banks")) {
                Intent in = new Intent(HomeActivity.this, BloodBankActivity.class);
                startActivity(in);
            }
            if (slider.getBundle().get("extra").equals("Medical details")) {
                Intent in = new Intent(HomeActivity.this, medicalActivity.class);
                startActivity(in);
            }
            if (slider.getBundle().get("extra").equals("Join the discussion")) {
                Intent in = new Intent(HomeActivity.this, SignUpActivity.class);
                startActivity(in);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    String myData;
    String urlParameters;
    String docName = null, presca = null;

    public class Abc extends AsyncTask<String, String, String> {


        int time[] = new int[3];
        SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("new", "going for netwrok");

        }

        @Override
        protected String doInBackground(String... params) {

            URL url = null;


            try {
                if (doctor == 1) {
                    url = new URL("http://floridmedicos.in/doctorVarification.php");
                    urlParameters = "k1=" + doctorEmail + "&k2=";
                    Log.e("urlParameters", urlParameters);

                } else {
                    url = new URL("http://floridmedicos.in/userPrescriptions.php");
                }


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

            if (myData != null && (!myData.equals("doctor is available"))) {
                JSONArray doctors = null;
                try {
                    doctors = new JSONArray(myData);


                    JSONObject c = doctors.getJSONObject(doctors.length() - 1);


                    docName = c.getString("doctor_id");
                    presca = c.getString("prescription");
                    try {
                        time[0] = Integer.parseInt(c.getString("time1"));
                        time[1] = Integer.parseInt(c.getString("time2"));
                        time[2] = Integer.parseInt(c.getString("time3"));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    editor.putString("docName", docName);
                    editor.putString("presc", presca);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return myData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (doctor == 1) {

                if (myData.equals("doctor is available")) {

                    SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", doctorEmail);
                    editor.putString("type", "doctor");
                    editor.apply();
                    Intent in = new Intent(HomeActivity.this, PrefActivity.class);
                    startActivity(in);


                } else {
                    Toast.makeText(HomeActivity.this, "Your email is not available in database please contact with us", Toast.LENGTH_LONG).show();
                }
            } else {
                int x;

                if (presca != null) {
                    for (int i = 0; i < time.length; i++) {


                        x = time[i];
                        if (x != 0) {
                            showNotification();

                            Log.e("time in for loop", "" + x);
                            Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, sharedPreferences.getString("presc", "tmc"));
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, 1);
                            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, x);
                            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
                            alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                            HomeActivity.this.startActivity(alarmIntent);


                        }

                    }
                }


            }
        }

    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("You have one prescription");
        builder.setContentText(presca);
        Intent intent = new Intent(HomeActivity.this, PrescriptionForUser.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }

    public void showAlertDialog() {
        //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(context).create();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Verification");

        // Setting Dialog Message
        alertDialog.setMessage("Are you a Doctor?");

        // Setting alert dialog icon
//        alertDialog.setIcon((false) ? R.drawable.success : R.drawable.fail);

//        // Setting OK Button
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                doctorVarification();
//            }
//        });
//        alertDialog.setButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//                Intent in4 = new Intent(HomeActivity.this, PrescriptionForUser.class);
//                startActivity(in4);
//
//            }
//        });
        // set dialog message
        alertDialog
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                doctorVarification();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent in4 = new Intent(HomeActivity.this, PrescriptionForUser.class);
                                startActivity(in4);
                            }
                        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void doctorVarification() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //   Toast.makeText(HomeActivity.this,userInput.getText().toString(),Toast.LENGTH_LONG).show();
                                doctor = 1;
                                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                                Boolean isInternetPresent = cd.isConnectingToInternet();

                                if (isInternetPresent) {

                                    if (!userInput.getText().toString().isEmpty()) {
                                        doctorEmail = userInput.getText().toString();
                                        new Abc().execute();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "Please enter correct email. Or check your Internet connection", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    showAlertDialog(HomeActivity.this, "No Internet Connection",
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

    int doctor = 0;
    String doctorEmail = null;

    private static long back_pressed;

    @Override
    public void onBackPressed() {


        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(HomeActivity.this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }


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

}


