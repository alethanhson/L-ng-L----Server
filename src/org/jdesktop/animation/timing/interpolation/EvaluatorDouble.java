package org.jdesktop.animation.timing.interpolation;

class EvaluatorDouble extends Evaluator<Double> {
   public EvaluatorDouble() {
   }

   public Double evaluate(Double v0, Double v1, float fraction) {
      return v0 + (v1 - v0) * (double)fraction;
   }
}
