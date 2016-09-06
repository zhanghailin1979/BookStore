package learning.moliying.com.bookstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * descreption:
 * company: moliying.com
 * Created by vince on 16/07/13.
 */
public class TouchWebView extends WebView {
    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchWebView(Context context) {
        super(context);
    }

    float down = 0;
    float up = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            down = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            up = event.getY();
        }
        if (down < up && this.getScrollY() == 0) {
            requestDisallowInterceptTouchEvent(false);
        } else {
            requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

}