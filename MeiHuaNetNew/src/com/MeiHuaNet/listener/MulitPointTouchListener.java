package com.MeiHuaNet.listener;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MulitPointTouchListener implements OnTouchListener, OnGestureListener {

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    float[] values = null;
    private ImageView view;

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private boolean isZoom = false;
    private GestureDetector gestureDetector;
    
    public MulitPointTouchListener() {
    	gestureDetector = new GestureDetector(this);
    	gestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				return false;
			}
			
			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				Matrix matrix = new Matrix();
	            if(!isZoom) {
	            	float scale = 1.5f;
	            	matrix.setScale(scale, scale);
	            }
	            else {
	            	matrix.setValues(values);
				}
	            view.setImageMatrix(matrix);
	            isZoom = !isZoom;
				Log.i("mice", "onDoubleTap");
				return true;
			}
		});
	}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    		gestureDetector.onTouchEvent(event);
            view = (ImageView) v;

            if(!view.getScaleType().equals(ScaleType.MATRIX)){
                view.setScaleType(ScaleType.MATRIX);
            }
            if(values == null) {
            	values = new float[9];
            	view.getImageMatrix().getValues(values);
            }
            // Dump touch event to log
//            dumpEvent(event);

            // Handle touch events here...
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                    matrix.set(view.getImageMatrix());
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;

                    break;
            case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                    }
                    break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;
            case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY()
                                            - start.y);
                    } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            //Log.d(TAG, "newDist=" + newDist);
                            if (newDist > 10f) {
                                    matrix.set(savedMatrix);
                                    float scale = newDist / oldDist;
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                    }
                    break;
            }

            view.setImageMatrix(matrix);
            return true; // indicate event was handled
    }

//    private void dumpEvent(MotionEvent event) {
//            String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
//                            "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//            StringBuilder sb = new StringBuilder();
//            int action = event.getAction();
//            int actionCode = action & MotionEvent.ACTION_MASK;
//            sb.append("event ACTION_").append(names[actionCode]);
//            if (actionCode == MotionEvent.ACTION_POINTER_DOWN
//                            || actionCode == MotionEvent.ACTION_POINTER_UP) {
//                    sb.append("(pid ").append(
//                                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
//                    sb.append(")");
//            }
//            sb.append("[");
//            for (int i = 0; i < event.getPointerCount(); i++) {
//                    sb.append("#").append(i);
//                    sb.append("(pid ").append(event.getPointerId(i));
//                    sb.append(")=").append((int) event.getX(i));
//                    sb.append(",").append((int) event.getY(i));
//                    if (i + 1 < event.getPointerCount())
//                            sb.append(";");
//            }
//            sb.append("]");
//    }

   
    private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return FloatMath.sqrt(x * x + y * y);
    }

   
    private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}


}
