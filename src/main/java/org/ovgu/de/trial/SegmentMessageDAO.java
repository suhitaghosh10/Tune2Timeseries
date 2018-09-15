/**
 * 
 */
package org.ovgu.de.trial;

import java.util.List;

/**
 * @author Suhita Ghosh
 *
 */
public class SegmentMessageDAO {

	List<SegmentDAOPhase1> sgmntListP1;
	List<SegmentDAOPhase2> sgmntListP2;
	String message;

	public List<SegmentDAOPhase1> getSgmntListP1() {
		return sgmntListP1;
	}

	public void setSgmntListP1(List<SegmentDAOPhase1> sgmntListP1) {
		this.sgmntListP1 = sgmntListP1;
	}

	public List<SegmentDAOPhase2> getSgmntListP2() {
		return sgmntListP2;
	}

	public void setSgmntListP2(List<SegmentDAOPhase2> sgmntListP2) {
		this.sgmntListP2 = sgmntListP2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
