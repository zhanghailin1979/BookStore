package learning.moliying.com.bookstore.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.zip.Inflater;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import learning.moliying.com.bookstore.R;
import learning.moliying.com.bookstore.utils.AppUtils;
import learning.moliying.com.bookstore.utils.ViewHolder;
import learning.moliying.com.bookstore.vo.Comment;
import learning.moliying.com.bookstore.vo._User;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CommentAdapter extends BaseAdapter {
    private static final String TAG = "CommentAdapter";
    private Context context;
    private List<Comment> comments;
    public CommentAdapter(Context context,List<Comment> comments){
        this.comments=comments;
        this.context=context;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment,null);
        }
        final SimpleDraweeView image= (SimpleDraweeView) ViewHolder.getView(convertView,R.id.simple_userIcon);
        TextView username_tv = (TextView) ViewHolder.getView(convertView,R.id.textView11_username);
        TextView comment_tv = (TextView) ViewHolder.getView(convertView,R.id.textView11_comment);
        TextView star_tv = (TextView) ViewHolder.getView(convertView,R.id.textView12_createdAt);
        RatingBar ratingBar = (RatingBar) ViewHolder.getView(convertView,R.id.ratingBar_star);
        Comment comment=comments.get(position);

        //设置内容
        username_tv.setText(AppUtils.formatPhoneNumber(comment.getUser()));
        comment_tv.setText(comment.getContent());
        star_tv.setText(comment.getStar()+"星");
        ratingBar.setRating(comment.getStar());
        String userId=comment.getUserId();
        BmobQuery<_User> query = new BmobQuery<>();
        query.getObject(context, userId, new GetListener<_User>() {
            @Override
            public void onSuccess(_User user) {
                BmobFile file =user.getUserIcon();
                if(file!=null){
                    image.setImageURI(user.getUserIcon().getUrl());
                }else{
                    image.setImageURI(Uri.parse("res:///"+R.mipmap.de_default_portrait));
                }
            }
            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: "+s);
            }
        });
        return convertView;
    }
}
