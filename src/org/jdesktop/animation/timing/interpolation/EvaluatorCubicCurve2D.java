package org.jdesktop.animation.timing.interpolation;

import java.awt.geom.CubicCurve2D;

class EvaluatorCubicCurve2D extends Evaluator<CubicCurve2D> {
   public EvaluatorCubicCurve2D() {
   }

   public CubicCurve2D evaluate(CubicCurve2D v0, CubicCurve2D v1, float fraction) {
      double x1 = v0.getX1() + (v1.getX1() - v0.getX1()) * (double)fraction;
      double y1 = v0.getY1() + (v1.getY1() - v0.getY1()) * (double)fraction;
      double x2 = v0.getX2() + (v1.getX2() - v0.getX2()) * (double)fraction;
      double y2 = v0.getY2() + (v1.getY2() - v0.getY2()) * (double)fraction;
      double ctrlx1 = v0.getCtrlX1() + (v1.getCtrlX1() - v0.getCtrlX1()) * (double)fraction;
      double ctrly1 = v0.getCtrlY1() + (v1.getCtrlY1() - v0.getCtrlY1()) * (double)fraction;
      double ctrlx2 = v0.getCtrlX2() + (v1.getCtrlX2() - v0.getCtrlX2()) * (double)fraction;
      double ctrly2 = v0.getCtrlY2() + (v1.getCtrlY2() - v0.getCtrlY2()) * (double)fraction;
      CubicCurve2D value = (CubicCurve2D)v0.clone();
      value.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
      return value;
   }
}
