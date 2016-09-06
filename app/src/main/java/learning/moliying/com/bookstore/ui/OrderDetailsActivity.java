package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.vo.Address;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

public class OrderDetailsActivity extends BaseActivity {
    private static final String TAG = "OrderDetailsActivity";
    Orders orders;
    private _User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_order_details);
        Intent intent = getIntent();
        orders = (Orders) intent.getSerializableExtra("orders");
        currentUser = BmobUser.getCurrentUser(this,_User.class);
        initUi();
        setText();


    }


    /**
     * 初始化组件
     */
    private TextView textView6_order_status,textView6_buyDate,textView6_order_id,
            textView6_address,textView6_username,textView5_bookname,textView5_count,
            textView5_discount_price,textView5_total;
    private SimpleDraweeView textView5_bookimage;
    private void initUi() {
        textView6_order_status = (TextView) findViewById(R.id.textView6_order_status);
        textView6_buyDate = (TextView) findViewById(R.id.textView6_buyDate);
        textView6_order_id = (TextView) findViewById(R.id.textView6_order_id);
        textView6_address = (TextView) findViewById(R.id.textView6_address);
        textView6_username = (TextView) findViewById(R.id.textView6_username);

        textView5_bookimage = (SimpleDraweeView) findViewById(R.id.textView5_bookimage);
        textView5_bookname = (TextView) findViewById(R.id.textView5_bookname);
        textView5_count = (TextView) findViewById(R.id.textView5_count);
        textView5_discount_price = (TextView) findViewById(R.id.textView5_discount_price);
        textView5_total = (TextView) findViewById(R.id.textView5_total);


    }

    /**
     * 填值
     */
    private void setText() {
        if(orders.getStatus()==3){
            textView6_order_status.setText("未发货");
        }else if(orders.getStatus()==4){
            textView6_order_status.setText("已发货");
        }else if(orders.getStatus()==6){
            textView6_order_status.setText("已评价");
        }
        textView6_buyDate.setText(orders.getCreatedAt());
        textView6_order_id.setText(orders.getOrderId());
        BmobQuery<Address> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser.getObjectId());
        query.addWhereEqualTo("isDefault",true);
        query.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> list) {
                if(list.size()!=0){
                    Address address = list.get(0);
                    textView6_address.setText(address.getAddress());
                    textView6_username.setText(address.getUsername());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        textView5_bookname.setText(orders.getBookName());
        textView5_count.setText("x"+orders.getTotal());
        textView5_discount_price.setText("￥"+orders.getDiscountPrice());
        textView5_total.setText("订单总额："+orders.getSubtotal()+"元");
        final String bookInfoId = orders.getBookInfoId();
        BmobQuery<BookInfo> qu=new BmobQuery<BookInfo>();
        qu.getObject(this, bookInfoId, new GetListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                textView5_bookimage.setImageURI(bookInfo.getBookImage().getUrl());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });


    }




    /**
     * 返回
     */
    public void backMainClick2(View v) {
        finish();
    }


}
