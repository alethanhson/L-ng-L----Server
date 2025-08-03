package org.jdesktop.animation.timing.triggers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import org.jdesktop.animation.timing.Animator;

public class ActionTrigger extends Trigger implements ActionListener {
   public static ActionTrigger addTrigger(Object object, Animator animator) {
      ActionTrigger trigger = new ActionTrigger(animator);

      try {
         Method addListenerMethod = object.getClass().getMethod("addActionListener", ActionListener.class);
         addListenerMethod.invoke(object, trigger);
         return trigger;
      } catch (Exception var4) {
         throw new IllegalArgumentException("Problem adding listener to object: " + var4);
      }
   }

   public ActionTrigger(Animator animator) {
      super(animator);
   }

   public void actionPerformed(ActionEvent ae) {
      this.fire();
   }
}
