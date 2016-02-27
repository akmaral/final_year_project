package dataset_generation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class main {
	
	static HashMap<String,List<String>> complete_dataset = new HashMap<String,List<String>>();
	static HashMap<String,List<String>> training_dataset = new HashMap<String,List<String>>();
	static HashMap<String,List<String>> testing_dataset = new HashMap<String,List<String>>();
	static HashMap<String,String> userIndex = new HashMap<String,String>();
	static HashMap<String,String> productIndex = new HashMap<String,String>();
	
	
	public static void main(String[] args) throws IOException {

		read_from_file readff = new read_from_file("Resource/apps_dataset");
		complete_dataset = readff.get_complete_dataset();
		
		for(int i=0;i<5;i++){
			System.out.println("Round:  " + i);
			split_dataset splitDataset = new split_dataset(complete_dataset);

			training_dataset = split_dataset.getTrainingDataset();
			testing_dataset = split_dataset.getTestingDataset();
		
			save_dataset_into_file.saveDatasetIntoFile(training_dataset, "Resource/apps_training_dataset-"+i+".txt");
			save_dataset_into_file.saveDatasetIntoFile(testing_dataset, "Resource/apps_testing_dataset-"+i+".txt");
//			
//			save_dataset_into_file.saveDatasetIntoFile(training_dataset, "testing/training_dataset-"+i+".txt");
//			save_dataset_into_file.saveDatasetIntoFile(testing_dataset, "testing/testing_dataset-"+i+".txt");
			
			user_product_index.userIndexGeneration(training_dataset);
			
			//System.out.println(training_dataset);

			userIndex = user_product_index.getUserIndex();
			productIndex = user_product_index.getProductIndex();
			
			save_dataset_into_file.saveIndexesIntoFile(userIndex, "Resource/apps_user_index-"+i+".txt");			
			save_dataset_into_file.saveIndexesIntoFile(productIndex, "Resource/apps_product_index-"+i+".txt");			

			training_dataset.clear();
			testing_dataset.clear();
			userIndex.clear();
			productIndex.clear();
		}
	}
	
}