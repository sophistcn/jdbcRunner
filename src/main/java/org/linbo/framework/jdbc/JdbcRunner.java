package org.linbo.framework.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.linbo.framework.jdbc.mapper.ColumnMapRowMapper;
import org.linbo.framework.jdbc.mapper.JdbcHandler;
import org.linbo.framework.jdbc.mapper.ResultSetMapper;
import org.linbo.framework.jdbc.mapper.RowMapper;
import org.linbo.framework.jdbc.model.SqlOutParameter;
import org.linbo.framework.jdbc.model.SqlParameter;
import org.linbo.framework.jdbc.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jdbc执行者，不是线程安全对象
 * @author linbo
 *
 */
public class JdbcRunner implements AutoCloseable{
	
	private static Logger logger = LoggerFactory.getLogger(JdbcRunner.class);
	/**
	 * 是否有事务
	 */
	private final boolean hasTransaction;
	private Connection conn;
	private PreparedStatement pst;
	private CallableStatement cst;
	private ResultSet rs;
	
	/**
	 * @param autoCommit	是否自动提交
	 * @throws SQLException
	 */
	public JdbcRunner(Connection conn, boolean autoCommit) throws SQLException{
		this.conn = conn;
		conn.setAutoCommit(autoCommit);
		hasTransaction = !autoCommit;
	}
	
	/**
	 * 执行后的清理，包括ResultSet，PreparedStatement，CallableStatement
	 */
	private void clean(){
		JdbcUtil.closeResult(rs);
		rs = null;
		JdbcUtil.closeStatement(pst);
		pst = null;
		JdbcUtil.closeStatement(cst);
		cst = null;
	}

	@Override
	public void close() {
		if(hasTransaction){
			logger.trace("存在事物，不关闭数据库连接");
		}else{
			logger.trace("不存在事务，关闭数据库连接");
			JdbcUtil.closeConnection(conn);
		}
	}
	
