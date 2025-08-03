package org.jdesktop.animation.timing.triggers;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;

public class FocusTrigger extends Trigger implements FocusListener {
   public static FocusTrigger addTrigger(JComponent component, Animator animator, FocusTriggerEvent event) {
      return addTrigger(component, animator, event, false);
   }

   public static FocusTrigger addTrigger(JComponent component, Animator animator, FocusTriggerEvent event, boolean autoReverse) {
      FocusTrigger trigger = new FocusTrigger(animator, event, autoReverse);
      component.addFocusListener(trigger);
      return trigger;
   }

   public FocusTrigger(Animator animator, FocusTriggerEvent event) {
      this(animator, event, false);
   }

   public FocusTrigger(Animator animator, FocusTriggerEvent event, boolean autoReverse) {
      super(animator, event, autoReverse);
   }

   public void focusGained(FocusEvent e) {
      this.fire(FocusTriggerEvent.IN);
   }

   public void focusLost(FocusEvent e) {
      this.fire(FocusTriggerEvent.OUT);
   }
}
