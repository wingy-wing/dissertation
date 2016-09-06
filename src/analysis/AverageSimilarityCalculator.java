package analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import recommendationEngine.Cast;
import recommendationEngine.RecommenderComponent;
import recommendationEngine.RecommenderEngine;
import recommendationEngine.TextRecommender;

public class AverageSimilarityCalculator {
	private HashMap<String, Double> avgSims;
	private ArrayList<Cast> casts;
	private String recommenderType;

	public AverageSimilarityCalculator(ArrayList<Cast> casts, String recommenderType){
		this.casts = casts;
		this.recommenderType = recommenderType;
		this.avgSims = new HashMap<>();
		this.avgSims = calculateAvgSims();		
	}
	
	public void outputToFile(String filePath) throws IOException{
		String lines = "";
		for (String el : avgSims.keySet()){
			lines += el+":"+avgSims.get(el)+",";
		}
		Writer output = null;
		File file = new File(filePath);
        output = new BufferedWriter(new FileWriter(file));
		output.write(lines);
		output.flush();
		output.close();
	}
	
	public HashMap<String, Double> getAverageSims(){
		return avgSims;
	}
	
	private HashMap<String, Double> calculateAvgSims(){
		HashMap<String, Double> averageSims = new HashMap<String, Double>();
		for (Cast cast : casts){
			System.out.println(cast.getCastId());
			ArrayList<RecommenderComponent> recList = new ArrayList<>();
			recList.add(new TextRecommender(casts, cast, recommenderType));
			RecommenderEngine recEng = new RecommenderEngine(recList, casts, cast);
			HashMap<String, Double> engSims = recEng.getCosineSims();
			for (String el : engSims.keySet()){
				if (averageSims.containsKey(el)){
					double d = averageSims.get(el) + engSims.get(el);
					averageSims.put(el, d);
				}
				else{
					averageSims.put(el, engSims.get(el));
				}				
			}
		}
		for(String el : avgSims.keySet()){
			double averagedSim = avgSims.get(el)/casts.size();
			averageSims.put(el, averagedSim);
		}
		return averageSims;
	}
}
