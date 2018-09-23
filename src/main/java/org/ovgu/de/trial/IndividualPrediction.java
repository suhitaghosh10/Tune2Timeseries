/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class IndividualPrediction {

	String predictedValue;
	String trueValue;

	public IndividualPrediction(String predictedClass) {
		super();
		this.predictedValue = predictedClass;
	}

	public IndividualPrediction( String trueClass, String predictedClass) {
		super();
		this.predictedValue = predictedClass;
		this.trueValue = trueClass;
	}

}
