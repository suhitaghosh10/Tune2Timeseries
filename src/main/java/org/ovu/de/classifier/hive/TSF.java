package org.ovu.de.classifier.hive;
/** * Implementation of Deng's Time Series Forest
 Time Series Forest (TimeSeriesForest) Deng 2013: 
@article{deng13forest,
author = {H. Deng and G. Runger and E. Tuv and M. Vladimir},
 title = {A time series forest for classification and feature extraction},
 journal = {Information Sciences},
 volume = {239},
 year = {2013}

Overview: Input n series length m
for each tree
    sample sqrt(m) intervals
    build tree on these features
    ensemble the trees with majority vote

Three interval features: mean, standard deviation and slope. 
 *
 * @author ajb

This implementation may deviate from the original, as it is using the same
structure as the weka random forest. If m is the series length
buildClassifier:
    1. Pick sqrt(m) intervals
    2. Construct instances of three features
    3. build a sqrt(m) RandomTree classifiers 
classifyInstance:
    4. majority vote with sqrt(m) RandomTree classifiers
    
Splitting criteria has a tiny refinement. Ties in entropy gain
are split with a further stat called margin that measures the distance of the split point
to the closest data. So if the split value for feature f=f_1,...f_n is v the margin is defined
as

margin= min{ |f_i-v| } 
 **/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Logger;

import org.ovgu.de.classifier.boss.TrainAccuracyEstimate;
import org.ovgu.de.classifier.saxvsm.AbstractClassifierWithTrainingData;
import org.ovgu.de.classifier.saxvsm.SAXVSM;
import org.ovgu.de.classifier.utility.ClassifierResults;
import org.ovgu.de.classifier.utility.ClassifierStatsMessage;
import org.ovgu.de.classifier.utility.ClassifierTools;
import org.ovgu.de.classifier.utility.InstanceTools;
import org.ovgu.de.classifier.utility.SaveParameterInfo;
import org.ovgu.de.file.OutFile;
import org.ovgu.de.trial.Phase2Results;
import org.ovgu.de.utils.PropertiesHandler;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.meta.RotationForest;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.TechnicalInformation;

/*


 */
public class TSF extends AbstractClassifierWithTrainingData implements SaveParameterInfo, TrainAccuracyEstimate {
	private static final String TSF = "tsf.txt";
	boolean setSeed = false;
	int seed = 0;
	RandomTree[] trees;
	int numTrees = 500;
	int numFeatures;
	int[][][] intervals;
	Random rand;
	Instances testHolder;

	/*
	 * There is no benefit from internally doing the CV for this classifier, so this
	 * is just for debugging really. Somewhat tidier
	 */
	boolean trainCV = false;
	/*
	 * If nonTrain results are overwritten with each call to buildClassifier File
	 * opened on this path.
	 */
	String trainCVPath = "";

	public TSF() {
		rand = new Random();
	}

	public TSF(int s) {
		rand = new Random();
		seed = s;
		rand.setSeed(seed);
		setSeed = true;
	}

	public void setSeed(int s) {
		this.setSeed = true;
		seed = s;
		rand = new Random();
		rand.setSeed(seed);
	}

	private static Logger logger = Logger.getLogger("TSF");

	@Override
	public void writeCVTrainToFile(String train) {
		trainCVPath = train;
		trainCV = true;
	}

	@Override
	public boolean findsTrainAccuracyEstimate() {
		return trainCV;
	}

	@Override
	public ClassifierResults getTrainResults() {
		// Temporary : copy stuff into trainResults.acc here
		// trainResults.acc=ensembleCvAcc;
		// TO DO: Write the other stats
		return trainResults;
	}

	@Override
	public String getParameters() {
		return super.getParameters() + ",numTrees," + numTrees + ",numFeatures," + numFeatures;
	}

	public void setNumTrees(int t) {
		numTrees = t;
	}

