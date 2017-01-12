package genero.dexterous.com.almanac.doctor;

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
 * Created by piyush on 12/25/15.
 */
public class CustomAdapter extends ArrayAdapter<String>{
    Context con;

    ArrayList<String> first_name1=new ArrayList<>();
    ArrayList<String> last_name1=new ArrayList<>();
    ArrayList<String> pincode11=new ArrayList<>();
    ArrayList<String> branch1 =new ArrayList<>();
    ArrayList<String> resi_address1=new ArrayList<>();
    ArrayList<String> id1=new ArrayList<>();
    ArrayList<String> phone1 =new ArrayList<>();
    ArrayList<String> speciality_11 =new ArrayList<>();
    ArrayList<String> speciality_21 =new ArrayList<>();
    ArrayList<String> email11=new ArrayList<>();

    public CustomAdapter(MainActivity mainActivity, ArrayList<String> first_name, ArrayList<String> last_name, ArrayList<String> pincode1, ArrayList<String> speciality_1, ArrayList<String> speciality_2, ArrayList<String> branch, ArrayList<String> phone, ArrayList<String> id, ArrayList<String> resi_address, ArrayList<String> email) {
        super(mainActivity, R.layout.list_item,first_name);
        con=mainActivity;

        first_name1=first_name;
        last_name1=last_name;
        pincode11=pincode1;
        branch1=branch;
        resi_address1=resi_address;
        id1=id;
        phone1=phone;
        speciality_11=speciality_1;
        speciality_21=speciality_2;
        email11=email;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.list_item,null);
        TextView name= (TextView) convertView.findViewById(R.id.name);
        try {


                name.setText(first_name1.get(position) + " " + last_name1.get(position) + "\nADDRESS:" + resi_address1.get(position) +
                        "\nPINCODE:" + pincode11.get(position) + "\nSPECIALITY:" + speciality_11.get(position) + "  " + speciality_21.get(position) +
                        "\n CONTACT:" + phone1.get(position) + "\n EMAIL:-" + email11.get(position));
            }catch (Exception e){
            e.printStackTrace();
        }
        ImageButton search= (ImageButton) convertView.findViewById(R.id.mapButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(con, MapActivity.class);
                in.putExtra("key",resi_address1.get(position)+","+pincode11.get(position));
                in.putExtra("key1",first_name1.get(position)+" "+last_name1.get(position)+" "+resi_address1.get(position)+","+pincode11.get(position));

                con.startActivity(in);

            }
        });


        if (position == 0) {
            convertView.setBackgroundColor(Color.parseColor("#ffa726"));
        }
        else if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#009688"));
        }
        else if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#26c6da"));
        }

        return convertView;
    }
}
