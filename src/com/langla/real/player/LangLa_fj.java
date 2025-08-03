package com.langla.real.player;

import java.util.Comparator;

 class LangLa_fj implements Comparator {
    LangLa_fj() {
    }

    
    public int compare(Object obj, Object obj2) {
        XYEntity xYEntity = (XYEntity) obj;
        XYEntity xYEntity2 = (XYEntity) obj2;
        return xYEntity.cy == xYEntity2.cy ? xYEntity2.cx - xYEntity.cx : xYEntity.cy - xYEntity2.cy;
    }
}
