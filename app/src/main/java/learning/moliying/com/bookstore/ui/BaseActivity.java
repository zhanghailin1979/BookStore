package learning.moliying.com.bookstore.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

import learning.moliying.com.bookstore.App;

/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        App.screenWidth = dm.widthPixels;
        App.screenHeight = dm.heightPixels;
    }



    long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis()-exitTime>2000){
                shwoToast("再按一次推出");
                exitTime=System.currentTimeMillis();
            }else {
                closeALL();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeALL() {
        for (Activity activity : App.activities) {
            activity.finish();
        }
        App.release();
    }

    private void shwoToast(String info) {
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        App.activities.remove(this);
        super.onDestroy();
    }
}
