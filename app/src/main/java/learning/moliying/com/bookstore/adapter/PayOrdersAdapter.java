package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.ui.MainActivity;
import learning.moliying.com.bookstore.ui.OrdersActivity;
import learning.moliying.com.bookstore.utils.Constant;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Orders;

/**
 * Created by Administrator on 2016/8/20.
 */
public class PayOrdersAdapter extends BaseAdapter {
    private static final String TAG = "PayOrdersAdapter";
    private Context context;
    private List<Orders> list;

    public PayOrdersAdapter(Context context, List<Orders> list) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_orders_item,null);
        }
        final Orders orders = list.get(position);

        if(orders.getStatus()==2){
            TextView tv = (TextView) ViewHolder.getView(convertView,R.id.textView6_Id);
            tv.setVisibility(View.GONE);
            CheckBox checkBox= (CheckBox) ViewHolder.getView(convertView,R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ((OrdersActivity)context).addCheckUi(orders);
                    }else{
                        ((OrdersActivity)context).deleteCheckUi(orders);
                    }
                }
            });


        }else{
            CheckBox checkBox= (CheckBox) ViewHolder.getView(convertView,R.id.checkBox);
            checkBox.setVisibility(View.GONE);
            TextView tv = (TextView) ViewHolder.getView(convertView,R.id.textView6_Id);
            String code=tv.getText().toString();
            tv.setText(code+orders.getOrderId());

        }
        final SimpleDraweeView image= (SimpleDraweeView) ViewHolder.getView(convertView,R.id.pay_bookImage2);
        TextView tv_name= (TextView) ViewHolder.getView(convertView,R.id.textView5_bookname);
        TextView tv_price= (TextView) ViewHolder.getView(convertView,R.id.textView6_discount_price);
        TextView tv_count= (TextView) ViewHolder.getView(convertView,R.id.textView5_count_total);
        TextView tv_pay= (TextView) ViewHolder.getView(convertView,R.id.textView5);
        ImageView button = (ImageView) ViewHolder.getView(convertView,R.id.button_delete2);
        switch (orders.getStatus()){
            case Constant.ORDER_NON_PAYMENT:
                tv_pay.setText("未付款");
                break;
            case Constant.ORDER_PAYMENTS_RECEIVED:
                tv_pay.setText("已付款");
                break;
            case Constant.ORDER_DELIVERED:
                tv_pay.setText("已发货");
                break;
            case Constant.ORDER_RECEIVED_GOODS:
                tv_pay.setText("待评价");
                break;
            case Constant.ORDER_HAS_BEEN_EVALUATED:
                tv_pay.setText("已评价");
                break;
        }
        String bookId = orders.getBookInfoId();
        new BmobQuery<BookInfo>().getObject(context, bookId, new GetListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                image.setImageURI(bookInfo.getBookImage().getUrl());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

        tv_name.setText(orders.getBookName());
        tv_price.setText("￥"+orders.getDiscountPrice());
        tv_count.setText("x"+orders.getTotal());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OrdersActivity)context).updateUi(orders);
            }
        });
        return convertView;
    }
}
