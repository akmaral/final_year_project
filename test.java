package review_based_recommender;

import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class test {

	public static void main(String[] args) {
	    String file = "..../datatest.txt";

	    TestFileReader fr = new TestFileReader();
	    fr.imports(file);
	    System.out.println(fr.content);

	    String text = fr.content;

	    Stopwords stopwords = new Stopwords();
	    stopwords.removeStopWords(text);
	    System.out.println(stopwords.removeStopWords(text));

	}
	public static String removeStopWords(String textFile) throws Exception {
	   // CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
	    final List<String> stop_Words = Arrays.asList("fox", "the");
	    final CharArraySet stopSet = new CharArraySet(Version.LUCENE_48, stop_Words, true);
	    TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_CURRENT, new StringReader(textFile.trim()));

	    tokenStream = new StopFilter(Version.LUCENE_CURRENT, tokenStream, stopWords);
	    StringBuilder sb = new StringBuilder();
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    while (tokenStream.incrementToken()) {
	        String term = charTermAttribute.toString();
	        sb.append(term + " ");
	    }
	    return sb.toString();
	}
}