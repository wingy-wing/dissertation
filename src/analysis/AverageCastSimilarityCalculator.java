package analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * this module calculates the average similarity of each cast across all similarity measures
 * this is done to show which are the most salient data points.
 */
public class AverageCastSimilarityCalculator {
	HashMap<String, Double> averageSimilarities;
	
	public AverageCastSimilarityCalculator(ArrayList<String> fileNames) throws IOException{
		averageSimilarities = new HashMap<>();
		ArrayList<HashMap<String, Double>> arr = getHms(fileNames);
		averageSimilarities = calculateAverageSims(getHms(fileNames));
	}
	
	public HashMap<String, Double> getAverageSimilarities(){
		return averageSimilarities;
	}
	
	private double getMaxValue(HashMap<String, Double> hm){
		double max = 0.0;
		for (String el : hm.keySet()){
			if (hm.get(el)>max){
				max = hm.get(el);
			}
		}
		return max;
	}
	
	private HashMap<String, Double> normaliseBetweenZeroOne(HashMap<String, Double> hm){
		HashMap<String, Double> normedHm = new HashMap<>();
		double max = getMaxValue(hm);
		for (String el : hm.keySet()){
			if (hm.get(el)<0){
				System.out.println("One element less than 0, normalisation will be incorrect");
			}
			normedHm.put(el, hm.get(el)/max);
		}
		return normedHm;
	}
	
	private ArrayList<HashMap<String, Double>> getHms(ArrayList<String> fileNames) throws IOException{
		ArrayList<HashMap<String, Double>> hms = new ArrayList<>();
		for (String fileName : fileNames){
			HashMap<String, Double> hm = new HashMap<>();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = "";
			line = br.readLine();
			String[] casts = line.split(",");
			for (String cast : casts){
				String[] splitsies = cast.split(":");
				hm.put(splitsies[0], Double.parseDouble(splitsies[1]));
			}

			hms.add(hm);
		}
		return hms;
	}
	
	private HashMap<String, Double> calculateAverageSims(ArrayList<HashMap<String, Double>> hms){
		HashMap<String, Double> avSims = new HashMap<>();
		for (HashMap<String, Double> hm : hms){
				HashMap<String, Double> normedHm = normaliseBetweenZeroOne(hm);
				for (String el : normedHm.keySet()){
					if (avSims.containsKey(el)){
						double newSim = avSims.get(el) + normedHm.get(el);
						avSims.put(el, newSim);
					}
					else{
						avSims.put(el, normedHm.get(el));
					}
				}
			}
		return avSims;
	}
	
}
