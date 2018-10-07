package org.ovgu.de.classifier.saxvsm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.ovgu.de.classifier.utility.ClassifierStatsMessage;
import org.ovgu.de.classifier.utility.ClassifierTools;
import org.ovgu.de.trial.Phase2Results;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.SparseInstance;
import weka.core.TechnicalInformation;

/**
 * Classifier using SAX and Vector Space Model.
 * 
 * Params: wordLength, alphabetSize, windowLength
 * 
 * In training, generates class weighting matrix for SAX patterns found in the
 * series, in testing uses cosine similarity to find most similar class
 * 
 * @inproceedings{senin13sax_vsm, author="P. Senin and S. Malinchik",
 * title="{SAX-VSM:} Interpretable Time Series Classification Using SAX and
 * Vector Space Model", booktitle ="Proc. 13th {IEEE ICDM}", year="2013" }
 * 
 * @author James
 */
public class SAXVSM extends AbstractClassifierWithTrainingData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3789596243813366103L;
	private static Logger logger = Logger.getLogger("SAXVSM");
	Instances transformedData;
	Instances corpus;

	private BagOfPatternsFilter bop;
	private int PAA_intervalsPerWindow;
	private int SAX_alphabetSize;
	private int windowSize;

	private final boolean useParamSearch; // does user want parameter search to be performed

	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;
		result = new TechnicalInformation(TechnicalInformation.Type.ARTICLE);
		result.setValue(TechnicalInformation.Field.AUTHOR, "P. Senin and S. Malinchik");
		result.setValue(TechnicalInformation.Field.TITLE,
				"SAX-VSM: Interpretable Time Series Classification Using SAX and Vector Space Model");
		result.setValue(TechnicalInformation.Field.JOURNAL, "Proc. 13th IEEE ICDM");
		result.setValue(TechnicalInformation.Field.YEAR, "2013");
		return result;
	}

	/**
	 * Will use parameter search during training
	 */
	public SAXVSM() {
		this.PAA_intervalsPerWindow = -1;
		this.SAX_alphabetSize = -1;
		this.windowSize = -1;

		useParamSearch = true;
	}

	/**
	 * Will build using only parameters passed
	 */
	public SAXVSM(int PAA_intervalsPerWindow, int SAX_alphabetSize, int windowSize) {
		this.PAA_intervalsPerWindow = PAA_intervalsPerWindow;
		this.SAX_alphabetSize = SAX_alphabetSize;
		this.windowSize = windowSize;

		bop = new BagOfPatternsFilter(PAA_intervalsPerWindow, SAX_alphabetSize, windowSize);

		useParamSearch = false;
	}

	public int getPAA_intervalsPerWindow() {
		return PAA_intervalsPerWindow;
	}

	public int getSAX_alphabetSize() {
		return SAX_alphabetSize;
	}

	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * @return { numIntervals(word length), alphabetSize, slidingWindowSize }
	 */
	public int[] getParametersArray() {
		return new int[] { PAA_intervalsPerWindow, SAX_alphabetSize, windowSize };
	}

	@Override
	public String getParameters() {
		return super.getParameters() + ",PAAIntervalsPerWindow," + PAA_intervalsPerWindow + ",alphabetSize,"
				+ SAX_alphabetSize + ",windowSize," + windowSize;
	}

	/**
	 * Performs cross validation on given data for varying parameter values, returns
	 * parameter set which yielded greatest accuracy
	 * 
	 * @param data
	 *            Data to perform cross validation testing on
	 * @return { numIntervals, alphabetSize, slidingWindowSize }
	 */
	public static int[] parameterSearch(Instances data) throws Exception {
		double bestAcc = -1.0;
		int bestAlpha = 0, bestWord = 0, bestWindowSize = 0;

		// BoP paper window search range suggestion
		int minWinSize = (int) ((data.numAttributes() - 1) * (15.0 / 100.0));
		int maxWinSize = (int) ((data.numAttributes() - 1) * (36.0 / 100.0));

		// int winInc = 1; //check every size in range
		int winInc = (int) ((maxWinSize - minWinSize) / 10.0); // check 10 sizes within that range
		if (winInc < 1)
			winInc = 1;

		for (int alphaSize = 2; alphaSize <= 8; alphaSize += 2) {
			for (int winSize = minWinSize; winSize <= maxWinSize; winSize += winInc) {
				// for (int wordSize = 2; wordSize <= 8 && wordSize < winSize; wordSize+=1) {
				// //change by suhita
				for (int wordSize = 5; wordSize <= 8 && wordSize < winSize; wordSize += 1) {
					SAXVSM vsm = new SAXVSM(wordSize, alphaSize, winSize);

					double acc = vsm.crossValidate(data);
					System.out.println(
							"wordsize-" + wordSize + " alphasize-" + alphaSize + "win-" + winSize + "----" + acc);
					if (acc > bestAcc) {
						bestAcc = acc;
						bestAlpha = alphaSize;
						bestWord = wordSize;
						bestWindowSize = winSize;
					}
				}
			}
		}

		return new int[] { bestWord, bestAlpha, bestWindowSize };
	}

	/**
	 * Leave-one-out CV without re-doing bop transformation every fold (still
	 * re-applying tfxidf)
	 * 
	 * @return cv accuracy
	 */
	private double crossValidate(Instances data) throws Exception {
		transformedData = bop.process(data);

		double correct = 0;
		for (int i = 0; i < data.numInstances(); ++i) {
			corpus = tfxidf(transformedData, i); // apply tfxidf while ignoring BOP bag i

			if (classifyInstance(data.get(i)) == data.get(i).classValue())
				++correct;
		}

		return correct / data.numInstances();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		trainResults.buildTime = System.currentTimeMillis();

		if (data.classIndex() != data.numAttributes() - 1)
			throw new Exception("SAXVSM_BuildClassifier: Class attribute not set as last attribute in dataset");

		if (useParamSearch) {
			int[] params = parameterSearch(data);

			this.PAA_intervalsPerWindow = params[0];
			this.SAX_alphabetSize = params[1];
			this.windowSize = params[2];

			bop = new BagOfPatternsFilter(PAA_intervalsPerWindow, SAX_alphabetSize, windowSize);
		}

		if (PAA_intervalsPerWindow < 1)
			throw new Exception("SAXVSM_BuildClassifier: Invalid PAA word size: " + PAA_intervalsPerWindow);
		if (PAA_intervalsPerWindow > windowSize)
			throw new Exception("SAXVSM_BuildClassifier: Invalid PAA word size, bigger than sliding window size: "
					+ PAA_intervalsPerWindow + "," + windowSize);
		if (SAX_alphabetSize < 2 || SAX_alphabetSize > 10)
			throw new Exception("SAXVSM_BuildClassifier: Invalid SAX alphabet size (valid=2-10): " + SAX_alphabetSize);
		if (windowSize < 1 || windowSize > data.numAttributes() - 1)
			throw new Exception("SAXVSM_BuildClassifier: Invalid sliding window size: " + windowSize
					+ " (series length " + (data.numAttributes() - 1) + ")");

		transformedData = bop.process(data);

		corpus = tfxidf(transformedData);
		trainResults.buildTime = System.currentTimeMillis() - trainResults.buildTime;
	}

	/**
	 * Given a set of *individual* series transformed into bop form, will return a
	 * corpus containing *class* bags made from that data with tfxidf weighting
	 * applied
	 */
	public Instances tfxidf(Instances bopData) {
		return tfxidf(bopData, -1); // include all instances into corpus
	}

	/**
	 * If skip = one of <0 ... numInstances-1>, will not include instance at that
	 * index into the corpus Part of leave one out cv, while avoiding unnecessary
	 * repeats of the BoP transformation
	 */
	private Instances tfxidf(Instances bopData, int skip) {
		int numClasses = bopData.numClasses();
		// int numInstances = bopData.numInstances();
		int numTerms = bopData.numAttributes() - 1; // minus class attribute

		// initialise class weights
		double[][] classWeights = new double[numClasses][numTerms];

		// build class bags
		int inst = 0;
		for (Instance in : bopData) {
			if (inst++ == skip) // skip 'this' one, for leave-one-out cv
				continue;

			int classVal = (int) in.classValue();
			for (int j = 0; j < numTerms; ++j) {
				classWeights[classVal][j] += in.value(j);
			}
		}

		// apply tf x idf
		for (int i = 0; i < numTerms; ++i) { // for each term
			double df = 0; // document frequency
			for (int j = 0; j < numClasses; ++j) // find how many classes (documents) this term appears in
				if (classWeights[j][i] != 0)
					++df;

			if (df != 0) { // if it appears
				if (df != numClasses) { // but not in all, apply weighting
					for (int j = 0; j < numClasses; ++j)
						if (classWeights[j][i] != 0)
							classWeights[j][i] = Math.log(1 + classWeights[j][i]) * Math.log(numClasses / df);
				} else { // appears in all
							// avoid log calculations
							// if df == num classes -> idf = log(N/df) = log(1) = 0
					for (int j = 0; j < numClasses; ++j)
						classWeights[j][i] = 0;
				}
			}
		}

		Instances tfxidfCorpus = new Instances(bopData, numClasses);
		for (int i = 0; i < numClasses; ++i)
			tfxidfCorpus.add(new SparseInstance(1.0, classWeights[i]));

		return tfxidfCorpus;
	}

	/**
	 * Takes two vectors of equal length, and computes the cosine similarity between
	 * them.
	 * 
	 * @return a.b / ( |a|*|b| )
	 * @throws java.lang.Exception
	 *             if a.length != b.length
	 */
	public double cosineSimilarity(double[] a, double[] b) throws Exception {
		if (a.length != b.length)
			throw new Exception("Cannot calculate cosine similarity between vectors of different lengths " + "("
					+ a.length + ", " + b.length + ")");

		return cosineSimilarity(a, b, a.length);

	}

	/**
	 * Takes two vectors, and computes the cosine similarity between them using the
	 * first n values in each vector.
	 * 
	 * To be used when e.g one or both vectors have class values as the last
	 * element, only compute similarity up to n-1
	 * 
	 * @param n
	 *            Elements 0 to n-1 will be computed for similarity, elements n to
	 *            size-1 ignored
	 * @return a.b / ( |a|*|b| )
	 * @throws java.lang.Exception
	 *             if n > (a.length or b.length)
	 */
	public double cosineSimilarity(double[] a, double[] b, int n) throws Exception {
		if (n > a.length || n > b.length)
			throw new IllegalArgumentException("SAXVSM_CosineSimilarity n greater than vector lengths " + "(a:"
					+ a.length + ", b:" + b.length + " n:" + n + ")");

		double dotProd = 0.0, aMag = 0.0, bMag = 0.0;

		for (int i = 0; i < n; ++i) {
			dotProd += a[i] * b[i];
			aMag += a[i] * a[i];
			bMag += b[i] * b[i];
		}

		if (aMag == 0 || bMag == 0 || dotProd == 0)
			return 0;
		if (aMag == bMag) // root(n) * root(n) just = n^(1/2)^2 = n, save the root operation
			return dotProd / aMag;

		return dotProd / (Math.sqrt(aMag) * Math.sqrt(bMag));

	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {
		int numClasses = corpus.numInstances();

		double[] distribution = distributionForInstance(instance);

		// find max probability
		double maxIndex = 0, max = distribution[0];
		for (int i = 1; i < numClasses; ++i)
			if (distribution[i] > max) {
				max = distribution[i];
				maxIndex = i;
			}
		return maxIndex;
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		int numClasses = corpus.numInstances();

		double[] termFreqs = bop.bagToArray(bop.buildBag(instance));

		// find similarity to each class
		double[] similarities = new double[numClasses];
		double sum = 0.0;
		for (int i = 0; i < numClasses; ++i) {
			similarities[i] = cosineSimilarity(corpus.get(i).toDoubleArray(), termFreqs, termFreqs.length);
			sum += similarities[i];
		}

		// return as a set of probabilities
		if (sum != 0)
			for (int i = 0; i < numClasses; ++i)
				similarities[i] /= sum;

		return similarities;
	}

	@Override
	public Capabilities getCapabilities() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public static void main(String[] args) {
		System.out.println("SAXVSMBasicTest\n");
		try {

			Instances train = ClassifierTools.loadData("E:\\user-study\\arff\\test3.arff");
			Instances test = ClassifierTools.loadData("E:\\user-study\\arff\\test3.arff");
			String resultsPath = "E:\\user-study\\output";
			SAXVSM vsm = new SAXVSM();

			vsm.buildClassifierAndSave(train, test, "E:\\user-study\\arff\\", "sax_train2_test3", 10, resultsPath);

			String classifier = "E:\\user-study\\arff\\sax_train2_test3.model";
			vsm.applyClassifier(test, classifier, true);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public String buildClassifierAndSave(Instances train, Instances test, String classifierSaveLoc,
			String classifierName, int folds, String resultsPath) throws IOException {

		StringBuffer msg = new StringBuffer("Start Building classifier...\n");

		// if train and test have different number of attributes then need to recreate
		// arffs
		if (train.classIndex() > test.classIndex()) {
			test = ClassifierTools.recreateArff(test, train.classIndex());
		} else if (train.classIndex() < test.classIndex()) {
			train = ClassifierTools.recreateArff(train, test.classIndex());
		}
		if (!(classifierSaveLoc.endsWith("/") || classifierSaveLoc.endsWith("\\")))
			classifierSaveLoc = classifierSaveLoc + "/";

		logger.info("Training starting...");
		long start = System.nanoTime();
		try {
			SAXVSM vsm = new SAXVSM();
			double accuracy = singleClassifierAndFold(train, test, vsm, folds, resultsPath);

			Evaluation eval = new Evaluation(test);
			PlainText forPredictionsPrinting = new PlainText();
			forPredictionsPrinting.setBuffer(new StringBuffer());

			CSV output = new CSV();
			output.setHeader(new Instances(test, 0));
			output.setBuffer(new StringBuffer());

			eval.evaluateModel(vsm, test, output);

			String classDetailsString = eval.toClassDetailsString();

			weka.core.SerializationHelper.write(classifierSaveLoc + classifierName + ".model", vsm);
			double trainTime = (System.nanoTime() - start) / 1000000000.0; // seconds

			logger.info("Training done (" + trainTime + "s)");
			msg.append("End Building classifier...\\n");
			logger.info("Accuracy with " + folds + " :" + accuracy);
			msg.append("Accuracy with " + folds + " :" + accuracy + "\n");
			msg.append(classDetailsString + "\n");
			logger.info("Best Params for classifier: " + classifierName);
			for (int p : ((SAXVSM) vsm).getParametersArray())
				logger.info(p + " ");
		} catch (Exception e) {
			logger.severe("Classifier could not be built!!!" + e.getMessage());
			msg.append("ERROR: Classifier could not be built!!!" + e.getMessage());
			return msg.toString();
		}
		return msg.toString();
	}

	public Phase2Results applyClassifier(Instances test, String classifierModel, boolean groundTruthAvailable)
			throws Exception, FileNotFoundException {

		Phase2Results results = new Phase2Results();
		
		logger.info("Testing starting...");
		StringBuffer msg = new StringBuffer("Applying ").append(this.toString()).append(" model <")
				.append(classifierModel).append("> on dataset <").append(test.relationName() + ">\n");

		SAXVSM vsm = (SAXVSM) SerializationHelper.read(new FileInputStream(classifierModel));
		long start = System.nanoTime();
		ClassifierStatsMessage clmsg = ClassifierTools.getClassifierPrediction(test, vsm, groundTruthAvailable);
		double testTime = (System.nanoTime() - start) / 1000000000.0; // sec
		logger.info("Testing done (" + testTime + "s)");
		//msg.append("Testing done (" + testTime + "s)\n");
		results.setTimeTaken(testTime);

		PlainText forPredictionsPrinting = new PlainText();
		forPredictionsPrinting.setBuffer(new StringBuffer());

		CSV output = new CSV();
		output.setHeader(new Instances(test,0));
		output.setBuffer(new StringBuffer());
		msg.append(clmsg.getMessage());
		if (groundTruthAvailable) {
			//logger.info("Accuracy : " + clmsg.getAccuracy());
			logger.info(clmsg.getMessage());
			//msg.append("Accuracy : " + clmsg.getAccuracy() + "\n");
			Evaluation eval = new Evaluation(test);
			//logger.info(""+eval.kappa());
			eval.evaluateModel(vsm, test, output);
			String classDetailsString = eval.toClassDetailsString();
			logger.info(classDetailsString);
			msg.append("\n"+classDetailsString + "\n");
		}
		results.setPredictionList(clmsg.getPredictionList());
		results.setMessage(msg.toString());
		return results;
	}

	@Override
	public String toString() {
		return "SAXVSM";
	}

}
