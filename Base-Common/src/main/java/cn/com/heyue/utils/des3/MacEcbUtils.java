package cn.com.heyue.utils.des3;

import java.util.Arrays;

/**
 * @author fuzuyuan
 * @Description
 * @create 2020-11-12 12:48 上午
 */
public class MacEcbUtils {
    public static byte[] IV = new byte[8];

    /**
     * 两个字节异或
     * @param src
     * @param src1
     * @return
     * @author Administrator 2016-4-12
     * @since 1.0
     */
    public static byte byteXOR(byte src, byte src1)
    {
        return (byte) ((src & 0xFF) ^ (src1 & 0xFF));
    }

    /**
     * 分别将两个数组中下标相同的字节进行异或
     * <p>要求数组长度一致
     * @param src
     * @param src1
     * @return
     * @author Administrator 2016-4-12
     * @since 1.0
     */
    public static byte[] bytesXOR(byte[] src, byte[] src1)
    {
        int length = src.length;
        if (length != src1.length)
        {
            return null;
        }
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
        {
            result[i] = byteXOR(src[i], src1[i]);
        }
        return result;
    }

    /**
     * mac计算,数据不为8的倍数，需要补0(64bit的mac算法)
     * <p>具体的步骤如下：
     * <p>1.源字节数组的长度应为8的倍数，否则补零至8的倍数，按8个字节分段(d0,d1,d2...)
     * <p>2.密钥key字节数组长度固定是8
     * <p>3.将key与第一段d0进行des[加密]运算,得到e1
     * <p>4.e1与d2进行异或运算得到x1
     * <p>5.key再与x1进行des[加密]运算,得到e2
     * <p>6.e2在于d3进行异或运算得到x2
     * <p>7.key再与x2进行des[加密]，依次进行，直到将源数组加密完毕,最后的到的字节数组即是mac值
     *
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key 密钥，字节数组长度应为16，多于16将会截取前16位，少于16位则补0
     * @param srcData 源数据
     * @return
     * @throws Exception
     */
    public static byte[] computeMac(byte[] key, byte[] srcData) throws Exception
    {
        int length = srcData.length;
        int x = length % 8;
        int addLen = 0;
        if (x != 0)
        {
            addLen = 8 - length % 8;
        }
        int pos = 0;
        //保证data是8的倍数
        byte[] data = new byte[length + addLen];
        //data的值就是源数组的值（包括补零的值）
        System.arraycopy(srcData, 0, data, 0, length);
        //源数组第一段8字节
        byte[] oper1 = new byte[8];
        System.arraycopy(data, pos, oper1, 0, 8);
        pos += 8;
        //用于存储每段字节与ka加密后的结果数组
        byte[] be = new byte[8];
        for (int i = 0; i < data.length / 8; i++)
        {
            //将第一段oper1与ka进行des加密,得到be
            be = DesUtils.des_encrypt(key,oper1);

            if((i+1)*8 < data.length){
                //将加密结果e1与第二段oper2异或
                byte[] oper2 = new byte[8];
                System.arraycopy(data, pos, oper2, 0, 8);
                //得到异或结果bx
                byte[] bx = bytesXOR(be, oper2);
                oper1 = bx;
                pos += 8;
            }
        }
        return be;
    }

    /**
     * mac计算,数据不为8的倍数，需要补0(128bit的mac算法)
     * <p>具体的步骤如下：
     * <p>1.源字节数组的长度应为8的倍数，否则补零至8的倍数，按8个字节分段(d0,d1,d2...)
     * <p>2.密钥key字节数组长度固定是16,分为左右两部分(ka,kb)
     * <p>3.左半部分ka与第一段d0进行des[加密]运算,得到e1
     * <p>4.e1与d2进行异或运算得到x1
     * <p>5.ka再与x1进行des[加密]运算,得到e2
     * <p>6.e2在于d3进行异或运算得到x2
     * <p>7.ka再与x2进行des[加密]，依次进行，直到将源数组加密完毕,假设最后得到字节数组dn
     * <p>8.用密钥的后半部分kb与dn进行des[解密]运算,得到p1
     * <p>9.最后使用ka与p1进行des[加密]得到最后的mac值
     *
     * <p>如果参数是字符串，请先用{@link DesUtils.hexStringToByte}转换为字节数组
     * @param key 密钥，字节数组长度应为16，多于16将会截取前16位，少于16位则补0
     * @param srcData 源数据
     * @return
     * @throws Exception
     */
    public static byte[] computeMac_128(byte[] key, byte[] srcData) throws Exception
    {
        int klen = key.length;
        byte[] ka = new byte[8];
        byte[] kb = new byte[8];
        byte[] temp = new byte[16];
        //判断key的长度，主要是确定key的左右两部分
        if(klen < 16){
            System.arraycopy(key, 0, temp, 0, key.length);
            System.arraycopy(temp, 0, ka, 0, 8);
            System.arraycopy(temp, 8, kb, 0, 8);
        } else {
            System.arraycopy(key, 0, ka, 0, 8);
            System.arraycopy(key, 8, kb, 0, 8);
        }

        int length = srcData.length;
        int x = length % 8;
        int addLen = 0;
        if (x != 0)
        {
            addLen = 8 - length % 8;
        }
        int pos = 0;
        //保证data是8的倍数
        byte[] data = new byte[length + addLen];
        //data的值就是源数组的值（包括补零的值）
        System.arraycopy(srcData, 0, data, 0, length);
        //源数组第一段8字节
        byte[] oper1 = new byte[8];
        System.arraycopy(data, pos, oper1, 0, 8);
        pos += 8;
        //用于存储每段字节与ka加密后的结果数组
        byte[] be = new byte[8];
        for (int i = 0; i < data.length / 8; i++)
        {
            //将第一段oper1与ka进行des加密,得到be
            be = DesUtils.des_encrypt(ka,oper1);

            if((i+1)*8 < data.length){
                //将加密结果e1与第二段oper2异或
                byte[] oper2 = new byte[8];
                System.arraycopy(data, pos, oper2, 0, 8);
                //得到异或结果bx
                byte[] bx = bytesXOR(be, oper2);
                oper1 = bx;
                pos += 8;
            }
        }

        //将最后加密的be与kb进行解密,得到dd
        byte[] bb = new byte[8];
        bb = DesUtils.des_decrypt(kb, be);

        //最后将bb与ka进行加密,得到mac值
        // 取8个长度字节
        byte[] retBuf = new byte[8];
        retBuf = DesUtils.des_encrypt(ka,bb);
        return retBuf;
    }

    public static void main(String[] args) throws Exception
    {
//        byte[] buff = DesUtils.hexStringToBytes("633962760000000084130001245C75FCA127C7D47DB9123E8BE5DB3488F54F8A33F5C6A1AE8DD0CC70B63A7A0B800000");
//        byte[] keys = DesUtils.hexStringToBytes("61A4E53BB5349173DA6E5DDC4C08C851");


        byte[] buff = DesUtils.hexStringToBytes("633962760000000084130001245C75FCA127C7D47DB9123E8BE5DB3488F54F8A33F5C6A1AE8DD0CC70B63A7A0B800000");
        byte[] keys = DesUtils.hexStringToBytes("61A4E53BB5349173DA6E5DDC4C08C851");

        byte[] result = computeMac(keys, buff);

        System.out.println("加密结果："+DesUtils.bytesToHexString(result));
        System.out.println(Arrays.toString(result));
    }
}
