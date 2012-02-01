package com.hfk.imageprocessing.blur;

import com.hfk.imageprocessing.R;
import com.hfk.widget.*;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.*;
import android.text.*;
import android.view.*;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class GaussianConfig extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.blur_gaussian);
        
        mTextKernelSize = (EditText)findViewById(R.id.editKernalSizeGaussian);
        mTextSigma = (EditText)findViewById(R.id.editSigmaGaussian);
        mGaussianCurve = (BarGraphView)findViewById(R.id.graphGaussianCurve);
        
        Bundle data = getIntent().getExtras();
        if(data != null){
        	if(data.containsKey("KERNEL_SIZE"))
        		mTextKernelSize.setText(Integer.toString(data.getInt("KERNEL_SIZE"))); 
        	if(data.containsKey("SIGMA"))
        		mTextSigma.setText(Double.toString(data.getDouble("SIGMA")));        	
        }
        
        TextWatcher kernelSizeTextWatcher = new TextWatcher() 
        {         
        	@Override         
        	public void afterTextChanged(Editable s) {         
        		updateKernelChart();         
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
        
        
        TextWatcher sigmaTextWatcher = new TextWatcher() 
        {         
        	@Override         
        	public void afterTextChanged(Editable s) {         
        		updateKernelChart();         
        	}          
        	
        	@Override         
        	public void beforeTextChanged(CharSequence s, int start, int count, int after) {         
        		
        	}          
        	
        	@Override         
        	public void onTextChanged(CharSequence s, int start, int before, int count) {             

        	}          
   
        };

        mTextSigma.addTextChangedListener(sigmaTextWatcher); 
        
        mTextSigma.setOnFocusChangeListener(new View.OnFocusChangeListener() {    
        	public void onFocusChange(View v, boolean hasFocus) {        
        		if (!hasFocus) {            
        			String sigmaValueAsString = mTextSigma.getText().toString();
        			if(sigmaValueAsString == null || sigmaValueAsString.length() == 0) {
        				mTextSigma.requestFocus();        
        			}
        		}    
        	}
        });
    	
	}
	
	@Override public void onResume() {     
		super.onResume(); 
		updateKernelChart();
	} 
	
	@Override
	public void onBackPressed() {
	    Intent result = new Intent();

	    Bundle b = new Bundle();
	    b.putInt("KERNEL_SIZE", Integer.parseInt(mTextKernelSize.getText().toString()));
	    b.putDouble("SIGMA", Double.parseDouble(mTextSigma.getText().toString()));

	    result.putExtras(b);

	    setResult(Activity.RESULT_OK, result);
	    
	    super.onBackPressed();

	}
	
	private void updateKernelChart() {
		String kernelValueAsString = mTextKernelSize.getText().toString();
		String sigmaValueAsString = mTextSigma.getText().toString();
		if((kernelValueAsString == null || kernelValueAsString.length() == 0 || Integer.parseInt(mTextKernelSize.getText().toString()) == 0)
				|| (sigmaValueAsString == null || sigmaValueAsString.length() == 0)) {
			return;
		}
		
		int kernelValue = Integer.parseInt(kernelValueAsString);
		double sigmaValue = Double.parseDouble(sigmaValueAsString);
    	Mat gaussianKernel = Imgproc.getGaussianKernel(kernelValue, sigmaValue);	
    	
    	mGaussianCurve.setMaxBarValue(1.0);
    	mGaussianCurve.setNumberOfBars(kernelValue);
    	
	    for (int i = 0; i < kernelValue; i++) {
	    	mGaussianCurve.setBarValue(i, gaussianKernel.get(i, 0)[0]);
	    }
	    
	    mGaussianCurve.invalidate();
	}
	
	private EditText mTextKernelSize;
	private EditText mTextSigma;
	private BarGraphView mGaussianCurve;
}
