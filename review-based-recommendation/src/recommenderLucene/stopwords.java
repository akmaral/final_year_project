package recommenderLucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class stopwords {
	
	
	public List<String> list_of_stopwords(){
		
		final List<String> list_of_stopwords = new ArrayList<String>();
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
		return list_of_stopwords;
	}
}
