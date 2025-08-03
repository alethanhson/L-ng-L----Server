package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Line2D;

class EvaluatorLine2D extends Evaluator<Line2D> {
   public EvaluatorLine2D() {
   }

   public Line2D evaluate(Line2D v0, Line2D v1, float fraction) {
      double x1 = v0.getX1() + (v1.getX1() - v0.getX1()) * (double)fraction;
      double y1 = v0.getY1() + (v1.getY1() - v0.getY1()) * (double)fraction;
      double x2 = v0.getX2() + (v1.getX2() - v0.getX2()) * (double)fraction;
      double y2 = v0.getY2() + (v1.getY2() - v0.getY2()) * (double)fraction;
      Line2D value = (Line2D)v0.clone();
      value.setLine(x1, y1, x2, y2);
      return value;
   }
}
