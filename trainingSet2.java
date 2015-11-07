package review_based_recommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.UnaryOperator;

public class trainingSet2 {
	public HashMap<String,ArrayList<String>> createTrainingSet(HashMap<String, ArrayList<String>> training_dataset){
		HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
		for(Entry<String, ArrayList<String>> entry : training_dataset.entrySet()) {
			String key;
			ArrayList<String> value;
			key = entry.getKey(); //string stores the user ids, called key
			value = entry.getValue(); //arraylist stores the values: app ids
			//		System.out.println("key: " + key + "\t\tvalue: " + value);
			Collections.shuffle(value); //shuffle the values in the arraylist of the hashmap
			if(training_dataset.containsKey(key)){
				training_dataset.put(key,value); //put shuffled values and key into hashmap
			}
			//	System.out.println("Shuffle key: " + key + "\t\tvalue: " + value + "\n");
			double counter = value.size();
			double eightyPercent = (counter*80)/100;
			Math.round(eightyPercent);
			Random rand = new Random();
			for(int i=0;i<eightyPercent;i++){
				String r = value.get(rand.nextInt(value.size()));
				//	System.out.println("random string: " + r);
				if(!temp.containsKey(key)){
					temp.put(key, new ArrayList<String>());
				}
				if(!temp.containsValue(r)){
					temp.get(key).add(r);
				}
			}
		}
		//System.out.println(temp);
		return temp;
	}
	public void saveDatasetIntoFile(HashMap<String, ArrayList<String>> toStore,String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

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
			System.out.println("\ntraining dataset was successfully saved in the file.\n");
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}


