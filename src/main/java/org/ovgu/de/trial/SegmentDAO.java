/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class SegmentDAO {

	String timeseries;
	int length;
	
	
	public SegmentDAO(String timeseries, int length) {
		super();
		this.timeseries = timeseries;
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
	
	
}
