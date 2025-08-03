package com.langla.data;

import com.langla.real.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class InfoChar {

    public byte idNVChar = 0;

    public String username = "";

    public byte lvPk = 0;

    public short speedMove = 600;

    public byte levelPhanThan = 0;

    public byte MaxLevelPhanThan = 5;
    public byte MaxNangCapViThu = 5;

    public int expPhanThan = 0;

    public boolean isPhanThan = false;

    public boolean isGoiPhanThanFree = false;
    public byte maxGhepCaiTrang = 17;

    public int hoatLuc = 0;

    public int sachChienDau;

    public int hp = 1;

    public int mp = 1;

    public int hpRecv = 0;

    public int mpRecv = 0;

    public int hpFull = 100;

    public int mpFull = 100;

    public int bac = 0;

    public int bacKhoa = 0;

    public int vangKhoa = 0;

    public int vang = 0;

    public int taiPhu;

    public int cuaCai = 0;

    public int cuaCaiTuan = 0;

    public byte gioiTinh = 1;

    public byte idClass = 0;

    public byte idhe = 0;

    public byte selectDanhHieu = -1;

    public byte rank = 0;

    public long exp = 0;

    public short exp_rank = 0;

    public short exp_plus = 0;

    public String name = "";

    public int selectCaiTrang;

    public int idTask = 0;

    public int idStep = -1;

    public int requireTask = 0;

    public int pointNAP;

    public int statusGD = 0;

    public long timeGiuRuong = 0;

    public int timeChatColor = -1;

    public int diemTiemNang = 0;

    public int diemKyNang = 0;

    public int level = 1;

    public int levelCheTao = 0;

    public byte levelKhamNgoc = 0;

    public int mapId = 86;

    public int mapDefault = 75;

    public short idZoneCustom = -1;

    public int groupId = -1;

    public boolean groupLock = false;

    public int cx;

    public int cy;

    public boolean isDie = false;

    public boolean isKhoaCap = false;

    public int bac_Ruong = 0;

    public int bacKhoa_Ruong = 0;

    public int vangKhoa_Ruong = 0;

    public int vang_Ruong = 0;

    public int tongVangNap = 0;

    public int tongCuongHoa = 0;

    public int cuongHoaTuan = 0;

    public boolean isNapDau = false;

    public long delayGuiGiaToc = 0;

    public int familyId = -1;

    public String familyName = "";

    public int soLanCamThuat = 1;
    public int sdIzanami = 0;
    public int sdIzanami2 = 0;
    public int chuyenCan = 0;
    public int chuyenCanTuan = 0;

    public int[] diem_Hokage = new int[10];

    public Item itemSach;
    public List<Integer> logUserItem = new ArrayList<>();


    public int getLogUserItem(int idItem){
        int a = 0;
        for (Integer integer : this.logUserItem) {
            if (integer == idItem) {
                a += 1;
            }
        }
        return a;
    }

}
