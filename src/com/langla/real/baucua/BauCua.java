package com.langla.real.baucua;

import com.langla.lib.Utlis;
import com.langla.real.bangxephang.BangXepHang;
import com.langla.real.bangxephang.Bxh_Tpl;
import com.langla.real.item.Item;
import com.langla.real.other.Thu;
import com.langla.real.player.Char;
import com.langla.real.player.CharDB;
import com.langla.real.player.PlayerManager;
import com.langla.server.main.Main;
import com.langla.server.main.Maintenance;
import com.langla.utlis.UTPKoolVN;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author PKoolVN
 **/
public class BauCua {
    protected static BauCua Instance;

    public static BauCua gI() {
        if (Instance == null)
            Instance = new BauCua();
        return Instance;
    }
    public int ketQua = 0;

    public String ketQuaText = "Chưa có";

    public long tongCuoc1 = 0;

    public long tongCuoc2 = 0;

    public long time = System.currentTimeMillis()+65000;

    public boolean isSetNumber = true;
    public ArrayList<BauCuaTpl> listChar = new ArrayList<BauCuaTpl>();

    public void Start() {
        new Thread(this::update, "Bầu Cua").start();
    }

    public void update(){
        while (!Maintenance.isRuning) {
            try {
                long now = System.currentTimeMillis();

                if (time <= now && !isSetNumber)
                {
                    time = System.currentTimeMillis()+65000;
                    isSetNumber = true;
                    handle();
                }
                else if (time - now <= 5000 && isSetNumber)
                {
                    isSetNumber = false;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                Utlis.logError(BauCua.class, ex , "Da say ra loi:\n" + ex.getMessage());
            }
        }
        UTPKoolVN.Print("Bau Cua Close Success..!!");
    }

    public void handle(){
        try {

            ketQua = Utlis.nextInt(1,2);
            ArrayList<BauCuaTpl> listWin = new ArrayList<BauCuaTpl>();
            for (BauCuaTpl bauCuaTpl: listChar){
                if(bauCuaTpl.cuoc == ketQua){
                    listWin.add(bauCuaTpl);
                }
            }
            switch (ketQua) {
                case 1:
                    ketQuaText = "TÔM";
                    break;
                case 2:
                    ketQuaText = "CUA";
                    break;
                default:
                    break;
            }

            if(listWin.size() > 0) {
                handleGift(listWin);
            } else {
                PlayerManager.getInstance().chatWord("Kết quả Bầu Cua lần này là: "+ketQuaText+" rất tiếc đã không có ai may mắn");
            }
            tongCuoc1 = 0;
            tongCuoc2 = 0;
            listChar.clear();
        } catch (Exception ex) {
            Utlis.logError(BauCua.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void handleGift(ArrayList<BauCuaTpl> listWin) {
        try {
            int amount = 0;

            int rand = Utlis.nextInt(1,3);
            for (BauCuaTpl bauCuaTpl: listWin){
                Char charWin = PlayerManager.getInstance().getChar(bauCuaTpl.idChar);
                Thu thu = new Thu();
                thu.chuDe = "Thưởng Mini Game Bầu Cua";
                thu.nguoiGui = "Hệ thống";
                thu.noiDung = "Phần thưởng tham gia mini game Bầu Cua";
                if(rand == 1){
                    double xvang = 1.5 + Math.random() * 0.5;
                    int a = (int) (bauCuaTpl.vangCuoc * xvang);
                    thu.vang = a;
                    amount += a;
                } else if(rand == 2){
                    double xso = 0.05 + Math.random() * 0.05;
                    Item voso = new Item(176);
                    int a = (int) (bauCuaTpl.vangCuoc * xso);
                    voso.amount = a;
                    thu.item = voso;
                    amount += a;
                } else if(rand == 3){
                    int xchar = Utlis.nextInt(10,15);
                    Item item = new Item(763);
                    int a =  bauCuaTpl.vangCuoc * xchar;
                    item.amount = a;
                    thu.item = item;
                    amount += a;
                }
                if(charWin == null) {
                    thu.id = 999;
                    CharDB.guiThuOffline(bauCuaTpl.idChar,thu);
                } else {
                    thu.id = charWin.baseIdThu++;
                    charWin.listThu.add(thu);
                    charWin.client.session.serivce.updateThu();
                }
            }

            if(rand == 1){
                PlayerManager.getInstance().chatWord("Chúc mừng "+listWin.size()+" người đã nhận được "+Utlis.numberFormat(amount)+" vàng từ Mini Game Bầu Cua");
            } else if(rand == 2){
                PlayerManager.getInstance().chatWord("Chúc mừng "+listWin.size()+" người đã nhận được "+Utlis.numberFormat(amount)+" Vỏ Sò từ Mini Game Bầu Cua");
            }  else if(rand == 3){
                PlayerManager.getInstance().chatWord("Chúc mừng "+listWin.size()+" người đã nhận được "+Utlis.numberFormat(amount)+" Chakra Vĩ Thú từ Mini Game Bầu Cua");
            }
        } catch (Exception ex) {
            Utlis.logError(BauCua.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public BauCuaTpl checkPlayer(int id)
    {
        for (BauCuaTpl bauCuaTpl: listChar){
            if(bauCuaTpl.idChar == id){
                return bauCuaTpl;
            }
        }
        return null;
    }
}
