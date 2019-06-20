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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Tools.Table;

public class MasaBilgi extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{
    TextView tvdatepic,tvtimepic;
    EditText etFullName,etTelno,etKisiSayisi,etNotes;
    TextView   secimTarih;
    SQLiteDatabase db;
    Veritabani mVeritabani;
    Button btnSil,btnGuncelle,btnMasaNo;
    Button btnSaatsec,btnDatesec;
    int RID=1,masano,dateID=0;
    int year, month,day,saat,dakika;
    String tablesno="";

    Button[] btnChooseMasalar= Table.btnChooseMasalar();
    Dialog dialog;
    SimpleDateFormat dateformatter;
    SimpleDateFormat formatter;
    boolean tarihsecKontrol=true;
    public int parti=0;

    public List<Integer> multiChoose=new ArrayList<Integer>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anabilgi);
        try {
            multiChoose.clear();
        Bundle bundle = getIntent().getExtras();
        final int id = bundle.getInt("ID");
        init();
        mVeritabani = new Veritabani(this);
        db= mVeritabani.getWritableDatabase();
        dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
        formatter = new SimpleDateFormat(getString(R.string.hour_minutes));

        Note myNote=mVeritabani.getNote("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.C_ID + "=" + id);
            if (myNote != null) {
                etFullName.setText(myNote.getFullname());
                    etTelno.setText(myNote.getPhone());
                    btnMasaNo.setText(getTablesNo(myNote.getDate())+"");
                    etKisiSayisi.setText(parti+"");
                    etNotes.setText(myNote.getNotes()+"");
                   RID=myNote.getId();
                   dateID=myNote.getDate();
                   masano=id;
                   year=myNote.getYil();
                   month=myNote.getAy();
                   day=myNote.getGun();
                   dakika=myNote.getDakika();
                   saat=myNote.getSaat();
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
                            sil(RID,dateID);
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

                            try {
                                guncelle(RID);
                            }catch (Exception eo){

                            }

                        }
                    });
                    builder.show();

                }
            });

            btnMasaNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        masaSec();
                    }catch (Exception eo){

                    }

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
        String partsayi=String.valueOf(etKisiSayisi.getText()).trim();
        str=c+str.substring(1);
       Note notum=mVeritabani.getNote("Select * From "+mVeritabani.TABLE_NAME+" where "+mVeritabani.C_ID+
       "="+ID);
        List<Note> notlar=mVeritabani.getListNote("Select * From "+mVeritabani.TABLE_NAME+" where "+mVeritabani.DATE+
                "="+notum.getDate());
        str=c+str.substring(1);
        int par=0,parti=Integer.parseInt(partsayi);
       if (multiChoose.size()==0){
           int count=0;
           for (Note note:notlar ) {
               note.setFullname(str);
               note.setPhone(String.valueOf(etTelno.getText()));
               note.setNotes(String.valueOf(etNotes.getText()));
               note.setMasa(note.getMasa());
               count++;
               if(parti>0 && count<notlar.size())
               {
                   switch (note.getMasa()){
                       case 1: case 2: case 3:case 4:case 12:case 8:
                           if(parti>=4){
                               par=4;
                               parti-=par;
                           }else par=parti;
                           break;
                       case 5: case 14:case 9:case 10:case 11:
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
               note.setParty(String.valueOf(par));
               note.setDate(notum.getDate());
               note.setYil(year);
               note.setAy(month);
               note.setGun(day);
               note.setSaat(saat);
               note.setDakika(dakika);
               mVeritabani.updateNote(note);
           }
    }else{
          mVeritabani.deleteNote(mVeritabani.DATE+"="+notum.getDate());

           final  int mydate=(int) System.currentTimeMillis();
           for ( int i=0;i<multiChoose.size();i++)
           {
               if(parti>0 && i<(multiChoose.size()-1))
               {
                   switch ( multiChoose.get(i)){
                       case 1: case 2: case 3:case 4:case 12:case 8:
                           if(parti>=4){
                               par=4;
                               parti-=par;
                           }else par=parti;
                           break;
                       case 5: case 14:case 9:case 10:case 11:
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
               note.setFullname(str);
               note.setPhone(String.valueOf(etTelno.getText()));
               note.setNotes(String.valueOf(etNotes.getText()));
               note.setMasa(multiChoose.get(i));
               note.setParty(String.valueOf(par));
               note.setDate(mydate);
               note.setYil(year);
               note.setAy(month);
               note.setKontrol(1);
               note.setGun(day);
               note.setSaat(saat);
               note.setDakika(dakika);
               mVeritabani.insertNote(note);
           }
       }
        Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();

        Intent ıntent=new Intent(MasaBilgi.this,Anasayfa.class);
        startActivity(ıntent);
    }
    private void sil(int ID,int d)
    {
        try {
            List<Note> deleteNotes=mVeritabani.getListNote("select * from "+mVeritabani.TABLE_NAME+" where "
            +mVeritabani.DATE+"="+d);
            if(deleteNotes.size()>0){
                for (Note no:deleteNotes) {
                    mVeritabani.deleteNote(mVeritabani.C_ID+"="+no.getId());
                }
            }
            Toast.makeText(this, "Delete Successful", Toast.LENGTH_LONG).show();
        }catch (Exception eo){
            Toast.makeText(this, "Delete UnSuccessful", Toast.LENGTH_LONG).show();
        }
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
                    if(tarihsecKontrol)
                    {
                        //openPickerDialog();
                        saatSecEkrani();
                    }
                   // openPickerDialog();
                break;
        }
    }

    private void saatSecEkrani() {
        LayoutInflater inflater = ((FragmentActivity) MasaBilgi.this).getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.layout_digitalsaat, null);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MasaBilgi.this, R.style.AppCompatAlertDialogStyle);

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
                        Toast.makeText(MasaBilgi.this, "Please choose a different Time", Toast.LENGTH_SHORT).show();
                    }else{
                        btnSaatsec.setText(mSpinnerSaat.getSelectedItem()+"");
                        saat=saatA;
                        dakika=dakikaA;

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

    private void masaSec() {
        dialog = new Dialog(MasaBilgi.this);
        dialog.setContentView(R.layout.choose_table);
        dialog.setTitle(""+getString(R.string.mSec));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        // set height for dialog
        int height =  WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialogButtonInit();
        ArrayList<Note> notes=mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " + mVeritabani.R_YEAR + "=" + year +" and " +
                mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day );
        Calendar dcalendar=Calendar.getInstance();
        dcalendar.set(Calendar.YEAR,year);
        dcalendar.set(Calendar.MONTH,month);
        dcalendar.set(Calendar.DAY_OF_MONTH,day);
        String timeString = dateformatter.format(new Date(dcalendar.getTimeInMillis()));;
        secimTarih.setText("Date :"+timeString);
        masaAktifET();
        multiChoose.clear();
        for (Note note:notes) {
            ArrayList<Note> myliste = mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                    mVeritabani.R_YEAR + "=" + year +" and " +
                    mVeritabani.R_MONTH + "=" + month +" and "+mVeritabani.R_DAY + "=" + day +" and "+
                    mVeritabani.MASANO+"="+ note.getMasa());
            if(myliste.size()>2)
            {
                int i=note.getMasa();
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
    private void dialogButtonInit() {
        for (int i=0;i<btnChooseMasalar.length;i++)
            btnChooseMasalar[i]=(Button) dialog.findViewById(Table.btnChooseID[i]);

        secimTarih=dialog.findViewById(R.id.tvtarih);
    }
    private void masaAktifET() {
        for(int i=0;i<btnChooseMasalar.length;i++){
            btnChooseMasalar[i].setEnabled(true);
        }
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
        try {
            masaSec();
        }catch (Exception eo){

        }

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
        }};

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

    void faceFace(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    public  String getTablesNo(int id){
        String str="";
        parti=0;
        List<Note> tableList = new ArrayList<Note>();
        try {
            tableList=mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where "+mVeritabani.DATE+"="+id);
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
