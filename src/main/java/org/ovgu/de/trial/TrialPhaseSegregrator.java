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

	private static final String TWEET_LIST = "tweetId.txt";
	private static final String SENTI_CSV = "senti.csv";
	private static final String FACT_CSV = "fact.csv";
	private static final String REL_CSV = "rel.csv";
	private static final String TOTAL_CSV = "total.csv";
	private static final String SENTI_ARFF = "senti.arff";
	private static final String FACT_ARFF = "fact.arff";
	private static final String REL_ARFF = "rel.arff";
	private static final String TOTAL_ARFF = "total.arff";
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("TrialPhaseISegregrator");
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
	public String preprocessAndGenerateArffForPhase1(String inputUnisensFolderLoc, String LogFile, String ArffFile,
			boolean startedAfter1min) throws IOException {

		StringBuffer msg = new StringBuffer();
		String temp_path = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");

		File file = new File(temp_path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}
		msg.append("Log file provided :" + LogFile + "\n");
		LOGGER.info("Log file provided :" + LogFile);
		msg.append("Unisens folder provided :" + inputUnisensFolderLoc + "\n");
		LOGGER.info("Unisens folder provided :" + inputUnisensFolderLoc);
		msg.append("Arff to be generated :" + ArffFile + "\n");
		LOGGER.info("Arff will be generated :" + ArffFile);
		long time = System.currentTimeMillis();
		String csvFile = temp_path + time + "temp.csv";

		try {
			// generate segments
			SegmentMessageDAO dao = generateSegmentsForPhase1(inputUnisensFolderLoc, LogFile, startedAfter1min);
			msg.append(dao.getMessage());
			// generate csv
			generateCSVForPhase1(dao.getSgmntListP1(), csvFile);
		} catch (IOException e) {
			msg.append("File not found " + e.getMessage());
			LOGGER.severe("File not found " + e.getMessage());
			return msg.toString();
		}

		// generate arff
		try {
			msg.append(ArffGenerator.generateDataset(csvFile, ArffFile, false));
		} catch (Exception e) {
			msg.append("Arff file could not be generated " + e.getMessage() + "\n");
			LOGGER.severe("Arff file could not be generated " + e.getMessage());
			return msg.toString();
		}
		return msg.toString();

	}

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
	public String preprocessAndGenerateArffForPhase2(String inputUnisensFolderLoc, String LogFile, String ArffFile,
			boolean startedAfter1min) throws IOException {

		StringBuffer msg = new StringBuffer();
		String temp_path = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");

		File file = new File(temp_path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}
		msg.append("Log file provided :" + LogFile + "\n");
		LOGGER.info("Log file provided :" + LogFile);
		msg.append("Unisens folder provided :" + inputUnisensFolderLoc + "\n");
		LOGGER.info("Unisens folder provided :" + inputUnisensFolderLoc);

		long time = System.currentTimeMillis();
		String csvFile = temp_path + time + "temp//";

		try {
			// generate segments
			SegmentMessageDAO dao = generateSegmentsForPhase2(inputUnisensFolderLoc, LogFile, startedAfter1min);
			msg.append(dao.getMessage());

			if (ArffFile.contains("."))
				ArffFile = ArffFile.split("\\.")[0];
			// generate csv
			generateCSVForPhase2(dao.getSgmntListP2(), csvFile, ArffFile);
		} catch (IOException e) {
			msg.append("File not found " + e.getMessage());
			LOGGER.severe("File not found " + e.getMessage());
			return msg.toString();
		}

		// generate arff
		try {
			msg.append("Arff to be generated :" + ArffFile + "/" + TOTAL_ARFF + " , " + ArffFile + "/" + REL_ARFF
					+ " , " + ArffFile + "/" + FACT_ARFF + " , " + ArffFile + "/" + SENTI_ARFF + "\n");
			LOGGER.info("Arff will be generated :" + ArffFile);

			msg.append(ArffGenerator.generateDataset(csvFile + TOTAL_CSV, ArffFile + "/" + TOTAL_ARFF, false));
			msg.append(ArffGenerator.generateDataset(csvFile + REL_CSV, ArffFile + "/" + REL_ARFF, false));
			msg.append(ArffGenerator.generateDataset(csvFile + FACT_CSV, ArffFile + "/" + FACT_ARFF, false));
			msg.append(ArffGenerator.generateDataset(csvFile + SENTI_CSV, ArffFile + "/" + SENTI_ARFF, false));
		} catch (

		Exception e) {
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

		List<LogEntryDAOPhase1> logEntries = getLogEntriesForPhase1(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<SegmentDAOPhase1> segments = new ArrayList<>();
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
						segments.add(new SegmentDAOPhase1(sbf.toString(), cls, samplesToAdd));
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
		dao.setSgmntListP1(segments);
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
	 *             The method generates segment object for phase2. each segment dao
	 *             represents either hard or easy ts
	 */
	public SegmentMessageDAO generateSegmentsForPhase2(String inputUnisensLoc, String logLoc, boolean startedAfter1min)
			throws IOException {

		SegmentMessageDAO dao = new SegmentMessageDAO();
		StringBuffer msg = new StringBuffer();
		msg.append("Generating Segments from the Log and Unisens File for phase 2...\n");
		LOGGER.info("Generating Segments from the Log and Unisens File for phase 2...");

		List<LogEntryDAOPhase2> logEntries = getLogEntriesForPhase2(logLoc);
		UnisensCSVGenerator unisens = new UnisensCSVGenerator();
		List<Double> uniData = unisens.generateDataFromBin(inputUnisensLoc, null, false);
		List<SegmentDAOPhase2> segments = new ArrayList<>();
		int counter = startedAfter1min ? 1920 : 0; // start after 1min- (32*60)

		try {
			for (int index = 0; index < logEntries.size(); index++) {

				int samplesToAdd = (SAMPLING_RATE * logEntries.get(index).getTotalTimeTaken()) / 1000;
				int samplesToAddUntilRelevant = (SAMPLING_RATE * logEntries.get(index).getRelevantTimeTaken()) / 1000;
				int samplesToAddUntilNFactual = (SAMPLING_RATE * logEntries.get(index).getNonFactualTimeTaken()) / 1000;
				int samplesToAddUntilSentiment = (SAMPLING_RATE * logEntries.get(index).getSentimentTimeTaken()) / 1000;
				StringBuffer total = new StringBuffer();
				StringBuffer rel = new StringBuffer();
				StringBuffer fact = new StringBuffer();
				StringBuffer senti = new StringBuffer();

				if (logEntries.get(index).isTweet()) {
					if ((!startedAfter1min && index > 0) || startedAfter1min) {
						for (int i = counter; i <= (counter + samplesToAdd); i++) {
							total.append(uniData.get(i)).append(",");
							if (i <= (counter + samplesToAddUntilRelevant))
								rel.append(uniData.get(i)).append(",");
							if (i <= (counter + samplesToAddUntilNFactual))
								fact.append(uniData.get(i)).append(",");
							if (i <= (counter + samplesToAddUntilSentiment))
								senti.append(uniData.get(i)).append(",");
						}

						total.deleteCharAt(total.length() - 1);
						rel.deleteCharAt(rel.length() - 1);
						fact.deleteCharAt(fact.length() - 1);
						senti.deleteCharAt(senti.length() - 1);

						segments.add(new SegmentDAOPhase2(logEntries.get(index).getTweetId(), total.toString(),
								rel.toString(), fact.toString(), senti.toString(), samplesToAdd));
					}
					LOGGER.info(logEntries.get(index).getTweetId() + " Tweet, has length " + samplesToAdd + " range :"
							+ counter + "-" + (counter + samplesToAdd));
				} else {
					LOGGER.info("R" + " Segment, has length " + samplesToAdd + " range :" + counter + "-"
							+ (counter + samplesToAdd));
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
		dao.setSgmntListP2(segments);
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
	public void generateCSVForPhase1(List<SegmentDAOPhase1> segments, String outputLoc) throws IOException {

		File f = new File(outputLoc);
		if (f.exists()) {
			f.delete();
		}
		int maxLngth = getMaxLengthForPhase1(segments) + 1;
		List<SegmentDAOPhase1> paddedSegments = fillWithMissingNotationForP1(segments, maxLngth);
		// generate and add header to csv
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i <= maxLngth; i++) {
			sbf.append(i == maxLngth ? "class" : "a" + i + ",");
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLoc, true));) {
			writer.write(sbf.toString());
			writer.append("\n");
			// add actual data to csv
			for (SegmentDAOPhase1 sgmnt : paddedSegments) {
				writer.write(sgmnt.getTimeseries() + "," + sgmnt.getClassName());
				writer.append("\n");
			}
		}
	}

	/**
	 * @param segments
	 * @param outputLoc
	 * @throws IOException
	 * 
	 *             The method generates csv from segments of timeseries. each
	 *             segment is either hard or easy type
	 */
	public void generateCSVForPhase2(List<SegmentDAOPhase2> segments, String outputLoc, String tweetFileLoc)
			throws IOException {
		File dir = new File(outputLoc);
		File tweetFileLocation = new File(tweetFileLoc);
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}
		
		if (!tweetFileLocation.exists()) {
			if (tweetFileLocation.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}
		int maxLngth = getMaxLengthForPhase2(segments) + 1;
		List<SegmentDAOPhase2> paddedSegments = fillWithMissingNotationforP2(segments, maxLngth);
		// generate and add header to csv
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i <= maxLngth; i++) {
			sbf.append(i == maxLngth ? "class" : "a" + i + ",");
		}

		try (BufferedWriter total = new BufferedWriter(new FileWriter(outputLoc + TOTAL_CSV, true));
				BufferedWriter rel = new BufferedWriter(new FileWriter(outputLoc + REL_CSV, true));
				BufferedWriter fact = new BufferedWriter(new FileWriter(outputLoc + FACT_CSV, true));
				BufferedWriter senti = new BufferedWriter(new FileWriter(outputLoc + SENTI_CSV, true));
				BufferedWriter tweetList = new BufferedWriter(new FileWriter(tweetFileLoc +"/"+ TWEET_LIST, true));) {
			total.write(sbf.toString());
			total.append("\n");
			rel.write(sbf.toString());
			rel.append("\n");
			fact.write(sbf.toString());
			fact.append("\n");
			senti.write(sbf.toString());
			senti.append("\n");

			// add actual data to csv
			for (SegmentDAOPhase2 sgmnt : paddedSegments) {
				total.write(sgmnt.getTotalTimeseries() + "," + "0");
				total.append("\n");
				rel.write(sgmnt.getRelevantTimeseries() + "," + "0");
				rel.append("\n");
				fact.write(sgmnt.getNonfactualTimeseries() + "," + "0");
				fact.append("\n");
				senti.write(sgmnt.getSentimentTimeseries() + "," + "0");
				senti.append("\n");
				tweetList.write(sgmnt.getTweetId());
				tweetList.append("\n");
			}
		}
	}

	/**
	 * @param segments
	 * @return
	 * 
	 * 		The method returns longest segment's length for p1
	 */
	private static int getMaxLengthForPhase1(List<SegmentDAOPhase1> segments) {
		int max = 0;
		for (SegmentDAOPhase1 dao : segments) {
			max = max < dao.getLength() ? dao.getLength() : max;
		}
		return max;
	}

	/**
	 * @param segments
	 * @return
	 * 
	 * 		The method returns longest segment's length for p2
	 */
	private static int getMaxLengthForPhase2(List<SegmentDAOPhase2> segments) {
		int max = 0;
		for (SegmentDAOPhase2 dao : segments) {
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
	private static List<LogEntryDAOPhase1> getLogEntriesForPhase1(String logLoc) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(logLoc));
		List<LogEntryDAOPhase1> elist = new ArrayList<>();
		for (String line : lines) {
			String[] arr = line.split(",");
			LogEntryDAOPhase1 dao = new LogEntryDAOPhase1(arr[0], Integer.parseInt(arr[4]));
			elist.add(dao);
		}
		return elist;

	}

	/**
	 * @param logLoc
	 * @return
	 * @throws IOException
	 * 
	 *             The method reads a log file and creates log dao
	 */
	private static List<LogEntryDAOPhase2> getLogEntriesForPhase2(String logLoc) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(logLoc));
		List<LogEntryDAOPhase2> elist = new ArrayList<>();
		String[] arr = null;
		try {
			for (String line : lines) {
				arr = line.split(",");
				boolean isTweet = arr[0].equals("T") ? true : false;
				int total = Integer.parseInt(arr[10]);
				int relevant = arr[4].equals("Relevant") ? Integer.parseInt(arr[5]) : total;
				int nonf = arr[4].equals("Relevant") ? Integer.parseInt(arr[7]) : total;
				int senti = arr[4].equals("Relevant") && arr[6].equals("Non-factual") ? Integer.parseInt(arr[9])
						: total;

				LogEntryDAOPhase2 dao = new LogEntryDAOPhase2(isTweet, arr[1], relevant, nonf, senti, total);
				elist.add(dao);
			}
		} catch (NumberFormatException e) {
			LOGGER.severe("Exception for tweet " + arr[1]);
			LOGGER.severe(e.getMessage());
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
	private static List<SegmentDAOPhase1> fillWithMissingNotationForP1(List<SegmentDAOPhase1> segments, int max) {
		StringBuffer sbf = new StringBuffer();
		List<SegmentDAOPhase1> modifiedS = new ArrayList<>();
		for (SegmentDAOPhase1 sgmnt : segments) {
			int length = sgmnt.getTimeseries().split(",").length;
			// System.out.println(length);
			if (length < max) {
				sbf = new StringBuffer(sgmnt.getTimeseries());
				for (int i = 0; i < (max - length); i++) {
					sbf.append(",?"); // jus append comma for missing values
				}
				modifiedS.add(new SegmentDAOPhase1(sbf.toString(), sgmnt.getClassName(), max));
			} else {
				modifiedS.add(sgmnt);
			}

		}
		return modifiedS;
	}

	/**
	 * @param segments
	 * @param max
	 * @return
	 * 
	 * 		The method fills the shorter segments with '?' notation, which is
	 *         needed to create arff file in weka
	 */
	private static List<SegmentDAOPhase2> fillWithMissingNotationforP2(List<SegmentDAOPhase2> segments, int max) {
		StringBuffer total = new StringBuffer();
		StringBuffer rel = new StringBuffer();
		StringBuffer fact = new StringBuffer();
		StringBuffer senti = new StringBuffer();

		List<SegmentDAOPhase2> modifiedS = new ArrayList<>();
		for (SegmentDAOPhase2 sgmnt : segments) {

			int length = sgmnt.getTotalTimeseries().split(",").length;
			total = new StringBuffer(sgmnt.getTotalTimeseries());
			if (length < max) {
				for (int i = 0; i < (max - length); i++) {
					total.append(",?"); // jus append comma for missing values
				}
			}

			length = sgmnt.getRelevantTimeseries().split(",").length;
			rel = new StringBuffer(sgmnt.getRelevantTimeseries());
			if (length < max) {
				for (int i = 0; i < (max - length); i++) {
					rel.append(",?"); // jus append comma for missing values
				}
			}

			length = sgmnt.getNonfactualTimeseries().split(",").length;
			fact = new StringBuffer(sgmnt.getNonfactualTimeseries());
			if (length < max) {
				for (int i = 0; i < (max - length); i++) {
					fact.append(",?"); // jus append comma for missing values
				}
			}
			length = sgmnt.getSentimentTimeseries().split(",").length;
			senti = new StringBuffer(sgmnt.getSentimentTimeseries());
			if (length < max) {
				for (int i = 0; i < (max - length); i++) {
					senti.append(",?"); // jus append comma for missing values
				}
			}
			modifiedS.add(new SegmentDAOPhase2(sgmnt.getTweetId(), total.toString(), rel.toString(), fact.toString(),
					senti.toString(), max));
		}
		return modifiedS;
	}

	/**
	 * @param logUnisensPathMap
	 * @return
	 * @throws IOException
	 */
	public SegmentMessageDAO generateSegmentsFromMultipleFilesForP1(List<PersonDAO> logUnisensPathMap)
			throws IOException {

		StringBuffer msg = new StringBuffer();
		SegmentMessageDAO dao = new SegmentMessageDAO();

		LOGGER.info("Generating segements for multiple files...");
		List<SegmentDAOPhase1> segments = new ArrayList<>();
		List<SegmentDAOPhase1> segmentsForOneFile = null;

		for (PersonDAO entry : logUnisensPathMap) {
			LOGGER.info("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName());
			msg.append("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName()
					+ "\n");
			segmentsForOneFile = generateSegmentsForPhase1(entry.getUnisensFolderName(), entry.getLogFileName(),
					entry.isStartedAfterOneMinute()).getSgmntListP1();
			segments.addAll(segmentsForOneFile);
			LOGGER.info("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName());
			msg.append("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName() + "\n");
		}
		dao.setSgmntListP1(segments);
		dao.setMessage(msg.toString());
		return dao;

	}

	/**
	 * @param logUnisensPathMap
	 * @return
	 * @throws IOException
	 */
	public SegmentMessageDAO generateSegmentsFromMultipleFilesForP2(List<PersonDAO> logUnisensPathMap)
			throws IOException {

		StringBuffer msg = new StringBuffer();
		SegmentMessageDAO dao = new SegmentMessageDAO();

		LOGGER.info("Generating segements for multiple files...");
		List<SegmentDAOPhase2> segments = new ArrayList<>();
		List<SegmentDAOPhase2> segmentsForOneFile = null;

		for (PersonDAO entry : logUnisensPathMap) {
			LOGGER.info("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName());
			msg.append("Start Generating segments for " + entry.getLogFileName() + "_" + entry.getUnisensFolderName()
					+ "\n");
			segmentsForOneFile = generateSegmentsForPhase2(entry.getUnisensFolderName(), entry.getLogFileName(),
					entry.isStartedAfterOneMinute()).getSgmntListP2();
			segments.addAll(segmentsForOneFile);
			LOGGER.info("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName());
			msg.append("End Generating " + segmentsForOneFile.size() + " segments for" + entry.getLogFileName() + "_"
					+ entry.getUnisensFolderName() + "\n");
		}
		dao.setSgmntListP2(segments);
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
	public String preprocessAndGenerateArffForMultipleForP1(String targetArffFileName,
			List<PersonDAO> logUnisensPathMap) throws IOException {

		String tempPath = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");

		File file = new File(tempPath);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}

		TrialPhaseSegregrator tp = new TrialPhaseSegregrator();
		StringBuffer sbf = new StringBuffer("Start Generating segments for Phase1 for multiple person\n");

		// create segments for all
		SegmentMessageDAO segmentForAll = tp.generateSegmentsFromMultipleFilesForP1(logUnisensPathMap);
		sbf.append(segmentForAll.getMessage());
		// generate csv
		long curTime = System.currentTimeMillis();
		String csvPath = tempPath + "temp.csv";
		tp.generateCSVForPhase1(segmentForAll.getSgmntListP1(), csvPath);
		sbf.append("Total number of Segments generated : " + segmentForAll.getSgmntListP1().size() + "\n");

		String msg = ArffGenerator.generateDataset(csvPath, targetArffFileName, false);
		sbf.append(msg + "\n");
		sbf.append("End Generating segments for multiple person\n");
		sbf.append("Time taken: " + (System.currentTimeMillis() - curTime) / 1000 + " sec\n");
		return sbf.toString();
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
	public String preprocessAndGenerateArffForMultipleForP2(String targetArffFileName,
			List<PersonDAO> logUnisensPathMap) throws IOException {

		String tempPath = PropertiesHandler.getPropertyVal("TEMP_FILE_PATH");

		File file = new File(tempPath);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}

		TrialPhaseSegregrator tp = new TrialPhaseSegregrator();
		StringBuffer sbf = new StringBuffer("Start Generating segments for Phase2 for multiple person\n");

		// create segments for all
		SegmentMessageDAO segmentForAll = tp.generateSegmentsFromMultipleFilesForP2(logUnisensPathMap);
		sbf.append(segmentForAll.getMessage());
		// generate csv
		long curTime = System.currentTimeMillis();
		String csvPath = tempPath + curTime + "temp//";
		tp.generateCSVForPhase2(segmentForAll.getSgmntListP2(), csvPath, targetArffFileName);
		sbf.append("Total number of Segments generated : " + segmentForAll.getSgmntListP2().size() + "\n");

		// generate arff
		try {
			if (targetArffFileName.contains(".")) {
				targetArffFileName = targetArffFileName.split("\\.")[0];
				sbf.append("Arff to be generated :" + targetArffFileName + "-" + TOTAL_ARFF + " , " + targetArffFileName
						+ "-" + REL_ARFF + " , " + targetArffFileName + "-" + FACT_ARFF + " , " + targetArffFileName
						+ "-" + SENTI_ARFF + "\n");
				LOGGER.info("Arff will be generated :" + targetArffFileName);
			}
			sbf.append(
					ArffGenerator.generateDataset(csvPath + TOTAL_CSV, targetArffFileName + "-" + TOTAL_ARFF, false));
			sbf.append(ArffGenerator.generateDataset(csvPath + REL_CSV, targetArffFileName + "-" + REL_ARFF, false));
			sbf.append(ArffGenerator.generateDataset(csvPath + FACT_CSV, targetArffFileName + "-" + FACT_ARFF, false));
			sbf.append(
					ArffGenerator.generateDataset(csvPath + SENTI_CSV, targetArffFileName + "-" + SENTI_ARFF, false));
		} catch (Exception e) {
			sbf.append("Arff file could not be generated " + e.getMessage() + "\n");
			LOGGER.severe("Arff file could not be generated " + e.getMessage());
			return sbf.toString();
		}
		sbf.append("Time taken: " + (System.currentTimeMillis() - curTime) / 1000 + " sec\n");
		return sbf.toString();
	}

	public static void main(String[] args) {

		String unisens = "E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh";
		String log = "E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh\\logLOG_P2_1534771224576.txt";
		boolean flag = true;
		String target_arff_file = "E:/user-study/arff/testp2.arff";
		TrialPhaseSegregrator a = new TrialPhaseSegregrator();
		String msg;

		String unisensFolder = "E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana/"; // validation-> a folder
		String logFileAbsolutePath = "E:/user-study/drive/Logs/gaja.txt"; // validation-> a txt file
		boolean startedAfter1min = true;
		/**
		 * The inputs from UI : end
		 */

		try {
			msg = a.preprocessAndGenerateArffForPhase2(unisens, log, target_arff_file, flag);
			System.out.println(msg);

			target_arff_file = "E:/user-study/arff/testp1.arff";
			msg = a.preprocessAndGenerateArffForPhase1(unisensFolder, logFileAbsolutePath, target_arff_file,
					startedAfter1min);
			System.out.println(msg);

			target_arff_file = "E:/user-study/arff/testp1M.arff";
			msg = a.preprocessAndGenerateArffForMultipleForP1(target_arff_file, generateLogUnisensMap());
			System.out.println(msg);

			target_arff_file = "E:/user-study/arff/testp2M.arff";
			msg = a.preprocessAndGenerateArffForMultipleForP2(target_arff_file, generateLogUnisensMapP2());
			System.out.println(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return generates the log unisens files mapping for phase1
	 */
	public static List<PersonDAO> generateLogUnisensMap() {
		// unisensFile#logFile, startsAfter1min-boolean

		List<PersonDAO> persons = new ArrayList<>();

		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 14.46.01",
				"E:/user-study/drive/Logs/31_1.txt", false));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 15.31.02",
				"E:/user-study/drive/Logs/31_2.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 17.10.00",
				"E:/user-study/drive/Logs/31_3.txt", false));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-01 17.36.01", "E:/user-study/drive/Logs/1_1.txt",
				false));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-01 18.27.01", "E:/user-study/drive/Logs/1_2.txt",
				false));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-05 15.43.00",
				"E:/user-study/drive/Logs/5_6_1.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-02 17.34.00_Simson",
				"E:/user-study/drive/Logs/Simson.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 14.28.00_Gabriel",
				"E:/user-study/drive/Logs/Gabriel.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 15.19.00_Anne",
				"E:/user-study/drive/Logs/Anne", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana",
				"E:/user-study/drive/Logs/gaja.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 14.28.00_Gabriel",
				"E:/user-study/drive/Logs/Gabriel.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 15.19.00_Anne",
				"E:/user-study/drive/Logs/Anne", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana",
				"E:/user-study/drive/Logs/gaja.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 16.03.01_Darshan",
				"E:/user-study/drive/Logs/darshan.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 16.32.01_Rahul",
				"E:/user-study/drive/Logs/rahul.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 18.08.00_Ankur",
				"E:/user-study/drive/Logs/ankur.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 19.32.02_Naga",
				"E:/user-study/drive/Logs/naga.txt", true));

		/*
		 * persons.add(new
		 * PersonDAO("E:/user-study/drive/unisens/2018-08-21 21.27.01_Agnihotri",
		 * "E:/user-study/drive/Logs/agnihotri.txt", true)); persons.add(new
		 * PersonDAO("E:/user-study/drive/unisens/2018-08-21 22.12.01_Chamanth",
		 * "E:/user-study/drive/Logs/chamanth.txt", true)); persons.add(new
		 * PersonDAO("E:/user-study/drive/unisens/2018-08-21 23.25.01_Harish",
		 * "E:/user-study/drive/Logs/harish.txt", true));
		 */
		return persons;
	}

	/**
	 * @return generates the log unisens files mapping for phase2
	 */
	public static List<PersonDAO> generateLogUnisensMapP2() {
		// unisensFile#logFile, startsAfter1min-boolean

		List<PersonDAO> persons = new ArrayList<>();

		persons.add(new PersonDAO("E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh",
				"E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh\\logLOG_P2_1534771224576.txt", true));
		persons.add(new PersonDAO("E:\\user-study\\p2\\2018-08-20 15.33.01_Ravi_CEE",
				"E:\\user-study\\p2\\2018-08-20 15.33.01_Ravi_CEE\\logLOG_P2_1534772994891.txt", true));

		return persons;
	}
}