package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.utils.AppUtils;
import learning.moliying.com.bookstore.vo._User;

public class ResetPasswordActivity extends BaseActivity {
    private static final String TAG = "ResetPasswordActivity";
    private static final int SLEEP_CODE = 0x1;
    private static final int STOP_SLEEP = 0x2;
    private ImageView imageView_show_password;
    private EditText editText_phone,editText_password_new,editText_validate_number;
    private Button button3_get_validateNumber;

    private boolean showPassword = true;
    private int vertifyCode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_reset_password);
        imageView_show_password = (ImageView) findViewById(R.id.imageView_show_password);
        editText_password_new = (EditText) findViewById(R.id.editText_password_new);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText_validate_number = (EditText) findViewById(R.id.editText_validate_number);
        button3_get_validateNumber = (Button) findViewById(R.id.button3_get_validateNumber);
    }


    /**
     * 重设密码
     * @param v
     */
    public void resetPasswordClick(View v){
        AppUtils.hideInputMethod(v);    //隐藏输入法
        String phone = editText_phone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showToast("请输入手机号码");
            return;
        }
        String password = editText_password_new.getText().toString();
        if(TextUtils.isEmpty(password)){
            showToast("请输入新密码");
            return;
        }
        String validateNumber = editText_validate_number.getText().toString();
        if(TextUtils.isEmpty(validateNumber)){
            showToast("请输入验证码");
            return;
        }
        BmobUser.resetPasswordBySMSCode(this,validateNumber,password, new ResetPasswordByCodeListener() {
            @Override
            public void done(BmobException ex) {
                if(ex==null){
                    showToast("密码重置成功");
                    finish();
                }else{
                    showToast("重置失败");
                }
            }
        });






    }


    /**
     * 发送短信验证码
     * @param v
     */
    public void getValidateNumberClick(View v){
        AppUtils.hideInputMethod(v);
        final String phone = editText_phone.getText().toString();
        if(TextUtils.isEmpty(editText_phone.getText().toString()) ){
            showToast("手机号不能为空");
            return;
        }
        if (phone.length()!=11){
            showToast("输入手机号格式不正确");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=60;i>0;i--){
                    try {
                        Message message = handler.obtainMessage();
                        message.arg1 = i;
                        message.what = SLEEP_CODE;
                        handler.sendMessage(message);
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(STOP_SLEEP);
            }
        }).start();

        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber",phone);
        query.addWhereEqualTo("username",phone);
        query.count(this, _User.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if(i==1){
                    BmobSMS.requestSMSCode(ResetPasswordActivity.this, phone, "smscode", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if(e==null){//验证码发送成功
                                showToast("验证码发送成功,请稍候查看短信");
                            }
                        }
                    });
                }else{
                    showToast("手机号码不存在,请重新注册");
                    startActivity(new Intent(ResetPasswordActivity.this,RegisterActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void showToast(String info) {
        Toast.makeText(ResetPasswordActivity.this, info, Toast.LENGTH_SHORT).show();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SLEEP_CODE:
                    int num = msg.arg1;
                    button3_get_validateNumber.setText("剩余"+num+"秒");
                    button3_get_validateNumber.setEnabled(false);
                    break;
                case STOP_SLEEP:
                    button3_get_validateNumber.setEnabled(true);
                    button3_get_validateNumber.setText("获取验证码");
                    break;
            }
        }
    };



    /**
     * 显示或隐藏密码
     * @param v
     */
    public void showHidePasswordClick(View v){
        if (showPassword){
            imageView_show_password.setSelected(true);
            editText_password_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showPassword = false;
        }else{
            imageView_show_password.setSelected(false);
            editText_password_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPassword = true;
        }
    }


    /**
     * 返回
     *
     */
    public void backMainClick2(View v){
        finish();
    }
}