	// <editor-fold defaultstate="collapsed" desc="results reported in Info Sciences
	// paper">
	static double[] reportedResults = { 0.2659, 0.2302, 0.2333, 0.0256, 0.2537, 0.0391, 0.0357, 0.2897, 0.2, 0.2436,
			0.049, 0.08, 0.0557, 0.2325, 0.0227, 0.101, 0.1543, 0.0467, 0.552, 0.6818, 0.0301, 0.1803, 0.2603, 0.0448,
			0.2237, 0.119, 0.0987, 0.0865, 0.0667, 0.4339, 0.233, 0.1868, 0.0357, 0.1056, 0.1116, 0.0267, 0.02, 0.1177,
			0.0543, 0.2102, 0.2876, 0.2624, 0.0054, 0.3793, 0.1513 };
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="problems used in Info Sciences
	// paper">
	static String[] problems = { "FiftyWords", "Adiac", "Beef", "CBF", "ChlorineConcentration", "CinCECGtorso",
			"Coffee", "CricketX", "CricketY", "CricketZ", "DiatomSizeReduction", "ECG", "ECGFiveDays", "FaceAll",
			"FaceFour", "FacesUCR", "Fish", "GunPoint", "Haptics", "InlineSkate", "ItalyPowerDemand", "Lightning2",
			"Lightning7", "Mallat", "MedicalImages", "MoteStrain", "NonInvasiveFetalECGThorax1",
			"NonInvasiveFetalECGThorax2", "OliveOil", "OSULeaf", "SonyAIBORobotSurface1", "SonyAIBORobot Surface2",
			"StarLightCurves", "SwedishLeaf", "Symbols", "Synthetic Control", "Trace", "TwoLeadECG", "TwoPatterns",
			"UWaveGestureLibraryX", "UWaveGestureLibraryY", "UWaveGestureLibraryZ", "Wafer", "WordsSynonyms", "Yoga" };
	// </editor-fold>

	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;
		result = new TechnicalInformation(TechnicalInformation.Type.ARTICLE);
		result.setValue(TechnicalInformation.Field.AUTHOR, "H. Deng, G. Runger, E. Tuv and M. Vladimir");
		result.setValue(TechnicalInformation.Field.YEAR, "2013");
		result.setValue(TechnicalInformation.Field.TITLE,
				"A time series forest for classification and feature extraction");
		result.setValue(TechnicalInformation.Field.JOURNAL, "Information Sciences");
		result.setValue(TechnicalInformation.Field.VOLUME, "239");
		result.setValue(TechnicalInformation.Field.PAGES, "142-153");

		return result;
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		long t1 = System.currentTimeMillis();
		numFeatures = (int) Math.sqrt(data.numAttributes() - 1);

		if (trainCV) {

			int numFolds = setNumberOfFolds(data);
			CrossValidator cv = new CrossValidator();
			if (setSeed)
				cv.setSeed(seed);
			cv.setNumFolds(numFolds);
			// Estimate train accuracy HERE
			TSF tsf = new TSF();
			tsf.trainCV = false;
			// trainResults=cv.crossValidateWithStats(tsf,data);
		}
		numFeatures = (int) Math.sqrt(data.numAttributes() - 1);
		intervals = new int[numTrees][][];
		trees = new RandomTree[numTrees];
		// Set up instances size and format.
		FastVector atts = new FastVector();
		String name;
		for (int j = 0; j < numFeatures * 3; j++) {
			name = "F" + j;
			atts.addElement(new Attribute(name));
		}
		// Get the class values as a fast vector
		Attribute target = data.attribute(data.classIndex());

		FastVector vals = new FastVector(target.numValues());
		for (int j = 0; j < target.numValues(); j++)
			vals.addElement(target.value(j));
		atts.addElement(new Attribute(data.attribute(data.classIndex()).name(), vals));
		// create blank instances with the correct class value
		Instances result = new Instances("Tree", atts, data.numInstances());
		result.setClassIndex(result.numAttributes() - 1);
		for (int i = 0; i < data.numInstances(); i++) {
			DenseInstance in = new DenseInstance(result.numAttributes());
			in.setValue(result.numAttributes() - 1, data.instance(i).classValue());
			result.add(in);
		}

