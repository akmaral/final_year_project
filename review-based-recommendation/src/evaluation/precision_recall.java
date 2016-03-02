package evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import dataset_generation.save_dataset_into_file;

public class precision_recall {

	static HashMap<String,List<String>> recommendations = new HashMap<String,List<String>>();
	static HashMap<String, List<String>> testingDataset = new HashMap<String,List<String>>();
	static HashMap<String,Float> user_precision = new HashMap<String,Float>();
	static HashMap<String,Float> user_recall = new HashMap<String,Float>();
	static HashMap<String,Float> averagePrecision = new HashMap<String,Float>();
	static HashMap<String,Float> averageRecall = new HashMap<String,Float>();

	public HashMap<String,List<String>> recommendationsReading(String filename){

		String line = null;
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				if(line.endsWith("[\\n]")){
					String[] parts = line.split("[\\n]+");
					recommendations.put(parts[0], new ArrayList<String>());
				}
				else{
					String[] parts = line.split("\t");

					if(!recommendations.containsKey(parts[0])){
						recommendations.put(parts[0], new ArrayList<String>());
					}
					for(int i=1;i<parts.length;i++){
						List<String> al = recommendations.get(parts[0]);
						al.add(parts[i]);
						recommendations.put(parts[0], al);
					}
				}
			}
			//System.out.println("recommendations: " + recommendations);
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + filename + "'");                  
		}

		return recommendations;
	}

	public HashMap<String,List<String>> testingReading(String filename){

		String line = null;
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {

				if(line.endsWith("[\\n]")){
					String[] parts = line.split("[\\n]+");
					testingDataset.put(parts[0], new ArrayList<String>());
				}
				else{
					String[] parts = line.split("\t");

					if(!testingDataset.containsKey(parts[0])){
						testingDataset.put(parts[0], new ArrayList<String>());
					}
					for(int i=1;i<parts.length;i++){
						List<String> al = testingDataset.get(parts[0]);
						al.add(parts[i]);
						testingDataset.put(parts[0], al);
					}
				}
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + filename + "'");                  
		}

		return testingDataset;
	}

	public static void precision_calculation(HashMap<String,List<String>> rec, HashMap<String,List<String>> test){

		float precision  = 0;

		Iterator<Entry<String, List<String>>> recIterator = rec.entrySet().iterator();

		while(recIterator.hasNext()) {
			Entry<String, List<String>> rec_pair = (Entry<String, List<String>>) recIterator.next();
			//System.out.println("pair1: " + pair1);

			Iterator<Entry<String, List<String>>> testIterator = test.entrySet().iterator();
			while(testIterator.hasNext()) {
				Entry<String, List<String>> test_pair = (Entry<String, List<String>>) testIterator.next();
				//System.out.println("pair 2: " + pair2);
				if(rec_pair.getKey().contains(test_pair.getKey())){
					Collection<String> list_one = rec_pair.getValue();
					Collection<String> list_two = test_pair.getValue();

					Collection<List<String>> similar = new HashSet(list_one);
					similar.retainAll(list_two);
					//System.out.println("key: " + rec_pair.getKey() + "\tsimilar: " + similar);
					float union = similar.size();
					float recsize = rec_pair.getValue().size();
					//System.out.println("similar: " + similar + "\t size: " + union + "\t rec size: " + recsize);
					precision = union/recsize;
					//System.out.println("precision: " + precision);
					if(Float.isNaN(precision)){
						precision = 0;
					}
					user_precision.put(rec_pair.getKey(), precision);
				}
			}
		}
	}

	public static void recall_calculation(HashMap<String,List<String>> rec, HashMap<String,List<String>> test){

		float recall  = 0;

		Iterator<Entry<String, List<String>>> recIterator = rec.entrySet().iterator();

		while(recIterator.hasNext()) {
			Entry<String, List<String>> rec_pair = (Entry<String, List<String>>) recIterator.next();

			Iterator<Entry<String, List<String>>> testIterator = test.entrySet().iterator();
			while(testIterator.hasNext()) {
				Entry<String, List<String>> test_pair = (Entry<String, List<String>>) testIterator.next();

				if(rec_pair.getKey().contains(test_pair.getKey())){
					Collection<String> list_one = rec_pair.getValue();
					Collection<String> list_two = test_pair.getValue();

					Collection<List<String>> similar = new HashSet(list_one);
					similar.retainAll(list_two);
					//System.out.println("key: " + rec_pair.getKey() + "\tsimilar: " + similar);
					float union = similar.size();
					float testsize = test_pair.getValue().size();
					//System.out.println("similar: " + similar + "\t size: " + union + "\t test size: " + testsize);
					recall = union/testsize;
					//System.out.println("recall: " + recall);
					if(Float.isNaN(recall)){
						recall=0;
					}
					user_recall.put(rec_pair.getKey(), recall);
				}
			}
		}
	}

	public static HashMap<String,Float> get_user_precision(){
		return user_precision;

	}

	public static HashMap<String,Float> get_user_recall(){
		return user_recall;
	}

	public static void main(String[] args) throws IOException {
		for(int i=0;i<5;i++){
			precision_recall pr = new precision_recall();
			recommendations = pr.recommendationsReading("Resource/recommendations-"+i+".txt");
			testingDataset = pr.testingReading("Resource/apps_testing_dataset-"+i+".txt");
			precision_calculation(recommendations,testingDataset);
			recall_calculation(recommendations,testingDataset);
			user_precision = get_user_precision();
			user_recall = get_user_recall();
			System.out.println(user_precision);
			System.out.println(user_recall);
			float precision_sum = 0.0f;
			for (float f : user_precision.values()) {
				precision_sum += f;
			}
			float average_precision = precision_sum/user_precision.size();
			System.out.println(user_precision.size());
			System.out.println(precision_sum);
			System.out.println(average_precision);
			averagePrecision.put("text-"+i, average_precision);
			
			float recall_sum = 0.0f;
			for (float f : user_recall.values()) {
				recall_sum += f;
			}
			float average_recall = recall_sum/user_recall.size();
			System.out.println(user_recall.size());
			System.out.println(recall_sum);
			System.out.println(average_recall);
			averageRecall.put("text-"+i, average_recall);
		}
		System.out.println(averagePrecision);
		System.out.println(averageRecall);
		save_dataset_into_file.saveValuesIntoFile(averagePrecision, "average precision.txt");
		save_dataset_into_file.saveValuesIntoFile(averageRecall, "average recall.txt");
	}
}
