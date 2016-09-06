/*This interface defines the structure of each component of our recommender system.
 * 
 * A component is passed an Array of casts, which make up the corpus of artcasts we wish to
 * make recommendations from. It is also passed the cast object that the recommendations will be based on.
 */
package recommendationEngine;

import java.util.HashMap;

public interface RecommenderComponent {	
	
	public HashMap<String, Double>  getSimilarities ();	
	
	/*
	 * this is a method that generates the HashMap of similarities. The HashMap contains the Id number
	 * of each cast and its similarity to a particular cast with Id baseCastId.
	 * This similarities in the HashMap are normalised using techniques discussed here: 
	 * http://www.inf.ed.ac.uk/teaching/courses/inf2b/learnnotes/inf2b-learn-note02-2up.pdf
	 * (find something better before final version of this)
	 * this uses STANDARD SCORES (research standard scores)
	 */
	public HashMap<String, Double>	calculateSimilarities(Cast baseCast);
	
}
