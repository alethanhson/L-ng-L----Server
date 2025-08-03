package org.jdesktop.animation.timing.interpolation;

class EvaluatorShort extends Evaluator<Short> {
   public EvaluatorShort() {
   }

   public Short evaluate(Short v0, Short v1, float fraction) {
      return (short)(v0 + (short)((int)((float)(v1 - v0) * fraction)));
   }
}
