package com.reservation.reservationapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Arama extends AppCompatActivity {
    private CustomAdapterList customAdapter;
    private ListView listview;
    private SearchView searchView;
    private List<Items> fav_list = new ArrayList<Items>();
    private ProgressDialog progressDialog;
    SQLiteDatabase db;
    Veritabani mVeritabani;
    String orderBy = "_id";

    private  String[] gunler=new String[180];
    private  String seciliGun;
    Spinner spinnerTarihler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_fav);
        listview = (ListView)findViewById(R.id.liste_favla);
        searchView=(SearchView)findViewById(R.id.search_fav);
        mVeritabani = new Veritabani(this);
        db= mVeritabani.getWritableDatabase();
        spinnerTarihler=findViewById(R.id.spinnerTarihler);

        Bundle bundle = getIntent().getExtras();
        Date simdikiZaman = new Date();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DateFormat df = new SimpleDateFormat(getString(R.string.dateformate));
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
        Calendar ctarih = Calendar.getInstance();
        seciliGun=df.format(simdikiZaman);
        ctarih.setTime(simdikiZaman);
        try{
            gunler[0]="All Reservations";
        for (int i=1;i<gunler.length;i++)
        {
            Date afterThirteenDay = ctarih.getTime();
            gunler[i]=df.format(afterThirteenDay);
            ctarih.add(Calendar.DATE, 1);

            // Toast.makeText(this, ""+gunler[i], Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, gunler);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarihler.setAdapter(adapter);
            //btnDatesec.setText(" "+dateString);
       // degerler(1);

        spinnerTarihler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // seciliGun=spinnerTarihler.getSelectedItem().toString();
                if (spinnerTarihler.getSelectedItemPosition()==0) {
                    degerler(false);
                }
                else
                degerler(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listview.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                List<Items> new_list = new ArrayList<Items>();
                for(Items temp:fav_list){

                    if (temp.getName().toLowerCase().contains(s.toLowerCase())||
                            temp.getName().toLowerCase().contains(s.toLowerCase())){
                        new_list.add(temp);
                    }
                }
                customAdapter = new CustomAdapterList(getApplicationContext(),new_list);
                listview.setAdapter(customAdapter);
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listGonder(position);

            }
        });

        }catch (Exception eo){
            faceFace(eo.getLocalizedMessage());
        }
    }

    void faceFace(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    private void degerler(boolean b)
    {
        Cursor cursor;
        Calendar calender = Calendar.getInstance();

        Calendar ctarih = Calendar.getInstance();
        int syil=ctarih.get(Calendar.YEAR);
        int say=ctarih.get(Calendar.MONTH);
        int sgun=ctarih.get(Calendar.DAY_OF_MONTH);
        //Toast.makeText(this, syil+"/"+say+"/"+sgun, Toast.LENGTH_LONG).show();
        fav_list.clear();
        if(b) {

            String strSpinner = spinnerTarihler.getSelectedItem().toString();
            String[] spinnerTarihler = strSpinner.split("/");
             cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR + "=" + Integer.parseInt(spinnerTarihler[2]) + " and " + mVeritabani.R_MONTH + "=" + (Integer.parseInt(spinnerTarihler[1]) - 1)
                    + " and " + mVeritabani.R_DAY + "=" + Integer.parseInt(spinnerTarihler[0])+"  group by "+mVeritabani.FULLNAME, null);
        }
        else{

            cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME+"  group by "+mVeritabani.FULLNAME + " order by " + mVeritabani.R_YEAR + " , " + mVeritabani.R_MONTH + "," + mVeritabani.R_DAY , null);

        }
        if (cursor != null) {
        while (cursor.moveToNext()) {
            Items items = new Items();
            items.setName(cursor.getString(cursor.getColumnIndex(mVeritabani.FULLNAME)));
            items.setGun(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_DAY)));
            items.setAy(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MONTH)));
            items.setYil(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_YEAR)));
            items.setID(cursor.getInt(cursor.getColumnIndex(mVeritabani.MASANO)));
            items.setSaat(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_HOUR)));
            items.setDakika(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MINUTE)));
            items.setPartys(cursor.getString(cursor.getColumnIndex(mVeritabani.PARTY)));
            items.setCID(cursor.getInt(cursor.getColumnIndex(mVeritabani.C_ID)));
            items.setDate(cursor.getInt(cursor.getColumnIndex(mVeritabani.DATE)));

            calender.clear();
            calender.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MONTH)));
            calender.set(Calendar.DAY_OF_MONTH,cursor.getInt(cursor.getColumnIndex(mVeritabani.R_DAY)));
            calender.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(mVeritabani.R_YEAR)));
            calender.set(Calendar.HOUR,23);
            calender.set(Calendar.MINUTE, 59);
            calender.set(Calendar.SECOND, 00);
            long farkZaman=Calendar.getInstance().getTimeInMillis()-calender.getTimeInMillis();
            if(farkZaman<=0)
            fav_list.add(items);
        }
        }

        customAdapter = new CustomAdapterList(getApplicationContext(),fav_list);
        listview.setAdapter(customAdapter);
    }

    //DetailsActivtyye deger gönderme
    private void listGonder(int position){

        Intent intent = new Intent(getApplicationContext(),MasaBilgi.class);

        intent.putExtra("ID",customAdapter.getItem(position).getCID());
        startActivity(intent);
    }
}
