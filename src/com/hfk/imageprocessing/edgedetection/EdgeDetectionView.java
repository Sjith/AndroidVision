package com.hfk.imageprocessing.edgedetection;

import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.hfk.imageprocessing.ImageProcessingView;
import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.edgedetection.SobelConfig;
import com.hfk.imageprocessing.gesture.ActionGesture;

public class EdgeDetectionView extends ImageProcessingView implements ActionGesture {
	static final int None = 0;
	static final int Sobel = 1;
	static final int Canny = 2;
	
	int derivXOrder = 1;
	int derivYOrder = 1;
	int kernel = 3;
	double scale = 1.0;
	double delta = 0.0;
	double lowerThreshold = 150.0;
	double upperThreshold = 250.0;
	
    public EdgeDetectionView(Context context) {
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
    		title = res.getString(R.string.filters_edgedetection_title);        		
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
    	return res.getStringArray(R.array.filters_edgedetection_items);
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
    	case Sobel:
    		configClass = SobelConfig.class;
    		break;
    	case Canny:
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
    	case Sobel:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		configBundle.putInt("DERIV_X_ORDER", derivXOrder);
    		configBundle.putInt("DERIV_Y_ORDER", derivYOrder);
    		configBundle.putDouble("SCALE", scale);
    		configBundle.putDouble("DELTA", delta);
    		break;
    	case Canny:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		configBundle.putDouble("LOWER_THRESHOLD", lowerThreshold);
    		configBundle.putDouble("LOWER_THRESHOLD", upperThreshold);
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
    	case Sobel:
    		text.append("Applying Sobel");
    		Imgproc.Sobel(sourceMat, targetMat, CvType.CV_8U, derivXOrder, derivYOrder, kernel, scale, delta);
    		break;
    	case Canny:
    		text.append("Applying Sobel");
    		Imgproc.Canny(sourceMat, targetMat, lowerThreshold, upperThreshold, kernel);
    		break;
		default:
			text.append("Applying Default");
			break;
    	}
	}
}
