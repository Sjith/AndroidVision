package com.hfk.widget;

import android.view.*;
import android.content.*;
import android.graphics.*;
import android.util.AttributeSet;

import java.util.*;

public class BarGraphView extends View {

	public BarGraphView(Context context, AttributeSet attrs){
        super(context, attrs);
	}	
	
	public void setNumberOfBars(int numberOfBars) {
		mNumberOfBars = numberOfBars;
		
		mBarValueList = new ArrayList<Double>();
		for (int i = 0; i < mNumberOfBars; i++) {
			mBarValueList.add(0.0);
		}
	}
	
	public void setMaxBarValue(double maxBarValue) {
		mMaxBarValue = maxBarValue;
	}
	
	public void setBarValue(int barIndex, double barValue) {
		mBarValueList.add(barIndex, barValue);
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
	    
	    for (int i = 0; i < mNumberOfBars; i++) {
	    	c.drawRect(
	    			/* left */   i*this.getWidth()/mNumberOfBars, 
	    			/* top */    (float) (mBarValueList.get(i)*this.getHeight()/mMaxBarValue), 
	    			/* right */  (i+1)*this.getWidth()/mNumberOfBars, 
	    			/* bottom */ 0, 
	    			paint);
	    }
	}
    
    private int mNumberOfBars = 0;
    private double mMaxBarValue;
    private List<Double> mBarValueList;
}
