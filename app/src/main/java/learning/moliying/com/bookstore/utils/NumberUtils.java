package learning.moliying.com.bookstore.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * descreption:
 * company: moliying.com
 * Created by vince on 16/07/13.
 */
public class NumberUtils {

    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static String format(double number){
        return df.format(number);
    }

    public static String getCurrentDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
