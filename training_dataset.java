package review_based_recommender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.UnaryOperator;
//WORKS!!!!!!!!!!
public class training_dataset {
	static HashMap<String, ArrayList<String>> trainingDataset = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
	static String key;
	static ArrayList<String> value;
	public training_dataset(String filename){
		trainingDataset = new HashMap<>();
		reading_from_file(filename);
	}
	public training_dataset(){
		trainingDataset = new HashMap<>();
	}
	
	public HashMap<String, ArrayList<String>> reading_from_file(String filename){
		String line = null;
		try {
			FileReader fileReader = 
					new FileReader(filename);

			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split("\t");

				if(!trainingDataset.containsKey(parts[0])){
					trainingDataset.put(parts[0], new ArrayList<String>());
				}
				for(int i=1;i<parts.length;i++){
					trainingDataset.get(parts[0]).add(parts[i]);
				}
				//   System.out.println(user_app_dataset);
			}
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							filename + "'");                
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '" 
							+ filename + "'");                  
		}
		return trainingDataset;
}		
	public static HashMap<String, ArrayList<String>> createTrainingSet(HashMap<String, ArrayList<String>> training_dataset){
		for(Entry<String, ArrayList<String>> entry : training_dataset.entrySet()) {
			key = entry.getKey(); //string stores the user ids, called key
			value = entry.getValue(); //arraylist stores the values: app ids
		//	System.out.println("key: " + key + "\t\tvalue: " + value);
			Collections.shuffle(value);
			if(training_dataset.containsKey(key)){
				training_dataset.put(key,value);
			}
		//	System.out.println("Shuffle key: " + key + "\t\tvalue: " + value + "\n");
		
			key = entry.getKey();
			value = entry.getValue();
			double counter = value.size();
			double eightyPercent = (counter*80)/100;
			Math.round(eightyPercent);
			Random rand = new Random();
			for(int i=0;i<eightyPercent;i++){
				String r = value.get(rand.nextInt(value.size()));
				//System.out.println("random string: " + r);
				if(!temp.containsKey(key)){
					temp.put(key, new ArrayList<String>());
				}
				temp.get(key).add(r);
			}
		}
		//System.out.println(temp);
		
		return temp;
	}
	public static void saveDatasetIntoFile(HashMap<String, ArrayList<String>> toStore,String filename) {
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
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//entire_dataset ed = new entire_dataset("toy_test.txt");
//		HashMap<String, ArrayList<String>> perm = new HashMap<String,ArrayList<String>>();
//		training_dataset td = new training_dataset("Resource/user_app_dataset.txt");
//		for(int i=0;i<5;i++){
//			perm = createTrainingSet(trainingDataset);
//			saveDatasetIntoFile(perm,"Resource" + File.separator + "training_dataset-"+i+".txt");
//			perm.clear();
//		}
//		//System.out.println(perm);
//	}
}


