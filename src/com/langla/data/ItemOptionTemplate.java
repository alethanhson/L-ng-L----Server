package com.langla.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemOptionTemplate {
    @JsonIgnore
    public short id;
    @JsonIgnore
    public String name;
    @JsonIgnore
    public byte type;
    @JsonIgnore
    public byte level;
    @JsonIgnore
    public String strOption;
    @JsonIgnore
    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", type=" + type + ", level=" + level + ", strOption=" + strOption;
    }
    @JsonIgnore
    public ItemOptionTemplate(int var1) {
        this.id = (short) var1;
    }
    @JsonIgnore
    public int[] a() {
        if (this.strOption.length() == 0) {
            return new int[0];
        } else {
            String[] var1;
            int var2 = (var1 = this.strOption.split(";")).length;
            int var3 = 17;
            if (this.id == 207 || this.id == 208) {
                var3 = 18;
            }

            if (this.type == 8) {
                var3 = var2;
            }

            if (var2 > var3) {
                var2 = var3;
            }

            int[] var4 = new int[var2];

            for (var3 = 0; var3 < var4.length; ++var3) {
                var4[var3] = Integer.parseInt(var1[var3]);
            }

            return var4;
        }
    }
}
