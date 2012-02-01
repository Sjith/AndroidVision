package com.hfk.imageprocessing.widget;

import android.content.*;
import android.graphics.Color;
import android.util.*;
import android.view.*;

import com.hfk.widget.*;

public class KernelView extends MatrixView {
	
    public KernelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setKernelCenter(int column, int row) {
    	kernelCenterX = column;
    	kernelCenterY = row;
    	
		for(int i = 0; i < getNumberOfColumns(); i++) {
			for(int j = 0; j < getNumberOfColumns(); j++) {
				setCellStyle(i, j, Color.WHITE);
			}
		}

    	setCellStyle(kernelCenterX, kernelCenterY, Color.RED);
    	
    	invalidate();    	
    }
    
    public void setKernelValue(int colIndex, int rowIndex, int value){
    	setCellText(colIndex, rowIndex, Integer.toString(value));
    	
    	invalidate();    	
    }
    
    public int getCenterX(){
    	return kernelCenterX;
    }
    
    public int getCenterY(){
    	return kernelCenterY;
    }
    
    public int getKernelValue(int colIndex, int rowIndex) {
    	return Integer.decode(getCellText(colIndex, rowIndex));
    }
    
    private int kernelCenterX;
    private int kernelCenterY;
}
