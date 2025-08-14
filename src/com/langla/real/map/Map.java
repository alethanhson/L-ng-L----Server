package com.langla.real.map;

import com.langla.data.*;
import com.langla.lib.Utlis;
import com.langla.real.family.Family;
import com.langla.real.family.FamilyTemplate;
import com.langla.real.family.Family_Member;
import com.langla.real.group.Group;
import com.langla.real.group.GroupTemplate;
import com.langla.real.item.Item;
import com.langla.real.npc.Npc;
import com.langla.real.other.Effect;
import com.langla.real.player.*;
import com.langla.real.task.TaskHandler;
import com.langla.server.lib.Writer;
import com.langla.utlis.UTPKoolVN;
import com.langla.server.lib.Message;
import java.io.IOException;
import java.util.*;
import com.langla.real.map.DropManager;
import com.langla.real.map.MapTypeManager;
import com.langla.real.map.SpecialMapHandler;
import com.langla.real.map.CombatManager;

public class Map {

    public static Map[] maps;
    public static final int NUM_ZONE = 15;
    public ArrayList<Zone> listZone = new ArrayList<Zone>();
    public ArrayList<WayPoint> listWayPoint = new ArrayList<WayPoint>();
    public ArrayList<Zone> listZoneCusTom = new ArrayList<Zone>();

    public int mapID;

    public int levelMap;

    private Map(int id) {
        this.mapID = id;
    }

    public MapTemplate getMapTemplate() {
        return DataCenter.gI().MapTemplate[mapID];
    }

    private void createZone() {
        createZone(NUM_ZONE);
    }

