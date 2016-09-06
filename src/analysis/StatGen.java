package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import recommendationEngine.Cast;

public class StatGen {
	ArrayList<String> stories;
	ArrayList<String> stopWords;
	
	public StatGen(ArrayList<Cast> casts) throws IOException{
		stories = new ArrayList<>();
		for (Cast cast : casts){
			stories.add(cast.getExplanation() + " " + cast.getName());
		}
		stopWords = new ArrayList<String>();
		File homedir = new File(System.getProperty("user.home"));
		BufferedReader br = new BufferedReader(new FileReader(homedir.toString() + "/workspace/Dissertation/src/analysis/stopwords.txt"));
		String line = "";
		while ((line = br.readLine())!=null){
			stopWords.add(line);
		}
		br.close();
	}
	public double getAverageDocLength(){
		double docLength = 0.0;
		for (String story : stories){
			String[] storyList = story.split(" ");
			docLength += storyList.length;
		}
		return docLength/stories.size();
	}
	public int getMaxLength(){
		int maxLen = 0;
		for (String story : stories){
			int storyLen = story.split(" ").length;
			if (storyLen > maxLen){
				maxLen = storyLen;
			}
		}
		return maxLen;
		
	}
	public int getMinLength(){
		int minLen = getMaxLength();
		for (String story : stories){
			int storyLen = story.split(" ").length;
			if (storyLen < minLen){
				minLen = storyLen;
			}
		}
		return minLen;
		
	}
	public double getStdDev(){
		double docLength = getAverageDocLength();
		double stdDev = 0.0;
		for (String story : stories){
			double storyLen = story.split(" ").length;
			stdDev += Math.abs(docLength - storyLen);
		}
		return stdDev/stories.size();
	}
	public double averageNoOfShareWords(){
		ArrayList<Double> documentNoOfWordShared = new ArrayList<>();
		int i = 0;
		while (i<stories.size()){
			String doc1 = stories.get(i);
			double noOfWordsShared = 0.0;
			int j = 0;
			while(j<stories.size()){
				String doc2 = stories.get(j);
				if (i==j){
					j++;
					if (j == stories.size()){
						break;
					}
					doc2 = stories.get(j);
				}
				noOfWordsShared+=noOfWordsShared(doc1, doc2);
				j++;
			}
			documentNoOfWordShared.add(noOfWordsShared/stories.size());
			i++;
		}
		double average = 0.0;
		for (double dubz : documentNoOfWordShared){
			average+=dubz;
		}
		return average/documentNoOfWordShared.size();
		
	}
	
	private double noOfWordsShared(String doc1, String doc2){
		double sw = 0;
		String[] doc1Words = doc1.split(" ");
		String[] doc2Words = doc2.split(" ");
		for (String word1 : doc1Words){
			for (String word2 : doc2Words){
				if (word1.equals(word2)&&!stopWords.contains(word1)){
					sw += 1;
				}
			}
				
		}
		return sw;
	}
	
	private double totalWords(){
		double words = 0.0;
		for (String story : stories){
			words+=story.split(" ").length;
		}
		return words;
	}
}
