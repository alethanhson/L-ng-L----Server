package org.jdesktop.animation.timing.interpolation;

public class KeyFrames {
   public  KeyValues keyValues;
   public  KeyTimes keyTimes;
   public  KeyInterpolators interpolators;

   public KeyFrames(KeyValues keyValues) {
      this.init(keyValues, (KeyTimes)null, (Interpolator)null);
   }

   public KeyFrames(KeyValues keyValues, KeyTimes keyTimes) {
      this.init(keyValues, keyTimes, (Interpolator)null);
   }

   public KeyFrames(KeyValues keyValues, KeyTimes keyTimes, Interpolator... interpolators) {
      this.init(keyValues, keyTimes, interpolators);
   }

   public KeyFrames(KeyValues keyValues, Interpolator... interpolators) {
      this.init(keyValues, (KeyTimes)null, interpolators);
   }

   public  void init(KeyValues keyValues, KeyTimes keyTimes, Interpolator... interpolators) {
      int numFrames = keyValues.getSize();
      if (keyTimes == null) {
         float[] keyTimesArray = new float[numFrames];
         float timeVal = 0.0F;
         keyTimesArray[0] = timeVal;

         for(int i = 1; i < numFrames - 1; ++i) {
            timeVal += 1.0F / (float)(numFrames - 1);
            keyTimesArray[i] = timeVal;
         }

         keyTimesArray[numFrames - 1] = 1.0F;
         this.keyTimes = new KeyTimes(keyTimesArray);
      } else {
         this.keyTimes = keyTimes;
      }

      this.keyValues = keyValues;
      if (numFrames != this.keyTimes.getSize()) {
         throw new IllegalArgumentException("keyValues and keyTimes must be of equal size");
      } else if (interpolators != null && interpolators.length != numFrames - 1 && interpolators.length != 1) {
         throw new IllegalArgumentException("interpolators must be either null (implying interpolation for all intervals), a single interpolator (which will be used for all intervals), or a number of interpolators equal to one less than the number of times.");
      } else {
         this.interpolators = new KeyInterpolators(numFrames - 1, interpolators);
      }
   }

   Class getType() {
      return this.keyValues.getType();
   }

   KeyValues getKeyValues() {
      return this.keyValues;
   }

   KeyTimes getKeyTimes() {
      return this.keyTimes;
   }

   public int getInterval(float fraction) {
      return this.keyTimes.getInterval(fraction);
   }

   Object getValue(float fraction) {
      int interval = this.getInterval(fraction);
      float t0 = this.keyTimes.getTime(interval);
      float t1 = this.keyTimes.getTime(interval + 1);
      float t = (fraction - t0) / (t1 - t0);
      float interpolatedT = this.interpolators.interpolate(interval, t);
      if (interpolatedT < 0.0F) {
         interpolatedT = 0.0F;
      } else if (interpolatedT > 1.0F) {
         interpolatedT = 1.0F;
      }

      return this.keyValues.getValue(interval, interval + 1, interpolatedT);
   }
}
