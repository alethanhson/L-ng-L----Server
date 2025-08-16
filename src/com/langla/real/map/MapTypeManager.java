package com.langla.real.map;

import com.langla.real.player.*;
import com.langla.real.family.*;
import java.util.*;

public class MapTypeManager {

    public enum MapType {
        NORMAL, // Map thường
        SPECIAL_EVENT, // Map sự kiện đặc biệt
        FAMILY_GATE, // Ải gia tộc
        CAM_THUAT, // Cấm thuật
        BOSS_ARENA, // Arena boss
        TRAINING // Map luyện tập
    }

    public static MapType getMapType(int mapId) {
        switch (mapId) {
            case 89:
                return MapType.CAM_THUAT;
            case 46:
            case 47:
                return MapType.FAMILY_GATE;
            case 84:
                return MapType.TRAINING;
            default:
                return MapType.NORMAL;
        }
    }

    public static boolean isSpecialMap(int mapId) {
        return getMapType(mapId) != MapType.NORMAL;
    }

    public static boolean requiresSpecialHandling(int mapId) {
        return mapId == 89 || mapId == 46 || mapId == 47;
    }
}
