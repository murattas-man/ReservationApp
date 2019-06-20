package com.reservation.reservationapp;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

import Tools.ImagesInit;
import Tools.Init;
import Tools.Method;
import Tools.Table;

public class Anasayfa extends AppCompatActivity  implements  DatePickerDialog.OnDateSetListener,BackupData.OnBackupListener{
    TextView tvTarih,tvdatepic,tvtimepic;
    EditText etFullName,etTelno,etKisiSayisi,etNotes;
    TextView secimTarih;
    Spinner spinnerTarihler;
    Button btnsaatSec,btndateSec;

    Button btnSave;
    int masano,year, month,day,saat,dakika;
    String tablesno="";
    Dialog dialog;
    Dialog dialogadd,dialogcheck;
    SQLiteDatabase db;
    Veritabani mVeritabani;
    int backmirestoremi=0;

    private  String[] gunler=new String[180];
    private  String seciliGun;
    public List<Integer> multiChoose=new ArrayList<Integer>();
    public List<Integer> masatext=new ArrayList<Integer>();
    boolean saatKontrol=false,tarihKontrol=false;
    private Context context;
    private BackupData backupData;
    DateFormat df;
    SimpleDateFormat formatter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 21;
    private  List<Note> yedekListe;
    private  List<Note> datalar;
    boolean tarihsecKontrol=false;

    public Button[] btnMasalar= Table.btnMasalar();
    public Button[] btnChooseMasalar= Table.btnChooseMasalar();
    public TextView[] btnTextler=Table.btnTextler();
    public TextView[] btnTextler2=Table.btnTextler2();
    public TextView[] btnTextler3=Table.btnTextler3();
    public TextView tvtotal,tvtotalpeople;

    private AdapterCostumerList customAdapter;
    private ListView listview;
    private List<Note> fav_listnew = new ArrayList<Note>();
    private Method mymethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        context = this;
        init();
        listview = (ListView)findViewById(R.id.lvcostumer);