		testHolder = new Instances(result, 0);
		DenseInstance in = new DenseInstance(result.numAttributes());
		testHolder.add(in);
		// For each tree
		for (int i = 0; i < numTrees; i++) {
			// 1. Select random intervals for tree i
			// TO DO: this may not be as published
			// IN CODE: inx = randsample(size(X,1),ceil(size(X,1)*2/2),1);%1: with
			// replacement; 0: without replacement

			intervals[i] = new int[numFeatures][2]; // Start and end
			for (int j = 0; j < numFeatures; j++) {
				intervals[i][j][0] = rand.nextInt(data.numAttributes() - 1); // Start point
				int length = rand.nextInt(data.numAttributes() - 1 - intervals[i][j][0]);// Min length 3
				intervals[i][j][1] = intervals[i][j][0] + length;
			}
			// 2. Generate and store random attributes
			for (int j = 0; j < numFeatures; j++) {
				// For each instance
				for (int k = 0; k < data.numInstances(); k++) {
					// extract the interval
					double[] series = data.instance(k).toDoubleArray();
					FeatureSet f = new FeatureSet();
					f.setFeatures(series, intervals[i][j][0], intervals[i][j][1]);
					result.instance(k).setValue(j * 3, f.mean);
					result.instance(k).setValue(j * 3 + 1, f.stDev);
					result.instance(k).setValue(j * 3 + 2, f.slope);
				}
			}
			// Set features
			/*
			 * Create and build tree using all the features. Feature selection has already
			 * occurred
			 */
			trees[i] = new RandomTree();
			trees[i].setKValue(numFeatures);
			trees[i].buildClassifier(result);
		}
		long t2 = System.currentTimeMillis();
		trainResults.buildTime = t2 - t1;
		if (trainCVPath != "") {
			OutFile of = new OutFile(trainCVPath);
			// of.writeLine(data.relationName()+",TSF,train");
			// of.writeLine(getParameters());
			// of.writeLine(trainResults.acc+"");
			double[] trueClassVals, predClassVals;
			trueClassVals = trainResults.getTrueClassVals();
			predClassVals = trainResults.getPredClassVals();
			for (int i = 1; i < data.numInstances(); i++) {
				// Basic sanity check
				if (data.instance(i).classValue() != trueClassVals[i]) {
					throw new Exception("ERROR in TSF cross validation, class mismatch!");
				}
				of.writeString((int) trueClassVals[i] + "," + (int) predClassVals[i] + ",");
				for (double d : trainResults.getDistributionForInstance(i))
					of.writeString("," + d);
				of.writeString("\n");
			}
		}

	}

	@Override
	public double[] distributionForInstance(Instance ins) throws Exception {
		double[] d = new double[ins.numClasses()];
		;
		int[] votes = new int[ins.numClasses()];
		// Build instance
		double[] series = ins.toDoubleArray();
		for (int i = 0; i < trees.length; i++) {
			for (int j = 0; j < numFeatures; j++) {
				// extract the interval
				FeatureSet f = new FeatureSet();
				f.setFeatures(series, intervals[i][j][0], intervals[i][j][1]);
				testHolder.instance(0).setValue(j * 3, f.mean);
				testHolder.instance(0).setValue(j * 3 + 1, f.stDev);
				testHolder.instance(0).setValue(j * 3 + 2, f.slope);
			}
			int c = (int) trees[i].classifyInstance(testHolder.instance(0));
			votes[c]++;
		}
		double sum = 0;
		for (int x : votes)
			sum += x;
		for (int i = 0; i < d.length; i++)
			d[i] = votes[i] / sum;
		return d;
	}

	@Override
	public double classifyInstance(Instance ins) throws Exception {
		int[] votes = new int[ins.numClasses()];
		// Build instance
		double[] series = ins.toDoubleArray();
		for (int i = 0; i < trees.length; i++) {
			for (int j = 0; j < numFeatures; j++) {
				// extract the interval
				FeatureSet f = new FeatureSet();
				f.setFeatures(series, intervals[i][j][0], intervals[i][j][1]);
				testHolder.instance(0).setValue(j * 3, f.mean);
				testHolder.instance(0).setValue(j * 3 + 1, f.stDev);
				testHolder.instance(0).setValue(j * 3 + 2, f.slope);
			}
			int c = (int) trees[i].classifyInstance(testHolder.instance(0));
			votes[c]++;
		}
		// Return majority vote
		int maxVote = 0;
		for (int i = 1; i < votes.length; i++)
			if (votes[i] > votes[maxVote])
				maxVote = i;
		return maxVote;
	}

	public static class FeatureSet {
		double mean;
		double stDev;
		double slope;
		RandomForest r;

		public void setFeatures(double[] data, int start, int end) {
			double sumX = 0, sumYY = 0;
			double sumY = 0, sumXY = 0, sumXX = 0;
			int length = end - start + 1;
			for (int i = start; i <= end; i++) {
				sumY += data[i];
				sumYY += data[i] * data[i];
				sumX += (i - start);
				sumXX += (i - start) * (i - start);
				sumXY += data[i] * (i - start);
			}
			mean = sumY / length;
			stDev = sumYY - (sumY * sumY) / length;
			slope = (sumXY - (sumX * sumY) / length);
			if (sumXX - (sumX * sumX) / length != 0)
				slope /= sumXX - (sumX * sumX) / length;
			else
				slope = 0;
			stDev /= length;
			if (stDev == 0) // Flat line
				slope = 0;
			// else
			// stDev=Math.sqrt(stDev);
			if (slope == 0)
				stDev = 0;
		}

		public void setFeatures(double[] data) {
			setFeatures(data, 0, data.length - 1);
		}

		@Override
		public String toString() {
			return "mean=" + mean + " stdev = " + stDev + " slope =" + slope;
		}
	}

	public static void main(String[] arg) throws Exception {
		FeatureSet f = new FeatureSet();
		double[] y = { 0, 4, 8, 12, 16 };
		f.setFeatures(y);

		System.out.println(f + "");
		// Set up instances size and format.
		FastVector atts = new FastVector();
		Instances train = ClassifierTools.loadData("/Users/nikhil/Desktop/KMD/ARFF/Simson.arff");
		Instances test = ClassifierTools.loadData("/Users/nikhil/Desktop/KMD/ARFF/Simson.arff");
		TSF tsf = new TSF();
		// tsf.writeCVTrainToFile("/Users/nikhil/Desktop/KMD/ARFF/");

		tsf.buildClassifier(train);
		System.out.println(
				"build ok: original atts=" + train.numAttributes() + " new atts =" + tsf.testHolder.numAttributes());
		double a = ClassifierTools.accuracy(test, tsf);
		System.out.println(" Accuracy =" + a);
		/*
		 * //Get the class values as a fast vector Attribute target
		 * =data.attribute(data.classIndex());
		 * 
		 * FastVector vals=new FastVector(target.numValues()); for(int
		 * j=0;j<target.numValues();j++) vals.addElement(target.value(j));
		 * atts.addElement(new
		 * Attribute(data.attribute(data.classIndex()).name(),vals)); //Does this create
		 * the actual instances? Instances result = new
		 * Instances("Tree",atts,data.numInstances()); for(int
		 * i=0;i<data.numInstances();i++){ DenseInstance in=new
		 * DenseInstance(result.numAttributes()); result.add(in); }
		 * result.setClassIndex(result.numAttributes()-1); Instances testHolder =new
		 * Instances(result,10); //For each tree
		 * System.out.println("Train size "+result.numInstances());
		 * System.out.println("Test size "+testHolder.numInstances());
		 */
	}

	public String buildClassifierAndSave(Instances train, Instances test, String classifierSaveLoc,
			String classifierName, int folds, String resultsPath) throws IOException {

		StringBuffer msg = new StringBuffer("Start Building classifier...\n");

		int classIndex = 0;
		// if train and test have different number of attributes then need to recreate
		// arffs
		if (train.classIndex() > test.classIndex()) {
			test = ClassifierTools.recreateArff(test, train.classIndex());
			classIndex = train.classIndex();
		} else if (train.classIndex() < test.classIndex()) {
			train = ClassifierTools.recreateArff(train, test.classIndex());
			classIndex = test.classIndex();
		}
		try {
			String hiveCtrFileName = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH") + TSF;
			File file = new File(hiveCtrFileName);
			file.createNewFile();
			Files.write(Paths.get(hiveCtrFileName), String.valueOf(classIndex).getBytes());

		} catch (IOException e2) {
			logger.severe("Rotation Forest prop File could not be created");
		}
		if (!(classifierSaveLoc.endsWith("/") || classifierSaveLoc.endsWith("\\")))
			classifierSaveLoc = classifierSaveLoc + "/";

		logger.info("Training starting...");
		long start = System.nanoTime();
		try {
			TSF tsf = new TSF();
			double accuracy = singleClassifierAndFold(train, test, tsf, folds, resultsPath);

			Evaluation eval = new Evaluation(test);
			PlainText forPredictionsPrinting = new PlainText();
			forPredictionsPrinting.setBuffer(new StringBuffer());

			CSV output = new CSV();
			output.setHeader(new Instances(test, 0));
			output.setBuffer(new StringBuffer());

			eval.evaluateModel(tsf, test, output);

			String classDetailsString = eval.toClassDetailsString();

			weka.core.SerializationHelper.write(classifierSaveLoc + classifierName + ".model", tsf);
			double trainTime = (System.nanoTime() - start) / 1000000000.0; // seconds

			logger.info("Training done (" + trainTime + "s)");
			msg.append("End Building classifier...\\n");
			logger.info("Accuracy with " + folds + " :" + accuracy);
			msg.append("Accuracy with " + folds + " :" + accuracy + "\n");
			logger.info(classDetailsString + "\n");
			msg.append(classDetailsString + "\n");

		} catch (Exception e) {
			logger.severe("Classifier could not be built!!!" + e.getMessage());
			msg.append("Classifier could not be built!!!" + e.getMessage());
			return msg.toString();
		}
		return msg.toString();
	}

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

	public Phase2Results applyClassifier(Instances test, String classifierModel, boolean groundTruthAvailable)
			throws Exception, FileNotFoundException {

		Phase2Results results = new Phase2Results();
		
		logger.info("Testing starting...");
		StringBuffer msg = new StringBuffer("Applying ").append(this.toString()).append(" model <")
				.append(classifierModel).append("> on dataset <").append(test.relationName() + ">\n");

		TSF vsm = (TSF) SerializationHelper.read(new FileInputStream(classifierModel));
		long start = System.nanoTime();
		ClassifierStatsMessage clmsg = ClassifierTools.getClassifierPrediction(test, vsm, groundTruthAvailable);
		double testTime = (System.nanoTime() - start) / 1000000000.0; // sec
		logger.info("Testing done (" + testTime + "s)");
		//msg.append("Testing done (" + testTime + "s)\n");
		results.setTimeTaken(testTime);

		PlainText forPredictionsPrinting = new PlainText();
		forPredictionsPrinting.setBuffer(new StringBuffer());

		CSV output = new CSV();
		output.setHeader(new Instances(test, 0));
		output.setBuffer(new StringBuffer());
		msg.append(clmsg.getMessage());
		if (groundTruthAvailable) {
			logger.info(clmsg.getMessage());
			//msg.append("Accuracy : " + clmsg.getAccuracy() + "\n");
			Evaluation eval = new Evaluation(test);
			eval.evaluateModel(vsm, test, output);
			String classDetailsString = eval.toClassDetailsString();
			logger.info(classDetailsString);
			msg.append(classDetailsString + "\n");
		}
		results.setPredictionList(clmsg.getPredictionList());
		results.setMessage(msg.toString());
		return results;
	}

}
