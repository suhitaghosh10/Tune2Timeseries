/**
 * 
 */
package org.ovgu.de.trial;

import org.ovgu.de.utils.ArffGenerator;

/**
 * @author Suhita Ghosh
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String input_unisens_folder = "E:/user-study/2018-05-31 15.31.02";
		String LOG_PATH = "E:\\user-study\\log\\LOG_1527775609337_2";
		String OUTPUT = "E:\\user-study\\HE_3.csv";
		String output_arff = "E:/user-study/HE_3.arff";
		
		TrialPhaseISegregrator.generateSegments(input_unisens_folder,LOG_PATH,OUTPUT,true);
		//p.s - generate arff after padding
		ArffGenerator.generateDataset(OUTPUT, output_arff,true);

	}

}
