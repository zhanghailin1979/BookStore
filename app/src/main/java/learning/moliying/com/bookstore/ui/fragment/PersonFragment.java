package learning.moliying.com.bookstore.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.PayOrdersAdapter;
import learning.moliying.com.bookstore.ui.AddAddressActivity;
import learning.moliying.com.bookstore.ui.AddressManagerActivity;
import learning.moliying.com.bookstore.ui.LoginActivity;
import learning.moliying.com.bookstore.ui.OrdersActivity;
import learning.moliying.com.bookstore.utils.AppUtils;
import learning.moliying.com.bookstore.utils.Constant;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener {
    public static final int REQUEST_CODE = 0x1;
    private static final String TAG = "PersonFragment";
    private static final int ALL_ORDERS = 0X1;
    private static final int WAIT_ORDERS = 0X2;
    private static final int WAIT_RECEIPT = 0X3;
    private static final int WAIT_COMMENT = 0X5;
    private SimpleDraweeView imageView_userIcon;


    private Button login_button, addAddressButton;
    private TextView textView2_username;
    private LinearLayout exit_layout;
    /*
    *存储用户头像的文件路径
     */
    private File tempImage;

    /*
    *获得当前用户
     */
    private _User currentUser = null;


    /**
     * 订单状态
     * @return
     */
    private LinearLayout all_orders_layout;     //全部订单
    private LinearLayout wait_pay_layout;       //待付款
    private LinearLayout wait_goodsReceipt_layout;       //待收货
    private LinearLayout wait_Evaluation_layout;       //待评价


    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        initUi(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(getActivity(), _User.class);
        if (currentUser != null)
            tempImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + currentUser.getObjectId() + ".jpg");
        updateState();
    }

    //初始化组件
    private void initUi(View view) {
        //查找第一部分组件
        imageView_userIcon = (SimpleDraweeView) view.findViewById(R.id.imageView_userIcon);
        Log.i(TAG, "imageView_userIcon: " + imageView_userIcon.getId());
        login_button = (Button) view.findViewById(R.id.login_button);
        addAddressButton = (Button) view.findViewById(R.id.addAddressButton);
        textView2_username = (TextView) view.findViewById(R.id.textView2_username);

        //注册点击事件
        imageView_userIcon.setOnClickListener(this);
        login_button.setOnClickListener(this);
        addAddressButton.setOnClickListener(this);
        //查找退出按钮组件
        exit_layout = (LinearLayout) view.findViewById(R.id.exit_layout);
        exit_layout.setOnClickListener(this);
        Log.i(TAG, "exit_layout: " + exit_layout.getId());
        updateState();

        //找到订单组件
        all_orders_layout = (LinearLayout) view.findViewById(R.id.all_orders_layout);
        wait_pay_layout = (LinearLayout) view.findViewById(R.id.wait_pay_layout);
        wait_goodsReceipt_layout = (LinearLayout) view.findViewById(R.id.wait_goodsReceipt_layout);
        wait_Evaluation_layout = (LinearLayout) view.findViewById(R.id.wait_Evaluation_layout);

        all_orders_layout.setOnClickListener(this);
        wait_pay_layout.setOnClickListener(this);
        wait_goodsReceipt_layout.setOnClickListener(this);
        wait_Evaluation_layout.setOnClickListener(this);


    }

    //设置按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选取头像
            case R.id.imageView_userIcon:
                System.out.println("--------------------------");
                selectImage();
                break;
            //登录按钮
            case R.id.login_button:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setPackage(getActivity().getPackageName());
                startActivity(intent);
                break;
            //退出登录
            case R.id.exit_layout:
                currentUser = null;
                _User.logOut(getActivity());
                updateState();
                Log.i(TAG, "onClick: " + currentUser);
                break;
            //添加地址
            case R.id.addAddressButton:
                addAddress();
                break;

            //全部订单
            case R.id.all_orders_layout:
                if(currentUser==null){
                    Toast.makeText(getActivity(), "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), OrdersActivity.class);
                intent.putExtra("orderStatus",ALL_ORDERS);
                intent.putExtra("userId",currentUser.getObjectId());
                startActivity(intent);
                break;
            //待付款
            case R.id.wait_pay_layout:
                if(currentUser==null){
                    Toast.makeText(getActivity(), "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), OrdersActivity.class);
                intent.putExtra("orderStatus",WAIT_ORDERS);
                intent.putExtra("userId",currentUser.getObjectId());
                startActivity(intent);
                break;
            //待收货
            case R.id.wait_goodsReceipt_layout:
                if(currentUser==null){
                    Toast.makeText(getActivity(), "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), OrdersActivity.class);
                intent.putExtra("orderStatus",WAIT_RECEIPT);
                intent.putExtra("userId",currentUser.getObjectId());
                startActivity(intent);
                break;
            //待评价
            case R.id.wait_Evaluation_layout:
                if(currentUser==null){
                    Toast.makeText(getActivity(), "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getActivity(), OrdersActivity.class);
                intent.putExtra("orderStatus",WAIT_COMMENT);
                intent.putExtra("userId",currentUser.getObjectId());
                startActivity(intent);
                break;


        }

    }


    /**
     * 收货地址管理
     */
    private void addAddress() {
        startActivity(new Intent(getActivity(), AddAddressActivity.class));
    }

    //点击选择图片
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImage));
//        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", String.valueOf(requestCode));
        Log.i("TAG", String.valueOf(resultCode));
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) Log.i("TAG", "NULL");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/user.jpg");
                Log.i("TAG", tempImage.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (tempImage.exists()) {
                Log.i("TAG", "文件存在");
                //imageView_userIcon.setImageURI(Uri.fromFile(tempImage));
                final BmobFile bmobFile = new BmobFile(tempImage);
                bmobFile.upload(getActivity(), new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "头像上传成功", Toast.LENGTH_SHORT).show();
                        currentUser.setUserIcon(bmobFile);
                        currentUser.update(getActivity(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "头像更新成功", Toast.LENGTH_SHORT).show();
                                imageView_userIcon.setImageURI(currentUser.getUserIcon().getUrl());
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getActivity(), "头像更新失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("TAG", "图片上传失败");
                    }
                });
            }
        }


    }

    private void updateState() {
        if (currentUser != null) {
            /**
             * 更新显示组件
             */
            login_button.setVisibility(View.GONE);//登录按钮
            imageView_userIcon.setVisibility(View.VISIBLE);//头像
            textView2_username.setVisibility(View.VISIBLE);//用户名(手机号)
            exit_layout.setVisibility(View.VISIBLE);//退出按钮
            addAddressButton.setVisibility(View.VISIBLE);//收货地址
            textView2_username.setText(AppUtils.formatPhoneNumber(currentUser.getUsername()));


            if (tempImage.exists()) {
//                imageView_userIcon.setImageURI(currentUser.getUserIcon().getUrl());
                Log.i(TAG, "updateState: " + Uri.fromFile(tempImage).toString());
                imageView_userIcon.setImageURI(Uri.fromFile(tempImage));
                return;
            }
            BmobFile bmobFile = currentUser.getUserIcon();
            if (bmobFile != null) {
                imageView_userIcon.setImageURI(currentUser.getUserIcon().getUrl());

            }
        } else {
            login_button.setVisibility(View.VISIBLE);
            imageView_userIcon.setVisibility(View.GONE);
            textView2_username.setVisibility(View.GONE);
            exit_layout.setVisibility(View.GONE);
            addAddressButton.setVisibility(View.GONE);
            return;
        }


    }




}
