package org.jdesktop.animation.timing.interpolation;

import java.util.ArrayList;

public class KeyTimes {
   public  ArrayList<Float> times = new ArrayList();

   public KeyTimes(float... times) {
      if (times[0] != 0.0F) {
         throw new IllegalArgumentException("First time value must be zero");
      } else if (times[times.length - 1] != 1.0F) {
         throw new IllegalArgumentException("Last time value must be one");
      } else {
         float prevTime = 0.0F;
         float[] arr$ = times;
         int len$ = times.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            float time = arr$[i$];
            if (time < prevTime) {
               throw new IllegalArgumentException("Time values must be in increasing order");
            }

            this.times.add(time);
            prevTime = time;
         }

      }
   }

   ArrayList getTimes() {
      return this.times;
   }

   int getSize() {
      return this.times.size();
   }

   int getInterval(float fraction) {
      int prevIndex = 0;

      for(int i = 1; i < this.times.size(); prevIndex = i++) {
         float time = (Float)this.times.get(i);
         if (time >= fraction) {
            return prevIndex;
         }
      }

      return prevIndex;
   }

   float getTime(int index) {
      return (Float)this.times.get(index);
   }
}
