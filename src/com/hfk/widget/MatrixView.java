package com.hfk.widget;

import android.view.*;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.content.*;
import android.graphics.*;
import android.util.*;

import java.util.*;

public class MatrixView extends View {
	
	public static interface OnCellTouchHandler {
		public abstract void OnCellTouch(int colIndex, int rowIndex);
		public abstract void OnCellLongTouch(int colIndex, int rowIndex);
	}

	public MatrixView(Context context, AttributeSet attrs){
        super(context, attrs);
        
        gestureDetector = new GestureDetector(null, new MyGestureDetector(), null, true);
	}	
	
	public void setNumberOfColumns(int numberOfColumns) {
		mNumberOfColumns = numberOfColumns;
		mCellStyleMatrix = null;
		mCellValueMatrix = null;
	}
	
	public int getNumberOfColumns() {
		return mNumberOfColumns;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		mNumberOfRows = numberOfRows;
		mCellStyleMatrix = null;
		mCellValueMatrix = null;
	}
	
	public int getNumberOfRows() {
		return mNumberOfRows;
	}
	
	public int getColumnAtPoint(int xCoord) {
		return xCoord / (this.getWidth()/mNumberOfColumns);
	}
	
	public int getRowAtPoint(int yCoord) {
		return yCoord / (this.getHeight()/mNumberOfRows);
	}
	
	public void setCellText(int colIndex, int rowIndex, String text) {
		if(mCellValueMatrix == null) {
			mCellValueMatrix = new ArrayList<List<String>>();
			
			for(int i=0; i<mNumberOfColumns; i++) {
				mCellValueMatrix.add(new ArrayList<String>());
				for(int j=0; j<mNumberOfRows; j++) {
					mCellValueMatrix.get(i).add("");
				}
			}
		}
		
		mCellValueMatrix.get(colIndex).set(rowIndex, text);
	}
	
	public String getCellText(int colIndex, int rowIndex) {
		return mCellValueMatrix.get(colIndex).get(rowIndex);
	}
	
	public void setCellStyle(int colIndex, int rowIndex, int color) {
		if(mCellStyleMatrix == null) {
			mCellStyleMatrix = new ArrayList<List<Integer>>();
			
			for(int i=0; i<mNumberOfColumns; i++) {
				mCellStyleMatrix.add(new ArrayList<Integer>());
				for(int j=0; j<mNumberOfRows; j++) {
					mCellStyleMatrix.get(i).add(Color.WHITE);
				}
			}
		}
		
		mCellStyleMatrix.get(colIndex).set(rowIndex, color);
	}
	
	public void setOnCellTouchHandler(OnCellTouchHandler onCellTouchHandler){
		mOnCellTouchHandler = onCellTouchHandler;
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
	        return true;        	
        }

        return false;
    }

    @Override
	protected void onDraw(Canvas c){
	    super.onDraw(c);
	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL);

	    // make the entire canvas white
	    paint.setColor(Color.WHITE);
	    c.drawPaint(paint);
	    
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLUE);
	    
	    if((mNumberOfColumns == 0) || (mNumberOfRows == 0)) {
	    	return;
	    }
	    
	    int cellWidth = this.getWidth()/mNumberOfColumns;
	    int cellHeight = this.getHeight()/mNumberOfRows;
	    
	    for (int i = 0; i < mNumberOfColumns; i++) {
	    	c.drawLine( 
	    			/* startX */ i * cellWidth,
	    			/* startY */ 0,
	    			/* stopX */  i * cellWidth,
	    			/* stopY */  this.getHeight(),
	    			paint);
	    }
	    
	    for (int i = 0; i < mNumberOfRows; i++) {
	    	c.drawLine( 
	    			/* startX */ 0,
	    			/* startY */ i * cellHeight,
	    			/* stopX */  this.getWidth(),
	    			/* stopY */  i * cellHeight,
	    			paint);
	    }
	    
	    if(mCellStyleMatrix != null) {
			for(int i=0; i<mNumberOfColumns; i++) {
				for(int j=0; j<mNumberOfRows; j++) {
					int color = mCellStyleMatrix.get(i).get(j);
					if(color != Color.WHITE) {
					    paint.setColor(color);
						c.drawRect(/* left */ i * cellWidth, 
								/* top */ j * cellHeight, 
								/* right */ (i + 1) * cellWidth, 
								/* bottom*/ (j + 1) * cellHeight, 
								paint);
					}
				}
			}	    	
	    }
	    
	    paint.setColor(Color.BLACK);
	    if(mCellValueMatrix != null) {
			for(int i=0; i<mNumberOfColumns; i++) {
				for(int j=0; j<mNumberOfRows; j++) {
					String text = mCellValueMatrix.get(i).get(j);
					if((text != null) && (text != ""))  {
						c.drawText(/* text */ text, 
								/* x */ i * cellWidth, 
								/* y */ (j + 1) * cellHeight, 
								paint);
					}
				}
			}	    	
	    }
	}
    

    class MyGestureDetector extends SimpleOnGestureListener {
    	public static final String X_COORD = "xCoord"; 
    	public static final String Y_COORD = "xCoord"; 
    	
        @Override
        public void onLongPress(MotionEvent event) {
        	
        	int touchedColumnIndex = getColumnAtPoint((int)event.getX());
        	int touchedRowIndex = getRowAtPoint((int)event.getY());
        	
        	mOnCellTouchHandler.OnCellTouch(touchedColumnIndex, touchedRowIndex);
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent event) {
        	return true;
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
        	
        	int touchedColumnIndex = getColumnAtPoint((int)event.getX());
        	int touchedRowIndex = getRowAtPoint((int)event.getY());
        	
        	mOnCellTouchHandler.OnCellTouch(touchedColumnIndex, touchedRowIndex);

        	return true;
        }
    }
    
    private int mNumberOfColumns = 0;
    private int mNumberOfRows = 0;
    private List<List<String>> mCellValueMatrix;
    private List<List<Integer>> mCellStyleMatrix;
    
	private GestureDetector gestureDetector;
    private OnCellTouchHandler mOnCellTouchHandler;
}
