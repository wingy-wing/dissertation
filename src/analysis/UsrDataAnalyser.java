package analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UsrDataAnalyser {
	
	ArrayList<User> users;
	
	public UsrDataAnalyser(String filePathToCsv){
		try {
			this.users = readCsv(filePathToCsv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<User> readCsv(String filePathToCSV) throws IOException{
		ArrayList<User> users = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filePathToCSV));
		String line = "";
		while ((line = br.readLine())!=null){
			ArrayList<Double> sims = new ArrayList<>();
			ArrayList<Double> interests = new ArrayList<>();
			ArrayList<String> comments = new ArrayList<>();
			String[] data = line.split(",");
			ArrayList<String> userDeets = parseUserDetails(data);
			int i = 3;
			while (i < data.length){
				sims.add(Double.parseDouble(data[i]));
				if (data[i+1].length()!=0){
					interests.add(Double.parseDouble(data[i+1]));
					
				}
				else{
					interests.add(0.0);
				}
				if (data[i+2]!=null){
					comments.add(data[i+2]);
				}
				i+=3;
			}
			users.add(new User(userDeets, sims, interests, comments));
			
		}

		br.close();
		return users;
	}
	
	private ArrayList<String> parseUserDetails(String[] deets){
		ArrayList<String> userDetails = new ArrayList<>();
		userDetails.add(deets[0]);
		userDetails.add(deets[1]);
		userDetails.add(deets[2]);
		return userDetails;
	}
	
	public ArrayList<Double> getAverageSimilarities(){
		Double[] totalSims = new Double[users.get(0).getRatedSims().size()];
		Arrays.fill(totalSims, 0.0);
		ArrayList<Double> avgSims = new ArrayList<>();
		for (int i = 0; i < users.get(0).getRatedSims().size(); i++){
			for (User user : users){
				totalSims[i]+=user.getRatedSims().get(i);
			}
		}
		int i = 0;
		
		for (i = 0; i < totalSims.length; i++){
			avgSims.add(totalSims[i]/users.size());
		}
		return avgSims;
	}
}
