package org.linbo.framework.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.linbo.framework.jdbc.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事物，提供给服务层调用<br/>
 * @author linbo
 *
 */
public class Transaction {
	
	private static Logger logger = LoggerFactory.getLogger(Transaction.class);
	
	private JdbcRunner jdbcRunner;
	private Connection connection;
	
	public JdbcRunner getJdbcRunner(){
		return jdbcRunner;
	}
	
	public Transaction(Connection connection) throws SQLException{
		if(connection == null){
			throw new SQLException("无效的数据库连接");
		}
		this.connection = connection;
		jdbcRunner = new JdbcRunner(connection, false);
	}
	
	/**
	 * 提交事物<br/>
	 * @throws SQLException
	 */
	public void commit() throws SQLException{
		logger.trace("提交事物并关闭数据库连接");
		connection.commit();
		JdbcUtil.closeConnection(connection);
		jdbcRunner = null;
	}
	
	/**
	 * 回滚事物<br/>
	 * @throws SQLException
	 */
	public void rollback() throws SQLException{
		logger.trace("回滚事物并关闭数据库连接");
		connection.rollback();
		JdbcUtil.closeConnection(connection);
		jdbcRunner = null;
	}
	
}
