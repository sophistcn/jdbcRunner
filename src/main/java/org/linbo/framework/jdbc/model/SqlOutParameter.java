package org.linbo.framework.jdbc.model;

public class SqlOutParameter extends SqlParameter {
	
	protected int sqlType;

	public SqlOutParameter(int sqlType) {
		this.sqlType = sqlType;
	}

	public int getSqlType() {
		return sqlType;
	}
	
}
