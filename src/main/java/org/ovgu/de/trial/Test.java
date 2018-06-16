/**
 * 
 */
package org.ovgu.de.trial;

import java.util.List;
import java.util.Map;

import org.ovgu.de.utils.ArffGenerator;

/**
 * @author Suhita Ghosh
 *
 */
public class Test {
	
	private static final String LOG_FOLDER = "E:\\user-study\\log\\";
	private static final String UNISENS_FOLDER = "E:\\user-study\\unisens\\";
	private static final String OUT_CSV_FOLDER = "E:\\user-study\\output\\";
	private static final String ARFF_FOLDER = "E:\\user-study\\arff\\";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TrialPhaseISegregrator tp = new TrialPhaseISegregrator();
		String INPUT_UNISENS_FILE = UNISENS_FOLDER + "2018-05-31 17.10.00";
		String LOG_FILE = LOG_FOLDER + "LOG_1527782376698_3.csv";
		String OUTPUT_FILE = OUT_CSV_FOLDER + "HE_3.csv";
		boolean startedAfter1min = false;
		//generate for 1 file
		List<SegmentDAO> segments = tp.generateSegments(INPUT_UNISENS_FILE, LOG_FILE, OUTPUT_FILE, startedAfter1min);
		tp.generateCSV(segments, OUTPUT_FILE);
		ArffGenerator.generateDataset( OUT_CSV_FOLDER+"all.csv", ARFF_FOLDER+"all.arff",true);

		//generate for multiple files
		
		//create mapping log and unisens files
		Map<String, Boolean> logUnisensPathMap = tp.generateLogUnisensMap();
		//create segments for all
		List<SegmentDAO> segmentForAll = tp.generateSegmentsFromMultipleFiles(logUnisensPathMap);// will shift to properties file later
		//generate csv
		tp.generateCSV(segmentForAll, OUT_CSV_FOLDER+"all.csv");
		System.out.println(segmentForAll.size());
		//generate arff
		ArffGenerator.generateDataset( OUT_CSV_FOLDER+"all.csv", ARFF_FOLDER+"all.arff",true);

	}

}
