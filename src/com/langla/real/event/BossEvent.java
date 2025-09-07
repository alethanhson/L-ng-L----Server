package com.langla.real.event;

import com.langla.data.DataCache;
import com.langla.lib.Utlis;
import com.langla.real.map.BossRunTime;
import com.langla.real.map.BossTpl;
import com.langla.real.map.Map;
import com.langla.real.map.Mob;
import com.langla.real.player.PlayerManager;
import com.langla.server.lib.Message;
import com.langla.server.main.Maintenance;
import com.langla.utlis.UTPKoolVN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hệ thống Event Random Boss - Ninja Event
 * Random boss xuất hiện tại vị trí ngẫu nhiên
 * 
 * @author PKoolVN
 */
public class BossEvent {

    private static BossEvent instance;

    // Danh sách boss có thể spawn
    private List<BossTpl> availableBosses = new ArrayList<>();

    // Danh sách map có thể spawn boss
    private List<Integer> availableMaps = new ArrayList<>();

    // Thời gian giữa các lần spawn boss (phút)
    private int spawnIntervalMinutes = 10;

    // Thời gian boss tồn tại (phút)
    private int bossLifetimeMinutes = 30;

    // Boss hiện tại đang active
    private BossTpl currentBoss = null;
    private long bossSpawnTime = 0;
    private long nextSpawnTime = 0;

    public static BossEvent gI() {
        if (instance == null) {
            instance = new BossEvent();
        }
        return instance;
    }

    public BossEvent() {
        initializeAvailableMaps();
    }

    /**
     * Khởi tạo danh sách map có thể spawn boss
     */
    private void initializeAvailableMaps() {
        // Thêm các map phổ biến có thể spawn boss
        availableMaps.add(79); // Map chính
        availableMaps.add(57); // Map khác
        availableMaps.add(65);
        availableMaps.add(87);
        availableMaps.add(41);
    }

    /**
     * Thêm boss vào danh sách có thể spawn
     */
    public void addBoss(BossTpl boss) {
        if (boss != null && !availableBosses.contains(boss)) {
            availableBosses.add(boss);
            UTPKoolVN.Print("DEBUG: Added boss " + boss.name + " (ID: " + boss.id + ") to NinjaEvent");
        }
    }

    /**
     * Khởi động hệ thống event
     */
    public void startEvent() {
        UTPKoolVN.Print("DEBUG: Starting NinjaEvent with " + availableBosses.size() + " available bosses");

        // Tính thời gian spawn đầu tiên (random 30s - 60s)
        nextSpawnTime = System.currentTimeMillis() + Utlis.nextInt(30000, 60000);

        new Thread(() -> {
            while (!Maintenance.isRunning) {
                try {
                    long currentTime = System.currentTimeMillis();

                    // Kiểm tra nếu cần spawn boss mới
                    if (currentTime >= nextSpawnTime && currentBoss == null) {
                        spawnRandomBoss();
                    }

                    // Kiểm tra nếu boss hiện tại đã hết thời gian
                    if (currentBoss != null &&
                            currentTime >= bossSpawnTime + (bossLifetimeMinutes * 60000L)) {
                        removeCurrentBoss();
                    }

                    // Sleep 10 giây
                    Thread.sleep(10000);

                } catch (Exception ex) {
                    Utlis.logError(BossEvent.class, ex, "Lỗi trong NinjaEvent: " + ex.getMessage());
                }
            }
        }, "NinjaEvent Thread").start();
    }

