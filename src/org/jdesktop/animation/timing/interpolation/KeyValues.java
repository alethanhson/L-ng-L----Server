package org.jdesktop.animation.timing.interpolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyValues<T> {
   public  final List<T> values;
   public  final Evaluator<T> evaluator;
   public  final Class<?> type;
   public  T startValue;

   public static <T> KeyValues<T> create(T... params) {
      return new KeyValues(params);
   }

   public static <T> KeyValues<T> create(Evaluator evaluator, T... params) {
      return new KeyValues(evaluator, params);
   }

   public  KeyValues(T... params) {
      this(Evaluator.create(params.getClass().getComponentType()), params);
   }

   public  KeyValues(Evaluator evaluator, T... params) {
      this.values = new ArrayList();
      if (params == null) {
         throw new IllegalArgumentException("params array cannot be null");
      } else if (params.length == 0) {
         throw new IllegalArgumentException("params array must have at least one element");
      } else {
         if (params.length == 1) {
            this.values.add(null);
         }

         Collections.addAll(this.values, params);
         this.type = params.getClass().getComponentType();
         this.evaluator = evaluator;
      }
   }

   int getSize() {
      return this.values.size();
   }

   Class<?> getType() {
      return this.type;
   }

   void setStartValue(T startValue) {
      if (this.isToAnimation()) {
         this.startValue = startValue;
      }

   }

   boolean isToAnimation() {
      return this.values.get(0) == null;
   }

   T getValue(int i0, int i1, float fraction) {
      T lowerValue = this.values.get(i0);
      if (lowerValue == null) {
         lowerValue = this.startValue;
      }

      Object value;
      if (i0 == i1) {
         value = lowerValue;
      } else {
         T v1 = this.values.get(i1);
         value = this.evaluator.evaluate(lowerValue, v1, fraction);
      }

      return (T) value;
   }
}
