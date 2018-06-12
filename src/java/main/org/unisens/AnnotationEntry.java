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

import java.io.IOException;

/**
 * AnnotationEntry is the superclass of EventEntry and SignalEntry and defines the common methods.
 * Direct instances of AnnotationEntry are normally not used.
 *  
 * @author Malte Kirst
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 *
 */

public interface AnnotationEntry extends TimedEntry {
	/**
	 * Deletes all Events from the data file.
	 * 
	 * @throws IOException
	 */
	void empty()throws IOException;
	
	
	/**
	 * Gets the comment length of this EventEntry. This 
	 * value defines the maximum allowed length for the 
	 * comment field
	 * 
	 * @return the comment length
	 */
	int getCommentLength();
	
	/**
	 * Gets the type length of this EventEntry. This 
	 * value defines the maximum allowed length for the 
	 * type field
	 * 
	 * @return the type length
	 */
	int getTypeLength();
	
	/**
	 * Sets the type length of this EventEntry. This 
	 * value defines the maximum allowed length for the 
	 * type field
	 * 
	 * @param typeLength the type length
	 */
	void setTypeLength(int typeLength);
	
	/**
	 * Sets the comment length of this EventEntry. This 
	 * value defines the maximum allowed length for the 
	 * comment field
	 * 
	 * @param commentLength the comment length
	 */
	void setCommentLength(int commentLength);
}
