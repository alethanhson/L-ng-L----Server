package com.langla.data;
public class MobTemplate {
   public byte[][] a;
   public LangLa_hz[] b;
   public short id;
   public short width;
   public short height;
   public short indexData;
   public short g;
   public short timeThuHoach;
   public String name;
   public String detail;
   public int[] k;
   public int[] l;
   public short speedMove;
   public byte type;
   public byte speedMoveByte;
   public byte p;
  public  String utf;
  public  String utf2;

    @Override
    public String toString() {
        return "MobTemplate{" + "a=" + a + ", b=" + b + ", id=" + id + ", width=" + width + ", height=" + height + ", indexData=" + indexData + ", g=" + g + ", timeThuHoach=" + timeThuHoach + ", name=" + name + ", detail=" + detail + ", k=" + k + ", l=" + l + ", speedMove=" + speedMove + ", type=" + type + ", speedMoveByte=" + speedMoveByte + ", p=" + p + ", utf=" + utf + ", utf2=" + utf2 + '}';
    }

   public MobTemplate(int var1) {
      this.id = (short)var1;
   }
}
