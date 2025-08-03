package com.langla.data;

import com.langla.lib.Utlis;
import com.langla.real.player.XYEntity;
import java.util.Vector;

public class XYBlockMap {

    public BlockMap[] a;

    public MapTemplate map;

    public XYBlockMap(BlockMap[] var1, MapTemplate map) {
        this.a = var1;
        this.map = map;
    }

    public boolean a(int var1, int var2) {
        if (this.a != null) {
            for (int var3 = 0; var3 < this.a.length; ++var3) {
                if (this.a[var3].a(var1, var2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public XYEntity b(int var1, int var2) {
        if (!this.a(var1, var2)) {
            return XYEntity.create(var1, var2);
        } else {
            for (int var3 = 1; var3 < 1000; ++var3) {
                if (!this.a(var1 + var3, var2)) {
                    return XYEntity.create(var1 + var3, var2);
                }

                if (!this.a(var1, var2 + var3)) {
                    return XYEntity.create(var1, var2 + var3);
                }

                if (!this.a(var1 - var3, var2)) {
                    return XYEntity.create(var1 - var3, var2);
                }

                if (!this.a(var1, var2 - var3)) {
                    return XYEntity.create(var1, var2 - var3);
                }

                if (!this.a(var1 + var3, var2 + var3)) {
                    return XYEntity.create(var1 + var3, var2 + var3);
                }

                if (!this.a(var1 - var3, var2 + var3)) {
                    return XYEntity.create(var1 - var3, var2 + var3);
                }

                if (!this.a(var1 + var3, var2 - var3)) {
                    return XYEntity.create(var1 + var3, var2 - var3);
                }

                if (!this.a(var1 - var3, var2 - var3)) {
                    return XYEntity.create(var1 - var3, var2 - var3);
                }
            }

            return XYEntity.create(var1, var2);
        }
    }

    public XYEntity c(int var1, int var2) {
        try {
            XYEntity var8;
            XYEntity var9 = XYEntity.create((var8 = XYEntity.create(var1, var2)).cx, map.maxY + 10);
            Vector var3 = new Vector();
            int var4;
            if (this.a != null) {
                for (var4 = 0; var4 < this.a.length; ++var4) {
                    var3.addAll(this.a[var4].a(var8, var9));
                }
            }

            for (var4 = 0; var4 < map.j.size(); ++var4) {
                for (int var5 = 0; var5 < ((BlockMap) map.j.get(var4)).arrayXY.length - 1; ++var5) {
                    XYEntity var6;
                    if ((var6 = Utlis.a(var8, var9, ((BlockMap) map.j.get(var4)).arrayXY[var5], ((BlockMap) map.j.get(var4)).arrayXY[var5 + 1])) != null) {
                        var3.add(var6);
                    }
                }
            }

            if (var3.size() > 0) {
                var4 = 0;
                XYEntity var10 = null;

                for (int var11 = 0; var11 < var3.size(); ++var11) {
                    var9 = (XYEntity) var3.get(var11);
                    if (var11 == 0 || Utlis.positive(var9.cy - var8.cy) < var4) {
                        var10 = var9;
                        var4 = Utlis.positive(var9.cy - var8.cy);
                    }
                }

                return var10;
            }
        } catch (Exception var7) {
        }

        return null;
    }
}
