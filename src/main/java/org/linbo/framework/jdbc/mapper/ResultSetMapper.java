package org.linbo.framework.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询结果映射到对象
 * @author linbo
 *
 * @param <T>
 */
public interface ResultSetMapper<T> {

	public T mapResultSet(ResultSet rs) throws SQLException;
	
}
