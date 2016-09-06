/*
 * This component of the recommender system makes recommendations 
 */

package recommendationEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class DistanceRecommender implements RecommenderComponent {
	
	private ArrayList<Cast> allCasts;
	private HashMap<String, Double> similarities;
	
	public DistanceRecommender(ArrayList<Cast> allCasts, Cast baseCast){
		this.allCasts = allCasts;
		similarities = calculateSimilarities(baseCast);
	}	

	/*
	 * calculate distances between baseCast and all other casts
	 */
	private HashMap<String, Double> getDists(Cast baseCast){
		HashMap<String, Double> dists = new HashMap<>();
		double baseLat = baseCast.getDest_lat();
		double baseLong = baseCast.getDest_lon();
		for (int i = 0; i < allCasts.size(); i++){
			Cast aCast = allCasts.get(i);
			double lat2 = (double) aCast.getDest_lat();
			double lon2 = (double) aCast.getDest_lon();
			dists.put(aCast.getCastId(), distanceBetweenPoints(baseLat, baseLong, lat2, lon2));
		}		
		return dists;
	}
	
	
	@Override
	public HashMap<String, Double>	calculateSimilarities(Cast baseCast) {
		HashMap<String, Double> dists = getDists(baseCast);
		HashMap<String, Double> sims = new HashMap<>();
		for (String el : dists.keySet()){
			sims.put(el, 1/1+(dists.get(el)));
		}
		return sims;
	}
	//gives the distance between two points in km
	private double distanceBetweenPoints(double lat1, double lon1, double lat2, double lon2){
		
		final int R = 6371;
		
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c; // convert to meters

	    distance = Math.pow(distance, 2);

	    return Math.sqrt(distance);
	}
	
	@Override
	public HashMap<String, Double> getSimilarities() {
		return similarities;
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
		System.out.println("stdDev" + Math.sqrt(stdDev/hm.size()));
		return Math.sqrt(stdDev/hm.size());
		
	}
}
