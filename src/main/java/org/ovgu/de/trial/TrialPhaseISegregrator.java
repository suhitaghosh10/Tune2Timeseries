package org.ovgu.de.trial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ovgu.de.unisens.UnisensCSVGenerator;

/**
 * 
 * @author Suhita Ghosh
 * 
 *         The class acts as segregator for easy, hard and relaxation segments
 *         of timeseries
 *
 */
public class TrialPhaseISegregrator {

	private static final Integer SAMPLING_RATE = 32;
	private static final String LOG_PATH = "E:\\user-study\\log\\";
	private static final String UNISENS_FILES_PATH = "E:\\user-study\\unisens\\";
	private static final String OUT_PATH = "E:\\user-study\\output\\";

	public static void main(String[] args) throws IOException {

		String INPUT_UNISENS_FILE = UNISENS_FILES_PATH + "2018-05-31 17.10.00";
		String LOG_FILE = LOG_PATH + "LOG_1527782376698_3.csv";
		String OUTPUT_FILE = OUT_PATH + "HE_3.csv";
		boolean startedAfter1min = false;
		TrialPhaseISegregrator tp = new TrialPhaseISegregrator();
		//generate for 1 file
		List<SegmentDAO> segments = tp.generateSegments(INPUT_UNISENS_FILE, LOG_FILE, OUTPUT_FILE, startedAfter1min);
		tp.generateCSV(segments, OUTPUT_FILE);
		Map<String, Boolean> logUnisensPathMap = tp.generateLogUnisensMap();

		//generate for multiple files
		List<SegmentDAO> segmentForAll = tp.generateSegmentsFromMultipleFiles(logUnisensPathMap);// will shift to properties file later
		tp.generateCSV(segmentForAll, OUT_PATH+"all.csv");
		System.out.println(segmentForAll.size());

	}

	/**
	 * @param logUnisensPathMap
	 * @return
	 * @throws IOException
	 */
	public List<SegmentDAO> generateSegmentsFromMultipleFiles(Map<String, Boolean> logUniEntries)
			throws IOException {
		List<SegmentDAO> segments = new ArrayList<>();
		List<SegmentDAO> segmentsForOneFile = null;
		for (Entry<String, Boolean> entry : logUniEntries.entrySet()) {
			System.out.println("generating ts for" + entry.getKey());
			String[] arr = entry.getKey().split("#");
			segmentsForOneFile = generateSegments(UNISENS_FILES_PATH + arr[0], LOG_PATH + arr[1],
					OUT_PATH + arr[0] + ".csv", entry.getValue());
			segments.addAll(segmentsForOneFile);
			System.out.println("End generating ts for" + entry.getKey());
		}
		return segments;

	}

	/**
	 * @return generates the log unisens files mapping
	 */
	public Map<String, Boolean> generateLogUnisensMap() {
		Map<String, Boolean> logUnisensPathMap = new HashMap<>();
		//unisensFile#logFile, startsAfter1min-boolean
		logUnisensPathMap.put("2018-05-31 14.46.01#LOG_1527772114264_1.csv", false);
		logUnisensPathMap.put("2018-05-31 15.31.02#LOG_1527775609337_2", true);
		logUnisensPathMap.put("2018-05-31 17.10.00#LOG_1527782376698_3.csv", false);
		logUnisensPathMap.put("2018-06-01 17.36.01#LOG_1527869558388_1", false);
		logUnisensPathMap.put("2018-06-01 18.27.01#LOG_1527872343275_2", false);
		return logUnisensPathMap;
	}

