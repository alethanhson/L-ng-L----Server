/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.langla.real.map;

import com.langla.data.DataCenter;
import com.langla.real.player.Entity;

/**
 * @author PKoolVN
 **/
public class WayPoint extends Entity {

    public short mapHere;
    public short mapNext;
    public short l;
    public short m;
    public short n;
    public short o;
    private String[] r = null;
    public int p = 0;
    public int q = 0;
    boolean isNext;

    public WayPoint(int i, int i0) {
        this.setXY(0, 0);
    }

    @Override
    public String toString() {
        return "WayPoint{" + "mapHere=" + DataCenter.gI().MapTemplate[mapHere].name + ", mapNext=" + DataCenter.gI().MapTemplate[mapNext].name + '}';
    }

    public void create(short var1, short var2, short var3, short var4, short var5, short var6, short var7, short var8) {
        this.mapHere = var1;
        this.mapNext = var2;
        this.l = (short) (var3 - 5);
        this.m = (short) (var4 - 5);
        this.n = (short) (var5 + 5);
        this.o = (short) (var6 + 5);
        this.setXY(var3 + (var5 - var3) / 2, var6);
        this.r = new String[]{""};
        this.p = var7;
        this.q = var8;
    }

}
