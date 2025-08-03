package com.langla.data;
public class EffectTemplate {
   public byte id;
   public String name;
   public String detail;
   public short type;
   public short idIcon;
   public short idMob;

    @Override
    public String toString() {
        return "EffectTemplate{" + "id=" + id + "<br>name=" + name + "<br>detail=" + detail + "<br>type=" + type + "<br>idIcon=" + idIcon + "<br>idMob=" + idMob + '}';
    }

   public EffectTemplate(int var1) {
      this.id = (byte)var1;
   }
}
