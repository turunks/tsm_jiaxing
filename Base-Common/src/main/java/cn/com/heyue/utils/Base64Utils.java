package cn.com.heyue.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * base64加解密
 *
 * @author 付祖远
 * @version V1.0
 */
public class Base64Utils {
    //加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 图片转base64
     *
     * @param img 图片地址
     * @return
     */
    public static String ImageToBase64(String img){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = img;//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try{
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * MultipartFile 转 base64
     *
     * @param mFile
     * @return
     * @throws Exception
     */
    public static String multipartFileToBASE64(MultipartFile mFile) throws Exception{
        BASE64Encoder bEncoder=new BASE64Encoder();
        String[] suffixArra=mFile.getOriginalFilename().split("\\.");
        String preffix="data:image/jpg;base64,".replace("jpg", suffixArra[suffixArra.length - 1]);
        String base64EncoderImg=preffix + bEncoder.encode(mFile.getBytes()).replaceAll("[\\s*\t\n\r]", "");
        return base64EncoderImg;
    }

    public static void main(String[] args) {
        System.out.println(Base64Utils.getBase64("这是一个测试"));
        System.out.println(Base64Utils.getFromBase64(Base64Utils.getBase64("这是一个测试")));
    }
}
