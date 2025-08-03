package org.jdesktop.animation.timing.interpolation;

class EvaluatorLong extends Evaluator<Long> {
   public EvaluatorLong() {
   }

   public Long evaluate(Long v0, Long v1, float fraction) {
      return v0 + (long)((float)(v1 - v0) * fraction);
   }
}
