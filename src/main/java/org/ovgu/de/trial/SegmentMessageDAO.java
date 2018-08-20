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

	List<SegmentDAO> sgmntList;
	String message;

	public List<SegmentDAO> getSgmntList() {
		return sgmntList;
	}

	public void setSgmntList(List<SegmentDAO> sgmntList) {
		this.sgmntList = sgmntList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
