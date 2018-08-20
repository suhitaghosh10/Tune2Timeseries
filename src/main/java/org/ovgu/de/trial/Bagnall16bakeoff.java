/*
Code to reproduce all the results in the paper
@article{bagnall16bakeoff,
  title={The Great Time Series Classification Bake Off: a Review and Experimental Evaluation of Recent Algorithmic Advance},
  author={A. Bagnall and J. Lines and  A. Bostrom and J. Large and E. Keogh},
  journal={Data Mining and Knowledge Discovery},
  volume={Online First},
  year={2016}
}
 */
package org.ovgu.de.trial;

import java.io.File;
import java.text.DecimalFormat;

import org.ovgu.de.classifier.boss.BOSS;
import org.ovgu.de.classifier.saxvsm.SAXVSM;
import org.ovgu.de.classifier.utility.ClassifierTools;
import org.ovgu.de.classifier.utility.InstanceTools;
import org.ovgu.de.classifier.utility.SaveParameterInfo;
import org.ovgu.de.file.OutFile;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
 *
 * @author ajb
 */
public class Bagnall16bakeoff {
	/**
	 * 
	 */
	private static final String USER_STUDY_PATH = "E:\\user-study\\";
	// All classifier names
	// <editor-fold defaultstate="collapsed" desc="Directory names for all
	// classifiers">
	static String[] standard = { "NB", "C45", "SVML", "SVMQ", "Logistic", "BN", "RandF", "RotF", "MLP" };
	static String[] elastic = { "Euclidean_1NN", "DTW_R1_1NN", "DTW_Rn_1NN", "DDTW_R1_1NN", "DDTW_Rn_1NN", "ERP_1NN",
			"LCSS_1NN", "MSM_1NN", "TWE_1NN", "WDDTW_1NN", "WDTW_1NN", "DD_DTW", "DTD_C", "DTW_F" };
	static String[] shapelet = { "ST", "LS", "FS" };
	static String[] dictionary = { "BoP", "SAXVSM", "BOSS" };
	static String[] interval = { "TSF", "TSBF", "LPS" };
	static String[] ensemble = { "ACF", "PS", "EE", "COTE" };
	static String[] complexity = { "CID_ED", "CID_DTW" };
	static String[][] classifiers = { standard, elastic, shapelet, dictionary, interval, ensemble, complexity };
	static final String[] directoryNames = { "standard", "elastic", "shapelet", "dictionary", "interval", "ensemble",
			"complexity" };

