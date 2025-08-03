package org.jdesktop.animation.timing.interpolation;

public final class LinearInterpolator implements Interpolator {
   public  static LinearInterpolator instance = null;

   public  LinearInterpolator() {
   }

   public static LinearInterpolator getInstance() {
      if (instance == null) {
         instance = new LinearInterpolator();
      }

      return instance;
   }

   public float interpolate(float fraction) {
      return fraction;
   }
}
