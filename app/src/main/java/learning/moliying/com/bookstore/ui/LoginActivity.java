package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.vo._User;

public class LoginActivity extends BaseActivity {
    private ImageView imageView_show_password;
    private EditText editText_password,editText_phone;
    private boolean showPassword = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_login);
        initComponet();
    }

    /**
     * 初始化组件
     */
    private void initComponet() {
        imageView_show_password = (ImageView) findViewById(R.id.imageView_show_password);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
    }


    /**
     * 点击返回按钮事件
     * @param v
     */
    public void backClick(View v){
        finish();
    }


    /**
     * 注册按钮事件
     */
    public void openRegisterClick(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 显示或隐藏密码
     */
    public void showHidePasswordClick(View v){
        if (showPassword){
            imageView_show_password.setSelected(true);
            editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showPassword = false;
        }else{
            imageView_show_password.setSelected(false);
            editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPassword = true;
        }
    }

    /**
     * 登录按钮事件
     */
    public void loginClick(View v){
        String username=editText_phone.getText().toString();
        String password=editText_password.getText().toString();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            showToast("用户名或密码不能为空");
            return;
        }
        if(username.length()!=11){
            showToast("用户名输入格式不正确");
            return;
        }
        final _User user=new _User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                showToast("登录成功");
                finish();
//                Intent intent = getIntent();
//                intent.putExtra("user",u);
//                setResult(RESULT_OK,intent);

//                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("登录失败");
            }
        });
    }

    /**
     * 忘记密码按钮
     */
    public void forgetPasswordClick(View v){
        Intent intent = new Intent(this,ResetPasswordActivity.class);
        startActivity(intent);

    }

    /**
     * Toast事件
     */
    public void showToast(String info){
        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
    }
}