	/**
	 * @param inputUnisensLoc
	 * @param logLoc
	 * @param outputLoc
	 * @param startedAfter1min
	 * @return
	 * @throws IOException
	 * 
	 *             The method generates segment object. each segment dao represents
	 *             either hard or easy ts
	 */
	public List<SegmentDAO> generateSegments(String inputUnisensLoc, String logLoc, String outputLoc,
			boolean startedAfter1min) throws IOException {

		List<LogEntryDAO> logEntries = getLogEntries(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<SegmentDAO> segments = new ArrayList<>();
		// System.out.println("Data present after truncation 1 min of initial relaxation
		// phase:" + uniData.size());
		int counter = startedAfter1min ? 1920 : 0; // start after 1min- (32*60)

		// int max = 0;
		try {
			for (int index = 0; index < logEntries.size(); index++) {

				int samplesToAdd = (SAMPLING_RATE * logEntries.get(index).getDuration()) / 1000;
				// max = samplesToAdd > max ? samplesToAdd : max;
				StringBuffer sbf = new StringBuffer();
				sbf.append(logEntries.get(index).getEasyHardRelaxFlag().equals("h") ? "1" : "0").append(",");

				if (logEntries.get(index).getEasyHardRelaxFlag().equals("h")
						|| logEntries.get(index).getEasyHardRelaxFlag().equals("e")) {
					if ((!startedAfter1min && index > 0) || startedAfter1min) {
						for (int i = counter; i <= (counter + samplesToAdd); i++) {
							sbf.append(uniData.get(i)).append(",");
						}
						sbf.deleteCharAt(sbf.length() - 1); // remove extra comma
						segments.add(new SegmentDAO(sbf.toString(), samplesToAdd));
					}
					System.out.println(
							logEntries.get(index).getEasyHardRelaxFlag().toUpperCase() + " Segment, has length "
									+ samplesToAdd + " range :" + counter + "-" + (counter + samplesToAdd));
				} else {
					System.out.println("R" + " Segment, has length " + samplesToAdd + " range :" + counter + "-"
							+ (counter + samplesToAdd));
				}
				counter = counter + samplesToAdd;

			}
		} catch (Exception e) {
			System.err.println("all data could not be segmented");
		} finally {
			System.out.println(inputUnisensLoc + " length-" + segments.size());
		}
		// generateCSV(segments, max + 1, outputLoc);
		return segments;

	}

	/**
	 * @param segments
	 * @param max
	 * @throws IOException
	 */
	public void generateCSV(List<SegmentDAO> segments, String outputLoc) throws IOException {

		int maxLngth = getMaxLength(segments)+1;
		List<String> paddedSegments = fillWithMissingNotation(segments, maxLngth);
		// generate and add header to csv
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i <= maxLngth; i++) {
			sbf.append(i == 0 ? "class," : "a" + i + ",");
		}
		sbf.deleteCharAt(sbf.length() - 1);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLoc, true));) {
			writer.write(sbf.toString());
			writer.append("\n");
			// add actual data to csv
			for (String sgmnt : paddedSegments) {
				writer.write(sgmnt);
				writer.append("\n");
			}
		}
	}

	/**
	 * @param segments
	 * @return
	 */
	private static int getMaxLength(List<SegmentDAO> segments) {
		int max = 0;
		for (SegmentDAO dao : segments) {
			max = max < dao.getLength() ? dao.getLength() : max;
		}
		return max;
	}

	/**
	 * @param logLoc
	 * @return
	 * @throws IOException
	 */
	private static List<LogEntryDAO> getLogEntries(String logLoc) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(logLoc));
		List<LogEntryDAO> elist = new ArrayList<>();
		for (String line : lines) {
			String[] arr = line.split(",");
			LogEntryDAO dao = new LogEntryDAO(arr[0], Integer.parseInt(arr[4]));
			elist.add(dao);
		}
		return elist;

	}

	private static List<String> fillWithMissingNotation(List<SegmentDAO> segments, int max) {
		StringBuffer sbf = new StringBuffer();
		List<String> modifiedS = new ArrayList<>();
		for (SegmentDAO sgmnt : segments) {
			int length = sgmnt.getTimeseries().split(",").length;
			System.out.println(length);
			if (length < max) {
				sbf = new StringBuffer(sgmnt.getTimeseries());
				for (int i = 0; i <= (max - length); i++) {
					sbf.append(","); // jus append comma for missing values
				}
				modifiedS.add(sbf.toString());
			} else {
				modifiedS.add(sgmnt.getTimeseries());
			}

		}
		return modifiedS;
	}

}