package com.hfk.imageprocessing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.widget.TableRow.LayoutParams;
//import android.opengl.GLSurfaceView;

import com.hfk.imageprocessing.blur.BlurView;


public class DrawableImageProcessingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.table_view_test);
        
        BlurView tvw = new BlurView(this);

        tvw.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));

        TableRow ttr = (TableRow)findViewById(R.id.topTableRow);
        ttr.addView(tvw);

//        GLSurfaceView bvw = new GLSurfaceView(this);
//        bvw.setRenderer(new CubeRenderer(false));
//        bvw.setLayoutParams(new LayoutParams(
//                LayoutParams.FILL_PARENT,
//                LayoutParams.FILL_PARENT));
        
        Draw2d dvw = new Draw2d(this);

        TableRow btr = (TableRow)findViewById(R.id.bottomTableRow);
        btr.addView(dvw);
    }
}
