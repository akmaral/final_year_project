package dataset_generation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class main {

	public static void main(String[] args) throws IOException {

		HashMap<String,List<String>> complete_dataset = new HashMap<String,List<String>>();
		HashMap<String,List<String>> training_dataset = new HashMap<String,List<String>>();
		HashMap<String,List<String>> testing_dataset = new HashMap<String,List<String>>();
		HashMap<String,String> userIndex = new HashMap<String,String>();
		HashMap<String,String> productIndex = new HashMap<String,String>();

		read_from_file readff = new read_from_file("Resource/tester");
		complete_dataset = readff.get_complete_dataset();
		for(int i=0;i<1;i++){
			
			split_dataset splitDataset = new split_dataset(complete_dataset);

			training_dataset = split_dataset.getTrainingDataset();
			testing_dataset = split_dataset.getTestingDataset();

			save_dataset_into_file.saveDatasetIntoFile(training_dataset, "Resource/training_dataset-"+i+".txt");
			save_dataset_into_file.saveDatasetIntoFile(testing_dataset, "Resource/testing_dataset-"+i+".txt");
			
			user_product_index.indexGeneration(training_dataset);
			user_product_index.indexGeneration(testing_dataset);

			userIndex = user_product_index.getUserIndex();
			productIndex = user_product_index.getProductIndex();
			
			save_dataset_into_file.saveIndexesIntoFile(userIndex, "Resource/user_index-"+i+".txt");
			save_dataset_into_file.saveIndexesIntoFile(productIndex, "Resource/product_index-"+i+".txt");

		}
	}
}