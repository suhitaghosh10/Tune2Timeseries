/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class SegmentDAOPhase2 extends SegmentMessageDAO{

	String totalTimeseries;
	String relevantTimeseries;
	String nonfactualTimeseries;
	String sentimentTimeseries;
	int length;

	public SegmentDAOPhase2(String totalTimeseries, String relevantTimeseries, String nonfactualTimeseries,
			String sentimentTimeseries, int length) {
		super();
		this.totalTimeseries = totalTimeseries;
		this.relevantTimeseries = relevantTimeseries;
		this.nonfactualTimeseries = nonfactualTimeseries;
		this.sentimentTimeseries = sentimentTimeseries;
		this.length = length;
	}

	public String getTotalTimeseries() {
		return totalTimeseries;
	}

	public void setTotalTimeseries(String totalTimeseries) {
		this.totalTimeseries = totalTimeseries;
	}

	public String getRelevantTimeseries() {
		return relevantTimeseries;
	}

	public void setRelevantTimeseries(String relevantTimeseries) {
		this.relevantTimeseries = relevantTimeseries;
	}

	public String getNonfactualTimeseries() {
		return nonfactualTimeseries;
	}

	public void setNonfactualTimeseries(String nonfactualTimeseries) {
		this.nonfactualTimeseries = nonfactualTimeseries;
	}

	public String getSentimentTimeseries() {
		return sentimentTimeseries;
	}

	public void setSentimentTimeseries(String sentimentTimeseries) {
		this.sentimentTimeseries = sentimentTimeseries;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
