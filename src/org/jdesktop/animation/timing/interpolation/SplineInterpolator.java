package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

public final class SplineInterpolator implements Interpolator {
   public  float x1;
   public  float y1;
   public  float x2;
   public  float y2;
   public  ArrayList lengths = new ArrayList();

   public SplineInterpolator(float x1, float y1, float x2, float y2) {
      if (!(x1 < 0.0F) && !(x1 > 1.0F) && !(y1 < 0.0F) && !(y1 > 1.0F) && !(x2 < 0.0F) && !(x2 > 1.0F) && !(y2 < 0.0F) && !(y2 > 1.0F)) {
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
         float prevX = 0.0F;
         float prevY = 0.0F;
         float prevLength = 0.0F;

         for(float t = 0.01F; t <= 1.0F; t += 0.01F) {
            Float xy = this.getXY(t);
            float length = prevLength + (float)Math.sqrt((double)((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY)));
            LengthItem lengthItem = new LengthItem(length, t);
            this.lengths.add(lengthItem);
            prevLength = length;
            prevX = xy.x;
            prevY = xy.y;
         }

         for(int i = 0; i < this.lengths.size(); ++i) {
            LengthItem lengthItem = (LengthItem)this.lengths.get(i);
            lengthItem.setFraction(prevLength);
         }

      } else {
         throw new IllegalArgumentException("Control points must be in the range [0, 1]:");
      }
   }

   public  Float getXY(float t) {
      float invT = 1.0F - t;
      float b1 = 3.0F * t * invT * invT;
      float b2 = 3.0F * t * t * invT;
      float b3 = t * t * t;
      Float xy = new Float(b1 * this.x1 + b2 * this.x2 + b3, b1 * this.y1 + b2 * this.y2 + b3);
      return xy;
   }

   public  float getY(float t) {
      float invT = 1.0F - t;
      float b1 = 3.0F * t * invT * invT;
      float b2 = 3.0F * t * t * invT;
      float b3 = t * t * t;
      return b1 * this.y1 + b2 * this.y2 + b3;
   }

   public float interpolate(float lengthFraction) {
      float interpolatedT = 1.0F;
      float prevT = 0.0F;
      float prevLength = 0.0F;

      for(int i = 0; i < this.lengths.size(); ++i) {
         LengthItem lengthItem = (LengthItem)this.lengths.get(i);
         float fraction = lengthItem.getFraction();
         float t = lengthItem.getT();
         if (lengthFraction <= fraction) {
            float proportion = (lengthFraction - prevLength) / (fraction - prevLength);
            interpolatedT = prevT + proportion * (t - prevT);
            return this.getY(interpolatedT);
         }

         prevLength = fraction;
         prevT = t;
      }

      return this.getY(interpolatedT);
   }
}
