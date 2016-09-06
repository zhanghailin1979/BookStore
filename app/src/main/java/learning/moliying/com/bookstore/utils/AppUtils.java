package learning.moliying.com.bookstore.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import learning.moliying.com.bookstore.App;

/**
 * Created by Administrator on 2016/8/16.
 */
public class AppUtils {
    public static String formatPhoneNumber(String mobile) {
        StringBuilder maskNumber = new StringBuilder(11);
        maskNumber.append(mobile.substring(0, 3));
        maskNumber.append("****");
        maskNumber.append(mobile.substring(7, mobile.length()));
        return maskNumber.toString();
    }


    /**
     * 隐藏输入法
     * @param view
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) App.context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
