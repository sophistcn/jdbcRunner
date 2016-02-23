package org.linbo.framework.jdbc;

import java.sql.SQLException;

import org.junit.Test;
import org.linbo.framework.jdbc.JdbcManager;
import org.linbo.framework.jdbc.JdbcRunner;

public class JdbcManagerTest {

	@Test
	public void testGetNewConnection() throws SQLException {
		int i = 0;
		while(i < 100){
			JdbcManager.newJdbcRunner();
			System.out.println(++i);
		}
	}
	
	@Test
	public void testCloseConnection() throws SQLException{
		//jdk7 支持
		int i = 0;
		while(i < 102){
			try(JdbcRunner conn = JdbcManager.newJdbcRunner()){
				System.out.println(++i);
			}
		}
	}
	
	public void testNotCloseConnection() {
		int i = 0;
		while(i++ < 52){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						JdbcRunner conn = JdbcManager.newJdbcRunner();
						System.out.println(Thread.currentThread().getId() + ":" + conn);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.setDaemon(false);
			System.out.println(i);
			t.start();
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		JdbcManagerTest tt = new JdbcManagerTest();
		tt.testNotCloseConnection();
	}
}
