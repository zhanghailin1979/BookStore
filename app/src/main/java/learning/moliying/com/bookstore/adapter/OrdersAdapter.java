package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.ui.PayActivity;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Orders;

/**
 * Created by Administrator on 2016/8/18.
 */
public class OrdersAdapter extends BaseAdapter {
    private static final String TAG = "OrdersAdapter";
    private Context context;
    private List<Orders> list;

    public OrdersAdapter(Context context, List<Orders> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Orders> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.pay_order_list_item,null);
        }
        final Orders orders = list.get(position);
        final SimpleDraweeView icon= (SimpleDraweeView) ViewHolder.getView(convertView,R.id.pay_bookImage);
        TextView tv_name= (TextView) ViewHolder.getView(convertView,R.id.textView5_bookname);
        TextView tv_total= (TextView) ViewHolder.getView(convertView,R.id.textView5_count_total);
        TextView tv_price= (TextView) ViewHolder.getView(convertView,R.id.textView6_discount_price);
        ImageView delButton = (ImageView) ViewHolder.getView(convertView,R.id.button_delete2);
        tv_name.setText(orders.getBookName());
        tv_total.setText("x"+orders.getTotal());
        tv_price.setText(orders.getDiscountPrice()+"元");

        String bookId = orders.getBookInfoId();
        BmobQuery<BookInfo> query = new BmobQuery<BookInfo>();
        query.getObject(context, bookId, new GetListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                icon.setImageURI(bookInfo.getBookImage().getUrl());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.delete(context);
                ((PayActivity)context).deleteUpdateUi(orders);
            }
        });


        return convertView;
    }
}
