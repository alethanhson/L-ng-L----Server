package com.langla.real.player;

import com.langla.data.ItemOption;
import com.langla.data.Skill;
import com.langla.data.SkillTemplate;
import com.langla.lib.Utlis;
import com.langla.real.map.Mob;
import com.langla.real.other.Effect;

import java.util.ArrayList;

/**
 * @author PKoolVN
 **/
public class HandleUseSkill {



    public static void HanderSkillNotFocus(Client client, Skill skill) {
        if (skill == null || skill.level == 0 || skill.mpUse > client.mChar.infoChar.mp || skill.levelNeed > client.mChar.level() || System.currentTimeMillis() - skill.time < skill.coolDown) {
            return;
        }
        skill.time = System.currentTimeMillis();
        client.mChar.MineMp(skill.mpUse);
        client.mChar.msgUpdateMp();

        //Skill hỗ trợ
        if(skill.getSkillTemplate().type == 1 || skill.getSkillTemplate().type == 6){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0){
                if(skill.getSkillTemplate().id == SkillTemplate.TRIEU_HOI_DOI_CHI_THUAT){
                    client.mChar.addEffect(new Effect(62, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(63, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));

                } else if(skill.getSkillTemplate().id == SkillTemplate.TRIEU_HOI_CHIM_CHI_THUAT){
                    client.mChar.addEffect(new Effect(59, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(60, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));

                }  else if(skill.getSkillTemplate().id == SkillTemplate.BYAKUGAN_13){
                    client.mChar.addEffect(new Effect(72, array[0].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(73, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));

                }  else if(skill.getSkillTemplate().id == SkillTemplate.TANG_SINH_CHI_THUAT){
                    client.mChar.addEffect(new Effect(30, array[0].a[1], System.currentTimeMillis(), 5000));
                    if(client.mChar.infoChar.groupId != -1){
                        ArrayList<Char> copyOfVecChar = client.mChar.zone.getVecChar();
                        for (Char aChar : copyOfVecChar) {
                            if (aChar != null && aChar.id != client.mChar.id && aChar.infoChar.groupId == client.mChar.infoChar.groupId && (Math.abs(aChar.cx - client.mChar.cx) < 300 && Math.abs(aChar.cy - client.mChar.cy) < 300) ) {
                                aChar.client.mChar.addEffect(new Effect(30, array[0].a[1], System.currentTimeMillis(), 5000));
                            }
                        }

                    }
                }  else if(skill.getSkillTemplate().id == SkillTemplate.THUY_LAO_THUAT){
                    client.mChar.addEffect(new Effect(34, array[0].a[1], System.currentTimeMillis(), 5000));
                    client.mChar.addEffect(new Effect(61, array[1].a[1], System.currentTimeMillis(), 5000));

                }  else if(skill.getSkillTemplate().id == SkillTemplate.BYAKUGAN_19){
                    client.mChar.addEffect(new Effect(31, array[0].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(73, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));

                }  else if(skill.getSkillTemplate().id == SkillTemplate.AN_THAN_CHI_THUAT){
                    client.mChar.addEffect(new Effect(53, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(54, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));

                } else if(skill.getSkillTemplate().id == SkillTemplate.TRIEU_HOI_CHIM_YEU_CHI_THUAT) {
                    client.mChar.addEffect(new Effect(57, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(58, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));

                } else if(skill.getSkillTemplate().id == SkillTemplate.CHARKRA_CUU_VI_HINH){
                    client.mChar.addEffect(new Effect(74, array[0].a[1], System.currentTimeMillis(), 10000));
                    client.mChar.addEffect(new Effect(77, array[1].a[1], System.currentTimeMillis(), 10000));

                } else if(skill.getSkillTemplate().id == SkillTemplate.ANH_PHAN_THAN_CHI_THUAT){
                    client.mChar.addEffect(new Effect(51, array[0].a[1], System.currentTimeMillis(), 15000));
                    client.mChar.addEffect(new Effect(52, array[1].a[1], System.currentTimeMillis(), 15000));

                } else if(skill.getSkillTemplate().id == SkillTemplate.HIEN_NHAN_THUAT){
                    client.mChar.addEffect(new Effect(64, array[1].a[1], System.currentTimeMillis(), array[0].a[1]));
                    client.mChar.addEffect(new Effect(65, array[2].a[1], System.currentTimeMillis(), array[0].a[1]));

                } else if(skill.getSkillTemplate().id == SkillTemplate.BACH_HAO_CHI_THUAT){
                    client.mChar.addEffect(new Effect(69, array[0].a[1], System.currentTimeMillis(), 10000));
                    client.mChar.addEffect(new Effect(70, array[1].a[1], System.currentTimeMillis(), 10000));

                } else if(skill.getSkillTemplate().id == SkillTemplate.SUSANO_ITACHI){
                    client.mChar.addEffect(new Effect(107, array[0].a[1], System.currentTimeMillis(), array[0].a[1]));

                } else if(skill.getSkillTemplate().id == SkillTemplate.SUSANOO){
                    client.mChar.addEffect(new Effect(68, array[0].a[1], System.currentTimeMillis(), 20000));
                }
            }
        }
    }


    public static void attackEffMob(Client client, Mob mob, Skill skill){


        // ấn chú chi thuật

        if(skill.getSkillTemplate().id == SkillTemplate.AN_CHU_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(55, array[0].a[1], System.currentTimeMillis(), 10000);
                Effect eff2 =new Effect(56, array[1].a[1], System.currentTimeMillis(), 10000);
                mob.addEff(eff1);
                mob.addEff(eff2);
                client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);
                client.mChar.zone.addEffMob(eff2, (short) mob.idEntity);
            }
        } else if(skill.getSkillTemplate().id == SkillTemplate.DICH_CHUYEN_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                int tang = client.mChar.getChiSoFormSkill(277);
                int sec = array[2].a[1]+tang;
                Effect eff1 = new Effect(36, 1, System.currentTimeMillis(),sec);
                mob.addEff(eff1);
                client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);

                client.mChar.setXY(mob.cx, mob.cy);
                client.session.serivce.setXYAllZone(client);
            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.ANH_THU_PHUOC_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(71, 1, System.currentTimeMillis(), array[1].a[1]);
                mob.addEff(eff1);
                client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);

            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.BIET_THIEN_THAN){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(94, 1, System.currentTimeMillis(), array[1].a[1]);
                mob.addEff(eff1);
                client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);

            }
        } else if(skill.getSkillTemplate().id == SkillTemplate.THAO_CU_THIEN_TOA){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(78, 1, System.currentTimeMillis(), array[1].a[1]);
                mob.addEff(eff1);
                client.mChar.zone.addEffMob(eff1, (short) mob.idEntity);

            }
        }
    }
    public static void attackEffChar(Client client, Char player, Skill skill){


        // ấn chú chi thuật

        if(skill.getSkillTemplate().id == SkillTemplate.AN_CHU_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(55, array[0].a[1], System.currentTimeMillis(), 10000);
                Effect eff2 =new Effect(56, array[1].a[1], System.currentTimeMillis(), 10000);
                player.addEffect(eff1);
                player.addEffect(eff2);
            }
        } else if(skill.getSkillTemplate().id == SkillTemplate.DICH_CHUYEN_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                int tang = client.mChar.getChiSoFormSkill(277);
                int sec = array[2].a[1]+tang;
                Effect eff1 = new Effect(36, 1, System.currentTimeMillis(),sec);
                player.addEffect(eff1);

                client.mChar.setXY(player.cx, player.cy);
                client.session.serivce.setXYAllZone(client);
            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.ANH_THU_PHUOC_CHI_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(71, 1, System.currentTimeMillis(), array[1].a[1]);
                player.addEffect(eff1);

            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.BIET_THIEN_THAN){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(94, 1, System.currentTimeMillis(), array[1].a[1]);
                player.addEffect(eff1);
            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.THAO_CU_THIEN_TOA){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(78, 1, System.currentTimeMillis(), array[1].a[1]);
                player.addEffect(eff1);
            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.THIEN_CHIEU){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(93, array[1].a[1], System.currentTimeMillis(), 15000);
                player.addEffect(eff1);
            }
        }   else if(skill.getSkillTemplate().id == SkillTemplate.BIET_THIEN_THAN){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(94, 1, System.currentTimeMillis(), array[1].a[1]);
                player.addEffect(eff1);
            }
        }   else if(skill.getSkillTemplate().id == SkillTemplate.THANH_SAT_CHAKRA){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(95, 1, System.currentTimeMillis(), array[1].a[1]);
                player.addEffect(eff1);
            }
        }  else if(skill.getSkillTemplate().id == SkillTemplate.KHONG_THOI_GIAN_THUAT){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                player.setXY(client.mChar.cx, client.mChar.cy);
                player.client.session.serivce.setXYAllZone(player.client);
            }
        }   else if(skill.getSkillTemplate().id == SkillTemplate.LOA_TOAN_LIEN_THU_LI_KIEM){
            ItemOption[] array = skill.getItemOption();
            if(array.length > 0) {
                Effect eff1 = new Effect(96, array[1].a[1], System.currentTimeMillis(), 15000);
                player.addEffect(eff1);
            }
        }
    }
    public static int getDameTuongKhac(Char _myChar, Char _cAnDame, int dame) {
        int dameDown = getDameTheoHe(_myChar, _cAnDame, dame);
        if (dameDown < 0) {
            dameDown = 0;
        }
        return dameDown;
    }

    private static int getDameTheoHe(Char _myChar, Char _cAnDame, int dame) {
        int dameDown = dame;
        int khang;
        switch (_cAnDame.infoChar.idClass) { // cAnDame is Lôi
            case 1:
                dameDown = dameDown + (dameDown * 10 / 100);

                dameDown += _myChar.getTangTanCongLenLoi();

                khang = getKhangTheoHe(_myChar, _cAnDame);

                if(isKhac(_myChar, _cAnDame)) {
                    dameDown += _myChar.getTangTuongKhac();
                    khang += _cAnDame.getGiamTuongKhac();
                }

                if(_myChar.getBoQuaKhangTinh() > 0) {
                    boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                    if (okBoQua) {
                        khang = 0;
                    }
                }
                khang += _cAnDame.getGiamSatThuong();
                dameDown -= khang;
                break;
            case 2:
                dameDown = dameDown + (dameDown * 10 / 100);

                dameDown += _myChar.getTangTanCongLenTho();

                khang = getKhangTheoHe(_myChar, _cAnDame);

                if(isKhac(_myChar, _cAnDame)) {
                    dameDown += _myChar.getTangTuongKhac();
                    khang += _cAnDame.getGiamTuongKhac();
                }
                if(_myChar.getBoQuaKhangTinh() > 0) {
                    boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                    if (okBoQua) {
                        khang = 0;
                    }
                }
                khang += _cAnDame.getGiamSatThuong();
                dameDown -= khang;
                break;
            case 3:
                dameDown = dameDown + (dameDown * 10 / 100);

                dameDown += _myChar.getTangTanCongLenThuy();

                khang = getKhangTheoHe(_myChar, _cAnDame);

                if(isKhac(_myChar, _cAnDame)) {
                    dameDown += _myChar.getTangTuongKhac();
                    khang += _cAnDame.getGiamTuongKhac();
                }

                if(_myChar.getBoQuaKhangTinh() > 0) {
                    boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                    if (okBoQua) {
                        khang = 0;
                    }
                }
                khang += _cAnDame.getGiamSatThuong();
                dameDown -= khang;
                break;
            case 4:
                dameDown = dameDown + (dameDown * 10 / 100);

                dameDown += _myChar.getTangTanCongLenHoa();

                khang = getKhangTheoHe(_myChar, _cAnDame);

                if(isKhac(_myChar, _cAnDame)) {
                    dameDown += _myChar.getTangTuongKhac();
                    khang += _cAnDame.getGiamTuongKhac();
                }

                if(_myChar.getBoQuaKhangTinh() > 0) {
                    boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                    if (okBoQua) {
                        khang = 0;
                    }
                }
                khang += _cAnDame.getGiamSatThuong();
                dameDown -= khang;
                break;
            case 5:
                dameDown = dameDown + (dameDown * 10 / 100);
                dameDown += _myChar.getTangTanCongLenPhong();

                khang = getKhangTheoHe(_myChar, _cAnDame);

                if(isKhac(_myChar, _cAnDame)) {
                    dameDown += _myChar.getTangTuongKhac();
                    khang += _cAnDame.getGiamTuongKhac();
                }

                if(_myChar.getBoQuaKhangTinh() > 0) {
                    boolean okBoQua = Utlis.randomBoolean(100, _myChar.getBoQuaKhangTinh());
                    if (okBoQua) {
                        khang = 0;
                    }
                }
                khang += _cAnDame.getGiamSatThuong();
                dameDown -= khang;
                break;
        }
        return dameDown;
    }
    private static boolean isKhac(Char _myChar, Char _cAnDame) {
        // 1 => Lôi,2 => thổ,3 => thủy ,4 => Hỏa , 5=> Phong
        switch (_myChar.infoChar.idClass) { // kháng lôi mychar
            case 1:
                return _cAnDame.infoChar.idClass == 2;
            case 2:
                return _cAnDame.infoChar.idClass == 3;
            case 3:
                return _cAnDame.infoChar.idClass == 4;
            case 4:
                return _cAnDame.infoChar.idClass == 5;
            case 5:
                return _cAnDame.infoChar.idClass == 1;
            default:
                return false;
        }
    }
    private static boolean isKhang(Char _myChar, Char _cAnDame) {
       // 1 => Lôi,2 => thổ,3 => thủy ,4 => Hỏa , 5=> Phong

        switch (_myChar.infoChar.idClass) { // kháng lôi mychar
            case 1:
                return _cAnDame.infoChar.idClass == 5;
            case 2:
                return _cAnDame.infoChar.idClass == 1;
            case 3:
                return _cAnDame.infoChar.idClass == 2;
            case 4:
                return _cAnDame.infoChar.idClass == 3;
            case 5:
                return _cAnDame.infoChar.idClass == 4;
            default:
                return false;
        }
    }
    private static int getKhangTheoHe(Char _myChar, Char _cAnDame) {

        int khang = 0;
        switch (_myChar.infoChar.idClass) { // kháng lôi mychar
            case 1:
                khang = _cAnDame.getKhangLoi() + _cAnDame.getKhangTatCa();
                break;
            case 2:
                khang = _cAnDame.getKhangTho() + _cAnDame.getKhangTatCa();
                break;
            case 3:
                khang = _cAnDame.getKhangThuy()  + _cAnDame.getKhangTatCa();
                break;
            case 4:
                khang = _cAnDame.getKhangHoa()  + _cAnDame.getKhangTatCa();
                break;
            case 5:
                khang = _cAnDame.getKhangPhong() + _cAnDame.getKhangTatCa();
                break;
        }
        return khang;
    }
}
