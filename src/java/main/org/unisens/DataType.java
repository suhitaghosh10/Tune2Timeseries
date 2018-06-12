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
 * Enumeration of all data types that can be used in the unsiens file format. See 
 * unisens documentation for detailed information.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst  
 */
public enum DataType {
	INT8("int8"),
	UINT8("uint8"),
	INT16("int16"),
    UINT16("uint16"),
    INT32("int32"),
    UINT32("uint32"),
    FLOAT("float"),
    DOUBLE("double");
 
    private final String value;

    /**
     * Private constructor
     * 
     * @param value string representation for a DataType enum object
     */
    DataType(String value) {
        this.value = value;
    }

    /** 
     * This method gives the representation of a datatype enum object as string.
     * 
     * @return correspoding string representation for a DataType enum object
     */
    public String value() {
        return value;
    }

    /** 
     * This method gives a DataType enum object that correspod to a given datatype name 
     * 
     * @param name the name of a datatype. See unisens documentation for a list of supported data types
     * @return DataType enum object, that correspod to that datatype name 
     * @throws IllegalArgumentException falls the datatype is not supported or the datatype name is not right written 
     */
    public static DataType fromValue(String name) {
        for (DataType dataType: DataType.values()) {
            if (dataType.value.equals(name)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException(name.toString());
    }

}