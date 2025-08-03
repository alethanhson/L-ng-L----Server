package org.jdesktop.animation.timing.interpolation;

class EvaluatorInteger extends Evaluator<Integer> {
   public EvaluatorInteger() {
   }

   public Integer evaluate(Integer v0, Integer v1, float fraction) {
      return v0 + (int)((float)(v1 - v0) * fraction);
   }
}
