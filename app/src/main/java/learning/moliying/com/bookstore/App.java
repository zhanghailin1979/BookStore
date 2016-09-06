package learning.moliying.com.bookstore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.LinkedList;

import c.b.BP;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/8/15.
 */
public class App extends Application{
    public static Context context;
    public static LinkedList<Activity> activities = new LinkedList<>();
    public static int screenWidth,screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"ccdeac6cc73c963e067bb9a83400b688");
        BP.init(this,"ccdeac6cc73c963e067bb9a83400b688");

        context = getApplicationContext();
        Fresco.initialize(context);
    }
    public static void release(){
        activities = null;
        context = null;
    }



}
