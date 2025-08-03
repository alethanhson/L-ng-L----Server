package org.jdesktop.animation.timing;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TimingSource {
   public  ArrayList<TimingEventListener> listeners = new ArrayList();

   public abstract void start();

   public abstract void stop();

   public abstract void setResolution(int var1);

   public abstract void setStartDelay(int var1);

   public final void addEventListener(TimingEventListener listener) {
      synchronized(this.listeners) {
         if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
         }

      }
   }

   public final void removeEventListener(TimingEventListener listener) {
      synchronized(this.listeners) {
         this.listeners.remove(listener);
      }
   }

   protected final void timingEvent() {
      synchronized(this.listeners) {
         Iterator i$ = this.listeners.iterator();

         while(i$.hasNext()) {
            TimingEventListener listener = (TimingEventListener)i$.next();
            listener.timingSourceEvent(this);
         }

      }
   }
}
