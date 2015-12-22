package review_based_recommender;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry; 

public class user_product_index  { 

	static HashMap<String,String> userIndex = new HashMap<String,String>();
	static HashMap<String,String> productIndex = new HashMap<String,String>();

	public static void indexGeneration(HashMap<String,List<String>> indexes){

		String key;
		List<String> value;
		
		for(Entry<String, List<String>> entry : indexes.entrySet()) {
			//System.out.println("hashmap temp: "+temp);
			key = entry.getKey(); 
			value = entry.getValue();
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
				String concatenated = "";
				for(int i=0;i<value.size();i++){
					rs = stmt.executeQuery("SELECT review FROM test WHERE appId="+value.get(i));
					//System.out.println(value.get(i));
					while ( rs.next() ) {
						String review = rs.getString("review");
						list.add(review);
						//System.out.println(value.get(i) + "  \t" + review);
					}
					for (String s : list)
					{
						concatenated += s + "\t";
					}
					//String stopWords = removeStopwords(userIndex,);
					userIndex.put(key,concatenated);
					productIndex.put(value.get(i),concatenated);
				}
				conn.close();
			}
			catch (Exception e)
			{
				System.err.println("D'oh! Got an exception!"); 
				System.err.println(e.getMessage()); 
			} 
		}
		indexes.clear();
		System.out.println("userIndex:\t"+userIndex);
		System.out.println("product index: " + productIndex  + "\n");
	}
	
	public static HashMap<String, String> getUserIndex(){
		return userIndex;
	}
	
	public static HashMap<String, String> getProductIndex(){
		return productIndex;
	}
	
} 