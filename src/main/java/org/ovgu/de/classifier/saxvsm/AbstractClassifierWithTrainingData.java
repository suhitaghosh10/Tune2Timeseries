package org.ovgu.de.classifier.saxvsm;
/*
@author: ajb

Extends the AbstractClassifier to store information about the training phase of 
the classifier. The minimium any classifier that extends this should store
is the build time in buildClassifier, through calls to System.currentTimeMillis()  
at the start and end. 

the method getParameters() can be enhanced to include any parameter info for the 
final classifier. getParameters() is called to store information on the second line
of file storage format testFoldX.csv.

ClassifierResults trainResults can also store other information about the training,
including estimate of accuracy, predictions and probabilities. NOTE that these are 
assumed to be set through nested cross validation in buildClassifier or through
out of bag estimates where appropriate. IT IS NOT THE INTERNAL TRAIN ESTIMATES.

If the classifier performs some internal parameter optimisation, then ideally 
there should be another level of nesting to get the estimates. IF THIS IS NOT DONE,
SET THE VARIABLE fullyNestedEstimates to false. The user can do what he wants 
with that info

Also note: all values in trainResults are set without any reference to the train 
set at all. All the variables for trainResults are set in buildClassifier, which 
has no access to test data at all. It is completely decoupled. 

Instances train=//Get train

AbstractClassifierWithTrainingData c= //Get classifier
c.buildClassifier(train)    //ALL STATS SET HERE


 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.ovgu.de.classifier.utility.ClassifierResults;
import org.ovgu.de.classifier.utility.InstanceTools;
import org.ovgu.de.classifier.utility.SaveParameterInfo;
import org.ovgu.de.file.OutFile;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author ajb
 */
abstract public class AbstractClassifierWithTrainingData extends AbstractClassifier implements SaveParameterInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4892501267391597305L;
	protected boolean fullyNestedEstimates = true;
	protected ClassifierResults trainResults = new ClassifierResults();

	@Override
	public String getParameters() {
		return "BuildTime," + trainResults.buildTime;
	}

	public String getTrainData() {
		StringBuilder sb = new StringBuilder("AccEstimateFromTrain,");
		sb.append(trainResults.acc).append(",");

		return "BuildTime," + trainResults.buildTime;
	}

	public abstract String buildClassifierAndSave(Instances train, Instances test, String classifierSaveLoc,
			String classifierName, int folds, String resultsPath) throws IOException;

	/**
	 * 
	 * @param train:
	 *            the standard train fold Instances from the archive
	 * @param test:
	 *            the standard test fold Instances from the archive
	 * @param c:
	 *            Classifier to evaluate
	 * @param fold:
	 *            integer to indicate which fold. Set to 0 to just use train/test
	 * @param resultsPath:
	 *            a string indicating where to store the results
	 * @return the accuracy of c on fold for problem given in train/test
	 * 
	 *         NOTES: 1. If the classifier is a SaveableEnsemble, then we save the
	 *         internal cross validation accuracy and the internal test predictions
	 *         2. The output of the file testFold+fold+.csv is Line 1:
	 *         ProblemName,ClassifierName, train/test Line 2: parameter information
	 *         for final classifier, if it is available Line 3: test accuracy then
	 *         each line is Actual Class, Predicted Class, Class probabilities
	 * 
	 * 
	 */
	public static double singleClassifierAndFold(Instances train, Instances test, Classifier c, int fold,
			String resultsPath) {
		Instances[] data = InstanceTools.resampleTrainAndTestInstances(train, test, fold);
		double acc = 0;
		int act;
		int pred;
		try {
			c.buildClassifier(data[0]);
			StringBuilder str = new StringBuilder();
			DecimalFormat df = new DecimalFormat("##.######");
			for (int j = 0; j < data[1].numInstances(); j++) {
				act = (int) data[1].instance(j).classValue();
				data[1].instance(j).setClassMissing();// Just in case ....
				double[] probs = c.distributionForInstance(data[1].instance(j));
				pred = 0;
				for (int i = 1; i < probs.length; i++) {
					if (probs[i] > probs[pred])
						pred = i;
				}
				if (act == pred)
					acc++;
				str.append(act);
				str.append(",");
				str.append(pred);
				str.append(",,");
				for (double d : probs) {
					str.append(df.format(d));
					str.append(",");
				}
				str.append("\n");
			}
			acc /= data[1].numInstances();
			System.out.println("Accuracy FOR fold" + fold + " -" + acc);
			OutFile p = new OutFile(
					resultsPath + "/testFold" + fold + "_" + train.relationName().split("-")[0] + ".csv");
			p.writeLine(train.relationName() + "," + c.getClass().getName() + ",test");
			if (c instanceof SaveParameterInfo) {
				p.writeLine(((SaveParameterInfo) c).getParameters());
			} else
				p.writeLine("No parameter info");
			p.writeLine(acc + "");
			p.writeLine(str.toString());
		} catch (Exception e) {
			System.out.println(" Error =" + e + " in method simpleExperiment" + e);
			e.printStackTrace();
			System.out.println(" TRAIN " + train.relationName() + " has " + train.numAttributes() + " attributes and "
					+ train.numInstances() + " instances");
			System.out.println(" TEST " + test.relationName() + " has " + test.numAttributes() + " attributes"
					+ test.numInstances() + " instances");

			System.exit(0);
		}
		return acc;
	}

	public abstract String applyClassifier(Instances test, String classifierModel, boolean groundTruthAvailable)
			throws Exception, FileNotFoundException;

}
