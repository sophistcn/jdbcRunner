package org.linbo.framework.jdbc.util;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import org.linbo.framework.jdbc.JdbcManager;
import org.linbo.framework.jdbc.JdbcRunner;
import org.linbo.framework.jdbc.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtil {

	private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);
	
	/**
	 * 转换java.util.Date类型为java.sql.Date类型
	 * @param date
	 * @return
	 */
	public static Date convertDate(java.util.Date date) {
		Date sqlDate = null;
		if(date != null){
			long time = date.getTime();
			sqlDate = new Date(time);
		}
		return sqlDate;
	}
	
	public static java.util.Date getDate(ResultSet rs, String coumnName) throws SQLException{
		Timestamp time = rs.getTimestamp(coumnName);
		if(time != null){
			Date date = new Date(time.getTime());
			return date;
		}
		return null;
	}
	
	/**
	 * 生成Oracle分页待预编译SQL，需要最后两个注入参数为分页信息：<br/>
	 * 	倒数第二个：返回记录数的最大下标<br/>
	 *  倒数第一个：返回记录数的最小下标<br/>
	 * @param sql
	 * @return
	 */
	public static String generateOraclePageSql(String sql) {
		StringBuilder buf = new StringBuilder();
		buf.append(" SELECT * FROM ");
		buf.append("   (SELECT rownum as rowno, a.* FROM ");
		buf.append("     (").append(sql).append(") a ");
		buf.append("   WHERE rownum < ?) ");
		buf.append(" WHERE rowno>=?");
		return buf.toString();
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param connection
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null){
			try {
				logger.trace("关闭数据库连接");
				conn.close();
			} catch (SQLException ex) {
				logger.warn("无法关闭 JDBC Connection", ex);
			} catch (Throwable ex) {
				logger.warn("关闭 JDBC Connection时发生意外", ex);
			}
		}
	}

	public static void closeResult(ResultSet rs) {
		if (rs != null) {
			try {
				logger.trace("关闭ResultSet");
				rs.close();
			} catch (SQLException ex) {
				logger.warn("无法关闭 JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.warn("关闭 JDBC ResultSet时发生意外", ex);
			}
		}
	}
	
	public static void closeStatement(Statement stat) {
		if(stat != null){
			try {
				logger.trace("关闭Statement");
				stat.close();
			} catch (SQLException ex) {
				logger.warn("无法关闭 JDBC Statement", ex);
			} catch (Throwable ex) {
				logger.warn("关闭 JDBC Statement时发生意外", ex);
			}
		}
	}
	
	/** 
	* 从Transaction获取JdbcRunner，如果Transaction为空，则生成一个不支持事物的JdbcRunner
	*/
	public static JdbcRunner getJdbcRunnerFromTransaction(Transaction tr) throws SQLException{
		if(tr == null){
			return JdbcManager.newJdbcRunner();
		}
		return tr.getJdbcRunner();
	}
	
	/**
	 * 查找结果集的数据项名
	 * @param resultSetMetaData
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1){
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	
	/**
	 * 获取数据库结果集指定下标的值<br/>
	 * 处理 Blob/Clob/Timestamp/Time/Date 类型
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null){
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob){
			obj = rs.getBytes(index);
		}else if (obj instanceof Clob){
			obj = rs.getString(index);
		}else if (className != null
				&& ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className))){
			obj = rs.getTimestamp(index);
		}else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)){
				obj = rs.getTimestamp(index);
			}else{
				obj = rs.getDate(index);
			}
		} else if (obj != null && (obj instanceof Date)
				&& "java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))){
			obj = rs.getTimestamp(index);
		}
		return obj;
	}

	/**
	 * 设置SQL占位符参数，优先从params类型判断需要设置的占位符参数类型
	 * @param pst
	 * @param params
	 * @throws SQLException
	 */
	public static void fillParameters(PreparedStatement pst, Object[] params) throws SQLException {
		if(params == null || params.length == 0){
			return;
		}
		// 执行SQL：select *   from smart_deal_201602_g a   join smart_card b on a.smt_cardid = b.smt_cardid   join smart_personnel c on b.smt_personnelid = c.smt_personnelid where b.smt_cardid=1
		// 出现数组越界，不进行参数个数判断，直接使用jdbc异常报错
//		ParameterMetaData meta = pst.getParameterMetaData();
//		int consumeCount = meta.getParameterCount();
//		int paramsLength = params.length;
//		if(consumeCount != paramsLength){
//			throw new SQLException(String.format("注入参数个数不对，需要 %s 个，实际 %s 个", consumeCount, paramsLength));
//		}
		for (int i = 0; i < params.length; i++) {
			Object param = params[i];
			fillParameter(pst, param, i + 1);
		}
	}
	
	public static void fillParameter(PreparedStatement pst, Object param, int index) throws SQLException{
		//有些jdbc驱动调用meta.getParameterType方法不兼容
//		int paramType = meta.getParameterType(i + 1);
//		String paramTypeName = meta.getParameterTypeName(i + 1);
		if(param == null){
			pst.setObject(index, null);
			return;
		}
		Class<?> paramClass = param.getClass();
		//处理时间类型
		if(java.util.Date.class.isAssignableFrom(paramClass)
				|| java.sql.Date.class.isAssignableFrom(paramClass)
				|| java.sql.Time.class.isAssignableFrom(paramClass)
				|| java.sql.Timestamp.class.isAssignableFrom(paramClass)
				|| java.util.Calendar.class.isAssignableFrom(paramClass)){
//			if(paramType != Types.DATE && paramType != Types.TIME && paramType != Types.TIMESTAMP){
//				if(paramTypeName != null && paramTypeName.length() > 0){
//					throw new SQLException(String.format("第 %s 个参数是 %s 类型，实际需要 %s", i + 1, paramClass.getName(), paramTypeName));
//				}
//				throw new SQLException(String.format("第 %s 个参数是 %s 类型，实际需要[java.util.Date, java.sql.Date, java.sql.Time, java.sql.Timestamp, java.util.Calendar]", i + 1, paramClass.getName()));
//			}
			if(java.util.Calendar.class.isAssignableFrom(paramClass)){
				Calendar calendar = (Calendar) param;
				pst.setTimestamp(index, new Timestamp(calendar.getTimeInMillis()));
			}else{
				java.util.Date date = (java.util.Date) param;
				Timestamp time = new Timestamp(date.getTime());
				pst.setTimestamp(index, time);
			}
//		} else if(java.math.BigDecimal.class.isAssignableFrom(paramClass)){
//		//处理大数字类型
//			if(paramType != Types.NUMERIC && paramType != Types.DECIMAL){
//				if(paramTypeName != null && paramTypeName.length() > 0){
//					throw new SQLException(String.format("第 %s 个参数是 %s 类型，实际需要 %s", i + 1, paramClass.getName(), paramTypeName));
//				}
//				throw new SQLException(String.format("第 %s 个参数是 java.math.BigDecimal 类型，实际需要 %s ", i + 1, paramTypeName));
//			}
//			pst.setBigDecimal(i + 1, (BigDecimal) param);
		}else{
			pst.setObject(index, param);
		}
	}
	
}
