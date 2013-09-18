package BolaoDoArmenio;

// TODO
// Prepared Statements & Exceptions
// Close Connection (? No destructor)

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class DBConfig {
	private static final String DB_USER = "postgres" ;
	private static final String DB_PASS = "poipoi09" ;
	private static final String DB_ADDR = "162.243.5.172" ;
	private static final String DB_NAME = "bolaodoarmenio" ;
	private static final int DB_PORT = 5432 ;
	private static Connection conn ;
	private static Statement stm ;
	
	private DBConfig() {
	}
	
	public static void init() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://" + DB_ADDR + ":" + DB_PORT + "/"+ DB_NAME +"/", DB_USER, DB_PASS);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			conn = null ;
			e.printStackTrace();
			System.exit(0);
		}		
	}
	
	public static void end() {
		
		if (conn == null)
		{
			return ;
		}// IF
		
		try {
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}		
		
		stm = null ;
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		conn = null ;
	}
	
	public static ArrayList<HashMap<String,Object>> runSql (String Sql){
		if (conn == null)
		{
			return null;
		}
		
		ResultSet rs = null;
		try {
			if(stm.execute(Sql)) {
				rs = stm.getResultSet();
			}
			else {
				return null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		ResultSetMetaData md = null;
		try {
			md = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		int columns = 0;
		try {
			columns = md.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();		
		try {
			while(rs.next())
			{
				HashMap<String,Object> row = new HashMap<String,Object>(columns);
				for(int i = 1; i <= columns; i++)
				{
					row.put(md.getColumnName(i),rs.getObject(i));
				}
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return list;
	}
	
}