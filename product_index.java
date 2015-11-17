package review_based_recommender;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry; 

public class product_index  { 
	
	static HashMap<String,String> product_Index = new HashMap<String,String>();
	
	public static HashMap<String,String> userIndexGeneration(){
		
		HashMap<String, List<String>> temp = new HashMap<String,List<String>>();
		HashMap<String, List<String>> dataset = new HashMap<String,List<String>>();
		String key;
		List<String> value;
		complete_dataset entiredataset = new complete_dataset();
		dataset = entiredataset.reading_from_file("Resource/toy_test2.txt");
		System.out.println("I am in product index");
		trainingSet2 trset = new trainingSet2();
		temp=trset.createTrainingSet(dataset);
		for(Entry<String, List<String>> entry : temp.entrySet()) {
		//	System.out.println("hashmap temp: "+temp);
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
				//	System.out.println(value.get(i));
					while ( rs.next() ) {
						String review = rs.getString("review");
						list.add(review);
						//System.out.println(value.get(i) + "  \t" + review);
					}
					for (String s : list)
					{
						concatenated += s + "\t";
					}
					product_Index.put(value.get(i),concatenated);
				}
				//System.out.println(list);
				System.out.println("product index: " + product_Index  + "\n");
			//	System.out.println("key: " + key + "\t\t"+ concatenated);
				conn.close();
			}
			catch (Exception e)
			{
				System.err.println("D'oh! Got an exception!"); 
				System.err.println(e.getMessage()); 
			} 
		}
	//	System.out.println("userIndex:\t"+userIndex);
		return product_Index;
	}
} 