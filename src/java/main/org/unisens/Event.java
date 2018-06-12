/*
Unisens Interface - interface for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV),
				   KIT, Germany

This file is part of the Unisens Interface. For more information, see
<http://www.unisens.org>

The Unisens Interface is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Interface is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Interface. If not, see <http://www.gnu.org/licenses/>. 
*/

package org.unisens;

/**
 * An Event happens at a cetain point in time and be of a certain type. It can have a comment. Events
 * have no signal or value data.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public class Event implements Cloneable{
	private long sampleStamp;
	private String type = null;
	private String comment = null;
	
	/**
	 * Gets the comment of this Event
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment for this Event. The maximum comment length is defined in the 
	 * EventEntry. If the comment is too long it will be truncated.
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Gets the sample stamp of this Event. The sample stamp is given in sample counts. 
	 * The real time offset can be calculated using the sampleRate of the EventEntry.
	 * 
	 * @return the sample stamp in sample counts 
	 */
	public long getSampleStamp() {
		return sampleStamp;
	}

	/**
	 * Sets the sample stamp of this Event. The sample stamp is given in sample counts. The real time 
	 * offset can be calculated using the sampleRate of the EventEntry
	 * 
	 * @param sampleStamp the sample stamp in sample counts
	 */
	public void setSampleStamp(long sampleStamp) {
		this.sampleStamp = sampleStamp;
	}
	/**
	 * Gets the samplestamp of this Event. The samplestamp is given in sample counts. 
	 * The real time offset can be calculated using the sampleRate of the EventEntry.
	 * 
	 * @return the samplestamp in sample counts 
	 * @deprecated use getSampleStamp
	 * @see org.unisens.getSampleStamp
	 */
	@Deprecated
	public long getSamplestamp() {
		return sampleStamp;
	}

	/**
	 * Sets the samplestamp of this Event. The samplestamp is given in sample counts. The real time 
	 * offset can be calculated using the sampleRate of the EventEntry
	 * 
	 * @param samplestamp the samplestamp in sample counts
	 * @deprecated use setSampleStamp
	 * @see org.unisens.setSampleStamp
	 */
	@Deprecated
	public void setSamplestamp(long samplestamp) {
		this.sampleStamp = samplestamp;
	}

	/**
	 * Gets the the type of this Event. The type can be one ore more characters. The length of the type 
	 * is defined in the EventEntry.
	 * 
	 * @return the type of this Event
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the the type of this Event. The type can be one ore more characters. The length of the type 
	 * is defined in the EventEntry. 

	 * @param type the type of this Event
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Event Contructor
	 * 
	 * @param samplestamp the samplestamp of this Event in sample counts
	 * @param type the type of this Event
	 * @param comment the comment of this Event
	 */
	public Event(long samplestamp , String type , String comment){
		this.sampleStamp = samplestamp;
		this.type = type;
		this.comment = comment;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Event))
			return false;
		if(this.sampleStamp == ((Event)o).getSampleStamp() && this.type.equalsIgnoreCase(((Event)o).getType()) && (((this.comment == null)&&(((Event)o).getComment() == null)) || this.comment.equalsIgnoreCase(((Event)o).getComment()))){
			return true;
		}
		return false;
	}
	
	/**
	 * Truncate comments and types when necessary
	 * 
	 * @param typeLength specified typeLength
	 * @param commentLength specified commentLength
	 */
	public void trim(int typeLength, int commentLength)
	{
		if (this.getComment().length() > commentLength)
		{
			this.setComment(this.getComment().substring(0, commentLength));
		}
		if (this.getType().length() > typeLength)
		{
			this.setType(this.getType().substring(0, typeLength));
		}
	}
}
