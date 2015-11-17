package review_based_recommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.tartarus.snowball.ext.PorterStemmer;

public class Stopwords_remove {

	public static void main(String args[]) throws IOException
	{
		final List<String> stop_Words = new ArrayList<String>();
		String line = "";
		try {
			FileReader fileReader = new FileReader("Resource/check2");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				stop_Words.add(line);
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + "Resource/check2.txt" + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + "Resource/check2.txt" + "'");                  
		}
		StringReader reader = new StringReader("I've a got about brand new combine harvester, and I'm giving you the key");

		Tokenizer tokenizer = new StandardTokenizer();
		tokenizer.setReader(reader);

		final StandardFilter standardFilter = new StandardFilter(tokenizer);
		final CharArraySet stopSet = new CharArraySet(stop_Words, true);
		final StopFilter stopFilter = new StopFilter(standardFilter, stopSet);

		final CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);

		stopFilter.reset();
		while(stopFilter.incrementToken()) {
			final String token = charTermAttribute.toString().toString();
			System.out.println(token);
		}

		final PorterStemmer stemmer = new PorterStemmer();

		stemmer.setCurrent("weakness");

		stemmer.stem();

		final String current = stemmer.getCurrent();

		System.out.println("current: " + current);
	}
}