package learning.moliying.com.bookstore.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.CategoryFragmentAdapter;
import learning.moliying.com.bookstore.ui.MainActivity;
import learning.moliying.com.bookstore.vo.Categroy;

/**
 * Created by Administrator on 2016/8/15.
 */
public class DiscoverFragment extends BaseFragment {
    private static final int INIT_FRAGMENT = 0X1;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private LinearLayout search_layout;
    private LinearLayout progressBar_loading;
    private ViewPager viewPager;

    private MainActivity activity;
    private List<Categroy> categroys;
    private List<Fragment> fragments=new ArrayList<>();
    private List<String> titles=new ArrayList<>();
    private CategoryFragmentAdapter adapter;
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        progressBar_loading = (LinearLayout) view.findViewById(R.id.progressBar_loading);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        initData();
        return view;
    }


    public void initData(){
        progressBar_loading.setVisibility(View.VISIBLE);
        BmobQuery<Categroy> query = new BmobQuery<>();
        query.findObjects(activity, new FindListener<Categroy>() {
            @Override
            public void onSuccess(List<Categroy> list) {
                categroys = list;
                initFragment();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("FAG",s);
            }
        });
    }

    public void initFragment(){
        progressBar_loading.setVisibility(View.GONE);
        for (Categroy categroy : categroys) {
            String categroyId = categroy.getObjectId();
            CategoryFragment categoryFragment=CategoryFragment.newInstance(categroyId);
            String categoryName = categroy.getCategoryName();
            titles.add(categoryName);
            fragments.add(categoryFragment);
        }
        adapter = new CategoryFragmentAdapter(activity.getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
    }



}
