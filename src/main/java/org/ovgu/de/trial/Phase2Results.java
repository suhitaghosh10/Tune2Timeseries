/**
 * 
 */
package org.ovgu.de.trial;

import java.util.List;

/**
 * @author Suhita Ghosh
 *
 */
public class Phase2Results {

	String message;
	double timeTaken;
	List<IndividualPrediction> predictionList;

	public List<IndividualPrediction> getPredictionList() {
		return predictionList;
	}

	public void setPredictionList(List<IndividualPrediction> predictionList) {
		this.predictionList = predictionList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(double testTime) {
		this.timeTaken = testTime;
	}

}