    /**
     * Spawn boss ngẫu nhiên tại vị trí ngẫu nhiên
     */
    private void spawnRandomBoss() {
        if (availableBosses.isEmpty()) {
            UTPKoolVN.Print("WARNING: Không có boss nào để spawn");
            return;
        }

        // Random chọn boss
        BossTpl selectedBoss = availableBosses.get(Utlis.nextInt(availableBosses.size()));

        // Random chọn map
        int selectedMapId = availableMaps.get(Utlis.nextInt(availableMaps.size()));
        Map selectedMap = Map.maps[selectedMapId];

        if (selectedMap == null) {
            UTPKoolVN.Print("ERROR: Map " + selectedMapId + " không tồn tại");
            return;
        }

        // Random chọn zone (0-8)
        int randomZone = Utlis.nextInt(9);
        Map.Zone selectedZone = selectedMap.listZone.get(randomZone);

        // Random vị trí trong zone
        int randomX = Utlis.nextInt(100, 900); // Random X từ 100-900
        int randomY = Utlis.nextInt(100, 700); // Random Y từ 100-700

        // Tạo boss
        Mob bossMob = createBossMob(selectedBoss, randomX, randomY);

        // Thêm vào zone
        selectedZone.vecMob.add(bossMob);
        selectedZone.reSpawnMobToAllChar(bossMob);

        BossRunTime.gI().setBoss(selectedBoss);
        // Cập nhật trạng thái
        currentBoss = selectedBoss;
        bossSpawnTime = System.currentTimeMillis();
        nextSpawnTime = System.currentTimeMillis() + (spawnIntervalMinutes * 60000L);

        // Thông báo
        String message = String.format(
                "🔥 NINJA EVENT ��\nBoss %s (Level %d) vừa xuất hiện tại %s - Zone %d\nTọa độ: (%d, %d)\nThời gian tồn tại: %d phút",
                selectedBoss.name, selectedBoss.level, selectedMap.getMapTemplate().name,
                randomZone, randomX, randomY, bossLifetimeMinutes);

        UTPKoolVN.Print("NINJA EVENT: " + message);
        PlayerManager.getInstance().chatWord(message);
    }

    /**
     * Tạo Mob boss từ template
     */
    private Mob createBossMob(BossTpl bossTemplate, int x, int y) {
        Mob mob = new Mob();
        mob.createNewEffectList();
        mob.idEntity = DataCache.getIDMob();
        mob.id = bossTemplate.id;
        mob.hp = bossTemplate.hp;
        mob.hpGoc = bossTemplate.hp;
        mob.hpFull = bossTemplate.hp;
        mob.exp = bossTemplate.exp;
        mob.expGoc = bossTemplate.exp;
        mob.level = bossTemplate.level;
        mob.levelBoss = 10; // Boss level cao
        mob.paintMiniMap = true;
        mob.isBoss = true;
        mob.status = 0;
        mob.setXY((short) x, (short) y);
        mob.reSpawn();

        // Tăng cường boss cho event
        mob.hp = (int) (mob.hp / 2); // Tăng HP 50%
        mob.hpFull = mob.hp;
        mob.hpGoc = mob.hp;
        mob.exp = (int) (mob.exp / 2); // Tăng EXP gấp đôi
        mob.expGoc = mob.exp;

        return mob;
    }

    /**
     * Xóa boss hiện tại
     */
    private void removeCurrentBoss() {
        if (currentBoss != null) {
            UTPKoolVN.Print("NINJA EVENT: Boss " + currentBoss.name + " đã biến mất");
            PlayerManager.getInstance().chatWord("🔥 NINJA EVENT ��\nBoss " + currentBoss.name + " đã biến mất!");
            currentBoss = null;
            bossSpawnTime = 0;
        }
    }

    /**
     * Lấy thông tin boss hiện tại
     */
    public String getCurrentBossInfo() {
        if (currentBoss == null) {
            return "Không có boss nào đang active";
        }

        long remainingTime = (bossSpawnTime + (bossLifetimeMinutes * 60000L)) - System.currentTimeMillis();
        int remainingMinutes = (int) (remainingTime / 60000);

        return String.format("Boss hiện tại: %s (Level %d)\nThời gian còn lại: %d phút",
                currentBoss.name, currentBoss.level, Math.max(0, remainingMinutes));
    }

    /**
     * Cập nhật cấu hình event
     */
    public void updateConfig(int spawnInterval, int bossLifetime) {
        this.spawnIntervalMinutes = spawnInterval;
        this.bossLifetimeMinutes = bossLifetime;
        UTPKoolVN.Print("DEBUG: Updated NinjaEvent config - Spawn interval: " + spawnInterval + "min, Boss lifetime: "
                + bossLifetime + "min");
    }

    /**
     * Thêm map vào danh sách có thể spawn
     */
    public void addMap(int mapId) {
        if (!availableMaps.contains(mapId)) {
            availableMaps.add(mapId);
            UTPKoolVN.Print("DEBUG: Added map " + mapId + " to NinjaEvent available maps");
        }
    }

    /**
     * Lấy danh sách boss có sẵn
     */
    public List<BossTpl> getAvailableBosses() {
        return new ArrayList<>(availableBosses);
    }

    /**
     * Lấy danh sách map có sẵn
     */
    public List<Integer> getAvailableMaps() {
        return new ArrayList<>(availableMaps);
    }
}
