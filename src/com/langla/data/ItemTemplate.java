package com.langla.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemTemplate {
    @JsonIgnore
    public short id;
    @JsonIgnore
    public String name = "";
    @JsonIgnore
    public String detail = "";
    @JsonIgnore
    public boolean isXepChong;
    @JsonIgnore
    public byte gioiTinh;
    @JsonIgnore
    public byte type;
    @JsonIgnore
    public byte idClass;
    @JsonIgnore
    public short idIcon;
    @JsonIgnore
    public short levelNeed;
    @JsonIgnore
    public int taiPhuNeed;
    @JsonIgnore
    public short idMob;
    @JsonIgnore
    public short idChar;

    @JsonIgnore
    @Override
    public String toString() {
        return "ItemTemplate{" + "id=" + id + ", name=" + name + ", detail=" + detail + ", isXepChong=" + isXepChong + ", gioiTinh=" + gioiTinh + ", type=" + type + ", idClass=" + idClass + ", idIcon=" + idIcon + ", levelNeed=" + levelNeed + ", taiPhuNeed=" + taiPhuNeed + ", idMob=" + idMob + ", idChar=" + idChar + '}';
    }
    @JsonIgnore
    public ItemTemplate(int var1) {
        this.id = (short) var1;
    }
}
