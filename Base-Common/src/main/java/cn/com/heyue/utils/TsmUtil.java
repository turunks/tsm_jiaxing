package cn.com.heyue.utils;

import cn.com.heyue.nfc.response.TsmSynParaDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TsmUtil {

	public static List<TsmSynParaDetail> toList(String src) throws Exception {
		List<TsmSynParaDetail> list = new ArrayList<TsmSynParaDetail>();
		String sourcePara = new String(RSAUtils.verifyWithRsa2ByPrivateKey(RSAUtils.decryptBASE64(src), NfcConstant.TSM_LOC_PRI_KEY));
		JSONArray array = JSONArray.parseArray(sourcePara);
		for(Object obj : array){
			TsmSynParaDetail tsmSynParaDetail = JSON.parseObject(obj.toString(),TsmSynParaDetail.class);
			list.add(tsmSynParaDetail);
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		String priKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoiwORWzj36Y7Nk7z6sIn47MT38C4b/nA1xi3dNMxiaeMWJ/YcEJ+OyDIVdHJi7Z9gpmPzFSFlD2hqKAvbJI3aEd+jzIyZU3yMFXUMHGlqa0nN7FLJhCHvSK2N+fCuCy3l6YsY2VjoZQ0R/oVgmh/2IY7u9dQ1hOuwbhUCCtnI1XiQqTzJC5Oc+l77bgmbByV+t/pR1e+JW8waFQRoyyaZf9yGCRbvKgn4EexnY/NS5Zg9ycPxEvkGQ7bkx7Vi+jEXWVCXnvKVI1CsyYmUwIkLmayNsuXaH7M60HCKWJ54ZB8uGcenKqUs32KvgycOSwac9DVzExWhpTYIKH1sVSn9AgMBAAECggEBAIYQXbwnlb8NVvwLQk0eVAFfRKX7tJGr6FG93AmSz+iQVYPwMYHNbz3B3KTYRYyPezED6YkYC8I5aVLXm+aqwvLtOLkwIb5sfCtpffsuNmPiB4j3LDsDEYMminbekyW92rqA9JXFfUUN7EGd9TYMItRDiVBm1m/j1R430hoXy3hFmid8feIGSA6wi5t7A+2Po7zICLzPqCuT5Nrbd+ZYCegbMqLYYOYdZ7w9vMErMKtKKQ+BD16tm1/AJ4ivqESdrMtPro/TrlJvSwwqzuD+uOR6+0i8za1jajbPW4DiAzT/2CqSBEZ7RJN78rAH9onYOByldTQZvuz9nYtGuC3Ic3kCgYEA3sb4pdhJhcayLiG/pJra5cj8iHRk+M25GhKnT3530LglUMcev4vA8dSXpX2zk9Cp0VNgvPW+PDWWV4CYA/p2CVEmGw8zBq0DYMcbUJrr14Pbut3e3BfKIGSUz56nFpO7Jyd011RqwDNeR06ussku1Hldl7Qz+4z034jtp3idwEMCgYEAwa2HMdWZueWZyYbm15ilPp4cxPYe+mPtptktUoiFuBNwPPdshKJOen5rHgzwXxWDnsvLtd3P8wOOMUthg2cNl4eEKMv0JXh3AhRiCoVMrLi5EFxuwXigq+aE7PfYXXB0nS8Wt8SdF2AZIngzvZROzAUEDJgma9oSI4S9Cxqi6L8CgYBWp2aIe/z9QDCGqTH2QRXsU6WHHl84OC1L6v2Rekv4r+gz54AaWa4PYvYptR9o271yYtA6MAxzNxTnrGl1ArNvJFWCKQNcEWhC+K+zRs994RhTM4JAcjB5MSaBK+EqcwRVUo+sQnXTlSeS7RQfDwpcM2koXTvrGQUW/hgTEGOsKQKBgQCRH4jFE6urutpAQoAonm17fRhLkaxmrIf/1I38YOXpp08br741qDSr3pw/GSzn4oQMceKDtOBD1K8wSq6XuplZqKajtyeLCDgMa9+GyJlKduPc2ZX4DH5AMGv78D7EzSXkPcuFCaM4R76tsGn8rOtaPovpL/ziJbYfvS+16VCWmwKBgDsgWHT3TEr95t5DZxRuBqMNMI/Vusr29rj3LDiYTp8TiY1KbhRQgGz4PU2De8R+KdHpEG3i0VFOOoLtgS+yzwLOq0zWynRF2YKoZMjaqDwKLx+/H11AyHLW6yWE3Scg1W+yaxw0aGBaBKABENFvagGG1XFYxkvnj+Ge7XA9gsPY";
		String paraStr = "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";
		String sourcePara = new String(RSAUtils.verifyWithRsa2ByPrivateKey(RSAUtils.decryptBASE64(paraStr),priKey));
		System.out.println(sourcePara);

		JSONArray array = JSONArray.parseArray(sourcePara);
		for(Object obj : array){
			TsmSynParaDetail tsmSynParaDetail = JSON.parseObject(obj.toString(),TsmSynParaDetail.class);
		}
	}
}
