package com.langla.data;

import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.player.Char;
import com.langla.real.player.Player;

import java.util.Vector;

public class Task {

    public int id;
    public String name;
    public int levelNeed;
    public int idNpc;
    public int idMap;
    public int x;
    public int y;
    public String STR1;
    public String STR2;
    public String STR3;
    public int amountExp;
    public int amountVangKhoa;
    public int amountBac;
    public int amountBacKhoa;
    public String strItem;
    public Vector vStep = new Vector();

    public Item getItem(Char character) {
        try {
            if (this.strItem != null && this.strItem.length() > 0) {
                String[] var1 = this.strItem.split("@");
                Item var2;
                (var2 = new Item()).id = Short.parseShort(var1[0]);
                var2.isLock = Boolean.parseBoolean(var1[1]);
                var2.expiry = Long.parseLong(var1[2]);
                var2.setAmount(Integer.parseInt(var1[3]));
                var2.he = Byte.parseByte(var1[4]);
                var2.level = Byte.parseByte(var1[5]);
                if (var1.length > 6) {
                    var2.strOptions = var1[6];
                    if (var2.getItemTemplate().gioiTinh != 2 && var2.getItemTemplate().gioiTinh != character.infoChar.gioiTinh) {
                        for(int var3 = 0; var3 < DataCenter.gI().ItemTemplate.length; ++var3) {
                            ItemTemplate var5;
                            if ((var5 = DataCenter.gI().ItemTemplate[var3]).gioiTinh == character.infoChar.gioiTinh && var5.type == var2.getItemTemplate().type && var5.levelNeed == var2.getItemTemplate().levelNeed) {
                                var2.id = var5.id;
                                break;
                            }
                        }
                    }

                    if (var2.getItemTemplate().type == 13) {
                        var2.he = character.infoChar.idhe;
                        switch(var2.he) {
                            case 1:
                                var2.strOptions = "54,0,500;62,0,500";
                                break;
                            case 2:
                                var2.strOptions = "55,0,500;58,0,500";
                                break;
                            case 3:
                                var2.strOptions = "56,0,500;59,0,500";
                                break;
                            case 4:
                                var2.strOptions = "57,0,500;60,0,500";
                                break;
                            case 5:
                                var2.strOptions = "53,0,500;61,0,500";
                        }
                    }
                }

                return var2;
            }
        }  catch (Exception ex) {
            Utlis.logError(Task.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }

        return null;
    }
}
