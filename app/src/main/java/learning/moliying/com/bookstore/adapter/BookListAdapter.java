package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.listener.DownloadFileListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.BookInfo;

/**
 * Created by Administrator on 2016/8/15.
 */
public class BookListAdapter extends BaseAdapter {
    private Context context;
    private List<BookInfo> list;
    public BookListAdapter(Context context,List<BookInfo> list){
        this.context=context;
        this.list=list;
    }

    public void setList(List<BookInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.book_list_item,null);
        }
        BookInfo bookInfo=list.get(position);
        final SimpleDraweeView imageView = (SimpleDraweeView) ViewHolder.getView(convertView,R.id.imageView_bookImage);
        TextView textView_bookName = (TextView) ViewHolder.getView(convertView,R.id.textView_bookName);
        TextView textView5_price = (TextView) ViewHolder.getView(convertView,R.id.textView5_price);
        TextView textView7_discountPrice = (TextView) ViewHolder.getView(convertView,R.id.textView7_discountPrice);
        TextView textView8_discount = (TextView) ViewHolder.getView(convertView,R.id.textView8_discount);
        textView_bookName.setText(bookInfo.getBookName());
        textView5_price.setText(String.valueOf(bookInfo.getPrice()));
        textView5_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textView7_discountPrice.setText(String.valueOf(bookInfo.getDiscountPrice()));
        textView8_discount.setText(String.valueOf(bookInfo.getDiscount())+"折");
        bookInfo.getBookImage().download(context, new DownloadFileListener() {
            @Override
            public void onSuccess(String s) {
                imageView.setImageURI(Uri.fromFile(new File(s)));
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
        return convertView;
    }
}
