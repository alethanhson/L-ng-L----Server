package com.langla.real.map.handle;

import com.langla.data.DataCache;
import com.langla.data.Skill;
import com.langla.lib.Utlis;
import com.langla.real.map.CombatManager;
import com.langla.real.map.Map;
import com.langla.real.map.Mob;
import com.langla.real.player.Char;
import com.langla.real.player.Client;
import java.util.ArrayList;

public class AutoCamThuat {

    private static final int MAP_CAM_THUAT_ID = 89;
    private static final int MAX_ATTACK_ATTEMPTS = 20;

    // Định nghĩa skill cho từng hệ
    private static final int SKILL_HE_LOI = 8;
    private static final int SKILL_HE_THO = 2;
    private static final int SKILL_HE_THUY = 14;
    private static final int SKILL_HE_HOA = 20;
    private static final int SKILL_HE_PHONG = 26;

    /**
     * Function tự động hunt quái cho map cấm thuật
     * Nhân vật sẽ tự động tìm và tiêu diệt tất cả quái trong map
     */
    public static void startAutoHuntCamThuat(Client client, Map.Zone zone) {
        if (zone.map.mapID != MAP_CAM_THUAT_ID) {
            return;
        }

        try {
            // Kiểm tra xem nhân vật có đang auto hunt không
            if (client.mChar.isAutoHunting) {
                client.session.serivce.NhacNhoMessage("Bạn đang trong chế độ auto hunt!");
                return;
            }

            client.mChar.isAutoHunting = true;
            client.session.serivce.NhacNhoMessage("Bắt đầu auto hunt trong map cấm thuật!");

            // Bắt đầu thread auto hunt
            new Thread(() -> {
                autoHuntLoop(client, zone);
            }).start();

        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi bắt đầu auto hunt: " + ex.getMessage());
        }
    }

    /**
     * Vòng lặp chính của auto hunt
     */
    private static void autoHuntLoop(Client client, Map.Zone zone) {
        try {
            while (client.mChar.isAutoHunting && !client.mChar.infoChar.isDie
                    && client.mChar.zone != null && client.mChar.zone.map.mapID == MAP_CAM_THUAT_ID) {

                // Tìm quái gần nhất
                Mob targetMob = findNearestMob(client.mChar, zone);

                if (targetMob != null && targetMob.hp > 0) {
                    // Tập trung tiêu diệt mob này cho đến khi nó chết
                    boolean mobKilled = focusKillMob(client, targetMob, zone);

                    if (mobKilled) {
                        // Mob đã chết, tìm mob tiếp theo
                        Thread.sleep(500);
                    } else {
                        // Mob chưa chết, tiếp tục tấn công
                        Thread.sleep(1000);
                    }
                } else {
                    // Không có quái, chờ một chút
                    Thread.sleep(1000);
                }
            }

            // Kết thúc auto hunt
            if (client.mChar.isAutoHunting) {
                client.mChar.isAutoHunting = false;
                client.session.serivce.NhacNhoMessage("Đã dừng auto hunt!");
            }

        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi trong auto hunt loop: " + ex.getMessage());
            client.mChar.isAutoHunting = false;
        }
    }

    /**
     * Tập trung tiêu diệt một mob cụ thể
     */
    private static boolean focusKillMob(Client client, Mob targetMob, Map.Zone zone) {
        try {
            int attackCount = 0;

            while (targetMob.hp > 0 && !targetMob.isDie && attackCount < MAX_ATTACK_ATTEMPTS
                    && client.mChar.isAutoHunting && !client.mChar.infoChar.isDie) {

                // Kiểm tra xem mob còn tồn tại trong zone không
                if (!zone.vecMob.contains(targetMob)) {
                    return true; // Mob đã bị xóa khỏi zone
                }

                // Di chuyển đến mob nếu cần
                if (Math.abs(client.mChar.cx - targetMob.cx) > 150 ||
                        Math.abs(client.mChar.cy - targetMob.cy) > 100) {
                    moveToMob(client, targetMob, zone);
                    Thread.sleep(300); // Chờ di chuyển hoàn tất
                }

                // Tấn công mob
                attackMob(client, targetMob, zone);
                attackCount++;

                // Chờ một chút để mob nhận damage
                Thread.sleep(800);

                // Kiểm tra lại HP của mob
                if (targetMob.hp <= 0 || targetMob.isDie) {
                    return true; // Mob đã chết
                }
            }

            // Nếu vượt quá số lần tấn công, coi như mob đã chết
            if (attackCount >= MAX_ATTACK_ATTEMPTS) {
                return true;
            }

            return false;

        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi tập trung tiêu diệt mob: " + ex.getMessage());
            return true; // Coi như đã xử lý xong để tránh lỗi
        }
    }

