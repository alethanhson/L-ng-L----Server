package com.langla.data;

import com.langla.lib.Utlis;
import com.langla.real.map.Mob;
import com.langla.real.npc.Npc;
import com.langla.real.player.XYEntity;
import com.langla.server.lib.Reader;

import java.util.ArrayList;
import java.util.Vector;

import com.langla.utlis.UTPKoolVN;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapTemplate {

    public short id;
    public String name;
    public short typeBlockMap;
    public byte type;

    public DataBlockMap[] f;
    public LangLa_gf[] g;

    public XYBlockMap h;
    public Vector d = new Vector();
    public boolean notBlock;

    public byte[] arrMap;

    public MapTemplate(int var1) {
        this.id = (short) var1;
    }

    @Override
    public String toString() {
        return "MapTemplate{" + "id=" + id + ", name=" + name + ", typeBlockMap=" + typeBlockMap + ", type=" + type + '}';
    }

    public boolean isMapLang() {
        return this.type == 2;
    }

    public boolean b() {
        return this.type == 3;

    }
    public Vector c = new Vector();
    public Vector i = new Vector();
    public Vector j = new Vector();
    public Vector k = new Vector();
    public Vector l = new Vector();

    public ArrayList<Npc> listNpc = new ArrayList<Npc>();
    public ArrayList<Mob> listMob = new ArrayList<Mob>();

    private static BlockMap crateBlockMap(int var0, int var1, int var2, int var3) {
        BlockMap var4;
        (var4 = new BlockMap()).arrayXY = new XYEntity[5];
        var4.arrayXY[0] = XYEntity.create(var0, var1);
        var4.arrayXY[1] = XYEntity.create(var2, var1);
        var4.arrayXY[2] = XYEntity.create(var2, var3);
        var4.arrayXY[3] = XYEntity.create(var0, var3);
        var4.arrayXY[4] = XYEntity.create(var0, var1);
        return var4;
    }

    public short maxX;
    public short maxY;

    public void createBlock(Reader var27) throws Exception {
        this.c.clear();
        this.readBlockMap(var27);
        new Vector();
        this.maxX = var27.dis.readShort();
        this.maxY = var27.dis.readShort();
        BlockMap[] var3;
        (var3 = new BlockMap[var27.readUnsignedByte() + 6])[var3.length - 1] = crateBlockMap(-50, -50, 128, 70);
        var3[var3.length - 2] = crateBlockMap(this.maxX - 100, -50, this.maxX + 50, 110);
        var3[var3.length - 3] = crateBlockMap(-50, -50, this.maxX + 50, 15);
        var3[var3.length - 4] = crateBlockMap(-50, -50, 15, this.maxY + 50);
        var3[var3.length - 5] = crateBlockMap(-50, this.maxY - 15, this.maxX + 50, this.maxY + 50);
        var3[var3.length - 6] = crateBlockMap(this.maxX - 15, -50, this.maxX + 50, this.maxY + 50);

        int var4;
        int var5;
        for (var4 = 0; var4 < var3.length - 6; ++var4) {
            var3[var4] = new BlockMap();
            var3[var4].arrayXY = new XYEntity[var27.readUnsignedByte()];

            for (var5 = 0; var5 < var3[var4].arrayXY.length; ++var5) {
                var3[var4].arrayXY[var5] = new XYEntity();
                var3[var4].arrayXY[var5].setXY(var27.dis.readShort(), var27.dis.readShort());
                if (var5 == 0) {
                    var3[var4].minX = var3[var4].maxX = var3[var4].arrayXY[var5].cx;
                    var3[var4].minY = var3[var4].maxY = var3[var4].arrayXY[var5].cy;
                } else {
                    if (var3[var4].arrayXY[var5].cx < var3[var4].minX) {
                        var3[var4].minX = var3[var4].arrayXY[var5].cx;
                    }

                    if (var3[var4].arrayXY[var5].cx > var3[var4].maxX) {
                        var3[var4].maxX = var3[var4].arrayXY[var5].cx;
                    }

                    if (var3[var4].arrayXY[var5].cy < var3[var4].minY) {
                        var3[var4].minY = var3[var4].arrayXY[var5].cy;
                    }

                    if (var3[var4].arrayXY[var5].cy > var3[var4].maxY) {
                        var3[var4].maxY = var3[var4].arrayXY[var5].cy;
                    }
                }
            }
        }

        this.h = new XYBlockMap(var3, this);
        LangLa_gr[] var32 = new LangLa_gr[var27.dis.readShort()];
        Vector var34 = new Vector();
        Vector var30 = new Vector();

        int var7;
        for (int var6 = 0; var6 < var32.length; ++var6) {
            var32[var6] = new LangLa_gr();
            var32[var6].a = var27.dis.readBoolean();
            if (var32[var6].a) {
                var30.add(var32[var6]);
            } else {
                var34.add(var32[var6]);
            }

            var32[var6].arrayXY = new XYEntity[var27.dis.readShort()];

            for (var7 = 0; var7 < var32[var6].arrayXY.length; ++var7) {
                var32[var6].arrayXY[var7] = new XYEntity();
                var32[var6].arrayXY[var7].setXY(var27.dis.readShort(), var27.dis.readShort());
            }
        }

        Vector var37 = new Vector();
        var32 = new LangLa_gr[var34.size()];

        int var8;
        int var9;
        for (var7 = 0; var7 < var34.size(); ++var7) {
            var32[var7] = (LangLa_gr) var34.get(var7);

            for (var8 = 0; var8 < var32[var7].arrayXY.length; ++var8) {
                if (var37.size() == 0) {
                    var37.add(var32[var7].arrayXY[var8]);
                } else {
                    for (var9 = 0; var9 < var37.size(); ++var9) {
                        if (Utlis.getRange((XYEntity) var37.get(var9), var32[var7].arrayXY[var8]) < 10) {
                            var32[var7].arrayXY[var8] = (XYEntity) var37.get(var9);
                        }
                    }

                    var37.add(var32[var7].arrayXY[var8]);
                }
            }
        }

        this.a(var32);

        LangLa_gr var40;
        for (var7 = 0; var7 < var30.size(); ++var7) {
            var40 = (LangLa_gr) var30.get(var7);

            for (var9 = 0; var9 < var40.arrayXY.length; ++var9) {
                XYEntity var10 = var40.arrayXY[var9];
                if (h.a(var10.cx, var10.cy)) {
                    XYEntity var33 = h.b(var10.cx, var10.cy);
                    var40.arrayXY[var9].cx = var33.cx;
                    var40.arrayXY[var9].cy = var33.cy;
                }
            }
        }

        this.j.clear();

        for (var7 = 0; var7 < var30.size(); ++var7) {
            var40 = (LangLa_gr) var30.get(var7);

            for (var9 = 0; var9 < var40.arrayXY.length - 1; ++var9) {
                BlockMap var45;
                (var45 = new BlockMap()).arrayXY = new XYEntity[2];
                var45.arrayXY[0] = var40.arrayXY[var9];
                var45.arrayXY[1] = var40.arrayXY[var9 + 1];
                this.j.add(var45);
            }
        }

        short var43 = var27.dis.readShort();

        for (var8 = 0; var8 < var43; ++var8) {
            var27.dis.readShort();
        }

        this.d.removeAllElements();
        var43 = var27.dis.readShort();
        Short var44 = null;
        int var46 = 0;
        Vector var35 = new Vector();

        int var31;
        for (var31 = 0; var31 < var43; ++var31) {
            if (var46 == 0) {
                var44 = Short.valueOf(var27.dis.readShort());
                this.c.addElement(var44);
                var46 = var27.dis.readShort();
            }

            LangLa_lo var47 = null;
            (var47 = new LangLa_lo(var44, this)).cx = var47.l = var27.dis.readShort();
            var47.cy = var47.m = var27.dis.readShort();
            var47.j = var27.dis.readByte();
            if (var47.k == 303) {
                var35.add(var47.k);
            } else {
                this.d.addElement(var47);
            }

            if (var46 > 0) {
                --var46;
            }
        }
    }

    private void a(LangLa_gr[] var1) {
//        Vector var2 = new Vector();
//
//        int var3;
//        int var4;
//        XYEntity var5;
//        for (var3 = 0; var3 < var1.length; ++var3) {
//            for (var4 = 0; var4 < var1[var3].arrayXY.length - 1; ++var4) {
//                var5 = var1[var3].arrayXY[var4];
//                XYEntity var6 = var1[var3].arrayXY[var4 + 1];
//                XYEntity[] var7 = new XYEntity[]{var5, var6};
//                var2.add(var7);
//            }
//        }
//
//        this.i.clear();
//
//        for (var3 = 0; var3 < var2.size(); ++var3) {
//            XYEntity var12 = ((XYEntity[]) var2.get(var3))[0];
//            var5 = ((XYEntity[]) var2.get(var3))[1];
//            this.a(var12, var5, var2, this.i);
//        }
//
//        Vector var11 = new Vector();
//
//        BlockMap var13;
//        for (var4 = this.i.size() - 1; var4 >= 0; --var4) {
//            var13 = (BlockMap) this.i.get(var4);
//            Vector var14 = new Vector();
//
//            for (int var16 = 0; var16 < var13.arrayXY.length; ++var16) {
//                var14.add(var13.arrayXY[var16]);
//            }
//
//            Collections.sort(var14, XYEntity.da);
//            String var17 = "";
//
//            for (int var8 = 0; var8 < var14.size(); ++var8) {
//                XYEntity var10 = (XYEntity) var14.get(var8);
//                var17 = var17 + var10.cx + "," + var10.cy + ";";
//            }
//
//            if (!var11.contains(var17)) {
//                var11.add(var17);
//            } else {
//                this.i.remove(var4);
//            }
//        }
//
//        this.k.clear();
//
//        for (var4 = this.i.size() - 1; var4 >= 0; --var4) {
//            var13 = (BlockMap) this.i.get(var4);
//
//            for (int var15 = 0; var15 < var13.arrayXY.length - 1; ++var15) {
//                XYEntity var18 = var13.arrayXY[var15];
//                XYEntity var9 = var13.arrayXY[var15 + 1];
//                this.k.add(new XYEntity[]{var18, var9});
//            }
//        }

    }

    private void readBlockMap(Reader var1) throws java.io.IOException {
        this.f = new DataBlockMap[var1.dis.readUnsignedShort()];
        Vector var4 = new Vector();

        short var2;
        short var3;
        BlockMap blockMap;
        int var7;
        int var8;
        int var9;
        BlockMap[] var11;
        for (short var6 = 0; var6 < this.f.length; ++var6) {
            var4.removeAllElements();
            this.f[var6] = new DataBlockMap();
            this.f[var6].width = var1.dis.readShort();
            this.f[var6].height = var1.dis.readShort();
            this.f[var6].type = var1.dis.readByte();
            this.f[var6].idEff = var1.dis.readShort();
            var2 = var1.readUnsignedByte();

            for (var7 = 0; var7 < var2; ++var7) {
                var3 = var1.readUnsignedByte();

                for (var8 = 0; var8 < var3; ++var8) {
                    (blockMap = new BlockMap()).arrayXY = new XYEntity[var1.dis.readByte() / 2];

                    for (var9 = 0; var9 < blockMap.arrayXY.length; ++var9) {
                        blockMap.arrayXY[var9] = new XYEntity();
                        blockMap.arrayXY[var9].setXY(var1.dis.readShort(), var1.dis.readShort());
                        if (var9 == 0) {
                            blockMap.minX = blockMap.maxX = blockMap.arrayXY[var9].cx;
                            blockMap.minY = blockMap.maxY = blockMap.arrayXY[var9].cy;
                        } else {
                            if (blockMap.arrayXY[var9].cx < blockMap.minX) {
                                blockMap.minX = blockMap.arrayXY[var9].cx;
                            }

                            if (blockMap.arrayXY[var9].cx > blockMap.maxX) {
                                blockMap.maxX = blockMap.arrayXY[var9].cx;
                            }

                            if (blockMap.arrayXY[var9].cy < blockMap.minY) {
                                blockMap.minY = blockMap.arrayXY[var9].cy;
                            }

                            if (blockMap.arrayXY[var9].cy > blockMap.maxY) {
                                blockMap.maxY = blockMap.arrayXY[var9].cy;
                            }
                        }
                    }

                    var4.addElement(blockMap);
                }
            }

            if (var4.size() > 0) {
                var11 = new BlockMap[var4.size()];

                for (var7 = 0; var7 < var4.size(); ++var7) {
                    var11[var7] = (BlockMap) var4.elementAt(var7);
                }

                this.f[var6].XYBlockMap = new XYBlockMap(var11, this);
            }
        }

        this.g = new LangLa_gf[var1.dis.readUnsignedShort()];

        for (int var12 = 0; var12 < this.g.length; ++var12) {
            var4.removeAllElements();
            this.g[var12] = new LangLa_gf();
            this.g[var12].a = var1.dis.readShort();
            this.g[var12].b = var1.dis.readShort();
            this.g[var12].c = new LangLa_lo[var1.readUnsignedByte()];

            for (var7 = 0; var7 < this.g[var12].c.length; ++var7) {
                this.g[var12].c[var7] = new LangLa_lo(var1.dis.readShort(), this);
                this.g[var12].c[var7].cx = this.g[var12].c[var7].l = var1.dis.readShort();
                this.g[var12].c[var7].cy = this.g[var12].c[var7].m = var1.dis.readShort();
                this.g[var12].c[var7].j = var1.dis.readByte();
                if (this.g[var12].c[var7].o().XYBlockMap != null) {
                    for (var8 = 0; var8 < this.g[var12].c[var7].o().XYBlockMap.a.length; ++var8) {
                        blockMap = this.g[var12].c[var7].o().XYBlockMap.a[var8].a();
                        if (this.g[var12].c[var7].j == 7) {
                            blockMap.arrayXY = Utlis.a(blockMap.arrayXY);
                        }

                        for (var9 = 0; var9 < blockMap.arrayXY.length; ++var9) {
                            XYEntity var10000 = blockMap.arrayXY[var9];
                            var10000.cx += this.g[var12].c[var7].cx;
                            var10000 = blockMap.arrayXY[var9];
                            var10000.cy += this.g[var12].c[var7].cy;
                            if (var9 == 0) {
                                blockMap.minX = blockMap.maxX = blockMap.arrayXY[var9].cx;
                                blockMap.minY = blockMap.maxY = blockMap.arrayXY[var9].cy;
                            } else {
                                if (blockMap.arrayXY[var9].cx < blockMap.minX) {
                                    blockMap.minX = blockMap.arrayXY[var9].cx;
                                }

                                if (blockMap.arrayXY[var9].cx > blockMap.maxX) {
                                    blockMap.maxX = blockMap.arrayXY[var9].cx;
                                }

                                if (blockMap.arrayXY[var9].cy < blockMap.minY) {
                                    blockMap.minY = blockMap.arrayXY[var9].cy;
                                }

                                if (blockMap.arrayXY[var9].cy > blockMap.maxY) {
                                    blockMap.maxY = blockMap.arrayXY[var9].cy;
                                }
                            }
                        }

                        var4.addElement(blockMap);
                    }
                }
            }

            var2 = var1.readUnsignedByte();

            for (var7 = 0; var7 < var2; ++var7) {
                var3 = var1.readUnsignedByte();

                for (var8 = 0; var8 < var3; ++var8) {
                    (blockMap = new BlockMap()).arrayXY = new XYEntity[var1.dis.readByte() / 2];

                    for (var9 = 0; var9 < blockMap.arrayXY.length; ++var9) {
                        blockMap.arrayXY[var9] = new XYEntity();
                        blockMap.arrayXY[var9].setXY(var1.dis.readShort(), var1.dis.readShort());
                        if (var9 == 0) {
                            blockMap.minX = blockMap.maxX = blockMap.arrayXY[var9].cx;
                            blockMap.minY = blockMap.maxY = blockMap.arrayXY[var9].cy;
                        } else {
                            if (blockMap.arrayXY[var9].cx < blockMap.minX) {
                                blockMap.minX = blockMap.arrayXY[var9].cx;
                            }

                            if (blockMap.arrayXY[var9].cx > blockMap.maxX) {
                                blockMap.maxX = blockMap.arrayXY[var9].cx;
                            }

                            if (blockMap.arrayXY[var9].cy < blockMap.minY) {
                                blockMap.minY = blockMap.arrayXY[var9].cy;
                            }

                            if (blockMap.arrayXY[var9].cy > blockMap.maxY) {
                                blockMap.maxY = blockMap.arrayXY[var9].cy;
                            }
                        }
                    }

                    var4.addElement(blockMap);
                }
            }

            if (var4.size() > 0) {
                var11 = new BlockMap[var4.size()];

                for (var7 = 0; var7 < var4.size(); ++var7) {
                    var11[var7] = (BlockMap) var4.elementAt(var7);
                }

                this.g[var12].d = new XYBlockMap(var11, this);
            }
        }

    }

    void loadNpc(JSONArray jsonArray) {
        listNpc.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                Npc npc = new Npc();
                npc.id = json.getInt("id");
                npc.status = (byte) json.getInt("status");
                npc.setXY(json);
                listNpc.add(npc);
            } catch (JSONException ex) {
            }
        }
    }

    void loadMob(JSONArray jsonArray) {
        listMob.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                Mob mob = new Mob();
                mob.id = json.getInt("id");
                mob.exp = mob.expGoc = json.getInt("exp");
                mob.hp = mob.hpGoc = mob.hpFull = json.getInt("hp");
                mob.level = json.getInt("level");
                mob.levelBoss = 0;
                mob.paintMiniMap = json.getBoolean("paintMiniMap");
                mob.status = 0;
                mob.setXY(json);
//                if(mob.id == 121){
//                    UTPKoolVN.Debug("Okk: "+mob.id);
//                }
                listMob.add(mob);
            } catch (JSONException ex) {

            }
        }
    }

}
/*
MapTemplate{id=0, name=, typeBlockMap=0, type=0}
MapTemplate{id=1, name=, typeBlockMap=0, type=0}
MapTemplate{id=2, name=Cổng số 1, typeBlockMap=0, type=5}
MapTemplate{id=3, name=, typeBlockMap=0, type=0}
MapTemplate{id=4, name=Cổng số 1, typeBlockMap=0, type=5}
MapTemplate{id=5, name=, typeBlockMap=0, type=0}
MapTemplate{id=6, name=Địa cung (sơ cấp), typeBlockMap=0, type=4}
MapTemplate{id=7, name=Địa cung (trung cấp), typeBlockMap=0, type=4}
MapTemplate{id=8, name=, typeBlockMap=0, type=0}
MapTemplate{id=9, name=, typeBlockMap=0, type=0}
MapTemplate{id=10, name=, typeBlockMap=0, type=0}
MapTemplate{id=11, name=, typeBlockMap=0, type=0}
MapTemplate{id=12, name=, typeBlockMap=0, type=0}
MapTemplate{id=13, name=, typeBlockMap=0, type=0}
MapTemplate{id=14, name=, typeBlockMap=0, type=0}
MapTemplate{id=15, name=, typeBlockMap=0, type=0}
MapTemplate{id=16, name=, typeBlockMap=0, type=0}
MapTemplate{id=17, name=Cổng số 1, typeBlockMap=0, type=5}
MapTemplate{id=18, name=Địa cung (cao cấp), typeBlockMap=0, type=4}
MapTemplate{id=19, name=Địa cung (thượng cấp), typeBlockMap=0, type=4}
MapTemplate{id=20, name=, typeBlockMap=0, type=0}
MapTemplate{id=21, name=, typeBlockMap=0, type=0}
MapTemplate{id=22, name=Cổng số 2, typeBlockMap=0, type=5}
MapTemplate{id=23, name=, typeBlockMap=0, type=0}
MapTemplate{id=24, name=            , typeBlockMap=0, type=0}
MapTemplate{id=25, name=, typeBlockMap=0, type=0}
MapTemplate{id=26, name=Cổng số 2, typeBlockMap=0, type=5}
MapTemplate{id=27, name=, typeBlockMap=0, type=0}
MapTemplate{id=28, name=, typeBlockMap=0, type=0}
MapTemplate{id=29, name=, typeBlockMap=0, type=0}
MapTemplate{id=30, name=, typeBlockMap=0, type=0}
MapTemplate{id=31, name=Cổng số 2, typeBlockMap=0, type=5}
MapTemplate{id=32, name=, typeBlockMap=0, type=0}
MapTemplate{id=33, name=, typeBlockMap=0, type=0}
MapTemplate{id=34, name=, typeBlockMap=0, type=0}
MapTemplate{id=35, name=, typeBlockMap=0, type=0}
MapTemplate{id=36, name=, typeBlockMap=0, type=0}
MapTemplate{id=37, name=Cổng số 3, typeBlockMap=0, type=5}
MapTemplate{id=38, name=, typeBlockMap=0, type=0}
MapTemplate{id=39, name=Cổng số 3, typeBlockMap=0, type=5}
MapTemplate{id=40, name=Cổng số 3, typeBlockMap=0, type=5}
MapTemplate{id=41, name=Chiến Trường Nhẫn Giả, typeBlockMap=0, type=6}
MapTemplate{id=42, name=Bản Doanh Làng Đá, typeBlockMap=0, type=6}
MapTemplate{id=43, name=Bản Doanh Làng Lá, typeBlockMap=0, type=6}
MapTemplate{id=44, name=Phòng đặt cược, typeBlockMap=0, type=7}
MapTemplate{id=45, name=Lôi đài, typeBlockMap=0, type=7}
MapTemplate{id=46, name=Cửa số 1, typeBlockMap=0, type=8}
MapTemplate{id=47, name=Cửa số 2, typeBlockMap=0, type=8}
MapTemplate{id=48, name=Du long cảnh, typeBlockMap=0, type=0}
MapTemplate{id=49, name=Đại hội Nhẫn Giả, typeBlockMap=0, type=10}
MapTemplate{id=50, name=Quân Danh, typeBlockMap=0, type=0}
MapTemplate{id=51, name=, typeBlockMap=0, type=0}
MapTemplate{id=52, name=, typeBlockMap=0, type=0}
MapTemplate{id=53, name=    , typeBlockMap=0, type=0}
MapTemplate{id=54, name=, typeBlockMap=0, type=0}
MapTemplate{id=55, name=, typeBlockMap=0, type=0}
MapTemplate{id=56, name=Đồi trung tâm, typeBlockMap=0, type=0}
MapTemplate{id=57, name=Thánh Địa Thất Kiếm, typeBlockMap=0, type=0}
MapTemplate{id=58, name=Hang Vĩ Thú, typeBlockMap=0, type=16}
MapTemplate{id=59, name=Làng Cát, typeBlockMap=1, type=2}
MapTemplate{id=60, name=Làng Sương Mù, typeBlockMap=0, type=2}
MapTemplate{id=61, name=Vách Chigiri, typeBlockMap=0, type=0}
MapTemplate{id=62, name=Núi Kirigakure, typeBlockMap=0, type=0}
MapTemplate{id=63, name=Cánh Đồng Kaminari, typeBlockMap=0, type=0}
MapTemplate{id=64, name=Thung Lũng Chết, typeBlockMap=0, type=0}
MapTemplate{id=65, name=Đồi Hoang, typeBlockMap=0, type=0}
MapTemplate{id=66, name=Hẻm Núi Mizu, typeBlockMap=0, type=0}
MapTemplate{id=67, name=Hầm bí mật, typeBlockMap=0, type=14}
MapTemplate{id=68, name=Làng Cỏ, typeBlockMap=0, type=2}
MapTemplate{id=69, name=Làng Mây, typeBlockMap=0, type=2}
MapTemplate{id=70, name=Vách Đá Ngang, typeBlockMap=0, type=0}
MapTemplate{id=71, name=Miếu Iwagakure, typeBlockMap=0, type=0}
MapTemplate{id=72, name=Chân Núi Tsuchi, typeBlockMap=0, type=0}
MapTemplate{id=73, name=Rừng Nấm, typeBlockMap=0, type=0}
MapTemplate{id=74, name=Dòng Sông Kusagakure, typeBlockMap=0, type=0}
MapTemplate{id=75, name=Làng Lá, typeBlockMap=2, type=0}
MapTemplate{id=76, name=Đồng Cỏ Tenchi, typeBlockMap=0, type=0}
MapTemplate{id=77, name=Rừng Kumogakure, typeBlockMap=0, type=0}
MapTemplate{id=78, name=Nghĩa Địa Bỏ Hoang, typeBlockMap=0, type=0}
MapTemplate{id=79, name=Chiến Trường Cổ, typeBlockMap=0, type=0}
MapTemplate{id=80, name=Đồi Cát, typeBlockMap=0, type=0}
MapTemplate{id=81, name=Sa mạc Sunagakure, typeBlockMap=0, type=0}
MapTemplate{id=82, name=Núi Hokage, typeBlockMap=0, type=0}
MapTemplate{id=83, name=Thung lũng Tận Cùng, typeBlockMap=0, type=0}
MapTemplate{id=84, name=Khu luyện tập, typeBlockMap=0, type=13}
MapTemplate{id=85, name=Làng Đá, typeBlockMap=3, type=2}
MapTemplate{id=86, name=Trường Konoha, typeBlockMap=0, type=0}
MapTemplate{id=87, name=Hang Khỉ, typeBlockMap=0, type=0}
MapTemplate{id=88, name=Cầu Kannabi, typeBlockMap=0, type=0}
MapTemplate{id=89, name=Vòng Lặp Ảo Tưởng, typeBlockMap=0, type=15}
MapTemplate{id=90, name=Hang Vĩ Thú, typeBlockMap=0, type=16}
MapTemplate{id=91, name=Hang Vĩ Thú, typeBlockMap=0, type=16}
MapTemplate{id=92, name=Hang Vĩ Thú, typeBlockMap=0, type=16}
MapTemplate{id=93, name=Hang Gamaken, typeBlockMap=0, type=17}
MapTemplate{id=94, name=Hang Gamatatsu, typeBlockMap=0, type=17}
MapTemplate{id=95, name=Hang Gama Armored, typeBlockMap=0, type=17}
MapTemplate{id=96, name=Hang Gamabunta, typeBlockMap=0, type=17}
MapTemplate{id=97, name=Hang Gamahiro, typeBlockMap=0, type=17}
MapTemplate{id=98, name=Chiến trường, typeBlockMap=0, type=0}
MapTemplate{id=99, name=Cửa phía tây, typeBlockMap=0, type=0}
MapTemplate{id=100, name=Cửa phía đông, typeBlockMap=0, type=0}
MapTemplate{id=101, name=Chiến trường, typeBlockMap=0, type=18}
MapTemplate{id=102, name=Làng Mưa, typeBlockMap=6, type=0}
MapTemplate{id=103, name=Pháo đài Amega, typeBlockMap=6, type=0}
MapTemplate{id=104, name=Vùng trũng Kusa, typeBlockMap=6, type=0}
MapTemplate{id=105, name=Lãnh địa thiên thần, typeBlockMap=6, type=0}
MapTemplate{id=106, name=Căn cứ Akatsuki, typeBlockMap=6, type=0}

 */
