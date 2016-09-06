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

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.ShoppingCarListAdapter;
import learning.moliying.com.bookstore.adapter.ShoppingCarSettingAdapter;
import learning.moliying.com.bookstore.utils.NumberUtils;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

public class ShoppingCarActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShoppingCarActivity";
    private Button editButton, completeButton, button_Settlement;
    private TextView textView5_count, textView4_sum;
    private ListView listView;
    private _User user;
    private List<Orders> listOrders = new ArrayList<>();
    private ShoppingCarListAdapter adapter_complete;
    private ShoppingCarSettingAdapter adapter_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_shopping_car);
        Intent intent = getIntent();
        user = (_User) intent.getSerializableExtra("user");
        initTitleBar();
        initMiddle();
        initBottom();
        queryOrders();


    }


    /**
     * 查询未支付订单是否存在
     */
    private void queryOrders() {
        final BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", user.getObjectId());
        query.addWhereEqualTo("status", 1);
        query.order("-createdAt");
        query.count(this, Orders.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i == 0) {
                    toast("暂无订单数据");
                    button_Settlement.setClickable(false);
                    return;
                }
                button_Settlement.setClickable(true);
                query.findObjects(ShoppingCarActivity.this, new FindListener<Orders>() {
                    @Override
                    public void onSuccess(List<Orders> list) {
                        listOrders = list;
                        adapter_complete = new ShoppingCarListAdapter(ShoppingCarActivity.this, listOrders);
                        adapter_setting = new ShoppingCarSettingAdapter(ShoppingCarActivity.this, listOrders);
                        listView.setAdapter(adapter_complete);
                        //注册监听事件
                        setTotalAmountToUI();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onFailure: " + s);
                    }
                });


            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: " + s);
            }
        });

    }


    //更新数据并显示


    /**
     * 初始化顶部组件
     */
    private void initTitleBar() {
        editButton = (Button) findViewById(R.id.editButton);
        completeButton = (Button) findViewById(R.id.completeButton);
        editButton.setOnClickListener(this);
        completeButton.setOnClickListener(this);
    }


    /**
     * 初始化ListView
     * ListView事件
     */
    private void initMiddle() {
        listView = (ListView) findViewById(R.id.listView2_shopping_cart);


    }


    /**
     * 初始化底部组件
     */
    private void initBottom() {
        button_Settlement = (Button) findViewById(R.id.button_Settlement);
        button_Settlement.setOnClickListener(this);
        textView5_count = (TextView) findViewById(R.id.textView5_count);
        textView4_sum = (TextView) findViewById(R.id.textView4_sum);
    }


    /**
     * 返回
     */
    public void backMainClick(View v) {
        finish();
    }


    /**
     * 实现点击接口回调事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //编辑按钮
            case R.id.editButton:
                editButton.setVisibility(View.GONE);
                completeButton.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter_setting);
                break;
            //完成按钮
            case R.id.completeButton:
                editButton.setVisibility(View.VISIBLE);
                completeButton.setVisibility(View.GONE);
                listView.setAdapter(adapter_complete);
                break;

            //跳转结算页面
            case R.id.button_Settlement:
                if (listOrders.size() == 0) {
                    Toast.makeText(ShoppingCarActivity.this, "购物车暂无商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Orders listOrder : listOrders) {
                    listOrder.setStatus(2);
                    listOrder.update(this);
                    listOrders.remove(listOrder);
                }
                adapter_setting.notifyDataSetChanged();
                adapter_complete.notifyDataSetChanged();
                setTotalAmountToUI();
                Intent intent = new Intent(this, PayActivity.class);
                Bundle bundle = new Bundle();
                startActivity(intent);
                break;
        }
    }


    private void toast(String info) {
        Toast.makeText(ShoppingCarActivity.this, info, Toast.LENGTH_SHORT).show();
    }


    /**
     * 删除订单更新数据库
     */
    public void setTotalAmountToUI_AndDataBase(Orders orders) {
        listOrders.remove(orders);
        adapter_complete.notifyDataSetChanged();
        adapter_setting.notifyDataSetChanged();
        setTotalAmountToUI();
    }


    /**
     * 点击添加删除 更新Ui
     */
    //设置总金额到UI组件
    public void setTotalAmountToUI() {
        double totalAmount = getTotalAmount();
        textView4_sum.setText("金额:" + NumberUtils.format(totalAmount) + "元");
        setProductCount();
        if (totalAmount <= 0) {
            button_Settlement.setEnabled(false);
        } else {
            button_Settlement.setEnabled(true);
        }
    }

    //设置叫商品数
    public void setProductCount() {
        textView5_count.setText("共" + listOrders.size() + "件商品");
    }

    //计算总金额
    private double getTotalAmount() {
        double totalAmount = 0;
        Orders orders = null;
        for (int i = 0; i < listOrders.size(); i++) {
            orders = listOrders.get(i);
            totalAmount += orders.getSubtotal();
        }
        return totalAmount;
    }

}
