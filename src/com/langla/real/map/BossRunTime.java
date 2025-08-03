package com.langla.real.map;

import com.langla.data.DataCache;
import com.langla.lib.Utlis;
import com.langla.real.player.PlayerManager;
import com.langla.real.task.TaskHandler;
import com.langla.server.lib.Message;
import com.langla.server.main.Maintenance;
import com.langla.utlis.UTPKoolVN;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PKoolVN
 **/


public class BossRunTime {

    // Võ Lâm Cao thủ
    public List<BossTpl> listBoss = new ArrayList<BossTpl>();
    private static BossRunTime instance;

    public static BossRunTime gI() {
        if (instance == null) {
            instance = new BossRunTime();
        }
        return instance;
    }
    public BossRunTime(){

    }

    public BossTpl getBoss (int id){
        for (int i = 0; i < listBoss.size(); i++){
            BossTpl boss = listBoss.get(i);
            if(boss.id == id){
               return boss;
            }
        }
        return null;
    }

    public void setBoss (BossTpl bossnew){
        for (int i = 0; i < listBoss.size(); i++){
            BossTpl boss = listBoss.get(i);
            if(boss.id == bossnew.id){
                listBoss.set(i, bossnew); // Cập nhật
                break; // Thoát khỏi vòng lặp sau khi đã cập nhật
            }
        }
    }
    public void StartBossRunTime() {
        new Thread(() -> {
            while (!Maintenance.isRuning) {
                try {
                for (int i = 0; i < listBoss.size(); i++){
                    BossTpl boss = listBoss.get(i);
                    if(boss.timeDelay < System.currentTimeMillis()){
                        if(boss.isDie){
                            Map map = Map.maps[boss.map];
                            int zonerandom = Utlis.nextInt(9);
                            Map.Zone zone = map.listZone.get(zonerandom);
                            Mob mob2 = new Mob();
                            mob2.createNewEffectList();
                            mob2.idEntity = DataCache.getIDMob();
                            mob2.id = boss.id;
                            mob2.hp = boss.hp;
                            mob2.hpGoc = boss.hp;
                            mob2.hpFull = boss.hp;
                            mob2.exp = boss.exp;
                            mob2.expGoc = boss.exp;
                            mob2.level = boss.level;
                            mob2.levelBoss = 0;
                            mob2.paintMiniMap = true;
                            mob2.isBoss = true;
                            mob2.status = 0;
                            mob2.setXY((short) boss.cx, (short) boss.cy);
                            mob2.reSpawn();
                            zone.vecMob.add(mob2);
                            zone.reSpawnMobToAllChar(mob2);

                            Message msg = new Message((byte) 1);
                            mob2.write(msg.writer);
                            zone.SendZoneMessage(msg);
                            boss.timeDelay = System.currentTimeMillis()+(boss.min_spam*60000L);
                            boss.isDie = false;
                            UTPKoolVN.Print("Create Boss: "+boss.name+" Map: "+boss.map+" Zone: "+zonerandom+" ");
                            PlayerManager.getInstance().chatWord("Boss "+boss.name+" vừa xuất hiện tại "+map.getMapTemplate().name);
                        } else {
                            Map map = Map.maps[boss.map];
                            boss.timeDelay = System.currentTimeMillis()+(boss.min_spam*60000L);
                            PlayerManager.getInstance().chatWord("Boss "+boss.name+" đã xuất hiện tại  "+map.getMapTemplate().name);
                        }
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception ex) {
                    Utlis.logError(BossRunTime.class, ex, "Da say ra loi:\n" + ex.getMessage());
                }
            }
        }, "Boss RUN TIME").start();
    }
}
