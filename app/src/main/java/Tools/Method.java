package Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reservation.reservationapp.Anasayfa;
import com.reservation.reservationapp.Note;
import com.reservation.reservationapp.R;
import com.reservation.reservationapp.Veritabani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Murtican on 15.03.2019.
 */

public class Method {

    public  Veritabani mVeritabani;
    public  Context context;
    public Method(Context context){
        this.context=context;
        mVeritabani = new Veritabani(context);
    }
    public void showDialog(int myCheckID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final Note note=mVeritabani.getNote("select * from "+ Veritabani.TABLE_NAME+" where "+Veritabani.C_ID+"="+myCheckID);
        final List<Note> mynotes=mVeritabani.getListNote("select * from "+mVeritabani.TABLE_NAME+" where "
        +mVeritabani.DATE+"="+note.getDate());
        builder.setTitle("Costumer : "+note.getFullname());
        //list of items
        final int[] secili=new int[1];
        String[] items = context.getResources().getStringArray(R.array.ringtone_list);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        secili[0] =which;

                    }
                });

        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mynotes.size()>0)
                        for (Note no:mynotes) {
                            if(secili[0]==0){//arrived

                                no.setKontrol(2);
                                mVeritabani.updateNote(no);
                            }
                            else if(secili[0]==1){//didn't come
                                no.setKontrol(3);
                                mVeritabani.updateNote(no);
                            }
                            else if(secili[0]==2){
                                mVeritabani.deleteNote(mVeritabani.C_ID+"="+no.getId());
                            }
                        }

                        Intent intent=new Intent(context,Anasayfa.class);
                        context.startActivity(intent);

                    }
                });

        String negativeText = context.getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void buttonBilgiAc(int btnID,String getSpinner)
    {

        String strSpinner=getSpinner;
        String[] spinnerTarihler = strSpinner.split("/");
        final ArrayList<Note> myliste = mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                mVeritabani.R_YEAR + "=" + Integer.parseInt(spinnerTarihler[2]) +" and " +
                mVeritabani.R_MONTH + "=" + (Integer.parseInt(spinnerTarihler[1])-1) +" and "+mVeritabani.R_DAY + "=" + Integer.parseInt(spinnerTarihler[0]) +" and "+
                mVeritabani.MASANO+"="+btnID);
        if(myliste.size()>0){
        String[] strnotlar=new String[myliste.size()];
        SimpleDateFormat formatter = new SimpleDateFormat(context.getString(R.string.hour_minutes));
        try {
            List<Note> tableList = new ArrayList<Note>();

        for (int i = 0; i < myliste.size(); i++) {
            Calendar c=Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, myliste.get(i).getSaat());
            c.set(Calendar.MINUTE, myliste.get(i).getDakika());
            String timeString = formatter.format(new Date(c.getTimeInMillis()));
            int parti=0;
            tableList=mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where "+mVeritabani.DATE+"="+myliste.get(i).getDate());
            for (int p = 0; p <tableList.size() ; p++) {
                parti+=Integer.parseInt(tableList.get(p).getParty());
            }

            strnotlar[i] = myliste.get(i).getFullname()+" - "+timeString+" - Party: "+parti;
        }
        }catch (Exception eo){

        }
        LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.layout_listmasa, null);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        // set view for dialog
        builder.setView(convertView);
        builder.setTitle("Choose Customer").setIcon(R.mipmap.ic_launcher);

        final android.support.v7.app.AlertDialog alert = builder.create();

        ListView lv = (ListView) convertView.findViewById(R.id.lv_listmasa);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, strnotlar);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(myliste.get(position).getId());
                alert.dismiss();

            }
        });
        alert.show();

        }
    }
    public int getbuttonCheck(int btnID,String getSpinner)
    {
        String strSpinner=getSpinner;
        String[] spinnerTarihler = strSpinner.split("/");
        final ArrayList<Note> myliste = mVeritabani.getListNote("select * from " + mVeritabani.TABLE_NAME + " where " +
                mVeritabani.R_YEAR + "=" + Integer.parseInt(spinnerTarihler[2]) +" and " +
                mVeritabani.R_MONTH + "=" + (Integer.parseInt(spinnerTarihler[1])-1) +" and "+mVeritabani.R_DAY + "=" + Integer.parseInt(spinnerTarihler[0]) +" and "+
                mVeritabani.MASANO+"="+btnID);
        return myliste.size();
    }

}
