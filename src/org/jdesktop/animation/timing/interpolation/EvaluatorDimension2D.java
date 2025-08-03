package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Dimension2D;

class EvaluatorDimension2D extends Evaluator<Dimension2D> {
   public EvaluatorDimension2D() {
   }

   public Dimension2D evaluate(Dimension2D v0, Dimension2D v1, float fraction) {
      double w = v0.getWidth() + (v1.getWidth() - v0.getWidth()) * (double)fraction;
      double h = v0.getHeight() + (v1.getHeight() - v0.getHeight()) * (double)fraction;
      Dimension2D value = (Dimension2D)v0.clone();
      value.setSize(w, h);
      return value;
   }
}
