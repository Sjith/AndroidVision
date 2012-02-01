package com.hfk.imageprocessing.histogram;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.hfk.imageprocessing.ImageProcessingView;
import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.gesture.ActionGesture;

public class HistogramView extends ImageProcessingView implements ActionGesture {
	static final int None = 0;
	static final int Equalizaton = 1;
	
    public HistogramView(Context context) {
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
    		title = res.getString(R.string.filters_histogram_title);        		
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
    	return res.getStringArray(R.array.filters_histogram_items);
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
    	case Equalizaton:
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
    	case Equalizaton:
    		break;
		default:
			break;
    	}
    	
    	return configBundle;
    }

    @Override
    protected void applyFilter(int filter, Mat sourceMat, Mat targetMat, Bundle configValues, StringBuilder text)
    {
    	switch(filter) {
    	case None:
    		text.append("Applying None");
    		sourceMat.copyTo(targetMat);
    		break;
    	case Equalizaton:
    		text.append("Applying Equalization");
    		Imgproc.equalizeHist(sourceMat, targetMat);
    		break;
		default:
			text.append("Applying Default");
			break;
    	}
	}
}
