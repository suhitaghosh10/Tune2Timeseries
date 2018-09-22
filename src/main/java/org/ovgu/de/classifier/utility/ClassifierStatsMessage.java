/**
 * 
 */
package org.ovgu.de.classifier.utility;

import java.util.List;

import org.ovgu.de.trial.IndividualPrediction;

/**
 * @author Suhita Ghosh
 *
 */
public class ClassifierStatsMessage {

	private String message;
	List<IndividualPrediction> predictionList;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<IndividualPrediction> getPredictionList() {
		return predictionList;
	}

	public void setPredictionList(List<IndividualPrediction> predictionList) {
		this.predictionList = predictionList;
	}

}
