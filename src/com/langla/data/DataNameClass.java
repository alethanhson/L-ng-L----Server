package com.langla.data;
public class DataNameClass {
   public byte id;
   public String name;

    @Override
    public String toString() {
        return "DataNameClass{" + "id=" + id + ", name=" + name + '}';
    }

   public DataNameClass(int var1) {
      this.id = (byte)var1;
   }
}
