package cn.com.heyue.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {
    /**
     * 元转分，确保price保留两位有效数字
     * @return
     */
    public static int changeY2F(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        int money = (int)(price * 100);
        return money;
    }

    /**
     * 分转元，转换为bigDecimal在toString
     * @return
     */
    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }

    public static void main(String[] args){
        int fen = MoneyUtil.changeY2F(2);
        System.out.println(fen);

        System.out.println(MoneyUtil.changeF2Y(101));
    }
}