        mVeritabani = new Veritabani(this);
        mymethod=new Method(this);
        db= mVeritabani.getWritableDatabase();
        backupData = new BackupData(context);
        backupData.setOnBackupListener(this);
        try {

        Date simdikiZaman = new Date();
         df = new SimpleDateFormat(getString(R.string.dateformate));
         formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
        Calendar ctarih = Calendar.getInstance();
        seciliGun=df.format(simdikiZaman);
        ctarih.setTime(simdikiZaman);
            multiChoose.clear();
        for (int i=0;i<gunler.length;i++)
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
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    listGonder(position);

                }
            });

            for (int m = 0; m <btnMasalar.length; m++) {
                btnMasalar[m].setOnLongClickListener(mylistener);
            }

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
        String str=String.valueOf(etFullName.getText());
        String telnumara=String.valueOf(etTelno.getText()).trim();
        String partsayi=String.valueOf(etKisiSayisi.getText()).trim();
        String notes= String.valueOf(etNotes.getText()).trim();
        final int kontrol = 1;//
        final  int mydate=(int) System.currentTimeMillis();
        char c=Character.toUpperCase(str.charAt(0));
        str=c+str.substring(1);
        String[] iler= tablesno.split(",");
        int par=0,parti=Integer.parseInt(partsayi);
        for ( int i=0;i<multiChoose.size();i++)
        {
            if(parti>0 && i<(multiChoose.size()-1))
            {
                switch ( multiChoose.get(i)){
                    case 1: case 2:case 3: case 4: case 12: case 8:
                        if(parti>=4){
                            par=4;
                            parti-=par;
                        }else par=parti;
                        break;
                    case 5: case 14: case 9:  case 10: case 11:
                        if(parti>=2){
                            par=2;
                            parti-=par;
                        }else par=parti;
                        break;
                    case 6:
                        if(parti>=8){
                            par=8;
                            parti-=par;
                        }else par=parti;
                        break;
                }
            }
            else {
                par=parti;
            }

        Note note = new Note();
        note.setFullname(str).setParty(String.valueOf(par)).setPhone(telnumara).setNotes(notes).setMasa(multiChoose.get(i))
        .setDate(mydate).setYil(year).setAy(month).setGun(day).setSaat(saat).setDakika(dakika).setKontrol(kontrol);
            mVeritabani.insertNote(note);
        }
        notify = "Booking Successful";
        tablesno="";
        Toast.makeText(this, notify, Toast.LENGTH_LONG).show();
        etFullName.setText("");
        etKisiSayisi.setText("");
        etTelno.setText("");
        etNotes.setText("");
        btnsaatSec.setText("");
        btndateSec.setText("");
        saatKontrol=false;
        tarihKontrol=false;
        masano=0;
        tarihsecKontrol=false;
        gosterMasa();

    }
    private  void gosterMasa()
    {

        String strSpinner=spinnerTarihler.getSelectedItem().toString();
        String[] spinnerTarihler = strSpinner.split("/");

        datalar=Araclar.veriSelect(context);
        int total=0;
        fav_listnew.clear();
        fav_listnew=mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR + "=" + Integer.parseInt(spinnerTarihler[2]) + " and " + mVeritabani.R_MONTH + "=" + (Integer.parseInt(spinnerTarihler[1]) - 1)
                + " and " + mVeritabani.R_DAY + "=" + Integer.parseInt(spinnerTarihler[0])+"  group by "+mVeritabani.DATE
                +" order by "+mVeritabani.R_HOUR+","+mVeritabani.R_MINUTE);
        customAdapter = new AdapterCostumerList(getApplicationContext(),fav_listnew);
        listview.setAdapter(customAdapter);
        try {
            masatext.clear();
            for (int i = datalar.size() - 1; i >= 0; i--)
        {


            if(datalar.get(i).getYil()==Integer.parseInt(spinnerTarihler[2]) &&
               datalar.get(i).getAy()==(Integer.parseInt(spinnerTarihler[1])-1) &&
               datalar.get(i).getGun()==Integer.parseInt(spinnerTarihler[0]))
            {
                     masaDoldur(datalar.get(i).getMasa(),datalar.get(i).getKontrol(),datalar.get(i).getSaat(),datalar.get(i).getDakika(),
                             datalar.get(i).getYil(),datalar.get(i).getAy(),datalar.get(i).getGun());
                total+=Integer.parseInt(datalar.get(i).getParty());


            }
        }
        if(total>0){
            tvtotal.setVisibility(View.VISIBLE);
            tvtotalpeople.setVisibility(View.VISIBLE);
            tvtotalpeople.setText(total+" people");
        }
        }catch (Exception eo){
            Toast.makeText(this, "eror(501)"+eo.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void masaDoldur(int gelenNo,int check,int s,int d,int myyear,int mymount,int myday) {

        if(!masatext.contains(gelenNo)){
            masatext.add(gelenNo);
            List<Note> mynotlar=mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                    mVeritabani.R_YEAR + "=" + myyear + " and " + mVeritabani.R_MONTH + "="
                    + mymount + " and " + mVeritabani.R_DAY + "=" + myday+" and "+mVeritabani.MASANO+"="+gelenNo
                    +" order by "+mVeritabani.R_HOUR+","+mVeritabani.R_MINUTE);
            textdoldur(mynotlar);

        }
        if(gelenNo<=6)
        {
            if(check==2) {
                btnMasalar[gelenNo - 1].setBackgroundResource(ImagesInit.arrivedMasalar[gelenNo - 1]);
                btnMasalar[gelenNo-1].setEnabled(true);
            }
            else if(check==1){
            btnMasalar[gelenNo-1].setBackgroundResource(ImagesInit.doluMasalar[gelenNo-1]);
                btnMasalar[gelenNo-1].setEnabled(true);}

        }
        else if(gelenNo<=12)
        { if(check==2) {
            btnMasalar[gelenNo - 2].setBackgroundResource(ImagesInit.arrivedMasalar[gelenNo - 2]);
            btnMasalar[gelenNo-2].setEnabled(true);
        }
        else if(check==1){
            btnMasalar[gelenNo-2].setBackgroundResource(ImagesInit.doluMasalar[gelenNo-2]);
            btnMasalar[gelenNo-2].setEnabled(true);}
        }
        else if(gelenNo==14)
        { if(check==2) {
            btnMasalar[gelenNo - 3].setBackgroundResource(ImagesInit.arrivedMasalar[gelenNo - 3]);
            btnMasalar[gelenNo-3].setEnabled(true);
        }
        else if(check==1){
            btnMasalar[gelenNo-3].setBackgroundResource(ImagesInit.doluMasalar[gelenNo-3]);
            btnMasalar[gelenNo-3].setEnabled(true);}
        }



    }

    private void textdoldur(List<Note> mynotlar) {
        int count=0;
        for (Note note:mynotlar) {
            if(note.getKontrol()==1){
            count++;
            int deger=0;
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, note.getSaat());
        c.set(Calendar.MINUTE,note.getDakika());
        String timeString = formatter.format(new Date(c.getTimeInMillis()));
            if(note.getMasa()<=6)
                deger=note.getMasa()-1;
            else if(note.getMasa()<=12)
                deger=note.getMasa()-2;
            else if(note.getMasa()==14)
                deger=11;
            if(count==1){
                btnTextler[deger].setVisibility(View.VISIBLE);
                btnTextler[deger].setText(timeString+"");
            }else if(count==2){
                btnTextler2[deger].setVisibility(View.VISIBLE);
                btnTextler2[deger].setText(timeString+"");
            }
            else{
                btnTextler3[deger].setVisibility(View.VISIBLE);
                btnTextler3[deger].setText(timeString+"");
            }
        }
    }
    }

    private  void bosMasa(){

        for(int i=0;i<btnMasalar.length;i++){
            btnMasalar[i].setBackgroundResource(ImagesInit.bosMasalar[i]);
            btnTextler[i].setVisibility(View.INVISIBLE);
            btnTextler2[i].setVisibility(View.INVISIBLE);
            btnTextler3[i].setVisibility(View.INVISIBLE);
        }
    }

    private void masaSec() {

        dialog = new Dialog(Anasayfa.this);
        dialog.setContentView(R.layout.choose_table);
        dialog.setTitle(""+getString(R.string.mSec));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        // set height for dialog
        int height =  WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
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
        multiChoose.clear();
        while (cursor.moveToNext())
        {
            ArrayList<Note> myliste = mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                    mVeritabani.R_YEAR + "=" + year +" and " +
                    mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day +" and "+
                    mVeritabani.MASANO+"="+ cursor.getInt(cursor.getColumnIndex(mVeritabani.MASANO)));

            if(myliste.size()>2)
            {
                int i=cursor.getInt(cursor.getColumnIndex(mVeritabani.MASANO));
                switch (i){
                    case 1:  btnChooseMasalar[0].setBackgroundResource(R.mipmap.d1);
                        btnChooseMasalar[0].setEnabled(false);
                        break;
                    case 2: btnChooseMasalar[1].setBackgroundResource(R.mipmap.d2);
                        btnChooseMasalar[1].setEnabled(false);
                        break;
                    case 3: btnChooseMasalar[2].setBackgroundResource(R.mipmap.d3);
                        btnChooseMasalar[2].setEnabled(false);
                        break;
                    case 4: btnChooseMasalar[3].setBackgroundResource(R.mipmap.d4);
                        btnChooseMasalar[3].setEnabled(false);
                        break;
                    case 5: btnChooseMasalar[4].setBackgroundResource(R.mipmap.d5);
                        btnChooseMasalar[4].setEnabled(false);
                        break;
                    case 6: btnChooseMasalar[5].setBackgroundResource(R.mipmap.d6);
                        btnChooseMasalar[5].setEnabled(false);
                        break;
                    case 8: btnChooseMasalar[6].setBackgroundResource(R.mipmap.d8);
                        btnChooseMasalar[6].setEnabled(false);
                        break;
                    case 9: btnChooseMasalar[7].setBackgroundResource(R.mipmap.d9);
                        btnChooseMasalar[7].setEnabled(false);
                        break;
                    case 10: btnChooseMasalar[8].setBackgroundResource(R.mipmap.d10);
                        btnChooseMasalar[8].setEnabled(false);
                        break;
                    case 11: btnChooseMasalar[9].setBackgroundResource(R.mipmap.d11);
                        btnChooseMasalar[9].setEnabled(false);
                        break;
                    case 12: btnChooseMasalar[10].setBackgroundResource(R.mipmap.d12);
                        btnChooseMasalar[10].setEnabled(false);
                        break;
                    case 14: btnChooseMasalar[11].setBackgroundResource(R.mipmap.d14);
                        btnChooseMasalar[11].setEnabled(false);
                        break;
                    default:
                }
            }

        }
        dialog.show();
    }

    private void costumerNewAdd(String date) {

        dialogadd = new Dialog(Anasayfa.this);
        dialogadd.setContentView(R.layout.layout_newadd);
        dialogadd.setTitle("   ");
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        // set height for dialog
        int height =  WindowManager.LayoutParams.WRAP_CONTENT;
        dialogadd.getWindow().setLayout(width, height);
        dialogadd.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialogaddInit();
        if(date.length()>0){
        btndateSec.setText(" "+date);
        tarihKontrol=true;
        tarihsecKontrol=true;
        }
        dialogadd.show();
    }

    public  void onClickNewCostumer(View view)
    {
        boolean click = ((Button) view).hasOnClickListeners();
        switch (view.getId())
        {
            case R.id.btnKaydet:
                if (click)
                {
                    if(etFullName.length()>0)
                    {
                        if(etKisiSayisi.length()>0) {
                            if(saatKontrol && tarihKontrol)
                            {
                                if (multiChoose.size()>0) {
                                    dialogadd.dismiss();
                                    kaydet();
                                }else Toast.makeText(Anasayfa.this, ""+getString(R.string.fullnameKontrol), Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(Anasayfa.this, ""+getString(R.string.fullnameKontrol), Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(Anasayfa.this, ""+getString(R.string.fullnameKontrol), Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(Anasayfa.this, ""+getString(R.string.fullnameKontrol), Toast.LENGTH_SHORT).show();

                }
                break;
        }
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
                    if(tarihsecKontrol)
                    {
                        //openPickerDialog();
                        saatSecEkrani();
                    }
                    else Toast.makeText(context, getString(R.string.dateKontrol), Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void saatSecEkrani() {
        LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.layout_digitalsaat, null);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        // set view for dialog
        builder.setView(convertView);
        builder.setTitle("Choose Time").setIcon(R.mipmap.ic_launcher);

        final android.support.v7.app.AlertDialog alert = builder.create();

        final Spinner mSpinnerSaat=convertView.findViewById(R.id.spinnersaat);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(
                this, R.array.note_saat, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSaat.setAdapter(adapterSpinner);
        final ArrayList<Note> myliste = mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                mVeritabani.R_YEAR + "=" + year +" and " +
                mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day +" and "+
                mVeritabani.MASANO+"="+masano);
        mSpinnerSaat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                //Toast.makeText(context, ""+mSpinnerSaat.getSelectedItem(), Toast.LENGTH_SHORT).show();
               if(id!=0){
                   String strSpinner = mSpinnerSaat.getSelectedItem().toString();
                   String[] selamSpin = strSpinner.split(":");
                   int saatA=Integer.parseInt(selamSpin[0].trim());
                   int dakikaA=Integer.parseInt(selamSpin[1].trim());
                   int kontrolSaatBak=0;
                   for (int a=0;a<myliste.size();a++){
                       if(myliste.get(a).getSaat()==saatA &&myliste.get(a).getDakika()==dakikaA ){
                           kontrolSaatBak++;
                       }
                   }
                   if(kontrolSaatBak>0){
                       Toast.makeText(context, "Please choose a different Time", Toast.LENGTH_SHORT).show();
                   }else{
                       btnsaatSec.setText(mSpinnerSaat.getSelectedItem()+"");
                       saat=saatA;
                       dakika=dakikaA;

                       saatKontrol=true;
                       alert.dismiss();
                   }
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        alert.show();
    }

    View.OnLongClickListener mylistener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Button longclik = (Button) view;
            multiChoose.clear();
            boolean check=false;
            for(int i=0;i<btnMasalar.length;i++){
                if(view.getId()==Table.btnMasaID[i])
                {
                    if(i<6)
                    {
                        if(mymethod.getbuttonCheck(i+1,spinnerTarihler.getSelectedItem().toString())>2)
                        { check=true;
                        }
                        multiChoose.add(i+1);
                    }
                    else if(i<11) {
                        if(mymethod.getbuttonCheck(i+2,spinnerTarihler.getSelectedItem().toString())>2)
                        { check=true;
                       }
                        multiChoose.add(i+2);
                    }
                    else if(i==11) {
                        if(mymethod.getbuttonCheck(i+3,spinnerTarihler.getSelectedItem().toString())>2)
                        { check=true;
                        }
                        multiChoose.add(i+3);
                    }
                    break;
                }
            }
            if(!check){
            String strSpinner=spinnerTarihler.getSelectedItem().toString();
            String[] spinnerTarihler = strSpinner.split("/");
            year=Integer.parseInt(spinnerTarihler[2]);
            month=(Integer.parseInt(spinnerTarihler[1])-1);
            day=Integer.parseInt(spinnerTarihler[0]);
            Calendar c=Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,day);

            SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
            String dateString = dateformatter.format(new Date(c.getTimeInMillis()));

            costumerNewAdd(dateString);

            return true;}
            else {

                return false;}
        }
    };
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
            case R.id.action_add:
                costumerNewAdd("");
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
        tarihKontrol=true;
        open();
        tablesno="";
        masaSec();
        tarihsecKontrol=true;
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
            saatKontrol=true;
        }};

    private  void init()
    {


        tvTarih=(TextView) findViewById(R.id.tvtarih);
        spinnerTarihler=findViewById(R.id.spinnerTarihler);
        tvtotalpeople=findViewById(R.id.tvtotalpeople);
        tvtotalpeople.setVisibility(View.INVISIBLE);
        tvtotal=findViewById(R.id.tvtotal);
        tvtotal.setVisibility(View.INVISIBLE);

        for (int i=0;i<btnMasalar.length;i++) {
            btnMasalar[i]=(Button) findViewById(Table.btnMasaID[i]);
            btnTextler[i]=(TextView)findViewById(Table.btnTextID[i]);
            btnTextler2[i]=(TextView)findViewById(Table.btnTextID2[i]);
            btnTextler3[i]=(TextView)findViewById(Table.btnTextID3[i]);
    }

    }
    private void dialogaddInit() {

        etFullName=(EditText) dialogadd.findViewById(R.id.etFullName);
        etTelno=(EditText) dialogadd.findViewById(R.id.etTel);
        etKisiSayisi=(EditText) dialogadd.findViewById(R.id.etKisisayisi);
        tvdatepic=(TextView) dialogadd.findViewById(R.id.tvDatepic);
        tvtimepic=(TextView)dialogadd.findViewById(R.id.tvTime);
        etNotes=(EditText) dialogadd.findViewById(R.id.etNotes);
        btnsaatSec=(Button) dialogadd.findViewById(R.id.btnsaatSec);
        btndateSec=(Button) dialogadd.findViewById(R.id.btndatesec);
        btnSave=(Button) dialogadd.findViewById(R.id.btnKaydet);
    }

    private void dialogButtonInit() {
        for (int i=0;i<btnChooseMasalar.length;i++)
            btnChooseMasalar[i]=(Button) dialog.findViewById(Table.btnChooseID[i]);

        secimTarih=dialog.findViewById(R.id.tvtarih);
    }


    //seçili buttoon tıklaması alma
    public void onButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((Button) view).hasOnClickListeners();
        // Check which checkbox was clicked
        switch (view.getId()){
            case R.id.btnchoose1:
                if (!multiChoose.contains(1)){
                    btnChooseMasalar[0].setBackgroundResource(R.mipmap.d1);
                    multiChoose.add(1);
                }
                else{
                    btnChooseMasalar[0].setBackgroundResource(R.mipmap.b1);
                    multiChoose.remove((Integer)1);
                }
                break;
            case R.id.btnchoose2:
                if (!multiChoose.contains(2)){
                    btnChooseMasalar[1].setBackgroundResource(R.mipmap.d2);
                    multiChoose.add(2);
                }
                else{
                    btnChooseMasalar[1].setBackgroundResource(R.mipmap.b2);
                    multiChoose.remove((Integer)2);
                }
                break;
            case R.id.btnchoose3:
                if (!multiChoose.contains(3)){
                    btnChooseMasalar[2].setBackgroundResource(R.mipmap.d3);
                    multiChoose.add(3);
                }
                else{
                    btnChooseMasalar[2].setBackgroundResource(R.mipmap.b3);
                    multiChoose.remove((Integer)3);
                }
                break;
            case R.id.btnchoose4:
                if (!multiChoose.contains(4)){
                    btnChooseMasalar[3].setBackgroundResource(R.mipmap.d4);
                    multiChoose.add(4);
                }
                else{
                    btnChooseMasalar[3].setBackgroundResource(R.mipmap.b4);
                    multiChoose.remove((Integer)4);
                }
                break;
            case R.id.btnchoose5:
                if (!multiChoose.contains(5)){
                    btnChooseMasalar[4].setBackgroundResource(R.mipmap.d5);
                    multiChoose.add(5);
                }
                else{
                    btnChooseMasalar[4].setBackgroundResource(R.mipmap.b5);
                    multiChoose.remove((Integer)5);
                }
                break;
            case R.id.btnchoose6 :
                if (!multiChoose.contains(6)){
                btnChooseMasalar[5].setBackgroundResource(R.mipmap.d6);
                multiChoose.add(6);
            }
            else{
                btnChooseMasalar[5].setBackgroundResource(R.mipmap.b6);
                multiChoose.remove((Integer)6);
            }
                break;
            case R.id.btnchoose8:
                if (!multiChoose.contains(8)){
                    btnChooseMasalar[6].setBackgroundResource(R.mipmap.d8);
                    multiChoose.add(8);
                }
                else{
                    btnChooseMasalar[6].setBackgroundResource(R.mipmap.b8);
                    multiChoose.remove((Integer)8);
                }
                break;
            case R.id.btnchoose9:
                if (!multiChoose.contains(9)){
                    btnChooseMasalar[7].setBackgroundResource(R.mipmap.d9);
                    multiChoose.add(9);
                }
                else{
                    btnChooseMasalar[7].setBackgroundResource(R.mipmap.b9);
                    multiChoose.remove((Integer)9);
                }
                break;
            case R.id.btnchoose10:
                if (!multiChoose.contains(10)){
                    btnChooseMasalar[8].setBackgroundResource(R.mipmap.d10);
                    multiChoose.add(10);
                }
                else{
                    btnChooseMasalar[8].setBackgroundResource(R.mipmap.b10);
                    multiChoose.remove((Integer)10);
                }
                break;
            case R.id.btnchoose11:
                if (!multiChoose.contains(11)){
                    btnChooseMasalar[9].setBackgroundResource(R.mipmap.d11);
                    multiChoose.add(11);
                }
                else{
                    btnChooseMasalar[9].setBackgroundResource(R.mipmap.b11);
                    multiChoose.remove((Integer)11);
                }
                break;
            case R.id.btnchoose12:
                if (!multiChoose.contains(12)){
                    btnChooseMasalar[10].setBackgroundResource(R.mipmap.d12);
                    multiChoose.add(12);
                }
                else{
                    btnChooseMasalar[10].setBackgroundResource(R.mipmap.b12);
                    multiChoose.remove((Integer)12);
                }
                break;
            case R.id.btnchoose14:
                if (!multiChoose.contains(14)){
                    btnChooseMasalar[11].setBackgroundResource(R.mipmap.d14);
                    multiChoose.add(14);
                }
                else{
                    btnChooseMasalar[11].setBackgroundResource(R.mipmap.b14);
                    multiChoose.remove((Integer)14);
                }
                break;
            default:
        }
       /* for(int i=0;i<btnChooseMasalar.length;i++){
            if(view.getId()==Table.btnChooseID[i])
            {
                if(i<6)
                    masano=i+1;
                else if(i<11)
                    masano=i+2;
                else if(i==11)
                masano=i+3;
                dialog.dismiss();
            }
        }*/

    }
    public  void onTamamClicked(View view){
        boolean checked = ((Button) view).hasOnClickListeners();
        if (view.getId()==R.id.coklubtnsesim){
            dialog.dismiss();

        }
    }
    //seçili buttoon tıklaması alma
    public void onMasaButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((Button) view).hasOnClickListeners();
        // Check which checkbox was clicked
        for(int i=0;i<btnMasalar.length;i++){
            if(view.getId()==Table.btnMasaID[i])
            {
                if(i<6)
                mymethod.buttonBilgiAc(i+1,spinnerTarihler.getSelectedItem().toString());
                else if(i<11)
                    mymethod.buttonBilgiAc(i+2,spinnerTarihler.getSelectedItem().toString());
                else if(i==11)
                    mymethod.buttonBilgiAc(i+3,spinnerTarihler.getSelectedItem().toString());
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

            moveTaskToBack(true);
    }
    private void masaAktifET() {
        for(int i=0;i<btnChooseMasalar.length;i++){
            btnChooseMasalar[i].setEnabled(true);
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
    private void listGonder(int position){

        Intent intent = new Intent(getApplicationContext(),MasaBilgi.class);

        intent.putExtra("ID",customAdapter.getItem(position).getId());
        startActivity(intent);
    }

}