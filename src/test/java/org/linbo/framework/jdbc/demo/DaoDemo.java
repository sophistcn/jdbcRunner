package org.linbo.framework.jdbc.demo;

import org.linbo.framework.jdbc.JdbcRunner;
import org.linbo.framework.jdbc.Transaction;
import org.linbo.framework.jdbc.util.JdbcUtil;

public class DaoDemo {

	public void doQuery(Transaction tr) throws Exception {
		JdbcRunner jdbc = JdbcUtil.getJdbcRunnerFromTransaction(tr);
		String sql = "select * from dual where 1=?";
		String s = jdbc.queryForString(sql, new Object[]{1});
		System.out.println(s);
		jdbc = JdbcUtil.getJdbcRunnerFromTransaction(tr);
		Integer i = jdbc.queryForInteger(sql, new Object[]{1});
		System.out.println(i);
	}
	
	public void doDelete(Transaction tr) throws Exception {
		JdbcRunner jdbc = JdbcUtil.getJdbcRunnerFromTransaction(tr);
		String sql = "delete from dual where 1=?";
		int out = jdbc.update(sql, new Object[]{1});
		System.out.println(out);
	}
	
}