	// Create classifier method
	public static Classifier setClassifier(String classifier) {
		Classifier c = null;
		switch (classifier) {
		// TIME DOMAIN CLASSIFIERS
		case "ED":
		//	c = new ED1NN();
			break;
		case "C45":
			c = new J48();
			break;
		case "NB":
			c = new NaiveBayes();
			break;
		case "SVML":
			c = new SMO();
			PolyKernel p = new PolyKernel();
			p.setExponent(1);
			((SMO) c).setKernel(p);
			break;
		case "SVMQ":
			c = new SMO();
			PolyKernel p2 = new PolyKernel();
			p2.setExponent(2);
			((SMO) c).setKernel(p2);
			break;
		case "BN":
			c = new BayesNet();
			break;
		case "MLP":
			c = new MultilayerPerceptron();
			break;
		case "RandF":
			c = new RandomForest();
		//	((RandomForest) c).setNumTrees(500);
			break;
		case "RotF":
			//c = new RotationForest();
			//((RotationForest) c).setNumIterations(50);
			break;
		case "Logistic":
			c = new Logistic();
			break;
		case "HESCA":
			//c = new CAWPE();
			break;
		// ELASTIC CLASSIFIERS
		case "DTW":
			//c = new DTW1NN();
			//((DTW1NN) c).setWindow(1);
			break;
		case "DTWCV":
			//c = new DTW1NN();
			break;
		case "DD_DTW":
			//c = new DD_DTW();
			break;
		case "DTD_C":
			//c = new DTD_C();
			break;
		case "CID_DTW":
			//c = new NN_CID();
			//((NN_CID) c).useDTW();
			break;
		case "MSM":
			//c = new MSM1NN();
			break;
		case "TWE":
			//c = new MSM1NN();
			break;
		case "WDTW":
			//c = new WDTW1NN();
			break;

		case "LearnShapelets":
		case "LS":
			//c = new LearnShapelets();
			break;
		case "FastShapelets":
		case "FS":
			//c = new FastShapelets();
			break;
		case "ShapeletTransform":
		case "ST":
		case "ST_Ensemble":
			//c = new ST_HESCA();
			break;
		case "TSF":
			//c = new TSF();
			break;
		case "RISE":
			//c = new RISE();
			break;
		case "TSBF":
			//c = new TSBF();
			break;
		case "BOP":
		case "BoP":
		case "BagOfPatterns":
			//c = new BagOfPatterns();
			break;
		case "BOSS":
		case "BOSSEnsemble":
			c = new BOSS();
			break;
		case "SAXVSM":
		case "SAX":
			c = new SAXVSM();
			break;
		case "LPS":
			//c = new LPS();
			break;
		case "COTE":
			//c = new FlatCote();
			break;
		default:
			System.out.println("UNKNOWN CLASSIFIER " + classifier);
			System.exit(0);
			// throw new Exception("Unknown classifier "+classifier);
		}
		return c;
	}

/** Run a given classifier/problem/fold combination with associated file set up
 @param args: 
 * args[0]: Classifier name. Create classifier with setClassifier
 * args[1]: Problem name
 * args[2]: Fold number. This is assumed to range from 1, hence we subtract 1
 * (this is because of the scripting we use to run the code on the cluster)
 *          the standard archive folds are always fold 0
 * 
 * NOTES: 
 * 1. this assumes you have set DataSets.problemPath to be where ever the 
 * data is, and assumes the data is in its own directory with two files, 
 * args[1]_TRAIN.arff and args[1]_TEST.arff 
 * 2. assumes you have set DataSets.resultsPath to where you want the results to
 * go It will NOT overwrite any existing results (i.e. if a file of non zero 
 * size exists)
 * 3. This method just does the file set up then calls the next method. If you 
 * just want to run the problem, go to the next method
* */
    public static void singleClassifierAndFold(String[] args){
//first gives the problem file      
        String classifier=args[0];
        String problem=args[1];
        int fold=Integer.parseInt(args[2])-1;
   
        Classifier c=setClassifier(classifier);
       Instances train = ClassifierTools.loadData("E:\\user-study\\arff\\train.arff");
		Instances test = ClassifierTools.loadData("E:\\user-study\\arff\\Subash_5_6.arff");
        
        File f=new File(USER_STUDY_PATH+classifier);
       if(!f.exists())
           f.mkdir();
        String predictions=USER_STUDY_PATH+classifier+"/Predictions";
        f=new File(predictions);
        if(!f.exists())
           f.mkdir();
        predictions=predictions+"/"+problem;
        f=new File(predictions);
        if(!f.exists())
         //   f.mkdir();
//Check whether fold already exists, if so, dont do it, just quit
		f=new File(predictions+"/testFold"+fold+".csv");
		 if(!f.exists() || f.length()==0){
		    // if(c instanceof TrainAccuracyEstimate)
		    //   ((TrainAccuracyEstimate)c).writeCVTrainToFile(predictions+"/trainFold"+fold+".csv");
            double acc =singleClassifierAndFold(train,test,c,fold,predictions);
            System.out.println(classifier+","+problem+","+fold+","+acc);
            try {
				weka.core.SerializationHelper.write("E:\\user-study\\arff\\sax_train_II.model", c);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

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
		// Save internal info for ensembles
		//if (c instanceof SaveableEnsemble)
		//	((SaveableEnsemble) c).saveResults(resultsPath + "/internalCV_" + fold + ".csv",
		//			resultsPath + "/internalTestPreds_" + fold + ".csv");
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
			OutFile p = new OutFile(resultsPath + "/testFold" + fold + ".csv");
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

	public static void main(String[] args) {
		// Example usage:

		// 1. Set up the paths
	//	DataSets.problemPath = DataSets.dropboxPath;
		//DataSets.resultsPath = "E:\\Results\\";
		// 2. Set up the arguments: Classifier, Problem, Fold
		String[] paras = { "SAXVSM", "movisens", "10" };
		// 3. Run a full experiment, saving the results
		singleClassifierAndFold(paras);

	}

}
