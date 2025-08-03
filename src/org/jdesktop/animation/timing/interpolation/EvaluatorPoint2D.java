package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Point2D;

class EvaluatorPoint2D extends Evaluator<Point2D> {
   public  Point2D value;

   public EvaluatorPoint2D() {
   }

   public Point2D evaluate(Point2D v0, Point2D v1, float fraction) {
      if (this.value == null) {
         this.value = (Point2D)v0.clone();
      }

      double x = v0.getX() + (v1.getX() - v0.getX()) * (double)fraction;
      double y = v0.getY() + (v1.getY() - v0.getY()) * (double)fraction;
      this.value.setLocation(x, y);
      return this.value;
   }
}
