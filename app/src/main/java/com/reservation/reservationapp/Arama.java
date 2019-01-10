package com.reservation.reservationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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

    private  String[] gunler=new String[30];
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
        Date simdikiZaman = new Date();
        DateFormat df = new SimpleDateFormat(getString(R.string.dateformate));
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
        Calendar ctarih = Calendar.getInstance();
        seciliGun=df.format(simdikiZaman);
        ctarih.setTime(simdikiZaman);
        try{
        for (int i=0;i<gunler.length;i++)
        {
            Date afterThirteenDay = ctarih.getTime();
            gunler[i]=df.format(afterThirteenDay);
            ctarih.add(Calendar.DATE, 1);

            // Toast.makeText(this, ""+gunler[i], Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gunler);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarihler.setAdapter(adapter);

        degerler(1);
        spinnerTarihler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // seciliGun=spinnerTarihler.getSelectedItem().toString();

                degerler(1);

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
    private void degerler(int p)
    {
        fav_list.clear();
        String strSpinner=spinnerTarihler.getSelectedItem().toString();;
        String[] spinnerTarihler = strSpinner.split("/");
         Cursor cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR+"="+Integer.parseInt(spinnerTarihler[2])+" and "+mVeritabani.R_MONTH+"="+(Integer.parseInt(spinnerTarihler[1])-1)
                +" and "+mVeritabani.R_DAY+"="+Integer.parseInt(spinnerTarihler[0]), null);

        if (cursor != null) {
        while (cursor.moveToNext()) {
            Items items = new Items();
            items.setName(cursor.getString(cursor.getColumnIndex(mVeritabani.FULLNAME)));
            items.setGun(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_DAY)));
            items.setAy(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MONTH)));
            items.setYil(cursor.getInt(cursor.getColumnIndex(mVeritabani.R_YEAR)));
            items.setID(cursor.getInt(cursor.getColumnIndex(mVeritabani.MASANO)));

            fav_list.add(items);
        }}
        customAdapter = new CustomAdapterList(getApplicationContext(),fav_list);
        listview.setAdapter(customAdapter);
    }

    //DetailsActivtyye deger gönderme
    private void listGonder(int position){

        Intent intent = new Intent(getApplicationContext(),MasaBilgi.class);

        // listview in hangi itemine tıklandıysa diğer sayfada ilgili resmi göstermek için sunucudan gelen resmin adını yolluyoruz.
        intent.putExtra("btnID",customAdapter.getItem(position).getID());
        intent.putExtra("gun",customAdapter.getItem(position).getGun());
        intent.putExtra("ay",customAdapter.getItem(position).getAy());
        intent.putExtra("yil",customAdapter.getItem(position).getYil());
        startActivity(intent);
    }

}
