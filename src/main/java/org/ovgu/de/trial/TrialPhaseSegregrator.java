package org.ovgu.de.trial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.ovgu.de.unisens.UnisensCSVGenerator;
import org.ovgu.de.utils.ArffGenerator;
import org.ovgu.de.utils.PropertiesHandler;

/**
 * 
 * @author Suhita Ghosh
 * 
 *         The class acts as segregator for easy, hard and relaxation segments
 *         of timeseries
 *
 */
public class TrialPhaseSegregrator {

	java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("TrialPhaseISegregrator");
	private static final Integer SAMPLING_RATE = 32;

	/**
	 * @param inputUnisensFolderLoc
	 * @param LogFile
	 * @param ArffFolderLoc
	 * @param startedAfter1min
	 * @param classFirstInArff
	 * @return
	 * 
	 * 		preprocess and generates arff file. The method returns messages to be
	 *         returned in the UI
	 * @throws IOException 
	 */
	public String preprocessAndGenerateArff(String inputUnisensFolderLoc, String LogFile, String ArffFile,
			boolean startedAfter1min) throws IOException {

		StringBuffer msg = new StringBuffer();
		String temp_path = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");
		if (!(temp_path.endsWith("/") | temp_path.endsWith("\\")))
			temp_path = temp_path + "/";
		
		msg.append("Log file provided :" + LogFile + "\n");
		LOGGER.info("Log file provided :" + LogFile);
		msg.append("Unisens folder provided :" + inputUnisensFolderLoc + "\n");
		LOGGER.info("Unisens folder provided :" + inputUnisensFolderLoc);
		msg.append("Arff to be generated :" + ArffFile + "\n");
		LOGGER.info("Arff will be generated :" + ArffFile);
		String csvFile = temp_path + "temp.csv";

		try {
			// generate segments
			SegmentMessageDAO dao = generateSegmentsForPhase1(inputUnisensFolderLoc, LogFile, startedAfter1min);
			msg.append(dao.getMessage());
			// generate csv
			generateCSV(dao.getSgmntList(), csvFile);
		} catch (IOException e) {
			msg.append("File not found " + e.getMessage());
			LOGGER.severe("File not found " + e.getMessage());
			return msg.toString();
		}

		// generate arff
		try {
			ArffGenerator.generateDataset(csvFile, ArffFile, false);
		} catch (Exception e) {
			msg.append("Arff file could not be generated " + e.getMessage() + "\n");
			LOGGER.severe("Arff file could not be generated " + e.getMessage());
			return msg.toString();
		}
		return msg.toString();

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
	public SegmentMessageDAO generateSegmentsForPhase1(String inputUnisensLoc, String logLoc, boolean startedAfter1min)
			throws IOException {

		SegmentMessageDAO dao = new SegmentMessageDAO();
		StringBuffer msg = new StringBuffer();
		msg.append("Generating Segments from the Log and Unisens File ...\n");
		LOGGER.info("Generating Segments from the Log and Unisens File ...");

		List<LogEntryDAO> logEntries = getLogEntries(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<SegmentDAO> segments = new ArrayList<>();
		int counter = startedAfter1min ? 1920 : 0; // start after 1min- (32*60)

		try {
			for (int index = 0; index < logEntries.size(); index++) {

				int samplesToAdd = (SAMPLING_RATE * logEntries.get(index).getDuration()) / 1000;
				StringBuffer sbf = new StringBuffer();

				if (logEntries.get(index).getEasyHardRelaxFlag().equals("h")
						|| logEntries.get(index).getEasyHardRelaxFlag().equals("e")) {
					if ((!startedAfter1min && index > 0) || startedAfter1min) {
						for (int i = counter; i <= (counter + samplesToAdd); i++) {
							sbf.append(uniData.get(i)).append(",");
						}
						sbf.deleteCharAt(sbf.length() - 1);
						String cls = logEntries.get(index).getEasyHardRelaxFlag().equals("h") ? "1" : "0";
						segments.add(new SegmentDAO(sbf.toString(), cls, samplesToAdd));
					}
					// LOGGER.info(logEntries.get(index).getEasyHardRelaxFlag().toUpperCase() + "
					// Segment, has length "
					// + samplesToAdd + " range :" + counter + "-" + (counter + samplesToAdd));
				} else {
					// LOGGER.info("R" + " Segment, has length " + samplesToAdd + " range :" +
					// counter + "-"
					// + (counter + samplesToAdd));
				}
				counter = counter + samplesToAdd;

			}
		} catch (Exception e) {
			LOGGER.info("all data could not be segmented");
			msg.append("all data could not be segmented\n");
		} finally {
			LOGGER.info(segments.size() + " segments have been generated...");
			msg.append(segments.size() + " segments have been generated...\n");
		}
		dao.setSgmntList(segments);
		dao.setMessage(msg.toString());
		return dao;

	}
	
	/**
	 * @param inputUnisensLoc
	 * @param logLoc
	 * @param outputLoc
	 * @param startedAfter1min
	 * @return
	 * @throws IOException
	 * 
	 *             The method generates segment object for phase2. each segment dao represents
	 *             either hard or easy ts
	 */
	public SegmentMessageDAO generateSegmentsForPhase2(String inputUnisensLoc, String logLoc, boolean startedAfter1min)
			throws IOException {

		SegmentMessageDAO dao = new SegmentMessageDAO();
		StringBuffer msg = new StringBuffer();
		msg.append("Generating Segments from the Log and Unisens File ...\n");
		LOGGER.info("Generating Segments from the Log and Unisens File ...");

		List<LogEntryDAO> logEntries = getLogEntries(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<SegmentDAO> segments = new ArrayList<>();
		int counter = startedAfter1min ? 1920 : 0; // start after 1min- (32*60)

		try {
			for (int index = 0; index < logEntries.size(); index++) {

				int samplesToAdd = (SAMPLING_RATE * logEntries.get(index).getDuration()) / 1000;
				StringBuffer sbf = new StringBuffer();

				if (logEntries.get(index).getEasyHardRelaxFlag().equals("h")
						|| logEntries.get(index).getEasyHardRelaxFlag().equals("e")) {
					if ((!startedAfter1min && index > 0) || startedAfter1min) {
						for (int i = counter; i <= (counter + samplesToAdd); i++) {
							sbf.append(uniData.get(i)).append(",");
						}
						sbf.deleteCharAt(sbf.length() - 1);
						String cls = logEntries.get(index).getEasyHardRelaxFlag().equals("h") ? "1" : "0";
						segments.add(new SegmentDAO(sbf.toString(), cls, samplesToAdd));
					}
					// LOGGER.info(logEntries.get(index).getEasyHardRelaxFlag().toUpperCase() + "
					// Segment, has length "
					// + samplesToAdd + " range :" + counter + "-" + (counter + samplesToAdd));
				} else {
					// LOGGER.info("R" + " Segment, has length " + samplesToAdd + " range :" +
					// counter + "-"
					// + (counter + samplesToAdd));
				}
				counter = counter + samplesToAdd;

			}
		} catch (Exception e) {
			LOGGER.info("all data could not be segmented");
			msg.append("all data could not be segmented\n");
		} finally {
			LOGGER.info(segments.size() + " segments have been generated...");
			msg.append(segments.size() + " segments have been generated...\n");
		}
		dao.setSgmntList(segments);
		dao.setMessage(msg.toString());
		return dao;

	}

	/**
	 * @param segments
	 * @param outputLoc
	 * @throws IOException
	 * 
	 *             The method generates csv from segments of timeseries. each
	 *             segment is either hard or easy type
	 */
	public void generateCSV(List<SegmentDAO> segments, String outputLoc) throws IOException {

		File f = new File(outputLoc);
		if (f.exists()) {
			f.delete();
		}
		int maxLngth = getMaxLength(segments) + 1;
		List<SegmentDAO> paddedSegments = fillWithMissingNotation(segments, maxLngth);
		// generate and add header to csv
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i <= maxLngth; i++) {
			sbf.append(i == maxLngth ? "class" : "a" + i + ",");
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLoc, true));) {
			writer.write(sbf.toString());
			writer.append("\n");
			// add actual data to csv
			for (SegmentDAO sgmnt : paddedSegments) {
				writer.write(sgmnt.getTimeseries() + "," + sgmnt.getClassName());
				writer.append("\n");
			}
		}
	}

