package org.linbo.framework.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.linbo.framework.jdbc.JdbcManager;
import org.linbo.framework.jdbc.JdbcRunner;
import org.linbo.framework.jdbc.util.PropertiesUtil;

public class JdbcRunnerTest {
	
	@Test
	public void testQueryForListMap() {
		try(JdbcRunner jdbc = JdbcManager.newJdbcRunner()) {
			String sql = "select *   from smart_deal a   join smart_card b on a.smt_cardid = b.smt_cardid   join smart_personnel c on b.smt_personnelid = c.smt_personnelid where a.smt_cardid=?";
			Object[] params = {51383};
			List<Map<String, ?>> list = jdbc.queryForListMap(sql, params);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testQueryRunner() throws SQLException{
//		String sql = "select *   from smart_deal_201602_g a   join smart_card b on a.smt_cardid = b.smt_cardid   join smart_personnel c on b.smt_personnelid = c.smt_personnelid   join smart_dealtype d on a.smt_dealcode = d.smt_dealcode   join smart_machine e on a.smt_authno = e.smt_authno   join smart_organize f on e.smt_org_id = f.smt_org_id where a.smt_dealcode in('00', '01', '02', '04', '09', '20', '21', '26', '28', '29', '70', '74')   and c.smt_salaryno = ?   and d.smt_debitcredit in (?)   and a.smt_dealdatetime >= to_date(?, 'yyyyMMdd') and a.smt_dealdatetime < to_date(?, 'yyyyMMdd') order by smt_dealdatetime";
//		QueryRunner qr = new QueryRunner();
//		Connection conn = JdbcManager.getConnection();
//		Object obj = qr.query(conn, sql, new ResultSetHandler<Object>(){
//
//			@Override
//			public Object handle(ResultSet arg0) throws SQLException {
//				// TODO Auto-generated method stub
//				return null;
//			}}, new Object[]{});
//		System.out.println(obj);
//	}
	
	@Test
	public void testQueryForInt(){
		try(JdbcRunner jdbc = JdbcManager.newJdbcRunner()){
			String sql = "select 2 from dual where 1=?";
			System.out.println(jdbc.queryForInteger(sql, new Object[]{2}));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdate(){
		try(JdbcRunner jdbc = JdbcManager.newJdbcRunner()){
			String sql = "update test_save set msg=? where msg = ?";
			int i = jdbc.update(sql, new Object[]{"l", null});
			System.out.println(i);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testInsert(){
		try(JdbcRunner jdbc = JdbcManager.newJdbcRunner()){
			String sql = "insert into test_save (datetime, msg) values(?,?)";
			int i = jdbc.update(sql, new Object[]{new BigDecimal(1), null});
			System.out.println(i);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Throwable t){
			t.printStackTrace();
		}
	}

//	@Test
//	public void testJdbcUpdate(){
//		try(Connection conn = JdbcManager.getConnection()) {
//			String sql = "update test_save set msg=null where msg is ?";
//			PreparedStatement pst = conn.prepareStatement(sql);
//			pst.setObject(1, null);
//			System.out.println(pst.executeUpdate());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void testOracleDriverError() throws Exception{
		try{
			String driver = PropertiesUtil.getString("dbcp.properties", "dbcp.dirver");
			String url = PropertiesUtil.getString("dbcp.properties", "dbcp.url");
			String user = PropertiesUtil.getString("dbcp.properties", "dbcp.user");
			String password = PropertiesUtil.getString("dbcp.properties", "dbcp.password");
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			String sql = "select *   from smart_deal a   join smart_card b on a.smt_cardid = b.smt_cardid   join smart_personnel c on b.smt_personnelid = c.smt_personnelid where a.smt_cardid=?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.getParameterMetaData();
		}catch(Exception e){
			Assert.assertTrue(e instanceof ArrayIndexOutOfBoundsException);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Calendar.class.isAssignableFrom(Calendar.getInstance().getClass()));
		System.out.println(Date.class.isAssignableFrom(Calendar.getInstance().getClass()));
		System.out.println(Date.class.isAssignableFrom(new Date().getClass()));
		System.out.println(new BigDecimal(2.3).doubleValue());
	}
}
