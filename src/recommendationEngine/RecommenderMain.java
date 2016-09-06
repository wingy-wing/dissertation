package recommendationEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import analysis.StoryWordCounter;
import analysis.UsrDataAnalyser;
import utilities.CastGetter;
import utilities.CastParser;


public class RecommenderMain {
	public static void main (String[] args){
		CastParser cp = new CastParser("/workspace/Dissertation/resources/casts.json");
		ArrayList<Cast> allCasts = cp.getCasts();
		JSONObject JSON = cp.getJSON();
		CastGetter cg = new CastGetter(allCasts);
//		StoryWordCounter swc = new StoryWordCounter(allCasts);
//		HashMap<String, Double> hm = sortByValue(swc.getWordCount());
//		System.out.println(hm.keySet());
		System.out.println(cg.getCastFromString("cast18").getName() + cg.getCastFromString("cast18").getExplanation());
//		System.out.println(cg.getCastFromString("cast62").getName() + cg.getCastFromString("cast62").getExplanation());
//		System.out.println(cg.getCastFromString("cast43").getName() + cg.getCastFromString("cast43").getExplanation());
//		System.out.println(swc.wordCount("photography"));
//		System.out.println(cg.getCastFromString("cast72").getName() + cg.getCastFromString("cast72").getExplanation());
		
		
		String homedir = new File(System.getProperty("user.home")).toString();
		String dir = homedir + "/workspace/Dissertation/resources/results/Survey Cast Similarities/normed/";

		UsrDataAnalyser dave = new UsrDataAnalyser(homedir + "/usrdata.csv");
		ArrayList<Double> avSims = normalise(dave.getAverageSimilarities());


		
		HashMap<Cast, ArrayList<Cast>> surveyCasts = new HashMap<>();
		
		ArrayList<Cast> subCasts1 = new ArrayList<Cast>();		
		subCasts1.add(cg.getCastFromString("cast76"));
		subCasts1.add(cg.getCastFromString("cast95"));
		subCasts1.add(cg.getCastFromString("cast83"));
		subCasts1.add(cg.getCastFromString("cast53"));
		surveyCasts.put(cg.getCastFromString("cast25"), subCasts1);
		
		LinkedHashMap<String, Double> usrData = new LinkedHashMap<>();
		usrData.put("cast25cast76", avSims.get(0));
		usrData.put("cast25cast95", avSims.get(1));
		usrData.put("cast25cast83", avSims.get(2));
		usrData.put("cast25cast53", avSims.get(3));		
		
		ArrayList<Cast> subCasts2 = new ArrayList<Cast>();		
		subCasts2.add(cg.getCastFromString("cast77"));
		subCasts2.add(cg.getCastFromString("cast73"));
		subCasts2.add(cg.getCastFromString("cast62"));
		subCasts2.add(cg.getCastFromString("cast55"));
		surveyCasts.put(cg.getCastFromString("cast32"), subCasts2);
		
		usrData.put("cast32cast77", avSims.get(4));
		usrData.put("cast32cast73", avSims.get(5));
		usrData.put("cast32cast62", avSims.get(6));
		usrData.put("cast32cast55", avSims.get(7));

		ArrayList<Cast> subCasts3 = new ArrayList<Cast>();		
		subCasts3.add(cg.getCastFromString("cast63"));
		subCasts3.add(cg.getCastFromString("cast57"));
		subCasts3.add(cg.getCastFromString("cast52"));
		subCasts3.add(cg.getCastFromString("cast36"));
		surveyCasts.put(cg.getCastFromString("cast65"), subCasts3);
		
		usrData.put("cast65cast63", avSims.get(8));
		usrData.put("cast65cast57", avSims.get(9));
		usrData.put("cast65cast52", avSims.get(10));
		usrData.put("cast65cast36", avSims.get(11));
		
		ArrayList<Cast> subCasts4 = new ArrayList<Cast>();		
		subCasts4.add(cg.getCastFromString("cast7"));
		subCasts4.add(cg.getCastFromString("cast81"));
		subCasts4.add(cg.getCastFromString("cast71"));
		subCasts4.add(cg.getCastFromString("cast78"));
		surveyCasts.put(cg.getCastFromString("cast43"), subCasts4);
		
		usrData.put("cast43cast7", avSims.get(12));
		usrData.put("cast43cast81", avSims.get(13));
		usrData.put("cast43cast71", avSims.get(14));
		usrData.put("cast43cast78", avSims.get(15));
		
		ArrayList<Cast> subCasts5 = new ArrayList<Cast>();		
		subCasts5.add(cg.getCastFromString("cast66"));
		subCasts5.add(cg.getCastFromString("cast3"));
		subCasts5.add(cg.getCastFromString("cast60"));
		subCasts5.add(cg.getCastFromString("cast2"));
		surveyCasts.put(cg.getCastFromString("cast62"), subCasts5);
		
		usrData.put("cast62cast66", avSims.get(16));
		usrData.put("cast62cast3", avSims.get(17));
		usrData.put("cast62cast60", avSims.get(18));
		usrData.put("cast62cast2", avSims.get(19));
		
		ArrayList<Cast> subCasts6 = new ArrayList<Cast>();		
		subCasts6.add(cg.getCastFromString("cast84"));
		subCasts6.add(cg.getCastFromString("cast37"));
		subCasts6.add(cg.getCastFromString("cast96"));
		subCasts6.add(cg.getCastFromString("cast51"));
		surveyCasts.put(cg.getCastFromString("cast16"), subCasts6);
		
		usrData.put("cast16cast84", avSims.get(20));
		usrData.put("cast16cast37", avSims.get(21));
		usrData.put("cast16cast96", avSims.get(22));
		usrData.put("cast16cast51", avSims.get(23));
		
		
		
		
		ArrayList<Cast> subCasts7 = new ArrayList<Cast>();		
		subCasts7.add(cg.getCastFromString("cast31"));
		subCasts7.add(cg.getCastFromString("cast24"));
		subCasts7.add(cg.getCastFromString("cast10"));
		subCasts7.add(cg.getCastFromString("cast99"));
		surveyCasts.put(cg.getCastFromString("cast18"), subCasts7);
		
		usrData.put("cast18cast31", avSims.get(24));
		usrData.put("cast18cast24", avSims.get(25));
		usrData.put("cast18cast10", avSims.get(26));
		usrData.put("cast18cast99", avSims.get(27));
		
		ArrayList<Cast> subCasts8 = new ArrayList<Cast>();		
		subCasts8.add(cg.getCastFromString("cast34"));
		subCasts8.add(cg.getCastFromString("cast97"));
		subCasts8.add(cg.getCastFromString("cast52"));
		subCasts8.add(cg.getCastFromString("cast36"));
		surveyCasts.put(cg.getCastFromString("cast80"), subCasts8);
		
		usrData.put("cast80cast34", avSims.get(28));
		usrData.put("cast80cast97", avSims.get(29));
		usrData.put("cast80cast52", avSims.get(30));
		usrData.put("cast80cast36", avSims.get(31));
		

		
		try {
			HashMap <String, Double> tfidfSims = buildHashMap(dir+"lch_wordnet_adapted_lesk.txt");
			
			double[] corr1 = new double[usrData.size()];
			double[] corr2 = new double[tfidfSims.size()];
			int index = 0;
			for (String key : usrData.keySet()) {
			    corr1[index] = usrData.get(key);
			    corr2[index] = tfidfSims.get(key);
			    index++;
			}
			double pcorr = new PearsonsCorrelation().correlation(corr1, corr2);
			double scorr = new SpearmansCorrelation().correlation(corr1, corr2);

			System.out.println(scorr);
			System.out.println(rmse(usrData,tfidfSims));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		

	
		
		System.out.print("Index for cast:");
		String castIndex = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			castIndex = br.readLine();
		}catch (IOException e) {
			System.err.println("No Input Detected");
		}
		Cast theCast = allCasts.get(Integer.parseInt(castIndex));		
		ArrayList<RecommenderComponent> components = new ArrayList<>();
		
		String recommendationType = "tfidf.py";
		components.add(new TextRecommender(allCasts, theCast, recommendationType));
//		components.add(new DistanceRecommender(allCasts, theCast));
//		components.add(new DateRecommender(allCasts, theCast));
		RecommenderEngine recEng = new RecommenderEngine(components, allCasts, theCast);
		
		
		HashMap<String, Double> sims = recEng.getCosineSims();
		for (String el : sims.keySet()){
			System.out.println(el + " : " + sims.get(el));
		}
	}
	
	
	public static void runScript(String castNo1, String castNo2, String script){
		String realCastNo1 = castNo1.substring(4);
		String realCastNo2 = castNo2.substring(4);
		File homedir = new File(System.getProperty("user.home"));
		String cmd = "python " + homedir.toString() + "/workspace/Dissertation/src/python/AnalysisScripts/" + script;
		System.out.println(cmd);
		try{
			Process p = Runtime.getRuntime().exec(cmd + " " + realCastNo1 + " " + realCastNo2);
			System.out.println("waiting...");
			p.waitFor();
			System.out.println("finished!");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	
	public static HashMap<String, Double> buildHashMap(String doc) throws IOException{
		HashMap<String, Double> hm = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(doc));
		String line = "";
		while((line = br.readLine())!=null){
			String[] data = line.split(",");
			for (String el : data){
				String[] splitsies = el.split(":");
				hm.put(splitsies[0], Double.parseDouble(splitsies[1]));
			}			
		}
		br.close();
		return hm;
	}
	
	public static double rmse(HashMap<String, Double> usrSims, HashMap<String, Double> algoSims){
		double diff = 0.0;
		for (String el : usrSims.keySet()){
			double difference = (usrSims.get(el) - algoSims.get(el))*(usrSims.get(el) - algoSims.get(el));
			diff = diff + difference;
		}
		return Math.sqrt(diff/usrSims.size());
	}
	
	public static ArrayList<Double> normalise(ArrayList<Double> list){
		double max = 0;
		ArrayList<Double> normed = new ArrayList<Double>();
		for (Double el : list){
			if (el>max){
				max = el;
			}
		}
		for (int i = 0; i<list.size(); i++){
			normed.add(list.get(i)/max);
		}
		return normed;
	}
	
}
