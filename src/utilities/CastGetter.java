package utilities;

import java.util.ArrayList;

import recommendationEngine.Cast;

public class CastGetter {
	private ArrayList<Cast> casts;
	
	public CastGetter(ArrayList<Cast> casts){
		this.casts = casts;
	}
	
	public Cast getCastFromString(String castId){
		Cast theCast = new Cast();
		for (Cast cast : casts){
			if (cast.getCastId().equals(castId)){
				theCast = cast;
			}
		}
		return theCast;
	}
}
