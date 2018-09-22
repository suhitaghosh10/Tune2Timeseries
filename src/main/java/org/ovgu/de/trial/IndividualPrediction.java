/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class IndividualPrediction {

	double predictedValue;
	double trueValue;

	public IndividualPrediction(double predictedClass) {
		super();
		this.predictedValue = predictedClass;
	}

	public IndividualPrediction( double trueClass, double predictedClass) {
		super();
		this.predictedValue = predictedClass;
		this.trueValue = trueClass;
	}

}
