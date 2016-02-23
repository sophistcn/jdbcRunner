package org.linbo.framework.jdbc.mapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 执行具体的JDBC操作
 * @author linbo
 *
 * @param <T>
 */
public interface JdbcHandler<T> {

	/**
	 * 将jdbc对象引用存放到对应PreparedStatement，CallableStatement，ResultSet中，可以不用进行手动关闭<br/>
	 * 对于Connection，不建议进行手动关闭。如果手动关闭Connectino时，需要考虑是否开启事务，否则可能引起连接为空的异常。
	 * @param sql
	 * @param params
	 * @param conn
	 * @param pst
	 * @param cst
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public T handle(String sql, Object[] params, Connection conn, PreparedStatement pst, CallableStatement cst, ResultSet rs) throws SQLException;
	
}
