package com.langla.real.map;

import com.langla.data.*;
import com.langla.real.item.Item;
import com.langla.real.player.Client;
import com.langla.utlis.UTPKoolVN;
import com.langla.lib.Utlis;
import java.util.*;

public class DropManager {

    // Boss Drop Configuration
    private static final List<Integer> BOSS_GEM_ITEMS = Arrays.asList(406, 407, 408, 409, 410, 411, 412, 413, 826, 827);
    private static final List<Integer> BOSS_HOKAGE_ITEMS = Arrays.asList(174, 175, 179, 216, 217, 218, 248, 278, 302,
            315);
    private static final double BOSS_HOKAGE_DROP_RATE = 30.0; // 30%

    // Normal Mob Drop Configuration
    private static final double STONE_DROP_RATE = 10.0; // 10%
    private static final double LOCKED_SILVER_DROP_RATE = 50.0; // 50%

    // Special Map Drop Configuration
    private static final List<Integer> CAM_THUAT_ITEMS = Arrays.asList(354, 562, 564, 566);
    private static final List<Integer> SHARINGAN_ITEMS = Arrays.asList(1001, 1002, 1003);
    private static final double SHARINGAN_DROP_RATE = 30.0; // 30%

    public static void dropBossItems(Client client, Mob mob, Map.Zone zone) {
        // Drop đá khảm
        dropBossGems(client, mob, zone);

        // Drop lệnh bài Hokage
        dropBossHokageItems(client, mob, zone);

        // Drop trang bị Hokage
        dropBossHokageEquipment(client, mob, zone);
    }

    private static void dropBossGems(Client client, Mob mob, Map.Zone zone) {
        int gemCount = Utlis.nextInt(1, mob.level / 2);
        for (int i = 0; i < gemCount; i++) {
            int x = Utlis.nextInt(50);
            Item item = new Item(UTPKoolVN.getRandomList(BOSS_GEM_ITEMS));
            createItemMap(item, mob, client, zone, x, i);
        }
    }

    private static void dropBossHokageItems(Client client, Mob mob, Map.Zone zone) {
        int itemCount = Utlis.nextInt(1, mob.level / 2);
        for (int i = 0; i < itemCount; i++) {
            int x = Utlis.nextInt(50);
            Item item = new Item(UTPKoolVN.getRandomList(BOSS_HOKAGE_ITEMS));
            createItemMap(item, mob, client, zone, x, i);
        }
    }

    private static void dropBossHokageEquipment(Client client, Mob mob, Map.Zone zone) {
        if (Utlis.nextInt(100) < BOSS_HOKAGE_DROP_RATE) {
            List<Integer> equipmentList = getHokageEquipmentByLevel(mob.level);
            int x = Utlis.nextInt(-50, 50);
            Item item = new Item(UTPKoolVN.getRandomList(equipmentList));

            // Set options cho trang bị Hokage
            if (item.isVuKhi()) {
                Item.setOptionsVuKhi_hokage(item, item.getItemTemplate().levelNeed);
            } else {
                Item.setOptionsTrangBiPhuKien_hokage(item, item.getItemTemplate().levelNeed);
            }
            item.createItemOptions();

            createItemMap(item, mob, client, zone, x, 0);
        }
    }

    private static List<Integer> getHokageEquipmentByLevel(int level) {
        if (level >= 50) {
            return Arrays.asList(58, 63, 68, 73, 78, 83, 88, 93, 98, 103, 108, 113, 118, 123);
        } else if (level >= 40) {
            return Arrays.asList(57, 62, 67, 72, 77, 82, 87, 92, 97, 102, 107, 112, 117, 122);
        } else if (level >= 30) {
            return Arrays.asList(56, 61, 66, 71, 76, 81, 86, 91, 96, 101, 106, 111, 116, 121);
        } else if (level >= 20) {
            return Arrays.asList(55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120);
        }
        return Arrays.asList(54, 59, 64, 69, 74, 79, 84, 89, 94, 99, 104, 109, 114, 119);
    }

    public static void dropNormalMobItems(Client client, Mob mob, Map.Zone zone) {
        // Kiểm tra chênh lệch level
        if (Math.abs(client.mChar.level() - mob.level) > 10) {
            return;
        }

        int x = Utlis.nextInt(-50, 50);
        int tile = Utlis.nextInt(100);

        // Xử lý map đặc biệt
        if (client.mChar.zone.map.mapID == 89) {
            dropCamThuatItems(client, mob, zone);
            return;
        }

        // Drop đá cấp
        if (tile < STONE_DROP_RATE) {
            dropStoneByLevel(mob, zone, x);
        }

        // Drop bạc khóa
        if (tile < LOCKED_SILVER_DROP_RATE) {
            dropLockedSilverByLevel(mob, zone, x);
        }
    }

    private static void dropCamThuatItems(Client client, Mob mob, Map.Zone zone) {
        int tileSharingan = Utlis.nextInt(100);
        Item item;

        if (tileSharingan < SHARINGAN_DROP_RATE) {
            // Drop đá Sharingan
            item = new Item(UTPKoolVN.getRandomList(SHARINGAN_ITEMS));
        } else {
            // Drop đá chế tạo thường
            item = new Item(UTPKoolVN.getRandomList(CAM_THUAT_ITEMS));
        }

        createItemMap(item, mob, client, zone, 0, 0);
    }

    private static void dropStoneByLevel(Mob mob, Map.Zone zone, int x) {
        int stoneLevel = getStoneLevelByMobLevel(mob.level);
        if (stoneLevel > 0) {
            Item item = new Item(stoneLevel);
            createItemMap(item, mob, null, zone, x, 0);
        }
    }

    private static int getStoneLevelByMobLevel(int mobLevel) {
        if (mobLevel >= 50) {
            return Utlis.nextInt(4, 6); // Đá cấp 4, 5, 6
        } else if (mobLevel >= 40) {
            return Utlis.nextInt(4, 5); // Đá cấp 4, 5
        } else if (mobLevel >= 30) {
            return Utlis.nextInt(3, 4); // Đá cấp 3, 4
        } else if (mobLevel >= 20) {
            return Utlis.nextInt(2, 3); // Đá cấp 2, 3
        }
        return 0;
    }

    private static void dropLockedSilverByLevel(Mob mob, Map.Zone zone, int x) {
        int silverAmount = getSilverAmountByMobLevel(mob.level);
        if (silverAmount > 0) {
            Item item = new Item(163, true, silverAmount); // ID 163 = Bạc khóa
            createItemMap(item, mob, null, zone, x, 0);
        }
    }

    private static int getSilverAmountByMobLevel(int mobLevel) {
        if (mobLevel >= 50) {
            return Utlis.nextInt(2000, 2500);
        } else if (mobLevel >= 40) {
            return Utlis.nextInt(1500, 2000);
        } else if (mobLevel >= 30) {
            return Utlis.nextInt(1000, 1500);
        } else if (mobLevel >= 20) {
            return Utlis.nextInt(500, 1000);
        }
        return 0;
    }

    private static void createItemMap(Item item, Mob mob, Client client, Map.Zone zone, int x, int index) {
        ItemMap itemMap = ItemMap.createItemMap(item, mob.cx + (index % 2 == 0 ? x : -x), mob.cy);
        itemMap.idEntity = DataCache.getIDItemMap();
        itemMap.idChar = client != null ? client.mChar.id : -1;
        itemMap.isSystem = true;
        
        // Sửa: sử dụng method có sẵn trong zone
        zone.createItemMap(itemMap, mob);
    }
}
