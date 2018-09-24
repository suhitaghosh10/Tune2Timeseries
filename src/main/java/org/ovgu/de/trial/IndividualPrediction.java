/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class IndividualPrediction {

	public String predictedValue;
	public String trueValue;

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
