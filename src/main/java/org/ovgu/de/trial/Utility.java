/**
 * 
 */
package org.ovgu.de.trial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Suhita Ghosh
 *
 */
public class Utility {

	
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
}
