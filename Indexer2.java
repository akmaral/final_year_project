package review_based_recommender;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.filechooser.FileFilter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer2 {
	
	private IndexWriter writer;
	private IndexableField contents;
	private IndexableField filename;
	private IndexableField fullpath;

	public Indexer2() throws IOException { //create Lucene IndexWriter

		Path indexDir = Paths.get("/Users/akmaralakhanova/Documents/ucd_my_workspace"
				+ "/review_based_recommender/Resource");

		//	Directory dir = FSDirectory.open(new File(indexDir));

		Directory dir = FSDirectory.open(indexDir);
		Analyzer analyzer = new StandardAnalyzer();
		//writer = new IndexWriter(dir,new StandardAnalyzer(),true,IndexWriter.MAX_TERM_LENGTH);
		writer = new IndexWriter(dir,IndexWriterConfig(analyzer)); 

	}

	private IndexWriterConfig IndexWriterConfig(Analyzer analyzer) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() throws IOException { //close IndexWriter
		writer.close();
	}

	public int index(String dataDir, TextFilesFilter textFilesFilter) throws Exception {
		File[] files = new File(dataDir).listFiles();
		for (File f: files) {
			if (!f.isDirectory() &&
					!f.isHidden() &&
					f.exists() &&
					f.canRead() &&
					(textFilesFilter == null || textFilesFilter.accept(f))) {
				indexFile(f);
			}
		}
		return writer.numDocs();
	}
	private static class TextFilesFilter {//implements FileFilter {
		public boolean accept(File path) {
			return path.getName().toLowerCase().endsWith(".txt");
		}
	}
	@SuppressWarnings("deprecation")
	
	protected Document getDocument(File f) throws Exception {
		Document doc = new Document();
		String documentId = "";
		String content = "";
		doc.add(new TextField("document id", documentId, Field.Store.YES));
		doc.add(new TextField("content", content, Field.Store.YES));
		//doc.add(fullpath);
		//doc.add(new Field("contents", new FileReader(f)));
		//doc.add(new Field("filename", f.getName(), Field.Store.YES));
		//doc.add(new Field("fullpath", f.getCanonicalPath(),Field.Store.YES, Field.Index.NOT_ANALYZED));
		return doc;
	}
	
	private void indexFile(File f) throws Exception {
		System.out.println("Indexing " + f.getCanonicalPath());
		Document doc = getDocument(f);
		writer.addDocument(doc);
	}
	
	public static void main(String[] args) throws Exception {
		//		if (args.length != 2) {
		//			throw new IllegalArgumentException("Usage: java " + Indexer.class.getName() + " <index dir> <data dir>");
		//		}
		String indexDir = args[0]; 	//create index in this directory
		String dataDir = args[1]; 	//index .txt files from this directory
		long start = System.currentTimeMillis();
		Indexer2 indexer = new Indexer2();
		int numIndexed;
		try {
			numIndexed = indexer.index(dataDir, new TextFilesFilter());
		} finally {
			indexer.close();
		}
		long end = System.currentTimeMillis();
		System.out.println("Indexing " + numIndexed + " files took "
				+ (end - start) + " milliseconds");
	}
}