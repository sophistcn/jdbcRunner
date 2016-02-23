package org.linbo.framework.jdbc.model;

public class SqlInOutParameter extends SqlOutParameter {
	
	public SqlInOutParameter(Object value, int sqlType) {
		super(sqlType);
		this.value = value;
	}

	public int getSqlType() {
		return sqlType;
	}
	
}
