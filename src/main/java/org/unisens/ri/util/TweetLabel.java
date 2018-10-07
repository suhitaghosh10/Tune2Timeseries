/**
 * 
 */
package org.unisens.ri.util;

/**
 * @author Suhita Ghosh
 *
 */
public class TweetLabel {

	private String relevantLabel;
	private String factualLabel;
	private String sentimentLabel;

	public String getRelevantLabel() {
		return relevantLabel;
	}

	public void setRelevantLabel(String relevantLabel) {
		this.relevantLabel = relevantLabel;
	}

	public String getFactualLabel() {
		return factualLabel;
	}

	public void setFactualLabel(String factualLabel) {
		this.factualLabel = factualLabel;
	}

	public String getSentimentLabel() {
		return sentimentLabel;
	}

	public void setSentimentLabel(String sentimentLabel) {
		this.sentimentLabel = sentimentLabel;
	}

	public TweetLabel(String relevantLabel, String factualLabel, String sentimentLabel) {
		super();
		this.relevantLabel = relevantLabel;
		this.factualLabel = factualLabel;
		this.sentimentLabel = sentimentLabel;
	}
	
	

}
