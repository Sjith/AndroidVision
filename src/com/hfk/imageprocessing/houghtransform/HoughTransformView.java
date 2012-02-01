package com.hfk.imageprocessing.houghtransform;

import java.lang.*;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.hfk.imageprocessing.ImageProcessingView;
import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.gesture.ActionGesture;

public class HoughTransformView extends ImageProcessingView implements ActionGesture {
	static final int None = 0;
	static final int Lines = 1;
	
    public HoughTransformView(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
    	Resources res = getResources();
    	if(res == null)
    	{
    		return "empty";
    	}
    	String title = "nothing";
    	try {
    		title = res.getString(R.string.filters_houghtransform_title);        		
    	}
    	catch (Resources.NotFoundException ex) {
    		return "nfexception";
    	}
    	catch (Exception ex) {
    		return "exception";    		
    	}
    	
    	return title;
    }

    @Override
    protected String[] getAvailableFilters() {
    	Resources res = getResources();
    	return res.getStringArray(R.array.filters_houghtransform_items);
    }
    
    public void onTapLeft() {
    }
    
    public void onTapRight() {
    }

    @Override
    public Class<?> getConfigClassForFilter(int filter)
    {
    	Class<?> configClass = null; 
    	switch(filter) {
    	case None:
    		break;
    	case Lines:
    		break;
		default:
			break;
    	}
    	
    	return configClass;
    }

    @Override
    public Bundle getDefaultConfigBundleForFilter(int filter)
    {
    	Bundle configBundle = null; 
    	switch(filter) {
    	case None:
    		break;
    	case Lines:
    		break;
		default:
			break;
    	}
    	
    	return configBundle;
    }

    @Override
    protected void applyFilter(int filter, Mat sourceMat, Mat targetMat, Bundle configValues, StringBuilder text)
    {
    	Mat cannyMat = new Mat();
    	Mat lineMat = new Mat();
    	switch(filter) {
    	case None:
    		text.append("Applying None");
    		sourceMat.copyTo(targetMat);
    		break;
    	case Lines:
    		text.append("Applying Houghs");
    		//sourceMat.copyTo(targetMat);
    		Imgproc.Canny(sourceMat, targetMat, 125, 350, 3);
    		Imgproc.Canny(sourceMat, cannyMat, 125, 350, 3);
    		Imgproc.HoughLines(cannyMat, lineMat, 1, Math.PI/180, 80);
    		
    		Scalar color = new Scalar(255, 255, 255); 
    		
    		double[] data; 
    		double rho, theta; 
    		Point pt1 = new Point(); 
    		Point pt2 = new Point(); 
    		double a, b; 
    		double x0, y0; 
    		for (int i = 0; i < lineMat.cols(); i++) {     
    			data = lineMat.get(0, i);     
    			rho = data[0];     
    			theta = data[1];     
    			a = Math.cos(theta);     
    			b = Math.sin(theta);     
    			x0 = a*rho;     
    			y0 = b*rho;     
    			pt1.x = Math.round(x0 + 1000*(-b));     
    			pt1.y = Math.round(y0 + 1000*a);     
    			pt2.x = Math.round(x0 - 1000*(-b));     
    			pt2.y = Math.round(y0 - 1000 *a);     
    			Core.line(targetMat, pt1, pt2, color, 3); 
    		} 
    		
    		
    		break;
		default:
			text.append("Applying Default");
			break;
    	}
	}
}