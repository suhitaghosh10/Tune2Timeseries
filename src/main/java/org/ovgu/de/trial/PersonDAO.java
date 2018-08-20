/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class PersonDAO {

	String unisensFolderName;
	String logFileName;
	boolean startedAfterOneMinute;

	public PersonDAO(String unisensFolderName, String logFileName, boolean startedAfterOneMinute) {
		super();
		this.unisensFolderName = unisensFolderName;
		this.logFileName = logFileName;
		this.startedAfterOneMinute = startedAfterOneMinute;
	}

	public String getUnisensFolderName() {
		return unisensFolderName;
	}

	public void setUnisensFolderName(String unisensFolderName) {
		this.unisensFolderName = unisensFolderName;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public boolean isStartedAfterOneMinute() {
		return startedAfterOneMinute;
	}

	public void setStartedAfterOneMinute(boolean startedAfterOneMinute) {
		this.startedAfterOneMinute = startedAfterOneMinute;
	}

}
