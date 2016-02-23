package org.linbo.framework.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties读取工具类
 * @author linbo
 *
 */
public class PropertiesUtil {
	
	/**
	 * 读取配置文件
	 * @param propertiesFile
	 * @return
	 * @throws IOException
	 */
	private static Properties getProperties(String propertiesFile) throws IOException {
		try(InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFile)){
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		}
	}
	
	/**
	 * 读取指定配置文件中的String类型数据
	 * @param propertiesFile
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getString(String propertiesFile, String key) throws IOException{
		Properties properties = getProperties(propertiesFile);
		return properties.getProperty(key);
	}
	
	/**
	 * 读取配置文件中Integer类型数据。
	 * @param propertiesFile
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static Integer getInteger(String propertiesFile, String key) throws IOException {
		Properties properties = getProperties(propertiesFile);
		String val = properties.getProperty(key);
		if(val == null){
			throw new RuntimeException(String.format("%s 配置文件中不存在配置项：%s", propertiesFile, key));
		}
		return Integer.valueOf(val);
	}


	/**
	 * 读取配置文件中的Long类型数据。
	 * @param propertiesFile
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public static Long getLong(String propertiesFile, String key) throws IOException {
		Properties properties = getProperties(propertiesFile);
		String val = properties.getProperty(key);
		if(val == null){
			throw new RuntimeException(String.format("%s 配置文件中不存在配置项：%s", propertiesFile, key));
		}
		return Long.valueOf(val);
	}

}
