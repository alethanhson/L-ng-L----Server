package com.langla.data;

public class Step {

    public int id;
    public String name;
    public int idItem;
    public int idNpc;
    public int idMob;
    public int idMap;
    public int x;
    public int y;
    public int require;
    public String STR;
    public String STR_ITEM;

    public boolean a() {
        return this.id == 1 || this.id == 29 || this.id == 2 || this.id == 12 || this.id == 10 || this.id == 31;
    }

    public int b() {
        try {
            if (this.id == 12 || this.id == 10) {
                return Integer.parseInt(this.STR_ITEM);
            }
        } catch (Exception var2) {
            //Utlis.println(var2);
        }

        return this.idItem;
    }

    public int c() {
        return this.idItem;
    }
}