	/**
	 * @param segments
	 * @return
	 * 
	 * 		The method returns longest segment's length
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
	 * 
	 *             The method reads a log file and creates log dao
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

	/**
	 * @param segments
	 * @param max
	 * @return
	 * 
	 * 		The method fills the shorter segments with '?' notation, which is
	 *         needed to create arff file in weka
	 */
	private static List<SegmentDAO> fillWithMissingNotation(List<SegmentDAO> segments, int max) {
		StringBuffer sbf = new StringBuffer();
		List<SegmentDAO> modifiedS = new ArrayList<>();
		for (SegmentDAO sgmnt : segments) {
			int length = sgmnt.getTimeseries().split(",").length;
			// System.out.println(length);
			if (length < max) {
				sbf = new StringBuffer(sgmnt.getTimeseries());
				for (int i = 0; i < (max - length); i++) {
					sbf.append(",?"); // jus append comma for missing values
				}
				modifiedS.add(new SegmentDAO(sbf.toString(), sgmnt.getClassName(), max));
			} else {
				modifiedS.add(sgmnt);
			}

		}
		return modifiedS;
	}

	/**
	 * @param logUnisensPathMap
	 * @return
	 * @throws IOException
	 */
	public SegmentMessageDAO generateSegmentsFromMultipleFiles(List<PersonDAO> logUnisensPathMap) throws IOException {

		StringBuffer msg = new StringBuffer();
		SegmentMessageDAO dao = new SegmentMessageDAO();

		LOGGER.info("Generating segements for multiple files...");
		List<SegmentDAO> segments = new ArrayList<>();
		List<SegmentDAO> segmentsForOneFile = null;

		for (PersonDAO entry : logUnisensPathMap) {
			LOGGER.info("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName());
			msg.append("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName()
					+ "\n");
			segmentsForOneFile = generateSegmentsForPhase1(entry.getUnisensFolderName(), entry.getLogFileName(),
					entry.isStartedAfterOneMinute()).getSgmntList();
			segments.addAll(segmentsForOneFile);
			LOGGER.info("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName());
			msg.append("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName() + "\n");
		}
		dao.setSgmntList(segments);
		dao.setMessage(msg.toString());
		return dao;

	}

	/**
	 * @param unisensFolder
	 * @param LogFolder
	 * @param arffFolder
	 * @param logUnisensPathMap
	 * @return
	 * @throws IOException
	 * 
	 *             processes multiple files and generates ones arff file. returns
	 *             name of the arff file
	 */
	public String preprocessAndGenerateArffForMultiple(String targetArffFileName, List<PersonDAO> logUnisensPathMap)
			throws IOException {

		String tempPath = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");
		if (!(tempPath.endsWith("/") | tempPath.endsWith("\\")))
			tempPath = tempPath + "/";

		TrialPhaseSegregrator tp = new TrialPhaseSegregrator();
		StringBuffer sbf = new StringBuffer("Start Generating segments for multiple person\n");

		// create segments for all
		SegmentMessageDAO segmentForAll = tp.generateSegmentsFromMultipleFiles(logUnisensPathMap);
		sbf.append(segmentForAll.getMessage());
		// generate csv
		long curTime = System.currentTimeMillis();
		String csvPath = tempPath + "temp.csv";
		tp.generateCSV(segmentForAll.getSgmntList(), csvPath);
		sbf.append("Total number of Segments generated : " + segmentForAll.getSgmntList().size() + "\n");

		String msg = ArffGenerator.generateDataset(csvPath, targetArffFileName, false);
		sbf.append(msg + "\n");
		sbf.append("End Generating segments for multiple person\n");
		return sbf.toString();
	}
}