    /**
     * Di chuyển nhân vật đến quái
     */
    private static void moveToMob(Client client, Mob mob, Map.Zone zone) {
        try {
            if (client.mChar.infoChar.isDie)
                return;

            // Tính toán vị trí tối ưu để tấn công (cách mob 100px)
            int targetX = mob.cx;
            int targetY = mob.cy;

            // Điều chỉnh vị trí để tránh đứng quá gần quái
            if (client.mChar.cx < mob.cx) {
                targetX = mob.cx - 100;
            } else {
                targetX = mob.cx + 100;
            }

            // Đảm bảo vị trí không vượt quá giới hạn map
            if (targetX < 50)
                targetX = 50;
            if (targetX > zone.map.getMapTemplate().maxX - 50)
                targetX = zone.map.getMapTemplate().maxX - 50;
            if (targetY < 50)
                targetY = 50;
            if (targetY > zone.map.getMapTemplate().maxY - 50)
                targetY = zone.map.getMapTemplate().maxY - 50;

            // Di chuyển nhân vật
            client.mChar.setXY(targetX, targetY);
            client.session.serivce.setXYChar();

        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi di chuyển đến quái: " + ex.getMessage());
        }
    }

    /**
     * Tấn công quái với skill phù hợp với hệ
     */
    private static void attackMob(Client client, Mob mob, Map.Zone zone) {
        try {
            if (client.mChar.infoChar.isDie || mob.hp <= 0)
                return;

            // Kiểm tra khoảng cách trước khi tấn công
            int distance = Utlis.getRange(mob.cx, client.mChar.cx);
            if (distance > 200) {
                // Quá xa, không thể tấn công
                return;
            }

            // Chọn skill phù hợp với hệ của nhân vật
            int skillId = selectBestSkillForElement(client.mChar);

            // Sử dụng skill tấn công
            CombatManager.attackMob(client, skillId, mob.idEntity, zone);

        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi tấn công quái: " + ex.getMessage());
        }
    }

    /**
     * Chọn skill tốt nhất dựa trên hệ của nhân vật
     */
    private static int selectBestSkillForElement(Char character) {
        try {
            // Lấy hệ của nhân vật
            int element = character.infoChar.idhe;

            // Chọn skill theo hệ
            switch (element) {
                case 1: // Hệ Lôi
                    return SKILL_HE_LOI;
                case 2: // Hệ Thổ
                    return SKILL_HE_THO;
                case 3: // Hệ Thủy
                    return SKILL_HE_THUY;
                case 4: // Hệ Hỏa
                    return SKILL_HE_HOA;
                case 5: // Hệ Phong
                    return SKILL_HE_PHONG;
                default:
                    // Nếu không xác định được hệ, sử dụng skill cơ bản
                    return SKILL_HE_THO;
            }
        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi chọn skill theo hệ: " + ex.getMessage());
            return SKILL_HE_THO; // Fallback về skill cơ bản
        }
    }

