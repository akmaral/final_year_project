package dataset_generation;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry; 

public class user_product_index  { 

	static HashMap<String,String> userIndex = new HashMap<String,String>();
	static HashMap<String,String> productIndex = new HashMap<String,String>();
	static HashMap<String,String> productIndexWithoutStopwords = new HashMap<String,String>();
	static HashMap<String,String> userIndexWithoutStopwords = new HashMap<String,String>();


	public static void indexGeneration(HashMap<String,List<String>> indexes) throws IOException{

		String key;
		List<String> value;

		for(Entry<String, List<String>> entry : indexes.entrySet()) {
			key = entry.getKey(); 
			value = entry.getValue();
			String concatenated = "";
			System.out.println("key: " + key + "\t value: " + value);

			try
			{
				// Step 1: "Load" the JDBC driver
				Class.forName("com.mysql.jdbc.Driver"); 

				// Step 2: Establish the connection to the database 
				String url = "jdbc:mysql://localhost:3306/akmaral?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";

				Connection conn = DriverManager.getConnection(url,"root","");  
				Statement stmt = conn.createStatement();
				ResultSet rs;
				ArrayList<String> list = new ArrayList<String>();
				
				for(int i=0;i<value.size();i++){
					/*query for getting reviews from the database using review's Id*/
					rs = stmt.executeQuery("SELECT review FROM blips_apps WHERE userId = " + key + " and appId="+value.get(i));
					while ( rs.next() ) {
						String review = rs.getString("review");
						list.add(review); //list of reviews for each product
					}
				} /*to concatenate all the reviews into one string for each user*/
				
				for (String s : list)
				{
					concatenated += s + "\t";
				}
				
				conn.close();
			}
			
			catch (Exception e)
			{
				System.err.println("D'oh! Got an exception!"); 
				System.err.println(e.getMessage()); 
			} 
			
			userIndex.put(key, concatenated);
			
			for(int k=0;k<value.size();k++){
				//System.out.println(value.get(k));
				productIndex.put(value.get(k),concatenated);
			}
		}
		System.out.println(userIndex);
		System.out.println(productIndex);
	}

	public static HashMap<String, String> getUserIndex(){
		return userIndex;
	}

	public static HashMap<String, String> getProductIndex(){
		return productIndex;
	}

} 