package com.reservation.reservationapp;

public class Note {
    private int id;
    private String fullname;
    private String phone;
    private String party;
    private String notes;
    private int date;
    private int masa;
    private int yil;
    private int ay;
    private int gun;
    private int saat;
    private int dakika;
    private  int kontrol;

    public int getId()
    {
        return id;
    }
    public Note setID(int id)
    {
        this.id = id;
        return this;
    }
    public  String getFullname()
    {
        return  fullname;
    }
    public Note setFullname(String fullname)
    {
        this.fullname=fullname;
        return this;
    }
    public  String getPhone()
    {
        return  phone;
    }
    public Note setPhone(String phone)
    {
        this.phone=phone;
        return this;
    }
    public  String getParty()
    {
        return  party;
    }
    public Note setParty(String party)
    {
        this.party=party;
        return this;
    }
    public  int getDate()
    {
        return  date;
    }
    public Note setDate(int date)
    {
        this.date=date;
        return this;
    }
    public  String getNotes()
    {
        return  notes;
    }
    public Note setNotes(String notes)
    {
        this.notes=notes;
        return this;
    }
    public int getYil()
    {
        return yil;
    }
    public Note setYil(int yil)
    {
        this.yil = yil;
        return this;
    }
    public int getAy()
    {
        return ay;
    }
    public Note setAy(int ay)
    {
        this.ay = ay;
        return this;
    }
    public int getGun()
    {
        return gun;
    }
    public Note setGun(int gun)
    {
        this.gun = gun;
        return this;
    }
    public int getSaat()
    {
        return saat;
    }
    public Note setSaat(int saat)
    {
        this.saat = saat;
        return this;
    }
    public int getDakika()
    {
        return dakika;
    }
    public Note setDakika(int dakika)
    {
        this.dakika = dakika;
        return this;
    }
    public int getMasa()
    {
        return masa;
    }
    public Note setMasa(int masa)
    {
        this.masa = masa;
        return this;
    }
    public  int getKontrol(){return kontrol;}
    public  Note setKontrol(int kontrol)
    {
        this.kontrol=kontrol;
        return this;
    }

}
