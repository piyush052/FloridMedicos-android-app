package genero.dexterous.com.almanac.medical;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
 * Created by piyush on 1/27/16.
 */
public class MedicalList extends ArrayAdapter {
    //MapFragment mp;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> address=new ArrayList<>();
    ArrayList<String> contact=new ArrayList<>();
    Context con;

    public MedicalList(medicalActivity medicalActivity, ArrayList<String> contact, ArrayList<String> address, ArrayList<String> name) {
            super(medicalActivity, R.layout.list_for_medical,name);
        this.name=name;
        this.address=address;
        this.contact=contact;
        con=medicalActivity;
    }


   // @SuppressWarnings("unchecked")
    String strl;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.list_for_medical,null);
        TextView tv= (TextView) convertView.findViewById(R.id.textView10);
        ImageButton ib= (ImageButton) convertView.findViewById(R.id.im);

        ib.setVisibility(View.VISIBLE);


         //frag = convertView.findViewById(R.id.fragment3);
       // frag.setVisibility(View.INVISIBLE);
        strl=address.get(position);


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
        /*

        Addd othe attribute here..........................
         */
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(con, MapActivity.class);
                in.putExtra("key",strl);
                in.putExtra("key1",name.get(position)+""+strl);
                con.startActivity(in);

            }
        });

        return convertView;
    }
}
