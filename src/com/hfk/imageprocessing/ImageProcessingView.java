package com.hfk.imageprocessing;

import org.opencv.android;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.os.Bundle;


public abstract class ImageProcessingView extends FrameGrabberView {
    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mTargetMat;
    private Mat mTopSourceImage;
    private Mat mTopTargetImage;
    private Mat mBottomTargetImage;
    
    private Rect mTopView;
    private Rect mBottomView;
    
    private int mTopViewFilter = 0;
    private int mBottomViewFilter = 0;
    
    private Bundle mTopConfigBundle;
    private Bundle mBottomConfigBundle;
    
    public String mTopText;
    public String mBottomText;

    public ImageProcessingView(Context context) {
        super(context);
        
		mTopView = new Rect();
		mTopView.x = 0;
		mTopView.y = 0;
		mTopView.width = getFrameWidth();
		mTopView.height = getFrameHeight();
    }

    protected abstract String getTitle();

    protected abstract String[] getAvailableFilters();

    public void DrawText(String text, int posX, int posY)
    {
    	if(mRgba != null)
    	{
    		//DecimalFormat df = new DecimalFormat("#.##");
            Core.putText(mRgba, text, new Point(posX, posY), 3/* CV_FONT_HERSHEY_COMPLEX */, 0.75, new Scalar(255, 0, 0, 255), 1);
    	}    	
    }

    public void DrawText()
    {
    	if(mTopText != null)
    		DrawText(mTopText, 10, 100);	
    	if(mBottomText != null)
    		DrawText(mBottomText, 10, mBottomView.y + 100);	
    }
    
    public int getViewFromPoint(float x, float y){
    	Point p = new Point(x, y);

    	if((mBottomView == null) || (mTopView.contains(p)))
    	{
    		return 0;
    	}
    	else if ((mBottomView != null) && (mBottomView.contains(p)))
    	{
    		return 1;
    	}
    	return -1;
    }
    
    public void splitView(int view)
    {
    	if(mBottomView != null)
    		return;
    	
    	if(view == 0)
    	{
    		mTopView = new Rect();
    		mTopView.x = 0;
    		mTopView.y = 0;
    		mTopView.width = getFrameWidth();
    		mTopView.height = getFrameHeight() / 2;

    		mBottomView = new Rect();
    		mBottomView.x = 0;
    		mBottomView.y = getFrameHeight() / 2;
    		mBottomView.width = getFrameWidth();
    		mBottomView.height = getFrameHeight();
    	}
    }
    
    public void mergeView(int view1, int view2)
    {
    	if(((view1 == 0) && (view2 == 1))
    		|| ((view1 == 1) && (view2 == 0)))
    	{
    		mTopView = new Rect();
    		mTopView.x = 0;
    		mTopView.y = 0;
    		mTopView.width = getFrameWidth();
    		mTopView.height = getFrameHeight();

    		mBottomView = null;
    	}
    }
    
    public void setFilterForViewPort(int view, int filter) {
    	if(view == 0 && mTopViewFilter != filter)
    	{
    		mTopConfigBundle = getDefaultConfigBundleForFilter(filter);
    		mTopText = null;
    		mTopViewFilter = filter;
    	}
    	else if(view == 1 && mBottomViewFilter != filter)
    	{
    		mBottomConfigBundle = getDefaultConfigBundleForFilter(filter);
    		mBottomText = null;
    		mBottomViewFilter = filter;
    	}
    }
    
    public int getFilterForViewPort(int view) {
    	if(view == 0)
    	{
    		return mTopViewFilter;
    	}
    	else if(view == 1)
    	{
    		return mBottomViewFilter;
    	}
    	
    	return 0;
    }
    
    public abstract Class<?> getConfigClassForFilter(int filter);
    
    public abstract Bundle getDefaultConfigBundleForFilter(int filter);
    
    public void setConfigBundleForView(int view, Bundle configValues)
    {
    	if(view == 0)
    	{
    		mTopConfigBundle = configValues;
    	}
    	else if(view == 1)
    	{
    		mBottomConfigBundle = configValues;
    	}    	
    }
    
    public Bundle getConfigBundleForView(int view)
    {
    	if(view == 0)
    	{
    		return mTopConfigBundle;
    	}
    	else if(view == 1)
    	{
    		return mBottomConfigBundle;
    	}  
    	
    	return null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int format, int width, int height) {
        super.surfaceChanged(_holder, format, width, height);

        synchronized (this) {
            // initialize Mats before usage
            mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
            mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

            mRgba = new Mat();
        }
    }

    protected abstract void applyFilter(int filter, Mat sourceMat, Mat targetMat, Bundle configValues, StringBuilder text);
    
    @Override
    protected Bitmap processFrame(byte[] data) {
        mYuv.put(0, 0, data);

        //Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420i2RGB, 4);
        
        if(mTargetMat != null)
        {
        	mTargetMat.dispose();
        	mTargetMat = null;
        }
        mTargetMat = mGraySubmat.clone();
        if(mTopSourceImage != null)
        {
        	mTopSourceImage.dispose();
        	mTopSourceImage = null;
        }
        if(mTopTargetImage != null)
        {
        	mTopTargetImage.dispose();
        	mTopTargetImage = null;
        }
        if(mBottomTargetImage != null)
        {
        	mBottomTargetImage.dispose();
        	mBottomTargetImage = null;
        }
        if(mBottomView != null)
        {
        	mTopSourceImage = mGraySubmat.submat(0, getFrameHeight()/2, 0, getFrameWidth());
	        
        	mTopTargetImage = mTargetMat.submat(0, getFrameHeight()/2, 0, getFrameWidth());
        	mBottomTargetImage = mTargetMat.submat(getFrameHeight()/2, getFrameHeight(), 0, getFrameWidth());
        }
        else
        {
        	mTopSourceImage = mGraySubmat.submat(0, getFrameHeight(), 0, getFrameWidth());        	
	        
        	mTopTargetImage = mTargetMat.submat(0, getFrameHeight(), 0, getFrameWidth());
        }
        
        StringBuilder sbTopText = new StringBuilder();
        applyFilter(mTopViewFilter, mTopSourceImage, mTopTargetImage, mTopConfigBundle, sbTopText);
        mTopText = sbTopText.toString();

        if(mBottomTargetImage != null)
        {
            StringBuilder sbBottomText = new StringBuilder();
            applyFilter(mBottomViewFilter, mTopSourceImage, mBottomTargetImage, mBottomConfigBundle, sbBottomText);
            mBottomText = sbBottomText.toString();
        }

        Imgproc.cvtColor(mTargetMat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
        DrawText();
        Bitmap bmp = Bitmap.createBitmap(getFrameWidth(), getFrameHeight(), Bitmap.Config.ARGB_8888);

        if (android.MatToBitmap(mRgba, bmp))
            return bmp;

        bmp.recycle();
        return null;
    }

    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (mYuv != null)
                mYuv.dispose();
            if (mRgba != null)
                mRgba.dispose();
            if (mGraySubmat != null)
                mGraySubmat.dispose();
            if (mTargetMat != null)
            	mTargetMat.dispose();
            if (mTopSourceImage != null)
            	mTopSourceImage.dispose();
            if (mTopTargetImage != null)
            	mTopTargetImage.dispose();
            if (mBottomTargetImage != null)
            	mBottomTargetImage.dispose();

            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mTargetMat = null;
            mTopSourceImage = null;
            mTopTargetImage = null;
            mBottomTargetImage = null;
        }
    }
}
