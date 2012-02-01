package com.hfk.imageprocessing.mathematicalmorphology;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.hfk.imageprocessing.ImageProcessingView;
import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.gesture.ActionGesture;

public class MathMorphView extends ImageProcessingView implements ActionGesture {
	static final int None = 0;
	static final int Erosion = 1;
	static final int Dilation = 2;
	static final int Opening = 3;
	static final int Closing = 4;
	static final int Blackhat = 5;
	static final int Tophat = 6;
	static final int Gradient = 7;

	int kernel = 3;
	int shape = Imgproc.MORPH_RECT;
	
    public MathMorphView(Context context) {
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
    		title = res.getString(R.string.filters_mathmorph_title);        		
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
    	return res.getStringArray(R.array.filters_mathmorph_items);
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

    	Bundle bottomConfigBundle = getConfigBundleForView(0);
    	if(bottomConfigBundle != null) {
	    	int bottomKernelSize = bottomConfigBundle.getInt("KERNEL_SIZE");
	    	bottomConfigBundle.putInt("KERNEL_SIZE", DecrementKernelValue(bottomKernelSize));
    	}
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
    	case 0:
    		break;
    	case 1:
    	case 2:
    	case 3:
    		configClass = StructElmConfig.class;
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
    	case 0:
    		break;
    	case 1:
    	case 2:
    	case 3:
    		configBundle = new Bundle();
    		configBundle.putInt("KERNEL_SIZE", kernel);
    		configBundle.putInt("KERNEL_CENTERX", (kernel-1)/2);
    		configBundle.putInt("KERNEL_CENTERY", (kernel-1)/2);
    		configBundle.putInt("KERNEL_SHAPE", shape);
    		break;
		default:
			break;
    	}
    	
    	return configBundle;
    }

    @Override
    protected void applyFilter(int filter, Mat sourceMat, Mat targetMat, Bundle configValues, StringBuilder text)
    {
    	int kernelSize = kernel;
    	if(configValues != null && configValues.containsKey("KERNEL_SIZE"))
    	{
    		kernelSize = configValues.getInt("KERNEL_SIZE", kernel);
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

    	int shapeValue = shape;
    	if(configValues != null && configValues.containsKey("KERNEL_SHAPE"))
    	{
    		shapeValue = configValues.getInt("KERNEL_SHAPE", shapeValue);
    	}
    	
    	Mat kernel = null;
    	if(shapeValue != -1) {
        	Size sz = new Size(kernelSize, kernelSize); 
        	kernel = Imgproc.getStructuringElement(shapeValue, sz, new Point(-1, -1));    		
    	}
    	else {
        	if(configValues != null && configValues.containsKey("KERNEL_VALUE_ARRAY")) {
        		int[] valueArray = configValues.getIntArray("KERNEL_VALUE_ARRAY");
        		kernel = new Mat(kernelSize, kernelSize, CvType.CV_8U, new Scalar(1));
        	    for(int col = 0; col < kernelSize; col++)
        	    	for(int row = 0; row < kernelSize; row++)
        	    		kernel.put(row, col, valueArray[col * kernelSize + row]);        		
        	}
    	}
    	
    	switch(filter) {
    	case None:
    		text.append("Applying None");
    		sourceMat.copyTo(targetMat);
    		break;
    	case Erosion:
    		text.append("Applying Erosion"); // " + kernelValue);
    		//Imgproc.adaptiveThreshold(sourceMat, targetMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C , Imgproc.THRESH_BINARY, 3, 0.0);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.erode(targetMat, targetMat, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Dilation:
    		text.append("Applying Dilation"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.dilate(targetMat, targetMat, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Opening:
    		text.append("Applying Opening"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.morphologyEx(targetMat, targetMat, Imgproc.MORPH_OPEN, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Closing:
    		text.append("Applying Closing"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.morphologyEx(targetMat, targetMat, Imgproc.MORPH_CLOSE, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Blackhat:
    		text.append("Applying Blackhat"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.morphologyEx(targetMat, targetMat, Imgproc.MORPH_BLACKHAT, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Tophat:
    		text.append("Applying Tophat"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.morphologyEx(targetMat, targetMat, Imgproc.MORPH_TOPHAT, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
    	case Gradient:
    		text.append("Applying Gradient"); // " + kernelValue);
    		Imgproc.threshold(sourceMat, targetMat, 128, 255, Imgproc.THRESH_BINARY);
        	Imgproc.morphologyEx(targetMat, targetMat, Imgproc.MORPH_GRADIENT, kernel, new Point(kernelCenterX, kernelCenterY));
    		break;
		default:
			text.append("Applying Default");
			break;
    	}
	}
}
