package BolaoDoArmenio;

// TODO
// Prepared Statements & Exceptions
// Close Connection (? No destructor)

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DBConfig {
	private static final String DB_USER = "postgres" ;
	private static final String DB_PASS = "poipoi09" ;
	private static final String DB_ADDR = "162.243.5.172" ;
	private static final String DB_NAME = "bolaodoarmenio" ;
	private static final int DB_PORT = 5432 ;
	protected Connection conn ;
	protected Statement stm ;
	
	protected DBConfig(){
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
			e.printStackTrace();
			System.exit(0);
		}		
	}
	
	protected ArrayList<HashMap<String,Object>> runSql (String Sql){
		ResultSet rs = null;
		try {
			rs = stm.executeQuery(Sql);
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