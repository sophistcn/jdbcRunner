package org.linbo.framework.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.linbo.framework.jdbc.dataSource.DataSourceDbcpImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接管理<br/>
 * 用于获取{@link Transaction} {@link JdbcRunner}
 * 
 * @author linbo
 *
 */
public class JdbcManager{

	private static Logger logger = LoggerFactory.getLogger(JdbcManager.class);

	/**
	 * 数据库连接池
	 */
	private static DataSource dataSource = new DataSourceDbcpImpl();

	/**
	 * 开始事物，并返回事物对象
	 * @return
	 * @throws SQLException
	 */
	public static Transaction beginTransaction() throws SQLException{
		logger.trace("开始事物");
		Connection conn = dataSource.getConnection();
		Transaction tr = new Transaction(conn);
		return tr;
	}

	/**
	 * 生成新的JdbcRunner
	 * @return
	 * @throws SQLException
	 */
	public static JdbcRunner newJdbcRunner() throws SQLException{
		Connection conn = dataSource.getConnection();
		logger.trace("生成JdbcRunner");
		return new JdbcRunner(conn, true);
	}

	/**
	 * 创建新数据库连接
	 * @return
	 * @throws SQLException
	 */
	public static Connection newConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
