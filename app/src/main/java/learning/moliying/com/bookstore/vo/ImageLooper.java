package learning.moliying.com.bookstore.vo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ImageLooper extends BmobObject {
    private BmobFile imageLooperPage;

    public BmobFile getImageLooperPage() {
        return imageLooperPage;
    }

    public void setImageLooperPage(BmobFile imageLooperPage) {
        this.imageLooperPage = imageLooperPage;
    }
}
