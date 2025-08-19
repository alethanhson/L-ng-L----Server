package com.langla.real.map;

import com.langla.real.player.*;
import com.langla.data.Skill;
import com.langla.real.other.Effect;
import com.langla.utlis.UTPKoolVN;
import com.langla.lib.Utlis;
import com.langla.data.DataCache;
import java.util.*;

public class CombatManager {

    public static void attackMob(Client client, int idSkill, int idMob, Map.Zone zone) {
        try {
            Skill skill = client.mChar.getSkillWithIdTemplate(idSkill);
            if (!validateAttack(client, skill)) {
                return;
            }

            Mob mob = zone.findMobInMap(idMob);
            if (mob == null || mob.hp <= 0 || mob.status == 4) {
                return;
            }

            if (!isInAttackRange(client, mob, skill)) {
                return;
            }

            // Tính toán sát thương theo logic gốc - GIỐNG HỆT BẢN GỐC
            int dameCoBan = client.mChar.getDame() + client.mChar.getDameMob(mob);
            int dame = (dameCoBan + skill.getDameMob(client, mob));

            UTPKoolVN.Debug("Attack Mob dame: " + dame);

            // Random damage theo gốc - GIỐNG HỆT BẢN GỐC
            dame = Utlis.nextInt(dame * 90 / 100, dame);

            // Critical hit theo gốc - GIỐNG HỆT BẢN GỐC
            boolean chi_mang = Utlis.randomBoolean(100, client.mChar.getChiMang() / 100);
            if (chi_mang) {
                int num = 80;
                num += client.mChar.getTangTanCongChiMang();
                dame = dame + (dame * num / 100);
            }

            int maxTarget = skill.maxTarget;

            // Xử lý hiệu ứng theo gốc - GIỐNG HỆT BẢN GỐC
            for (int i = 0; i < client.mChar.listEffect.size(); i++) {
                Effect effect = client.mChar.listEffect.get(i);
                if (effect.id == 57) {
                    zone.sendEffAttackMob(client.mChar.id, (short) mob.idEntity);
                    client.mChar.setAttackMob(mob, effect.value, false);
                } else if (effect.id == 58 && Utlis.nextInt(500) < effect.value) {
                    int time = Math.min(effect.value * 100, 10000);
                    Effect newEff = new Effect(38, effect.value, System.currentTimeMillis(), time);
                    mob.addEff(newEff);
                    zone.addEffMob(newEff, (short) mob.idEntity);
                } else if (effect.id == 68) {
                    int dameCong = (dame * effect.value) / 120;
                    dame += dameCong;
                } else if (effect.id == 63) {
                    if (mob.hp > effect.value) {
                        client.mChar.PlusHp(effect.value);
                        client.mChar.msgUpdateHp();
                        client.mChar.setAttackMob(mob, effect.value, false);
                    }
                } else if (effect.id == 72 && Utlis.nextInt(100) < 50) {
                    client.mChar.setAttackMob(mob, dame, chi_mang);
                } else if (effect.id == 53) {
                    long timeStart = System.currentTimeMillis() - effect.timeStart;
                    if (timeStart < (effect.maintain - 500)) {
                        effect.maintain = 500;
                        effect.timeStart = System.currentTimeMillis();
                    }
                }
            }

            HandleUseSkill.attackEffMob(client, mob, skill);
            zone.sendAttackMobToAllChar(client, mob, idSkill);
            client.mChar.setAttackMob(mob, dame, chi_mang);

            client.mChar.effAttackMob(mob, skill);

            if (client.mChar.infoChar.isPhanThan) {
                zone.sendPhanThanAttack(false, client.mChar.id, mob.idEntity);
                int xdame = DataCache.dataDamePhanThan[client.mChar.infoChar.levelPhanThan];
                int damePT = dame * xdame / 100;
                client.mChar.setAttackMob(mob, damePT, chi_mang);
            }

            // Xử lý tấn công lane theo gốc - GIỐNG HỆT BẢN GỐC
            ArrayList<Mob> list = new ArrayList<Mob>();
            for (int i = 0; i < maxTarget - 1; i++) {
                Mob mob2 = null;
                for (int j = 0; j < zone.vecMob.size(); j++) {
                    Mob cmob = zone.vecMob.get(j);
                    if (cmob.idEntity != mob.idEntity && cmob.hp > 0 && cmob.getMobTemplate().type != 10) {
                        if (Utlis.getRange(cmob.cx, mob.cx) <= skill.rangeNgang
                                && Math.abs(mob.cy - cmob.cy) < skill.rangeDoc
                                && Utlis.checkDirection(client.mChar.cx, mob.cx) == Utlis
                                        .checkDirection(client.mChar.cx, cmob.cx)) {
                            if (!list.contains(cmob)) {
                                mob2 = cmob;
                                break;
                            }
                        }
                    }
                }
                if (mob2 != null) {
                    list.add(mob2);
                    dame = (dameCoBan + skill.getDameMob(client, mob));
                    dame = Utlis.nextInt(dame * 90 / 100, dame);
                    chi_mang = Utlis.randomBoolean(100, client.mChar.getChiMang() / 100);
                    if (chi_mang) {
                        int num = 80;
                        num += client.mChar.getTangTanCongChiMang();
                        dame = dame + (dame * num / 100);
                    }
                    client.mChar.setAttackMob(mob2, dame, chi_mang);
                }
            }

        } catch (Exception ex) {
            Utlis.logError(CombatManager.class, ex, "Lỗi tấn công quái vật: " + ex.getMessage());
        }
    }

