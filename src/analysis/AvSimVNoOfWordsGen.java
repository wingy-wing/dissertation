package analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import recommendationEngine.Cast;

public class AvSimVNoOfWordsGen {
	private ArrayList<Cast> allCasts;
	String simType;
	public AvSimVNoOfWordsGen(String inputFile, String outputFile, String simType, ArrayList<Cast> allCasts){
		this.allCasts = allCasts;
		this.simType = simType;
		HashMap<String, Double> sims = new HashMap<String, Double>();
		try {
			sims = readLines(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Double> wordCount = getWordCounts();
		try {
			outputToFile(outputFile, sims, wordCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Double> readLines(String file) throws IOException{
		HashMap<String, Double> hm = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = br.readLine())!=null){
			String[] vals = line.split(",");
			for (String el : vals){
				try{
					String[] splitsies = el.split(":");
					hm.put(splitsies[0], Double.parseDouble(splitsies[1]));
				}catch(Exception NumberFormatException){
				}
			}
		}
		br.close();
		
		return hm;
	}
	
	private HashMap<String, Double> getWordCounts(){
		HashMap<String, Double> storyWords = new StoryWordCounter(allCasts).getWordCount();
		return storyWords;
	}
	
	private void outputToFile(String fileName, HashMap<String, Double> avSims, HashMap<String, Double> wordCount) throws IOException{

		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write("#" + simType + " NoOfWords");
		bw.newLine();
		for (String el : avSims.keySet()){
			bw.write(avSims.get(el) + " " + wordCount.get(el));
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
}
