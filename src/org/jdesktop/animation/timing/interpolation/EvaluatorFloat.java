package org.jdesktop.animation.timing.interpolation;

class EvaluatorFloat extends Evaluator<Float> {
   public EvaluatorFloat() {
   }

   public Float evaluate(Float v0, Float v1, float fraction) {
      return v0 + (v1 - v0) * fraction;
   }
}
