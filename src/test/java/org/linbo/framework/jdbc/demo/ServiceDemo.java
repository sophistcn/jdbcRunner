package org.linbo.framework.jdbc.demo;

import java.sql.SQLException;

import org.junit.Test;
import org.linbo.framework.jdbc.JdbcManager;
import org.linbo.framework.jdbc.Transaction;

public class ServiceDemo {

	private DaoDemo dao = new DaoDemo();
	
	@Test
	public void doTransaction() throws SQLException {
		Transaction tr = JdbcManager.beginTransaction();
		try{
			dao.doQuery(tr);
			dao.doDelete(tr);
			tr.commit();
		}catch(Exception e){
			e.printStackTrace();
			tr.rollback();
		}
	}

	@Test
	public void doNoTransaction() throws SQLException {
		try{
			dao.doQuery(null);
			dao.doDelete(null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
