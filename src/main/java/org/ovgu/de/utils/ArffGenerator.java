/**
 * 
 */
package org.ovgu.de.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * @author Suhita Ghosh
 *
 */
public class ArffGenerator {
	/**
	 * 
	 */

	private static Logger logger = Logger.getLogger("ArffGenerator");

	public static void main(String[] args) throws Exception {
		String inputFileName = "C:/Users/com/Dropbox/Content/Tune2/ms1/susu.csv";
		String outputFileName = "C:/Users/com/Dropbox/Content/Tune2/ms1/susu.arff";
		generateDataset(inputFileName, outputFileName, true);
	}

	public static String generateDataset(String inputFileName, String targetArffFileName, boolean classNameFirst) {

		StringBuffer msg = new StringBuffer();
		// generate arff
		if (targetArffFileName.endsWith("/") || targetArffFileName.endsWith("\\"))
			targetArffFileName = targetArffFileName + Constants.DEFAULT_ARFF_FILE_NAME;
		if (!targetArffFileName.contains(Constants.ARFF))
			targetArffFileName = targetArffFileName + Constants.ARFF;

		msg.append("Start Generating Arff file...\n");
		logger.info("Start Generating Arff file...\n");
		CSVLoader loader = new CSVLoader();
		Instances dataset = null;

		File trainFile = new File(inputFileName);
		Writer fileWriter = null;
		try {
			loader.setFile(trainFile);
			dataset = loader.getDataSet();
			dataset.setClassIndex(classNameFirst ? 0 : dataset.numAttributes() - 1);

			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = classNameFirst ? "first" : "last";
			convert.setOptions(options);
			convert.setInputFormat(dataset);
			Instances newData = Filter.useFilter(dataset, convert);

			ArffSaver saver = new ArffSaver();
			saver.setInstances(newData);
			saver.setFile(new File(targetArffFileName));
			saver.writeBatch();

			String arfcontent = new String(Files.readAllBytes(Paths.get(targetArffFileName)));
			String STRING = "string";
			if (arfcontent.contains(STRING)) {
				arfcontent = arfcontent.replaceAll(STRING, "numeric");
				fileWriter = new FileWriter(targetArffFileName, false);
				fileWriter.write(arfcontent);
			}
			logger.info("End Generating Arff :" + targetArffFileName);
			msg.append("End Generating Arff :" + targetArffFileName + "\n");

		} catch (FileNotFoundException fe) {
			logger.severe("File not found " + fe.getLocalizedMessage());
			msg.append("File not found " + fe.getLocalizedMessage());
		} catch (IOException e) {
			logger.severe("arff file not created "+e.getMessage());
			msg.append("File not found " + e.getLocalizedMessage());
		} catch (Exception e) {
			logger.severe("arff file not created " + e.getMessage());
			msg.append("File not found " + e.getLocalizedMessage());
		} finally {
			if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {
					logger.severe("File not found " + e.getLocalizedMessage());
					msg.append("File not found " + e.getLocalizedMessage());
				}
		}
		return msg.toString();

	}

}
