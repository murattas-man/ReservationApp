package com.reservation.reservationapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterCostumerList extends ArrayAdapter<Note> {

    private Context context;
    private ViewHolderList viewHolder;
    private List<Note> arrayList = new ArrayList<Note>();
    private Veritabani veritabani;
    public int parti=0;

    public AdapterCostumerList(Context context, List<Note> list_items) {

        super(context, R.layout.layout_newitem, list_items);
        this.context = context;
        this.arrayList = list_items;
        veritabani=new Veritabani(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.layout_newitem, parent,false);

            viewHolder = new ViewHolderList();
            viewHolder.txt_name = (TextView)view.findViewById(R.id.tvnameList);
            viewHolder.txt_saat = (TextView)view.findViewById(R.id.tvTimelist);
            viewHolder.txtpax = (TextView)view.findViewById(R.id.tvPax);
            viewHolder.txtmasalist = (TextView)view.findViewById(R.id.tvListmasa);

            view.setTag(viewHolder);


        }else{

            viewHolder = (ViewHolderList) view.getTag();

        }
        DateFormat df = new SimpleDateFormat(context.getString(R.string.dateformate));
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,arrayList.get(position).getYil());
        c.set(Calendar.MONTH,(arrayList.get(position).getAy()));
        c.set(Calendar.DAY_OF_MONTH,arrayList.get(position).getGun());
        c.set(Calendar.HOUR_OF_DAY, arrayList.get(position).getSaat());
        c.set(Calendar.MINUTE, arrayList.get(position).getDakika());

        String dateString = df.format(new Date(c.getTimeInMillis()));
        viewHolder.txt_name.setText( ""+arrayList.get(position).getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.hour_minutes));
        String timeString = formatter.format(new Date(c.getTimeInMillis()));
        viewHolder.txt_saat.setText(timeString);
        //getTablesNo(arrayList.get(position).getId());

        viewHolder.txtmasalist.setText("Re/"+ getTablesNo(arrayList.get(position).getDate()));
        viewHolder.txtpax.setText( ""+parti);


        return view;
    }

    private static class ViewHolderList{

        TextView txt_name;
        TextView txt_saat;
        TextView txtmasalist;
        TextView txtpax;

    }
    public  String getTablesNo(int id){
        String str="";
        parti=0;
         List<Note> tableList = new ArrayList<Note>();
        try {
            tableList=veritabani.getListNote("select * from " + veritabani.TABLE_NAME + " where "+veritabani.DATE+"="+id);
            for (int i = 0; i <tableList.size() ; i++) {
                str+=tableList.get(i).getMasa()+",";
                parti+=Integer.parseInt(tableList.get(i).getParty());
            }
            str=str.substring(0,str.length()-1);
        }catch (Exception eo){

        }

        return str;
    }

}