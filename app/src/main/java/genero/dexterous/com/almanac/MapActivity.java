package genero.dexterous.com.almanac;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import genero.dexterous.com.almanac.Home.HomeActivity;

public class MapActivity extends AppCompatActivity {
     static double lat=0.0;
    public GoogleMap map;
    public MapFragment mp;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;

    static  double lon=0.0;


    String str1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        String str=getIntent().getExtras().getString("key");
             str1=getIntent().getExtras().getString("key1");




        Log.e("intent string=", str);

        permissionCheck();

      //  gotLoc(str);

        gotLocation(str,str1);


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false

        if (isInternetPresent) {

            FragmentManager fm=getFragmentManager();


            mp = (MapFragment) fm.findFragmentById(R.id.fragmentMap1);
            map = mp.getMap();


            if (map != null ) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    map.setMyLocationEnabled(true);
                    map.clear();
                    moveMapToMyLocation();



                   if(lat!=0.0 && lon!=0.0) {
                       map.addCircle(new CircleOptions()
                               .center(new LatLng(lat, lon))
                               .radius(1000)
                               .strokeColor(Color.parseColor("#e57373"))
                               .fillColor(Color.parseColor("#5486F6F3")));
                   }else{

                       showAlertDialog(MapActivity.this, "Sorry there is no data for this location",
                               "You can go for google Search ", false);

                   }


                }

            } else {
                permissionCheck();
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
        alertDialog.setIcon( R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent in = new Intent(context, HomeActivity.class);
                startActivity(in);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void moveMapToMyLocation() {




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            CameraPosition camPos = new CameraPosition.Builder()

                    .target(new LatLng(lat, lon))

                    .zoom(12.8f)

                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            map.moveCamera(camUpdate);
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(str1));

            Toast.makeText(this,"your location is in the shaded circle",Toast.LENGTH_LONG).show();
        }


    }


    public  void gotLocation(String string ,String str1 ){
        Geocoder gc=new Geocoder(this);
        Log.e("piyush", string);
        String demo=string;
       // Log.e("address",addre);
           String array[]=new String[]{};

        if (gc.isPresent()) {
            List<Address> list ;
            Address address ;

            for (int i=0;i<4;i++) {

                try {
                    list = gc.getFromLocationName(demo, 10);

                    Log.e("length=", "" + list.size());

                    // if (list != null) {
                    address = list.get(0);
                    lat = address.getLatitude();
                    lon = address.getLongitude();



                    Log.e("latiude and lon=", "" + lat + "  " + lon);


                    if(list.size()>0){
                        break;
                    }

                } catch (Exception e) {
                   // string=addre;
                   // Log.e("address=",string);
                    Log.e("piyush", "exception in post method" + e.toString());
                    assert demo != null;
                    array=demo.split(",");
                    Log.e("length=", "" + array.length);


                  StringBuilder sb=new StringBuilder();

                    for(int d=1;d<array.length;d++) {
                        sb.append(array[d]);
                        Log.e("piyush", sb.toString());

                    }
                    demo=sb.toString();
                    Log.e("piyush", demo);

                    e.printStackTrace();
                }
            }

        }
        try {
            if (map != null && lat!=0.0) {
                map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(str1));

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }



    }

    public void permissionCheck() {


        if ((int) Build.VERSION.SDK_INT > 22)
        {


            // Assume thisActivity is the current activity
            int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            // Assume thisActivity is the current activity
            int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if(permissionCheck1==PackageManager.PERMISSION_GRANTED  && permissionCheck2==PackageManager.PERMISSION_GRANTED) {


            }else{

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }



        }
    }
//  public void gotLoc(String addr){
//
//      Geocoder coder=new Geocoder(this);
//      List<Address> addresses;
//      try {
//          addresses=coder.getFromLocationName(addr,5);
//            if((addresses !=null && !addresses.isEmpty())){
//                Address location=addresses.get(0);
//              lat=  location.getLatitude();
//                lon=location.getLongitude();
//                map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(addr));
//
//            }
//
//
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//
//  }


}
