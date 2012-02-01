package com.hfk.imageprocessing.blur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hfk.imageprocessing.*;

public class MedianConfig extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.blur_median);
        
        mTextKernelSize = (EditText)findViewById(R.id.editKernalSizeMedian);
        
        Bundle data = getIntent().getExtras();
        if(data != null){
        	if(data.containsKey("KERNEL_SIZE"))
        		mTextKernelSize.setText(Integer.toString(data.getInt("KERNEL_SIZE")));        	
        }

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
	}
	
	@Override
	public void onBackPressed() {
	    Intent result = new Intent();

	    Bundle b = new Bundle();
	    b.putInt("KERNEL_SIZE", Integer.parseInt(mTextKernelSize.getText().toString()));

	    result.putExtras(b);

	    setResult(Activity.RESULT_OK, result);
	    
	    super.onBackPressed();
	}
	
	private EditText mTextKernelSize;
}
