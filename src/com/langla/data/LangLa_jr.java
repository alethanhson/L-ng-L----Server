package com.langla.data;
public class LangLa_jr {
   public int a;
   public int b;
   public int c = 0;
   public LangLa_ju[] d;

   public LangLa_jr a() {
      LangLa_jr var1;
      (var1 = new LangLa_jr()).a = this.a;
      var1.b = this.b;
      var1.c = this.c;
      var1.d = this.d;
      return var1;
   }

   public boolean b() {
      return this.b == 3;
   }

   public boolean c() {
      return this.b == 4;
   }

   public boolean d() {
      return this.b == 2;
   }

   public boolean e() {
      return this.b == 1;
   }

   
   public Object clone() {
      return this.a();
   }
}
