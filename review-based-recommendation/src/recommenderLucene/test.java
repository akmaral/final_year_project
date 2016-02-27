package recommenderLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import dataset_generation.save_dataset_into_file;

public class test {


	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException
	{
		HashMap<String, List<String>> recommendations = new HashMap<String, List<String>>();
		HashMap<String, String> product_index = new HashMap<String,String>();
		
		Indexer indexer = new Indexer();

		File sourceDirectory = new File("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review-based-recommendation/productIndex");
		
		
		for (File f : sourceDirectory.listFiles()) {
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {

				for(String line; (line = br.readLine()) != null; ) {
					String[] lines = line.split("\t");
					if(!product_index.containsKey(lines[0])){
						product_index.put(lines[0], lines[1]);
					}
				}
			}
		}
		
		indexer.productIndex(product_index,new File("/Users/akmaralakhanova/Documents/ucd_my_workspace/"
				+ "review-based-recommendation/productIndexOut"));

		
		
		indexer.userIndex(new File("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review-based-recommendation/userIndex"), new File("/Users/akmaralakhanova/Documents/ucd_my_workspace/"
						+ "review-based-recommendation/userIndexOut"));

		

		List<String> listOfStopwords = new ArrayList<String>();
		
		stopwords stopwords = new stopwords();
		listOfStopwords = stopwords.list_of_stopwords();

		querySearch q = new querySearch("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review-based-recommendation/userIndex");
		q.queryMaker(listOfStopwords);	

		recommendations = q.get_recommendations();
		save_dataset_into_file.saveDatasetIntoFile(recommendations, "recommendations");
	}
}
