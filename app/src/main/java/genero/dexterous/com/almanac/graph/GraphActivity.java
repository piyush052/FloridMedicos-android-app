package genero.dexterous.com.almanac.graph;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import java.util.Random;

import genero.dexterous.com.almanac.ConnectionDetector;
import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;

public class GraphActivity extends AppCompatActivity {
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


    ProgressBar pr;
    String myData;
    ListView lv;
    String nameArray[]=new String[15];
    String array[]=new String[15];
    float[] values={700f, 400f, Float.valueOf(100), Float.valueOf(500), Float.valueOf(600)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        try{
            android.support.v7.app.ActionBar actionBar=getSupportActionBar();
            actionBar.hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        pr= (ProgressBar) findViewById(R.id.graphProgressBar);

         lv= (ListView) findViewById(R.id.graphList);
        //nameArray =getResources().getStringArray(R.array.doctorSpeciality);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
        new  ABC().execute();

        }else{
            showAlertDialog(GraphActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        LinearLayout lv1 = (LinearLayout) findViewById(R.id.linear);

        values = calculateData(values);
        MyGraphview graphview = new MyGraphview(GraphActivity.this, values);
        lv1.addView(graphview);

    }

    public void showAlertDialog(final Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent in=new Intent(context, HomeActivity.class);
                startActivity(in);
            }
        });
        alertDialog.show();
    }


    public class ABC extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pr.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL     url=new URL("http://floridmedicos.in/graph.php");
                HttpURLConnection    connection= (HttpURLConnection) url.openConnection();
                // set connection output to true
                connection.setDoOutput(true);
                connection.setDoInput(true);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.e("piyush", "Everythingh is allright");
                } else {
                    Log.e("piyush","something error ");
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
            if(myData!=null){
                try {

                    int a;
                    JSONArray graphe=new JSONArray(myData);
                    JSONObject c = graphe.getJSONObject(0);
                    a= Integer.parseInt(c.getString("FAMILY_PHYSICIAN"));
                    a= a+Integer.parseInt(c.getString("PHYSICIAN"));
                    a= a+Integer.parseInt(c.getString("FAMILY_MEDICINE"));
                    a= a+Integer.parseInt(c.getString("CONSULTANT_PHYSICIAN")+Integer.parseInt(c.getString("GENERAL_PHYSICIAN"))+
                    Integer.parseInt(c.getString("INTERNAL_MEDICINE")));

                    array[0]=""+a;
                    nameArray[0]="FAMILY_PHYSICIAN";
                    Log.e("sdkj",""+a);
                    array[1]=c.getString("PAEDRIATICS");
                    nameArray[1]="PAEDRIATICS";

                    array[2]=c.getString("CHEST_SPECIALIST");
                    nameArray[2]="CHEST SPECIALIST";

                    array[3]=c.getString("OBS");
                    nameArray[3]="OBS";

                    array[4]=c.getString("DCH");
                    nameArray[4]="DCH";

                    array[5]=c.getString("GASTROENTROLOGIST");
                    nameArray[5]="GASTROENTROLOGIST";

                    array[6]=c.getString("NEUROLOGY");
                    nameArray[6]="NEUROLOGY";

                    array[7]=c.getString("CARDIOLOGIST");
                    nameArray[7]="CARDIOLOGIST";

                    array[8]=c.getString("OBES.MANG.");
                    nameArray[8]="OBES.MANG.";

                    array[9]=c.getString("D.A.");
                    nameArray[9]="D.A.";

                    array[10]=c.getString("NEURO_SURGERY");
                    nameArray[10]="NEURO_SURGERY";

                    array[11]=c.getString("UROLOGY");
                    nameArray[11]="UROLOGY";

                    array[12]=c.getString("G.I._SURGERY");
                    nameArray[12]="G.I._SURGERY";

                    array[13]=c.getString("CARDIAC_SURGEON");
                    nameArray[13]="CARDIAC_SURGEON";

                    array[14]=c.getString("NEPHROLOGY");
                    nameArray[14]="NEPHROLOGY";

//                    array[15]=c.getString("ONCOLOGY");
//                    nameArray[15]="PAEDRIATICS";
//
//                    array[16]=c.getString("ONCOSURGEON");
//                    nameArray[16]="PAEDRIATICS";



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

            GraphAdapter gd=new GraphAdapter(GraphActivity.this,array,nameArray);
            lv.setAdapter(gd);

        }
    }



    private float[] calculateData(float[] data) {
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 360 * (data[i] / total);
        }
        return data;
    }

    public class MyGraphview extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        RectF rectf = new RectF(200, 200, 380, 380);
        float temp = 0;

        public MyGraphview(Context context, float[] values) {
            super(context);
            value_degree = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Random r;
            for (int i = 0; i < value_degree.length; i++) {
                if (i == 0) {
                    r = new Random();
                    int color = Color.argb(100, r.nextInt(156), r.nextInt(206),
                            r.nextInt(206));
                    paint.setColor(color);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += value_degree[i - 1];
                    r = new Random();
                    int color = Color.argb(255, r.nextInt(156), r.nextInt(256),
                            r.nextInt(216));
                    paint.setColor(color);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }
    }

}