    public static void attackPlayer(Client client, int idSkill, int idPlayer, Map.Zone zone) {
        try {
            Skill skill = client.mChar.getSkillWithIdTemplate(idSkill);
            if (!validateAttack(client, skill)) {
                return;
            }

            Char player = zone.findCharInMap(idPlayer);
            if (client.mChar.infoChar.isDie || player == null || player.infoChar.hp <= 0 || player.infoChar.isDie
                    || !zone.isPk(client.mChar, player)) {
                return;
            }

            if (!isInAttackRange(client, player, skill)) {
                return;
            }

            // Tính toán sát thương theo logic gốc - GIỐNG HỆT BẢN GỐC
            int dameCoBan = client.mChar.getDame();
            int dame = HandleUseSkill.getDameTuongKhac(client.mChar, player, dameCoBan);
            dame = skill.getDamePlayer(client, player, dame);
            dame /= 10;

            // Random damage theo gốc - GIỐNG HỆT BẢN GỐC
            dame = Utlis.nextInt(dame * 90 / 100, dame);

            // Critical hit theo gốc - GIỐNG HỆT BẢN GỐC
            int chiMang = client.mChar.getChiMang() - player.getGiamTruChiMang();
            if (chiMang < 0)
                chiMang = 0;
            chiMang = chiMang / 100;

            int giamCM = player.getGiamTanCongKhiBiCM();
            boolean chi_mang = Utlis.randomBoolean(100, chiMang);

            if (chi_mang) {
                int num = 80;
                num += client.mChar.getTangTanCongChiMang();
                if (num > giamCM) {
                    num -= giamCM;
                } else {
                    num = 0;
                }
                dame = dame + (dame * num / 100);
            }

            // Xử lý hiệu ứng theo gốc - GIỐNG HỆT BẢN GỐC
            for (int i = 0; i < client.mChar.listEffect.size(); i++) {
                Effect effect = client.mChar.listEffect.get(i);
                if (effect.id == 57) {
                    int value = effect.value / 2;
                    int dame2 = Utlis.nextInt(value * 90 / 100, value);
                    zone.sendEffAttackChar(client.mChar.id, (short) player.id);
                    client.mChar.setAttackPlayer(player, dame2, false);
                } else if (effect.id == 58 && Utlis.nextInt(500) < effect.value) {
                    int time = Math.min(effect.value * 100, 10000);
                    Effect newEff = new Effect(38, effect.value, System.currentTimeMillis(), time);
                    player.addEffect(newEff);
                } else if (effect.id == 68) {
                    int dameCong = (dame * effect.value) / 150;
                    dame += dameCong;
                } else if (effect.id == 63) {
                    if (player.infoChar.hp > effect.value) {
                        client.mChar.PlusHp(effect.value);
                        client.mChar.msgUpdateHp();
                        client.mChar.setAttackPlayer(player, effect.value, false);
                    }
                } else if (effect.id == 72 && Utlis.nextInt(100) < 50) {
                    client.mChar.setAttackPlayer(player, dame, chi_mang);
                } else if (effect.id == 53) {
                    long timeStart = System.currentTimeMillis() - effect.timeStart;
                    if (timeStart < (effect.maintain - 500)) {
                        effect.maintain = 500;
                        effect.timeStart = System.currentTimeMillis();
                    }
                }
            }

            int maxTarget = skill.maxTarget;
            if (dame > 0)
                HandleUseSkill.attackEffChar(client, player, skill);
            zone.sendAttackPlayerToAllChar(client, player, idSkill);

            client.mChar.setAttackPlayer(player, dame, chi_mang); // send attack
            if (client.mChar.getSatThuongChuyenHp() > 0) {
                int hpPlus = dame * client.mChar.getSatThuongChuyenHp() / 100;
                client.mChar.PlusHp(hpPlus);
                client.mChar.msgUpdateHp();
            }
            if (dame > 0)
                client.mChar.effAttackPlayer(player, skill);

            if (client.mChar.infoChar.isPhanThan) {
                zone.sendPhanThanAttack(true, client.mChar.id, player.id);
                int xdame = DataCache.dataDamePhanThan[client.mChar.infoChar.levelPhanThan];
                int damePT = dame * xdame / 100;
                client.mChar.setAttackPlayer(player, damePT, chi_mang);
            }

            if (dame <= 0)
                zone.sendNeSatThuong(player.id);
            if (client.mChar.info.isCuuSat || client.mChar.info.idCharPk != -1)
                return; // đang tỷ võ hoặc cừu sát bỏ qua không đánh lane

            // Xử lý tấn công lane theo gốc - GIỐNG HỆT BẢN GỐC
            ArrayList<Char> list = new ArrayList<Char>();
            for (int i = 0; i < maxTarget - 1; i++) {
                Char player2 = null;
                for (int j = 0; j < zone.vecChar.size(); j++) {
                    Char cplayer = zone.vecChar.get(j);
                    if (cplayer.id != player.id && cplayer.id != client.mChar.id && cplayer.infoChar.hp > 0
                            && !cplayer.infoChar.isDie) {
                        if (Utlis.getRange(player.infoChar.cx, cplayer.infoChar.cx) <= skill.rangeNgang
                                && Math.abs(player.infoChar.cy - cplayer.infoChar.cy) < skill.rangeDoc
                                && Utlis.checkDirection(client.mChar.cx, player.cx) == Utlis
                                        .checkDirection(client.mChar.cx, cplayer.cx)) {
                            if (!list.contains(cplayer)) {
                                player2 = cplayer;
                                break;
                            }
                        }
                    }
                }
                if (player2 != null) {
                    list.add(player2);

                    dameCoBan = client.mChar.getDame();

                    dame = HandleUseSkill.getDameTuongKhac(client.mChar, player2, dameCoBan);

                    dame = skill.getDamePlayer(client, player, dame);

                    dame /= 15; // đánh lane giảm /15

                    dame = Utlis.nextInt(dame * 90 / 100, dame);

                    chiMang = client.mChar.getChiMang() - player.getGiamTruChiMang();

                    if (chiMang < 0)
                        chiMang = 0;

                    chiMang = chiMang / 100;

                    chi_mang = Utlis.randomBoolean(100, chiMang);
                    if (chi_mang) {
                        int num = 80;
                        num += client.mChar.getTangTanCongChiMang();

                        if (num > giamCM) {
                            num -= giamCM;
                        } else {
                            num = 0;
                        }

                        dame = dame + (dame * num / 100);
                    }

                    client.mChar.setAttackPlayer(player2, dame, chi_mang);
                    if (dame <= 0)
                        zone.sendNeSatThuong(player.id);
                }
            }

        } catch (Exception ex) {
            Utlis.logError(CombatManager.class, ex, "Lỗi tấn công người chơi: " + ex.getMessage());
        }
    }

    private static boolean validateAttack(Client client, Skill skill) {
        return client.mChar.infoChar.isDie == false
                && skill != null
                && skill.level > 0
                && skill.mpUse <= client.mChar.infoChar.mp
                && skill.levelNeed <= client.mChar.level()
                && System.currentTimeMillis() - skill.time >= skill.coolDown
                && client.mChar.info.isBiChoang == false;
    }

    private static boolean isInAttackRange(Client client, Object target, Skill skill) {
        if (target instanceof Mob) {
            Mob mob = (Mob) target;
            return Utlis.getRange(mob.cx, client.mChar.cx) <= skill.rangeNgang + mob.getMobTemplate().speedMove
                    && Utlis.getRange(mob.cy, client.mChar.cy) <= skill.rangeDoc + mob.getMobTemplate().speedMove;
        } else if (target instanceof Char) {
            Char player = (Char) target;
            return Utlis.getRange(player.cx, client.mChar.cx) <= skill.rangeNgang
                    && Utlis.getRange(player.cy, client.mChar.cy) <= skill.rangeDoc;
        }
        return false;
    }
}
