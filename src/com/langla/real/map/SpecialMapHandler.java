package com.langla.real.map;

import com.langla.data.*;
import com.langla.real.player.*;
import com.langla.real.family.*;
import java.util.*;

public class SpecialMapHandler {

    public static void handleMapCustom(Client client, Map.Zone zone, Mob mob) {
        int mapId = zone.map.mapID;

        switch (mapId) {
            case 89:
                handleCamThuatMap(client, zone, mob);
                break;
            case 46:
                handleFamilyGateMap46(client, zone, mob);
                break;
            case 47:
                handleFamilyGateMap47(client, zone, mob);
                break;
        }
    }

    private static void handleCamThuatMap(Client client, Map.Zone zone, Mob mob) {
        ArrayList<Mob> aliveMobs = getAliveMobs(zone.vecMob);

        if (aliveMobs.isEmpty()) {
            if (!zone.infoMap.isBossCamThuat) {
                // Tạo boss Kabuto
                createBossKabuto(zone, mob);
            } else {
                // Xử lý vòng lặp ảo tưởng
                handleCamThuatLoop(zone);
            }
        }
    }

    private static void createBossKabuto(Map.Zone zone, Mob mob) {
        Mob boss = new Mob();
        boss.createNewEffectList();
        boss.idEntity = DataCache.getIDMob();
        boss.id = 238; // ID boss Kabuto
        boss.hpGoc = mob.hpFull * 100;
        boss.exp = mob.exp * 100;
        boss.expGoc = mob.expGoc * 100;
        boss.level = mob.level;
        boss.paintMiniMap = false;
        boss.isBoss = true;
        boss.timeRemove = 300000 + System.currentTimeMillis();
        boss.setXY((short) 1050, (short) 137);
        boss.reSpawn();

        zone.vecMob.add(boss);
        zone.addMobToZone(boss);
        zone.showMessWhiteZone("Kabuto đã xuất hiện.");
        zone.infoMap.isBossCamThuat = true;
    }

    private static void handleCamThuatLoop(Map.Zone zone) {
        if (zone.infoMap.vongLap < 15) {
            zone.infoMap.vongLap++;
            zone.infoMap.time = zone.infoMap.timeClose = 11000;
            zone.infoMap.timeStartHoatDong = System.currentTimeMillis();
            zone.infoMap.isNextMapCamThuat = true;
            zone.updateTimeHoatDongZone(zone.infoMap.timeStartHoatDong, zone.infoMap.time, false);
        } else {
            zone.infoMap.time = zone.infoMap.timeClose = 11000;
            zone.infoMap.timeStartHoatDong = System.currentTimeMillis();
            zone.infoMap.isNextMapCamThuat = false;
            zone.updateTimeHoatDongZone(zone.infoMap.timeStartHoatDong, zone.infoMap.time, false);
        }
    }

    private static void handleFamilyGateMap46(Client client, Map.Zone zone, Mob mob) {
        ArrayList<Mob> aliveMobs = getAliveMobs(zone.vecMob);

        if (aliveMobs.isEmpty()) {
            // Thưởng hoàn thành ải
            rewardFamilyGateCompletion(zone, 1, 2);
        }
    }

    private static void handleFamilyGateMap47(Client client, Map.Zone zone, Mob mob) {
        ArrayList<Mob> aliveMobs = getAliveMobs(zone.vecMob);

        if (aliveMobs.isEmpty()) {
            if (!zone.infoMap.isBossAi) {
                // Tạo boss và quái phụ
                createFamilyGateBoss(zone);
            } else if (mob.id == 112) {
                // Boss bị đánh chết
                rewardFamilyGateCompletion(zone, 1, 2);
                handleFamilyGateCompletion(zone);
            }
        }
    }

    private static void createFamilyGateBoss(Map.Zone zone) {
        zone.infoMap.isBossAi = true;

        // Tạo quái phụ
        createSupportMobs(zone);

        // Tạo boss chính
        createMainBoss(zone);
    }

    private static void createSupportMobs(Map.Zone zone) {
        int l = 60;
        for (int i = 0; i < 10; i++) {
            Mob mob = new Mob();
            mob.id = 130; // Bi dược
            mob.level = zone.map.levelMap;
            mob.cx = (short) (2200 + l);
            mob.cy = 372;
            mob.hpGoc = 1;
            mob.paintMiniMap = false;
            mob.isHoiSinhMob = false;
            mob.createNewEffectList();
            mob.idEntity = DataCache.getIDMob();
            mob.reSpawnMobHoatDong(1, true);
            zone.vecMob.add(mob);
            zone.addMobToZone(mob);
            l += 60;
        }
    }

    private static void createMainBoss(Map.Zone zone) {
        int hpBoss = 1800000;
        Mob boss = new Mob();
        boss.id = 112;
        boss.level = zone.map.levelMap;
        boss.cx = (short) 2500;
        boss.cy = 771;
        boss.hpGoc = boss.hp = boss.hpFull = zone.map.levelMap * hpBoss;
        boss.expGoc = boss.hpGoc / 8;
        boss.paintMiniMap = false;
        boss.isHoiSinhMob = false;
        boss.isBoss = true;
        boss.createNewEffectList();
        boss.idEntity = DataCache.getIDMob();
        boss.reSpawnMobHoatDong(1, true);
        boss.setPhanDon();
        boss.setNeTranh();
        boss.setHoiHp();
        zone.vecMob.add(boss);
        zone.addMobToZone(boss);
    }

    private static void rewardFamilyGateCompletion(Map.Zone zone, int chuyenCan, int congHien) {
        for (Char c : zone.vecChar) {
            c.client.session.serivce.ShowMessWhite(
                    "Bạn nhận được " + chuyenCan + " điểm chuyên cần " + congHien + " điểm cống hiến gia tộc");
            c.infoChar.chuyenCan += chuyenCan;
            c.infoChar.chuyenCanTuan += chuyenCan;

            FamilyTemplate giaToc = Family.gI().getGiaToc(zone.FamilyId);
            if (giaToc != null) {
                Family_Member getMem = Family.gI().getMe(c, giaToc);
                if (getMem != null) {
                    getMem.congHien += congHien;
                    getMem.congHienTuan += congHien;
                    giaToc.info.congHienTuan += congHien;
                    giaToc.info.PlusExp(20);
                }
            }
        }
    }

    private static void handleFamilyGateCompletion(Map.Zone zone) {
        FamilyTemplate giaToc = Family.gI().getGiaToc(zone.FamilyId);
        if (giaToc != null) {
            Map.Zone zone46 = Map.maps[46].FindMapCustom(giaToc.MapAi.get(46));
            if (zone46 != null) {
                zone46.infoMap.time = zone46.infoMap.timeClose = 60000;
                zone46.infoMap.timeStartHoatDong = System.currentTimeMillis();
            }
        }

        zone.infoMap.time = zone.infoMap.timeClose = 60000;
        zone.infoMap.timeStartHoatDong = System.currentTimeMillis();
        zone.updateTimeHoatDongZone(zone.infoMap.timeStartHoatDong, zone.infoMap.time, false);
    }

    private static ArrayList<Mob> getAliveMobs(ArrayList<Mob> mobs) {
        ArrayList<Mob> aliveMobs = new ArrayList<>();
        for (Mob mob : mobs) {
            if (mob != null && !mob.isDie) {
                aliveMobs.add(mob);
            }
        }
        return aliveMobs;
    }
}
