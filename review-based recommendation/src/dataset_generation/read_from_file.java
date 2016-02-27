package dataset_generation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class read_from_file {

	HashMap<String,List<String>> user_product_dataset = new HashMap<String,List<String>>();

	public read_from_file(){
		user_product_dataset = new HashMap<>();
	}
	public read_from_file(String filename){
		generating_complete_dataset(filename);
	}
	public void generating_complete_dataset(String filename){
		String line = null;
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split("\t");

				if(!user_product_dataset.containsKey(parts[0])){
					user_product_dataset.put(parts[0], new ArrayList<String>());
				}
				for(int i=1;i<parts.length;i++){
					List<String> al = user_product_dataset.get(parts[0]);
					al.add(parts[i]);
					user_product_dataset.put(parts[0], al);
				}
			}

			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + filename + "'");                  
		}
	}

	public HashMap<String,List<String>> get_complete_dataset(){
		return user_product_dataset;
	}
}
