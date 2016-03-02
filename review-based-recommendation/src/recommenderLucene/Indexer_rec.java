package recommenderLucene;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;
import org.apache.lucene.index.FieldInfo;

public class Indexer_rec {      

	public void productIndex(HashMap<String,String> productindex,File indexOutputDirectory,List<String> listOfStopwords) throws IOException{
		
		List<String> stopwords_removed = new ArrayList<String>();
		Directory dir = FSDirectory.open(indexOutputDirectory);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
		//IndexWriterConfig.setSimilarity(Similarity);

		if (indexOutputDirectory.exists()) {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		} 
		else {
			// Add new documents to an existing index:
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		}

		IndexWriter writer = new IndexWriter(dir, iwc);

		for(Entry<String,String> entry: productindex.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			StringReader strreader = new StringReader(value.toLowerCase()); //String reader reads each value in the hashmap (each review)
			Tokenizer tokenizer = new StandardTokenizer(strreader);
			tokenizer.setReader(strreader);

			final StandardFilter standardFilter = new StandardFilter(tokenizer);
			final CharArraySet stopSet = new CharArraySet(listOfStopwords, true);
			final StopFilter stopFilter = new StopFilter(standardFilter, stopSet);
			final CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
			stopFilter.reset();

			while(stopFilter.incrementToken()) { //filtering in order to remove stopwords from each review
				final String token = charTermAttribute.toString().toString();
				//System.out.println("token: \t" + token);
				final PorterStemmer stemmer = new PorterStemmer(); //stemming of reviews
				stemmer.setCurrent(token);
				stemmer.stem();
				final String current = stemmer.getCurrent();
				stopwords_removed.add(current);
			}

			String reviews_concatenated = "";   //then all the reviews for each user and for each product concatenated into one single string

			for (String s : stopwords_removed)
			{
				reviews_concatenated += s + "\t";
			}
			
			Document doc = new Document();
			FieldType fieldType = new FieldType();
			fieldType.setIndexed(true);
			fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			fieldType.setStored(true);
			fieldType.setStoreTermVectors(true);
			fieldType.setTokenized(true);
			Field idField = new Field("id", key, fieldType);
			Field reviewField = new Field("review", reviews_concatenated, fieldType);

			doc.add(idField);
			doc.add(reviewField);
			writer.addDocument(doc);
			stopwords_removed.clear();
		}
		writer.close();	
	}
}