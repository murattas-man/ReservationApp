package com.reservation.reservationapp;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataKontrol {
    public static Veritabani db;
    public  static  List<Note>  VeriYedkle(Context context)
    { List<Note> listNote=new ArrayList<>();
        db = new Veritabani(context);
        listNote.clear();
        ArrayList<Note> ls = db.getListNote("SELECT * FROM " + Veritabani.TABLE_NAME);
        for (int i = ls.size() - 1; i >= 0; i--) {
            listNote.add(ls.get(i));
        }
        return listNote;
    }
    public static void veriEkle(Context context,List<Note> listNote)
    {
        db = new Veritabani(context);
        ArrayList<Note> ls = db.getListNote("SELECT * FROM " + Veritabani.TABLE_NAME);
        for (int i = listNote.size() - 1; i >= 0; i--) {
            int sayac=0;
            for (int n=0;n<ls.size();n++)
            {
                if(listNote.get(i).getYil()==ls.get(n).getYil()&&
                        listNote.get(i).getAy()==ls.get(n).getAy()&&
                        listNote.get(i).getGun()==ls.get(n).getGun()&&
                        listNote.get(i).getMasa()==ls.get(n).getMasa())
                {
                    sayac++;
                }
                if(ls.get(n).getKontrol()==listNote.get(i).getKontrol())
                    sayac++;
            }
           // Toast.makeText(context, ""+listNote.get(i).getAy(), Toast.LENGTH_LONG).show();
            if(sayac==0)
            {
                Note note = new Note();
                note.setFullname(listNote.get(i).getFullname()).setParty(listNote.get(i).getParty()).setPhone(listNote.get(i).getPhone())
                        .setNotes(listNote.get(i).getNotes()).setMasa(listNote.get(i).getMasa()).setDate(listNote.get(i).getDate())
                        .setYil(listNote.get(i).getYil()).setAy(listNote.get(i).getAy()).setGun(listNote.get(i).getGun()).setSaat(listNote.get(i).getSaat())
                        .setDakika(listNote.get(i).getDakika()).setKontrol(listNote.get(i).getKontrol());
                db.insertNote(note);

            }

        }
    }
}
