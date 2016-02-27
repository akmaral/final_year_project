/*author: Akmaral Akhanova
 * this class is for generating user index and product index, where for each user the product ids are
 * replaced with the review associated with this product
 * same for products: all the reviews written for a product from training dataset is replaced with reviews concatenated as one
 * string
 * as the result this class will product two indices, saved in a text file, where would be user id followed by a string of reviews
 * written by this user
 * for products it will have product id followed by all the reviews written for that prodcut*/

package dataset_generation;
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


	public static void userIndexGeneration(HashMap<String,List<String>> indexes) {

		String key;
		List<String> value;
		ArrayList<String> userlist = new ArrayList<String>();
		ArrayList<String> productlist = new ArrayList<String>();


		for(Entry<String, List<String>> entry : indexes.entrySet()) {
			key = entry.getKey(); 
			value = entry.getValue();
			//System.out.println(" key: \t" + key + "\tvalue: \t" + value);
			String users_reviews_concatenated = "";
			try {
				// Step 1: "Load" the JDBC (Java Database Connectivity) driver
				Class.forName("com.mysql.jdbc.Driver"); 

				// Step 2: Establish the connection to the database called 'akmaral'
				String url = "jdbc:mysql://localhost:3306/akmaral?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";

				Connection connection = DriverManager.getConnection(url,"root","");  
				Statement statement = connection.createStatement(); //statement object for executing queries
				ResultSet usersResult; //object for getting all the records of the table (result of users query 1)
				ResultSet productsResult; //results of query 2 for products

				for(int i=0;i<value.size();i++){
					/*query for getting reviews written by the user about the products from the database*/
					usersResult = statement.executeQuery("SELECT review FROM blips_apps WHERE userId = " + key + " and appId="+value.get(i));
					while ( usersResult.next() ) {
						String review1 = usersResult.getString("review");
						userlist.add(review1); //list of reviews for the user written about the products he liked
					}
				} /*to concatenate all the reviews into one string for each user*/

				for (String s1 : userlist)
				{
					users_reviews_concatenated += s1 + "\t";
				}
				userlist.clear();
				userIndex.put(key, users_reviews_concatenated);

				for(int i=0;i<value.size();i++){
					//	System.out.println("value: " + value.get(i));
					productsResult = statement.executeQuery("SELECT review FROM blips_apps WHERE appId = " + value.get(i));
					while ( productsResult.next() ) {
						String review2 = productsResult.getString("review");
						productlist.add(review2); //list of reviews for each product
					}
					List<String> values = new ArrayList<String>();
					values.add(value.get(i));
					productindex(values,productlist);
				} /*to concatenate all the reviews into one string for each product*/
				connection.close();
			}
			catch (Exception e)
			{
				System.err.println("D'oh! Got an exception!"); 
				System.err.println(e.getMessage()); 
			} 
		}

	}

	public static void productindex(List<String> values,ArrayList<String> reviews){
		
		
		String products_reviews_concatenated = "";
		for (String s2 : reviews)
		{
			products_reviews_concatenated += s2 + "\t";
		}

		reviews.clear();
		for(int i=0;i<values.size();i++){
				productIndex.put(values.get(i), products_reviews_concatenated);
		}
		//System.out.println(productIndex);
	}

	public static HashMap<String, String> getUserIndex(){
		return userIndex;
	}
	public static HashMap<String, String> getProductIndex(){
		return productIndex;
	}
} 