    /**
     * Kiểm tra xem mob có còn sống và tồn tại trong zone không
     */
    private static boolean isMobAliveAndValid(Mob mob, Map.Zone zone) {
        try {
            if (mob == null)
                return false;

            // Kiểm tra xem mob có trong danh sách zone không
            if (!zone.vecMob.contains(mob))
                return false;

            // Kiểm tra HP và trạng thái
            return mob.hp > 0 && !mob.isDie;

        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Tìm quái gần nhất với nhân vật
     */
    private static Mob findNearestMob(Char character, Map.Zone zone) {
        try {
            Mob nearestMob = null;
            double minDistance = Double.MAX_VALUE;

            for (Mob mob : zone.vecMob) {
                if (isMobAliveAndValid(mob, zone)) {
                    double distance = Utlis.getRange(mob.cx, character.cx);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestMob = mob;
                    }
                }
            }

            return nearestMob;
        } catch (Exception ex) {
            Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi tìm quái gần nhất: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Dừng auto hunt
     */
    public static void stopAutoHuntCamThuat(Client client) {
        if (client.mChar.isAutoHunting) {
            client.mChar.isAutoHunting = false;
            client.session.serivce.NhacNhoMessage("Đã dừng auto hunt!");
        }
    }

    /**
     * Kiểm tra và tự động hunt khi vào map cấm thuật
     */
    public static void checkAndStartAutoHunt(Client client, Map.Zone zone) {
        if (zone.map.mapID == MAP_CAM_THUAT_ID && !client.mChar.isAutoHunting) {
            // Tự động bắt đầu hunt sau 1 giây
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    if (client.mChar.zone != null && client.mChar.zone.map.mapID == MAP_CAM_THUAT_ID
                            && !client.mChar.isAutoHunting && !client.mChar.infoChar.isDie) {

                        // Kiểm tra xem có quái trong zone không
                        if (!zone.vecMob.isEmpty()) {
                            startAutoHuntCamThuat(client, zone);
                        } else {
                            // Chờ quái xuất hiện
                            Thread.sleep(2000);
                            if (!zone.vecMob.isEmpty()) {
                                startAutoHuntCamThuat(client, zone);
                            }
                        }
                    }
                } catch (Exception ex) {
                    Utlis.logError(AutoCamThuat.class, ex, "Lỗi khi tự động bắt đầu hunt: " + ex.getMessage());
                }
            }).start();
        }
    }

    /**
     * Kiểm tra xem có phải map cấm thuật không
     */
    public static boolean isCamThuatMap(int mapId) {
        return mapId == MAP_CAM_THUAT_ID;
    }

    /**
     * Lấy thông tin trạng thái auto hunt
     */
    public static String getAutoHuntStatus(Client client) {
        if (client.mChar.isAutoHunting) {
            return "Đang auto hunt";
        } else {
            return "Không auto hunt";
        }
    }

    /**
     * Lấy thông tin hệ và skill đang sử dụng
     */
    public static String getElementAndSkillInfo(Char character) {
        try {
            int element = character.infoChar.idhe;
            String elementName = getElementName(element);
            int skillId = selectBestSkillForElement(character);
            String skillName = getSkillName(skillId);

            return "Hệ: " + elementName + " | Skill: " + skillName + " (ID: " + skillId + ")";
        } catch (Exception ex) {
            return "Không thể lấy thông tin hệ và skill";
        }
    }

    /**
     * Lấy tên hệ
     */
    private static String getElementName(int element) {
        switch (element) {
            case 1:
                return "Lôi";
            case 2:
                return "Thổ";
            case 3:
                return "Thủy";
            case 4:
                return "Hỏa";
            case 5:
                return "Phong";
            default:
                return "Không xác định";
        }
    }

    /**
     * Lấy tên skill
     */
    private static String getSkillName(int skillId) {
        switch (skillId) {
            case 8:
                return "LOA_KIEM";
            case 30:
                return "KIEM_THUAT_CO_BAN";
            case 17:
                return "THUY_LAO_THUAT";
            case 21:
                return "AO_THUAT_TAM_PHAP";
            case 26:
                return "PHI_LOI_THAN_THUAT";
            default:
                return "Skill " + skillId;
        }
    }
}
