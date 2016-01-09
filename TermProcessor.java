package review_based_recommender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;


public class TermProcessor {
	
	public void processDocument(String indexDirName) throws CorruptIndexException, IOException
	{
		Path indexOut = Paths.get("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review_based_recommender/Resource/result");
		
		FSDirectory dir = NIOFSDirectory.open(indexOut);
		
		IndexReader reader = IndexReader();
		TermsEnum termEnum = ((Object) reader).terms();
		
		ArrayList<String> termList = new ArrayList<String>();
		
		while (termEnum.next() != null)
		{
			String term = termEnum.term().text();
			termList.add(term);
		}
		
		System.out.printf("%-20s %-4s\t %-4s\t %-4s\t %-4s\n","Term","D1","D2","D3","D4");
		System.out.println("------------------------------------------------------------");
		for (int i = 0; i < termList.size(); i++)
		{
			System.out.printf("%-20s",termList.get(i));
			String term = termList.get(i);
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < reader.numDocs(); j++)
			{
				System.out.printf("%4.2f\t",getTFIDF(reader, term, "contents", j));
			}
			System.out.println();
		}
	}
	private IndexReader IndexReader() {
		// TODO Auto-generated method stub
		return null;
	}
	public double getTF(IndexReader reader, String term, String field, int docID) throws IOException
	{
		Terms termVector = reader.getTermVector(docID, field);
		int idx = ((List<String>) termVector).indexOf(term);
		if (idx == -1)
		{
			return 0;
		}
		else
		{	
			//int[] freqs = termVector.getTermFrequencies();
			int[] freqs = termVector.getSumTotalTermFreq();
			
			//termVector.getSumTotalTermFreq();
			return freqs[idx];
		}
	}
	
	public double getIDF(IndexReader reader, String field, String termName) throws IOException
	{
		return Math.log(reader.numDocs()/ ((double)reader.docFreq(new Term(field, termName))));
	}
	
	public double getTFIDF(IndexReader reader, String termName, String field, int docID) throws IOException
	{
		return getTF(reader, termName, field, docID) * getIDF(reader, field, termName);
	}
}

