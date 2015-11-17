package review_based_recommender;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
public class getDatasetFromDatabase {
	public static void main(String[] args) {
		try
		{
			// Step 1: "Load" the JDBC driver
			Class.forName("com.mysql.jdbc.Driver"); 

			// Step 2: Establish the connection to the database 
			String url = "jdbc:mysql://localhost:3306/akmaral?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";

			Connection conn = DriverManager.getConnection(url,"root","");  
			Statement stmt = conn.createStatement();
			ResultSet rs;
			HashMap<String,List<String>> user_product_dataset = new HashMap<String,List<String>>();
			rs = stmt.executeQuery("SELECT userId, appId FROM test ORDER BY 'userId'");
			while ( rs.next() ) {
				String userId = rs.getString("userId");
				String appId = rs.getString("appId");
				if(!user_product_dataset.containsKey(userId)){
					user_product_dataset.put(userId, new ArrayList<String>());
				}
				if(user_product_dataset.containsKey(userId)){
					user_product_dataset.get(userId).add(appId);
				}
				
				System.out.println(user_product_dataset);
//				if(!user_product_dataset.containsKey(userId)){
//					user_product_dataset.put(userId, appId);
//				}
				saveDataBaseIntoFile(user_product_dataset, "my_database");
			}
		} 
		catch (Exception e)
		{
			System.err.println("D'oh! Got an exception!"); 
			System.err.println(e.getMessage()); 
		} 
	}
	
	
	public static void saveDataBaseIntoFile(HashMap<String, List<String>> user_product_dataset,String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			Iterator<Entry<String, List<String>>> it = user_product_dataset.entrySet().iterator();

			while(it.hasNext()) {
				Entry<String, List<String>> entry = it.next();

				bw.write(entry.getKey() + "\t");

				for(int i = 0; i < entry.getValue().size(); i++) {
					String value = entry.getValue().get(i);

					if(i == entry.getValue().size()-1)
						bw.write(value);
					else
						bw.write(value + "\t");
				}
				bw.newLine();
				bw.flush();
			}
			System.out.println("\ndataset was successfully saved in the file.\n");
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
