package com.langla.real.family;

import com.langla.data.InfoChar;
import com.langla.real.player.Char;

/**
 * @author PKoolVN
 **/
public class Family_Member {
    public int idCharacter;

    public int role;

    public int congHien;

    public int congHienTuan;

    public boolean isCamChat = false;

    public long timeThamGia = System.currentTimeMillis();

    public InfoChar infoChar;
}
