/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.other;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.langla.data.DataCenter;
import com.langla.data.EffectTemplate;
import com.langla.lib.Utlis;
import com.langla.real.map.Mob;
import com.langla.real.player.Char;
import com.langla.server.lib.Writer;
import com.langla.real.map.Map.Zone;
import com.langla.utlis.UTPKoolVN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class Effect {

    public int id;

    public int value;

    public long timeStart;

    public long maintain;

    @JsonIgnore
    public Char charAttack;
    private long delay;

    List<Integer> listChiSo = new ArrayList<>();

    public Effect(int id, int value, long timeStart, int maintain) {
        this.id = id;
        this.value = value;
        this.timeStart = timeStart;
        this.maintain = maintain;
    }

    public Effect() {
    }

    @JsonIgnore
    public void write(Writer writer) throws IOException {
        writer.writeShort(id);
        writer.writeInt(value);
        writer.writeLong(timeStart);
        writer.writeInt((int) maintain);

    }
    public long getMaintain() {
        return maintain - (System.currentTimeMillis() - timeStart);
    }
    @JsonIgnore
    public EffectTemplate getEffectTemplate() {
        return DataCenter.gI().EffectTemplate[id];

    }
    @JsonIgnore
    public void updateMob(Mob mob, Zone zone) {
        long l = System.currentTimeMillis();
        if (l - timeStart >= maintain) {
            mob.removeEff(this);
            zone.removeEffMob(this, (short) mob.idEntity);
            return;
        }
        if (mob.isDie) {
            return;
        }
        EffectTemplate eff = getEffectTemplate();
        switch (eff.id) {
            case 9: // trúng độc
                if (System.currentTimeMillis() - delay >= 350) {
                    int hpMine = value*5;
                    hpMine = Utlis.nextInt(hpMine * 90 / 100, hpMine);

                    if (charAttack != null && charAttack.client != null&& mob.hp > 0) {
                        zone.setDameMob(charAttack.client, zone, mob, hpMine, false);
                    }
                    delay = System.currentTimeMillis();
                }
                break;
        }

    }
    @JsonIgnore
    public void updateChar(Char aThis) {
        long l = System.currentTimeMillis();
        if (l - timeStart >= maintain || maintain < 0) {
            setEff(aThis, this, true);
            aThis.listEffect.remove(this);
            aThis.msgRemoveEffect(this);
            return;
        }
        if (aThis.infoChar.hp <= 0) {
            return;
        }
        EffectTemplate eff = getEffectTemplate();
        switch (eff.type) { /// luu yyyyyyyyyyyyyyyyyyy su dung type
            case 0:
                if (System.currentTimeMillis() - delay >= 500) {
                    if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
                        aThis.PlusHp(value);
                        aThis.msgUpdateHp();
                    }
                    if (aThis.infoChar.mp < aThis.infoChar.mpFull) {
                        aThis.PlusMp(value);
                        aThis.msgUpdateMp();
                    }

                    delay = System.currentTimeMillis();
                }
                break;
            case 2: // trúng độc
                if (System.currentTimeMillis() - delay >= 350) {
                    int hpMine = value*2;
                    hpMine = Utlis.nextInt(hpMine * 90 / 100, hpMine);
                    if (charAttack != null && charAttack.client != null) {
                        charAttack.setAttackPlayer(aThis, hpMine, false);
                    }
                    delay = System.currentTimeMillis();
                }
                break;
            case 6:
            case 11:
            case 15:
                if (System.currentTimeMillis() - delay >= 500) {
                    if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
                        aThis.PlusHp(value);

                        aThis.msgUpdateHp();
                    }

                    delay = System.currentTimeMillis();
                }
                break;
            case 7:
                if (System.currentTimeMillis() - delay >= 500) {
                    if (aThis.infoChar.mp < aThis.infoChar.mpFull) {
                        aThis.PlusMp(value);
                        aThis.msgUpdateMp();
                    }
                    delay = System.currentTimeMillis();
                }
                break;
            case 51:
                if (System.currentTimeMillis() - delay >= 2000) {
                    if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
                        aThis.PlusHp(value);
                        aThis.msgUpdateHp();
                    }
                    delay = System.currentTimeMillis();
                }
                break;
            case 61:
                if (System.currentTimeMillis() - delay >= 1000) {
                    if (aThis.infoChar.hp < aThis.infoChar.hpFull) {
                        long hpPlus = (long) aThis.infoChar.hpFull * value / 100;
                        aThis.PlusHp((int) hpPlus);
                        aThis.msgUpdateMp();
                    }
                    delay = System.currentTimeMillis();
                }
                break;

        }

    }
    @JsonIgnore
    public static void setEff(Char _myChar, Effect effect, boolean isRemove) {
        try {

            int hpBack = _myChar.infoChar.hpFull;
            int mpBack = _myChar.infoChar.mpFull;
            int speedBack = _myChar.infoChar.speedMove;

            switch (effect.id){
                case 8:
                    if(isRemove){
                        _myChar.TuongKhac.TanCong += effect.listChiSo.get(0);
                        _myChar.TuongKhac.KhangHoa += effect.listChiSo.get(1);
                        _myChar.TuongKhac.KhangLoi += effect.listChiSo.get(2);
                        _myChar.TuongKhac.KhangThuy += effect.listChiSo.get(3);
                        _myChar.TuongKhac.KhangPhong += effect.listChiSo.get(4);
                        _myChar.TuongKhac.KhangTho += effect.listChiSo.get(5);
                        _myChar.TuongKhac.KhangTatCa += effect.listChiSo.get(6);
                    } else {
                        int tanCong = _myChar.TuongKhac.TanCong/2;
                        int khangHoa = _myChar.TuongKhac.KhangHoa/2;
                        int khangLoi = _myChar.TuongKhac.KhangLoi/2;
                        int khangThuy = _myChar.TuongKhac.KhangThuy/2;
                        int khangPhong = _myChar.TuongKhac.KhangPhong/2;
                        int khangTho = _myChar.TuongKhac.KhangTho/2;
                        int khangAll = _myChar.TuongKhac.KhangTatCa/2;

                        _myChar.TuongKhac.TanCong -= tanCong;
                        _myChar.TuongKhac.KhangHoa -= khangHoa;
                        _myChar.TuongKhac.KhangLoi -= khangLoi;
                        _myChar.TuongKhac.KhangThuy -= khangThuy;
                        _myChar.TuongKhac.KhangPhong -= khangPhong;
                        _myChar.TuongKhac.KhangTho -= khangTho;
                        _myChar.TuongKhac.KhangTatCa -= khangAll;

                        effect.listChiSo.add(tanCong);
                        effect.listChiSo.add(khangHoa);
                        effect.listChiSo.add(khangLoi);
                        effect.listChiSo.add(khangThuy);
                        effect.listChiSo.add(khangPhong);
                        effect.listChiSo.add(khangTho);
                        effect.listChiSo.add(khangAll);
                    }
                    break;
                case 11:
                    if(isRemove){
                        _myChar.info.isBiBong = false;
                    } else {
                        _myChar.info.isBiBong = true;
                    }
                    break;

                case 12:
                    if(isRemove){
                        _myChar.info.isBiChoang = false;
                    } else {
                        _myChar.info.isBiChoang = true;
                    }
                    break;
                case 49:
                    if(isRemove){
                        _myChar.info.isBiDuoc = false;
                    } else {
                        _myChar.info.isBiDuoc = true;
                    }
                    break;
                case 39:
                    if(isRemove){
                        _myChar.infoChar.hpFull -= effect.value;
                        _myChar.infoChar.mpFull -= effect.value;
                    } else {
                        _myChar.infoChar.hpFull += effect.value;
                        _myChar.infoChar.mpFull += effect.value;
                    }
                    break;
                case 55:
                    if(isRemove){
                        _myChar.TuongKhac.KhangTatCa += effect.value;

                    } else {
                        _myChar.TuongKhac.KhangTatCa -= effect.value;
                    }
                    break;
                case 56:
                    if(isRemove){
                        _myChar.TuongKhac.NeTranh += effect.value;
                        _myChar.TuongKhac.GiamSatThuong += effect.value;

                    } else {
                        _myChar.TuongKhac.NeTranh -= effect.value;
                        _myChar.TuongKhac.GiamSatThuong -= effect.value;
                    }
                    break;
                case 60:
                    if(isRemove) {
                        _myChar.infoChar.hpFull -= effect.value;
                    } else {
                        _myChar.infoChar.hpFull += effect.value;
                    }
                    break;

                case 62:
                    if(isRemove) {
                        _myChar.TuongKhac.TangTanCongChiMang -= effect.value;
                    } else {
                        _myChar.TuongKhac.TangTanCongChiMang += effect.value;
                    }
                    break;
                case 64:
                    if(isRemove) {
                        _myChar.TuongKhac.BoQuaKhangTinh -= effect.value;
                    } else {
                        _myChar.TuongKhac.BoQuaKhangTinh += effect.value;
                    }
                    break;
                case 41:
                    if(isRemove){
                        _myChar.TuongKhac.TanCongCoBan -= effect.value;
                    } else {
                        _myChar.TuongKhac.TanCongCoBan += effect.value;
                    }
                    break;
                case 61:
                case 101:
                    if(isRemove){
                        _myChar.TuongKhac.TanCong -= effect.value;
                    } else {
                        _myChar.TuongKhac.TanCong += effect.value;
                    }
                    break;
                case 74:
                    if(isRemove) {
                        _myChar.infoChar.hpFull -= effect.value/3;
                        _myChar.infoChar.mpFull -= effect.value/3;
                        _myChar.TuongKhac.TanCong -= effect.value/3;
                    } else {
                        _myChar.infoChar.hpFull += effect.value/3;
                        _myChar.infoChar.mpFull += effect.value/3;
                        _myChar.TuongKhac.TanCong += effect.value/3;
                    }
                    break;
                case 93:
                    if(isRemove){
                        _myChar.TuongKhac.NeTranh += effect.value;
                    } else {
                        _myChar.TuongKhac.NeTranh -= effect.value;
                    }
                    break;
                case 81:
                    if(isRemove){
                        _myChar.info.isBuaUeTho = false;
                    } else {
                        _myChar.info.isBuaUeTho = true;
                    }
                    break;
                case 95:
                    if(isRemove){
                        _myChar.info.isThanhSatChar = false;
                    } else {
                        _myChar.info.isThanhSatChar = true;
                    }
                    break;
                case 96:
                    if(isRemove){
                        _myChar.TuongKhac.ChinhXac += effect.value;
                    } else {
                        _myChar.TuongKhac.ChinhXac -= effect.value;
                    }
                    break;
                case 100:
                    if(isRemove){
                        _myChar.infoChar.hpFull -= 1000;
                        _myChar.TuongKhac.TanCong -= effect.value;
                    } else {
                        _myChar.infoChar.hpFull += 1000;
                        _myChar.TuongKhac.TanCong += effect.value;
                    }
                    break;
                case 51:
                case 107:
                    if(isRemove){
                        _myChar.infoChar.speedMove -= effect.value;
                    } else {
                        _myChar.infoChar.speedMove += effect.value;
                    }
                    break;
                case 99:
                    if(isRemove){
                        _myChar.infoChar.isPhanThan = false;
                    } else {
                        _myChar.infoChar.isPhanThan = true;
                    }
                    break;
                case 37:
                case 42:
                case 47:
                case 67:
                case 85:
                case 103:
                    if(isRemove){
                        _myChar.infoChar.exp_plus -= effect.value;
                    } else {
                        _myChar.infoChar.exp_plus += effect.value;
                    }
                    break;
            }
            if(hpBack != _myChar.infoChar.hpFull) _myChar.msgUpdateHpFull();
            if(mpBack != _myChar.infoChar.mpFull) _myChar.msgUpdateMpFull();
            if(speedBack != _myChar.infoChar.speedMove) _myChar.msgUpdateStatusChar();
        } catch (Exception ex) {
            Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }
    @JsonIgnore
    public static int getValueEffectFormIdItem(int id) {
        switch (id) {
            /*Thức ăn*/
            case 22:
                return 7;
            case 23:
                return 27;
            case 24:
                return 36;
            case 25:
                return 45;
            case 26:
                return 54;
            case 27:
                return 63;
            case 238:
                return 72;
            case 239:
                return 81;
            case 240:
                return 90;
            case 241:
                return 99;
            case 242:
                return 108;
            case 243:
                return 117;
            /*Hp*/
            case 12:
            case 17:
                return 50;
            case 13:
            case 18:
                return 160;
            case 14:
            case 19:
                return 370;
            case 15:
            case 20:
                return 580;
            case 16:
            case 21:
                return 790;
            case 219:
            case 221:
                return 940;
            case 220:
            case 222:
                return 1100;
                // Nhân sâm
            case 159:
                return 50;
            case 281:
                return 75;
            case 347:
                return 100;
                // đan dược
            case 171:
                return 1500;
            case 172:
                return 100;
            case 173:
                return 350;
            case 355:
                return 3000;
            case 356:
                return 200;
            case 357:
                return 700;
            case 358:
                return 4500;
            case 359:
                return 300;
            case 360:
                return 1050;
            case 265:
                return 200;
            case 285:
                return 300;
        }
        return 0;
    }
    @JsonIgnore
    public static int getIDEffectFormIdItem(int id) {
        switch (id) {
            case 171:
            case 355:
            case 358:
                return 39;

            case 172:
            case 356:
            case 359:
                return 40;

            case 173:
            case 357:
            case 360:
                return 41;
            case 265:
            case 285:
                return 47;
        }
        return 0;
    }

}
