package com.hfk.imageprocessing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.hfk.imageprocessing.blur.BlurView;
import com.hfk.imageprocessing.mathematicalmorphology.MathMorphView;
import com.hfk.imageprocessing.histogram.HistogramView;
import com.hfk.imageprocessing.houghtransform.HoughTransformView;
import com.hfk.imageprocessing.edgedetection.EdgeDetectionView;
import com.hfk.imageprocessing.gesture.*;

public class ImageProcessingActivity extends Activity {

    class MyGestureDetector extends SimpleOnGestureListener {
    	public static final String X_COORD = "xCoord"; 
    	public static final String Y_COORD = "xCoord"; 
    	
        @Override
        public void onLongPress(MotionEvent e) {
        	mViewPort = mainView.getViewFromPoint(e.getX(), e.getY());
            showDialog(DIALOG_FILTERS);
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
        	mViewPort = mainView.getViewFromPoint(e.getX(), e.getY());
        	int filter = mainView.getFilterForViewPort(mViewPort);
        	Bundle configBundle = mainView.getConfigBundleForView(mViewPort);
        	Class<?> cfgClass = mainView.getConfigClassForFilter(filter);
        	if(cfgClass != null)
        	{
        		Intent myIntent = new Intent(ImageProcessingActivity.this, cfgClass);
        		if(configBundle != null)
        			myIntent.putExtras(configBundle);
        		startActivityForResult(myIntent, mViewPort);
        	}
        	return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	
        	if(actionGesture != null && ((mainView.getWidth() / 4) < e.getX()))
        	{
        		actionGesture.onTapLeft();
        	}
        	else if (actionGesture != null && ((mainView.getWidth() * 3 / 4) >= e.getX()))
        	{
        		actionGesture.onTapRight();
        	}
        	return true;
        }
    }
    
//    class MyScaleDetector extends SimpleOnScaleGestureListener{
//    	@Override
//    	public void onScaleEnd (ScaleGestureDetector detector) {
//
//    	}
//    }
    
    class MyPinchDetector extends PinchGestureDetector.SimpleOnPinchListener{
    	@Override
    	public boolean onPinchOpen(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2) {
        	mainView.splitView(0);
    		return true;
    	}
    	
    	@Override
    	public boolean onPinchClose(MotionEvent s1, MotionEvent s2, MotionEvent e1, MotionEvent e2) {
        	mainView.mergeView(0, 1);
    		return true;
    	}
    }

    private static final int MENU_SPLIT_SCREEN = Menu.FIRST;
    private static final int MENU_MERGE_SCREEN = Menu.FIRST + 1;
    
    private static final int DIALOG_FILTERS = 1;
    
    private ImageProcessingView mainView;
	private GestureDetector gestureDetector;
	private PinchGestureDetector pinchDetector;
	private ActionGesture actionGesture;
	
	private int mViewPort;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setFullscreen();
		
		Bundle bundle = this.getIntent().getExtras();
		String param1 = bundle.getString(Main.FILTER_GROUP);
		
		if(param1.equals(Main.FILTER_GROUP_BLUR))
		{
			setBlurView();
		}
		
		if(param1.equals(Main.FILTER_GROUP_MATHMORPH))
		{
			setMathMorphView();
		}
		
		if(param1.equals(Main.FILTER_GROUP_HISTOGRAM))
		{
			setHistogramView();
		}
		
		if(param1.equals(Main.FILTER_GROUP_EDGEDETECTION))
		{
			setEdgeDetectionView();
		}
		
		if(param1.equals(Main.FILTER_GROUP_HOUGHTRANSFORM))
		{
			setHoughTransformView();
		}

        gestureDetector = new GestureDetector(null, new MyGestureDetector(), null, true);
        pinchDetector = new PinchGestureDetector(new MyPinchDetector());
        
        setContentView(mainView);
    }
    
    public void setBlurView() {
    	BlurView blurView = new BlurView(this);

		mainView = blurView;
		actionGesture = blurView;
    }
    
    public void setMathMorphView() {
    	MathMorphView mathMorphView = new MathMorphView(this);

		mainView = mathMorphView;
		actionGesture = mathMorphView;
    }
    
    public void setHistogramView() {
    	HistogramView histogramView = new HistogramView(this);

		mainView = histogramView;
		actionGesture = histogramView;
    }
    
    public void setEdgeDetectionView() {
    	EdgeDetectionView edgeDetectionView = new EdgeDetectionView(this);

		mainView = edgeDetectionView;
		actionGesture = edgeDetectionView;
    }
    
    public void setHoughTransformView() {
    	HoughTransformView houghTransformView = new HoughTransformView(this);

		mainView = houghTransformView;
		actionGesture = houghTransformView;
    }

	/**
	 * Maximize the application.
	 */
	public void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ImageProcessingActivity.MENU_SPLIT_SCREEN, 0, "Split");
        menu.add(1, ImageProcessingActivity.MENU_MERGE_SCREEN, 0, "Merge");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SPLIT_SCREEN:
                handleSplitScreen();
                return true;
            case MENU_MERGE_SCREEN:
                handleMergeScreen();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return pinchDetector.onTouchEvent(event);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
        case DIALOG_FILTERS:
            return new AlertDialog.Builder(ImageProcessingActivity.this)
                .setTitle(mainView.getTitle())
                .setItems(mainView.getAvailableFilters(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	mainView.setFilterForViewPort(mViewPort, which);
                    }
                })
                .create();
        }
        return null;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    	mainView.setConfigBundleForView(requestCode, intent.getExtras());
    }
    
    private void handleSplitScreen() {
    	mainView.splitView(0);
    }
    
    private void handleMergeScreen() {
    	mainView.mergeView(0, 1);
    }
    
}