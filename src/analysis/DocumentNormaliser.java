package analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class DocumentNormaliser {
	
	public DocumentNormaliser(String pathToDoc, String outputPath){
		HashMap<String, Double> hm = new HashMap<String, Double>();
		try {
			hm = normalise(readLines(pathToDoc));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputToFile(outputPath, hm);
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
	
	private HashMap<String, Double> normalise(HashMap<String, Double> hm){
		HashMap<String, Double> normed = new HashMap<>();
		double max = getMax(hm);
		for(String el : hm.keySet()){
			normed.put(el, hm.get(el)/max);
		}
		return normed;
		
	}
	
	public void outputToFile(String filePath, HashMap<String, Double> hm) throws IOException{
		String lines = "";
		for (String el : hm.keySet()){
			lines += el+":"+hm.get(el)+",";
		}
		Writer output = null;
		File file = new File(filePath);
        output = new BufferedWriter(new FileWriter(file));
		output.write(lines);
		output.flush();
		output.close();
	}
	
	private double getMax (HashMap<String, Double> hm){
		double max = 0;
		for (String el : hm.keySet()){
			if (hm.get(el)>max){
				max = hm.get(el);
			}
		}
		return max;
	}
}
