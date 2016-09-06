package learning.moliying.com.bookstore.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.vo.Address;
import learning.moliying.com.bookstore.vo._User;

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AddAddressActivity";
    private EditText editText_username,editText_phoneNumber,editText_address;
    private Button button_saveAddress,button_saveAndSetDefaultAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_add_address);

        /**
         * 初始化组件
         */
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        editText_address = (EditText) findViewById(R.id.editText_address);

        button_saveAddress = (Button) findViewById(R.id.button_saveAddress);
        button_saveAndSetDefaultAddress = (Button) findViewById(R.id.button_saveAndSetDefaultAddress);
        button_saveAddress.setOnClickListener(this);
        button_saveAndSetDefaultAddress.setOnClickListener(this);
    }





    /**
     * 返回按钮
     */
    public void backMainClick2(View view){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //保存地址
            case R.id.button_saveAddress:
                addAddress();
                break;

            //保存为默认地址
            case R.id.button_saveAndSetDefaultAddress:
                addDefaultAddress();
                break;



        }
    }




    /**
     * 添加地址
     */
    private void addAddress() {
        String username = editText_username.getText().toString();
        String phoneNumber = editText_phoneNumber.getText().toString();
        String address = editText_address.getText().toString();
        if(TextUtils.isEmpty(username)){
            toast("请输入收货人姓名");
            return;
        }
        if(TextUtils.isEmpty(editText_phoneNumber.getText().toString())){
            toast("请输入收货人电话号码");
            return;
        }
        if(TextUtils.isEmpty(editText_address.getText().toString())){
            toast("请输入收货人地址");
            return;
        }

        Address addressObj = new Address();
        addressObj.setUserId(BmobUser.getCurrentUser(AddAddressActivity.this,_User.class).getObjectId());
        addressObj.setUsername(username);
        addressObj.setPhoneNumber(phoneNumber);
        addressObj.setAddress(address);
        addressObj.setIsDefault(false);
        addressObj.save(AddAddressActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("保存地址成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });


    }


    /**
     * 添加默认地址
     */
    private void addDefaultAddress() {
        String username = editText_username.getText().toString();
        String phoneNumber = editText_phoneNumber.getText().toString();
        final String address = editText_address.getText().toString();
        if(TextUtils.isEmpty(username)){
            toast("请输入收货人姓名");
            return;
        }
        if(TextUtils.isEmpty(editText_phoneNumber.getText().toString())){
            toast("请输入收货人电话号码");
            return;
        }
        if(TextUtils.isEmpty(editText_address.getText().toString())){
            toast("请输入收货人地址");
            return;
        }
       String userId =  BmobUser.getCurrentUser(AddAddressActivity.this,_User.class).getObjectId();
        final Address addressObj = new Address();
        addressObj.setUserId(userId);
        addressObj.setUsername(username);
        addressObj.setPhoneNumber(phoneNumber);
        addressObj.setAddress(address);
        addressObj.setIsDefault(true);

        BmobQuery<Address> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",userId);
        query.findObjects(AddAddressActivity.this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> list) {
                List<Address> addresses = list;
                for (Address address1 : addresses) {
                    address1.setIsDefault(false);
                    address1.save(AddAddressActivity.this);
                }
                addressObj.save(AddAddressActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        toast("保存默认地址成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(TAG, "onFailure: "+s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });


    }


    private void toast(String info){
        Toast.makeText(AddAddressActivity.this, info, Toast.LENGTH_SHORT).show();
    }

}
