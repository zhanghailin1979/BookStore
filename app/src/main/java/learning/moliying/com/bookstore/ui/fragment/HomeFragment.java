package learning.moliying.com.bookstore.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.BookListAdapter;
import learning.moliying.com.bookstore.ui.BookDetailActivity;
import learning.moliying.com.bookstore.ui.MainActivity;
import learning.moliying.com.bookstore.ui.ShoppingCarActivity;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.ImageLooper;
import learning.moliying.com.bookstore.vo._User;

/**
 * Created by Administrator on 2016/8/15.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    private static final int ADD_BOOK_DATA = 0X2;
    private static final int LOADING_DATA=0X1;
    private static final String TAG = "HomeFragment";
    private ImageIndicatorView imageIndicatorView;
    private MainActivity activity;
    private PullToRefreshListView pull_refresh_list;
    private BookListAdapter bookListAdapter;
    private List<BookInfo> bookInfos = new ArrayList<>();
    private ProgressBar progressBar;
    private static  int count=0;
    private ImageView shopping_car;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        imageIndicatorView = (ImageIndicatorView) view.findViewById(R.id.indicate_view);
        pull_refresh_list = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        shopping_car= (ImageView) view.findViewById(R.id.shopping_car); //查找购物车组件
        shopping_car.setOnClickListener(this);  //给购物车设置点击事件
        setImageIndicatorLoop();//设置轮播方法并并加载图片
        bookListAdapter = new BookListAdapter(activity,bookInfos);
        pull_refresh_list.setAdapter(bookListAdapter);
        refresh();
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        pull_refresh_list.setOnItemClickListener(this);
        return view;
    }
    //加载数据
    private void refresh() {
        progressBar.setVisibility(View.VISIBLE);
        BmobQuery<BookInfo> query = new BmobQuery<BookInfo>();
        query.setLimit(10);
        query.setSkip(count);
        query.findObjects(activity, new FindListener<BookInfo>() {
            @Override
            public void onSuccess(List<BookInfo> list) {
                for (BookInfo bookInfo : list) {
                    Log.i("TAG",bookInfo.getBookName());
                    bookInfos.add(bookInfo);
                }
                handler.sendEmptyMessage(ADD_BOOK_DATA);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(activity, "数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setImageIndicatorLoop() {

        final Integer[] resArray = new Integer[] { R.mipmap.qdzt, R.mipmap.qinghuabiancheng,R.mipmap.scala };
        imageIndicatorView.setupLayoutByDrawable(resArray);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imageIndicatorView.show();

        AutoPlayManager autoBrocastManager =  new AutoPlayManager(imageIndicatorView);
        autoBrocastManager.setBroadcastEnable(true);
//        autoBrocastManager.setBroadCastTimes(5);//loop times
        autoBrocastManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoBrocastManager.loop();



    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ADD_BOOK_DATA:
                    progressBar.setVisibility(View.GONE);
                    bookListAdapter.setList(bookInfos);
                    bookListAdapter.notifyDataSetChanged();
                    count+=10;
                    return;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: "+position);
        BookInfo bookInfo= bookInfos.get(position-1);
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookInfo",bookInfo);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopping_car:
                if(currentUser==null){
                    Toast.makeText(getActivity(), "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), ShoppingCarActivity.class);
                intent.putExtra("user",currentUser);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取当前用户
     */
    _User currentUser;
    @Override
    public void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(getActivity(),_User.class);
    }
}
