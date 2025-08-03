package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.Arc2D;

class EvaluatorArc2D extends Evaluator<Arc2D> {
   public EvaluatorArc2D() {
   }

   public Arc2D evaluate(Arc2D v0, Arc2D v1, float fraction) {
      double x = v0.getX() + (v1.getX() - v0.getX()) * (double)fraction;
      double y = v0.getY() + (v1.getY() - v0.getY()) * (double)fraction;
      double w = v0.getWidth() + (v1.getWidth() - v0.getWidth()) * (double)fraction;
      double h = v0.getHeight() + (v1.getHeight() - v0.getHeight()) * (double)fraction;
      double start = v0.getAngleStart() + (v1.getAngleStart() - v0.getAngleStart()) * (double)fraction;
      double extent = v0.getAngleExtent() + (v1.getAngleExtent() - v0.getAngleExtent()) * (double)fraction;
      Arc2D value = (Arc2D)v0.clone();
      value.setArc(x, y, w, h, start, extent, v0.getArcType());
      return value;
   }
}
