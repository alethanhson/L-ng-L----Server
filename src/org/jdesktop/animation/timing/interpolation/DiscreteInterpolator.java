package org.jdesktop.animation.timing.interpolation;

public final class DiscreteInterpolator implements Interpolator {
   public  static DiscreteInterpolator instance = null;

   public  DiscreteInterpolator() {
   }

   public static DiscreteInterpolator getInstance() {
      if (instance == null) {
         instance = new DiscreteInterpolator();
      }

      return instance;
   }

   public float interpolate(float fraction) {
      return fraction < 1.0F ? 0.0F : 1.0F;
   }
}
