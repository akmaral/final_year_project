package review_based_recommender;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class main {
	
	static HashMap<String, List<String>> training_dataset = new HashMap<String,List<String>>();
	static HashMap<String, List<String>> testing_dataset  = new HashMap<String,List<String>>(); //full dataset toy-test
	static HashMap<String, List<String>> complete_dataset = new HashMap<String,List<String>>(); //short dataset toy-test 2
	static HashMap<String,String> userIndex = new HashMap<String,String>();
	static HashMap<String,String> productIndex = new HashMap<String,String>();
	static HashMap<String,String> userIndexWithoutStopwords = new HashMap<String,String>();
	static HashMap<String,String> productIndexWithoutStopwords = new HashMap<String,String>();

	
	public static void main(String[] args) throws IOException {
		
		System.out.println("=============================================================================================");
		
		complete_dataset completeDataset = new complete_dataset();
		complete_dataset = completeDataset.reading_from_file("Resource/toy_test2.txt"); //short dataset just for testing
		
		//save_dataset_into_file.saveDatasetIntoFile(dataset, "Resource/complete_dataset");
		
		for(int i=0;i<5;i++){
			split_dataset.splitDataset(complete_dataset); //for testing use indexes, but usually use dataset
			//System.out.println("\n user and review (complete dataset): " + complete_dataset);
			
			training_dataset = split_dataset.getTrainingDataset();
		//	System.out.println("training dataset: " + training_dataset + "\n");			
		//	save_dataset_into_file.saveDatasetIntoFile(perm,"Resource/training_dataset-"+i+".txt");
			
			user_product_index.indexGeneration(training_dataset);
			userIndex = user_product_index.getUserIndex();
			save_dataset_into_file.saveIndexesIntoFile(userIndex,"Resource/user_index-"+i+".txt");
			System.out.println("user index concatenated: " + userIndex);
			stopwords_remove.remove_stopwords(userIndex);
			userIndexWithoutStopwords = stopwords_remove.getUserIndexWithoutStopwords();
			System.out.println("user index without stopwords: " + userIndexWithoutStopwords);
			System.out.println("\n***********************************************************");
			
			testing_dataset = split_dataset.getTestingDataset();
			user_product_index.indexGeneration(testing_dataset);
			productIndex = user_product_index.getProductIndex();
			save_dataset_into_file.saveIndexesIntoFile(productIndex, "Resource/product_index-"+i+".txt");
			System.out.println("product index concatenated: " + productIndex);
			stopwords_remove.remove_stopwords(productIndex);
			productIndexWithoutStopwords = stopwords_remove.getProductIndexWithoutStopwords();
			System.out.println("product index without stopwords: " + productIndexWithoutStopwords);
			System.out.println("\n***********************************************************");

			training_dataset.clear();
		}
		System.out.println("=============================================================================================");

	}

}
