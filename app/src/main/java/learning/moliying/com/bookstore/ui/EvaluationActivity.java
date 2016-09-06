package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Comment;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

public class EvaluationActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "EvaluationActivity";
    Orders orders;
    private SimpleDraweeView imageView3_bookimage;
    private TextView textView7_bookName,textView10_discount_price;
    private RatingBar ratingBar_star;
    private EditText editText_evaluation;
    private Button button_submit;
    private _User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_evaluation);
        currentUser = BmobUser.getCurrentUser(this,_User.class);
        Intent intent = getIntent();
        orders = (Orders) intent.getSerializableExtra("orders");
        imageView3_bookimage = (SimpleDraweeView) findViewById(R.id.imageView3_bookimage);
        textView7_bookName = (TextView) findViewById(R.id.textView7_bookName);
        textView10_discount_price = (TextView) findViewById(R.id.textView10_discount_price);
        editText_evaluation = (EditText) findViewById(R.id.editText_evaluation);
        ratingBar_star = (RatingBar) findViewById(R.id.ratingBar_star);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
        initUi();
    }

    //初始化数据
    private void initUi() {
        final String bookInfoId = orders.getBookInfoId();
        BmobQuery<BookInfo> query=new BmobQuery<>();
        query.getObject(this, bookInfoId, new GetListener<BookInfo>() {
            @Override
            public void onSuccess(BookInfo bookInfo) {
                imageView3_bookimage.setImageURI(bookInfo.getBookImage().getUrl());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

        textView7_bookName.setText(orders.getBookName());
        textView10_discount_price.setText("￥"+orders.getDiscountPrice());


    }


    /**
     * 返回上一页
     * @param view
     */
    public void backMainClick2(View view){
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit:
                submitComment();
                break;
        }
    }


    /**
     * 提交评价
     */
    private void submitComment() {
        int rating = (int) ratingBar_star.getRating();
        String info = editText_evaluation.getText().toString();
        if(rating==0){
            Toast.makeText(EvaluationActivity.this, "评分等级未填写", Toast.LENGTH_SHORT).show();
            return;
        }
        if(info.length()<5){
            Toast.makeText(EvaluationActivity.this, "最低不少于5个字评论", Toast.LENGTH_SHORT).show();
            return;
        }
        Comment comment = new Comment();
        comment.setUser(currentUser.getUsername());
        comment.setUserId(currentUser.getObjectId());
        comment.setBookInfoId(orders.getBookInfoId());
        comment.setContent(info);
        comment.setStar(rating);
        comment.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(EvaluationActivity.this, "评论添加成功", Toast.LENGTH_SHORT).show();
                orders.setStatus(6);
                orders.update(EvaluationActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(TAG, "onFailure: "+s);
                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

    }
}
