package review_based_recommender;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class trainingSet2 {
	
	public static HashMap<String, List<String>> createTrainingSet(HashMap<String, List<String>> training_dataset){
		List<String> random_list;
		HashMap<String, List<String>> user_product_hashmap = new HashMap<String, List<String>>();
		for(Entry<String, List<String>> entry : training_dataset.entrySet()) {
			String key;
			List<String> value;
			key = entry.getKey(); //string stores the user ids, called key
			value = entry.getValue(); //arraylist stores the values: app ids
			//	System.out.println("key: " + key + "\t\tvalue: " + value);
			//Collections.shuffle(value); //shuffle the values in the arraylist of the hashmap
			double counter = value.size();
			double eightyPercent = (counter*80)/100;
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
	
	
}


