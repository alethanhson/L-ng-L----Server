package org.jdesktop.animation.timing.interpolation;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Evaluator<T> {
   public  static final Map<Class<?>, Class<? extends Evaluator>> impls = new HashMap();

   public  static void register(Class<?> type, Class<? extends Evaluator> impl) {
      impls.put(type, impl);
   }

   public  static void deregister(Class<?> type) {
      impls.remove(type);
   }

   static <T> Evaluator<T> create(Class<?> type) {
      Class<? extends Evaluator> interpClass = null;
      Iterator i$ = impls.keySet().iterator();

      while(i$.hasNext()) {
         Class<?> klass = (Class)i$.next();
         if (klass.isAssignableFrom(type)) {
            interpClass = (Class)impls.get(klass);
            break;
         }
      }

      if (interpClass == null) {
         throw new IllegalArgumentException("No Evaluator can be found for type " + type + "; consider using" + " different types for your values or supplying a custom" + " Evaluator");
      } else {
         try {
            Constructor<? extends Evaluator> ctor = interpClass.getConstructor();
            return (Evaluator)ctor.newInstance();
         } catch (Exception var4) {
            throw new IllegalArgumentException("Problem constructing appropriate Evaluator for type " + type + ":", var4);
         }
      }
   }

   public abstract T evaluate(T var1, T var2, float var3);

   static {
      impls.put(Byte.class, EvaluatorByte.class);
      impls.put(Short.class, EvaluatorShort.class);
      impls.put(Integer.class, EvaluatorInteger.class);
      impls.put(Long.class, EvaluatorLong.class);
      impls.put(Float.class, EvaluatorFloat.class);
      impls.put(Double.class, EvaluatorDouble.class);
      impls.put(Color.class, EvaluatorColor.class);
      impls.put(Point2D.class, EvaluatorPoint2D.class);
      impls.put(Line2D.class, EvaluatorLine2D.class);
      impls.put(Dimension2D.class, EvaluatorDimension2D.class);
      impls.put(Rectangle2D.class, EvaluatorRectangle2D.class);
      impls.put(RoundRectangle2D.class, EvaluatorRoundRectangle2D.class);
      impls.put(Ellipse2D.class, EvaluatorEllipse2D.class);
      impls.put(Arc2D.class, EvaluatorArc2D.class);
      impls.put(QuadCurve2D.class, EvaluatorQuadCurve2D.class);
      impls.put(CubicCurve2D.class, EvaluatorCubicCurve2D.class);
   }
}
