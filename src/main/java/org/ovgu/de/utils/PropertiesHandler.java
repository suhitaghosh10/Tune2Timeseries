/**
 * 
 */
package org.ovgu.de.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Suhita Ghosh
 *
 */
public class PropertiesHandler {

	/**
	 * @param propName
	 * @return The method returns the property value associated wit the property
	 *         name passed in the method
	 * @throws IOException
	 */
	public static String getPropertyVal(String propName) throws IOException {
		Properties prop = new Properties();

		InputStream input = PropertiesHandler.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE);

		prop.load(input);
		return (prop.getProperty(propName));
	}
}
