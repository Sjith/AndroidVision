package com.hfk.imageprocessing.blur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hfk.widget.*;
import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.widget.*;

import android.text.Editable;
import android.text.TextWatcher;


public class BoxConfig extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.blur_box);
        
        mTextKernelSize = (EditText)findViewById(R.id.editKernelSizeBox);
        mKernelMatrix = (KernelView)findViewById(R.id.matrixBoxKernel);
        
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
        
        mKernelMatrix.setOnCellTouchHandler(new MatrixView.OnCellTouchHandler() {			
			@Override
			public void OnCellTouch(int colIndex, int rowIndex) {
		    	mKernelMatrix.setKernelCenter(colIndex, rowIndex);
			}
			
			@Override
			public void OnCellLongTouch(int colIndex, int rowIndex) {
			}
		});
	}
	
//	@Override public void onResume() {     
//		super.onResume(); 
//		//updateKernelMatrix();
//	} 

	@Override
	public void onBackPressed() {
	    Intent result = new Intent();

	    Bundle b = new Bundle();
	    b.putInt("KERNEL_SIZE", Integer.parseInt(mTextKernelSize.getText().toString()));
	    b.putInt("KERNEL_CENTERX", mKernelMatrix.getCenterX());
	    b.putInt("KERNEL_CENTERY", mKernelMatrix.getCenterY());

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
    	
    	mKernelMatrix.setNumberOfColumns(kernelValue);
    	mKernelMatrix.setNumberOfRows(kernelValue);
    	
    	mKernelMatrix.setKernelCenter((kernelValue-1)/2, (kernelValue-1)/2);
	    
    	mKernelMatrix.invalidate();
	}

	private EditText mTextKernelSize;
	private KernelView mKernelMatrix;
}
