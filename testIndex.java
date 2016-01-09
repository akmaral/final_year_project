package review_based_recommender;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;


public class testIndex {

	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		Indexer indexer = new Indexer("Resource/user_index-0.txt", "indexOut");
		indexer.index();
		
		TermProcessor processor = new TermProcessor();
		processor.processDocument("indexOut");
	}
}
