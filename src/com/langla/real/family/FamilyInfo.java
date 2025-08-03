package com.langla.real.family;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PKoolVN
 **/
public class FamilyInfo {

    public int nganSach = 0;

    public int congHienTuan = 0;

    public int soLuongThuNapTrongNgay = 20;

    public int level = 1;

    public int exp = 0;

    public int soLanMoAi = 1;

    public int maxMember = 20;

    public String nameKey = "";

    public int idKey = -1;

    public long timeCreateLog;

    public String thongBao = "";

    public LocalDate lastDailyUpdate; // Ngày cuối cùng cập nhật hàng ngày

    public LocalDate lastWeeklyUpdate; // Ngày cuối cùng cập nhật hàng tuần

    public int getMaxExp(){
        return level * 10000;
    }

    public void PlusExp(int exp){
        this.exp += exp;
        if(exp >= getMaxExp()){
            this.level += 1;
            this.maxMember += 5;
        }
    }

}
