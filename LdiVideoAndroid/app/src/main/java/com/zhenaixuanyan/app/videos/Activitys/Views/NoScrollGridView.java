package com.zhenaixuanyan.app.videos.Activitys.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Gservfocus on 15/9/7.
 * @author forrest
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int motionPosition = pointToPosition((int) event.getX(), (int) event.getY());
        if( motionPosition == INVALID_POSITION ) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
