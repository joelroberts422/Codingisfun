package comedyanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class TranscriptReader {

	private static String folderpath = "C:\\Users\\jrobert\\Documents\\comedyfiles\\";
	private static String clean = "clean\\";
	private static String frequency = "frequency\\";
	private static String probability = "probability\\";
	private static String data = "data\\";

	public static TranscriptData read(String filename) throws Exception {
		File file = new File(folderpath+filename);
		return read(file);
	}
	
	public static TranscriptData read(File file) throws Exception {
		
		TranscriptData ret = new TranscriptData(file);
		String filename = file.getName();
		
		//create wordfrequency map
		Map<String, Integer> wordfrequency = ret.getWordFrequencies();
		Map<String, Double> wordprobability = ret.getWordProbabilities();
	
		String frequencyfilename = filename.replace(".txt", "_frequency.txt");
		File frequencyfile = new File(folderpath+frequency+frequencyfilename);
		if(!writeFile(frequencyfile, wordfrequency)) throw new Exception();
		
		String probabilityfilename = filename.replace(".txt", "_frequency.txt");
		File probabilityfile = new File(folderpath+probability+probabilityfilename);
		if(!writeFile(probabilityfile, wordprobability)) throw new Exception();
		
		String datafilename = filename.replace(".txt", "_data.csv");
		File datafile = new File(folderpath+data+datafilename);
		if(!writeCSV(datafile, ret)) throw new Exception();

		return ret;
	}
	
	public static <V> boolean writeFile(File file, Map<String, V> map) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		
		for (String s : map.keySet()) {
			writer.printf("%20s%25s\n", s, map.get(s));
		}
		
		writer.close();
		return true;
	}
	
	// Removes punctuation and rewrites the raw words of a file to a new text document
	public static File cleanFile(String filename) {
		File file = new File(folderpath+filename);
		String cleanfilename = filename.replace(".txt", "_clean.txt");
		File cleanfile = new File(folderpath+clean+cleanfilename);

		Scanner reader = null;
		PrintWriter writer = null;
		try {
			reader = new Scanner(file);
			writer = new PrintWriter(cleanfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// lose brackets
		while (reader.hasNext()) {
			String line = reader.nextLine();
			//System.out.println(line);
			line = line.replaceAll("\\[.*?\\]", "");
			line = line.replaceAll("\\(.*?\\)", "");
			line = line.replace(".", "");
			line = line.replace(",", "");
			line = line.replace("'", "");
			line = line.replace("!", "");
			line = line.replace("?", "");
			line = line.replace("\"", "");
			line = line.replace("–", "");
			line = line.replace("…", "");
			line = line.replace('-', ' ');
			line = line.replace("”", "");
			line = line.replace("“", "");
			line = line.replace("—", "");
			line = line.replace("‘", "");
			line = line.replace(":", "");
			line = line.replace('*', ' ');
			line = line.replace("#", "");
			line = line.replace(";", "");
			line = line.replace('_', ' ');
			line = line.replace("(", "");
			line = line.replace(")", "");
			line = line.replace("[", "");
			line = line.replace("]", "");
			line = line.replace('=', ' ');
			line = line.replace("/", "");
			line = line.replace("♪", "");
			line = line.replace("’", "");

			line = line.toLowerCase();
			//System.out.println(line);
			writer.println(line);
		}
		
		reader.close();
		writer.close();
		
		return cleanfile;
	}
	
	// From the clean file, creates a sorted word frequency map
	public static Map<String, Integer> frequencyMap(File file) throws FileNotFoundException {
		Scanner reader = new Scanner(file);
		Map<String, Integer> ret = new HashMap<String, Integer>();
		while (reader.hasNext()) {
			String word = reader.next();
			if (!ret.containsKey(word)) {
				ret.put(word, 1);
			} else {
				ret.replace(word, ret.get(word), ret.get(word)+1);
			}
		}
		reader.close();
		ret = sortByValue(ret);
		return ret;
	}
	
	// From the word frequency map returns a sorted word probability map
	public static Map<String, Double> probabilityMap(Map<String, Integer> map) {
		Map<String, Double> ret = new HashMap<String, Double>();
		
		int wordcount = 0;
		for (Integer i : map.values()) {
			wordcount += i;
		}
		
		for (String s : map.keySet()) {
			ret.put(s, ((double)map.get(s))/wordcount);
		}
		ret = sortByValue(ret);
		return ret;
	}
	
	public static boolean writeCSV(File file, TranscriptData tdata) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		Map<String, Integer> freq = tdata.getWordFrequencies();
		
		for (String s : freq.keySet()) {
			writer.printf("%s,%d,,%s,%f,\n", s, freq.get(s), s, (double)freq.get(s)/tdata.getTotalWordCount());
		}
		
		writer.close();
		return true;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
