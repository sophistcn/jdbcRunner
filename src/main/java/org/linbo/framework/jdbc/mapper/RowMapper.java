package org.linbo.framework.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库行记录映射
 * @author linbo
 *
 * @param <T>
 */
public interface RowMapper<T> {

	/**
	 * 记录映射到实体
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public T mapRow(ResultSet rs, int index) throws SQLException;
	
}
