package com.langla.data;
import com.langla.lib.Utlis;
import com.langla.real.player.XYEntity;

import java.util.Vector;

public class BlockMap {
   public XYEntity[] arrayXY;
   public short minX;
   public short minY;
   public short maxX;
   public short maxY;

   public BlockMap a() {
      BlockMap var1;
      (var1 = new BlockMap()).minX = this.minX;
      var1.minY = this.minY;
      var1.maxX = this.maxX;
      var1.maxY = this.maxY;
      var1.arrayXY = new XYEntity[this.arrayXY.length];

      for(int var2 = 0; var2 < this.arrayXY.length; ++var2) {
         var1.arrayXY[var2] = new XYEntity();
         var1.arrayXY[var2].setXY(this.arrayXY[var2].cx, this.arrayXY[var2].cy);
      }

      return var1;
   }

   public Vector a(XYEntity var1, XYEntity var2) {
      Vector var4 = new Vector();

      for(int var5 = 0; var5 < this.arrayXY.length - 1; ++var5) {
         XYEntity var3;
         if ((var3 = Utlis.a(var1, var2, this.arrayXY[var5], this.arrayXY[var5 + 1])) != null) {
            var4.add(var3);
         }
      }

      return var4;
   }

   public XYEntity b(XYEntity var1, XYEntity var2) {
      for(int var4 = 0; var4 < this.arrayXY.length - 1; ++var4) {
         XYEntity var3;
         if ((var3 = Utlis.a(var1, var2, this.arrayXY[var4], this.arrayXY[var4 + 1])) != null) {
            return var3;
         }
      }

      return null;
   }

   public boolean a(int var1, int var2) {
      return a(var1, var2, this.arrayXY);
   }

   private static boolean a(int var0, int var1, XYEntity[] var2) {
      for(int var5 = 0; var5 < var2.length - 1; ++var5) {
         try {
            int[] var3 = new int[]{var2[var5].cx, var2[var5].cy};
            int[] var4 = new int[]{var2[var5 + 1].cx, var2[var5 + 1].cy};
            var3 = new int[]{var4[0] - var3[0], var4[1] - var3[1]};
            var4 = new int[]{var4[0] - var0, var4[1] - var1};
            if (var3[0] * var4[1] - var3[1] * var4[0] > 0) {
               return false;
            }
         } catch (Exception var6) {
            return false;
         }
      }

      return true;
   }

   
   public Object clone() {
      return this.a();
   }
}
