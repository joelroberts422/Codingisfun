package comedyanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver {

	private static String folderpath = "C:\\Users\\jrobert\\Documents\\comedyfiles\\";
	
	public static void main(String[] args) throws FileNotFoundException {
		
		File big = new File(folderpath+"completelynormal_tomsegura.txt");
		File mostlystories = new File(folderpath+"mostlystories_tomsegura.txt");
		TranscriptData bigdata = null;
		TranscriptData mostlystoriesdata = null;
		try {
			bigdata = TranscriptReader.read(big);
			mostlystoriesdata = TranscriptReader.read(mostlystories);
			
			Map<String, Double> retmap = mostlystoriesdata.relativeFrequencies(bigdata);
			
			System.out.println(retmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		List<File> files = getListOfFiles(folderpath);
//		Map<String, TranscriptData> listData = new HashMap<String, TranscriptData>();
//
//		
//		for (File f : files) {
//			TranscriptData temp = null;
//			try {
//				temp = TranscriptReader.read(f);
//				listData.put(f.getName(), temp);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		for (String s : listData.keySet()) {
//			System.out.println(s+": "+listData.get(s).getTotalWordCount());
//		}
	}
	
	private static List<File> getListOfFiles(String dirName) {
        List<File> fileNames = new ArrayList<>();
        File[] files = new File(dirName).listFiles(File::isFile);
    	fileNames=Arrays.asList(files);
        return fileNames;
    }
}
