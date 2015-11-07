/*entire_dataset class reads the dataset from the text file called userId_appId.txt 
 * where it has 2 columns, 1st column with user id and 2nd column with product id
 * then it combines all the values of one user together
 * so we get all the product ids that the user reviewed
 * if user 1 reviewed 6 products, then it will store 1 (user id )as the key and all the 6 values separately
 * as values in the HashMap called user_app_dataset
 * hashmap has a form of String, ArrayList<String> */
package review_based_recommender;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class entire_dataset {
	public HashMap<String, ArrayList<String>> user_app_dataset;
	public ArrayList<String> appId;
	public entire_dataset(){
		user_app_dataset = new HashMap<>();
		appId = new ArrayList<>();
	}
	//read from excel file or text file for now
	// The name of the file to open.
	/*method takes a file as an argument, reads it, splits each line, puts first value into HashMap as a key, and the rest 
	 * as the values, and returns the hashmap containing the user id and app ids that user reviewed*/
	public HashMap<String,ArrayList<String>> reading_from_file(String filename){
		String line = null;
		try {
			FileReader fileReader = 
					new FileReader(filename);
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split("\t");

				if(!user_app_dataset.containsKey(parts[0])){
					user_app_dataset.put(parts[0], new ArrayList<String>());
				}
				for(int i=1;i<parts.length;i++){
					//user_app_dataset.get(parts[0]).add(parts[i]);
					ArrayList<String> al = user_app_dataset.get(parts[0]);
					al.add(parts[i]);
					user_app_dataset.put(parts[0], al);
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
		return user_app_dataset;
	}

	public void saveDatasetIntoFile(HashMap<String, ArrayList<String>> toStore) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("Resource" + File.separator + "user_app_dataset.txt"));

			Iterator<Entry<String, ArrayList<String>>> it = toStore.entrySet().iterator();

			while(it.hasNext()) {
				Entry<String, ArrayList<String>> entry = it.next();

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
			System.out.println("\nUser/app dataset was successfully saved in the file.\n");
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
