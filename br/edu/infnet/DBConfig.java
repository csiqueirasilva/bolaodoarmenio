package br.edu.infnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public final class DBConfig {
        public static final String DATE_PARSE_STRING = "YYYY-MM-dd HH:mm:ss.SSS";
	private static final String DB_USER = "postgres";
	private static final String DB_PASS = "poipoi09";
	private static final String DB_ADDR = "localhost";
	private static final String DB_NAME = "bolaodoarmenio";
	private static final int DB_PORT = 5432;
	private static Connection conn = null;
	private static Statement stm = null;

	private DBConfig() {
	}

	private static void init() throws SQLException {
		if (conn == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
                            throw new SQLException(e);
			}
			conn = DriverManager.getConnection("jdbc:postgresql://" + DB_ADDR + ":"
					+ DB_PORT + "/" + DB_NAME, DB_USER, DB_PASS);
		}
	}

	private static void end() throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public static void initTransaction() throws SQLException {
		if (conn != null) {
			return;
		}
		init();
		conn.setAutoCommit(false);
	}

	public static void endTransaction() throws SQLException {
		if (conn == null) {
			return;
		}
		conn.commit();
		end();
	}

	private static ArrayList<HashMap<String, Object>> buildQuery(String sql) {
		ArrayList<HashMap<String, Object>> list = null;
		try {
			boolean hasResult = false;
			if(sql == null)
			{
				hasResult = ((PreparedStatement) stm).execute();
			} else {
				hasResult = stm.execute(sql);
			}
			
			if (hasResult) {
				ResultSet rs = stm.getResultSet();
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				list = new ArrayList<HashMap<String, Object>>();
				while (rs.next()) {
					HashMap<String, Object> row = new HashMap<String, Object>(
							columns);
					for (int i = 1; i <= columns; i++) {
						String colname = md.getColumnName(i);
						Object colvalue;
						switch (md.getColumnType(i)) {
						case java.sql.Types.BIGINT:
							colvalue = new Long(rs.getLong(i));
							break;
						case java.sql.Types.SMALLINT:
							colvalue = new Short(rs.getShort(i));
							break;
						case java.sql.Types.VARCHAR:
							colvalue = rs.getString(i) != null ? new String(
									rs.getString(i)) : null;
							break;
						case java.sql.Types.TIMESTAMP:
							colvalue = rs.getTimestamp(i);
							break;
						case java.sql.Types.DATE:
							colvalue = rs.getDate(i) != null ? rs.getDate(i)
									.clone() : null;
							break;
                                                case java.sql.Types.DOUBLE:
                                                        colvalue = new Double(rs.getDouble(i));
							break;
                                                case java.sql.Types.INTEGER:
                                                        colvalue = new Integer(rs.getInt(i));
							break;
                                                case java.sql.Types.FLOAT:
                                                        colvalue = new Float(rs.getFloat(i));
							break;    
                                                case java.sql.Types.CHAR:
						default:
							colvalue = rs.getObject(i);
						}
						row.put(colname, colvalue);
					}
					list.add(row);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return list;
	}

	private static void readArgsPreparedStatement(String sql, Object... args)
			throws SQLException {
		
                for (int i = 0; i < args.length; i++) {
			PreparedStatement pstm = (PreparedStatement) stm;
			if (args[i] instanceof String) {
                            pstm.setString(i + 1, (String) args[i]);
			} else if (args[i] instanceof Date) {
                            pstm.setTimestamp(i + 1,
                                            new Timestamp(((Date) args[i]).getTime()));
 			} else if (args[i] instanceof Integer) {
                            pstm.setInt(i + 1, (Integer) args[i]);
			} else if (args[i] instanceof Double) {
                            pstm.setDouble(i + 1, (Double) args[i]);
			} else if (args[i] instanceof Float) {
                            pstm.setFloat(i + 1, (Float) args[i]);
			} else if (args[i] instanceof Long) {
                            pstm.setLong(i + 1, (Long) args[i]);
			} else if (args[i] instanceof Short) {
                            pstm.setShort(i + 1, (Short) args[i]);
			} else if (args[i] == null) {
                            pstm.setObject(i + 1, null);
                        }
		}
	}

	public static void transactionQuery(String sql) throws SQLException {
		if (!conn.getAutoCommit()) {
			stm = conn.createStatement();
			buildQuery(sql);
			stm.close();
			stm = null;
		}
	}
	
	public static void transactionQuery(String sql, Object... args)
			throws SQLException {
		if (!conn.getAutoCommit()) {
			stm = conn.prepareStatement(sql);
			readArgsPreparedStatement(sql, args);
			buildQuery(null);
			stm.close();
			stm = null;
		}
	}

	public static ArrayList<HashMap<String, Object>> runPreparedSql(String sql,
			Object... args) throws SQLException {
		ArrayList<HashMap<String, Object>> list = null;
		if (conn == null) {
			init();
			stm = conn.prepareStatement(sql);
			readArgsPreparedStatement(sql, args);
			list = buildQuery(null);
			stm.close();
			stm = null;
			end();
		}
		return list;
	}

	public static ArrayList<HashMap<String, Object>> runSql(String sql)
			throws SQLException {
		ArrayList<HashMap<String, Object>> list = null;
		if (conn == null) {
			init();
			stm = conn.createStatement();
			list = buildQuery(sql);
			stm.close();
			stm = null;
			end();
		}
		return list;
	}
}