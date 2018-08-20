/**
 * 
 */
package org.ovgu.de.trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ovgu.de.classifier.boss.BOSS;
import org.ovgu.de.classifier.saxvsm.SAXVSM;
import org.ovgu.de.classifier.utility.ClassifierTools;

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
		TrialPhaseISegregrator tp = new TrialPhaseISegregrator();

		/**
		 * Preprocess for 1 file => The inputs from UI : start
		 */
		String target_arff_file = "E:/user-study/arff/suhita"; // validation-> should end with .arff ext
		String unisensFolder = "E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana/"; // validation-> a folder
		String logFileAbsolutePath = "E:/user-study/drive/Logs/gaja.txt"; // validation-> a txt file
		boolean startedAfter1min = true;
		/**
		 * The inputs from UI : end
		 */

		String msg = tp.preprocessAndGenerateArff(unisensFolder, logFileAbsolutePath, target_arff_file,startedAfter1min);
		System.out.println(msg);

		/**
		 * Preprocess for 1 file : end
		 */

		/**
		 * Preprocess for Multples file => The inputs from UI : start
		 */
		target_arff_file = "E:/user-study/arff/";// validation-> a folder
		// generated from multiple unisens,log,startedAfter1min inputs
				List<PersonDAO> logUnisensPathMap = generateLogUnisensMap();
		/**
		 * The inputs from UI : end
		 */
		String trainFileName = tp.preprocessAndGenerateArffForMultiple(target_arff_file, logUnisensPathMap);
		System.out.println(trainFileName);

		/**
		 * Preprocess for Multiple files : end
		 */

		/**
		 * Classifier SAXVSM : start
		 */
		System.out.println("SAXVSM....");
		try {

			/**
			 * Input for Build Classifier -SAXVSM : start
			 */
			Instances train = ClassifierTools.loadData("E:\\user-study\\arff\\train.arff"); // validation-> ends with .arff
			Instances test = ClassifierTools.loadData("E:\\user-study\\arff\\test.arff");//  validation-> ends with .arff
			// path where you will generate the csv, which will further store details
			String resultsPath = "E:/user-study/output";//  validation-> a folder
			int foldsNo = 10; //  validation-> an integer and >=2
			String classifierSaveLoc = "E:/user-study/arff/";//  validation-> a folder
			String modelName = "sax";// from dropdown, hence no validation
			/**
			 * Input for Build Classifier -SAXVSM : end
			 */
			
			SAXVSM vsm = new SAXVSM();
			// the model will be saved as <classifierSaveLoc>+<modelName>.model
			vsm.buildClassifierAndSave(train, test, classifierSaveLoc, modelName, foldsNo, resultsPath);
			/**
			 * Build Classifier -SAXVSM : end
			 */

			/**
			 * Input for Apply Classifier for phase2 -SAXVSM : start
			 */
			String vsmclassifier = "E:/user-study/arff/sax.model";// validation-> ends with .model
			test = ClassifierTools.loadData("E:\\user-study\\drive\\arff\\test.arff");// validation->  ends with .arff
			/**
			 * Input for Apply Classifier for phase2 -SAXVSM : end
			 */
			vsm.applyClassifier(test, vsmclassifier);

			/**
			 * Classifier Boss : same as saxvsm, except boss_modelName
			 */
			BOSS boss = new BOSS();
			String boss_modelName = "boss"; // from dropdown
			boss.buildClassifierAndSave(train, test, classifierSaveLoc, boss_modelName, foldsNo, resultsPath);

			String bossclassifier = "E:/user-study/arff/boss.model";// validation-> ends with .model
			boss.applyClassifier(test, bossclassifier);
			/**
			 * Boss - End
			 */
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
		
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 14.46.01", "E:/user-study/drive/Logs/31_1.txt", false));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 15.31.02","E:/user-study/drive/Logs/31_2.txt", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-05-31 17.10.00","E:/user-study/drive/Logs/31_3.txt", false));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-01 17.36.01","E:/user-study/drive/Logs/1_1.txt", false));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-01 18.27.01","E:/user-study/drive/Logs/1_2.txt", false));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-05 15.43.00","E:/user-study/drive/Logs/5_6_1.txt", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-02 17.34.00_Simson","E:/user-study/drive/Logs/Simson.txt", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 14.28.00_Gabriel","E:/user-study/drive/Logs/Gabriel.txt",
		 true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 15.19.00_Anne","E:/user-study/drive/Logs/Anne", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana","E:/user-study/drive/Logs/gaja.txt", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 14.28.00_Gabriel","E:/user-study/drive/Logs/Gabriel.txt",
		 true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-06-03 15.19.00_Anne","E:/user-study/drive/Logs/Anne", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 15.19.01_Gajanana","E:/user-study/drive/Logs/gaja.txt", true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 16.03.01_Darshan","E:/user-study/drive/Logs/darshan.txt",
		 true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 16.32.01_Rahul","E:/user-study/drive/Logs/rahul.txt",
		 true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 18.08.00_Ankur","E:/user-study/drive/Logs/ankur.txt",
		 true));
		 persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 19.32.02_Naga","E:/user-study/drive/Logs/naga.txt",
		 true));
		 
/*
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 21.27.01_Agnihotri",
				"E:/user-study/drive/Logs/agnihotri.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 22.12.01_Chamanth",
				"E:/user-study/drive/Logs/chamanth.txt", true));
		persons.add(new PersonDAO("E:/user-study/drive/unisens/2018-08-21 23.25.01_Harish",
				"E:/user-study/drive/Logs/harish.txt", true));
*/
		return persons;
	}

}
