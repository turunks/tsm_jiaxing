package cn.com.heyue.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 获取配置文件属性
 * 
 * @author pengyuan
 *
 * @version V1.0
 */
public class PropertiesFileRead {

	public static ResourceBundle getResourceBundle(String filePath) {
		ResourceBundle resourceBundle = null;
		if (null == filePath || filePath.length() <= 0) {
			return resourceBundle;
		}
		// filePath，如果是放在src下，直接传文件名即可，若放在包下，带上包名
		resourceBundle = ResourceBundle.getBundle(filePath);
		return resourceBundle;
	}

	/**
	 * 从配置文件中获取属性值
	 * 
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String getFileValue(String filePath, String key) {
		if (null == filePath || filePath.length() <= 0) {
			return "";
		}
		// properties属性文件不需要加.properties后缀名，只需要文件名即可
		ResourceBundle resource = ResourceBundle.getBundle(filePath);
		return resource.getString(key);
	}

	/**
	 * 从流中获取属性值
	 * 
	 * @param inStream
	 * @param key
	 * @return
	 */
	public static String getInStreamValue(InputStream inStream, String key) {
		try {
			ResourceBundle resource = new PropertyResourceBundle(inStream);
			return resource.getString(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
