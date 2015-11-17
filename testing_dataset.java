package review_based_recommender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class testing_dataset {
	public static HashMap<String, List<String>> createTestingSet(HashMap<String, List<String>> dataset){
		List<String> random_list;
		HashMap<String, List<String>> user_product_hashmap = new HashMap<String, List<String>>();
		for(Entry<String, List<String>> entry : dataset.entrySet()) {
			String key;
			List<String> value;
			key = entry.getKey(); //string stores the user ids, called key
			value = entry.getValue(); //arraylist stores the values: app ids
			//	System.out.println("key: " + key + "\t\tvalue: " + value);
			//Collections.shuffle(value); //shuffle the values in the arraylist of the hashmap
			double counter = value.size();
			double eightyPercent = (counter*20)/100;
			int newValue = (int) Math.round(eightyPercent);
			random_list = pickNRandom(value,newValue);
			//System.out.println("random list:  " + random_list);
			
				if(!user_product_hashmap.containsKey(key)){
					user_product_hashmap.put(key, random_list);
				}
		}
		//System.out.println("user and product id hashmap: "+ user_product_hashmap);
		return user_product_hashmap;
	}
	public static List<String> pickNRandom(List<String> lst, int n) {
	    List<String> copy = new LinkedList<String>(lst);
	    Collections.shuffle(copy);
	    return copy.subList(0, n);
	}
	
	public static void saveDatasetIntoFile(HashMap<String, String> dataset,String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			Iterator<Entry<String, String>> it = dataset.entrySet().iterator();

			while(it.hasNext()) {
				Entry<String, String> entry = it.next();

				bw.write(entry.getKey() + "\t");
				bw.write(entry.getValue() + "\t");

			}
			bw.newLine();
			bw.flush();

			System.out.println("\ndataset was successfully saved in the file.\n");
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}