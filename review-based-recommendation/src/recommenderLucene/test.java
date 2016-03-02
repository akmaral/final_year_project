package recommenderLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.LockObtainFailedException;

import dataset_generation.save_dataset_into_file;

public class test {

	static HashMap<String, String> product_index = new HashMap<String,String>();
	static HashMap<String, List<String>> recommendations = new HashMap<String, List<String>>();
	static List<String> listOfStopwords = new ArrayList<String>();

	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException
	{

		stopwords2 stopwords = new stopwords2();
		listOfStopwords = stopwords.list_of_stopwords();

		File sourceDirectory = new File("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review-based-recommendation/productIndex");

		for (File f : sourceDirectory.listFiles()) {
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {

				for(String line; (line = br.readLine()) != null; ) {
					String[] lines = line.split("\\t");
					String newString = "";
					if(lines.length>1){
						StringBuilder strBuilder = new StringBuilder();
						for(int i=1;i<lines.length;i++){
							strBuilder.append(lines[i]);
							newString = strBuilder.toString();
						}
					}
					if(!product_index.containsKey(lines[0])){
						product_index.put(lines[0], newString);
					}
				}
			}
		}

		Indexer_rec indexer = new Indexer_rec();

		indexer.productIndex(product_index,new File("/Users/akmaralakhanova/Documents/ucd_my_workspace/"
				+ "review-based-recommendation/productIndexOut"),listOfStopwords);

		querySearch q = new querySearch("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review-based-recommendation/userIndex");
		q.queryMaker(listOfStopwords);	

		recommendations = q.get_recommendations();
		save_dataset_into_file.saveDatasetIntoFile(recommendations, "50-recommendations.txt");
	}
}
