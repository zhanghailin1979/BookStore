package learning.moliying.com.bookstore.ui;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.ComponentFragmentAdapter;
import learning.moliying.com.bookstore.ui.fragment.DiscoverFragment;
import learning.moliying.com.bookstore.ui.fragment.HomeFragment;
import learning.moliying.com.bookstore.ui.fragment.PersonFragment;
import learning.moliying.com.bookstore.vo._User;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    //Pager与Fragment组件
    private ViewPager mViewPager;
    public HomeFragment mHomeFragment;
    public DiscoverFragment mDiscoverFragment;
    public PersonFragment mPersonFragment;
    private ComponentFragmentAdapter cfAdapter;
    //Fragment数组
    ArrayList<Fragment> fragments=new ArrayList<>();

    //RadioGroup组件
    private RadioButton home_button,find_button,person_button;
    private ArrayList<RadioButton> radioButtons=new ArrayList<>();

    //当前用户
    _User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_main);
        initUi();
        initFragment();
    }


    //初始化Ui组件
    private void initUi() {
        mViewPager=(ViewPager) findViewById(R.id.main_ViewPager);
        home_button = (RadioButton) findViewById(R.id.home_button);
        home_button.setChecked(true);
        find_button = (RadioButton) findViewById(R.id.find_button);
        person_button = (RadioButton) findViewById(R.id.person_button);
        home_button.setOnCheckedChangeListener(this);
        find_button.setOnCheckedChangeListener(this);
        person_button.setOnCheckedChangeListener(this);
        radioButtons.add(home_button);
        radioButtons.add(find_button);
        radioButtons.add(person_button);
    }

    //初始化Fragment
    private void initFragment() {
        mHomeFragment =HomeFragment.newInstance();
        mDiscoverFragment=DiscoverFragment.newInstance();
        mPersonFragment=PersonFragment.newInstance();
        fragments.add(mHomeFragment);
        fragments.add(mDiscoverFragment);
        fragments.add(mPersonFragment);
        cfAdapter = new ComponentFragmentAdapter(getSupportFragmentManager(),this,fragments);
        mViewPager.setAdapter(cfAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        changeTab(home_button);
                        break;
                    case 1:
                        changeTab(find_button);
                        break;
                    case 2:
                        changeTab(person_button);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //改变RadioButton选中状态
    private void changeTab(RadioButton home_button) {
        home_button.setChecked(true);
    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            int index=radioButtons.indexOf(buttonView);
            mViewPager.setCurrentItem(index);
        }

    }

}
