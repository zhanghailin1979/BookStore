package learning.moliying.com.bookstore.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.BookListAdapter;
import learning.moliying.com.bookstore.ui.BookDetailActivity;
import learning.moliying.com.bookstore.vo.BookInfo;

/**
 * Created by Administrator on 2016/8/16.
 */
public class CategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private List<BookInfo> bookInfos;
    private BookListAdapter adapter;
    private PullToRefreshListView pullToRefreshListView;
    private LinearLayout categroy_loading;

    public static CategoryFragment newInstance(String categoryId) {
        Bundle args = new Bundle();
        args.putCharSequence("categoryId", categoryId);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list2);
        categroy_loading = (LinearLayout) view.findViewById(R.id.categroy_loading);
        categroy_loading.setVisibility(View.VISIBLE);
        Bundle arguments = getArguments();
        String categoryId = arguments.getCharSequence("categoryId").toString();
        queryData(categoryId);
        return view;
    }

    private void queryData(String categoryId) {
        BmobQuery<BookInfo> query = new BmobQuery<BookInfo>();
        query.addWhereEqualTo("categoryId", categoryId);
        query.findObjects(getActivity(), new FindListener<BookInfo>() {
            @Override
            public void onSuccess(List<BookInfo> list) {
                bookInfos = list;
                setAdapterData();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("Tag", "" + s);
            }
        });
    }

    private void setAdapterData() {
        categroy_loading.setVisibility(View.GONE);
        adapter = new BookListAdapter(getActivity(), bookInfos);
        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookInfo bookInfo= bookInfos.get(position-1);
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookInfo",bookInfo);
        startActivity(intent);
    }
}
