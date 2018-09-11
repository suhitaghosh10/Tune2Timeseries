/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class LogEntryDAOPhase2 {

	private boolean isTweet;
	private String tweetId;
	private int relevantTimeTaken;
	private int nonFactualTimeTaken;
	private int sentimentTimeTaken;
	private int totalTimeTaken;

	public LogEntryDAOPhase2(boolean isTweet, String tweetId, int relevantTimeTaken, int nonFactualTimeTaken,
			int sentimentTimeTaken, int totalTimeTaken) {
		super();
		this.isTweet = isTweet;
		this.tweetId = tweetId;
		this.relevantTimeTaken = relevantTimeTaken;
		this.nonFactualTimeTaken = nonFactualTimeTaken;
		this.sentimentTimeTaken = sentimentTimeTaken;
		this.totalTimeTaken = totalTimeTaken;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public int getRelevantTimeTaken() {
		return relevantTimeTaken;
	}

	public void setRelevantTimeTaken(int relevantTimeTaken) {
		this.relevantTimeTaken = relevantTimeTaken;
	}

	public int getNonFactualTimeTaken() {
		return nonFactualTimeTaken;
	}

	public void setNonFactualTimeTaken(int nonFactualTimeTaken) {
		this.nonFactualTimeTaken = nonFactualTimeTaken;
	}

	public int getSentimentTimeTaken() {
		return sentimentTimeTaken;
	}

	public void setSentimentTimeTaken(int sentimentTimeTaken) {
		this.sentimentTimeTaken = sentimentTimeTaken;
	}

	public int getTotalTimeTaken() {
		return totalTimeTaken;
	}

	public void setTotalTimeTaken(int totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}

	public boolean isTweet() {
		return isTweet;
	}

	public void setTweet(boolean isTweet) {
		this.isTweet = isTweet;
	}

}
