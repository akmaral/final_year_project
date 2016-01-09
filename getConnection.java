//  Establish a connection to a mSQL database using JDBC
package review_based_recommender;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap; 

public class getConnection  { 

	public static void main (String[] args) { 
		try
		{
			// Step 1: "Load" the JDBC driver
			Class.forName("com.mysql.jdbc.Driver"); 

			// Step 2: Establish the connection to the database 
			String url = "jdbc:mysql://localhost:3306/akmaral?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";
			
			Connection conn = DriverManager.getConnection(url,"root","");  
			Statement stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT userId,appId FROM blips_apps ORDER BY 'userId'");
			while ( rs.next() ) {
				HashMap<String,String> test = new HashMap<String,String>();
				String user_id = rs.getString("userId");
				String app_id = rs.getString("appId");
				test.put(user_id, app_id);				
				save_dataset_into_file.saveIndexesIntoFile(test, "Resource/full dataset");
				//System.out.println("this is my array: \t" + test);
			}
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println("D'oh! Got an exception!"); 
			System.err.println(e.getMessage()); 
		} 
	} 
} 