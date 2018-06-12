/**
 * 
 */
package org.ovgu.de.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

	public static void main(String[] args) throws Exception {
		String inputFileName = "C:/Users/com/Dropbox/Content/Tune2/ms1/susu.csv";
		String outputFileName = "C:/Users/com/Dropbox/Content/Tune2/ms1/susu.arff";
		generateDataset(inputFileName, outputFileName,true);
	}

	public static void generateDataset(String inputFileName, String outputFileName, boolean classNameFirst)
			throws Exception {
		CSVLoader loader = new CSVLoader();
		Instances dataset = null;

		System.out.println("Start generating Arff from :" + inputFileName);
		File trainFile = new File(inputFileName);
		loader.setFile(trainFile);
		dataset = loader.getDataSet();
		dataset.setClassIndex(classNameFirst ? 0 : dataset.numAttributes() - 1);

		NumericToNominal convert = new NumericToNominal();
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "last-last";
		convert.setOptions(options);
		convert.setInputFormat(dataset);
		Instances newData = Filter.useFilter(dataset, convert);

		ArffSaver saver = new ArffSaver();
		saver.setInstances(newData);
		saver.setFile(new File(outputFileName));
		saver.writeBatch();
		System.out.println("End generating Arff :" + outputFileName);

	}

}
