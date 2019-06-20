package Tools;

import android.widget.Button;
import android.widget.TextView;

import com.reservation.reservationapp.R;

/**
 * Created by Murattaş on 21.02.2019.
 */

public class Table {
    public static  Button btnMasa1,btnMasa2,btnMasa3,btnMasa4,btnMasa5,btnMasa6,btnMasa8,btnMasa9,btnMasa10,
            btnMasa11,btnMasa12,btnMasa14;
    public static TextView btntext1,btntext2,btntext3,btntext4,btntext5,btntext6,btntext8,btntext9,btntext10,
            btntext11,btntext12,btntext14;
    public static TextView btntext12x,btntext22,btntext32,btntext42,btntext52,btntext62,btntext82,btntext92,btntext102,
            btntext112,btntext122,btntext142;
    public static TextView btntext13,btntext23,btntext33,btntext43,btntext53,btntext63,btntext83,btntext93,btntext103,
            btntext113,btntext123,btntext143;
    public static int[] btnTextID={
            R.id.btntext1,
            R.id.btntext2,
            R.id.btntext3,
            R.id.btntext4,
            R.id.btntext5,
            R.id.btntext6,
            R.id.btntext8,
            R.id.btntext9,
            R.id.btntext10,
            R.id.btntext11,
            R.id.btntext12,
            R.id.btntext14,
    };
    public static int[] btnTextID2={
            R.id.btntext12x,
            R.id.btntext22,
            R.id.btntext32,
            R.id.btntext42,
            R.id.btntext52,
            R.id.btntext62,
            R.id.btntext82,
            R.id.btntext92,
            R.id.btntext102,
            R.id.btntext112,
            R.id.btntext122,
            R.id.btntext142,
    };
    public static int[] btnTextID3={
            R.id.btntext13,
            R.id.btntext23,
            R.id.btntext33,
            R.id.btntext43,
            R.id.btntext53,
            R.id.btntext63,
            R.id.btntext83,
            R.id.btntext93,
            R.id.btntext103,
            R.id.btntext113,
            R.id.btntext123,
            R.id.btntext143,
    };
    public static TextView[] btnTextler(){
        TextView[] textler=new TextView[12];
        textler[0]=btntext1;
        textler[1]=btntext2;
        textler[2]=btntext3;
        textler[3]=btntext4;
        textler[4]=btntext5;
        textler[5]=btntext6;
        textler[6]=btntext8;
        textler[7]=btntext9;
        textler[8]=btntext10;
        textler[9]=btntext11;
        textler[10]=btntext12;
        textler[11]=btntext14;
        return  textler;
    }
    public static TextView[] btnTextler2(){
        TextView[] textler=new TextView[12];
        textler[0]=btntext12x;
        textler[1]=btntext22;
        textler[2]=btntext32;
        textler[3]=btntext42;
        textler[4]=btntext52;
        textler[5]=btntext62;
        textler[6]=btntext82;
        textler[7]=btntext92;
        textler[8]=btntext102;
        textler[9]=btntext112;
        textler[10]=btntext122;
        textler[11]=btntext142;
        return  textler;
    }
    public static TextView[] btnTextler3(){
        TextView[] textler=new TextView[12];
        textler[0]=btntext13;
        textler[1]=btntext23;
        textler[2]=btntext33;
        textler[3]=btntext43;
        textler[4]=btntext53;
        textler[5]=btntext63;
        textler[6]=btntext83;
        textler[7]=btntext93;
        textler[8]=btntext103;
        textler[9]=btntext113;
        textler[10]=btntext123;
        textler[11]=btntext143;
        return  textler;
    }
    public static   int[] btnMasaID={
            R.id.btnMasa1,
            R.id.btnMasa2,
            R.id.btnMasa3,
            R.id.btnMasa4,
            R.id.btnMasa5,
            R.id.btnMasa6,
            R.id.btnMasa8,
            R.id.btnMasa9,
            R.id.btnMasa10,
            R.id.btnMasa11,
            R.id.btnMasa12,
            R.id.btnMasa14
    };
    public static Button[] btnMasalar(){
        Button[] butonlar=new Button[12];
        butonlar[0]=btnMasa1;
        butonlar[1]=btnMasa2;
        butonlar[2]=btnMasa3;
        butonlar[3]=btnMasa4;
        butonlar[4]=btnMasa5;
        butonlar[5]=btnMasa6;
        butonlar[6]=btnMasa8;
        butonlar[7]=btnMasa9;
        butonlar[8]=btnMasa10;
        butonlar[9]=btnMasa11;
        butonlar[10]=btnMasa12;
        butonlar[11]=btnMasa14;
        return  butonlar;
    }

    public static Button choose1,choose2,choose3,choose4,choose5,choose6,choose8,choose9,choose10,
            choose11,choose12,choose14;
    public static   int[] btnChooseID={
            R.id.btnchoose1,
            R.id.btnchoose2,
            R.id.btnchoose3,
            R.id.btnchoose4,
            R.id.btnchoose5,
            R.id.btnchoose6,
            R.id.btnchoose8,
            R.id.btnchoose9,
            R.id.btnchoose10,
            R.id.btnchoose11,
            R.id.btnchoose12,
            R.id.btnchoose14
    };
    public static Button[] btnChooseMasalar(){
        Button[] butonlar=new Button[12];
        butonlar[0]=choose1;
        butonlar[1]=choose2;
        butonlar[2]=choose3;
        butonlar[3]=choose4;
        butonlar[4]=choose5;
        butonlar[5]=choose6;
        butonlar[6]=choose8;
        butonlar[7]=choose9;
        butonlar[8]=choose10;
        butonlar[9]=choose11;
        butonlar[10]=choose12;
        butonlar[11]=choose14;
        return  butonlar;
    }

}