	/**
	 * 查询SQL
	 * @param sql
	 * @param mapper
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryForList(String sql, Object[] params, RowMapper<T> mapper) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			List<T> list = new LinkedList<>();
			int index = 1;
			while(rs.next()){
				list.add(mapper.mapRow(rs, index++));
			}
			return list;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}

	public List<Map<String, ?>> queryForListMap(String sql, Object... params) throws SQLException {
		return queryForList(sql, params, new ColumnMapRowMapper());
	}

	public Map<String, ?> queryForMap(String sql, Object[] params) throws SQLException{
		List<Map<String, ?>> list = queryForList(sql, params, new ColumnMapRowMapper());
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	
//	public Map<String, String> queryForObject(String sql, Object[] params,
//			ResultSetMapper<Map<String, String>> resultMapper) throws SQLException{
//		try{
//			pst = conn.prepareStatement(sql);
//			JdbcUtil.fillParameters(pst, params);
//			rs = pst.executeQuery();
//			SqlLogger.log(sql, params);
//			return resultMapper.mapResultSet(rs);
//		}catch(SQLException e){
//			SqlLogger.logWithError(sql, params, e);
//			throw e;
//		} finally {
//			doClean();
//		}
//	}
	
	public <T> T queryForObject(String sql, Object[] params,
			ResultSetMapper<T> resultMapper) throws SQLException{
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			return resultMapper.mapResultSet(rs);
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		} finally {
			clean();
		}
	}
	

	/**
	 * 查询Integer结果，若查询不到数据，则返回null。
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Integer queryForInteger(String sql, Object[] params) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			if(rs.next()){
				return rs.getInt(1);
			}
			return null;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}
	
	/**查询Double结果，若查询不到数据，则返回null。
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Double queryForDouble(String sql, Object[] params) throws SQLException {
		try {
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			if(rs.next()){
				return rs.getDouble(1);
			}
			return null;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}

	/**查询Long结果，若查询不到数据，则返回null。
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Long queryForLong(String sql, Object[] params) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			if(rs.next()){
				return rs.getLong(1);
			}
			return null;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}
	
	/**
	 * 查询Stirng结果，若查询不到数据，则返回null。
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public String queryForString(String sql, Object[] params) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			if(rs.next()){
				return rs.getString(1);
			}
			return null;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}
	
	/**
	 * 查询java.util.Date结果，若查询不到数据，则返回null。
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public java.util.Date queryForDate(String sql, Object[] params) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			rs = pst.executeQuery();
			SqlLogger.log(sql, params);
			if(rs.next()){
				return rs.getTimestamp(1);
			}
			return null;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}

	/**
	 * 用于执行单条update和insert语句，操作单条数据
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int update(String sql, Object[] params) throws SQLException {
		try{
			pst = conn.prepareStatement(sql);
			JdbcUtil.fillParameters(pst, params);
			int count =  pst.executeUpdate();
			SqlLogger.log(sql, params);
			return count;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}

	/**
	 * 用于执行单条update或insert SQL语句，操作多条数据
	 * @param sql
	 * @param allParams
	 * @return
	 * @throws SQLException
	 */
	public int batchUpdate(String sql, List<Object[]> allParams) throws SQLException {
		Object[] params = null;
		try{
			int effectCount = 0;
			if(allParams == null || allParams.isEmpty()){
				throw new RuntimeException("参数为空，无法进行更新操作");
			}
			pst = conn.prepareStatement(sql);
			int batchCount = 0;
			for(int i = 0; i < allParams.size(); i++){
				params = allParams.get(i);
				JdbcUtil.fillParameters(pst, params);
				SqlLogger.log(sql, params);
				pst.addBatch();
				if(++batchCount > 100){
					int[] effects = pst.executeBatch();
					if(effects != null){
						for(int count : effects){
							effectCount += count;
						}
					}
					batchCount = 0;
				}
			}
			if(batchCount > 0){
				int[] effects = pst.executeBatch();
				if(effects != null){
					for(int count : effects){
						effectCount += count;
					}
				}
			}
			return effectCount;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}
	
	/**
	 * 用于执行存储过程，可将sql和param传入，便于记录执行SQL日志
	 * @param sql
	 * @param params
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public <T> T execute(String sql, Object[] params, JdbcHandler<T> handler) throws SQLException {
		try{
			T result = handler.handle(sql, params, conn, pst, cst, rs);
			if(sql == null || "".equals(sql)){
				logger.warn("未接收到执行SQL");
			} else {
				SqlLogger.log(sql, params);
			}
			return result;
		}catch(SQLException e){
			SqlLogger.logWithError(sql, params, e);
			throw e;
		}finally{
			clean();
		}
	}

	/**
	 * 未完善，不建议使用，建议使用execute方法
	 * 对于某些类型返回值需要进行处理，如Long
	 * @param procedure
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Object[] call(String procedure, SqlParameter[] params) throws SQLException {
		try{
			String sql = "{call ".concat(procedure).concat("}");
			try{
				cst = conn.prepareCall(sql);
				Map<Integer, Integer> outMetaData = new HashMap<Integer, Integer>();
				if(params != null){
					for (int i = 0; i < params.length; i++) {
						SqlParameter param = params[i];
						if(param == null){
							throw new SQLException(String.format("第 %d 个SqlParameter为空", i + 1));
						}
						Object value = param.getValue();
						if(param instanceof SqlOutParameter){
							//得到输出参数的下标和类型
							SqlOutParameter outParam = (SqlOutParameter) param;
							int sqlType = outParam.getSqlType();
							cst.registerOutParameter(i + 1, sqlType);
							outMetaData.put(i + 1, sqlType);
							cst.setObject(i + 1, value, sqlType);
						} else {
							JdbcUtil.fillParameter(cst, value, i + 1);
						}
					}
				}
				cst.execute();
				SqlLogger.log(sql, params);
				int paramsLength = (params == null) ? 0 : params.length;
				if(paramsLength == 0){
					return new Object[]{};
				}
				Object[] out = new Object[paramsLength];
				for (Integer index : outMetaData.keySet()) {
					int sqlType = outMetaData.get(index);
					if(sqlType == Types.DATE || sqlType ==Types.TIME || sqlType == Types.TIMESTAMP){
						Timestamp time = cst.getTimestamp(index);
						if(time == null){
							continue;
						}
						out[index - 1] = new Date(time.getTime());
					}
					out[index - 1] = cst.getObject(index);
				}
				return out;
			}catch(SQLException e){
				SqlLogger.logWithError(sql, params, e);
				throw e;
			}
		}finally{
			clean();
		}
	}

}
