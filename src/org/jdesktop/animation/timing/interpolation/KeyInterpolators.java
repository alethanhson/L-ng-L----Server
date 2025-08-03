package org.jdesktop.animation.timing.interpolation;

import java.util.ArrayList;

class KeyInterpolators {
   public  ArrayList<Interpolator> interpolators = new ArrayList();

   KeyInterpolators(int numIntervals, Interpolator... interpolators) {
      int i;
      if (interpolators != null && interpolators[0] != null) {
         if (interpolators.length < numIntervals) {
            for(i = 0; i < numIntervals; ++i) {
               this.interpolators.add(interpolators[0]);
            }
         } else {
            for(i = 0; i < numIntervals; ++i) {
               this.interpolators.add(interpolators[i]);
            }
         }
      } else {
         for(i = 0; i < numIntervals; ++i) {
            this.interpolators.add(LinearInterpolator.getInstance());
         }
      }

   }

   float interpolate(int interval, float fraction) {
      return ((Interpolator)this.interpolators.get(interval)).interpolate(fraction);
   }
}
