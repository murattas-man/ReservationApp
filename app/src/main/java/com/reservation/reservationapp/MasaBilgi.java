package com.reservation.reservationapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MasaBilgi extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{
    TextView tvdatepic,tvtimepic;
    EditText etFullName,etTelno,etKisiSayisi,etNotes;
    SQLiteDatabase db;
    Veritabani mVeritabani;
    Button btnSil,btnGuncelle,btnMasaNo;
    Button btnSaatsec,btnDatesec;
    int RID=1,masano;
    int year, month,day,saat,dakika;

    Button choose1,choose2,choose3,choose4,choose5,choose6,choose7,choose8,choose9,choose10;
    Button choose11,choose12,choose13,choose14,choose15,choose16,choose17,choose18,choose19,choose20;
    Dialog dialog;
    SimpleDateFormat dateformatter;
    SimpleDateFormat formatter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anabilgi);
        try {


        Bundle bundle = getIntent().getExtras();
        final int id = bundle.getInt("btnID");
        final int gun = bundle.getInt("gun");
        final int ay = bundle.getInt("ay");
        final int yil = bundle.getInt("yil");
        init();
        mVeritabani = new Veritabani(this);
        db= mVeritabani.getWritableDatabase();
        dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
        formatter = new SimpleDateFormat(getString(R.string.hour_minutes));

        Cursor cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.MASANO + "=" + id
                +" and "+ mVeritabani.R_YEAR+"="+yil+" and "+mVeritabani.R_MONTH+"="+ay
                +" and "+mVeritabani.R_DAY+"="+gun, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    etFullName.setText(cursor.getString(cursor.getColumnIndex(mVeritabani.FULLNAME)));
                    etTelno.setText(cursor.getString(cursor.getColumnIndex(mVeritabani.PHONENUMBER)));
                    etKisiSayisi.setText(cursor.getString(cursor.getColumnIndex(mVeritabani.PARTY)));
                    etNotes.setText(cursor.getString(cursor.getColumnIndex(mVeritabani.NOTES)));
                   RID=cursor.getInt(cursor.getColumnIndex(mVeritabani.C_ID));
                   masano=id;
                   year=cursor.getInt(cursor.getColumnIndex(mVeritabani.R_YEAR));
                   month=cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MONTH));
                   day=cursor.getInt(cursor.getColumnIndex(mVeritabani.R_DAY));
                   dakika=cursor.getInt(cursor.getColumnIndex(mVeritabani.R_MINUTE));
                   saat=cursor.getInt(cursor.getColumnIndex(mVeritabani.R_HOUR));
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);

                    c.set(Calendar.HOUR_OF_DAY, saat);
                    c.set(Calendar.MINUTE, dakika);

                    String dateString = dateformatter.format(new Date(c.getTimeInMillis()));
                    btnDatesec.setText(" "+dateString);

                    String timeString = formatter.format(new Date(c.getTimeInMillis()));
                    btnSaatsec.setText(" "+timeString);
                }
                cursor.close();
            }

            btnSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MasaBilgi.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.btnDelete).setIcon(R.mipmap.ic_launcher)
                            .setMessage(R.string.delete_message);
                    builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sil(RID);
                        }
                    });
                    builder.show();
                }
            });

            btnGuncelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MasaBilgi.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(R.string.btnUpdate).setIcon(R.mipmap.ic_launcher)
                            .setMessage(R.string.Update_message);
                    builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            guncelle(RID);
                        }
                    });
                    builder.show();

                }
            });
            btnMasaNo.setText(masano+"");
            btnMasaNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   masaSec();
                }
            });
        }catch (Exception eo){
            faceFace(eo.getLocalizedMessage());
        }
    }
    private void init()
    {
        tvdatepic=(TextView)findViewById(R.id.tvDatepicUp);
        etFullName=(EditText) findViewById(R.id.etNameUp);
        etTelno=(EditText) findViewById(R.id.etTelUp);
        etKisiSayisi=(EditText) findViewById(R.id.etKisisayisiUp);
        etNotes=(EditText)findViewById(R.id.etNotesUp);
        tvtimepic=(TextView)findViewById(R.id.tvTimeUp);
        btnSil=(Button)findViewById(R.id.btnDelete);
        btnGuncelle=(Button)findViewById(R.id.btnUp);
        btnMasaNo=(Button)findViewById(R.id.btnMasaNo);
        btnSaatsec=(Button)findViewById(R.id.btnsaatSec);
        btnDatesec=(Button)findViewById(R.id.btndatesec);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ara, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.geriac:

                Intent intentAra=new Intent(MasaBilgi.this,Anasayfa.class);
                startActivity(intentAra);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void guncelle(int ID)
    {
        String str=String.valueOf(etFullName.getText());
        char c=Character.toUpperCase(str.charAt(0));
        str=c+str.substring(1);
        ContentValues cv = new ContentValues();
        cv.put(mVeritabani.FULLNAME,str);
        cv.put(mVeritabani.PHONENUMBER, String.valueOf(etTelno.getText()));
        cv.put(mVeritabani.NOTES, String.valueOf(etNotes.getText()));
        cv.put(mVeritabani.MASANO,masano);
        cv.put(mVeritabani.DATE, String.valueOf(new Date()));
        cv.put(mVeritabani.R_YEAR,year);
        cv.put(mVeritabani.R_MONTH,month);
        cv.put(mVeritabani.R_DAY,day);
        cv.put(mVeritabani.R_HOUR,saat);
        cv.put(mVeritabani.R_MINUTE,dakika);
        db.update(mVeritabani.TABLE_NAME, cv, mVeritabani.C_ID + "=" + ID, null);
        Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();
        Intent ıntent=new Intent(MasaBilgi.this,Anasayfa.class);
        startActivity(ıntent);
    }
    private void sil(int ID)
    {
        db.delete(Veritabani.TABLE_NAME, Veritabani.C_ID + "=" + ID, null);
        Toast.makeText(this, "Delete Successful", Toast.LENGTH_LONG).show();
        Intent ıntent=new Intent(MasaBilgi.this,Anasayfa.class);
        startActivity(ıntent);

    }

    public  void onTextviewUpClicked(View view)
    {
        boolean click = ((Button) view).hasOnClickListeners();
        switch (view.getId())
        {
            case R.id.btndatesec:
                if (click)
                {
                    DialogFragment datePicker=new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(),"Date Picker");
                }
                break;
            case R.id.btnsaatSec:
                if (click)
                    openPickerDialog();
                break;
        }
    }
    private void masaSec() {
        dialog = new Dialog(MasaBilgi.this);
        dialog.setContentView(R.layout.choose_table);
        dialog.setTitle(""+getString(R.string.mSec));
        int width = (int) (MasaBilgi.this.getResources().getDisplayMetrics().widthPixels * 0.4);
        // set height for dialog
        int height = (int) (MasaBilgi.this.getResources().getDisplayMetrics().heightPixels * 0.90);
        dialog.getWindow().setLayout(width, height);
        dialogButtonInit();
        Cursor cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR + "=" + year +" and " +
                mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day  , null);

        masaAktifET();
        while (cursor.moveToNext())
        {
            switch (cursor.getInt(cursor.getColumnIndex(mVeritabani.MASANO)))
            {
                case 1:choose1.setBackgroundResource(R.color.doluMasa);
                    choose1.setEnabled(false);
                    break;
                case 2:choose2.setBackgroundResource(R.color.doluMasa);
                    choose2.setEnabled(false);
                    break;
                case 3:choose3.setBackgroundResource(R.color.doluMasa);
                    choose3.setEnabled(false);
                    break;
                case 4:choose4.setBackgroundResource(R.color.doluMasa);
                    choose4.setEnabled(false);
                    break;
                case 5:choose5.setBackgroundResource(R.color.doluMasa);
                    choose5.setEnabled(false);
                    break;
                case 6:choose6.setBackgroundResource(R.color.doluMasa);
                    choose6.setEnabled(false);
                    break;
                case 7:choose7.setBackgroundResource(R.color.doluMasa);
                    choose7.setEnabled(false);
                    break;
                case 8:choose8.setBackgroundResource(R.color.doluMasa);
                    choose8.setEnabled(false);
                    break;
                case 9:choose9.setBackgroundResource(R.color.doluMasa);
                    choose9.setEnabled(false);
                    break;
                case 10:choose10.setBackgroundResource(R.color.doluMasa);
                    choose10.setEnabled(false);
                    break;
                case 11:choose11.setBackgroundResource(R.color.doluMasa);
                    choose11.setEnabled(false);
                    break;
                case 12:choose12.setBackgroundResource(R.color.doluMasa);
                    choose12.setEnabled(false);
                    break;
                case 13:choose13.setBackgroundResource(R.color.doluMasa);
                    choose13.setEnabled(false);
                    break;
                case 14:choose14.setBackgroundResource(R.color.doluMasa);
                    choose14.setEnabled(false);
                    break;
                case 15:choose15.setBackgroundResource(R.color.doluMasa);
                    choose15.setEnabled(false);
                    break;
                case 16:choose16.setBackgroundResource(R.color.doluMasa);
                    choose16.setEnabled(false);
                    break;
                case 17:choose17.setBackgroundResource(R.color.doluMasa);
                    choose17.setEnabled(false);
                    break;
                case 18:choose18.setBackgroundResource(R.color.doluMasa);
                    choose18.setEnabled(false);
                    break;
                case 19:choose19.setBackgroundResource(R.color.doluMasa);
                    choose19.setEnabled(false);
                    break;
                case 20:choose20.setBackgroundResource(R.color.doluMasa);
                    choose20.setEnabled(false);
                    break;
                default:break;
            }
        }
        dialog.show();
    }
    private void dialogButtonInit() {
        choose1=dialog.findViewById(R.id.btnchoose1);
        choose2=dialog.findViewById(R.id.btnchoose2);
        choose3=dialog.findViewById(R.id.btnchoose3);
        choose4=dialog.findViewById(R.id.btnchoose4);
        choose5=dialog.findViewById(R.id.btnchoose5);
        choose6=dialog.findViewById(R.id.btnchoose6);
        choose7=dialog.findViewById(R.id.btnchoose7);
        choose8=dialog.findViewById(R.id.btnchoose8);
        choose9=dialog.findViewById(R.id.btnchoose9);
        choose10=dialog.findViewById(R.id.btnchoose10);
        choose11=dialog.findViewById(R.id.btnchoose11);
        choose12=dialog.findViewById(R.id.btnchoose12);
        choose13=dialog.findViewById(R.id.btnchoose13);
        choose14=dialog.findViewById(R.id.btnchoose14);
        choose15=dialog.findViewById(R.id.btnchoose15);
        choose16=dialog.findViewById(R.id.btnchoose16);
        choose17=dialog.findViewById(R.id.btnchoose17);
        choose18=dialog.findViewById(R.id.btnchoose18);
        choose19=dialog.findViewById(R.id.btnchoose19);
        choose20=dialog.findViewById(R.id.btnchoose20);
    }
    private void masaAktifET() {
        choose1.setEnabled(true);
        choose2.setEnabled(true);
        choose3.setEnabled(true);
        choose4.setEnabled(true);
        choose5.setEnabled(true);
        choose6.setEnabled(true);
        choose7.setEnabled(true);
        choose8.setEnabled(true);
        choose9.setEnabled(true);
        choose10.setEnabled(true);
        choose11.setEnabled(true);
        choose12.setEnabled(true);
        choose13.setEnabled(true);
        choose14.setEnabled(true);
        choose15.setEnabled(true);
        choose16.setEnabled(true);
        choose17.setEnabled(true);
        choose18.setEnabled(true);
        choose19.setEnabled(true);
        choose20.setEnabled(true);
    }

    public void onDateSet(DatePicker view, int yil, int ay, int gun) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,yil);
        c.set(Calendar.MONTH,ay);
        c.set(Calendar.DAY_OF_MONTH,gun);
        year=yil;
        month=ay;
        day=gun;
        //SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
        String dateString = dateformatter.format(new Date(c.getTimeInMillis()));
        btnDatesec.setText(" "+dateString);
        btnDatesec.setBackgroundResource(R.color.bosMasa);
        masaSec();
    }
    private void openPickerDialog( ) {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MasaBilgi.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false);
        timePickerDialog.setTitle("Time");

        timePickerDialog.show();
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            saat=hourOfDay;
            dakika=minute;
            //SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
            String timeString = formatter.format(new Date(calSet.getTimeInMillis()));
            btnSaatsec.setText(" "+timeString);
            btnSaatsec.setBackgroundResource(R.color.bosMasa);
        }};

    public void onButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((Button) view).hasOnClickListeners();
        // Check which checkbox was clicked
        switch(view.getId())
        {
            case R.id.btnchoose1:
                if (checked)
                {
                    masano=1;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose2:
                if (checked)
                {

                    masano=2;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose3:
                if (checked)
                {

                    masano=3;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose4:
                if (checked)
                {

                    masano=4;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose5:
                if (checked)
                {

                    masano=5;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose6:
                if (checked)
                {

                    masano=6;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose7:
                if (checked)
                {

                    masano=7;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;case R.id.btnchoose8:
            if (checked)
            {

                masano=8;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose9:
            if (checked)
            {

                masano=9;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose10:
            if (checked)
            {

                masano=10;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose11:
            if (checked)
            {

                masano=11;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose12:
            if (checked)
            {

                masano=12;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose13:
            if (checked)
            {

                masano=13;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose14:
            if (checked)
            {

                masano=14;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;case R.id.btnchoose15:
            if (checked)
            {

                masano=15;
                btnMasaNo.setText(masano+"");
            }
            dialog.dismiss();
            break;
            case R.id.btnchoose16:
                if (checked)
                {

                    masano=16;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose17:
                if (checked)
                {

                    masano=17;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose18:
                if (checked)
                {

                    masano=18;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose19:
                if (checked)
                {

                    masano=19;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose20:
                if (checked)
                {

                    masano=20;
                    btnMasaNo.setText(masano+"");
                }
                dialog.dismiss();
                break;


        }
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, Anasayfa.class);
        startActivity(setIntent);
    }

    void faceFace(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
