package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.BookDetailsAdapter;
import learning.moliying.com.bookstore.ui.fragment.ContentDescriptionFragment;
import learning.moliying.com.bookstore.ui.fragment.PublishDescriptionFragment;
import learning.moliying.com.bookstore.utils.AppUtils;
import learning.moliying.com.bookstore.view.IndexViewPager;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Comment;
import learning.moliying.com.bookstore.vo.Orders;
import learning.moliying.com.bookstore.vo._User;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BookDetailActivity";
    private SimpleDraweeView draweeView;
    private TextView textView4_discountPrice,textView5_price,textView6_discount;
    private TextView textView7_bookName,textView8_author;
    private Button button_Shopping_Cart;
    private ImageView findShoppingCarButton;
    /**
     * 商品评价组件
     */
    private LinearLayout linearLayout4_comment;
    private TextView textView10_comment_count;
    private SimpleDraweeView simple_userIcon;
    private TextView textView11_username,textView12_createdAt,textView11_comment;
    private RatingBar ratingBar_star;

    private PagerSlidingTabStrip tabs;
    private ViewPager indexViewPager;
    private BookDetailsAdapter adapter;
    private ArrayList<Fragment> fragments=new ArrayList<>();

    private BookInfo bookInfo;
    private _User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_book_detail);
        Intent intent = getIntent();
        bookInfo = (BookInfo) intent.getSerializableExtra("bookInfo");
        Log.i("TAG",bookInfo.getBookName());
        initComponentUi();

        initComment();

    }

    /**
     * 初始化评论相关组件并查询内容
     */
    private void initComment() {
        /**
         * 初始化组件
         */
        textView10_comment_count = (TextView) findViewById(R.id.textView10_comment_count);
        simple_userIcon = (SimpleDraweeView) findViewById(R.id.simple_userIcon);
        textView11_username =(TextView) findViewById(R.id.textView11_username);
        textView12_createdAt =(TextView) findViewById(R.id.textView12_createdAt);
        textView11_comment =(TextView) findViewById(R.id.textView11_comment);
        ratingBar_star = (RatingBar) findViewById(R.id.ratingBar_star);
        final BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("bookInfoId",bookInfo.getObjectId());     //根据书的ID进行条件查询
        query.order("-createdAt");      //倒序查询
        query.count(this, Comment.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                textView10_comment_count.setText("共有"+i+"条评论");
                int comment_count=i;
                //查询一条记录
                if (comment_count>0){
                    linearLayout4_comment.setClickable(true);
                    query.setLimit(1);
                    query.findObjects(BookDetailActivity.this, new FindListener<Comment>() {
                        @Override
                        public void onSuccess(List<Comment> list) {
                            Comment comment =list.get(0);
                            textView11_comment.setText(comment.getContent());
                            textView12_createdAt.setText(comment.getStar()+"星");
                            ratingBar_star.setRating(comment.getStar());
                            textView11_username.setText(AppUtils.formatPhoneNumber(comment.getUser()));
                            BmobQuery<_User> queryUser = new BmobQuery<_User>();
                            queryUser.getObject(BookDetailActivity.this, comment.getUserId(), new GetListener<_User>() {
                                @Override
                                public void onSuccess(_User user) {
                                    BmobFile file=user.getUserIcon();
                                    if(file!=null){
                                        simple_userIcon.setImageURI(user.getUserIcon().getUrl());
                                    }else{
                                        simple_userIcon.setImageURI("res://"+R.mipmap.de_default_portrait);
                                    }

                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.i(TAG, "onFailure: "+s);
                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.i(TAG, "onError: "+s);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });

    }

    /**
     * 初始化组件
     *
     */
    private void initComponentUi() {
        draweeView = (SimpleDraweeView) findViewById(R.id.draweeView_BookDetail);
        draweeView.setImageURI(bookInfo.getBookImage().getUrl());
        textView4_discountPrice = (TextView) findViewById(R.id.textView4_discountPrice);
        textView5_price = (TextView) findViewById(R.id.textView5_price);
        textView6_discount = (TextView) findViewById(R.id.textView6_discount);
        findShoppingCarButton = (ImageView) findViewById(R.id.findShoppingCarButton);
        //显示内容
        textView4_discountPrice.setText("￥"+String.valueOf(bookInfo.getDiscountPrice()));
        textView5_price.setText(String.valueOf(bookInfo.getPrice()));
        //设置下划横线
        textView5_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textView6_discount.setText(String.valueOf(bookInfo.getDiscount())+"折");


        textView7_bookName = (TextView) findViewById(R.id.textView7_bookName);
        textView8_author = (TextView) findViewById(R.id.textView8_author);
        button_Shopping_Cart = (Button) findViewById(R.id.button_Shopping_Cart);
        button_Shopping_Cart.setOnClickListener(this);
        findShoppingCarButton.setOnClickListener(this);

        //显示组件内容
        textView7_bookName.setText(bookInfo.getBookName());
        textView8_author.setText(bookInfo.getAuthor());


        linearLayout4_comment=(LinearLayout) findViewById(R.id.linearLayout4_comment);
        linearLayout4_comment.setOnClickListener(this);

        //tab组件
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_bookDetail);
        indexViewPager = (ViewPager) findViewById(R.id.pager_bookDetail);
        initFragments();
        adapter = new BookDetailsAdapter(getSupportFragmentManager(),fragments);
        indexViewPager.setAdapter(adapter);
        tabs.setViewPager(indexViewPager);


    }
    //初始化Fragments
    private void initFragments() {
        ContentDescriptionFragment content=ContentDescriptionFragment.newInstance(bookInfo.getContentDescription());
        ContentDescriptionFragment author=ContentDescriptionFragment.newInstance(bookInfo.getAuthorDescription());
        ContentDescriptionFragment logs = ContentDescriptionFragment.newInstance(bookInfo.getCatalog());
        PublishDescriptionFragment publish=PublishDescriptionFragment.newInstance(bookInfo);
        fragments.add(content);
        fragments.add(author);
        fragments.add(logs);
        fragments.add(publish);
    }


    public void backMainClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 购物车点击事件
             */
            case R.id.button_Shopping_Cart:
                if(currentUser==null) {
                    Toast.makeText(BookDetailActivity.this,"用户尚未登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                Orders orders = new Orders();
                orders.setTotal(1);
                orders.setUserId(currentUser.getObjectId());
                orders.setDiscountPrice(bookInfo.getDiscountPrice());
                orders.setFreight(1);
                orders.setStatus(1);
                orders.setBookName(bookInfo.getBookName());
                orders.setSubtotal(bookInfo.getDiscountPrice());
                orders.setBookInfoId(bookInfo.getObjectId());
                orders.save(this);
                Toast.makeText(BookDetailActivity.this, "已成功加入购物车", Toast.LENGTH_SHORT).show();
                break;

            /**
             * 点击商品评论
             */
            case R.id.linearLayout4_comment:
                Intent intent=new Intent(BookDetailActivity.this,CommentActivity.class);
                intent.putExtra("bookInfo",bookInfo);
                startActivity(intent);
                break;

            /**
             * 跳转查看购物车
             */
            case R.id.findShoppingCarButton:
                if(currentUser==null){
                    Toast.makeText(this, "用户尚未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(this, ShoppingCarActivity.class);
                intent2.putExtra("user",currentUser);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(this,_User.class);
    }
}
