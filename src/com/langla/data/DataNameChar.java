package com.langla.data;
public class DataNameChar {
   public byte id;
   public byte b;
   public String name;
   public short d;

    @Override
    public String toString() {
        return "DataNameChar{" + "id=" + id + ", b=" + b + ", name=" + name + ", d=" + d + '}';
    }

   public DataNameChar(int var1) {
      this.id = (byte)var1;
   }
}
