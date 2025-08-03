/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.map;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.family.Family_Member;
import com.langla.real.map.Map.Zone;
import com.langla.data.DataCenter;
import com.langla.data.MobTemplate;
import com.langla.lib.Utlis;
import com.langla.real.other.Effect;
import com.langla.real.player.Char;
import com.langla.real.player.Entity;
import com.langla.server.lib.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class Mob extends Entity implements Cloneable {


    public int id;

    public int hp;
    public int hpGoc;

    public int hpFull;

    public int he;

    public int exp;

    public int dame;
    public int tangDameLenLoi;
    public int tangDameLenTho;
    public int tangDameLenThuy;
    public int tangDameLenHoa;
    public int tangDameLenPhong;
    public int khangLoi;
    public int khangTho;

    public int khangThuy;

    public int khangHoa;

    public int khangPhong;

    public int NeTranh;

    public int PhanDon;

    public int HoiHp;
    public int expGoc;

    public int level;

    public int levelBoss;

    public boolean paintMiniMap;

    public boolean isDie;
    public boolean isReSpawn;
    public long timeDie;
    public long delayAttack;

    public long delayUpdate;

    public long delayHoiHP;
    public boolean isBoss;
    public boolean isHoiSinhMob = true;
    public long timeRemove = 0;
    public String nameChar;

    public List<Effect> listEffect;
    /* Hiệu Ứng khống chế*/
    public boolean IsBong;

    public boolean IsSuyYeu;
    public boolean IsChoang;
    public boolean IsTeLiet;
    public boolean IsBietThienThan;
    public boolean CanAttack()
    {
        return !isDie && !IsTeLiet && !IsBietThienThan && !IsChoang && getMobTemplate().type != 10 && getMobTemplate().type != 8;
    }
    public Mob cloneMob() {
        try {
            return (Mob) super.clone();
        } catch (Exception e) {
            Utlis.logError(Mob.class, e , "Da say ra loi:\n" + e.getMessage());
            return null;
        }
    }
    public void  createNewEffectList() {
        this.listEffect = new ArrayList<>();
        this.nameChar = "";
    }

    public void write(Writer writer) throws IOException {
        writer.writeShort(this.idEntity);
        writer.writeBoolean(paintMiniMap);
        writer.writeUTF(nameChar);
        writer.writeShort(this.id);
        this.writeXY(writer);
        writer.writeShort(this.level);
        writer.writeByte(he);
        writer.writeByte(getStatus());
        writer.writeInt(hp);
        writer.writeInt(hpFull);
        writer.writeInt(exp);
        writer.writeByte(levelBoss);
        writeEffect(writer);
    }

    public int getStatus() {
        return !paintMiniMap && hp > 0 ? 0 : 4;
    }

    private void writeEffect(Writer writer) throws IOException {
        writer.writeByte(this.listEffect.size());

        for (int i = 0; i < this.listEffect.size(); i++) {
            Effect effect = this.listEffect.get(i);
            if(effect != null){
                effect.write(writer);
            }
        }
    }
    public void addEff(Effect effect){
        this.listEffect.add(effect);
        switch (effect.id) {
            case 8:
                this.IsSuyYeu = true;
                break;
            case 11:
                this.IsBong = true;
                break;
            case 12:
                this.IsChoang = true;
                break;
            case 36:
                this.IsTeLiet = true;
                break;
            case 94:
                this.IsBietThienThan = true;
                break;
        }
    }

    public void removeEff(Effect effect){
        this.listEffect.remove(effect);
        switch (effect.id) {
            case 8:
                this.IsSuyYeu = false;
                break;
            case 11:
                this.IsBong = false;
                break;
            case 12:
                this.IsChoang = false;
                break;
            case 36:
                this.IsTeLiet = false;
                break;
            case 94:
                this.IsBietThienThan = false;
                break;
        }
    }

    public void update(Zone zone){
        if(HoiHp > 0 && !isDie && hp < hpFull){
            hp += HoiHp;
            if(hp > hpFull) hp = hpFull;
            zone.updateHpMob(this);
        }
        if(this.listEffect.size() == 0) return;
        for (int i = 0; i < this.listEffect.size(); i++) {
            if(this.listEffect.get(i) != null) this.listEffect.get(i).updateMob(this, zone);
        }
    }
    public int getValueEff(int idEff){
        int value = 0;
        for (int i = 0; i < this.listEffect.size(); i++) {
            Effect effect = this.listEffect.get(i);
            if(effect != null && effect.id == idEff){
                value += effect.value;
            }
        }
        return value;
    }
    void setHp() {
        if (hp <= 0) {
            hp = 0;
            status = 4;
            isDie = true;
            isReSpawn = true;
            this.timeDie = System.currentTimeMillis();
        } else {
            status = 0;
            isDie = false;
            isReSpawn = false;
            this.timeDie = 0l;
        }
    }

    public void reSpawn() {
        hp = hpFull = hpGoc;
        he = Utlis.nextInt(1, 5);
        if(Utlis.nextInt(100) < 60 && id == 213 && hpGoc == 99999999){
            expGoc = Utlis.nextInt(100000000,1100000000);
        }
        exp = expGoc;
        levelBoss = 0;
        if(isBoss){
            levelBoss = 7;
        }
        if(!isBoss){
            int num = Utlis.nextInt(10000);
            if (num < 1) {
                hp = hpFull = hpGoc * 100;
                exp = expGoc * 100;
                levelBoss = 2;
            } else if (num < 5) {
                hp = hpFull = hpGoc * 10;
                levelBoss = 1;
                exp = expGoc * 10;
            }
        }
        if(exp > 2100000000) exp = 2100000000;
        setHp();

        // 1 => Lôi,2 => thổ,3 => thủy ,4 => Hỏa , 5=> Phong
        if(he == 1){
            tangDameLenTho = getRandom(hpFull);
            khangPhong = getRandom(hpFull);
        } else if(he == 2){
            tangDameLenThuy = getRandom(hpFull);
            khangLoi = getRandom(hpFull);
        }  else if(he == 3){
            tangDameLenHoa = getRandom(hpFull);
            khangTho = getRandom(hpFull);
        }  else if(he == 4){
            tangDameLenPhong = getRandom(hpFull);
            khangThuy = getRandom(hpFull);
        }  else if(he == 5){
            tangDameLenLoi = getRandom(hpFull);
            khangHoa = getRandom(hpFull);
        }
        dame = getRandomDame(hpFull);
    }

    public void reSpawnMobHoatDong(int xLevel, boolean isRandomHe) {
        hp = hpFull = hpGoc * xLevel;
        if(isRandomHe) he = Utlis.nextInt(1, 5);
        exp = expGoc * xLevel;
        levelBoss = 0;
        if(isBoss){
            levelBoss = 10;
        }
        setHp();
        if(exp > 2100000000) exp = 2100000000;
        if(hp > 2100000000) hp = 2100000000;
        if(hpFull > 2100000000) hpFull = 2100000000;

        // 1 => Lôi,2 => thổ,3 => thủy ,4 => Hỏa , 5=> Phong
        if(he == 1){
            tangDameLenTho = getRandom(hpFull);
            khangPhong = getRandom(hpFull);
        } else if(he == 2){
            tangDameLenThuy = getRandom(hpFull);
            khangLoi = getRandom(hpFull);
        }  else if(he == 3){
            tangDameLenHoa = getRandom(hpFull);
            khangTho = getRandom(hpFull);
        }  else if(he == 4){
            tangDameLenPhong = getRandom(hpFull);
            khangThuy = getRandom(hpFull);
        }  else if(he == 5){
            tangDameLenLoi = getRandom(hpFull);
            khangHoa = getRandom(hpFull);
        }
        dame = getRandomDame(hpFull);
    }
    public void setHoiHp() {
        HoiHp = Utlis.nextInt((hpFull/1500), (hpFull/1000));
        if(HoiHp > 5000) HoiHp = 5000;
    }
    public void setNeTranh() {
        NeTranh = Utlis.nextInt(1000, 5555);
    }
    public void setPhanDon() {
        PhanDon = Utlis.nextInt(1, 21);
    }
    public MobTemplate getMobTemplate() {
        return DataCenter.gI().MobTemplate[id];
    }
    public void writeView(Writer writer) throws java.io.IOException {
        writer.writeShort(idEntity);
        writer.writeShort(dame); // dame
        writer.writeShort(0); // chính xác
        writer.writeShort(0); // bỏ qua né tránh
        writer.writeShort(0); // chí mạng
        writer.writeShort(0); // tấn công khi đánh chí mạng
        writer.writeShort(tangDameLenLoi); // tấn công lên lôi
        writer.writeShort(tangDameLenTho); // tấn công lên thổ
        writer.writeShort(tangDameLenThuy); // tấn công lên thủy
        writer.writeShort(tangDameLenHoa); // tấn công lên hảo
        writer.writeShort(tangDameLenPhong); // tấn công lên phong
        writer.writeShort(0); // gây suy yêú
        writer.writeShort(0); // trúng độc
        writer.writeShort(0); // làm chậm
        writer.writeShort(0); // bỏng
        writer.writeShort(0); // choáng
        writer.writeShort(khangLoi); // khang lôi
        writer.writeShort(khangTho); // kháng thổ
        writer.writeShort(khangThuy); // kháng thủy
        writer.writeShort(khangHoa); // kháng hoa
        writer.writeShort(khangPhong); // kháng phong
        writer.writeShort(0); // giảm sát thương
        writer.writeShort(NeTranh); // né tránh
        writer.writeShort(PhanDon); // phản đòn
        writer.writeShort(0); // phòng chí mạng
        writer.writeShort(0); // tương khắc lên hệ
        writer.writeShort(0); // giảm tương khắc


    }
    public int getDameTheoHe(Char _cAnDam) {
        // 1 => Lôi,2 => thổ,3 => thủy ,4 => Hỏa , 5=> Phong
        int num = dame;
        switch (_cAnDam.infoChar.idClass){
            case 1:
                num += tangDameLenLoi;
                break;
            case 2:
                num += tangDameLenTho;
                break;
            case 3:
                num += tangDameLenThuy;
                break;
            case 4:
                num += tangDameLenHoa;
                break;
            case 5:
                num += tangDameLenPhong;
                break;
        }
        switch (he){
            case 1:
                num -= _cAnDam.getKhangLoi();
                break;
            case 2:
                num -= _cAnDam.getKhangTho();
                break;
            case 3:
                num -= _cAnDam.getKhangThuy();
                break;
            case 4:
                num -=  _cAnDam.getKhangHoa();
                break;
            case 5:
                num -=  _cAnDam.getKhangPhong();
                break;
        }

        dame -= _cAnDam.getGiamSatThuong()+_cAnDam.getKhangTatCa();

        if(this.IsSuyYeu) num /= 2;

        return num;
    }

    public int getRandom(int hpFull) {
        int r = 0;
        if(hpFull < 1000){
            r =  Utlis.nextInt(1,11);
        } else {
            r = Utlis.nextInt((hpFull/300), (hpFull/200));
        }
        if(r >= 800){
            r = Utlis.nextInt(800,1000);
        }
        return r;
    }
    public int getRandomDame(int hpFull) {
        int r = 0;
        r = Utlis.nextInt((hpFull/150), (hpFull/100));
        if(r >= 1500){
            r = Utlis.nextInt(1500,2000);
        }
        return r;
    }

}
