package testDB;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Mini {
	public static void main(String[] argv) throws SQLException {
		String user = "zhac344";
		String password = "eimohy";
		String database = "teachdb.cs.rhul.ac.uk";
		
		Connection connection = connectToDatabase(user, password, database);
		if (connection != null) {
			System.out.println("SUCCESS: You made it!"
					+ "\n\t You can now take control of your database!\n");
		} else {
			System.out.println("ERROR: \tFailed to make connection!");
			System.exit(1);
		}
		
		System.out.println("Creating table delayedFlights...");
		createTable(connection, "delayedFlights (ID_of_Delayed_Flight integer, Month integer, "
				+ "DayofMonth integer, DayOfWeek integer, DepTime integer, ScheduledDepTime integer, "
				+ "ArrTime integer, ScheduledArrTime integer, UniqueCarrier varchar(10), FlightNum integer, "
				+ "ActualFlightTime integer, scheduledFlightTime integer, AirTime integer, ArrDelay integer, "
				+ "DepDelay integer, Orig varchar(10), Dest varchar(10), Distance integer, "
				+ "primary key(ID_of_Delayed_Flight));");
		insertIntoTableFromFileDelayedFlights(connection, "delayedFlights", "minDelayedFlight");
		String query = "SELECT * FROM delayedFlights;";
		ResultSet rs = executeQuery(connection, query);
		try {
			while (rs.next()) {
				System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3)
				+ ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6)
				+ ", " + rs.getString(7) + ", " + rs.getString(8) + ", " + rs.getString(9) 
				+ ", " + rs.getString(10) + ", " + rs.getString(11) + ", " + rs.getString(12) 
				+ ", " + rs.getString(13) + ", " + rs.getString(14) + ", " + rs.getString(15) 
				+ ", " + rs.getString(16) + ", " + rs.getString(17) + ", " + rs.getString(18));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		
		System.out.println("Creating table airport...");
		createTable(connection, "airport (airportCode varchar(10), airportName varchar(50), "
				+ "City varchar(15), State varchar(10), primary key(airportCode));");
		insertIntoTableFromFileAirport(connection, "airport", "minAirport");
		
		query = "SELECT * FROM airport;";
		rs = executeQuery(connection, query);
		try {
			while (rs.next()) {
				System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
	}
	
	
	
	public static Connection connectToDatabase(String user, String password, String database) {
		System.out.println("------ Testing PostgreSQL JDBC Connection ------");
		Connection connection = null;
		try {
			String protocol = "jdbc:postgresql://";
			String dbName = "/CS2855/";
			String fullURL = protocol + database + dbName + user;
			connection = DriverManager.getConnection(fullURL, user, password);
		} catch (SQLException e) {
			String errorMsg = e.getMessage();
			if (errorMsg.contains("authentication failed")) {
				System.out.println("ERROR: \tDatabase password is incorrect. Have you changed the password string above?");
				System.out.println("\n\tMake sure you are NOT using your university password.\n"
						+ "\tYou need to use the password that was emailed to you!");
			} else {
				System.out.println("Connection failed! Check output console.");
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public static ResultSet executeQuery(Connection connection, String query) {
		System.out.println("DEBUG: Executing query...");
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static void dropTable(Connection connection, String table) {
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute("DROP TABLE IF EXISTS " + table);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTable(Connection connection, String tableDescription) {
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute("CREATE TABLE " + tableDescription);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int insertIntoTableFromFileDelayedFlights(Connection connection, String table, String filename) {
		int numRows = 0;
		String currentLine = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/cim/ug/zhac344/CS2855/minDelayedFlight"));
			Statement st = connection.createStatement();
			// Read in each line of the file until we reach the end.
			while ((currentLine = br.readLine()) != null) {
				String[] values = currentLine.split(",");
				String composedLine = "INSERT INTO " + table + " VALUES (" + "'" + values[0] +"', '" 
				+ values[1] + "', '"+ values[2] + "', '" + values[3] + "', '" + values[4] + "', '"
						+ values[5] + "', '"+ values[6] +"', '" + values[7] + "', '"+ values[8] + "', '"
				               + values[9] + "', '" + values[10] + "', '"+ values[11] +"', '"+ values[12] +"', '" 
				               		  + values[13] + "', '"+ values[14] + "', '"+ values[15] + "', '" 
				                             + values[16] + "', '"+ values[17] + "');";
				System.out.println(composedLine);
				numRows = st.executeUpdate(composedLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
		return numRows;
	}
	
	public static int insertIntoTableFromFileAirport(Connection connection, String table, String filename) {
		int numRows = 0;
		String currentLine = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/cim/ug/zhac344/CS2855/minAirport"));
			Statement st = connection.createStatement();
			// Read in each line of the file until we reach the end.
			while ((currentLine = br.readLine()) != null) {
				String[] values = currentLine.split(",");
				String composedLine = "INSERT INTO " + table + " VALUES (" + "'" + values[0] + "', '" + values[1] + "', '"+ values[2] + "', '" + values[3] + "');";				
				System.out.println(composedLine);
				numRows = st.executeUpdate(composedLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Insert Done");
		return numRows;
	}
}
