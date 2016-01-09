package review_based_recommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.tartarus.snowball.ext.PorterStemmer;

public class stopwords_remove {

	static HashMap<String,String> userIndexWithoutStopwords = new HashMap<String,String>();
	static HashMap<String,String> productIndexWithoutStopwords = new HashMap<String,String>();


	public static void remove_stopwords(HashMap<String,String> index) throws IOException {

		final List<String> list_of_stopwords = new ArrayList<String>();
		List<String> stopwords_removed = new ArrayList<String>();
		String line="";
		try {
			FileReader fileReader = new FileReader("Resource/stopwords.txt"); // reading the list of stopwords from the text file
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				list_of_stopwords.add(line); //storing the list of stopwords in a list
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + "Resource/stopwords.txt" + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + "Resource/stopwords.txt" + "'");     

		}
		for(Entry<String, String> entry : index.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			StringReader reader = new StringReader(value.toLowerCase()); //String reader reads each value in the hashmap (each review)
			Tokenizer tokenizer = new StandardTokenizer();
			tokenizer.setReader(reader);

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

			userIndexWithoutStopwords.put(key, reviews_concatenated); //key = user ID, concatenated = string of reviews concatenated
			productIndexWithoutStopwords.put(key, reviews_concatenated); //key = product ID, concatenated = string of reviews concatenated

		}
	}

	public static HashMap<String, String> getUserIndexWithoutStopwords(){
		return userIndexWithoutStopwords;
	}

	public static HashMap<String, String> getProductIndexWithoutStopwords(){
		return productIndexWithoutStopwords;
	}
}