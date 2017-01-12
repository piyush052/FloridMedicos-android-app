package genero.dexterous.com.almanac.doctor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import genero.dexterous.com.almanac.Home.HomeActivity;
import genero.dexterous.com.almanac.R;


public class DoctorActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    TextView tv;
    static String speciality="jhsdjh";
    static  String pincode=null;
     static String url;
    EditText pin;
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
        setContentView(R.layout.activity_doctor);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle("Fill the details");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

        }



        pin= (EditText) findViewById(R.id.editText);
        Button search = (Button) findViewById(R.id.button);
         tv= (TextView) findViewById(R.id.textView2);
        tv.setText("Select speciality of doctor");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  !pin.getText().toString().isEmpty()) {
                    enterAction();
                }else{
                    Toast.makeText(DoctorActivity.this,"Please Enter Correct Pincode",Toast.LENGTH_LONG).show();

                }
            }
        });
      pin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

              if (actionId == EditorInfo.IME_ACTION_SEARCH && !pin.getText().toString().isEmpty()) {

                  enterAction();
                  return true;
              }else {
                  Toast.makeText(DoctorActivity.this,"Please Enter Correct Pincode",Toast.LENGTH_LONG).show();

                  return false;
              }
          }
      });



        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.doctorSpeciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
          spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {

        Intent in=new Intent(DoctorActivity.this, HomeActivity.class);
        startActivity(in);
        super.onBackPressed();
    }

    private void enterAction() {



        pincode= String.valueOf(pin.getText());

        if(Integer.parseInt(pincode)>100000 ) {

            //  tv.setText(speciality);

            url = speciality + pincode;
            //Toast.makeText(DoctorActivity.this,url,Toast.LENGTH_LONG).show();
            Intent in = new Intent(DoctorActivity.this, MainActivity.class);
            in.putExtra("k1", speciality);
            in.putExtra("k2", pincode);
            startActivity(in);
        }else{
            Toast.makeText(this,"Please Enter Correct Pincode",Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Resources res=getResources();
        String str []=res.getStringArray(R.array.doctorSpeciality);
      speciality=  parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
