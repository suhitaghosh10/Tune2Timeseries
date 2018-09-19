/**
 * 
 */
package org.ovgu.de.trial;

import java.util.ArrayList;
import java.util.List;

import org.ovgu.de.classifier.boss.BOSS;
import org.ovgu.de.classifier.saxvsm.SAXVSM;
import org.ovgu.de.classifier.utility.ClassifierTools;
import org.ovgu.de.utils.Constants;
import org.ovu.de.hive.HiveCote;

import weka.classifiers.Classifier;
import weka.classifiers.meta.RotationForest;
import weka.core.Instances;

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
		TrialPhaseSegregrator tp = new TrialPhaseSegregrator();

		/**
		 * Phase1::: Preprocess for 1 file => The inputs from UI : start
		 */
//		String target_arff_file = "E:/user-study/arff/suhita"; // validation-> should end with .arff ext
//		String unisensFolder = "E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana/"; // validation-> a folder
//		String logFileAbsolutePath = "E:/user-study/drive/Logs/gaja.txt"; // validation-> a txt file
//		boolean startedAfter1min = true;
		/**
		 * The inputs from UI : end
		 */

//		String msg = tp.preprocessAndGenerateArffForPhase1(unisensFolder, logFileAbsolutePath, target_arff_file,
//				startedAfter1min);
//		System.out.println(msg);

		/**
		 * Preprocess for 1 file : end
		 */

		/**
		 * Preprocess for Multiple file => The inputs from UI : start
		 */
		//target_arff_file = "E:/user-study/arff/";// validation-> a folder
		// generated from multiple unisens,log,startedAfter1min inputs
		List<PersonDAO> logUnisensPathMap = generateLogUnisensMap();
		/**
		 * The inputs from UI : end
		 */
		//String msg2 = tp.preprocessAndGenerateArffForMultipleForP1(target_arff_file, logUnisensPathMap);
		//System.out.println(msg2);

		/**
		 * Phase2::: Preprocess for 1 file
		 */
//		String unisens = "E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh";
//		String log = "E:\\user-study\\p2\\2018-08-20 15.05.01_Suresh\\logLOG_P2_1534771224576.txt";
////		target_arff_file = "E:/user-study/arff/testp2M.arff";
////		startedAfter1min = true;
////
////		msg = tp.preprocessAndGenerateArffForPhase2(unisens, log, target_arff_file, startedAfter1min);
////		System.out.println(msg);

		/**
		 * Preprocess for Multiple files : end
		 */

		/**
		 * Classifier SAXVSM : start
		 */
		
		try {

			/**
			 * Input for Build Classifier : start
			 */
			Instances train = ClassifierTools.loadData("/Users/nikhil/Desktop/KMD/ARFF/train.arff"); // validation-> ends
																							// with
																							// .arff
			Instances test = ClassifierTools.loadData("/Users/nikhil/Desktop/KMD/ARFF/test.arff");// validation-> ends with
																							// .arff
			// path where you will generate the csv, which will further store details
			String resultsPath = "/Users/nikhil/Desktop/KMD/ARFF/";// validation-> a folder
			int foldsNo = 10; // validation-> an integer and >=2
			String classifierSaveLoc = "/Users/nikhil/Desktop/KMD/ARFF/";// validation-> a folder
			String modelName = Constants.ROT_F;// from dropdown, hence no validation
			System.out.println(modelName + "....");

			if (modelName.equals(Constants.SAXVSM)) {

				SAXVSM vsm = new SAXVSM();
				// the model will be saved as <classifierSaveLoc>+<modelName>.model
				String msg3 = vsm.buildClassifierAndSave(train, test, classifierSaveLoc, modelName, foldsNo,
						resultsPath);
				System.out.println(msg3);

				/**
				 * Input for Apply Classifier for phase2 -SAXVSM : start
				 */
				String vsmclassifier = "E:/user-study/arff/sax.model";// validation-> ends with .model
				test = ClassifierTools.loadData("E:\\user-study\\suhitatotal.arff");// validation-> ends with .arff
				/**
				 * Input for Apply Classifier for phase2 -SAXVSM : end
				 */
				String msgAplyCl = vsm.applyClassifier(test, vsmclassifier, false);
				System.out.println(msgAplyCl);

			} else if (modelName.equalsIgnoreCase(Constants.BOSS)) {

				BOSS boss = new BOSS();
				String bossBCl = boss.buildClassifierAndSave(train, test, classifierSaveLoc, modelName, foldsNo,
						resultsPath);
				System.out.println(bossBCl);

				/**
				 * input
				 */
				String bossclassifier = "/Users/nikhil/Desktop/KMD/ARFF/ boss.model";// validation-> ends with .model
				/**
				 * end input
				 */
				String bossACl = boss.applyClassifier(test, bossclassifier, true);
				System.out.println(bossACl);

				/**
				 * Boss - End
				 */
			} else if (modelName.equalsIgnoreCase(Constants.ROT_F)) {

				RotationForest rotf = new RotationForest();
				String rotfBCl = rotf.buildClassifierAndSave(train, test, classifierSaveLoc, modelName, foldsNo,
						resultsPath);
				System.out.println(rotfBCl);

				/**
				 * input
				 */
				String rotfclassifier = "/Users/nikhil/Desktop/KMD/ARFF/rotf.model";// validation-> ends with .model
				/**
				 * end input
				 */
				String rotfACl = rotf.applyClassifier(test,rotfclassifier, false);
				System.out.println(rotfACl);

				/**
				 * Boss - End
				 */
			}else if (modelName.equalsIgnoreCase(Constants.COTE)) {

				HiveCote rotf = new HiveCote();
				String bossBCl = rotf.buildClassifierAndSave(train, test, classifierSaveLoc, modelName, foldsNo,
						resultsPath);
				System.out.println(bossBCl);

				/**
				 * input
				 */
				String bossclassifier = "/Users/nikhil/Desktop/KMD/ARFF/cote.model";// validation-> ends with .model
//				String bossclassifier = "E:/user-study/arff/rotf.model";// validation-> ends with .model
//				String bossclassifier = "E:/user-study/arff/rotf.model";// validation-> ends with .model
//				String bossclassifier = "E:/user-study/arff/rotf.model";// validation-> ends with .model
//				String bossclassifier = "E:/user-study/arff/rotf.model";// validation-> ends with .model
//				String bossclassifier = "E:/user-study/arff/rotf.model";// validation-> ends with .model
				/**
				 * end input
				 */
				Instances testRotf = ClassifierTools.loadData("/Users/nikhil/Desktop/KMD/ARFF/test.arff");
				String bossACl = rotf.applyClassifier(testRotf, bossclassifier, false);
				

				/**
				 * Boss - End
				 */
			}

			Classifier c = new RotationForest();
			((RotationForest) c).setNumIterations(50);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	/**
	 * @return generates the log unisens files mapping
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
	 * @return generates the log unisens files mapping
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
