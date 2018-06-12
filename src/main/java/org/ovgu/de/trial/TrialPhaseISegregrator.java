package org.ovgu.de.trial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

	public static void main(String[] args) throws IOException {

		String INPUT_UNISENS_PATH = "E:\\user-study\\2018-05-31 15.31.02";
		String LOG_PATH = "E:\\user-study\\log\\LOG_1527775609337_2";
		String OUTPUT = "E:\\user-study\\HE_2.csv";
		boolean startedAfter1min = true;
		List<String> segments = generateSegments(INPUT_UNISENS_PATH, LOG_PATH, OUTPUT, startedAfter1min);

	}

	public static List<String> generateSegments(String inputUnisensLoc, String logLoc, String outputLoc,
			boolean startedAfter1min) throws IOException {

		List<LogEntryDAO> logEntries = getLogEntries(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<String> segments = new ArrayList<>();
		System.out.println("Data present after truncation 1 min of initial relaxation phase:" + uniData.size());
		int counter = startedAfter1min ? 1920 : 0; // start after 1min- (32*60)

		int max = 0;
		try {
			for (LogEntryDAO entry : logEntries) {

				int samplesToAdd = (SAMPLING_RATE * entry.getDuration()) / 1000;
				max = samplesToAdd > max ? samplesToAdd : max;
				StringBuffer sbf = new StringBuffer();
				sbf.append(entry.getEasyHardRelaxFlag().equals("h") ? "1" : "0").append(",");

				if (entry.getEasyHardRelaxFlag().equals("h") || entry.getEasyHardRelaxFlag().equals("e")) {
					for (int i = counter; i <= (counter + samplesToAdd); i++) {
						sbf.append(uniData.get(i)).append(",");
					}
					sbf.deleteCharAt(sbf.length() - 1); // remove extra comma

					System.out.println(entry.getEasyHardRelaxFlag().toUpperCase() + " Segment, has length "
							+ samplesToAdd + " range :" + counter + "-" + (counter + samplesToAdd));
					segments.add(sbf.toString());
					// writer.write(sbf.toString());
					// writer.append("\n");
				}
				counter = counter + samplesToAdd;

			}
			System.out.println(segments.size());
		} catch (Exception e) {
			System.err.println("all data could not be segmented");
		}
		generateCSV(segments, max + 1, outputLoc);
		return segments;

	}

	/**
	 * @param segments
	 * @param max
	 * @throws IOException
	 */
	private static void generateCSV(List<String> segments, int max, String outputLoc) throws IOException {

		List<String> paddedSegments = fillWithMissingNotation(segments, max);
		// generate and add header to csv
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i <= max; i++) {
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

	private static List<String> fillWithMissingNotation(List<String> segments, int max) {
		StringBuffer sbf = new StringBuffer();
		List<String> modifiedS = new ArrayList<>();
		for (String sgmnt : segments) {
			int length = sgmnt.split(",").length;
			System.out.println(length);
			if (length < max) {
				sbf = new StringBuffer(sgmnt);
				for (int i = 0; i <= (max-length); i++) {
					sbf.append(","); //jus append comma for missing values
				}
				modifiedS.add(sbf.toString());
			} else {
				modifiedS.add(sgmnt);
			}

		}
		return modifiedS;
	}

}