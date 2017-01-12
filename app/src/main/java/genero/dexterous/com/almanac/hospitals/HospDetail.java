package genero.dexterous.com.almanac.hospitals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import genero.dexterous.com.almanac.MapActivity;
import genero.dexterous.com.almanac.R;

/**
 * Created by piyush on 3/1/16.
 */
public class HospDetail extends ArrayAdapter {
    Context con;

    ArrayList<String> id=new ArrayList<>();
    ArrayList<String> zone=new ArrayList<>();
    ArrayList<String> h_name=new ArrayList<>();
    ArrayList<String> address=new ArrayList<>();
    ArrayList<String> city_name=new ArrayList<>();
    ArrayList<String> state1=new ArrayList<>();
    ArrayList<String> uhcp_name=new ArrayList<>();
    ArrayList<String> pincode=new ArrayList<>();
    ArrayList<String> std_code=new ArrayList<>();
    ArrayList<String> phone=new ArrayList<>();
    ArrayList<String> mobile=new ArrayList<>();
    ArrayList<String> fax=new ArrayList<>();
    ArrayList<String> email=new ArrayList<>();


    public HospDetail(HospitalDetailsActivity hospitalDetailsActivity, ArrayList<String> id, ArrayList<String> h_name, ArrayList<String> zone, ArrayList<String> address, ArrayList<String> city_name, ArrayList<String> state1, ArrayList<String> pincode, ArrayList<String> std_code, ArrayList<String> uhcp_name, ArrayList<String> phone, ArrayList<String> mobile, ArrayList<String> fax, ArrayList<String> email) {
        super(hospitalDetailsActivity, R.layout.hospital_detail_list,id);
con=hospitalDetailsActivity;
        this.id=id;
        this.zone=zone;
        this.h_name=h_name;
        this.address=address;
        this.city_name=city_name;
        this.state1=state1;
        this.uhcp_name=uhcp_name;
        this.pincode=pincode;
        this.std_code=std_code;
        this.phone=phone;
        this.mobile=mobile;
        this.fax=fax;
        this.email=email;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     convertView=   li.inflate(R.layout.hospital_detail_list,null);
        TextView tv= (TextView) convertView.findViewById(R.id.hospd);
        try {
            Log.e("h_name", h_name.get(position));
            tv.setText(h_name.get(position) + "\n" + address.get(position) + "\n" + city_name.get(position) + "\n" + mobile.get(position) + "\n" + email.get(position));
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            tv.setText("There is no hospital present in this city.\n" +
                    "Or try correct city name");
        }



        if (position == 0) {
            convertView.setBackgroundColor(Color.parseColor("#ffa726"));
        }
        else if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#009688"));
        }
        else if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#26c6da"));
        }
        ImageButton btn= (ImageButton) convertView.findViewById(R.id.mapHosp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in=new Intent(con, MapActivity.class);
                in.putExtra("key",address.get(position));
                in.putExtra("key1",h_name.get(position)+" "+address.get(position));

                con.startActivity(in);
            }
        });


        return convertView;
    }
}
