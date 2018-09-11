/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class SegmentDAOPhase1 extends SegmentMessageDAO{

	String timeseries;
	String className;
	int length;

	public SegmentDAOPhase1(String timeseries, String className, int length) {
		super();
		this.timeseries = timeseries;
		this.className = className;
		this.length = length;
	}

	public String getTimeseries() {
		return timeseries;
	}

	public void setTimeseries(String timeseries) {
		this.timeseries = timeseries;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
