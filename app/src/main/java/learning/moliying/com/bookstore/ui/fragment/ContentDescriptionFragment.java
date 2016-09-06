package learning.moliying.com.bookstore.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import learning.moliying.com.bookstore.R;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ContentDescriptionFragment extends BaseFragment {
    private WebView webView;
    public static ContentDescriptionFragment newInstance(String content) {

        Bundle args = new Bundle();
        args.putString("content",content);
        ContentDescriptionFragment fragment = new ContentDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_description,container,false);
        webView = (WebView) view.findViewById(R.id.webView_book);
        Bundle arguments = getArguments();
        String content = arguments.getString("content");
        webView.loadDataWithBaseURL("", content, "text/html", "utf8", null);
        return view;
    }
}
