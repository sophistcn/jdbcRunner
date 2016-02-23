package org.linbo.framework.jdbc.demo;

import org.linbo.framework.jdbc.JdbcRunner;
import org.linbo.framework.jdbc.Transaction;
import org.linbo.framework.jdbc.util.JdbcUtil;

public class DaoDemo {

	public void doQuery(Transaction tr) throws Exception {
		JdbcRunner jdbc = JdbcUtil.getJdbcRunnerFromTransaction(tr);
		String sql = "select s.name from student s where s.id = ?";
		String s = jdbc.queryForString(sql, new Object[]{1});
		System.out.println(s);
	}
	
	public void doDelete(Transaction tr) throws Exception {
		JdbcRunner jdbc = JdbcUtil.getJdbcRunnerFromTransaction(tr);
		String sql = "delete from student s where s.id = ?";
		int out = jdbc.update(sql, new Object[]{1});
		System.out.println(out);
	}
	
}
