package com.example.mad.lab2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daniel on 01/04/17.
 */
public class groups_adapter extends ArrayAdapter<groups_class> {
    Context context;
    private ArrayList<groups_class> data;
    int layoutResourceId;
    //groups_class data[] = null;

    //Constructor
    public groups_adapter(Context context, int layoutResourceId, ArrayList<groups_class> data){
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    // Create the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        groups_holder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new groups_holder();
            holder.image = (ImageView) row.findViewById(R.id.ima1);
            holder.text = (TextView) row.findViewById(R.id.text1);
            holder.textnot = (TextView) row.findViewById(R.id.text2);
            row.setTag(holder);
        }else{
            holder = (groups_holder)row.getTag();
        }

        //groups_class pos_gro = data[position];
        groups_class group = data.get(position);
        holder.text.setText(group.getTitle());
        holder.textnot.setText(group.getNoti());
       /* holder.image.setImageResource(group.icon);*/
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(group.getIcon()));
        if(bitmap != null)
            holder.image.setImageResource(group.icon);
        else{
            holder.image.setImageResource(R.drawable.bills);
        }


        //In order to return the view
        return row;
    }

    //Keep the data ir order to be alble to work with them
    static class groups_holder{
        ImageView image;
        TextView text;
        TextView textnot;


    }

}
