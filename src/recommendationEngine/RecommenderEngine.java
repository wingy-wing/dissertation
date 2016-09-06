package recommendationEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;


public class RecommenderEngine {
	private ArrayList<Cast> allCasts;
	private ArrayList<RecommenderComponent> recommenders;
	private HashMap<String, Vector<Double>> featureVecs;
	private HashMap<String, Double> cosineSims;
	
	public RecommenderEngine (ArrayList<RecommenderComponent> recommenders, ArrayList<Cast> allCasts, Cast baseCast){
		this.recommenders = recommenders;
		this.allCasts = allCasts;
		featureVecs = calculateFeatureVecs(baseCast);
		cosineSims = sortByValue(getCosineSimilarities(baseCast));
	}
	
	public HashMap<String, Double> getCosineSims(HashMap<String, Double> hm){
		return hm;
	}
	
	private HashMap<String, Vector<Double>> calculateFeatureVecs (Cast baseCast){
		HashMap<String, Vector<Double>> fVecs = new HashMap<>();
		int j = 0;
		ArrayList<RecommenderComponent> ourRecs = new ArrayList<>();
		for (RecommenderComponent rec : recommenders){
			if (!rec.getClass().equals(TextRecommender.class)){
				ourRecs.add(rec);
			}
		}
		for (int i = 0; i < ourRecs.size(); i++){			
				HashMap<String, Double> hm = ourRecs.get(i).getSimilarities();
				for (String el : hm.keySet()){					
					Vector<Double> v = new Vector<>(ourRecs.size()-j);
					if (fVecs.keySet().contains(el)){
						v = fVecs.get(el);
					}
					
					v.add(i, hm.get(el));
					fVecs.put(el, v);
				}
			}
		return fVecs;
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
	
	private Vector<Double> normalise(Vector<Double> v){
		double mean = getMean(v);
		double stdDev = getStdDev(v);
		Vector<Double> normed = new Vector<>();
		for (int i =0; i<v.size(); i++){
			normed.add(i,(mean - v.get(i))/stdDev);
		}
		return normed;
	}
	
	private double getStdDev(Vector<Double> v){
		double mean = getMean(v);
		double stdDev = 0.0;
		for (double el : v){
			stdDev+= Math.pow(mean - el, 2);
		}
		return Math.sqrt(stdDev/v.size());
	}
	
	private double getMean(Vector<Double> v){
		double mean = 0;
		for (double el : v){
			mean += el;
		}
		return (mean/v.size());
	}
	
	private <K, V extends Comparable<? super V>> HashMap<K, V>  sortByValue( HashMap<K, V> HashMap ){
		LinkedList<HashMap.Entry<K, V>> list = new LinkedList<HashMap.Entry<K, V>>( HashMap.entrySet() );
		Collections.sort( list, new Comparator<HashMap.Entry<K, V>>(){
			public int compare( HashMap.Entry<K, V> o1, HashMap.Entry<K, V> o2 ){
				return (o1.getValue()).compareTo( o2.getValue() );
			}
		});

		HashMap<K, V> result = new LinkedHashMap<K, V>();
		for (HashMap.Entry<K, V> entry : list){
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	private HashMap<String, Double> getCosineSimilarities(Cast baseCast){
		Vector<Double> fV = featureVecs.get(baseCast.getCastId());
		HashMap<String, Double> sims = new HashMap<>();
		for (String el : featureVecs.keySet()){
			sims.put(el, cosineSimilarity(fV, featureVecs.get(el)));
		}
		for (RecommenderComponent rec : recommenders){
			if (rec.getClass().equals(TextRecommender.class)){
				for (String el : rec.getSimilarities().keySet()){
					if (sims.containsKey(el)){
						double newSim = sims.get(el) + rec.getSimilarities().get(el);
						sims.put(el, newSim);
					}
					else{
						sims.put(el, rec.getSimilarities().get(el));
					}

				}
			}
		}
		return sims;
	}
	
	private Double cosineSimilarity(Vector<Double> v1, Vector<Double> v2){
		Double top = 0.0;
		Double bot1 = 0.0;
		Double bot2 = 0.0;
		for (int i = 0; i<v1.size(); i++){
			top += v1.get(i) * v2.get(i);
			bot1 += v1.get(i) * v1.get(i);
			bot2 += v2.get(i) * v2.get(i);
		}
		return top/(Math.sqrt(bot1)*Math.sqrt(bot2));
	}
	
	public HashMap<String, Double> getCosineSims(){
		return cosineSims;
	}
}
	
