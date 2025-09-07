package com.langla.real.bangxephang;

import com.langla.real.player.Char;

/**
 * Template cho xếp hạng Boss Killer
 * @author PKoolVN
 */
public class BossKillerRanking {
    
    public int playerId;                 // ID người chơi
    public String playerName;            // Tên người chơi
    public int level;                    // Cấp độ người chơi
    public int bossKills;                // Tổng số boss đã tiêu diệt
    public long lastBossKillTime;        // Thời gian tiêu diệt boss cuối
    public String lastBossName;          // Tên boss cuối cùng bị tiêu diệt
    public int rank;                     // Thứ hạng hiện tại
    
    public BossKillerRanking() {}
    
    public BossKillerRanking(Char player) {
        this.playerId = player.id;
        this.playerName = player.infoChar.name;
        this.level = player.level();
        this.bossKills = 0;
        this.lastBossKillTime = 0;
        this.lastBossName = "";
        this.rank = 0;
    }
}
