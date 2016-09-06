package recommendationEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class DateRecommender implements RecommenderComponent {

	private ArrayList<Cast> allCasts;
	private HashMap<String, Double> similarities;
	
	public DateRecommender(ArrayList<Cast> allCasts, Cast baseCast){
		this.allCasts = allCasts;
		similarities = calculateSimilarities(baseCast);
	}
	
	//returns the number of days between the base cast and all other casts
	private HashMap<String, Double> getDists(Cast baseCast){
		HashMap<String, Double> dists = new HashMap<>();
		for (int i = 0; i < allCasts.size(); i++){
			Cast aCast = allCasts.get(i);
			Integer period = baseCast.getTimeDateCreated().until(aCast.getTimeDateCreated()).getDays();
			dists.put(aCast.getCastId(),  Math.abs(period.doubleValue()));
		}
		return dists;
	}


	@Override
	public HashMap<String, Double> getSimilarities() {
		return similarities;
	}


	@Override
	public HashMap<String, Double> calculateSimilarities(Cast baseCast) {
		HashMap<String, Double> dists = getDists(baseCast);
		HashMap<String, Double> sims = new HashMap<>();
		for (String el : dists.keySet()){
			sims.put(el, 1/1+(dists.get(el)));
		}
		return sims;
	}
	
	private HashMap<String, Double> normalise(HashMap<String, Double> hm){
		double mean = getMean(hm);
		double stdDev = getStandardDeviation(hm);
		HashMap<String, Double> normed = new HashMap<>();
		for (String el : hm.keySet()){
			normed.put(el, (hm.get(el)-mean)/stdDev);
		}
		return normed;
	}
	
	private double getMean(HashMap<String, Double> hm){
		double mean = 0;
		for (String el : hm.keySet()){
			mean += hm.get(el);
		}		
		return mean/hm.size();
	}
	
	
	private double getStandardDeviation(HashMap<String, Double> hm){
		double stdDev = 0;
		double meanDist = getMean(hm);
		for (String el : hm.keySet()){
			stdDev+= Math.pow(meanDist - hm.get(el), 2);
		}
		return Math.sqrt(stdDev/hm.size());	
	}
}