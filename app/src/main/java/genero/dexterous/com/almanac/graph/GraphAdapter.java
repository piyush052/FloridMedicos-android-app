package genero.dexterous.com.almanac.graph;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import genero.dexterous.com.almanac.R;

/**
 * Created by piyush on 4/1/16.
 */
public class GraphAdapter extends ArrayAdapter {

    String array[]=new String[]{};
    Context con;
    String nameArray []=new String[]{};
    public GraphAdapter(GraphActivity graphActivity, String[] array, String[] nameArray) {
        super(graphActivity, R.layout.graph_list, array);
        this.array=array;
        con=graphActivity;
        this.nameArray=nameArray;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.graph_list,null);
        TextView tv= (TextView) convertView.findViewById(R.id.graphTest);
        TextView detail= (TextView) convertView.findViewById(R.id.detail);
        ProgressBar pr= (ProgressBar) convertView.findViewById(R.id.graphProgressBar1);

        try {
        if(nameArray[position]==null || array[position]==null){
            tv.setVisibility(View.INVISIBLE);
            detail.setVisibility(View.INVISIBLE);
        }else {
                    pr.setVisibility(View.VISIBLE);
                    pr.setMax(Integer.parseInt(array[position]));
                    tv.setText("IN THIS AREA\n"+nameArray[position]+"\nARE SEARCH BY "+array[position]+"%");
                    detail.setText(array[position] + "%");
            pr.setVisibility(View.INVISIBLE);

            }
            }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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
        return convertView;
    }
}
