package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Ellipse2D;

class EvaluatorEllipse2D extends Evaluator<Ellipse2D> {
   public EvaluatorEllipse2D() {
   }

   public Ellipse2D evaluate(Ellipse2D v0, Ellipse2D v1, float fraction) {
      double x = v0.getX() + (v1.getX() - v0.getX()) * (double)fraction;
      double y = v0.getY() + (v1.getY() - v0.getY()) * (double)fraction;
      double w = v0.getWidth() + (v1.getWidth() - v0.getWidth()) * (double)fraction;
      double h = v0.getHeight() + (v1.getHeight() - v0.getHeight()) * (double)fraction;
      Ellipse2D value = (Ellipse2D)v0.clone();
      value.setFrame(x, y, w, h);
      return value;
   }
}
