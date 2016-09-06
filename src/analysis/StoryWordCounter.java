package analysis;

import java.util.ArrayList;
import java.util.HashMap;

import recommendationEngine.Cast;

public class StoryWordCounter {
	private ArrayList<Cast> casts;
	private HashMap<String, Double> storyWordCount;
	private HashMap<String, Double> wordCount;
	
	public StoryWordCounter(ArrayList<Cast> casts){
		this.casts = casts;
		this.storyWordCount = countStories();
		this.wordCount = wordCount();
	}
	
	public HashMap<String, Double> getWordCount(){
		return storyWordCount;
	}
	
	public double wordCount(String word){
		return wordCount.get(word);
	}
	
	private HashMap<String, Double> wordCount(){
		HashMap<String, Double> wordCount = new HashMap<>();
		String allStories = "";
		for (Cast cast : casts){
			String story = cast.getName() + " " + cast.getExplanation();
			allStories = allStories + " " + story;
		}
		String[] splits = allStories.split(" ");
		
		for (String word1 : splits){			
			String word = word1.toLowerCase().replaceAll("[^a-zA-Z]", "");
			if (wordCount.containsKey(word)){				
				double inc = wordCount.get(word) + 1.0;
				wordCount.put(word, inc);				
			}
			else {
				wordCount.put(word, 1.0);
			}
		}
		return wordCount;
	}
	
	private HashMap<String, Double> countStories(){
		HashMap<String, Double> wordCount = new HashMap<>();
		for (Cast cast : casts){
			String story = cast.getName() + " " + cast.getExplanation();
			story.trim().toLowerCase().replaceAll("[^a-zA-Z]", "");
			String[] words = story.split(" ");
			wordCount.put(cast.getCastId(), Double.parseDouble("" + words.length));
		}
		return wordCount;
	}
}
