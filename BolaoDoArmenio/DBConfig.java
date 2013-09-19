package BolaoDoArmenio;

// TODO
// Prepared Statements & Exceptions
// Close Connection (? No destructor)

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class DBConfig {
	private static final String DB_USER = "postgres";
	private static final String DB_PASS = "poipoi09";
	private static final String DB_ADDR = "162.243.5.172";
	private static final String DB_NAME = "bolaodoarmenio";
	private static final int DB_PORT = 5432;
	private static Connection conn;
	private static Statement stm;

	private DBConfig() {
	}

	private static void init() {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://" + DB_ADDR
					+ ":" + DB_PORT + "/" + DB_NAME + "/", DB_USER, DB_PASS);
			stm = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
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

	public static ArrayList<HashMap<String, Object>> runSql(String Sql) {
		init();

		ArrayList<HashMap<String, Object>> list = null;

		try {
			ResultSet rs;

			if (stm.execute(Sql)) {
				rs = stm.getResultSet();
			} else {
				return null;
			}

			ResultSetMetaData md = null;
			md = rs.getMetaData();

			int columns = 0;
			columns = md.getColumnCount();

			list = new ArrayList<HashMap<String, Object>>();
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>(
						columns);
				for (int i = 1; i <= columns; i++) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		end();

		return list;
	}

}