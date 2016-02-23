package org.linbo.framework.jdbc.dataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.linbo.framework.jdbc.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 数据库连接池
 * @author linbo
 *
 */
public class DataSourceDbcpImpl extends BasicDataSource{
	
	private static Logger logger = LoggerFactory.getLogger(DataSourceDbcpImpl.class);
	
	private static final String CONFIG_FILE = "dbcp.properties";
	
	public DataSourceDbcpImpl() {
		init();
	}

	/**
	 * 初始化数据库连接池
	 */
	private void init() {
		try {
			String driver = PropertiesUtil.getString(CONFIG_FILE, "dbcp.dirver");
			String url = PropertiesUtil.getString(CONFIG_FILE, "dbcp.url");
			String user = PropertiesUtil.getString(CONFIG_FILE, "dbcp.user");
			String password = PropertiesUtil.getString(CONFIG_FILE, "dbcp.password");
			Integer maxTotal = PropertiesUtil.getInteger(CONFIG_FILE, "dbcp.maxTotal");
			Long maxWaitMillis = PropertiesUtil.getLong(CONFIG_FILE, "dbcp.maxWaitMillis");
			this.setUrl(url);
			this.setDriverClassName(driver);
			this.setUsername(user);
			this.setPassword(password);
			if(maxTotal != null){
				setMaxTotal(maxTotal);
				logger.debug("设置最大活动连接数量：{}", maxTotal);
			}
			if(maxWaitMillis != null){
				this.setMaxWaitMillis(maxWaitMillis);
				logger.debug("设置获得连接的超时时间(以毫秒数为单位)：{}", maxWaitMillis);
			}
			logger.info("dbcp初始化完成");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建数据库连接池失败", e);
		}
	}

	public Connection getConnection() throws SQLException {
		try{
			return super.getConnection();
		}catch(SQLException e){
			e.printStackTrace();
			String msg = e.getMessage();
			if("Cannot get a connection, pool error Timeout waiting for idle object".equals(msg)){
				throw new SQLException("获取数据库连接失败：超出连接池连接数上限");
			}
			if(msg != null && msg.indexOf("Got minus one from a read call") > 0){
				throw new SQLException("获取数据库连接失败：可能达到数据库服务器设置的连接数上限");
			}
			throw e;
		}
	}

	@Override
	public void close() {
		logger.debug("销毁DBCP数据源");
		try {
			super.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("销毁DBCP数据源失败", e);
		}
	}

}
