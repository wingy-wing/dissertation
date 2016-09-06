package recommendationEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
/* this gives the similarities between casts according to their tfidf vectors with reference to the 
 * complete corpus of artcasting documents.
 * NOTE that it references a python script. It may not do this in such a way that it can run on all systems.
 * You probably need to increase the number of arguments this part of the program sends/receives to the python
 * script - right now the script parses in the JSONObject to generate its recs. Look at passing it an array 
 * of stories or something
 */
public class TextRecommender implements RecommenderComponent{
	
	private HashMap<String, Double> similarities;
	String recommendationType;	
	
	public TextRecommender(ArrayList<Cast> allCasts, Cast baseCast, String recommendationType){
		this.recommendationType = recommendationType;
		this.similarities = calculateSimilarities(baseCast);
	}

	@Override
	public HashMap<String, Double> getSimilarities() {
		return similarities;
	}

	@Override
	public HashMap<String, Double> calculateSimilarities(Cast baseCast) {
		File homedir = new File(System.getProperty("user.home"));
		HashMap<String, Double> sims = new HashMap<>();
		int castNo = Integer.parseInt(baseCast.getCastId().substring(4));
		String cmd  = "python " + homedir.toString() + "/workspace/Dissertation/src/python/" + recommendationType;
		try{
			Process p = Runtime.getRuntime().exec(cmd + " " + castNo);
			System.out.println("waiting...");
			p.waitFor();
			System.out.println("finished!");
		}catch(Exception e){
			System.out.println(e);
		}
		try {
			String path = Paths.get(System.getProperty("user.home"), "/workspace/Dissertation/resources/text_sims.csv").toString();
			ArrayList<Double> weights = readLines(path);
			for (int i = 0; i<weights.size(); i++){
				sims.put("cast" + i, weights.get(i));
			}	
		} catch (IOException e) {
			System.out.println(e);
		}
		return sims;
	}
	
	private ArrayList<Double> readLines(String file) throws IOException{
		ArrayList<Double> doubles = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = br.readLine())!=null){
			String[] vals = line.split(",");
			for (String el : vals){
				try{
					doubles.add(Double.parseDouble(el));
				}catch(Exception NumberFormatException){
					doubles.add(0.0);
				}
			}
		}
		br.close();
		
		return doubles;
	}
}
