package genero.dexterous.com.almanac.bloodBank;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.List;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.MapActivity;
import genero.dexterous.com.almanac.R;


public class BloodBankActivity extends ActionBarActivity {
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

    public  GoogleMap map;
    Location location;

    String myData;
   public FragmentManager fm;
   public MapFragment mp;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;

       ListView ls;

    static final String url1 = "http://floridmedicos.in/blood_bank.php";
    ArrayList<String> name;
    ArrayList<String> b_address;
    ArrayList<String> contact;
    HttpURLConnection connection;
    URL url;
    ImageButton list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

         ls= (ListView) findViewById(R.id.bloodList);
        list= (ImageButton) findViewById(R.id.imageButton);

        list.setVisibility(View.INVISIBLE);

              permissionCheck();


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {


            name = new ArrayList<>();
            b_address = new ArrayList<>();
            contact = new ArrayList<>();


             fm = getFragmentManager();
             mp = (MapFragment) fm.findFragmentById(R.id.fragment);
            map = mp.getMap();

            if (map != null) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setZoomGesturesEnabled(true);

                    AppLocationService appLocationService = new AppLocationService(getApplicationContext());

                    location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);


                    moveMapToMyLocation();


                    try {


                        map.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                .radius(10000)
                                .strokeColor(Color.parseColor("#FEFEFE"))
                                .fillColor(Color.parseColor("#5486F6F3")));


                    }catch (NullPointerException e){
                        Log.e("null pointer exception","find");
                    }
                }

            } else {
                   permissionCheck();
            }
            new DetailsOfBloodBank().execute();


        }else{
            showAlertDialog(BloodBankActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

         list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
               mp.getView().setVisibility(View.INVISIBLE);
                 android.support.v7.app.ActionBar actionBar=getSupportActionBar();
                    actionBar.show();
                actionBar.setTitle("BloodBank's details");
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }


                ls.setVisibility(View.VISIBLE);
                ls.setAdapter(new CustomMapAdapter(BloodBankActivity.this, name, b_address, contact));
            }
        });
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(BloodBankActivity.this, MapActivity.class);
                in.putExtra("key",b_address.get(position));
                Log.e("SendData",b_address.get(position));
                startActivity(in);
            }
        });
    }

    public void permissionCheck() {


        if ((int) Build.VERSION.SDK_INT > 22)
        {


            // Assume thisActivity is the current activity
            int permissionCheck1 = ContextCompat.checkSelfPermission(BloodBankActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            // Assume thisActivity is the current activity
            int permissionCheck2 = ContextCompat.checkSelfPermission(BloodBankActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if(permissionCheck1==PackageManager.PERMISSION_GRANTED  && permissionCheck2==PackageManager.PERMISSION_GRANTED) {


            }else{

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }



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

    private void moveMapToMyLocation() {





        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {




           if (location!=null){
            CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))

                    .zoom(10.8f)

                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            map.moveCamera(camUpdate);}
             }






    }




ProgressBar pr;

    private class DetailsOfBloodBank extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.setVisibility(View.INVISIBLE);
            pr= (ProgressBar) findViewById(R.id.progressBar3);
            pr.setVisibility(View.VISIBLE);
            Toast.makeText(BloodBankActivity.this,"Please wait it takes some time",Toast.LENGTH_LONG).show();

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
                    // OK
                    Log.e("piyush", "Everythingh is allright");
                    // otherwise, if any other status code is returned, or no status
                    // code is returned, do stuff in the else block
                } else {
                    Log.e("piyush","something error ");
                    // Server returned HTTP error code.
                    showAlertDialog(BloodBankActivity.this, "No Internet Connection",
                            "You don't have an active internet connection.", false);
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
                        name.add(c.getString("name"));
                        Log.e("piyush",c.getString("name"));
                        // fatch other attribute from here
                        b_address.add(c.getString("address"));

                        contact.add(c.getString("contact_numbers"));



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

            list.setVisibility(View.VISIBLE);


            Toast.makeText(BloodBankActivity.this,"For more convinent, go for LISTVIEW",Toast.LENGTH_LONG).show();

            double lat=0.0,lng=0.0;
            Geocoder gc=new Geocoder(BloodBankActivity.this);


            for(int i=0;i<b_address.size();i=i+2) {
                if (gc.isPresent()) {
                    List<Address> list = null;
                    try {
                        Log.e("piyush",name.get(i) +" "+b_address.get(i));
                        list = gc.getFromLocationName(b_address.get(i), 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {

                        Address address = list.get(0);
                         lat = address.getLatitude();
                         lng = address.getLongitude();


                    }catch (Exception e){
                        Log.e("piyush","exception in post method"+e.toString());
                        e.printStackTrace();
                    }

                }

                if(map!=null && lat!=0.0){
                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name.get(i) +" "+ b_address.get(i) + contact.get(i)));

                }

            }

            pr.setVisibility(View.INVISIBLE);
           // list.setVisibility(View.VISIBLE);


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

                    Toast.makeText(BloodBankActivity.this,"permission not granted",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
