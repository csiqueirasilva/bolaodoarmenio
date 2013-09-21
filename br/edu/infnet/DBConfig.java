package br.edu.infnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public final class DBConfig {
	private static final String DB_USER = "root";
	private static final String DB_PASS = "admin";
	private static final String DB_ADDR = "localhost";
	private static final String DB_NAME = "bolaodoarmenio";
	private static final int DB_PORT = 3306;
	private static Connection conn = null;
	private static Statement stm = null;

	private DBConfig() {
	}

	private static void init() {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://" + DB_ADDR
						+ ":" + DB_PORT + "/" + DB_NAME, DB_USER, DB_PASS);
				stm = conn.createStatement();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private static void end() {
		try {
			stm.close();
			stm = null;
			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void initTransaction() {
		if (conn != null) {
			return;
		}
		init();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void transactionQuery(String sql) {
		try {
			if (!conn.getAutoCommit()) {
				buildQuery(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void endTransaction() {
		if (conn == null) {
			return;
		}
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		end();
	}

	private static ArrayList<HashMap<String, Object>> buildQuery(String sql) {
		ArrayList<HashMap<String, Object>> list = null;

		try {

			if (stm.execute(sql)) {
				ResultSet rs = stm.getResultSet();

				ResultSetMetaData md = rs.getMetaData();

				int columns = 0;
				columns = md.getColumnCount();

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
							break ;
						case java.sql.Types.SMALLINT:
							colvalue = new Short(rs.getShort(i));
							break ;
						case java.sql.Types.VARCHAR:
							colvalue = rs.getString(i) != null ? new String(rs.getString(i)) : null ;
							break ;
						case java.sql.Types.TIMESTAMP:
							colvalue = rs.getString(i) != "0000-00-00 00:00:00" ? rs.getString(i) : null ;
							break ;
						case java.sql.Types.DATE:
							colvalue = rs.getDate(i) != null ? rs.getDate(i).clone() : null ;
							break ;
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

	private static void readArgsPreparedStatement(String sql, Object ... args) {
		for(int i = 0; i < args.length; i++) {
			PreparedStatement pstm = (PreparedStatement) stm;
			if(args[i] instanceof String) {
				pstm.setString(i+1, (String) args[i]);
			} else if (args[i] instanceof Date) {
			}
		}
	}
	
	public static ArrayList<HashMap<String, Object>> runPreparedSql(String sql, Object ... args ) {
		ArrayList<HashMap<String, Object>> list = null;
		init();
		try {
			stm = conn.prepareStatement(sql);
			
			if (conn.getAutoCommit()) {
				list = buildQuery(sql);
				end();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}	
	
	public static ArrayList<HashMap<String, Object>> runSql(String sql) {
		ArrayList<HashMap<String, Object>> list = null;
		init();
		try {
			if (conn.getAutoCommit()) {
				list = buildQuery(sql);
				end();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}