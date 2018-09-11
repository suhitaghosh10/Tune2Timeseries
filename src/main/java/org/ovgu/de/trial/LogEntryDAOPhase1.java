/**
 * 
 */
package org.ovgu.de.trial;

/**
 * @author Suhita Ghosh
 *
 */
public class LogEntryDAOPhase1 {

	private int questionId;
	private String easyHardRelaxFlag;
	private int duration;

	public LogEntryDAOPhase1(String easyHardRelaxFlag, int duration) {
		super();
		this.easyHardRelaxFlag = easyHardRelaxFlag;
		this.duration = duration;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getEasyHardRelaxFlag() {
		return easyHardRelaxFlag;
	}

	public void setEasyHardRelaxFlag(String easyHardRelaxFlag) {
		this.easyHardRelaxFlag = easyHardRelaxFlag;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
