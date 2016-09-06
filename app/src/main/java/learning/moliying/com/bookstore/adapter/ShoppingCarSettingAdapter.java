package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.ui.ShoppingCarActivity;
import learning.moliying.com.bookstore.utils.NumberUtils;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Orders;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ShoppingCarSettingAdapter extends BaseAdapter {
    private static final String TAG = "ShoppingCarSettingAdapter";
    private Context context;
    private List<Orders> list;

    public ShoppingCarSettingAdapter(Context context, List<Orders> list) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_car_setting_item,null);
        }
        final SimpleDraweeView icon= (SimpleDraweeView) ViewHolder.getView(convertView,R.id.imageView_book2);
        TextView tv_name= (TextView) ViewHolder.getView(convertView,R.id.textView6_bookName);
        TextView tv_subtotal= (TextView) ViewHolder.getView(convertView,R.id.textView7_subtotal);
        ImageView button_delete= (ImageView) ViewHolder.getView(convertView,R.id.button_delete);

        final Orders orders = list.get(position);
        //显示图片
        String bookId=orders.getBookInfoId();
        BmobQuery<BookInfo> query = new BmobQuery<>();
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

        tv_name.setText(orders.getBookName());
        tv_subtotal.setText("￥"+ NumberUtils.format(orders.getSubtotal()));
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.delete(context);
                ((ShoppingCarActivity)context).setTotalAmountToUI_AndDataBase(orders);
            }
        });



        return convertView;
    }




}
