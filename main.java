package review_based_recommender;

import java.util.HashMap;
import java.util.List;

public class main {

	public static void main(String[] args) {

		HashMap<String, List<String>> perm = new HashMap<String,List<String>>();
		HashMap<String, List<String>> dataset  = new HashMap<String,List<String>>();
		HashMap<String, List<String>> indexes = new HashMap<String,List<String>>();
		HashMap<String,String> getIndex = new HashMap<String,String>();
		
		complete_dataset completeDataset = new complete_dataset();
		dataset = completeDataset.reading_from_file("Resource/toy_test.txt"); //full dataset
		indexes = completeDataset.reading_from_file("Resource/toy_test2.txt"); //short dataset just for testing
		
		save_dataset_into_file.saveDatasetIntoFile(dataset, "Resource/complete_dataset");
		

		for(int i=0;i<5;i++){
			split_dataset.splitDataset(indexes); //for testing use indexes, but usually use dataset
			perm = split_dataset.getTrainingDataset();
//			save_dataset_into_file.saveDatasetIntoFile(perm,"Resource/training_dataset-"+i+".txt");
			user_product_index.indexGeneration(perm);
			getIndex = user_product_index.getUserIndex();
			save_dataset_into_file.saveIndexesIntoFile(getIndex,"Resource/user_index-"+i+".txt");
			perm.clear();
		}
		for(int l=0;l<5;l++){
			split_dataset.splitDataset(indexes); //for testing use indexes, but usually use dataset
			perm = split_dataset.getTestingDataset();
//			save_dataset_into_file.saveDatasetIntoFile(perm,"Resource/testing_dataset-"+l+".txt");
			user_product_index.indexGeneration(perm);
			getIndex = user_product_index.getProductIndex();
			save_dataset_into_file.saveIndexesIntoFile(getIndex,"Resource/product_index-"+l+".txt");
			perm.clear();
		}
	}

}
