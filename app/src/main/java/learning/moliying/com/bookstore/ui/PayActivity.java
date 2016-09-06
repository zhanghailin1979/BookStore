package learning.moliying.com.bookstore.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.OrdersAdapter;
import learning.moliying.com.bookstore.utils.NumberUtils;
import learning.moliying.com.bookstore.vo.Address;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

public class PayActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PayActivity";
    private static final int ADDRESS_CODE = 0X1 << 3;
    /*
        用户信息组件
         */
    private TextView textView4_username, textView4_phone, textView4_address;
    private RelativeLayout relativeLayout2_address;
    private Address address;

    /**
     * 支付按钮
     */
    private RadioGroup radioGroup_pay;

    /**
     * 订单ListView组件
     *
     * @param savedInstanceState
     */
    private ListView listView;
    private _User currentUser;
    private OrdersAdapter adapter;
    private List<Orders> listOrders;

    /**
     * 底部总金额 和支付按钮
     *
     * @param savedInstanceState
     */
    private TextView textView5_total_amount;
    private Button button_payment;
    private double money;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_pay);
        currentUser = BmobUser.getCurrentUser(this, _User.class);

        initUserInfo();     //初始化用户信息组件

        initPay();          //初始化支付组件
        initButtom();       //初始化底部总金额和支付组件

        Intent intent = getIntent();
        List<Orders> orderses = (List<Orders>) intent.getSerializableExtra("orderses");
        if (orderses != null) {
            listView = (ListView) findViewById(R.id.listView2_orders);
            listOrders = orderses;
            adapter = new OrdersAdapter(this, listOrders);
            listView.setAdapter(adapter);
            upBottomTotalUi();
        } else {
            initOrders();       //初始化订单ListView
        }




    }


    /**
     * 初始化用户信息组件
     */
    private void initUserInfo() {
        textView4_username = (TextView) findViewById(R.id.textView4_username);
        textView4_phone = (TextView) findViewById(R.id.textView4_phone);
        textView4_address = (TextView) findViewById(R.id.textView4_address);
        relativeLayout2_address = (RelativeLayout) findViewById(R.id.relativeLayout2_address);
        relativeLayout2_address.setOnClickListener(this);
        String userId = currentUser.getObjectId();
        BmobQuery<Address> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        query.addWhereEqualTo("isDefault", true);
        query.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> list) {
                //先查看是否有默认地址，若没有，再查询是否有普通地址
                if (list.size() != 0) {
                    address = list.get(0);
                    textView4_username.setText(address.getUsername());
                    textView4_phone.setText(address.getPhoneNumber());
                    textView4_address.setText(address.getAddress());
                    return;
                } else {
                    String userId = currentUser.getObjectId();
                    BmobQuery<Address> query = new BmobQuery<>();
                    query.addWhereEqualTo("userId", userId);
                    query.order("-createdAt");
                    query.setLimit(1);
                    query.findObjects(PayActivity.this, new FindListener<Address>() {
                        @Override
                        public void onSuccess(List<Address> list) {
                            if (list.size() > 0) {
                                address = list.get(0);
                                textView4_username.setText(address.getUsername());
                                textView4_phone.setText(address.getPhoneNumber());
                                textView4_address.setText(address.getAddress());
                                return;
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.i(TAG, "onError: " + s);
                        }
                    });

                }

            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: " + s);
            }
        });


    }


    //初始化支付组件
    private void initPay() {
        radioGroup_pay = (RadioGroup) findViewById(R.id.radioGroup_pay);
    }


    //初始化订单组件
    private void initOrders() {
        listView = (ListView) findViewById(R.id.listView2_orders);
        final BmobQuery<Orders> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", currentUser.getObjectId());
        query.addWhereEqualTo("status", 2);
        query.order("-createdAt");
        query.count(this, Orders.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i == 0) return;
                query.findObjects(PayActivity.this, new FindListener<Orders>() {
                    @Override
                    public void onSuccess(List<Orders> list) {
                        listOrders = list;
                        adapter = new OrdersAdapter(PayActivity.this, listOrders);
                        listView.setAdapter(adapter);
                        upBottomTotalUi();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onError: " + s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onError: " + s);
            }
        });

    }


    /**
     * 初始化底部组件
     */

    private void initButtom() {
        textView5_total_amount = (TextView) findViewById(R.id.textView5_total_amount);
        button_payment = (Button) findViewById(R.id.button_payment);
        button_payment.setOnClickListener(this);
    }

    /**
     * 删除更新操作
     */
    public void deleteUpdateUi(Orders orders) {
        listOrders.remove(orders);
        adapter.notifyDataSetChanged();
        upBottomTotalUi();
    }


    //更新底部Ui组件
    public void upBottomTotalUi() {
        textView5_total_amount.setText("合计: " + NumberUtils.format(totalMoney()) + "元");
        if (listOrders.size() == 0) {
            button_payment.setClickable(false);
        } else {
            button_payment.setClickable(true);
        }

    }

    private double totalMoney() {
        double total = 0;
        for (Orders listOrder : listOrders) {
            total += listOrder.getSubtotal();
        }
        return total;
    }


    /**
     * 点击事件接口回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //用户信息管理点击
            case R.id.relativeLayout2_address:
                Intent intent = new Intent(PayActivity.this, AddressManagerActivity.class);
                intent.putExtra("userId", currentUser.getObjectId());
                startActivityForResult(intent, ADDRESS_CODE);
                break;

            //支付按钮
            case R.id.button_payment:
                payMoney();
                break;


        }

    }


    /**
     * 支付
     */
    private void payMoney() {
        if (address == null) {
            Toast.makeText(PayActivity.this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取被选中的单选组件的ID
        int id = radioGroup_pay.getCheckedRadioButtonId();
        switch (id) {
            case R.id.radioButton_alipay:
                pay(true);
                break;
            case R.id.radioButton2_weixinpay:
                pay(false);
                break;
        }


    }
    //支付
    private void pay(boolean flag) {
        money = (Math.round(totalMoney() * 100)) / 100;
        String username = textView4_username.getText().toString();
        String description = new StringBuffer().append(username).
                append("付款").append(0.1).append("元,").append("购买").
                append(listOrders.size()).append("件商品,").append("电话:").
                append(textView4_phone.getText().toString()).append(",地址:").
                append(textView4_address.getText().toString()).toString();
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        BP.pay(username, description, money, flag, new PListener() {
            @Override
            public void orderId(String s) {
                orderId = s;
                Log.i(TAG, "orderId: " + s);
            }

            @Override
            public void succeed() {
                showToast("付款成功");
                Log.i(TAG, "succeed: ");
                updateOrders();
            }

            @Override
            public void fail(int i, String s) {
                Log.i(TAG, "fail: " + i);
                Log.i(TAG, "fail: " + s);
                if(i==-3){
                    installWeiPay();
                }
            }

            @Override
            public void unknow() {
                Log.i(TAG, "unknow: ");
            }
        });
    }

    private void installWeiPay() {
        new AlertDialog.Builder(PayActivity.this)
                .setMessage(
                        "监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
                .setPositiveButton("安装",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                installBmobPayPlugin("bp.db");
                            }
                        })
                .setNegativeButton("支付宝支付",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                pay(true);
                            }
                        }).create().show();
    }

    private void installBmobPayPlugin(String bmobpay) {
            try {
                InputStream is = getAssets().open(bmobpay);
                File file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "BmobPayPlugin.apk");
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] temp = new byte[1024];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
            fos.close();
            is.close();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //更新订单
    private void updateOrders() {
        for (Orders listOrder : listOrders) {
            listOrder.setOrderId(orderId);
            listOrder.setBuyDate(NumberUtils.getCurrentDate(new Date()));
            listOrder.setFreight(0);
            listOrder.setStatus(3);
            listOrder.update(PayActivity.this);
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void showToast(String info) {
        Toast.makeText(PayActivity.this, info, Toast.LENGTH_SHORT).show();
    }


    /**
     * 返回按钮
     */
    public void backMainClick2(View v) {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ADDRESS_CODE == requestCode && resultCode == RESULT_OK) {
            address = (Address) data.getSerializableExtra("address");
            textView4_username.setText(address.getUsername());
            textView4_phone.setText(address.getPhoneNumber());
            textView4_address.setText(address.getAddress());
        }
    }
}
