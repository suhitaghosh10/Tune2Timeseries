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

import java.util.HashMap;


/**
 * CustomEntry can be used to describe a data file in a unisens dataset that is not 
 * specified in the unisens specification. Attributes can be used. These are stored 
 * and retreived from the unisens.xml file. THIS FEATURE IS STILL EXPERIMENTAL AND
 * MAY CHANGE IN FUTURE.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface CustomEntry extends Entry {
	
	/**
	 * Gets the value of an attribute by its name
	 * 
	 * @param attributeName the name of the attribute
	 * @return the value of the attribute
	 */
	String getAttribute(String attributeName);
	
	/**
	 * Sets a new attriubte and its values
	 * 
	 * @param attributeName the name of the attribute
	 * @param attributeValue the value of the attriute
	 */
	void setAttribute(String attributeName, String attributeValue);
	
	/**
	 * Gets all attributes as a HashMap
	 * 
	 * @return all attributes
	 */
	HashMap<String, String> getAttributes();
}
