package com.reservation.reservationapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapterList extends ArrayAdapter<Items> {

    private Context context;
    private ViewHolderList viewHolder;
    private List<Items> arrayList = new ArrayList<Items>();
    public int parti=0;
    private Veritabani veritabani;

    public CustomAdapterList(Context context, List<Items> list_items) {

        super(context, R.layout.itam_list_fav, list_items);
        this.context = context;
        this.arrayList = list_items;
        veritabani=new Veritabani(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.itam_list_fav, parent,false);

            viewHolder = new ViewHolderList();
            viewHolder.txt_name = (TextView)view.findViewById(R.id.namefav);
            viewHolder.txt_date = (TextView)view.findViewById(R.id.tarihara);
            viewHolder.txt_saat = (TextView)view.findViewById(R.id.ysaatara);
            viewHolder.txt_kisisayi = (TextView)view.findViewById(R.id.kisiSayisiara);

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
      // String date=arrayList.get(position).getGun()+" / "+(arrayList.get(position).getAy()+1)+" / "+arrayList.get(position).getYil();
        viewHolder.txt_name.setText( ""+arrayList.get(position).getName());
        viewHolder.txt_date.setText(dateString);
        SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.hour_minutes));
        String timeString = formatter.format(new Date(c.getTimeInMillis()));
        viewHolder.txt_saat.setText(timeString);
        String masano=getTablesNo(arrayList.get(position).getDate());
        viewHolder.txt_kisisayi.setText(""+parti);


        return view;
    }

    private static class ViewHolderList{

        TextView txt_name;
        TextView txt_date;
        TextView txt_saat;
        TextView txt_kisisayi;

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