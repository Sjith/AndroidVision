package com.hfk.imageprocessing;

import android.content.Context;
import android.view.View;
import android.graphics.*;

public class Draw2d extends View {  

     public Draw2d(Context context) {  

         super(context);  

     }     

    

         @Override 

     protected void onDraw(Canvas c){  

         super.onDraw(c);  

         Paint paint = new Paint();  
          paint.setStyle(Paint.Style.FILL);  
      
          // make the entire canvas white  
          paint.setColor(Color.WHITE);  

          c.drawPaint(paint); 
          
          paint.setAntiAlias(true);  
          paint.setColor(Color.BLUE);  
          c.drawCircle(80, 20, 15, paint); 


     }  
} 
