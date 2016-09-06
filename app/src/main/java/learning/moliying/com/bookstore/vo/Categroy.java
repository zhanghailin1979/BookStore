package learning.moliying.com.bookstore.vo;

import cn.bmob.v3.BmobObject;

/**
 * descreption:分类表
 * company: moliying.com
 * Created by vince on 16/07/13.
 */
public class Categroy extends BmobObject{


    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
