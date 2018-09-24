/**
 * 
 */
package org.ovgu.de.trial;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Suhita Ghosh
 *
 */
public class Utility {

	
	private static Logger LOGGER = Logger.getLogger(Utility.class.getName());
	/**
	 * @param propName
	 * @return The method returns the property value associated wit the property
	 *         name passed in the method, for phase1
	 * @throws IOException
	 */
	public static String getPropertyVal(String propName) throws IOException {
		Properties prop = new Properties();

		InputStream input = Utility.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE);

		prop.load(input);
		return (prop.getProperty(propName));
	}
	
	/**
	 * @param path
	 * check if a directory exists. if not create it
	 */
	public static void checkAndCreateDirectory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info("Directory is now created!");
			} else {
				LOGGER.warning("Failed to create directory may be already existing!");
			}
		}
	}
	
	
	/**
	 * @param outputLoc
	 * 
	 * check if the file exists. if exists then delete it
	 */
	public static void deleteFile(String fileLoc) {
		File f = new File(fileLoc);
		if (f.exists()) {
			f.delete();
		}
	}
}
