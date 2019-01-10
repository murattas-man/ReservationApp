package com.reservation.reservationapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Anasayfa extends AppCompatActivity  implements  DatePickerDialog.OnDateSetListener,BackupData.OnBackupListener{
    TextView tvTarih,tvdatepic,tvtimepic;
    EditText etFullName,etTelno,etKisiSayisi,etNotes;
    TextView secimTarih;
    Spinner spinnerTarihler;
    Button btnsaatSec,btndateSec;

    Button btnSave,btnMasa1,btnMasa2,btnMasa3,btnMasa4,btnMasa5,btnMasa6,btnMasa7,btnMasa8,btnMasa9;
    Button btnMasa10,btnMasa11,btnMasa12,btnMasa13,btnMasa14,btnMasa15,btnMasa16,btnMasa17,btnMasa18,btnMasa19,btnMasa20;
    Button choose1,choose2,choose3,choose4,choose5,choose6,choose7,choose8,choose9,choose10;
    Button choose11,choose12,choose13,choose14,choose15,choose16,choose17,choose18,choose19,choose20;
    int masano,year, month,day,saat,dakika;
    Dialog dialog;
    SQLiteDatabase db;
    Veritabani mVeritabani;
    int backmirestoremi=0;

    private  String[] gunler=new String[30];
    private  String seciliGun;
    boolean saatKontrol=false,tarihKontrol=false;
    private Context context;
    private BackupData backupData;
    DateFormat df;
    SimpleDateFormat formatter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 21;
    private  List<Note> yedekListe;
    private  List<Note> datalar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        context = this;
        init();

        mVeritabani = new Veritabani(this);
        db= mVeritabani.getWritableDatabase();
        backupData = new BackupData(context);
        backupData.setOnBackupListener(this);
        demoKontrol();
        try {


        Date simdikiZaman = new Date();
         df = new SimpleDateFormat(getString(R.string.dateformate));
         formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
        Calendar ctarih = Calendar.getInstance();
        seciliGun=df.format(simdikiZaman);
        ctarih.setTime(simdikiZaman);
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

        spinnerTarihler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // seciliGun=spinnerTarihler.getSelectedItem().toString();
                bosMasa();
                gosterMasa();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gosterMasa();
        tvTarih.setText(getString(R.string.tarih)+"  ");
        //tvdatepic.setText("Date :"+df.format(simdikiZaman));
        //tvtimepic.setText("Time :"+formatter.format(simdikiZaman));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etFullName.length()>0)
                {
                      if(etKisiSayisi.length()>0) {
                          if(saatKontrol && tarihKontrol)
                          {
                              if (masano != 0) {
                                kaydet();
                            }else Toast.makeText(Anasayfa.this, ""+getString(R.string.masanoKontrol), Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(Anasayfa.this, ""+getString(R.string.dateKontrol), Toast.LENGTH_SHORT).show();
                      }else Toast.makeText(Anasayfa.this, ""+getString(R.string.partyKontrol), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(Anasayfa.this, ""+getString(R.string.fullnameKontrol), Toast.LENGTH_SHORT).show();

            }
        });

        }catch (Exception eo){
            faceFace(eo.getLocalizedMessage());
        }
        if (!checkAndRequestPermissions()) {
            return;
        }
    }
        void faceFace(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    private void kaydet() {
        String notify = null;
        // if(masano==2)btnMasa2.setBackgroundResource(R.color.doluMasa);
        String str=String.valueOf(etFullName.getText());
        String telnumara=String.valueOf(etTelno.getText()).trim();
        String partsayi=String.valueOf(etKisiSayisi.getText()).trim();
        String notes= String.valueOf(etNotes.getText()).trim();
        final int kontrol = (int) System.currentTimeMillis();
        char c=Character.toUpperCase(str.charAt(0));
        str=c+str.substring(1);
        Note note = new Note();
        note.setFullname(str).setParty(partsayi).setPhone(telnumara).setNotes(notes).setMasa(masano)
        .setDate(String.valueOf(new Date())).setYil(year).setAy(month).setGun(day).setSaat(saat).setDakika(dakika).setKontrol(kontrol);
        if (mVeritabani.insertNote(note) > 0) {
            notify = "add success!";
        } else {
            notify = "add fail!";
        }
        Toast.makeText(this, notify, Toast.LENGTH_LONG).show();
        etFullName.setText("");
        etKisiSayisi.setText("");
        etTelno.setText("");
        etNotes.setText("");
        btnsaatSec.setText("Choose");
        btndateSec.setText("Choose");
        btnsaatSec.setBackgroundResource(R.color.btnbac);
        btndateSec.setBackgroundResource(R.color.btnbac);
        saatKontrol=false;
        tarihKontrol=false;
        masano=0;
        gosterMasa();

    }
    private  void gosterMasa()
    {
        String strSpinner=spinnerTarihler.getSelectedItem().toString();
        String[] spinnerTarihler = strSpinner.split("/");

        datalar=Araclar.veriSelect(context);
        for (int i = datalar.size() - 1; i >= 0; i--)
        {
            if(datalar.get(i).getYil()==Integer.parseInt(spinnerTarihler[2]) &&
               datalar.get(i).getAy()==(Integer.parseInt(spinnerTarihler[1])-1) &&
               datalar.get(i).getGun()==Integer.parseInt(spinnerTarihler[0]))
                masaDoldur(datalar.get(i).getMasa());
        }
    }

    private void masaDoldur(int gelenNo) {
        switch (gelenNo)
        {
            case 1:btnMasa1.setBackgroundResource(R.color.doluMasa);
                  btnMasa1.setEnabled(true);
            break;
            case 2:btnMasa2.setBackgroundResource(R.color.doluMasa);
                btnMasa2.setEnabled(true);
                break;
            case 3:btnMasa3.setBackgroundResource(R.color.doluMasa);
                btnMasa3.setEnabled(true);
            break;
            case 4:btnMasa4.setBackgroundResource(R.color.doluMasa);
                btnMasa4.setEnabled(true);
            break;
            case 5:btnMasa5.setBackgroundResource(R.color.doluMasa);
                btnMasa5.setEnabled(true);
            break;
            case 6:btnMasa6.setBackgroundResource(R.color.doluMasa);
                btnMasa6.setEnabled(true);
            break;
            case 7:btnMasa7.setBackgroundResource(R.color.doluMasa);
                btnMasa7.setEnabled(true);
            break;
            case 8:btnMasa8.setBackgroundResource(R.color.doluMasa);
                btnMasa8.setEnabled(true);
            break;
            case 9:btnMasa9.setBackgroundResource(R.color.doluMasa);
                btnMasa9.setEnabled(true);
            break;
            case 10:btnMasa10.setBackgroundResource(R.color.doluMasa);
                btnMasa10.setEnabled(true);
            break;
            case 11:btnMasa11.setBackgroundResource(R.color.doluMasa);
                btnMasa11.setEnabled(true);
            break;
            case 12:btnMasa12.setBackgroundResource(R.color.doluMasa);
                btnMasa12.setEnabled(true);
            break;
            case 13:btnMasa13.setBackgroundResource(R.color.doluMasa);
                btnMasa13.setEnabled(true);
            break;
            case 14:btnMasa14.setBackgroundResource(R.color.doluMasa);
                btnMasa14.setEnabled(true);
            break;
            case 15:btnMasa15.setBackgroundResource(R.color.doluMasa);
                btnMasa15.setEnabled(true);
            break;
            case 16:btnMasa16.setBackgroundResource(R.color.doluMasa);
                btnMasa16.setEnabled(true);
            break;
            case 17:btnMasa17.setBackgroundResource(R.color.doluMasa);
                btnMasa17.setEnabled(true);
            break;
            case 18:btnMasa18.setBackgroundResource(R.color.doluMasa);
                btnMasa18.setEnabled(true);
            break;
            case 19:btnMasa19.setBackgroundResource(R.color.doluMasa);
                btnMasa19.setEnabled(true);
            break;
            case 20:btnMasa20.setBackgroundResource(R.color.doluMasa);
                btnMasa20.setEnabled(true);
            break;
            default:break;
        }
    }
    private  void bosMasa(){
        btnMasa1.setBackgroundResource(R.color.bosMasa);
        btnMasa2.setBackgroundResource(R.color.bosMasa);
        btnMasa3.setBackgroundResource(R.color.bosMasa);
        btnMasa4.setBackgroundResource(R.color.bosMasa);
        btnMasa5.setBackgroundResource(R.color.bosMasa);
        btnMasa6.setBackgroundResource(R.color.bosMasa);
        btnMasa7.setBackgroundResource(R.color.bosMasa);
        btnMasa8.setBackgroundResource(R.color.bosMasa);
        btnMasa9.setBackgroundResource(R.color.bosMasa);
        btnMasa10.setBackgroundResource(R.color.bosMasa);
        btnMasa11.setBackgroundResource(R.color.bosMasa);
        btnMasa12.setBackgroundResource(R.color.bosMasa);
        btnMasa13.setBackgroundResource(R.color.bosMasa);
        btnMasa14.setBackgroundResource(R.color.bosMasa);
        btnMasa15.setBackgroundResource(R.color.bosMasa);
        btnMasa16.setBackgroundResource(R.color.bosMasa);
        btnMasa17.setBackgroundResource(R.color.bosMasa);
        btnMasa18.setBackgroundResource(R.color.bosMasa);
        btnMasa19.setBackgroundResource(R.color.bosMasa);
        btnMasa20.setBackgroundResource(R.color.bosMasa);
        btnMasa1.setEnabled(false);
        btnMasa2.setEnabled(false);
        btnMasa3.setEnabled(false);
        btnMasa4.setEnabled(false);
        btnMasa5.setEnabled(false);
        btnMasa6.setEnabled(false);
        btnMasa7.setEnabled(false);
        btnMasa8.setEnabled(false);
        btnMasa9.setEnabled(false);
        btnMasa10.setEnabled(false);
        btnMasa11.setEnabled(false);
        btnMasa12.setEnabled(false);
        btnMasa13.setEnabled(false);
        btnMasa14.setEnabled(false);
        btnMasa15.setEnabled(false);
        btnMasa16.setEnabled(false);
        btnMasa17.setEnabled(false);
        btnMasa18.setEnabled(false);
        btnMasa19.setEnabled(false);
        btnMasa20.setEnabled(false);


    }

    private void masaSec() {

        dialog = new Dialog(Anasayfa.this);
        dialog.setContentView(R.layout.choose_table);
        dialog.setTitle(""+getString(R.string.mSec));
        int width = (int) (Anasayfa.this.getResources().getDisplayMetrics().widthPixels * 0.4);
        // set height for dialog
        int height = (int) (Anasayfa.this.getResources().getDisplayMetrics().heightPixels * 0.90);
        dialog.getWindow().setLayout(width, height);
        dialogButtonInit();
        Cursor cursor = db.rawQuery("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR + "=" + year +" and " +
                mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day  , null);
        Calendar dcalendar=Calendar.getInstance();
        dcalendar.set(Calendar.YEAR,year);
        dcalendar.set(Calendar.MONTH,month);
        dcalendar.set(Calendar.DAY_OF_MONTH,day);
        String timeString = df.format(new Date(dcalendar.getTimeInMillis()));;
        secimTarih.setText("Date :"+timeString);
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




    public  void onTextviewClicked(View view)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ana_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_ara:

                Intent intentAra=new Intent(Anasayfa.this,Arama.class);
                startActivity(intentAra);
                return true;
            case R.id.action_yedekleme:
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.backup_data).setIcon(R.mipmap.ic_launcher)
                        .setMessage(R.string.backup_export);
                builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backupData.exportToSD();
                    }
                });
                builder.show();

                return true;
            case R.id.action_geriyukle:
                yedekListe=DataKontrol.VeriYedkle(context);
                backupData.importFromSD();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //gün ay yıl değerlerini alma
    @Override
    public void onDateSet(DatePicker view, int yil, int ay, int gun) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,yil);
        c.set(Calendar.MONTH,ay);
        c.set(Calendar.DAY_OF_MONTH,gun);
        year=yil;
        month=ay;
        day=gun;
        SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
        String dateString = dateformatter.format(new Date(c.getTimeInMillis()));
        btndateSec.setText(" "+dateString);
        btndateSec.setBackgroundResource(R.color.bosMasa);
        tarihKontrol=true;
        open();
        masaSec();
        close();
    }
    //saat ve dakika bilgilerini alma
    private void openPickerDialog( ) {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Anasayfa.this,
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
            // formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
            String timeString = formatter.format(new Date(calSet.getTimeInMillis()));;
            btnsaatSec.setText(" "+timeString);
            btnsaatSec.setBackgroundResource(R.color.bosMasa);
            saatKontrol=true;
        }};

    private  void init()
    {
        tvTarih=(TextView) findViewById(R.id.tvtarih);
        tvdatepic=(TextView)findViewById(R.id.tvDatepic);
        etFullName=(EditText) findViewById(R.id.etFullName);
        etTelno=(EditText) findViewById(R.id.etTel);
        etKisiSayisi=(EditText) findViewById(R.id.etKisisayisi);
        etNotes=(EditText)findViewById(R.id.etNotes);
        tvtimepic=(TextView)findViewById(R.id.tvTime);
        spinnerTarihler=findViewById(R.id.spinnerTarihler);

        btnSave=(Button) findViewById(R.id.btnKaydet);

        btnsaatSec=(Button) findViewById(R.id.btnsaatSec);
        btndateSec=(Button) findViewById(R.id.btndatesec);

        btnMasa1=(Button) findViewById(R.id.btnMasa1);
        btnMasa2=(Button) findViewById(R.id.btnMasa2);
        btnMasa3=(Button) findViewById(R.id.btnMasa3);
        btnMasa4=(Button) findViewById(R.id.btnMasa4);
        btnMasa5=(Button) findViewById(R.id.btnMasa5);
        btnMasa6=(Button) findViewById(R.id.btnMasa6);
        btnMasa7=(Button) findViewById(R.id.btnMasa7);
        btnMasa8=(Button) findViewById(R.id.btnMasa8);
        btnMasa9=(Button) findViewById(R.id.btnMasa9);
        btnMasa10=(Button) findViewById(R.id.btnMasa10);
        btnMasa11=(Button) findViewById(R.id.btnMasa11);
        btnMasa12=(Button) findViewById(R.id.btnMasa12);
        btnMasa13=(Button) findViewById(R.id.btnMasa13);
        btnMasa14=(Button) findViewById(R.id.btnMasa14);
        btnMasa15=(Button) findViewById(R.id.btnMasa15);
        btnMasa16=(Button) findViewById(R.id.btnMasa16);
        btnMasa17=(Button) findViewById(R.id.btnMasa17);
        btnMasa18=(Button) findViewById(R.id.btnMasa18);
        btnMasa19=(Button) findViewById(R.id.btnMasa19);
        btnMasa20=(Button) findViewById(R.id.btnMasa20);


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
        secimTarih=dialog.findViewById(R.id.tvtarih);
    }


    //seçili buttoon tıklaması alma
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
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose2:
                if (checked)
                {

                    masano=2;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose3:
                if (checked)
                {

                    masano=3;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose4:
                if (checked)
                {

                    masano=4;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose5:
                if (checked)
                {

                    masano=5;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose6:
                if (checked)
                {

                    masano=6;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose7:
                if (checked)
                {

                    masano=7;
                }
                dialog.dismiss();
                break;case R.id.btnchoose8:
            if (checked)
            {

                masano=8;
            }
            dialog.dismiss();
            break;case R.id.btnchoose9:
            if (checked)
            {

                masano=9;
            }
            dialog.dismiss();
            break;case R.id.btnchoose10:
            if (checked)
            {

                masano=10;
            }
            dialog.dismiss();
            break;case R.id.btnchoose11:
            if (checked)
            {

                masano=11;
            }
            dialog.dismiss();
            break;case R.id.btnchoose12:
            if (checked)
            {

                masano=12;
            }
            dialog.dismiss();
            break;case R.id.btnchoose13:
            if (checked)
            {

                masano=13;
            }
            dialog.dismiss();
            break;case R.id.btnchoose14:
            if (checked)
            {

                masano=14;
            }
            dialog.dismiss();
            break;case R.id.btnchoose15:
            if (checked)
            {

                masano=15;
            }
            dialog.dismiss();
            break;
            case R.id.btnchoose16:
                if (checked)
                {

                    masano=16;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose17:
                if (checked)
                {

                    masano=17;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose18:
                if (checked)
                {

                    masano=18;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose19:
                if (checked)
                {

                    masano=19;
                }
                dialog.dismiss();
                break;
            case R.id.btnchoose20:
                if (checked)
                {

                    masano=20;
                }
                dialog.dismiss();
                break;


        }
    }
    //seçili buttoon tıklaması alma
    public void onMasaButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((Button) view).hasOnClickListeners();
        // Check which checkbox was clicked
        switch(view.getId())
        {
            case R.id.btnMasa1:
                if (checked)
                {
                    buttonBilgiAc(1);

                }

                break;
            case R.id.btnMasa2:
                if (checked)
                {
                    buttonBilgiAc(2);

                }

                break;
            case R.id.btnMasa3:
                if (checked)
                {

                    buttonBilgiAc(3);
                }

                break;
            case R.id.btnMasa4:
                if (checked)
                {

                    buttonBilgiAc(4);
                }

                break;
            case R.id.btnMasa5:
                if (checked)
                {

                    buttonBilgiAc(5);
                }

                break;
            case R.id.btnMasa6:
                if (checked)
                {

                    buttonBilgiAc(6);
                }

                break;
            case R.id.btnMasa7:
                if (checked)
                {

                    buttonBilgiAc(7);
                }

                break;
            case R.id.btnMasa8:
            if (checked)
            {

                buttonBilgiAc(8);
            }

            break;
            case R.id.btnMasa9:
            if (checked)
            {

                buttonBilgiAc(9);
            }

            break;
            case R.id.btnMasa10:
            if (checked)
            {

                buttonBilgiAc(10);
            }

            break;
            case R.id.btnMasa11:
            if (checked)
            {

                buttonBilgiAc(11);
            }

            break;case R.id.btnMasa12:
            if (checked)
            {

                buttonBilgiAc(12);
            }

            break;case R.id.btnMasa13:
            if (checked)
            {

                buttonBilgiAc(13);
            }

            break;case R.id.btnMasa14:
            if (checked)
            {

                buttonBilgiAc(14);
            }

            break;case R.id.btnMasa15:
            if (checked)
            {

                buttonBilgiAc(15);
            }

            break;
            case R.id.btnMasa16:
                if (checked)
                {

                    buttonBilgiAc(16);
                }

                break;
            case R.id.btnMasa17:
                if (checked)
                {

                    buttonBilgiAc(17);
                }

                break;
            case R.id.btnMasa18:
                if (checked)
                {
                    buttonBilgiAc(18);
                }

                break;
            case R.id.btnMasa19:
                if (checked)
                {
                    buttonBilgiAc(19);
                }

                break;
            case R.id.btnMasa20:
                if (checked)
                {
                    buttonBilgiAc(20);
                }

                break;


        }
    }

    private void buttonBilgiAc(int btnID)
    {
        String strSpinner=spinnerTarihler.getSelectedItem().toString();;
        String[] spinnerTarihler = strSpinner.split("/");
        Intent ıntent=new Intent(this,MasaBilgi.class);
        ıntent.putExtra("btnID",btnID);
        ıntent.putExtra("gun",Integer.parseInt(spinnerTarihler[0]));
        ıntent.putExtra("ay",Integer.parseInt(spinnerTarihler[1])-1);
        ıntent.putExtra("yil",Integer.parseInt(spinnerTarihler[2]));
        startActivity(ıntent);

    }
    @Override
    public void onBackPressed() {

            moveTaskToBack(true);
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
    private void demoKontrol() {
        Calendar calender = Calendar.getInstance();
        calender.clear();
        calender.set(Calendar.MONTH, 1);
        calender.set(Calendar.DAY_OF_MONTH, 20);
        calender.set(Calendar.YEAR, 2019);
        calender.set(Calendar.HOUR, 12);
        calender.set(Calendar.MINUTE, 30);
        calender.set(Calendar.SECOND, 00);
        final Calendar takvim = Calendar.getInstance();
        long farkZaman = Calendar.getInstance().getTimeInMillis() - calender.getTimeInMillis();
        if (farkZaman > 0) {
            moveTaskToBack(true);
        }
    }
    public void open() {
        try {
            db = mVeritabani.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFinishExport(String error) {
        String notify = error;
        if (error == null) {
            notify = "Export success";
        }
        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishImport(String error) {
        String notify = error;
        if (error == null) {
            notify = "Import success";
            DataKontrol.veriEkle(context,yedekListe);
           Intent ıntent=new Intent(this,Anasayfa.class);
           startActivity(ıntent);

        }
        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
    }
    private boolean checkAndRequestPermissions() {
        int permissionYazma = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionOkuma= ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionYazma != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // Toast.makeText(this,   "WAKE_LOCK", Toast.LENGTH_LONG).show();
        }
        if (permissionOkuma != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}