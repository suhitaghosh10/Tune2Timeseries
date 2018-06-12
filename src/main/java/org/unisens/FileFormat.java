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
 * FileFormat is used to define which representation of Entry data is used 
 * as file format. 
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface FileFormat extends Cloneable {
	
	/**
	 * Gets the comment for this file format. 
	 * 
	 * @return the comment for this file format
	 */
	String getComment();
	
	/**
	 * Gets the name of this file format as String.
	 * 
	 * @return the name of this file format
	 */
	String getFileFormatName();
	
	/**
	 * Sets the comment for this file format.
	 * 
	 * @param comment the comment for this file format
	 */
	void setComment(String comment);
	
	FileFormat clone();
}