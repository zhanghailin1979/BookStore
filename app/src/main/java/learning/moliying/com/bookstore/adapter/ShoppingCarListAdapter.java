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
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.ui.ShoppingCarActivity;
import learning.moliying.com.bookstore.utils.NumberUtils;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Orders;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ShoppingCarListAdapter extends BaseAdapter {
    private static final String TAG = "ShoppingCarListAdapter";
    private Context context;
    private List<Orders> list;

    public ShoppingCarListAdapter(Context context, List<Orders> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_item,null);
        }
        final Orders orders = list.get(position);
        final SimpleDraweeView image= (SimpleDraweeView) ViewHolder.getView(convertView,R.id.imageView_book);
        TextView tv_date = (TextView) ViewHolder.getView(convertView,R.id.textView_bookDate);
        final TextView tv_total = (TextView) ViewHolder.getView(convertView,R.id.textView7_subtotal); //总金额
        final TextView tv_bookName = (TextView) ViewHolder.getView(convertView,R.id.textView6_bookName);

        final ImageView bt_reduce = (ImageView) ViewHolder.getView(convertView,R.id.button3_reduce);
        ImageView bt_add = (ImageView) ViewHolder.getView(convertView,R.id.button4_add);
        final TextView tv_nums = (TextView) ViewHolder.getView(convertView,R.id.textView_total);  //总数量

        /**
         * 加载图片
         */
        String bookId=orders.getBookInfoId();
        BmobQuery<BookInfo> query = new BmobQuery<>();
        query.getObject(context, bookId, new GetListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                image.setImageURI(bookInfo.getBookImage().getUrl());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

        //显示日期
        tv_date.setText(orders.getCreatedAt());
        //显示总金额
        tv_total.setText( "￥"+NumberUtils.format(orders.getSubtotal()));
        //显示书名
        tv_bookName.setText(orders.getBookName());

        //显示购买数量
        tv_nums.setText(String.valueOf(orders.getTotal()));

        //注册加减事件
        bt_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total =Integer.parseInt(tv_nums.getText().toString());
                if(total==0){
                    v.setClickable(false);
                    return;
                }
                total = total-1;
                orders.setTotal(total);
                orders.setSubtotal(total*orders.getDiscountPrice());
                orders.update(context);         //更新并返回数据库
                tv_nums.setText(String.valueOf(total));
                tv_total.setText("￥"+NumberUtils.format(orders.getSubtotal()));
                ((ShoppingCarActivity)context).setTotalAmountToUI();
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total =Integer.parseInt(tv_nums.getText().toString());
                if(total>=100){
                    tv_nums.setText("100");       //单价商品最大购买数量为100
                    return;
                }else if(total==1){
                    bt_reduce.setClickable(true);
                }
                total=total+1;
                orders.setTotal(total);
                orders.setSubtotal(total*orders.getDiscountPrice());
                orders.update(context);         //更新并返回数据库
                tv_nums.setText(String.valueOf(total));
                tv_total.setText("￥"+ NumberUtils.format(orders.getSubtotal()));
                ((ShoppingCarActivity)context).setTotalAmountToUI();
            }
        });
        return convertView;
    }
}
