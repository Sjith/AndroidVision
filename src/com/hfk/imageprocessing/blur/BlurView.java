package com.hfk.imageprocessing.blur;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import android.content.*;
import android.content.res.*;
import android.os.*;

import com.hfk.imageprocessing.*;
import com.hfk.imageprocessing.gesture.ActionGesture;

public class BlurView extends ImageProcessingView implements ActionGesture {
	static final int None = 0;
	static final int Box = 1;
	static final int Median = 2;
	static final int Gaussian = 3;
	
	int kernel = 5;
	double sigma = 2.0;
	
    public BlurView(Context context) {
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
    		title = res.getString(R.string.filters_blur_title);        		
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
    	return res.getStringArray(R.array.filters_blur_items);
    }
    
    public void onTapLeft() {
    	DecrementKernel();
    }
    
    public void onTapRight() {
    	IncrementKernel();
    }

    public void IncrementKernel()
    {
    	Bundle topConfigBundle = getConfigBundleForView(0);
    	if(topConfigBundle != null) {
	    	int topKernelSize = topConfigBundle.getInt("KERNEL_SIZE");
	    	topConfigBundle.putInt("KERNEL_SIZE", IncrementKernelValue(topKernelSize));
    	}

    	Bundle bottomConfigBundle = getConfigBundleForView(0);
    	if(bottomConfigBundle != null) {
	    	int bottomKernelSize = bottomConfigBundle.getInt("KERNEL_SIZE");
	    	bottomConfigBundle.putInt("KERNEL_SIZE", IncrementKernelValue(bottomKernelSize));
    	}
    	
//		int newValue = size;
//	    StringBuilder sb = new StringBuilder();
//        //sb.append("[").append(mainView.getWidth() / 2).append("][").append(e.getX()).append("]");
//	    sb.append("Increment[").append(newValue).append("]");
//    	Toast.makeText(this.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
    }
    
    public int IncrementKernelValue(int kernelValue)
    {
		if(kernelValue + 2 > 31)
		{
			return 31;
		}
		else
		{
	    	return kernelValue + 2;			
		}
    	
    }
    
    public void DecrementKernel()
    {
    	Bundle topConfigBundle = getConfigBundleForView(0);
    	if(topConfigBundle != null) {
	    	int topKernelSize = topConfigBundle.getInt("KERNEL_SIZE");
	    	topConfigBundle.putInt("KERNEL_SIZE", DecrementKernelValue(topKernelSize));
    	}

    	Bundle bottomConfigBundle = getConfigBundleForView(1);
    	if(bottomConfigBundle != null) {
	    	int bottomKernelSize = bottomConfigBundle.getInt("KERNEL_SIZE");
	    	bottomConfigBundle.putInt("KERNEL_SIZE", DecrementKernelValue(bottomKernelSize));
    	}
		
//	    int newValue = size;
//	    StringBuilder sb = new StringBuilder();
//        //sb.append("[").append(mainView.getWidth() / 2).append("][").append(e.getX()).append("]");
//        sb.append("Decrement[").append(newValue).append("]");
//		Toast.makeText(this.getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
		
	}
    
    public int DecrementKernelValue(int kernelValue)
    {
		if(kernelValue - 2 < 3)
		{
			return 3;
		}
		else
		{
	    	return kernelValue - 2;
		}
    }

    @Override
    public Class<?> getConfigClassForFilter(int filter)
    {
    	Class<?> configClass = null; 
    	switch(filter) {
    	case None:
    		break;
    	case Box:
    		configClass = BoxConfig.class;
    		break;
    	case Median:
    		configClass = MedianConfig.class;
    		break;
    	case Gaussian:
    		configClass = GaussianConfig.class;
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
    	case Box:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		configBundle.putInt("KERNEL_CENTERX", (kernel-1)/2);
    		configBundle.putInt("KERNEL_CENTERY", (kernel-1)/2);
    		break;
    	case Median:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		break;
    	case Gaussian:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		configBundle.putDouble("SIGMA", sigma);
    		break;
		default:
			break;
    	}
    	
    	return configBundle;
    }

    @Override
    protected void applyFilter(int filter, Mat sourceMat, Mat targetMat, Bundle configValues, StringBuilder text)
    {
    	int kernelValue = kernel;
    	if(configValues != null && configValues.containsKey("KERNEL_SIZE"))
    	{
    		kernelValue = configValues.getInt("KERNEL_SIZE", kernel);
    	}
    	int kernelCenterX = (kernel-1)/2;
    	if(configValues != null && configValues.containsKey("KERNEL_CENTERX"))
    	{
    		kernelCenterX = configValues.getInt("KERNEL_CENTERX", (kernel-1)/2);
    	}
    	int kernelCenterY = (kernel-1)/2;
    	if(configValues != null && configValues.containsKey("KERNEL_CENTERY"))
    	{
    		kernelCenterY = configValues.getInt("KERNEL_CENTERY", (kernel-1)/2);
    	}
    	
    	double sigmaValue = sigma;
    	if(configValues != null && configValues.containsKey("SIGMA"))
    	{
    		sigmaValue = configValues.getDouble("SIGMA", sigma);
    	}
    	
    	Size sz = new Size(kernelValue, kernelValue); 
    	switch(filter) {
    	case None:
    		text.append("Applying None");
    		sourceMat.copyTo(targetMat);
    		break;
    	case Box:
    		text.append("Applying Box " + kernelValue);
        	Imgproc.blur(sourceMat, targetMat, sz, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Median:
    		text.append("Applying Median " + kernelValue);
        	Imgproc.medianBlur(sourceMat, targetMat, kernelValue);
    		break;
    	case Gaussian:
    		text.append("Applying Gaussian " + kernelValue + " Sigma:" + sigmaValue);
        	Imgproc.GaussianBlur(sourceMat, targetMat, sz, sigmaValue);
    		break;
		default:
			text.append("Applying Default");
			break;
    	}
    }
}
