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
 * Enumeration to describe the Endianess of a binary file format.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public enum Endianess {
    LITTLE,
    BIG;
    
	/**
	 * Gets the value ???
	 * 
	 * @return the value of the attribute
	 */
    public String value() {
        return name();
    }

    /** 
     * This method gives a Endianess enum object that correspod to a given endianess name. 
     *  
	 * @param name endianess name as String
	 * @return Endianess enum object, that correspod to that endianess name 
	 */
    public static Endianess fromValue(String name) {
        return valueOf(name);
    }
}
