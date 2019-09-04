package comedyanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TranscriptData {
	
	private String name;
	private int totalwordcount;
	private Map<String, Integer> wordfrequencies;
	private Map<String, Double> wordprobabilities;
	private Map<String, Double> wordzscores;
	private File transcript;
	//private File cleantranscript;
	//private File counts;
	//private File frequencies;
	
	public TranscriptData(File transcript) throws FileNotFoundException {
		this.transcript = transcript;
		name = transcript.getName();
		File cleantranscript = TranscriptReader.cleanFile(transcript.getName());
		wordfrequencies = TranscriptReader.frequencyMap(cleantranscript);
		wordprobabilities = TranscriptReader.probabilityMap(wordfrequencies);
		
		totalwordcount = 0;
		for (String s : wordfrequencies.keySet())
			totalwordcount += wordfrequencies.get(s);
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, Integer> getWordFrequencies() {
		return wordfrequencies;
	}
	
	public Map<String, Double> getWordProbabilities() {
		return wordprobabilities;
	}
	
	public File getTranscript() {
		return transcript;
	}
	
	public int getTotalWordCount() {
		return totalwordcount;
	}
	
	public Map<String, Double> relativeFrequencies(TranscriptData compare) {
		Map<String, Integer> comparemap = compare.getWordFrequencies();
		int comparesize = compare.getTotalWordCount();
		Map<String, Double> zscores = new HashMap<String, Double>();
		
		for (String s : wordfrequencies.keySet()) {
			Integer c = comparemap.get(s);
			if(c == null) {
				System.out.println(s);
				c = 0;
			}
			double z = twoPropZTest(c, comparesize, wordfrequencies.get(s), totalwordcount);
			zscores.put(s, z);
		}
		
		wordzscores = zscores;
		wordzscores = TranscriptReader.sortByValue(wordzscores);
		return wordzscores;
	}
	
	private double twoPropZTest(int x1, int n1, int x2, int n2) {
		double p1 = (double)x1/n1;
		double p2 = (double)x2/n2;
		double p =  (double)(x1+x2)/(n1+n2);
		double z = (p2-p1)/(Math.sqrt(p*(1-p)*((1.0/n1)+(1.0/n2))));
		return z;
	}
}