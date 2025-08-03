package org.jdesktop.animation.timing.triggers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;

public class MouseTrigger extends Trigger implements MouseListener {
   public static MouseTrigger addTrigger(JComponent component, Animator animator, MouseTriggerEvent event) {
      return addTrigger(component, animator, event, false);
   }

   public static MouseTrigger addTrigger(JComponent component, Animator animator, MouseTriggerEvent event, boolean autoReverse) {
      MouseTrigger trigger = new MouseTrigger(animator, event, autoReverse);
      component.addMouseListener(trigger);
      return trigger;
   }

   public MouseTrigger(Animator animator, MouseTriggerEvent event) {
      this(animator, event, false);
   }

   public MouseTrigger(Animator animator, MouseTriggerEvent event, boolean autoReverse) {
      super(animator, event, autoReverse);
   }

   public void mouseEntered(MouseEvent e) {
      this.fire(MouseTriggerEvent.ENTER);
   }

   public void mouseExited(MouseEvent e) {
      this.fire(MouseTriggerEvent.EXIT);
   }

   public void mousePressed(MouseEvent e) {
      this.fire(MouseTriggerEvent.PRESS);
   }

   public void mouseReleased(MouseEvent e) {
      this.fire(MouseTriggerEvent.RELEASE);
   }

   public void mouseClicked(MouseEvent e) {
      this.fire(MouseTriggerEvent.CLICK);
   }
}
