package com.hfk.imageprocessing.edgedetection;

import com.hfk.imageprocessing.R;
import com.hfk.imageprocessing.widget.KernelView;
import com.hfk.widget.MatrixView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SobelConfig extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edgedet_sobel);
        
        mTextKernelSize = (EditText)findViewById(R.id.editKernelSizeSobel);
        mTextDerivX = (EditText)findViewById(R.id.editDerivXOrderSobel);
        mTextDerivY = (EditText)findViewById(R.id.editDerivYOrderSobel);
        mTextScale = (EditText)findViewById(R.id.editScaleSobel);
        mTextDelta = (EditText)findViewById(R.id.editDeltaSobel);
        
        Bundle data = getIntent().getExtras();
        if(data != null){        	
        	if(data.containsKey("KERNEL_SIZE")) {
        		int kernelSize = data.getInt("KERNEL_SIZE");
        		mTextKernelSize.setText(Integer.toString(kernelSize));        		
        	}

        	if(data.containsKey("DERIV_X_ORDER")) {
        		int derivXOrder = data.getInt("DERIV_X_ORDER");
        		mTextDerivX.setText(Integer.toString(derivXOrder));        		
        	}
        	
        	if(data.containsKey("DERIV_Y_ORDER")) {
        		int derivYOrder = data.getInt("DERIV_Y_ORDER");
        		mTextDerivY.setText(Integer.toString(derivYOrder));        		
        	}
        	
        	if(data.containsKey("SCALE")) {
        		double scale = data.getDouble("SCALE");
        		mTextScale.setText(Double.toString(scale));        		
        	}
        	
        	if(data.containsKey("DELTA")) {
        		double delta = data.getDouble("DELTA");
        		mTextDelta.setText(Double.toString(delta));        		
        	}
        }
        
//        mTextKernelSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {    
//        	public void onFocusChange(View v, boolean hasFocus) {        
//        		if (!hasFocus) {            
//        			String kernelValueAsString = mTextKernelSize.getText().toString();
//        			if(kernelValueAsString == null || kernelValueAsString.length() == 0 || Integer.parseInt(mTextKernelSize.getText().toString()) == 0) {
//            			mTextKernelSize.requestFocus();        
//        			}
//        		}    
//        	}
//        });
	}

	@Override
	public void onBackPressed() {
	    Intent result = new Intent();

	    Bundle b = new Bundle();
	    b.putInt("KERNEL_SIZE", Integer.parseInt(mTextKernelSize.getText().toString()));
		b.putInt("DERIV_X_ORDER", Integer.parseInt(mTextDerivX.getText().toString()));
		b.putInt("DERIV_Y_ORDER", Integer.parseInt(mTextDerivY.getText().toString()));
		b.putDouble("SCALE", Double.parseDouble(mTextScale.getText().toString()));
		b.putDouble("DELTA", Double.parseDouble(mTextDelta.getText().toString()));

	    result.putExtras(b);

	    setResult(Activity.RESULT_OK, result);
	    
	    super.onBackPressed();
	}

	private EditText mTextKernelSize;
	private EditText mTextDerivX;
	private EditText mTextDerivY;
	private EditText mTextScale;
	private EditText mTextDelta;
}
