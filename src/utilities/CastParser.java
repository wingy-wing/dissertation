package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import recommendationEngine.Cast;

public class CastParser {
	private ArrayList<Cast> casts;
	private JSONObject JSONFile;
	
	public CastParser(String filePath){
		try {
			casts = castify(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSON(){
		return JSONFile;
	}
	
	private ArrayList<Cast> castify(String filePath) throws FileNotFoundException, IOException, ParseException{
		File homedir = new File(System.getProperty("user.home"));
		File JSONCastFile = new File(homedir, filePath);	
		JSONParser parser = new JSONParser();		
		Object obj = parser.parse(new FileReader(JSONCastFile));
		JSONObject casts = (JSONObject) obj;
		JSONFile = casts;
		ArrayList<Cast> allCasts = new ArrayList<Cast>();
		for (int i = 0; i<casts.size(); i++){
			String castId = "cast" + i;
			JSONObject cast = (JSONObject) casts.get(castId);
			try{
			allCasts.add(new Cast(castId, 
					(String) cast.get("name"),
					(String) cast.get("created"),
					(String) cast.get("artwork"),					
					(String) cast.get("explanation"),
					(double) cast.get("dest_lat"),
					(double) cast.get("dest_lon"),
					(long) cast.get("day"),
					(long) cast.get("month"),
					(long) cast.get("year")
					));
			}
			catch(Exception e){
				System.out.println("Malformed data: " + e);
				deleteEntry(filePath, castId);
				i++;				
			}
		}		
		return allCasts;
	}
	
	private void deleteEntry(String filePath, String castId) throws IOException{
		String castIdentifier = "\"" + castId + "\"";
		filePath = System.getProperty("user.home") + filePath;
		ArrayList<String> linesList = (ArrayList<String>) Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
		String lines = "";
		for (String el : linesList){
			lines = lines + el;
		}
		String dataToRemove = lines.substring(lines.indexOf(castIdentifier)).split("}")[0] + "} ,";
		System.out.println(dataToRemove);
		lines = lines.replace(dataToRemove, "");		
		int i = Integer.parseInt(castId.substring(4)) + 1;		
		while (lines.contains("cast" + i)){
			lines = lines.replace("cast"+i, "cast"+(i-1));
			System.out.println(i);
			i++;
		}		
		Writer output = null;
		File file = new File(filePath);
        output = new BufferedWriter(new FileWriter(file));
		output.write(lines);
		output.flush();
		output.close();
	}
	
	public ArrayList<Cast> getCasts(){
		return casts;
	}
}
