package com.reservation.reservationapp;


public class Items {

    private String name;
    private String partys;
    private int  gun;
    private int ay;
    private int yil;
    private int ID;
    private int C_ID;
    private int saat;
    private int dakika;
    private int date;

    //param

    public void setGun(int gun){
        this.gun=gun;
    }
    public int getGun(){
        return this.gun;
    }
    public void setAy(int ay){
        this.ay=ay;
    }
    public int getAy(){
        return this.ay;
    }public void setYil(int yil){
        this.yil=yil;
    }
    public int getYil(){
        return this.yil;
    }
    public void setID(int ID){
        this.ID=ID;
    }
    public void setCID(int C_ID){
        this.C_ID=C_ID;
    }
    public int getID(){
        return this.ID;
    }
    public int getCID(){
        return this.C_ID;
    }
    public void setName(String name){

        this.name = name;
    }
    public String getName(){

        return this.name;
    }

    public void setSaat(int saat){
        this.saat=saat;
    }
    public int getSaat(){
        return this.saat;
    }
    public void setDakika(int dakika){
        this.dakika=dakika;
    }
    public int getDakika(){
        return this.dakika;
    }
    public  void setPartys(String partys){
        this.partys=partys;
    }
    public String getPartys(){
        return this.partys;
    }
    public void setDate(int date){
        this.date=date;
    }
    public int getDate(){
        return this.date;
    }
}