package com.langla.real.item;

import com.langla.data.DataCache;
import com.langla.data.DataCenter;
import com.langla.server.handler.IActionItem;

/**
 * @author PKoolVN
 **/
public class ItemShop {
    public short id_item = 0;
    public boolean isLock = false;
    public long expiry = -1L;
    public int idhe = -1;

    public int idclass = 1;
    public int sex = -1;

    public String strOptions = "";
    public int gia_ban_tinh_thach = 0;
    public int gia_ban_vang = 0;
    public int gia_ban_vang_khoa = 0;
    public int gia_ban_bac_khoa = 0;
    public int gia_ban_bac = 0;
    public int moneyNew = 0;

    public short id_buy = 0; // id buy
    public ItemShop() {
        this.id_buy = DataCache.idbuyshop++;
    }

}
