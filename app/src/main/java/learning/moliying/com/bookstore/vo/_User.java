package learning.moliying.com.bookstore.vo;

import android.os.Parcelable;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * descreption:
 * company: moliying.com
 * Created by vince on 16/07/13.
 */
public class _User extends BmobUser{

    private BmobFile userIcon;

    public BmobFile getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        this.userIcon = userIcon;
    }
}
