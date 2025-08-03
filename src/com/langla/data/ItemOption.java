package com.langla.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.langla.lib.Utlis;

import com.tgame.model.Caption;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemOption {

    @Override
    public String toString() {
        return b();
    }
    @JsonIgnore
    public int[] a;

    public ItemOption(String var1) {
        String[] var3 = Utlis.split(var1, ",");
        this.a = new int[var3.length];

        for (int var2 = 0; var2 < var3.length; ++var2) {
            this.a[var2] = Integer.parseInt(var3[var2]);
        }

    }
    @JsonIgnore
    public ItemOption() {

    }
    @JsonIgnore
    public ItemOption(int... var1) {
        this.a = var1;
    }
    @JsonIgnore
    public ItemOptionTemplate getItemOptionTemplate() {
        return DataCenter.gI().ItemOptionTemplate[this.a[0]];
    }
    @JsonIgnore
    public String a(int var1) {
        if (this.getItemOptionTemplate().type == 54) {
            return Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, Utlis.l(this.a[1]));
        } else if (this.getItemOptionTemplate().type == 52) {
            String var2 = Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "" + this.a[1]);

            for (int var3 = 0; var3 < DataCenter.gI().SkillTemplate.length; ++var3) {
                if (DataCenter.gI().SkillTemplate[var3].idChar == var1 && DataCenter.gI().SkillTemplate[var3].levelNeed == this.getItemOptionTemplate().level) {
                    String var4 = DataCenter.gI().SkillTemplate[var3].name;
                    return var2.replaceAll("@", var4);
                }
            }

            return null;
        } else {
            return this.getItemOptionTemplate().id == 98 ? Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "" + (float) this.a[1] / 100.0F) : Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "" + this.a[1]);
        }
    }
    @JsonIgnore
    public String b() {
        if (this.a[0] == 340 && this.a[1] > 0) {
            return "("+this.a[0]+")"+Caption.qC + " " + DataCenter.gI().ItemTemplate[this.a[1]].name;
        } else if (this.a[1] >= 0 && this.f() >= 0 && this.a[1] != this.f()) {
            return"("+this.a[0]+")"+ Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "(" + Utlis.numberFormat(this.a[1]) + "-" + Utlis.numberFormat(this.f()) + ")");
        } else if (this.a[1] >= 0) {
            return "("+this.a[0]+")"+Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, Utlis.numberFormat(this.a[1]));
        } else {
            return "("+this.a[0]+")"+(this.f() >= 0 ? Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, Utlis.numberFormat(this.f())) : "");
        }
    }
    @JsonIgnore
    public String c() {
        return Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "" + this.a[1]).replaceAll("@", "" + this.f());
    }
    @JsonIgnore
    public String b(int var1) {
        var1 = var1;
        if (this.a.length > 3) {
            var1 += this.a[3];
        }

        if ((var1 = this.a[1] + var1 / 10) > this.f()) {
            var1 = this.f();
        }

        return Utlis.replaceAll(DataCenter.gI().ItemOptionTemplate[this.a[0]].name, "" + var1).replaceAll("@", "" + this.f());
    }
    @JsonIgnore
    public int d() {
        return this.a[0];
    }
    @JsonIgnore
    public int e() {
        return this.a[1];
    }
    @JsonIgnore
    public int c(int var1) {
        return this.a[1] = var1;
    }
    @JsonIgnore
    public int d(int var1) {
        int[] var10000 = this.a;
        return var10000[1] += var1;
    }
    @JsonIgnore
    public int e(int var1) {
        return this.a[2] = var1;
    }
    @JsonIgnore
    public int f() {
        return this.a.length < 3 ? -1 : this.a[2];
    }
    @JsonIgnore
    public String g() {
        String var1 = "";

        for (int var2 = 0; var2 < this.a.length; ++var2) {
            var1 = var1 + this.a[var2];
            if (var2 < this.a.length - 1) {
                var1 = var1 + ",";
            }
        }

        return var1;
    }


    public static int f(int var0) {
        switch (var0) {
            case 406:
                return 199;
            case 407:
                return 200;
            case 408:
                return 201;
            case 409:
                return 202;
            case 410:
                return 203;
            case 411:
                return 204;
            case 412:
                return 205;
            case 413:
                return 206;
            case 826:
                return 344;
            case 827:
                return 345;
            default:
                return -1;
        }
    }

    public int h() {
        switch (this.a[0]) {
            case 199:
                return 406;
            case 200:
                return 407;
            case 201:
                return 408;
            case 202:
                return 409;
            case 203:
                return 410;
            case 204:
                return 411;
            case 205:
                return 412;
            case 206:
                return 413;
            case 344:
                return 826;
            case 345:
                return 827;
            default:
                return -1;
        }
    }
    @JsonIgnore
    public int i() {
        try {
            return this.a[3];
        } catch (Exception var1) {
            return 0;
        }
    }

    public static ItemOption g(int var0) {
        return new ItemOption(f(var0) + ",0,-1,0");
    }

    public boolean j() {
        return this.a[0] == 159 || this.a[0] == 163 || this.a[0] == 164 || this.a[0] == 165;
    }

    public boolean k() {
        return this.a[0] == 361;
    }

    public boolean canCreateItemOption() {
        return this.a[1] >= 0 && this.f() >= 0 && this.a[1] != this.f() && this.a[0] != 305;
    }

}
