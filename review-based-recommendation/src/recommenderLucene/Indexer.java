package recommenderLucene;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.FieldInfo;

public class Indexer {


	public void userIndex(File sourceDirectory,File indexOutputDirectory) throws CorruptIndexException, LockObtainFailedException, IOException {

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

		for (File f : sourceDirectory.listFiles()) {
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {

				for(String line; (line = br.readLine()) != null; ) {
					String[] lines = line.split("\t");

					Document doc = new Document();
					FieldType fieldType = new FieldType();
					fieldType.setIndexed(true);
					fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
					fieldType.setStored(true);
					fieldType.setStoreTermVectors(true);
					fieldType.setTokenized(true);
					Field idField = new Field("id", lines[0], fieldType);
					Field reviewField = new Field("review", lines[1], fieldType);

					doc.add(idField);
					doc.add(reviewField);
					writer.addDocument(doc);
				}
			}
		}
		writer.close();	
	}

	public void productIndex(HashMap<String,String> productindex,File indexOutputDirectory) throws IOException{
		
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
			
			Document doc = new Document();
			FieldType fieldType = new FieldType();
			fieldType.setIndexed(true);
			fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			fieldType.setStored(true);
			fieldType.setStoreTermVectors(true);
			fieldType.setTokenized(true);
			Field idField = new Field("id", key, fieldType);
			Field reviewField = new Field("review", value, fieldType);

			doc.add(idField);
			doc.add(reviewField);
			writer.addDocument(doc);
		}

		writer.close();	
	}
}
