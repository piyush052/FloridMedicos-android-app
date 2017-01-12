package genero.dexterous.com.almanac.Home;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import genero.dexterous.com.almanac.R;

/**
 * Created by piyush on 2/8/16.
 */
public class GridAdapter extends BaseAdapter {

    String[] str;
    int images [];
    Context con;
          static int pos;
    public GridAdapter(HomeActivity homeActivity, String[] str, int[] images) {

        this.str=str;
        this.images=images;
        con=homeActivity;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        CardView cd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.gridview,null);
        Holder holder=new Holder();
        pos=position;

//       holder.cd= (CardView) convertView.findViewById(R.id.card_viewHome);
//       convertView.findViewById(R.id.card_viewHome).setBackgroundColor(Color.parseColor("#ffffff"));
        holder.img= (ImageView) convertView.findViewById(R.id.imageView2);
        //holder.img.setVisibility(View.INVISIBLE);
        holder.tv= (TextView) convertView.findViewById(R.id.textView4);

        holder.img.setImageResource(images[position]);
        holder.tv.setText(str[position]);

        switch (position){
            case 0:convertView.setBackgroundColor(Color.parseColor("#26c6da"));break;

            case 1:convertView.setBackgroundColor(Color.parseColor("#009688"));break;

            case 2:convertView.setBackgroundColor(Color.parseColor("#ffa726"));break;

            case 3:convertView.setBackgroundColor(Color.parseColor("#26c6da"));break;

            case 4:convertView.setBackgroundColor(Color.parseColor("#009688"));break;

            case 5:convertView.setBackgroundColor(Color.parseColor("#ffa726"));break;

            case 6:convertView.setBackgroundColor(Color.parseColor("#26c6da"));break;

            case 7:convertView.setBackgroundColor(Color.parseColor("#009688"));break;


        }


        return convertView;

    }
}
