package org.jdesktop.animation.timing.interpolation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.langla.utlis.UTPKoolVN;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PropertySetter extends TimingTargetAdapter {
   public  Object object;
   public  String propertyName;
   public  KeyFrames keyFrames;
   public  Method propertySetter;
   public  Method propertyGetter;

   public static Animator createAnimator(int duration, Object object, String propertyName, KeyFrames keyFrames) {
      PropertySetter ps = new PropertySetter(object, propertyName, keyFrames);
      Animator animator = new Animator(duration, ps);
      return animator;
   }

   public static <T> Animator createAnimator(int duration, Object object, String propertyName, T... params) {
      PropertySetter ps = new PropertySetter(object, propertyName, params);
      Animator animator = new Animator(duration, ps);
      return animator;
   }

   public static <T> Animator createAnimator(int duration, Object object, String propertyName, Evaluator evaluator, T... params) {
      PropertySetter ps = new PropertySetter(object, propertyName, evaluator, params);
      Animator animator = new Animator(duration, ps);
      return animator;
   }

   public PropertySetter(Object object, String propertyName, KeyFrames keyFrames) {
      this.object = object;
      this.propertyName = propertyName;
      this.keyFrames = keyFrames;

      try {
         this.setupMethodInfo();
      } catch (NoSuchMethodException var5) {
         throw new IllegalArgumentException("Bad property name (" + propertyName + "): could not find " + "an appropriate setter or getter method for that property");
      }
   }

   public <T> PropertySetter(Object object, String propertyName, T... params) {
      this(object, propertyName, new KeyFrames(KeyValues.create(params)));
   }

   public <T> PropertySetter(Object object, String propertyName, Evaluator evaluator, T... params) {
      this(object, propertyName, new KeyFrames(KeyValues.create(evaluator, params)));
   }

   public  void setupMethodInfo() throws NoSuchMethodException {
      try {
         String firstChar = this.propertyName.substring(0, 1);
         String remainder = this.propertyName.substring(1);
         Class propertyType = this.getType();
         String propertySetterName = "set" + firstChar.toUpperCase() + remainder;
         PropertyDescriptor prop = new PropertyDescriptor(this.propertyName, this.object.getClass(), (String)null, propertySetterName);
         this.propertySetter = prop.getWriteMethod();
         if (this.isToAnimation()) {
            String propertyGetterName = "get" + firstChar.toUpperCase() + remainder;
            prop = new PropertyDescriptor(this.propertyName, this.object.getClass(), propertyGetterName, (String)null);
            this.propertyGetter = prop.getReadMethod();
         }

      } catch (Exception var7) {
         throw new NoSuchMethodException("Cannot find property methods: " + var7);
      }
   }

   public void begin() {
      if (this.isToAnimation()) {
         try {
            this.setStartValue(this.propertyGetter.invoke(this.object));
         } catch (Exception var2) {
            UTPKoolVN.Print("Problem with propertySetter in ObjectModifier");
         }
      }

   }

   public void timingEvent(float fraction) {
      try {
         this.setValue(this.object, this.propertySetter, fraction);
      } catch (Exception var3) {
         UTPKoolVN.Print("Problem in ObjectModifier.timingEvent: " + var3);
      }

   }

   public  String getPropertyName() {
      return this.propertyName;
   }

   public  void setStartValue(Object object) {
      this.keyFrames.getKeyValues().setStartValue(object);
   }

   public  void setValue(Object object, Method method, float fraction) {
      try {
         method.invoke(object, this.keyFrames.getValue(fraction));
      } catch (Exception var5) {
         UTPKoolVN.Print("Problem invoking method " + this.propertySetter + " in object " + object + " in setValue" + var5);
      }

   }

   public  Class getType() {
      return this.keyFrames.getType();
   }

   public  boolean isToAnimation() {
      return this.keyFrames.getKeyValues().isToAnimation();
   }
}
