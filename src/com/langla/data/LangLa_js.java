package com.langla.data;
public class LangLa_js {
   public short[][] a = new short[3][];

   public static short a(int var0, byte var1, byte var2, byte var3, int var4, int var5) {
      int var6;
      if (var2 == 0) {
         var6 = var3;
      } else if (var1 == 1) {
         var6 = var2 - 1;
      } else {
         var6 = var2 + 4;
      }

      if (var5 > 0) {
         var6 = var5;
      }

      var6 = var6;
      if (var4 >= 18) {
         var6 += 60;
      } else if (var4 >= 17) {
         var6 += 50;
      } else if (var4 >= 16) {
         var6 += 40;
      } else if (var4 >= 14) {
         var6 += 30;
      } else if (var4 >= 12) {
         var6 += 20;
      } else if (var4 >= 8) {
         var6 += 10;
      }

      if (DataCenter.gI().H[var0].a[0].length <= var6) {
         var6 = DataCenter.gI().H[var0].a[0].length - 1;
      }

      return DataCenter.gI().H[var0].a[0][var6];
   }
    String utf;
    String utf2;
    String utf3;
}
