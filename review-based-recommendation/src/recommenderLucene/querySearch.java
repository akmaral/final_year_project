package recommenderLucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;


public class querySearch {

	private File sourceDirectory;
	HashMap<String,List<String>> recommendations = new HashMap<>();

	public querySearch(String directory) {
		this.sourceDirectory = new File(directory);
	}

	public void queryMaker(List<String> list_of_stopwords) throws IOException, ParseException{

		List<String> stopwords_removed = new ArrayList<String>();

		for (File f : sourceDirectory.listFiles()) {
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {

				for(String line; (line = br.readLine()) != null; ) {
					System.out.println("\n"+" query search line: " + line);
					String[] parts = line.split("\t");
					String newString = "";
					if(parts.length>1){
						StringBuilder strBuilder = new StringBuilder();
						for(int i=1;i<parts.length;i++){
							strBuilder.append(parts[i]);
							newString = strBuilder.toString();
						}
					}

					StringReader strreader = new StringReader(newString.toLowerCase()); //String reader reads each value in the hashmap (each review)
					Tokenizer tokenizer = new StandardTokenizer(strreader);
					tokenizer.setReader(strreader);

					final StandardFilter standardFilter = new StandardFilter(tokenizer);
					final CharArraySet stopSet = new CharArraySet(list_of_stopwords, true);
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

					FSDirectory directory = NIOFSDirectory.open(new File("/Users/akmaralakhanova/Documents/ucd_my_workspace/"
							+ "review-based-recommendation/productIndexOut"));
					Analyzer analyzer = new StandardAnalyzer();

					System.out.println("user id: " + parts[0]);
					System.out.println(reviews_concatenated);
					QueryParser parser = new QueryParser("review", analyzer);
					String cooked;
					cooked = reviews_concatenated.replaceAll("[^\\w\\s\\(1\\+1\\)\\:2]"," ");
					Query q = parser.parse(QueryParser.escape(cooked));
					
					BooleanQuery.setMaxClauseCount(2000000);

					// Searching code
					int hitsPerPage = 10;
					IndexReader reader = DirectoryReader.open(directory);
					IndexSearcher searcher = new IndexSearcher(reader);
					TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
					searcher.search(q, collector);

					ScoreDoc[] hits = collector.topDocs().scoreDocs;
					Document d = null;
					//	Code to display the results of search
					//System.out.println("Found " + hits.length + " hits.");
					List<String> productID = new ArrayList<String>();
					for(int i=0;i<hits.length;++i) 
					{
						int docId = hits[i].doc;
						//System.out.println("Score: " + hits[i].score);
						d = searcher.doc(docId);
						//System.out.println(searcher.explain(q, docId));
						System.out.println((i + 1) + ". " + "score: " + hits[i].score + "\t" + d.get("id") + "\t" + d.get("review"));
						if(!productID.contains(d.get("id"))){
							productID.add(d.get("id"));
						}
					}
					if(!recommendations.containsValue(productID)){
						recommendations.put(parts[0], productID);
					}
					reader.close();
					directory.close();
					stopwords_removed.clear();
				}
			}
		}
	}

	public HashMap<String,List<String>> get_recommendations(){
		return recommendations;
	}
}
