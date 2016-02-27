/*author: Akmaral Akhanova
 * this class get access to the database called akmaral, where it gets dataset containing user id and product id for that user
 * using the SQL query
 * after that saves the generated dataset in text file called 'my_dataset'*/

package dataset_generation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
public class getDatasetFromDatabase {
	public static void main(String[] args) {
		try
		{
			// "Load" the JDBC driver
			Class.forName("com.mysql.jdbc.Driver"); 

			// Establish the connection to the database 
			String url = "jdbc:mysql://localhost:3306/akmaral?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";

			Connection connection = DriverManager.getConnection(url,"root","");  
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			HashMap<String,List<String>> user_product_dataset = new HashMap<String,List<String>>();
			resultSet = statement.executeQuery("SELECT userId, movieId FROM blips_movies where bias > 0 and movieId in (SELECT movieId FROM `blips_movies`"
					+ "GROUP BY movieId having count(movieId) > 3 order by 'userId');");
//			"SELECT userId, appId FROM blips_apps where bias > 0 and appId in (SELECT appId FROM `blips_apps`"
//			+ "GROUP BY appId having count(appId) > 3 order by 'userId');"
			while ( resultSet.next() ) {
				save_dataset_into_file.saveDatasetIntoFile(user_product_dataset, "movies_dataset");
			}
		} 
		catch (Exception e)
		{
			System.err.println("D'oh! Got an exception!"); 
			System.err.println(e.getMessage()); 
		} 
	}
}
