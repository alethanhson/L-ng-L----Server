package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Rectangle2D;

class EvaluatorRectangle2D extends Evaluator<Rectangle2D> {
   public EvaluatorRectangle2D() {
   }

   public Rectangle2D evaluate(Rectangle2D v0, Rectangle2D v1, float fraction) {
      double x = v0.getX() + (v1.getX() - v0.getX()) * (double)fraction;
      double y = v0.getY() + (v1.getY() - v0.getY()) * (double)fraction;
      double w = v0.getWidth() + (v1.getWidth() - v0.getWidth()) * (double)fraction;
      double h = v0.getHeight() + (v1.getHeight() - v0.getHeight()) * (double)fraction;
      Rectangle2D value = (Rectangle2D)v0.clone();
      value.setRect(x, y, w, h);
      return value;
   }
}
