package cn.com.heyue.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：dengjie
 * @date ：2021/5/10
 * @description ：腾讯api
 */
public class TxApiUtils {

    // 腾讯云id
    private static String secretId = TxConstant.TX_CLOUD_SECRET_ID;
    // 腾讯云密钥
    private static String secretKey = TxConstant.TX_CLOUD_SECRET_KEY;

    /**
     * 身份证识别机接口
     *
     * @param files MultipartFile
     * @return
     */
    public static Object IDCardOCR(MultipartFile files)  {
        IDCardOCRResponse resp = null;
        String error = "";
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);

            IDCardOCRRequest req = new IDCardOCRRequest();
            req.setImageBase64(Base64Utils.multipartFileToBASE64(files));
            resp = client.IDCardOCR(req);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            error = e.getMessage();
        }
        return error;
    }

    public static void main(String [] args) {
        try{

            Credential cred = new Credential("AKIDNWO31kWSTFjzd3iWLxd5HXaDZmMd3RvB", "H9kQq4EqFPIuKn1zqfGachhSxNYUBnCj");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);

            IDCardOCRRequest req = new IDCardOCRRequest();
//            req.setImageBase64(Base64Utils.ImageToBase64("C:\\Users\\djalh\\Desktop\\微信图片_20210510084916.jpg"));
            req.setImageBase64(Base64Utils.ImageToBase64("C:\\Users\\djalh\\Desktop\\touxiang.jpg"));
            IDCardOCRResponse resp = client.IDCardOCR(req);

            System.out.println(IDCardOCRResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.getMessage());
        }

    }


}

 