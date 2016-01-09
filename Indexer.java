package review_based_recommender;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	//private File sourceDirectory;
	private File sourceDirectory = new File ("Resource/user_index-0.txt");
	private Path indexOutputDirectory = Paths.get("/Users/akmaralakhanova/Documents/ucd_my_workspace"
			+ "/review_based_recommender/Resource/result");
	private Field contents;
	private static final String FIELD_NAME = "contents";

	public Indexer(String directory, String outDirectory) {
		this.sourceDirectory = new File(directory);
		this.indexOutputDirectory = Paths.get(outDirectory);
	}
	
	public void index() throws CorruptIndexException, LockObtainFailedException, IOException {
		
		FSDirectory dir = NIOFSDirectory.open(indexOutputDirectory);
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, iwc);

		for (String f : sourceDirectory.list()) {
			Document doc = new Document();
			Field contentField = (contents);
			doc.add(contentField);
			writer.addDocument(doc);
		}
		writer.close();
	}
}