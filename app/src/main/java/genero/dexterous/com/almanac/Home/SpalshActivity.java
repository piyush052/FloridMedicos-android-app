 package genero.dexterous.com.almanac.Home;

 import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import genero.dexterous.com.almanac.R;


 public class SpalshActivity extends Activity {
     private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh);
       ImageView imgv = (ImageView) findViewById(R.id.splashImageView);
        imgv.setScaleType(ImageView.ScaleType.FIT_XY);
       TextView tv = (TextView) findViewById(R.id.textView);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SpalshActivity.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spalsh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//     @Override
//     protected void onDestroy() {
//
//
//         try {
//             Process process = Runtime.getRuntime().exec("logcat -d*:E");
//             BufferedReader bufferedReader = new BufferedReader(
//                     new InputStreamReader(process.getInputStream()));
//
//             StringBuilder log=new StringBuilder();
//             String line;
//             while ((line = bufferedReader.readLine()) != null) {
//                 log.append(line);
//             }
//
//
//             Intent i = new Intent(Intent.ACTION_SEND);
//             i.setType("message/rfc822");
//             i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"piyushupadhyay052@gmail.com"});
//             i.putExtra(Intent.EXTRA_SUBJECT, "Error from florid medicos");
//             i.putExtra(Intent.EXTRA_TEXT   , log.toString());
//             try {
//                 startActivity(Intent.createChooser(i, "Send mail..."));
//             } catch (android.content.ActivityNotFoundException ex) {
//                 Toast.makeText(SpalshActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//             }
//
//
//         } catch (IOException e) {
//         }
//         super.onDestroy();
//     }
 }
