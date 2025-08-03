package org.jdesktop.animation.timing.interpolation;

import java.awt.Color;

class EvaluatorColor extends Evaluator<Color> {
   public EvaluatorColor() {
   }

   public Color evaluate(Color v0, Color v1, float fraction) {
      int r = v0.getRed() + (int)((float)(v1.getRed() - v0.getRed()) * fraction + 0.5F);
      int g = v0.getGreen() + (int)((float)(v1.getGreen() - v0.getGreen()) * fraction + 0.5F);
      int b = v0.getBlue() + (int)((float)(v1.getBlue() - v0.getBlue()) * fraction + 0.5F);
      int a = v0.getAlpha() + (int)((float)(v1.getAlpha() - v0.getAlpha()) * fraction + 0.5F);
      Color value = new Color(r, g, b, a);
      return value;
   }
}
