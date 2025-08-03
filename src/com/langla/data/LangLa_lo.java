package com.langla.data;

import com.langla.real.player.XYEntity;


public class LangLa_lo extends XYEntity {
   public byte j;
   public short k;
   public short l;
   public short m;
   public int p;

   public MapTemplate map;
   public DataBlockMap o() {
      try {
         return map.f == null ? null : map.f[this.k];
      } catch (Exception var1) {
         return null;
      }
   }

   public LangLa_lo(int var1,MapTemplate map) {
      this.k = (short)var1;
      this.map = map;
      this.p = this.o().type;
   }

  
}
