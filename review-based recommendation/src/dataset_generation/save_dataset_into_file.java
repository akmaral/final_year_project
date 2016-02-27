package dataset_generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class save_dataset_into_file {
	
	public static void saveDatasetIntoFile(HashMap<String, List<String>> dataset,String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			Iterator<Entry<String, List<String>>> it = dataset.entrySet().iterator();

			while(it.hasNext()) {
				Entry<String, List<String>> entry = it.next();

				bw.write(entry.getKey() + "\t");

				for(int i = 0; i < entry.getValue().size(); i++) {
					String value = entry.getValue().get(i);

					if(i == entry.getValue().size()-1)
						bw.write(value);
					else
						bw.write(value + "\t");
				}
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveIndexesIntoFile(HashMap<String, String> dataset,String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			Iterator<Entry<String, String>> it = dataset.entrySet().iterator();

			while(it.hasNext()) {
				Entry<String, String> entry = it.next();

				bw.write(entry.getKey() + "\t");
				bw.write(entry.getValue() + "\t");
				
				bw.newLine();
				bw.flush();
			}
			
			bw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
