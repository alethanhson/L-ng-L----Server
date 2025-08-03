package org.jdesktop.animation.timing.interpolation;

class EvaluatorByte extends Evaluator<Byte> {
   public EvaluatorByte() {
   }

   public Byte evaluate(Byte v0, Byte v1, float fraction) {
      return (byte)(v0 + (byte)((int)((float)(v1 - v0) * fraction)));
   }
}
