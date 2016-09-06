package learning.moliying.com.bookstore.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.vo.BookInfo;

/**
 * Created by Administrator on 2016/8/17.
 */
public class PublishDescriptionFragment extends BaseFragment {
    private static final String TAG = "PublishDescriptionFragment";
    private TextView textView5_bookname_content,
            textView5_isbn_content,
            textView5_author_content,
            textView5_publishing_content,
            textView5_publishingTime_content,
            textView5_revision_content,
            textView5_impression_content,
            textView5_pages_content,
            textView5_numberOfWords_content,
            textView5_folio_content,
            textView5_pager_content,
            textView5_packing_content;
    public static PublishDescriptionFragment newInstance(BookInfo bookInfo) {

        Bundle args = new Bundle();
        args.putSerializable("bookInfo",bookInfo);
        PublishDescriptionFragment fragment = new PublishDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_publish,container,false);
        BookInfo bookInfo = (BookInfo) getArguments().getSerializable("bookInfo");
        initUi(view,bookInfo);
        return view;
    }

    private void initUi(View view, BookInfo bookInfo) {
        textView5_bookname_content = (TextView) view.findViewById(R.id.textView5_bookname_content);
        textView5_isbn_content = (TextView) view.findViewById(R.id.textView5_isbn_content);
        textView5_author_content = (TextView) view.findViewById(R.id.textView5_author_content);
        textView5_publishing_content = (TextView) view.findViewById(R.id.textView5_publishing_content);
        textView5_publishingTime_content = (TextView) view.findViewById(R.id.textView5_publishingTime_content);
        textView5_revision_content = (TextView) view.findViewById(R.id.textView5_revision);
        textView5_impression_content = (TextView) view.findViewById(R.id.textView5_impression_content);
        textView5_pages_content = (TextView) view.findViewById(R.id.textView5_pages_content);
        textView5_numberOfWords_content = (TextView) view.findViewById(R.id.textView5_numberOfWords_content);
        textView5_folio_content = (TextView) view.findViewById(R.id.textView5_folio_content);
        textView5_pager_content = (TextView) view.findViewById(R.id.textView5_pager_content);
        textView5_packing_content = (TextView) view.findViewById(R.id.textView5_packing_content);

        //设置内容
        BookInfo book = bookInfo;
        textView5_bookname_content.setText(book.getBookName());
        textView5_isbn_content.setText(book.getISBN());
        textView5_author_content.setText(book.getAuthor());
        textView5_publishing_content.setText(book.getPublishingCompany());
        textView5_publishingTime_content.setText(book.getPublishingTime());
        textView5_revision_content.setText(String.valueOf(book.getRevision()));
        textView5_impression_content.setText(String.valueOf(book.getImpression()));
        textView5_pages_content.setText(String.valueOf(book.getPages()));
        textView5_numberOfWords_content.setText(String.valueOf(book.getNumberOfWords()));
        textView5_folio_content.setText(book.getFolio());
        textView5_pager_content.setText(book.getPaper());
        textView5_packing_content.setText(book.getPacking());
    }
}
