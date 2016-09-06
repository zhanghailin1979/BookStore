package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.PayOrdersAdapter;
import learning.moliying.com.bookstore.vo.Orders;

public class OrdersActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private static final int ALL_ORDERS = 0X1;
    private static final int WAIT_ORDERS = 0X2;
    private static final int WAIT_RECEIPT = 0X3;
    private static final int WAIT_COMMENT = 0X5;
    private static final String TAG = "OrdersActivity";
    private static final int AFTER_COMMENT = 0x6;
    private int status;
    private String userId;

    private ListView listView_orders;
    private List<Orders> ordersList;
    private PayOrdersAdapter adapter;

    private TextView tv_count;
    private TextView tv_sum;
    private Button button;

    private int total = 0;
    private double sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_orders);
        Intent intent = getIntent();
        status = intent.getIntExtra("orderStatus", 0);
        userId = intent.getStringExtra("userId");
        listView_orders = (ListView) findViewById(R.id.listView_orders);
        showOrders(status);
        tv_count = (TextView) findViewById(R.id.textView8_count);
        tv_sum = (TextView) findViewById(R.id.textView8_sum);
        button = (Button) findViewById(R.id.button_Settlement8);
        button.setOnClickListener(this);

    }

    private void showOrders(int status) {
        switch (status) {
            case ALL_ORDERS:
                queryAllOrders();
                break;
            case WAIT_ORDERS:
                queryWaitOrders();
                break;
            case WAIT_RECEIPT:
                queryReceipt();
                break;
            case WAIT_COMMENT:
                queryComment();
                break;
        }


    }


    /**
     * 待评价
     */
    private void queryComment() {
        BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        query.addWhereEqualTo("status", 5);
        query.order("-createdAt");
        queryOrders(query);
    }


    /**
     * 待收货
     */
    private void queryReceipt() {
        BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        query.addWhereEqualTo("status", 3);
        query.order("-createdAt");
        queryOrders(query);
    }


    /**
     * 查询全部订单
     */
    private void queryAllOrders() {
        BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        query.addWhereNotEqualTo("status", 1);
        query.order("-createdAt");
        queryOrders(query);

    }


    //代付款
    private void queryWaitOrders() {
        BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        query.addWhereEqualTo("status", 2);
        query.order("-createdAt");
        queryOrders(query);
    }


    private void queryOrders(BmobQuery<Orders> query) {
        query.findObjects(this, new FindListener<Orders>() {
            @Override
            public void onSuccess(List<Orders> list) {
                if (list.size() == 0) {
                    toast("暂无订单数据");
                    return;
                }
                ordersList = list;
                adapter = new PayOrdersAdapter(OrdersActivity.this, ordersList);
                listView_orders.setAdapter(adapter);
                listView_orders.setOnItemClickListener(OrdersActivity.this);

            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: " + s);
            }
        });
    }

    private void toast(String info) {
        Toast.makeText(OrdersActivity.this, info, Toast.LENGTH_SHORT).show();
    }


    /**
     * 返回上一页
     */
    public void backMainClick2(View v) {
        finish();
    }


    public void updateUi(Orders orders) {
        ordersList.remove(orders);
        orders.delete(this);
        adapter.setList(ordersList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //结算
            case R.id.button_Settlement8:
                if (total == 0) {
                    toast("暂无付款订单");
                    return;
                }
                if(orderses.size()>0){
                    Intent intent = new Intent(this,PayActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("orderses",orderses);
                    startActivity(intent);
                }
                break;
        }
    }

    private ArrayList<Orders> orderses = new ArrayList<>();
    public void addCheckUi(Orders orders) {
        Log.i(TAG, "addCheckUi: " + orders.getDiscountPrice());
        total += 1;
        sum += orders.getDiscountPrice();
        tv_count.setText("共有" + total + "件商品");
        tv_sum.setText("金额:" + sum + "元");
        orderses.add(orders);
    }

    public void deleteCheckUi(Orders orders) {
        total -= 1;
        sum -= orders.getDiscountPrice();
        tv_count.setText("共有" + total + "件商品");
        tv_sum.setText("金额:" + sum + "元");
        orderses.remove(orders);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: "+position);
        Orders orders = ordersList.get(position);
        Log.i(TAG, "onItemClick: "+orders.getStatus());
        Log.i(TAG, "onItemClick: "+orders.getBookName());
        int status = orders.getStatus();
        switch (status){
            //待付款
            case WAIT_ORDERS:
                ArrayList<Orders> list = new ArrayList<>();
                list.add(orders);
                Intent intent1=new Intent(this,PayActivity.class);
                intent1.putExtra("orderses",list);
                startActivity(intent1);
                break;
            case WAIT_RECEIPT:
                Intent intent2 = new Intent(this,OrderDetailsActivity.class);
                intent2.putExtra("orders",orders);
                startActivity(intent2);

                break;
            case WAIT_COMMENT:
                Intent intent3 = new Intent(this,EvaluationActivity.class);
                intent3.putExtra("orders",orders);
                startActivity(intent3);
                break;
            case AFTER_COMMENT:
                Intent intent4 = new Intent(this,OrderDetailsActivity.class);
                intent4.putExtra("orders",orders);
                startActivity(intent4);
                break;
        }

    }
}
