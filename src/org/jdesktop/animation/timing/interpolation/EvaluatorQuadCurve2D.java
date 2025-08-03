package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.QuadCurve2D;

class EvaluatorQuadCurve2D extends Evaluator<QuadCurve2D> {
   public EvaluatorQuadCurve2D() {
   }

   public QuadCurve2D evaluate(QuadCurve2D v0, QuadCurve2D v1, float fraction) {
      double x1 = v0.getX1() + (v1.getX1() - v0.getX1()) * (double)fraction;
      double y1 = v0.getY1() + (v1.getY1() - v0.getY1()) * (double)fraction;
      double x2 = v0.getX2() + (v1.getX2() - v0.getX2()) * (double)fraction;
      double y2 = v0.getY2() + (v1.getY2() - v0.getY2()) * (double)fraction;
      double ctrlx = v0.getCtrlX() + (v1.getCtrlX() - v0.getCtrlX()) * (double)fraction;
      double ctrly = v0.getCtrlY() + (v1.getCtrlY() - v0.getCtrlY()) * (double)fraction;
      QuadCurve2D value = (QuadCurve2D)v0.clone();
      value.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
      return value;
   }
}
