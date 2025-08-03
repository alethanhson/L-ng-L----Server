package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.RoundRectangle2D;

class EvaluatorRoundRectangle2D extends Evaluator<RoundRectangle2D> {
   public EvaluatorRoundRectangle2D() {
   }

   public RoundRectangle2D evaluate(RoundRectangle2D v0, RoundRectangle2D v1, float fraction) {
      double x = v0.getX() + (v1.getX() - v0.getX()) * (double)fraction;
      double y = v0.getY() + (v1.getY() - v0.getY()) * (double)fraction;
      double w = v0.getWidth() + (v1.getWidth() - v0.getWidth()) * (double)fraction;
      double h = v0.getHeight() + (v1.getHeight() - v0.getHeight()) * (double)fraction;
      double arcw = v0.getArcWidth() + (v1.getArcWidth() - v0.getArcWidth()) * (double)fraction;
      double arch = v0.getArcHeight() + (v1.getArcHeight() - v0.getArcHeight()) * (double)fraction;
      RoundRectangle2D value = (RoundRectangle2D)v0.clone();
      value.setRoundRect(x, y, w, h, arcw, arch);
      return value;
   }
}