    private void createZone(int NUM_ZONE) {
        for (int i = 0; i < NUM_ZONE; i++) {
            Zone zone = new Zone(this, listZone.size());
            try {
                zone.createNpc();
                zone.createMob();
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
            zone.createThread();
            listZone.add(zone);
        }
    }

    public synchronized short createZoneCustom(int GroupId, int FamilyId, int timeClose, long timeStartHoatDong,
            boolean isHoatDongTime, boolean isTimeHoatDong, int levelMob, int xExp, int xHp, boolean isHoiSinhMob) {
        try {
            short id = DataCache.getIDZoneCustom();
            Zone zone = new Zone(this, id);
            zone.infoMap.time = timeClose;
            if (zone.map.mapID == 46 || zone.map.mapID == 47) {
                zone.infoMap.timeClose = 7200000;
            } else {
                zone.infoMap.timeClose = timeClose;
            }
            zone.FamilyId = FamilyId;
            zone.GroupId = GroupId;
            zone.type = 1;
            zone.map.levelMap = levelMob;
            zone.infoMap.timeStartHoatDong = timeStartHoatDong;
            zone.infoMap.isHoatDongTime = isHoatDongTime;
            zone.infoMap.isTimeHoatDong = isTimeHoatDong;
            zone.createNpc();
            if (zone.map.mapID == 46 || zone.map.mapID == 47) {
                // zone.setMobFamilyGate(levelMob);
            } else {
                zone.setMobCusTom(levelMob, xExp, xHp, isHoiSinhMob);
            }
            zone.createThread();
            listZoneCusTom.add(zone);
            return id;
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
            return -1;
        }
    }

    public Zone FindMapCustom(int idZone) {
        try {
            for (Zone zone : listZoneCusTom) {
                if (zone.zoneID == idZone) {
                    return zone;
                }
            }
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
        return null;
    }

    public synchronized void removeZoneCustom(Zone zone) {
        try {
            listZoneCusTom.remove(zone);
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    private void createWayPoint() {
        for (int index = 0; index < DataCenter.gI().dataWayPoint.length; ++index) {
            WayPoint waypoint = null;
            if (DataCenter.gI().dataWayPoint[index][0] == this.mapID) {
                (waypoint = new WayPoint(0, 0)).create(DataCenter.gI().dataWayPoint[index][0],
                        DataCenter.gI().dataWayPoint[index][5], DataCenter.gI().dataWayPoint[index][1],
                        DataCenter.gI().dataWayPoint[index][2], DataCenter.gI().dataWayPoint[index][3],
                        DataCenter.gI().dataWayPoint[index][4], DataCenter.gI().dataWayPoint[index][10],
                        DataCenter.gI().dataWayPoint[index][11]);
                waypoint.isNext = true;
                this.listWayPoint.add(waypoint);
            } else if (DataCenter.gI().dataWayPoint[index][5] == this.mapID) {
                (waypoint = new WayPoint(0, 0)).create(DataCenter.gI().dataWayPoint[index][5],
                        DataCenter.gI().dataWayPoint[index][0], DataCenter.gI().dataWayPoint[index][6],
                        DataCenter.gI().dataWayPoint[index][7], DataCenter.gI().dataWayPoint[index][8],
                        DataCenter.gI().dataWayPoint[index][9], DataCenter.gI().dataWayPoint[index][12],
                        DataCenter.gI().dataWayPoint[index][13]);
                waypoint.isNext = false;
                this.listWayPoint.add(waypoint);
            }
        }

    }

    public WayPoint getWayPoint_WhenNextMap(int idMapNext) {
        Map mapNext = this;
        for (int i = 0; i < mapNext.listWayPoint.size(); i++) {
            WayPoint waypoint = mapNext.listWayPoint.get(i);
            if (waypoint.mapNext == idMapNext) {
                return waypoint;
            }
        }
        return null;
    }

    public WayPoint getWayPoint_WhenInMap(int idMapNext) {
        Map mapNext = maps[idMapNext];

        return mapNext.getWayPoint_WhenNextMap(mapID);
    }

    public WayPoint getWayPoint(XYEntity xy) {
        Map mapNext = this;
        WayPoint _waypoint = null;
        for (int i = 0; i < mapNext.listWayPoint.size(); i++) {
            WayPoint waypoint = mapNext.listWayPoint.get(i);
            if (_waypoint == null || waypoint.getRe(xy) < _waypoint.getRe(xy)) {
                _waypoint = waypoint;
            }
        }
        return _waypoint;
    }

    public boolean addChar(Client client) {
        if (DataCache.idMapCustom.contains(this.mapID)) {
            if (addCharInMapCustom(client, client.mChar.infoChar.idZoneCustom)) {
                return true;
            } else {
                client.mChar.veMapMacDinh();
                return false;
            }
        }

        if (this.mapID != client.mChar.infoChar.mapDefault && client.mChar.infoChar.isDie) {
            client.session.serivce.ShowMessGold("Bạn đã bị trọng thương không thể thực hiện");
            return false;
        }
        int lvmap = DataCenter.gI().getLockMap(this.mapID);
        if (lvmap > 0) {
            if (client.mChar.level() < lvmap) {
                client.session.serivce.ShowMessGold("Cần đạt cấp " + lvmap + " mới có thể tới khu vực này.");
                return false;
            }
        }

        for (int i = 0; i < this.listZone.size(); i++) {
            Zone z = this.listZone.get(i);
            if (z.vecChar.size() < Zone.MAX_CHAR_INZONE) {//
                boolean can = z.addChar(client);
                if (can) {
                    return true;
                }
            }
        }
        client.session.serivce.ShowMessGold("Khu vực đã đầy.");
        return false;
    }

    public boolean addCharInMapCustom(Client client, short idZoneCustom) {
        for (Zone z : listZoneCusTom) {
            if (z.zoneID == idZoneCustom) {
                boolean ok = z.addChar(client);
                if (ok) {
                    client.mChar.infoChar.idZoneCustom = idZoneCustom;
                    if (client.mChar.zone.map.mapID == 89) {
                        client.session.serivce
                                .ShowMessWhite("Vòng lặp ảo tưởng thứ " + client.mChar.zone.infoMap.vongLap);
                    }
                    return true;
                }
            }
        }
        client.mChar.infoChar.idZoneCustom = -1;
        return false;
    }

    public void nextMap(Client client) {
        try {
            boolean b = false;
            XYEntity xy = client.mChar;
            WayPoint waypoint_next = getWayPoint(xy);
            if (waypoint_next != null) {
                WayPoint waypoint = getWayPoint_WhenInMap(waypoint_next.mapNext);
                if (waypoint != null) {
                    if (waypoint_next.mapNext == 46 || waypoint_next.mapNext == 47) {
                        if (!client.mChar.zone.infoMap.isSpamwMobAi) {
                            client.session.serivce.ShowMessGold("Chưa thể qua map");
                            client.mChar.backXY();
                            client.session.serivce.setXYChar();
                            return;
                        }
                        for (int i = 0; i < client.mChar.zone.vecMob.size(); i++) {
                            Mob mob = client.mChar.zone.vecMob.get(i);
                            if (!mob.isDie) {
                                client.session.serivce.ShowMessGold("Cần hạ hết quái và boss mới có thể chuyển map");
                                client.mChar.backXY();
                                client.session.serivce.setXYChar();
                                return;
                            }
                        }
                        FamilyTemplate giaToc = Family.gI().getGiaToc(client.mChar);
                        if (giaToc != null) {
                            int idMapNext = waypoint_next.mapNext;
                            short idZone = giaToc.MapAi.getOrDefault(idMapNext, (short) 0);

                            b = Map.maps[waypoint_next.mapNext].addCharInMapCustom(client, idZone);
                        } else {
                            client.session.serivce.ShowMessGold("Bạn chưa có gia tộc [3]");
                        }
                    } else {
                        b = Map.maps[waypoint_next.mapNext].addChar(client);
                    }
                }
            }
            if (!b) {
                client.mChar.backXY();
                client.session.serivce.setXYChar();
            }
        } catch (Exception ex) {
            Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public static class Zone {

        public Map map;
        public int zoneID;

        public int type;
        public int GroupId = -1;
        public int FamilyId = -1;
        public InfoMap infoMap = new InfoMap();
        public final ArrayList<Npc> vecNpc = new ArrayList<Npc>();
        public final ArrayList<Mob> vecMob = new ArrayList<Mob>();
        public final ArrayList<ItemMap> vecItemMap = new ArrayList<ItemMap>();
        public final ArrayList<Char> vecChar = new ArrayList<Char>();

        public Thread thread;
        public int id_ENTITY_ITEM_MAP;

        private Zone(Map map, int zone) {
            this.map = map;
            this.zoneID = zone;
        }

        public ArrayList<Char> getVecChar() {
            return this.vecChar;
        }

        private void createNpc() {
            vecNpc.clear();
            for (int i = 0; i < map.getMapTemplate().listNpc.size(); i++) {
                try {
                    Npc npc1 = map.getMapTemplate().listNpc.get(i);
                    Npc npc2 = npc1.cloneNpc();
                    vecNpc.add(npc2);
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
        }

        private void createMob() {
            vecMob.clear();
            for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
                try {
                    Mob mob1 = map.getMapTemplate().listMob.get(i);
                    Mob mob2 = mob1.cloneMob();
                    mob2.createNewEffectList();
                    mob2.idEntity = DataCache.getIDMob();
                    mob2.reSpawn();
                    vecMob.add(mob2);
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
        }

        private void setMobFamilyGate(int level) {
            vecMob.clear();
            infoMap.isSpamwMobAi = true;
            infoMap.time = 1;
            infoMap.isHoatDongTime = true;
            infoMap.timeStartHoatDong = System.currentTimeMillis();
            updateTimeHoatDongZone(infoMap.timeStartHoatDong, infoMap.time, infoMap.isHoatDongTime);
            ShowMessWhite("Bắt đầu vượt ải gia tộc");
            if (map.mapID == 46) {
                int hpMob = 18000;
                int l = 60;

                for (int i = 0; i < 14; i++) { // thổ
                    Mob mob = new Mob();
                    mob.id = 123;
                    mob.level = level;
                    mob.he = 2;
                    mob.cx = (short) (460 + l);
                    mob.cy = 363;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, false);
                    vecMob.add(mob);
                    addMobToZone(mob);
                    l += 60;
                }
                l = 60;
                for (int i = 0; i < 14; i++) { // thủy
                    Mob mob = new Mob();
                    mob.id = 124;
                    mob.level = level;
                    mob.he = 3;
                    mob.cx = (short) (1560 + l);
                    mob.cy = 363;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, false);
                    vecMob.add(mob);
                    addMobToZone(mob);
                    l += 60;
                }

                l = 60;
                for (int i = 0; i < 14; i++) { // lôi
                    Mob mob = new Mob();
                    mob.id = 122;
                    mob.level = level;
                    mob.he = 1;
                    mob.cx = (short) (660 + l);
                    mob.cy = 800;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, false);
                    vecMob.add(mob);
                    addMobToZone(mob);
                    l += 60;
                }
                l = 60;
                for (int i = 0; i < 14; i++) { // hỏa
                    Mob mob = new Mob();
                    mob.id = 125;
                    mob.level = level;
                    mob.he = 4;
                    mob.cx = (short) (1660 + l);
                    mob.cy = 800;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, false);
                    vecMob.add(mob);
                    addMobToZone(mob);
                    l += 60;
                }

                l = 60;
                for (int i = 0; i < 14; i++) { // phong
                    Mob mob = new Mob();
                    mob.id = 126;
                    mob.level = level;
                    mob.he = 5;
                    mob.cx = (short) (3500 + l);
                    mob.cy = 800;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, false);
                    vecMob.add(mob);
                    addMobToZone(mob);
                    l += 60;
                }
            } else if (map.mapID == 47) {
                int hpMob = 18000;
                int l = 60;

                for (int i = 0; i < 19; i++) { // hồi sức
                    Mob mob = new Mob();
                    mob.id = 127;
                    mob.level = level;
                    mob.cx = (short) (200 + l);
                    mob.cy = 302;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, true);
                    mob.setHoiHp();
                    vecMob.add(mob);
                    l += 60;
                }

                l = 60;

                for (int i = 0; i < 19; i++) { // phản
                    Mob mob = new Mob();
                    mob.id = 128;
                    mob.level = level;
                    mob.cx = (short) (200 + l);
                    mob.cy = 600;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, true);
                    mob.setPhanDon();
                    vecMob.add(mob);
                    l += 60;
                }

                l = 60;

                for (int i = 0; i < 19; i++) { // né
                    Mob mob = new Mob();
                    mob.id = 129;
                    mob.level = level;
                    mob.cx = (short) (200 + l);
                    mob.cy = 873;
                    mob.timeRemove = 7200000 + System.currentTimeMillis();
                    mob.hpGoc = mob.hp = mob.hpFull = level * hpMob;
                    mob.expGoc = mob.hpGoc / 8;
                    mob.paintMiniMap = false;
                    mob.isHoiSinhMob = false;
                    mob.createNewEffectList();
                    mob.idEntity = DataCache.getIDMob();
                    mob.reSpawnMobHoatDong(1, true);
                    mob.setNeTranh();
                    vecMob.add(mob);
                    l += 60;
                }
            }

        }

        private void setMobCusTom(int level, int xExp, int xHp, boolean isHoiSinhMob) {
            for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
                try {
                    Mob mob1 = map.getMapTemplate().listMob.get(i);
                    mob1.level = level;
                    mob1.expGoc = level * xExp;
                    mob1.hpGoc = level * xHp;
                    mob1.isHoiSinhMob = isHoiSinhMob;
                    if (DataCache.idBoss.contains(mob1.id))
                        mob1.isBoss = true;
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
            vecMob.clear();
            for (int i = 0; i < map.getMapTemplate().listMob.size(); i++) {
                try {
                    Mob mob1 = map.getMapTemplate().listMob.get(i);
                    Mob mob2 = mob1.cloneMob();
                    mob2.createNewEffectList();
                    mob2.idEntity = DataCache.getIDMob();
                    mob2.reSpawnMobHoatDong(1, true);
                    vecMob.add(mob2);
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
        }

        public void updateTimeHoatDongZone(long timeStartHoatDong, int timeHoatDong, boolean isHoatDongTime) {
            try {
                Message msg = Message.c((byte) -80);
                msg.writeLong(timeStartHoatDong);
                msg.writeInt(timeHoatDong);
                msg.writeBoolean(isHoatDongTime);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void clearItemMap() {
            try {
                Message msg = Message.c((byte) -119);
                msg.writeShort(0);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void write(Client client, Writer writer) throws IOException {
            writer.writeShort(zoneID);
            writer.writeShort(map.mapID);

            client.mChar.writeXY(writer);

            writeVecItemMap(writer);
            writeVecChar(client, writer);
            writeVecMob(writer);
            writeVecNpc(writer);

            writer.writeByte(client.mChar.info.typePK);
            writer.writeLong(infoMap.timeStartHoatDong);
            writer.writeInt(infoMap.time);
            writer.writeBoolean(infoMap.isHoatDongTime);
            writer.writeBoolean(infoMap.isTimeHoatDong);
        }

        public void ShowMessGold(String text) {
            try {
                Message msg = new Message((byte) -106);
                msg.writeUTF(text);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public void ShowMessWhite(String text) {
            try {
                Message msg = new Message((byte) -107);
                msg.writeUTF(text);
                msg.writeBoolean(true);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }

        }

        public static final int MAX_CHAR_INZONE = 15;

        public boolean addChar(Client client) {
            if (vecChar.size() > MAX_CHAR_INZONE && type != 1)
                return false;
            if (client.mChar.zone != null) {
                client.mChar.zone.removeChar(client);
                if (client.mChar.zone.map.mapID != this.map.mapID) {

                    WayPoint waypoint = map.getWayPoint_WhenNextMap(this.map.mapID);
                    if (waypoint != null) {
                        XYEntity xy = getXYBlockMapNotCheck(waypoint.cx, waypoint.cy);
                        client.mChar.vec.clear();
                        if (xy.cx < 500) {
                            client.mChar.setXY(Utlis.nextInt(100, 200), xy.cy);
                        } else {
                            client.mChar.setXY(this.map.getMapTemplate().maxX - Utlis.nextInt(400, 500), xy.cy);
                        }
                    } else {
                        int cx = 200;
                        int cy = 500;
                        XYEntity xy = getXYBlockMapNotCheck(cx, cy);
                        if (xy != null) {
                            client.mChar.setXY(xy.cx, xy.cy);
                        }
                    }
                }
            } else {
                int cx = client.mChar.cx;
                int cy = client.mChar.cy;
                client.mChar.setXY(cx, cy);
            }
            if (map.mapID == 89) { // cấm thuật
                client.mChar.setXY(150, 428);
            }
            if (!vecChar.contains(client.mChar))
                vecChar.add(client.mChar);
            client.mChar.zone = this;
            client.mChar.zone.addToAllChar(client);
            client.mChar.infoChar.mapId = map.mapID;
            client.session.serivce.sendArrMap(map.mapID);
            client.session.serivce.sendIntoMap();
            TaskHandler.gI().checkDoneJoinMap(client.mChar);
            TaskHandler.gI().createMobJoinMap(client.mChar);
            client.session.serivce.sendInfoGiaTocAllChar(client.mChar);
            client.mChar.zone.writeGiaToc(client);
            return true;
        }

        public void createThread() {
            if (thread == null || !thread.isAlive()) {
                try {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                long l = System.currentTimeMillis();
                                try {
                                    if (infoMap.timeClose != -1
                                            && (infoMap.timeClose + infoMap.timeStartHoatDong) < l) {
                                        if (infoMap.isNextMapCamThuat) {
                                            updateZone();
                                        } else {
                                            for (int i = vecChar.size() - 1; i >= 0; i--) {
                                                Char c = vecChar.get(i);
                                                if (c.client != null) {
                                                    c.veMapMacDinh();
                                                    c.client.session.serivce
                                                            .NhacNhoMessage("Đã hết thời gian bạn được đưa về làng.");
                                                }
                                            }

                                            if (GroupId != -1)
                                                updateInfoGroup();
                                            if (FamilyId != -1)
                                                updateInfoFamily();
                                            Zone.this.map.removeZoneCustom(Zone.this);
                                            Zone.this.thread.interrupt();
                                            return;
                                        }
                                    }
                                    //
                                    if ((map.mapID == 46 || map.mapID == 47) && !infoMap.isSpamwMobAi) {
                                        if (infoMap.time != -1 && (infoMap.time + infoMap.timeStartHoatDong) < l) {
                                            setMobFamilyGate(map.levelMap);
                                        }
                                    }

                                    if (vecChar.size() > 0) {
                                        for (int i = vecChar.size() - 1; i >= 0; i--) {
                                            Char c = vecChar.get(i);
                                            if (c.client != null) {
                                                c.update();
                                            }
                                        }
                                        for (int i = vecItemMap.size() - 1; i >= 0; i--) {
                                            ItemMap mItemMap = vecItemMap.get(i);
                                            if (mItemMap != null)
                                                mItemMap.update(Zone.this);
                                        }
                                        for (int i = vecMob.size() - 1; i >= 0; i--) {
                                            Mob mob = vecMob.get(i);
                                            if (System.currentTimeMillis() - mob.delayUpdate >= 1000L) {
                                                mob.update(Zone.this);
                                                mob.delayUpdate = System.currentTimeMillis();
                                            }

                                            if (mob.timeRemove < System.currentTimeMillis() && mob.timeRemove != 0) {
                                                removeMobToAllChar((short) mob.idEntity);
                                                vecMob.remove(mob);
                                                continue;
                                            }
                                            if (mob.isReSpawn && mob.isHoiSinhMob) {
                                                if (mob.timeRemove == 0 && !mob.isBoss
                                                        && System.currentTimeMillis() - mob.timeDie >= 2500L) {
                                                    mob.reSpawn();
                                                    reSpawnMobToAllChar(mob);
                                                }
                                            } else {
                                                if (mob.CanAttack()) {
                                                    for (int k = vecChar.size() - 1; k >= 0; k--) {
                                                        Char c = vecChar.get(k);
                                                        if (c.client != null) {
                                                            if (!c.infoChar.isDie) {
                                                                if (mob.getRe(c) < 50
                                                                        + mob.getMobTemplate().speedMove) {
                                                                    if (System.currentTimeMillis()
                                                                            - mob.delayAttack >= 5000) {
                                                                        Zone.this.mobAttackChar(mob, c.client);
                                                                        mob.delayAttack = System.currentTimeMillis();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }

                                } catch (Exception ex) {
                                    Utlis.logError(Map.class, ex, "Da say ra loi UPDATE :\n" + ex.getMessage());
                                } finally {
                                    long sleep = (100 - (System.currentTimeMillis() - l));
                                    if (sleep < 1) {
                                        sleep = 1;
                                    }
                                    Utlis.sleep(sleep);
                                }
                            }
                        }

                    });
                    thread.start();
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi UPDATE:\n" + ex.getMessage());
                }
            }
        }

        private void updateZone() {
            if (map.mapID == 89) {
                infoMap.time = infoMap.timeClose = 300000;
                infoMap.timeStartHoatDong = System.currentTimeMillis();
                infoMap.isNextMapCamThuat = false;
                infoMap.isBossCamThuat = false;
                for (Mob mob : vecMob) {

                    mob.reSpawnMobHoatDong(infoMap.vongLap, true);
                    reSpawnMobToAllChar(mob);
                }
                vecItemMap.clear();
                clearItemMap();
                updateTimeHoatDongZone(infoMap.timeStartHoatDong, infoMap.time, false);
                for (Char c : vecChar) {
                    int cx = 150;
                    int cy = 428;
                    c.setXY(cx, cy);
                    c.client.session.serivce.setXYAllZone(c.client);
                    c.client.session.serivce.ShowMessWhite("Vòng lặp ảo tưởng thứ " + infoMap.vongLap);
                }

            }
        }

        private void updateInfoGroup() {
            GroupTemplate group = Group.gI().getGroup(GroupId);
            if (group != null && (group.idZoneCamThuat == zoneID || group.idZoneLuyenTap == zoneID)) {
                if (map.mapID == 84) {
                    group.idZoneLuyenTap = 0;
                } else if (map.mapID == 89) {
                    group.idZoneCamThuat = 0;
                }
            }
        }

        private void updateInfoFamily() {
            FamilyTemplate giaToc = Family.gI().getGiaToc(FamilyId);
            if (giaToc != null) {
                giaToc.MapAi.clear();
            }
        }

        public void sendEffAttackChar(int idChar, int idPlayer) {
            try {
                Message msg = new Message((byte) -44);
                msg.writeInt(idChar);
                msg.writeInt(idPlayer);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        public void sendPhanThanAttack(boolean isAttackChar, int idChar, int idEntry) {
            try {
                if (isAttackChar) {
                    Message msg = new Message((byte) 84);
                    msg.writeInt(idChar);
                    msg.writeInt(idEntry);
                    SendZoneMessage(msg);
                } else {
                    Message msg = new Message((byte) -19);
                    msg.writeInt(idChar);
                    msg.writeShort(idEntry);
                    SendZoneMessage(msg);
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        public void sendEffAttackMob(int idChar, short idEntryMob) {
            try {
                Message msg = new Message((byte) -43);
                msg.writeInt(idChar);
                msg.writeShort(idEntryMob);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        public void addEffMob(Effect effect, short idEntryMob) {
            try {
                Message msg = new Message((byte) 15);
                msg.writeShort(idEntryMob);
                msg.writeShort(effect.id);
                msg.writeInt(effect.value);
                msg.writeLong(effect.timeStart);
                msg.writeInt((int) effect.maintain);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        public void addMobToZone(Mob mob) {
            try {
                Message msg = new Message((byte) 1);
                mob.write(msg.writer);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void updateHpMob(Mob mob) {
            try {
                Message msg = new Message((byte) -36);
                msg.writeShort(mob.idEntity);
                msg.writeInt(mob.hp);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(TaskHandler.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void removeEffMob(Effect effect, short idEntryMob) {
            try {
                Message msg = new Message((byte) 16);
                msg.writeShort(idEntryMob);
                msg.writeShort(effect.id);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        private void writeVecItemMap(Writer writer) throws IOException {
            synchronized (vecItemMap) {
                writer.writeShort(vecItemMap.size());
                for (ItemMap item : vecItemMap) {
                    item.write(writer, -1, -1, this);
                }
            }
        }

        private void writeVecChar(Client client, Writer writer) throws IOException {
            ArrayList<Char> copyOfVecChar = getVecChar();
            writer.writeByte(copyOfVecChar.size() - 1);
            for (Char c : copyOfVecChar) {
                if (vecChar.contains(c) && c != null && client != null && c != client.mChar) {
                    writer.writeInt(c.id);
                    c.write(writer);
                    if (c.infoChar.familyName.length() > 0) {
                        client.session.serivce.sendInfoGiaTocToMe(c);
                    }
                }
            }
        }

        private void writeGiaToc(Client client) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (Char c : copyOfVecChar) {
                if (vecChar.contains(c) && c != null && client != null && c != client.mChar) {
                    if (c.infoChar.familyName.length() > 0) {
                        client.session.serivce.sendInfoGiaTocToMe(c);
                    }
                }
            }
        }

        private void writeVecMob(Writer writer) throws IOException {
            writer.writeShort(vecMob.size());
            for (int i = 0; i < vecMob.size(); i++) {
                vecMob.get(i).write(writer);
            }
        }

        private void writeVecNpc(Writer writer) throws IOException {
            writer.writeShort(vecNpc.size());

            for (int i = 0; i < vecNpc.size(); i++) {
                Npc npc = vecNpc.get(i);
                writer.writeByte(npc.status);
                writer.writeShort(npc.id);
                npc.writeXY(writer);
            }
        }

        public void removeChar(Client client) {
            if (client != null && client.mChar != null) {
                try {
                    boolean c = vecChar.remove(client.mChar);
                    if (c) {
                        removeToAllChar(client);
                    }
                    // tỷ võ
                    client.mChar.updatePK((byte) 1);
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi removeChar:\n" + ex.getMessage());
                }
            }
        }

        public void removeToAllChar(Client client) {
            try {
                if (client == null)
                    return;
                Writer writer = new Writer();
                writer.writeInt(client.mChar.id);
                for (int i = vecChar.size() - 1; i >= 0; i--) {
                    Char c = vecChar.get(i);
                    if (c.client != null && c != client.mChar) {
                        try {
                            c.client.session.serivce.removeCharIntoMap(writer);
                        } catch (Exception ex) {
                            Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
                        }
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi removeToAllChar:\n" + ex.getMessage());
            }
        }

        public void addToAllChar(Client client) {
            try {
                if (client == null)
                    return;
                Writer writer = new Writer();
                writer.writeInt(client.mChar.id);
                client.mChar.write(writer);

                ArrayList<Char> copyOfVecChar = getVecChar();

                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null && c != client.mChar) {
                        try {
                            c.client.session.serivce.addCharIntoMap(writer);
                        } catch (Exception ex) {
                            Utlis.logError(Map.class, ex, "Da say ra loi addToAllChar:\n" + ex.getMessage());
                        }
                    }
                }

            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi addToAllChar:\n" + ex.getMessage());
            }
        }

        public void updateXYChar(Client client, boolean when_move) {
            try {
                Writer writer = new Writer();
                writer.writeInt(client.mChar.id);
                client.mChar.writeXY(writer);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null && c != client.mChar) {
                        try {
                            c.client.session.serivce.updateXYChar(writer, when_move);
                        } catch (Exception ex) {
                            Utlis.logError(Map.class, ex, "Da say ra loi updateXYChar:\n" + ex.getMessage());
                        }
                    }
                }

            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi updateXYChar:\n" + ex.getMessage());
            }
        }

        public void SendZoneMessage(Message msg) {
            try {
                vecChar.forEach(c -> c.client.session.sendMessage(msg));
                // msg.close();
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi SendZoneMessage:\n" + ex.getMessage());
            }
        }

        public void showMessWhiteZone(String mess) {
            try {
                Message msg = new Message((byte) -107);
                msg.writeUTF(mess);
                msg.writeBoolean(false);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Char.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public boolean isPk(Char c, Char p) {
            return (c.info.typePK == 0 && c.info.idCharPk != -1)
                    || (c.info.typePK == 0 && c.info.isCuuSat
                            || (p.info.typePK == 3)
                            || (c.info.typePK == 3)
                            || (c.info.typePK == p.info.typePK && c.info.typePK != 0));
        }

        public void attackPlayer(Client client, int idSkill, int idplayer) {
            CombatManager.attackPlayer(client, idSkill, idplayer, this);
        }

        public void attackMob(Client client, int idSkill, int idMob) {
            CombatManager.attackMob(client, idSkill, idMob, this);
        }

        public XYEntity getXYBlockMap(int var0, int var1) {
            try {
                XYEntity var3;
                if ((var3 = map.getMapTemplate().h.c(var0, var1)) != null && Utlis.positive(var1 - var3.cy) > 1) {
                    return var3;
                }
            } catch (Exception var2) {
            }

            return null;
        }

        public XYEntity getXYBlockMapNotCheck(int var0, int var1) {
            try {
                XYEntity var3;
                if ((var3 = map.getMapTemplate().h.c(var0, var1)) != null) {
                    return var3;
                }
            } catch (Exception var2) {
            }

            return null;
        }

        public Mob findMobInMap(int idMob) {
            for (int i = 0; i < vecMob.size(); i++) {
                Mob mob = vecMob.get(i);
                if (mob.idEntity == idMob) {
                    return mob;
                }
            }
            return null;
        }

        public ItemMap findItemMapInMap(int idItemMap) {
            synchronized (vecItemMap) {
                for (ItemMap itemmap : vecItemMap) {
                    if (itemmap.idEntity == idItemMap) {
                        return itemmap;
                    }
                }
            }
            return null;
        }

        public Char findCharInMap(String name) {
            for (Char c : vecChar) {
                if (c != null && c.infoChar.name.equals(name)) {
                    return c;
                }
            }
            return null;
        }

        public Char findCharInMap(int id) {
            for (Char c : vecChar) {
                if (c != null && c.id == id) {
                    return c;
                }
            }
            return null;
        }

        public void setDameMob(Client client, Zone zone, Mob mob, int dame, boolean chi_mang) {
            try {
                mob.hp -= dame;
                mob.setHp();
                if (mob.hp <= 0) {
                    mob.hp = 0;
                    TaskHandler.gI().checkDoneKillMob(client.mChar, mob);
                    setMobDie(client, zone, mob);
                    if (mob.timeRemove > 0) {
                        for (int i = 0; i < zone.vecMob.size(); i++) {
                            Mob m = zone.vecMob.get(i);
                            if (m.idEntity == mob.idEntity) {
                                zone.vecMob.remove(m);
                                break;
                            }
                        }
                    }
                }
                Writer writer = new Writer();
                writer.writeShort(mob.idEntity);
                writer.writeInt(mob.hp);
                writer.writeBoolean(chi_mang);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (c.client != null) {
                        c.client.session.serivce.sendHpMob(writer);
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi setDameMob:\n" + ex.getMessage());
            }
        }

        public void setDameMobDuoc(Client client, Zone zone, Mob mob, int dame, boolean chi_mang) {
            try {
                mob.hp -= dame;
                mob.setHp();
                if (mob.hp <= 0) {
                    mob.hp = 0;
                    TaskHandler.gI().checkDoneKillMob2(client.mChar, mob);
                    setMobDie(client, zone, mob);
                    if (mob.timeRemove > 0) {
                        for (int i = 0; i < zone.vecMob.size(); i++) {
                            Mob m = zone.vecMob.get(i);
                            if (m.idEntity == mob.idEntity) {
                                zone.vecMob.remove(m);
                                break;
                            }
                        }
                    }
                }
                Writer writer = new Writer();
                writer.writeShort(mob.idEntity);
                writer.writeInt(mob.hp);
                writer.writeBoolean(chi_mang);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (c.client != null) {
                        c.client.session.serivce.sendHpMob(writer);
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi setDameMob:\n" + ex.getMessage());
            }
        }

        public void reSpawnMobToAllChar(Mob mob) {
            try {
                Writer writer = new Writer();

                writer.writeShort(mob.idEntity);
                writer.writeShort(mob.level);
                writer.writeByte(mob.he);
                writer.writeInt(mob.hp);
                writer.writeInt(mob.hpFull);
                writer.writeInt(mob.exp);
                writer.writeByte(mob.levelBoss);
                writer.writeByte(mob.status);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (c != null && c.client != null && c.client.session != null) {
                        c.client.session.serivce.sendMobReSpawn(writer);
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi reSpawnMobToAllChar:\n" + ex.getMessage());
            }
        }

        public void removeMobToAllChar(short idEntry) {
            try {
                Message msg = new Message((byte) 0);
                msg.writeShort(idEntry);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi reSpawnMobToAllChar:\n" + ex.getMessage());
            }
        }

        public void sendAttackMobToAllChar(Client client, Mob mob, int idSkill) {
            try {
                Writer writer = new Writer();
                writer.writeInt(client.mChar.id);
                writer.writeInt(client.mChar.infoChar.mp);
                writer.writeShort(client.mChar.getSkillWithIdTemplate(idSkill).index);
                writer.writeShort(mob.idEntity);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (c.client != null) {
                        c.client.session.serivce.sendAttackMob(writer);
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi sendAttackMobToAllChar:\n" + ex.getMessage());
            }
        }

        public void sendAttackPlayerToAllChar(Client client, Char player, int idSkill) {
            try {
                Writer writer = new Writer();
                writer.writeInt(client.mChar.id);
                writer.writeInt(client.mChar.infoChar.mp);
                writer.writeShort(client.mChar.getSkillWithIdTemplate(idSkill).index);
                writer.writeInt(player.id);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    Char c = copyOfVecChar.get(i);
                    if (c.client != null && c.client != client) {
                        c.client.session.serivce.sendAttackPlayer(writer);
                    }
                }
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi sendAttackMobToAllChar:\n" + ex.getMessage());
            }
        }

        public void sendNeSatThuong(int idEntry) {
            try {
                Message msg = Message.c((byte) -42);
                msg.writeInt(idEntry);
                SendZoneMessage(msg);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi reSpawnMobToAllChar:\n" + ex.getMessage());
            }
        }

        public void openTabZone(Client client) {
            try {
                Writer writer = new Writer();
                writer.writeByte(this.map.listZone.size());
                writer.writeByte(this.zoneID);
                writer.writeByte(this.vecChar.size());
                ArrayList<Zone> listZone = new ArrayList<Zone>();
                for (int i = 0; i < map.listZone.size(); i++) {
                    Zone z = map.listZone.get(i);
                    if (z.vecChar.size() > 0) {
                        listZone.add(z);
                    }
                }
                writer.writeByte(listZone.size());
                for (int i = 0; i < listZone.size(); i++) {

                    Zone z = listZone.get(i);
                    writer.writeByte(map.listZone.indexOf(z));
                    writer.writeByte(z.vecChar.size());
                }
                client.session.serivce.sendOpenTabZone(writer);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi openTabZone:\n" + ex.getMessage());
            }
        }

        public void changeZone(Client client, byte zoneNext) {
            if (zoneNext == this.zoneID) {
                client.session.serivce.setXYChar();
                return;
            }
            if (client.mChar.infoChar.isDie) {
                client.session.serivce.ShowMessGold("Bạn đã bị trọng thương không thể thực hiện");
                client.session.serivce.setXYChar();
                return;
            }
            if (zoneNext >= 0 && zoneNext < map.listZone.size()) {
                Zone z = map.listZone.get(zoneNext);
                if (z.vecChar.size() < Zone.MAX_CHAR_INZONE) {
                    z.addChar(client);
                } else {
                    client.session.serivce.setXYChar();
                }
            } else {
                client.session.serivce.setXYChar();
            }
        }

        /*
         * public void ao(Message var1) {
         * try {
         * Mob var2 = this.r(var1.reader.dis.readShort());
         * ItemMap var3;
         * (var3 = new ItemMap()).idChar = var1.reader.dis.readInt();
         * var3.idEntity = var1.reader.dis.readShort();
         * var3.cx = var3.k = var1.reader.dis.readShort();
         * var3.cy = var3.l = var1.reader.dis.readShort();
         * var3.item = new Item();
         * var3.item.read(var1);
         * var2.G.addElement(var3);
         * } catch (Exception var5) {
         * Utlis.println(var5);
         * }
         * }
         */
        private ArrayList<Mob> getAliveMobs(ArrayList<Mob> mobs) {
            ArrayList<Mob> aliveMobs = new ArrayList<>();
            for (Mob mob : mobs) {
                if (mob != null && !mob.isDie) {
                    aliveMobs.add(mob);
                }
            }
            return aliveMobs;
        }

        private void setMobDie(Client client, Zone zone, Mob mob) {
            long expp = mob.exp;
            if (client.mChar.getPlusExp() > 0)
                expp += expp * client.mChar.getPlusExp() / 100; // tăng exp khi có eff
            if (client.mChar.infoChar.groupId != -1) {
                long expTuSkillBuff = 0;
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (Char aChar : copyOfVecChar) {
                    try {
                        if (aChar != null && aChar.id != client.mChar.id
                                && aChar.infoChar.groupId == client.mChar.infoChar.groupId) {
                            aChar.client.mChar.addExp((long) (mob.exp * 0.30));
                            if (Math.abs(aChar.cx - client.mChar.cx) < 300
                                    && Math.abs(aChar.cy - client.mChar.cy) < 300)
                                expTuSkillBuff += aChar.client.mChar.getChiSoFormSkill(104);
                        }
                    } catch (Exception ex) {
                        Utlis.logError(Map.class, ex, "Da say ra loi setMobDie:\n" + ex.getMessage());
                    }
                }
                if (expTuSkillBuff > 0)
                    expp += expp * expTuSkillBuff / 100;

            }
            client.mChar.addExp(expp);
            if (mob.getMobTemplate().type == 10 || mob.getMobTemplate().type == 8)
                return;
            dropItem(client, mob);
            handleMapCustom(client, zone, mob);
            handleItemBody(client, mob);

            // nếu là boss thì xóa khỏi Zone và cập nhật vào bossruntime
            if (mob.isBoss) {
                BossTpl updatedBoss = BossRunTime.gI().getBoss(mob.id);
                if (updatedBoss != null) {
                    updatedBoss.isDie = true;
                    updatedBoss.timeDelay = System.currentTimeMillis() + (updatedBoss.min_spam * 60000L);
                    BossRunTime.gI().setBoss(updatedBoss);
                }
                // removeMobToAllChar((short) mob.idEntity);
                zone.vecMob.remove(mob);
            }
        }

        public void handleItemBody(Client client, Mob mob) {
            // tu luyện bí kíp
            if (mob.levelBoss == 1 || mob.levelBoss == 2) {
                if (client.mChar.arrItemBody[11] != null) {
                    if (client.mChar.arrItemBody[11].getChiSo(1, client, 128) < client.mChar.arrItemBody[11].getChiSo(2,
                            client, 128)) {
                        int plus = 1;
                        int valueEff = client.mChar.getValueEff(47);
                        if (valueEff == 200) {
                            plus = 2;
                        } else if (valueEff == 300) {
                            plus = 3;
                        }
                        client.mChar.arrItemBody[11].plusOption(128, 1, plus);
                        client.mChar.updateItemBody(client.mChar.arrItemBody[11]);
                    }
                }
            }

            // tang exxp phan than

            if (client.mChar.infoChar.isPhanThan) {
                if (client.mChar.infoChar.levelPhanThan < client.mChar.infoChar.MaxLevelPhanThan) {
                    int exp_plus = 1;
                    if (mob.levelBoss == 1) {
                        exp_plus = 5;
                    } else if (mob.levelBoss == 2) {
                        exp_plus = 10;
                    } else if (mob.isBoss) {
                        exp_plus = 100;
                    }
                    client.mChar.infoChar.expPhanThan += exp_plus;
                    int expMax = 5000000;
                    expMax = expMax * (client.mChar.infoChar.levelPhanThan + 1);
                    if (client.mChar.infoChar.expPhanThan >= expMax) {
                        client.mChar.infoChar.levelPhanThan++;
                    }
                }
            }
        }

        public void handleMapCustom(Client client, Zone zone, Mob mob) {
            if (MapTypeManager.requiresSpecialHandling(zone.map.mapID)) {
                SpecialMapHandler.handleMapCustom(client, zone, mob);
            }
        }

        public void dropItem(Client client, Mob mob) {
            if (mob.isBoss) {
                DropManager.dropBossItems(client, mob, this);
            } else {
                DropManager.dropNormalMobItems(client, mob, this);
            }
        }

        public void sendItemDropFormMob(Writer writer) {
            try {
                SendZoneMessage(new Message((byte) 60, writer));
            } catch (Exception ex) {
                Utlis.logError(Session.class, ex, "Da say ra loi:\n" + ex.getMessage());
            }
        }

        public void createItemMap(ItemMap itemMap, Mob mob) {
            try {
                this.vecItemMap.add(itemMap);
                Writer writer = new Writer();
                writer.writeShort(mob.idEntity);
                itemMap.write(writer, -1, -1, this);
                sendItemDropFormMob(writer);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi createItemMap:\n" + ex.getMessage());
            }
        }

        public synchronized void pickUpItem(Client client, short idEntity) {
            ItemMap itemMap = this.findItemMapInMap(idEntity);
            if (itemMap == null) {
                try {
                    Writer writer = new Writer();
                    writer.writeShort(idEntity);
                    client.session.serivce.removeItemMap(writer);
                    return;
                } catch (Exception ex) {
                }
            }
            assert itemMap != null;
            if (itemMap.idChar != -1 && itemMap.idChar != client.mChar.id) {
                return;
            }
            if (client.mChar.checkAddItem(itemMap.item)) {
                synchronized (vecItemMap) {
                    this.vecItemMap.remove(itemMap);
                }
                if (itemMap.item.getItemTemplate().id == 163) {
                    client.mChar.addBacKhoa(itemMap.item.getAmount(), true, true, "Nhặt từ Map");
                } else if (itemMap.item.getItemTemplate().id == 191) {
                    client.mChar.addBac(itemMap.item.getAmount(), true, true, "Nhặt từ Map");
                } else {
                    client.mChar.addItem(itemMap.item, "Nhặt từ Map: " + map.mapID);
                }
                if (itemMap.isSystem) {
                    client.mChar.infoChar.cuaCai++;
                    client.mChar.infoChar.cuaCaiTuan++;
                }
                try {
                    Writer writer = new Writer();
                    writer.writeShort(idEntity);
                    writer.writeInt(client.mChar.id);
                    itemMap.item.write(writer);
                    ArrayList<Char> copyOfVecChar = getVecChar();
                    for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                        try {
                            Char c = copyOfVecChar.get(i);
                            if (vecChar.contains(c) && c.client != null) {
                                c.client.session.serivce.pickUpItem(writer);
                            }
                        } catch (Exception ex) {
                            Utlis.logError(Map.class, ex, "Da say ra loi pickUpItem:\n" + ex.getMessage());
                        }
                    }
                    return;
                } catch (Exception ex) {

                }
            } else {
                client.session.serivce.ShowMessWhite("Hành trang không đủ chỗ chứa.");
            }
        }

        public void addExpToAllChar(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.addExp(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi addExpToAllChar:\n" + ex.getMessage());
                }
            }
        }

        public void vutItemToAllChar(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.vutItem(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi vutItemToAllChar:\n" + ex.getMessage());
                }
            }
        }

        void removeItemMap(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.removeItemMap(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi removeItemMap:\n" + ex.getMessage());
                }
            }
        }

        public void mobAttackChar(Mob mob, Client client) {
            try {
                if (client.mChar.infoChar.isDie)
                    return;
                int dame = mob.getDameTheoHe(client.mChar);
                int neTranh = client.mChar.getNeTranh() / 100;
                boolean ne_Tranh = Utlis.randomBoolean(100, neTranh);
                if (ne_Tranh) {
                    sendNeSatThuong(client.mChar.id);
                    return;
                }
                client.mChar.MineHp(dame);

                if (client.mChar.getPhanDon() > 0) { // phản đòn
                    int damePhan = dame * client.mChar.getPhanDon() / 100;
                    client.mChar.setAttackMob(mob, damePhan, false);
                }
                Writer writer = new Writer();
                writer.writeShort(mob.idEntity);
                writer.writeInt(client.mChar.id);
                ArrayList<Char> copyOfVecChar = getVecChar();
                for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                    try {
                        Char c = copyOfVecChar.get(i);
                        if (vecChar.contains(c) && c.client != null) {
                            c.client.session.serivce.mobAttackChar(writer);
                        }
                    } catch (Exception ex) {
                        Utlis.logError(Map.class, ex, "Da say ra loi mobAttackChar:\n" + ex.getMessage());
                    }
                }
                client.mChar.msgUpdateHpMpWhenAttack(false, "");
            } catch (Exception ex) {
            }
        }

        public void updateHpMpWhenAttack(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.updateHpMpWhenAttack(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateHpMpWhenAttack:\n" + ex.getMessage());
                }
            }
        }

        public void reSpawn(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.reSpawn(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi reSpawn:\n" + ex.getMessage());
                }
            }
        }

        public void setXYChar(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.setXYChar(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi setXYChar:\n" + ex.getMessage());
                }
            }
        }

        public void addEffect(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.addEffect(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi addEffect:\n" + ex.getMessage());
                }
            }
        }

        public void removeEffect(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null) {
                        c.client.session.serivce.removeEffect(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi removeEffect:\n" + ex.getMessage());
                }
            }
        }

        public void updateHp_Orther(Client client, Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c != null && client != null && c.client != client) {
                        c.client.session.serivce.updateHp_Orther(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateHp_Orther:\n" + ex.getMessage());
                }
            }
        }

        public void updateHpFull_Orther(Client client, Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c != null && client != null && c.client != client) {
                        c.client.session.serivce.updateHpFull_Orther(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateHpFull_Orther:\n" + ex.getMessage());
                }
            }
        }

        public void updateMp_Orther(Client client, Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (c.client != null && client != null && c.client != client) {
                        c.client.session.serivce.updateMp_Orther(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateMp_Orther:\n" + ex.getMessage());
                }
            }
        }

        public void updateMpFull_Orther(Client client, Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null && client != null && c.client != client) {
                        c.client.session.serivce.updateMpFull_Orther(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateMpFull_Orther:\n" + ex.getMessage());
                }
            }
        }

        public void updateItemBody_Orther(Client client, Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c.client != null && client != null && c.client != client) {
                        c.client.session.serivce.updateItemBody_Orther(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateItemBody_Orther:\n" + ex.getMessage());
                }
            }
        }

        public void openNpc(Client client, short idNpc) {
            if (idNpc < 0 || idNpc > this.vecNpc.size()) {
                return;
            }
            Npc npc = vecNpc.get(idNpc);
            if (npc.id == 21) {
                client.mChar.zone.selectNpc(client, 11, 0, -1);
                return;
            }
            String[] array = Npc.getTextNpc(this, npc, client);
            String str = "";
            for (int i = 0; i < array.length; i++) {
                str += array[i];
                if (i < array.length - 1) {
                    str += ";";
                }
            }
            try {
                Writer writer = new Writer();
                writer.writeShort(idNpc);
                writer.writeUTF(str);
                client.session.serivce.openNpc(writer);
                UTPKoolVN.Debug("OPEN NPC ID: " + npc.id);
            } catch (Exception ex) {
                Utlis.logError(Map.class, ex, "Da say ra loi openNpc:\n" + ex.getMessage());
            }
        }

        public void selectNpc(Client client, int idNpc, int index1, int index2) {
            UTPKoolVN.Debug("npc: " + idNpc + ", index 1: " + index1 + ", index2, " + index2);
            if (idNpc < 0 || idNpc > this.vecNpc.size()) {
                return;
            }
            Npc npc = vecNpc.get(idNpc);
            Npc.getActionNpc(this, npc, client)[index1].action.action(client);
        }

        public void updateStatusChar(Writer writer) {
            ArrayList<Char> copyOfVecChar = getVecChar();
            for (int i = copyOfVecChar.size() - 1; i >= 0; i--) {
                try {
                    Char c = copyOfVecChar.get(i);
                    if (vecChar.contains(c) && c != null && c.client != null) {
                        c.client.session.serivce.updateStatusChar(writer);
                    }
                } catch (Exception ex) {
                    Utlis.logError(Map.class, ex, "Da say ra loi updateStatusChar:\n" + ex.getMessage());
                }
            }
        }

    }

    public static void createMap() {
        // Thêm null check
        if (DataCenter.gI() == null || DataCenter.gI().MapTemplate == null) {
            System.err.println("DataCenter hoặc MapTemplate chưa được khởi tạo!");
            return;
        }

        if (maps == null) {
            maps = new Map[DataCenter.gI().MapTemplate.length];
            for (int i = 0; i < maps.length; i++) {
                Map map = new Map(i);

                if (!DataCenter.gI().MapTemplate[i].notBlock) {
                    map.createZone();
                    map.createWayPoint();
                }

                maps[i] = map;
            }
        }
    }
}
