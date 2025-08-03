package com.langla.data;
import java.util.Vector;

public class LangLa_jt {
   public short[] a;

   public void a(Vector var1) {
      try {
         for(int var2 = this.a.length - 1; var2 >= 0; --var2) {
            short var10001 = this.a[var2];
            LangLa_jr var3;
            (var3 = DataCenter.gI().F[var10001].a()).c = 0;
            var1.addElement(var3);
         }

      } catch (Exception var4) {
      }
   }

   public void a(Vector var1, int var2) {
      try {
         for(int var3 = this.a.length - 1; var3 >= 0; --var3) {
            short var10001 = this.a[var3];
            LangLa_jr var4;
            (var4 = DataCenter.gI().F[var10001].a()).c = var2;
            var1.addElement(var4);
         }

      } catch (Exception var5) {
      }
   }
}
