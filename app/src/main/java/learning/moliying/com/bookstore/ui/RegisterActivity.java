package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.utils.AppUtils;
import learning.moliying.com.bookstore.vo._User;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private static final int SLEEP_CODE = 0X1;
    private static final int STOP_SLEEP = 0X2;
    private EditText editText_phone2, editText_password2, editText_validate_number;
    private Button button3_get_validateNumber;
    private int vertifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_register);
        editText_phone2 = (EditText) findViewById(R.id.editText_phone2);
        editText_password2 = (EditText) findViewById(R.id.editText_password2);
        editText_validate_number = (EditText) findViewById(R.id.editText_validate_number);
        button3_get_validateNumber = (Button) findViewById(R.id.button3_get_validateNumber);
    }

    /**
     * 注册新用户
     */
    public void registerNewUserClick(View v) {
        AppUtils.hideInputMethod(v);    //隐藏输入法
        if (TextUtils.isEmpty(editText_phone2.getText().toString()) ||
                TextUtils.isEmpty(editText_password2.getText().toString())) {
            showToast("手机号或密码不能为空");
            return;
        }
        final String phone = editText_phone2.getText().toString();
        if (phone.length() != 11) {
            showToast("输入手机号格式不正确");
            return;
        }
        if (String.valueOf(vertifyCode).equals(editText_validate_number.getText().toString())) {
            showToast("验证码错误");
            return;
        }
        String password = editText_password2.getText().toString();


        final _User user = new _User();
        user.setUsername(phone);
        user.setPassword(password);
        user.setMobilePhoneNumber(phone);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                showToast("注册成功");
                user.login(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("当前用户已被注册");
                editText_phone2.setText("");
                editText_password2.setText("");
                editText_validate_number.setText("");
            }
        });


    }

    /**
     * 发送短信验证码
     *
     * @param v
     */
    public void sendValidateClick(View v) {
        final String phone = editText_phone2.getText().toString();
        if (TextUtils.isEmpty(editText_phone2.getText().toString())) {
            showToast("手机号不能为空");
            return;
        }
        if (phone.length() != 11) {
            showToast("输入手机号格式不正确");
            return;
        }

        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", phone);
        query.addWhereEqualTo("username", phone);
        query.count(this, _User.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (i == 1) {
                    BmobSMS.requestSMSCode(RegisterActivity.this, phone, "smscode", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {//验证码发送成功
                                showToast("验证码发送成功,请稍候查看短信");
                                vertifyCode = integer;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 60; i > 0; i--) {
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


                            }
                        }
                    });
                } else {
                    showToast("手机号码不存在,请重新注册");
                    editText_phone2.setText("");
                    editText_password2.setText("");
                    editText_validate_number.setText("");
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SLEEP_CODE:
                    int num = msg.arg1;
                    button3_get_validateNumber.setText("剩余" + num + "秒");
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
     * 返回按钮事件
     */
    public void backClick(View v) {
        finish();
    }


    /**
     * Toast事件
     */
    public void showToast(String info) {
        Toast.makeText(RegisterActivity.this, info, Toast.LENGTH_SHORT).show();
    }
}
