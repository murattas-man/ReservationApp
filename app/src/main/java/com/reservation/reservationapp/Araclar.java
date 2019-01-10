package com.reservation.reservationapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Araclar {
    public static Veritabani db;
    public  static List<Note> veriSelect(Context context)
    { List<Note> listNote=new ArrayList<>();
        db = new Veritabani(context);
        listNote.clear();
        ArrayList<Note> ls = db.getListNote("SELECT * FROM " + Veritabani.TABLE_NAME);
        for (int i = ls.size() - 1; i >= 0; i--) {
            listNote.add(ls.get(i));
        }
        return listNote;
    }
}
