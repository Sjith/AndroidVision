package com.hfk.imageprocessing.gesture;

import android.util.Log;
import android.view.MotionEvent;

public class PinchGestureDetector {

   public interface OnPinchListener {
	   boolean onPinchOpen(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2);
	   boolean onPinchClose(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2);
   }
	
    public static class SimpleOnPinchListener implements OnPinchListener {
        public boolean onPinchOpen(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2) {
            return false;
        }

        public boolean onPinchClose(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2) {
            return false;
        }
    }

    private static final String TAG = "Touch";
	
   private String PinchMode = "PinchMode";

   private String mMode;
	
   private float dxi = 0;
   private float dyi = 0;
   
   private OnPinchListener mOnPinchListener;
   
   public PinchGestureDetector(OnPinchListener onPinchListener) {
	   mOnPinchListener = onPinchListener;
   }
   
   public boolean onTouchEvent(MotionEvent event) {
      // Dump touch event to log
      if((event.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_MOVE)
      {
    	  dumpEvent(event);
      }

      // Handle touch events here...
      switch (event.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
         break;
      case MotionEvent.ACTION_POINTER_DOWN:
    	 if((event.getPointerCount() == 2))
    	 {
			 int pointerIndex0 = event.getPointerId(0);
			 int pointerIndex1 = event.getPointerId(1);
	        
	         dxi = Math.abs(event.getX(pointerIndex0) - event.getX(pointerIndex1));
	         dyi = Math.abs(event.getY(pointerIndex0) - event.getY(pointerIndex1));
    		 mMode = PinchMode;
    	 }
         break;
      case MotionEvent.ACTION_UP:
		 if(mMode == PinchMode && (event.getPointerCount() == 1))
		 {
			 mMode = null;
		 }
		 break;
      case MotionEvent.ACTION_POINTER_UP:
         break;
      case MotionEvent.ACTION_MOVE:
		 if(mMode == PinchMode && (event.getPointerCount() == 2))
		 {
			//DecimalFormat df = new DecimalFormat("#.##");
			
			int pointerIndex0 = event.getPointerId(0);
			int pointerIndex1 = event.getPointerId(1);
			
			float cxi = Math.abs(event.getX(pointerIndex0) - event.getX(pointerIndex1));
			//float cyi = Math.abs(event.getY(pointerIndex0) - event.getY(pointerIndex1));
			
			//splittabelView.DrawText("dxi:" + df.format(dxi) + " - cxi:" + df.format(cxi));
			
			if(cxi > 2 * dxi)
			{
				mOnPinchListener.onPinchOpen(null, null, null, null);
			}
			
			if(cxi < 0.5 * dxi)
			{
				mOnPinchListener.onPinchClose(null, null, null, null);
			}
			
		 }
         break;
      }

      return true; // indicate event was handled
   }

   /** Show an event in the LogCat view, for debugging */
   private void dumpEvent(MotionEvent event) {
      // ...
      String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
            "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
      StringBuilder sb = new StringBuilder();
      int action = event.getAction();
      int actionCode = action & MotionEvent.ACTION_MASK;
      sb.append("event ACTION_").append(names[actionCode]);
      if (actionCode == MotionEvent.ACTION_POINTER_DOWN
            || actionCode == MotionEvent.ACTION_POINTER_UP) {
         sb.append("(pid ").append(
               action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
         sb.append(")");
      }
      sb.append("[");
      for (int i = 0; i < event.getPointerCount(); i++) {
         sb.append("#").append(i);
         sb.append("(pid ").append(event.getPointerId(i));
         sb.append(")=").append((int) event.getX(i));
         sb.append(",").append((int) event.getY(i));
         if (i + 1 < event.getPointerCount())
            sb.append(";");
      }
      sb.append("]");
      Log.d(TAG, sb.toString());
   }
}
