/*author: Akmaral Akhanova
 * the following class called split dataset, takes as argument*/
package dataset_generation;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class split_dataset {
	
	static HashMap<String, List<String>> training_dataset = new HashMap<String, List<String>>();
	static HashMap<String, List<String>> testing_dataset = new HashMap<String, List<String>>();
	
	public split_dataset(){
		training_dataset = new HashMap<>();
		testing_dataset = new HashMap<>();
	}
	
	public split_dataset(HashMap<String,List<String>> dataset){
		splitDataset(dataset);
	}

	public static void splitDataset(HashMap<String, List<String>> dataset){
		
		List<String> training_list;
		List<String> testing_list;

		for(Entry<String, List<String>> entry : dataset.entrySet()) {
			String key;
			List<String> value;
			key = entry.getKey(); //string stores the user ids, called key
			value = entry.getValue(); //arraylist stores the values: app ids
			double counter = value.size();
			double eightyPercent = (counter*80)/100;
			int newValue = (int) Math.round(eightyPercent);
			List<String> copy = new LinkedList<String>(value);
			Collections.shuffle(copy);
			training_list = copy.subList(0, newValue);
			testing_list = copy.subList(newValue, copy.size());
			if(!training_dataset.containsKey(key)){
				training_dataset.put(key, training_list);
			}
			if(!testing_dataset.containsKey(key)){
				testing_dataset.put(key, testing_list);
			}
		}
	}
	public static HashMap<String,List<String>> getTrainingDataset(){
		return training_dataset;
	}
	public static HashMap<String,List<String>> getTestingDataset(){
		return testing_dataset;
	}	
}