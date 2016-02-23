package org.linbo.framework.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.linbo.framework.jdbc.util.JdbcUtil;



/**
 * 字段映射Map
 * @author linbo
 *
 */
public class ColumnMapRowMapper implements RowMapper<Map<String, ?>>{

	@Override
	public Map<String, ?> mapRow(ResultSet rs, int index) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = new HashMap<String, Object>(columnCount);
		for(int i = 1; i <= columnCount; i++){
			String key = JdbcUtil.lookupColumnName(rsmd, i);
			Object obj = JdbcUtil.getResultSetValue(rs, i);
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}
	
}