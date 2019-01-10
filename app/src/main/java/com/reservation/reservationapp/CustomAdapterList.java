package com.reservation.reservationapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapterList extends ArrayAdapter<Items> {

    private Context context;
    private ViewHolderList viewHolder;
    private List<Items> arrayList = new ArrayList<Items>();

    public CustomAdapterList(Context context, List<Items> list_items) {

        super(context, R.layout.itam_list_fav, list_items);
        this.context = context;
        this.arrayList = list_items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.itam_list_fav, parent,false);

            viewHolder = new ViewHolderList();
            viewHolder.txt_name = (TextView)view.findViewById(R.id.namefav);

            view.setTag(viewHolder);


        }else{

            viewHolder = (ViewHolderList) view.getTag();

        }

       String date=arrayList.get(position).getGun()+" / "+(arrayList.get(position).getAy()+1)+" / "+arrayList.get(position).getYil();
        viewHolder.txt_name.setText( ""+arrayList.get(position).getName());


        return view;
    }

    private static class ViewHolderList{

        TextView txt_name;

    }

}