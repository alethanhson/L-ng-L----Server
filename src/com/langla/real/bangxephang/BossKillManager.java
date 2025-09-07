package com.langla.real.bangxephang;

import com.langla.lib.Utlis;
import com.langla.real.player.Char;
import com.langla.real.player.PlayerManager;

/**
 * Quản lý việc ghi điểm boss killer
 * 
 * @author PKoolVN
 */
public class BossKillManager {

    private static BossKillManager instance;

    public static BossKillManager gI() {
        if (instance == null) {
            instance = new BossKillManager();
        }
        return instance;
    }
}
