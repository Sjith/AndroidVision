package com.hfk.imageprocessing.mathematicalmorphology;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.widget.KernelView;
import com.hfk.widget.MatrixView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class StructElmConfig extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.mathmorph_structelm);
        
		Resources res = getResources();
		String[] shapes = res.getStringArray(R.array.filters_mathmorph_kernelshapes);
		
		mKernelShape = (Spinner) findViewById(R.id.spinnerKernelShapeStructElm);

		ArrayAdapter<String> availableShapes = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_spinner_item, 
				shapes);

		availableShapes.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		mKernelShape.setAdapter(availableShapes);
        
        mTextKernelSize = (EditText)findViewById(R.id.editKernelSizeStructElm);
        mKernelMatrix = (KernelView)findViewById(R.id.matrixStructElm);
        
        Bundle data = getIntent().getExtras();
        if(data != null){
        	if(data.containsKey("KERNEL_SIZE")) {
        		int kernelSize = data.getInt("KERNEL_SIZE");
        		mTextKernelSize.setText(Integer.toString(kernelSize));        		
        		mKernelMatrix.setNumberOfColumns(kernelSize);
        		mKernelMatrix.setNumberOfRows(kernelSize);
        	}
        		
        	if(data.containsKey("KERNEL_CENTERX") && data.containsKey("KERNEL_CENTERY")) {        		
        		mKernelMatrix.setKernelCenter(data.getInt("KERNEL_CENTERX"), data.getInt("KERNEL_CENTERY"));
        	}
        	
        	if(data.containsKey("KERNEL_SHAPE")) {
        		int kernelShape = data.getInt("KERNEL_SHAPE");
    			mKernelShape.setSelection(getSelectionFromShape(kernelShape));
    			
    			if(kernelShape == -1) {
    	        	if(data.containsKey("KERNEL_VALUE_ARRAY")) {    
    	        	    int[] valueArray = data.getIntArray("KERNEL_VALUE_ARRAY");
    	        	    for(int col = 0; col < mKernelMatrix.getNumberOfColumns(); col++)
    	        	    	for(int row = 0; row < mKernelMatrix.getNumberOfRows(); row++)
    	        	    		mKernelMatrix.setKernelValue(col, row, 
    	        	    				valueArray[col * mKernelMatrix.getNumberOfColumns() + row]);
    	        	}    				
    			}
        	}
        }
        
        TextWatcher kernelSizeTextWatcher = new TextWatcher() 
        {         
        	@Override         
        	public void afterTextChanged(Editable s) {         
        		updateKernelMatrix();         
        	}          
        	
        	@Override         
        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {         
        		
        	}          
        	
        	@Override         
        	public void onTextChanged(CharSequence s, int start, int before, int count) {     
        		
        	}          
   
        };

        mTextKernelSize.addTextChangedListener(kernelSizeTextWatcher); 
        
        mTextKernelSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {    
        	public void onFocusChange(View v, boolean hasFocus) {        
        		if (!hasFocus) {            
        			String kernelValueAsString = mTextKernelSize.getText().toString();
        			if(kernelValueAsString == null || kernelValueAsString.length() == 0 || Integer.parseInt(mTextKernelSize.getText().toString()) == 0) {
            			mTextKernelSize.requestFocus();        
        			}
        		}    
        	}
        });
        
		mKernelShape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
        		updateKernelMatrix();         
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}			
		});
        
        mKernelMatrix.setOnCellTouchHandler(new MatrixView.OnCellTouchHandler() {			
			@Override
			public void OnCellTouch(int colIndex, int rowIndex) {
				if(mKernelShape.getSelectedItemPosition() != 3) {
					return;
				}
				
				int value = mKernelMatrix.getKernelValue(colIndex, rowIndex);
				if(value == 0)
					mKernelMatrix.setKernelValue(colIndex, rowIndex, 1);
				else
					mKernelMatrix.setKernelValue(colIndex, rowIndex, 0);
			}
			
			@Override
			public void OnCellLongTouch(int colIndex, int rowIndex) {
		    	mKernelMatrix.setKernelCenter(colIndex, rowIndex);
			}
		});

	}

	@Override
	public void onBackPressed() {
	    Intent result = new Intent();
	    
	    int kernelSize = Integer.parseInt(mTextKernelSize.getText().toString());

	    Bundle b = new Bundle();
	    b.putInt("KERNEL_SIZE", kernelSize);
	    b.putInt("KERNEL_CENTERX", mKernelMatrix.getCenterX());
	    b.putInt("KERNEL_CENTERY", mKernelMatrix.getCenterY());
	    b.putInt("KERNEL_SHAPE", getShapeFromSelection());
	    
	    ArrayList<Integer> valueList = new ArrayList<Integer>();
	    for(int col = 0; col < kernelSize; col++)
	    	for(int row = 0; row < kernelSize; row++)
	    		valueList.add(mKernelMatrix.getKernelValue(col, row));
	    int[] valueArray = new int[valueList.size()];
	    for(int i = 0; i < valueList.size(); i++)
	    	valueArray[i] = valueList.get(i);
		b.putIntArray("KERNEL_VALUE_ARRAY", valueArray);

	    result.putExtras(b);

	    setResult(Activity.RESULT_OK, result);
	    
	    super.onBackPressed();
	}

	private void updateKernelMatrix() {
		String kernelValueAsString = mTextKernelSize.getText().toString();
		if((kernelValueAsString == null || kernelValueAsString.length() == 0)) {
			return;
		}
		
		int kernelValue = Integer.parseInt(kernelValueAsString);
    	
		if(mKernelMatrix.getNumberOfColumns() != kernelValue){
	    	mKernelMatrix.setNumberOfColumns(kernelValue);
	    	mKernelMatrix.setNumberOfRows(kernelValue);
	    	
	    	mKernelMatrix.setKernelCenter((kernelValue-1)/2, (kernelValue-1)/2);			
		}
		
		int kernelShape = getShapeFromSelection();		
		if(kernelShape != -1){
	    	Size sz = new Size(kernelValue, kernelValue); 
	    	Mat kernel = Imgproc.getStructuringElement(
	    			kernelShape, 
	    			sz, 
	    			new Point(mKernelMatrix.getCenterX(), mKernelMatrix.getCenterY()));			
	    	for(int row = 0; row < kernel.rows(); row++) {
	    		for(int col = 0; col < kernel.cols(); col++){
	    			double cellValue = kernel.get(row, col)[0];
	    			if(cellValue == 1.0) {
	    				mKernelMatrix.setKernelValue(col, row, 1);
	    			}
	    			else {
	    				mKernelMatrix.setKernelValue(col, row, 0);
	    			}
	    		}
	    	}
		}
		else {
			//mKernelMatrix.setKernelValue(colIndex, rowIndex, value);
		}
	    
    	mKernelMatrix.invalidate();
	}
	
	private int getShapeFromSelection() {
		int kernelShape = Imgproc.MORPH_RECT;
		switch(mKernelShape.getSelectedItemPosition()){
		case 0:
			kernelShape = Imgproc.MORPH_RECT;
			break;
		case 1:
			kernelShape = Imgproc.MORPH_ELLIPSE;
			break;
		case 2:
			kernelShape = Imgproc.MORPH_CROSS;
			break;
		case 3:
			kernelShape = -1;
			break;
		}
		
		return kernelShape;
	}
	
	private int getSelectionFromShape(int shape) {
		int shapeSelection = 0;
		switch(shape) {
		case Imgproc.MORPH_RECT:
			shapeSelection = 0;
			break;
		case Imgproc.MORPH_ELLIPSE:
			shapeSelection = 1;
			break;
		case Imgproc.MORPH_CROSS:
			shapeSelection = 2;
			break;
		case -1:
			shapeSelection = 3;
			break;
		}
		
		return shapeSelection;
	}
	


	private EditText mTextKernelSize;
	private KernelView mKernelMatrix;
	private Spinner mKernelShape;
}
