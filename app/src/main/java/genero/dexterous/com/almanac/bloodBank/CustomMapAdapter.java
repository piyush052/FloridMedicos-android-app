package genero.dexterous.com.almanac.bloodBank;

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
 * Created by piyush on 3/5/16.
 */
public class CustomMapAdapter extends ArrayAdapter {

    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> address=new ArrayList<>();
    ArrayList<String> contact=new ArrayList<>();
    Context con;
    public CustomMapAdapter(BloodBankActivity bloodBankActivity, ArrayList<String> name, ArrayList<String> b_address, ArrayList<String> contact) {
    super(bloodBankActivity, R.layout.blood_list,name);

        Log.e("Custom map adapter", "custom map adapter");

        this.name=name;
        address=b_address;
        this.contact=contact;
        con=bloodBankActivity;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.blood_list,null);
        TextView tv= (TextView) convertView.findViewById(R.id.bloodText);
        ImageButton ib= (ImageButton) convertView.findViewById(R.id.mapBlood);

        tv.setText(name.get(position)+"\n"+address.get(position)+"\n"+contact.get(position));


        if (position == 0) {
            convertView.setBackgroundColor(Color.parseColor("#ffa726"));
        }
        else if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#009688"));
        }
        else if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#26c6da"));
        }


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(con, MapActivity.class);
                in.putExtra("key1",name.get(position)+" "+address.get(position));
                in.putExtra("key",address.get(position));

                con.startActivity(in);

            }
        });

        return convertView;
    }
}