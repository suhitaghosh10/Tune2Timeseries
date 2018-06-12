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
 * UnisensFactory is used to create all Objects of a unisens implementation, that have 
 * to be created directly (i.e not by a Unisens object).  
 *  
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface UnisensFactory {
	
	/**
	 * Creates a new Unisens object. If path points to an existing unisens dataset, it is 
	 * loaded. Else new unisens dataset is created.
	 * 
	 * @param path path to existing or new unisens dataset
	 * @return Unisens object
	 */
	Unisens createUnisens(String path) throws UnisensParseException;

	/**
	 *  
	 * Creates a new BinFileFormat object. 
	 * 
	 * @return new BinFileFormat object
	 * 
	 * @see org.unisens.Entry
	 * @deprecated moved to Entry
	 */
	@Deprecated
	BinFileFormat createBinFileFormat();

	/**
	 * Creates a new CsvFileFormat object. 
	 * 
	 * @return new CsvFileFormat object
	 * @see org.unisens.Entry
	 * @deprecated moved to Entry
	 */
	@Deprecated
	CsvFileFormat createCsvFileFormat();

	/**
	 * Creates a new XmlFileFormat object. 
	 * 
	 * @return new XmlFileFormat object
	 * @see org.unisens.Entry
	 * @deprecated moved to Entry
	 */
	@Deprecated
	XmlFileFormat createXmlFileFormat();

	/**
	 * Creates a new CustomFileFormat object. 
	 * 
	 * @param fileFormatName the name of the CustomFileFormat
	 * @return new CustomFileFormat object
	 * @see org.unisens.Entry
	 * @deprecated moved to Entry
	 */
	@Deprecated
	CustomFileFormat createCustomFileFormat(String fileFormatName);
}
