package review_based_recommender;

import java.util.HashMap;
import java.util.List;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, List<String>> perm = new HashMap<String,List<String>>();
		HashMap<String, List<String>> dataset  = new HashMap<String,List<String>>();
		HashMap<String,String> indexes = new HashMap<String,String>();
		complete_dataset completeDataset = new complete_dataset();
		save_dataset_into_file save = new save_dataset_into_file();
		dataset = completeDataset.reading_from_file("Resource/toy_test.txt");
		save.saveDatasetIntoFile(dataset, "Resource/complete_dataset");
		//completeDataset.saveDatasetIntoFile(dataset);
		for(int i=0;i<5;i++){
			perm = trainingSet2.createTrainingSet(dataset);
			//System.out.println(perm);
			save.saveDatasetIntoFile(perm,"Resource/training_dataset-"+i+".txt");
			perm.clear();
		}
		for(int m=0;m<5;m++){
			perm = testing_dataset.createTestingSet(dataset);
			save.saveDatasetIntoFile(perm, "Resource/testing_dataset-"+m+".txt");
			perm.clear();
		}
		for(int i=0;i<5;i++){
			indexes=user_index.userIndexGeneration();
			save_dataset_into_file.saveIndexesIntoFile(indexes,"Resource/user_index-"+i+".txt");
		}
		for(int i=0;i<5;i++){
			indexes=product_index.userIndexGeneration();
			save_dataset_into_file.saveIndexesIntoFile(indexes,"Resource/product_index-"+i+".txt");
		}
	}

}
