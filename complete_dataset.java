/*complete_dataset class reads the dataset from the text file called userId_appId.txt 
 * where it has 2 columns, 1st column with user id and 2nd column with product id
 * then it combines all the values of one user together
 * so we get all the product ids that the user reviewed
 * if user 1 reviewed 6 products, then it will store 1 (user id )as the key and all the 6 values separately
 * as values in the HashMap called user_app_dataset
 * hashmap has a form of String, ArrayList<String> */
package review_based_recommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class complete_dataset {
	public HashMap<String, List<String>> user_product_dataset;
	public ArrayList<String> product_id;
	public complete_dataset(){
		user_product_dataset = new HashMap<>();
		product_id = new ArrayList<>();
	}
	
	/*method takes a file as an argument, reads it, splits each line, puts first value into HashMap as a key, and the rest 
	 * as the values, and returns the hashmap containing the user id and app ids that user reviewed*/
	public HashMap<String, List<String>> reading_from_file(String filename){
		String line = null;
		try {
			FileReader fileReader = 
					new FileReader(filename);
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split("\t");

				if(!user_product_dataset.containsKey(parts[0])){
					user_product_dataset.put(parts[0], new ArrayList<String>());
				}
				for(int i=1;i<parts.length;i++){
					//user_app_dataset.get(parts[0]).add(parts[i]);
					List<String> al = user_product_dataset.get(parts[0]);
					al.add(parts[i]);
					user_product_dataset.put(parts[0], al);
				}
				//   System.out.println(user_app_dataset);
			}

			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + filename + "'");                  
		}
		return user_product_dataset;
	}

}
