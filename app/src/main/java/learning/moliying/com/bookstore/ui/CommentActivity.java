package learning.moliying.com.bookstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import learning.moliying.com.bookstore.App;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.adapter.CommentAdapter;
import learning.moliying.com.bookstore.vo.BookInfo;
import learning.moliying.com.bookstore.vo.Comment;

public class CommentActivity extends BaseActivity {
    private static final  String TAG ="CommentActivity" ;
    private BookInfo bookInfo;
    private ListView listView_comment;
    private CommentAdapter adapter;
    private List<Comment> comments=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activities.add(this);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        bookInfo = (BookInfo) intent.getSerializableExtra("bookInfo");
        listView_comment = (ListView) findViewById(R.id.listView_comment);
        adapter = new CommentAdapter(this,comments);
        listView_comment.setAdapter(adapter);
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("bookInfoId",bookInfo.getObjectId());
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if(list.size()>0){
//                    adapter = new CommentAdapter(CommentActivity.this,list);
//                    listView_comment.setAdapter(adapter);
                    comments=list;
                    adapter.setComments(comments);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: "+s);
            }
        });
    }


    /**
     * 返回
     */
    public void backMainClick2(View view){
        finish();
    }
}
