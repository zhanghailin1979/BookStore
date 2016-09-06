package learning.moliying.com.bookstore.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.AddressAdapter;
import learning.moliying.com.bookstore.vo.Address;

public class AddressManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "AddressManagerActivity";
    private ListView listView;
    String userId;
    List<Address> addresses;
    AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_address_manager);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        listView = (ListView) findViewById(R.id.listView);

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateList();


    }

    private void updateList() {
        BmobQuery<Address> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",userId);
        query.order("-isDefault,-createdAt");
        query.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> list) {
                if(list.size()==0){
                    toast("暂无地址信息");
                    return;
                }
                addresses = list;
                if(adapter==null){
                    adapter = new AddressAdapter(AddressManagerActivity.this,addresses);
                    listView.setOnItemClickListener(AddressManagerActivity.this);
                }else{
                    adapter.setAddresses(addresses);
                    adapter.notifyDataSetChanged();
                }
                listView.setAdapter(adapter);

            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: "+s);
            }
        });
    }

    /**
     * 添加收货地址
     */

    public void addAddressClick(View view){
        Intent intent = new Intent(AddressManagerActivity.this,AddAddressActivity.class);
        startActivity(intent);
    }




    /**
     * 返回
     */
    public void backMainClick2(View v){
        finish();
    }



    //Toast信息
    private void toast(String info){
        Toast.makeText(AddressManagerActivity.this,info,Toast.LENGTH_SHORT).show();
    }


    /**
     * 选项按钮点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder  builder  =new AlertDialog.Builder(this);
        builder.setTitle("确定选择当前地址为收货地址？");
        final int item = position;
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Address address =addresses.get(position);
                Intent intent = new Intent();
                intent.putExtra("address",address);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    //设置默认地址
    private void setDefaultAddress() {
        int position = 0; int item = 0;
        if(position==0){
            toast("当前已为默认地址");
            return;
        }
        Address defaultAddress = addresses.get(0);
        defaultAddress.setIsDefault(false);
        defaultAddress.update(AddressManagerActivity.this);
        Address address =addresses.get(item);
        address.setIsDefault(true);
        address.update(AddressManagerActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
               updateList();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
