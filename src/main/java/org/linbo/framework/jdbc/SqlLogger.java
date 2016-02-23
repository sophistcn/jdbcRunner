package org.linbo.framework.jdbc;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.linbo.framework.jdbc.model.SqlParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL日志输出
 * @author linbo
 *
 */
public class SqlLogger {

	private static Logger logger = LoggerFactory.getLogger(SqlLogger.class);
	private static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss:SSS");
	
	private static void print(String sql, Object[] params, SQLException cause){
		StringBuilder buf = new StringBuilder();
		if(params != null && params.length > 0){
			for(Object param : params){
				if (param == null){
					buf.append("null, ");
					continue;
				}
				if (SqlParameter.class.isAssignableFrom(param.getClass())) {
					SqlParameter sqlParam = (SqlParameter) param;
					param = sqlParam.getValue();
				}
				if (param instanceof Date){
					Date date = (Date) param;
					buf.append("(Date[").append(dateFormat.format(date)).append("])");
				} else {
					buf.append(String.valueOf(param));
				}
				buf.append(", ");
			}
			buf.delete(buf.length()-2, buf.length());
		}
		if(cause != null){
			String msg = cause.getMessage();
			if(msg == null){
				msg = "";
			}
			logger.error("{} SQL：{}; 参数：[{}]", msg, sql, buf.toString());
		}else {
			logger.info("SQL：{}; 参数：[{}]", sql, buf.toString());
		}			
	}
	
	public static void log(String sql, Object[] params){
		print(sql, params, null);
	}

	public static void logWithError(String sql, Object[] params, SQLException cause){
		print(sql, params, cause);
	}
}
