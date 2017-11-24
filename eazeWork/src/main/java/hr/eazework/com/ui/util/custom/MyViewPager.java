package hr.eazework.com.ui.util.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyViewPager extends ViewPager {

    private static final int VELOCITY = 200;
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*
		 * View slider=findViewById(R.id.image_slider); if(slider!=null) {
		 * boolean isVisible=slider.getGlobalVisibleRect(new Rect(),null);
		 * //YebhiLog.d("MyViewPAger",isVisible+"");
		 * 
		 * if(isVisible && slider.dispatchTouchEvent(ev)) {
		 * //YebhiLog.d("MyViewPAger","slider handles event");
		 * 
		 * return false; }
		 * 
		 * }
		 */
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ScrollView) {
			return false;
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}
	}

	/**
     * Override the Scroller instance with our own class so we can change the
     * duration
     *//*
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ViewPagerCustomDurationScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    *//**
     * Set the factor by which the duration will change
     *//*
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }*/
}
