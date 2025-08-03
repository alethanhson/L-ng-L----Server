package com.langla.data;

public class LangLa_fq {

//    public static mImage a = Binary.createImage(1, 1);
//    public static Hashtable b = new Hashtable();
//    public static Hashtable c = new Hashtable();
//    public static Hashtable d = new Hashtable();
//    public static Hashtable e = new Hashtable();
//    public static Hashtable f = new Hashtable();
//    public static Hashtable g = new Hashtable();
//    public static Vector h = new Vector();
//    public static Vector i = new Vector();
//    public static Vector j = new Vector();
//    private static boolean k;
//    private static int l;
//
//    private static boolean c(int var0) {
//        return var0 == 3 || var0 == 4 || var0 == 24 || var0 == 80 || var0 <= -2 && var0 > -2000;
//    }
//
//    public static mImage a(int var0) {
//        mImage var1;
//        if ((var1 = (mImage) e.get((short) var0)) == null) {
//            a(var0, "client", e);
//            var1 = a;
//        }
//
//        return var1;
//    }
//
//    public static mImage b(int var0) {
//        return Binary.createImage("client_icon_" + var0);
//    }
//
//    public static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
//        if (var1 == 713) {
//            if (DataCenter.gI().i % 7 == 0) {
//                if (!k) {
//                    l += 4;
//                } else {
//                    l -= 4;
//                }
//
//                if (l == 0) {
//                    k = false;
//                } else if (l == 40) {
//                    k = true;
//                }
//            }
//
//            var0.d(100 - l / 2);
//            Binary2.a(var0, var1, var2, var3 + 4, var4 + 4, 100 - l, (byte) 3);
//            var0.m = 1.0F;
//            return new short[]{0, 0};
//        } else if (c(var1)) {
//            var0.a(var0, var1, var2, var3, var4, 20 * DataCenter.gI().zoomLevel, 20 * DataCenter.gI().zoomLevel);
//            return new short[]{20, 20};
//        } else {
//            return a(var0, var1, var2, var3, var4, 0, 0, var5, "client", e, (byte) 0, b, DataCenter.gI().aj);
//        }
//    }
//
//    public static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
//        try {
//            Short var9 = new Short((short) var1);
//            mImage var10;
//            if ((var10 = (mImage) f.get(var9)) != null) {
//                var0.a(var10, 0, 0, var10.c, var10.d, var2, var3 - var5 / 2, var4 - var6, var5 * DataCenter.gI().zoomLevel, var6 * DataCenter.gI().zoomLevel, 20);
//            } else {
//                a(var1, "tree", f);
//            }
//
//            return new short[]{(short) var10.c, (short) var10.d};
//        } catch (Exception var8) {
//            return new short[]{0, 0};
//        }
//    }
//
//    public static short[] b(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
//        return a(var0, var1, var2, var3, var4, 0, 0, var5, "char", g, (byte) 2, d, DataCenter.gI().ak);
//    }
//
//    public static short[] b(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
//        return a(var0, var1, var2, var3, var4, 51, var6, 33, "char", g, (byte) 2, d, DataCenter.gI().ak);
//    }
//
//    public static short[] c(Graphics var0, int var1, int var2, int var3, int var4, int var5) {
//        return a(var0, var1, var2, var3, var4, 3, "char", g, (byte) 2, d, DataCenter.gI().ak);
//    }
//
//    private static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, String var6, Hashtable var7, byte var8, Hashtable var9, Hashtable var10) {
//        try {
//            Short var15 = new Short((short) var1);
//            mImage var16;
//            if ((var16 = (mImage) var7.get(var15)) == null) {
//                a(var1, var6, var7);
//            }
//
//            if (var0 != null && var16 != null && (var16.c > 1 || var16.d > 1)) {
//                if (var6.equals("tree")) {
//                    var0.a(var16, 0, 0, var16.c, var16.d, var2, var3, var4, 20);
//                } else if (var6.equals("char")) {
//                    LangLa_et var12;
//                    int var14 = (var12 = (LangLa_et) var10.get(var15)).d * DataCenter.gI().zoomLevel;
//                    var1 = var12.e * DataCenter.gI().zoomLevel;
//                    var0.b(var16, 0, 0, var16.c, var16.d, var2, var3, var4, var14, var1, var5);
//                } else {
//                    boolean var13 = true;
//                    var0.l = var13;
//                    var0.a(var16, 0, 0, var16.c, var16.d, var2, var3, var4, var5);
//                    var13 = false;
//                    var0.l = var13;
//                }
//
//                return new short[]{(short) var16.c, (short) var16.d};
//            }
//        } catch (Exception var11) {
//        }
//
//        return new short[]{0, 0};
//    }
//
//    private static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, String var8, Hashtable var9, byte var10, Hashtable var11, Hashtable var12) {
//        try {
//            Short var20 = new Short((short) var1);
//            mImage var22;
//            if ((var22 = (mImage) var9.get(var20)) == null) {
//                a(var1, var8, var9);
//            }
//
//            if (var0 != null && var22 != null && (var22.c > 1 || var22.d > 1)) {
//                String var14 = var8;
//                byte var17 = -1;
//                switch (var14.hashCode()) {
//                    case -1357712437:
//                        if (var14.equals("client")) {
//                            var17 = 2;
//                        }
//                        break;
//                    case 3052374:
//                        if (var14.equals("char")) {
//                            var17 = 1;
//                        }
//                        break;
//                    case 3568542:
//                        if (var14.equals("tree")) {
//                            var17 = 0;
//                        }
//                }
//
//                switch (var17) {
//                    case 0:
//                        var0.a(var22, 0, 0, var22.c, var22.d, var2, var3, var4, 20);
//                        break;
//                    case 1:
//                        LangLa_et var15;
//                        if ((var15 = (LangLa_et) var12.get(var20)) == null) {
//                            var15 = (LangLa_et) DataCenter.gI().am.get(var20);
//                        }
//
//                        int var19 = var15.d * DataCenter.gI().zoomLevel;
//                        int var18 = var15.e * DataCenter.gI().zoomLevel;
//                        boolean var21 = var0.k && var2 < 0;
//                        if ((var5 > 0 || var6 > 0) && var5 <= var15.d && var6 <= var15.e) {
//                            var19 = var5 * DataCenter.gI().zoomLevel;
//                            var18 = var6 * DataCenter.gI().zoomLevel;
//                            if (var21) {
//                                var0.b(var22, 0, var15.e - var6, var22.c, var18, var2, var3, var4, var19, var18, var7);
//                            } else {
//                                var0.a(var22, 0, var15.e - var6, var22.c, var18, var2, var3, var4, var19, var18, var7);
//                            }
//                        } else if (var21) {
//                            var0.b(var22, 0, 0, var22.c, var22.d, var2, var3, var4, var19, var18, var7);
//                        } else {
//                            var0.a(var22, 0, 0, var22.c, var22.d, var2, var3, var4, var19, var18, var7);
//                        }
//                        break;
//                    case 2:
//                        boolean var16 = true;
//                        var0.l = var16;
//                        var0.a(var22, 0, 0, var22.c, var22.d, var2, var3, var4, var7);
//                        var16 = false;
//                        var0.l = var16;
//                }
//
//                return new short[]{(short) var22.c, (short) var22.d};
//            }
//        } catch (Exception var13) {
//        }
//
//        return new short[]{0, 0};
//    }
//
//    public static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
//        return a(var0, var1, var2, var3, var4, var5, var6, "client", e, (byte) 0, b, DataCenter.gI().aj);
//    }
//
//    public static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
//        if (c(var1)) {
//            var0.a(var0, var1, 0, var3, var4, var5, var6);
//            return new short[]{20, 20};
//        } else {
//            return a(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, "client", e, (byte) 0, b, DataCenter.gI().aj);
//        }
//    }
//
//    private static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, String var12, Hashtable var13, byte var14, Hashtable var15, Hashtable var16) {
//        try {
//            Short var19 = new Short((short) var1);
//            Object var20;
//            if ((var20 = var13.get(var19)) != null) {
//                Graphics var10000;
//                mImage var10001;
//                mImage var18;
//                int var10002;
//                int var10003;
//                int var10004;
//                int var10005;
//                label37:
//                {
//                    var18 = (mImage) var20;
//                    if (var1 != 101 && var1 != 102) {
//                        var10000 = var0;
//                        var10001 = var18;
//                        var10002 = var7;
//                        var10003 = var8;
//                        var10004 = var9 == 0 ? var18.c / DataCenter.gI().zoomLevel : var9;
//                        if (var10 == 0) {
//                            var10005 = var18.d / DataCenter.gI().zoomLevel;
//                            break label37;
//                        }
//                    } else {
//                        var10000 = var0;
//                        var10001 = var18;
//                        var10002 = var7;
//                        var10003 = var8;
//                        var10004 = var9;
//                    }
//
//                    var10005 = var10;
//                }
//
//                Binary2.a(var10000, var10001, var10002, var10003, var10004, var10005, 0, 0, var2, var3, var4, var5, var6, var11);
//                return new short[]{(short) var18.c, (short) var18.d};
//            }
//
//            a(var1, var12, var13);
//        } catch (Exception var17) {
//        }
//
//        return new short[]{0, 0};
//    }
//
//    private static short[] a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, String var7, Hashtable var8, byte var9, Hashtable var10, Hashtable var11) {
//        try {
//            Short var15 = new Short((short) var1);
//            Object var16;
//            if ((var16 = var8.get(var15)) != null) {
//                mImage var13 = (mImage) var16;
//                boolean var14 = true;
//                var0.k = var14;
//                Binary2.a(var0, var13, 0, 0, var13.c / DataCenter.gI().zoomLevel, var13.d / DataCenter.gI().zoomLevel, 0, 0, var2, var3, var4, var13.c * var5 / 100, var13.d * var5 / 100, var6);
//                var14 = false;
//                var0.k = var14;
//                return new short[]{(short) var13.c, (short) var13.d};
//            }
//
//            a(var1, var7, var8);
//        } catch (Exception var12) {
//        }
//
//        return new short[]{0, 0};
//    }
//
//    public static int a(mImage var0) {
//        return var0 == null ? 0 : var0.c / DataCenter.gI().zoomLevel;
//    }
//
//    public static int b(mImage var0) {
//        return var0 == null ? 0 : var0.d / DataCenter.gI().zoomLevel;
//    }
//
//    public static void a() {
//        try {
//            while (!DataCenter.gI().ba) {
//                Thread.sleep(100L);
//            }
//
//            Iterator var0 = d.keySet().iterator();
//
//            while (var0.hasNext()) {
//                Object var1 = var0.next();
//                Binary.delete("char_" + var1 + ".bin");
//            }
//
//            Binary2.a(d);
//            c();
//        } catch (Exception var2) {
//        }
//    }
//
//    public static void b() {
//        try {
//            while (!DataCenter.gI().ba) {
//                Thread.sleep(100L);
//            }
//
//            Iterator var0 = b.keySet().iterator();
//
//            while (var0.hasNext()) {
//                Object var1 = var0.next();
//                Binary.delete("client_" + var1 + ".bin");
//            }
//
//            Binary2.a(b);
//            a(true);
//        } catch (Exception var2) {
//        }
//    }
//
//    public static void a(boolean var0) {
//        try {
//            i.clear();
//            Binary2.a(e);
//            short[] var1 = new short[]{1, 2, 5, 14, 17, 20, 34, 36, 38, 49, 55, 56, 59, 67, 68, 69, 70, 71, 72, 73, 75, 76, 77, 78, 81, 82, 84, 87, 88, 89, 90, 94, 98, 99, 305, 375, 376, 553, 554, 643, 644, 645, 646, 504, 505, 506, 647, 648, 660, 687, 705, 706, 751, 752, 1023, 1024, 1025, 1026, 460, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 620, 621, 622, 623, 624, 625, 626, 627, 628, 924, 925, 926, 927, 928, 929, 930, 931, 932, 979, 980, 981, 982, 983, 984};
//
//            for (int var2 = 0; var2 < 114; ++var2) {
//                i.add(var1[var2]);
//            }
//
//            for (short var9 = 461; var9 <= 503; ++var9) {
//                i.add(var9);
//            }
//
//            String var10 = "iconClient";
//
//            for (int var8 = 0; var8 < i.size(); ++var8) {
//                try {
//                    short var3 = (Short) i.get(var8);
//                    String var4;
//                    byte[] var5;
//                    if ((var5 = Binary.read(var4 = "client_icon_" + var3)) == null) {
//                        if (var0) {
//                            StringBuilder var10000 = (new StringBuilder()).append(DataCenter.gI().aU).append("img/zoom");
//                            DataCenter.gI();
//                            if ((var5 = Binary.b(var10000.append(DataCenter.a(var10) + 1).append("/iconClient/").append(var3).append(".png").toString())) != null) {
//                                Binary.write(var4, var5);
//                                e.put(var3, Binary.createImage(Utlis.a(var5)));
//                            } else {
//                                e.put(var3, a);
//                            }
//                        } else {
//                            e.put(var3, a);
//                        }
//                    } else {
//                        e.put(var3, Binary.createImage(var5));
//                    }
//                } catch (Exception var6) {
//                }
//            }
//
//            Binary2.a(b);
//        } catch (Exception var7) {
//        }
//
//        DataCenter.gI().bb = true;
//    }
//
//    public static void c() {
//        j.clear();
//        Binary2.a(g);
//        Binary2.a(d);
//    }
//
//    public static void b(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
//        a(var0, var1, 0, var3, var4, 20);
//        a(var0, var1, -360, var3 + var5 - 4, var4, 20);
//        Binary2.a(var0, var2, 0, var3 + 4, var4, var5 - 8, (int) var6);
//    }
//
//    public static void a(short var0) {
//        mImage var1;
//        if ((var1 = Binary.createImage("client_icon_" + var0)) != null) {
//            e.put(var0, var1);
//        }
//
//    }
//
//    public static void d() {
//        Binary2.a(b);
//        Binary2.a(e);
//        Binary2.a(d);
//        Binary2.a(g);
//        e();
//    }
//
//    public static void e() {
//        Binary2.a(c);
//        Binary2.a(f);
//    }
//
//    private static void a(int var0, String var1, Hashtable var2) {
//        mImage var3 = a;
//        var2.put(new Short((short) var0), var3);
//        if (!mConfig.gI().readBoolean(18) || !(DataCenter.gI().currentScreen instanceof GameSrc) || !var1.toLowerCase().contains("char") && !var1.toLowerCase().contains("tree")) {
//            LangLa_fb var4;
//            (var4 = new LangLa_fb()).g = var2;
//            var4.a = (short) var0;
//            var4.d = "icon" + var1.substring(0, 1).toUpperCase() + var1.substring(1, var1.length()) + "/" + var0 + ".png";
//            if (var1.toLowerCase().contains("tree")) {
//                var4.e = var1 + "_" + var0;
//            } else {
//                var4.e = var1 + "_icon_" + var0;
//            }
//
//            LangLa_cz.cW.add(var4);
//            LangLa_cz.S();
//        }
//
//    }
//
//    public static Vector f() {
//        Vector var0 = new Vector();
//
//        try {
//            Iterator var2 = f.keySet().iterator();
//
//            while (var2.hasNext()) {
//                Object var1 = var2.next();
//
//                try {
//                    mImage var5;
//                    if ((var5 = (mImage) f.get(var1)) != null) {
//                        var0.add(var5);
//                    }
//                } catch (Exception var3) {
//                }
//            }
//        } catch (Exception var4) {
//        }
//
//        return var0;
//    }
}
