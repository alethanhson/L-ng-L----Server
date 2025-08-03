package com.langla.data;

public class SkillTemplate {

    public static int SONG_THANG_LONG = 0;
    public static int TAM_CHUYEN_THAN_THUAT = 1;
    public static int ANH_PHONG_XA = 2;
    public static int TIEU_THUAT_TAM_PHAP = 3;
    public static int AN_CHU_CHI_THUAT = 4;
    public static int TRIEU_HOI_DOI_CHI_THUAT = 5;
    public static int TAM_TRUNG_OAN_THU_THUAT = 6;
    public static int SUSANOO = 7;
    public static int LOI_KIEM = 8;
    public static int KIEM_THUAT_TAM_PHAP = 9;
    public static int TRIEU_HOI_CHIM_CHI_THUAT = 10;
    public static int DICH_CHUYEN_CHI_THUAT = 11;
    public static int THUY_LONG_AN_THUAT = 12;
    public static int BYAKUGAN_13 = 13;
    public static int AI_BOC_BO_THUAT = 14;
    public static int GAY_THUAT_TAM_PHAP = 15;
    public static int TANG_SINH_CHI_THUAT = 16;
    public static int THUY_LAO_THUAT = 17;
    public static int AM_SAT_THUAT = 18;
    public static int BYAKUGAN_19 = 19;
    public static int HAO_HOA_CAU_THUAT = 20;
    public static int AO_THUAT_TAM_PHAP = 21;
    public static int AN_THAN_CHI_THUAT = 22;
    public static int TRIEU_HOI_CHIM_YEU_CHI_THUAT = 23;
    public static int THIEN_SAT_THUY_PHI = 24;
    public static int CHARKRA_CUU_VI_HINH = 25;
    public static int PHI_LOI_THAN_THUAT = 26;
    public static int DAO_THUAT_TAM_PHAP = 27;
    public static int ANH_PHAN_THAN_CHI_THUAT = 28;
    public static int HIEN_NHAN_THUAT = 29;
    public static int KIEM_THUAT_CO_BAN = 30;
    public static int BACH_HAO_CHI_THUAT = 31;
    public static int ANH_THU_PHUOC_CHI_THUAT = 32;
    public static int THAO_CU_THIEN_TOA = 33;
    public static int NHA_THONG_NHA = 34;
    public static int SUSANO_ITACHI = 35;
    public static int LOA_TOAN_LIEN_THU_LI_KIEM = 36;
    public static int THANH_SAT_CHAKRA = 37;
    public static int THIEN_CHIEU = 38;
    public static int BIET_THIEN_THAN = 39;
    public static int KHONG_THOI_GIAN_THUAT = 40;
    public short id;
    public String name;
    public String detail;
    public short levelNeed;
    public byte idChar;
    public byte levelMax;
    public byte type;
    public short idIcon;

    @Override
    public String toString() {
        return "SkillTemplate{" + "id=" + id + "<br>name=" + (name) + "<br>detail=" + detail + "<br>levelNeed=" + levelNeed + "<br>idChar=" + idChar + "<br>levelMax=" + levelMax + "<br>type=" + type + "<br>idIcon=" + idIcon + '}';
    }

    public SkillTemplate(int var1) {
        this.id = (short) var1;
    }
}
