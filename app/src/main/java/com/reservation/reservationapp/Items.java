package com.reservation.reservationapp;


public class Items {

    private String name;
    private int  gun;
    private int ay;
    private int yil;
    private int ID;

    //param
    private double toplam;
    private String paraSembol;

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
    public int getID(){
        return this.ID;
    }
    public void setName(String name){

        this.name = name;
    }
    public String getName(){

        return this.name;
    }

}