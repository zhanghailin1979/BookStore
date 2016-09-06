package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ComponentFragmentAdapter extends FragmentPagerAdapter {
    Context context;
    ArrayList<Fragment> fragments;

    public ComponentFragmentAdapter(FragmentManager fm, Context context,ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
