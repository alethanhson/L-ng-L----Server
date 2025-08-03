package org.jdesktop.animation.timing.interpolation;

class LengthItem {
   float length;
   float t;
   float fraction;

   LengthItem(float length, float t, float fraction) {
      this.length = length;
      this.t = t;
      this.fraction = fraction;
   }

   LengthItem(float length, float t) {
      this.length = length;
      this.t = t;
   }

   public float getLength() {
      return this.length;
   }

   public float getT() {
      return this.t;
   }

   public float getFraction() {
      return this.fraction;
   }

   void setFraction(float totalLength) {
      this.fraction = this.length / totalLength;
   }
}
