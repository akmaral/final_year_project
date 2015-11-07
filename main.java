package review_based_recommender;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> perm = new HashMap<String,ArrayList<String>>();
		HashMap<String, ArrayList<String>> dataset  = new HashMap<String,ArrayList<String>>();
		entire_dataset entiredataset = new entire_dataset();
		dataset = entiredataset.reading_from_file("Resource/toy_test.txt");
		entiredataset.saveDatasetIntoFile(dataset);
		trainingSet2 trset2 = new trainingSet2();
		for(int i=0;i<5;i++){
			perm = trset2.createTrainingSet(dataset);
			//System.out.println(perm);
			trset2.saveDatasetIntoFile(perm,"Resource" + File.separator + "training_dataset-"+i+".txt");
			perm.clear();
		}
		testing_dataset testd=new testing_dataset();
		for(int m=0;m<5;m++){
			perm = testd.createTestingSet(dataset);
			testd.saveDatasetIntoFile(perm, "Resource" + File.separator + "testing_dataset-"+m+".txt");
			perm.clear();
		}
	}

}
