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
import java.util.HashMap;


/**
 * An Entry is a structural unit of a unisens dataset. Entry is the base class of the following Entries:
 * 	 
 * <ul>
 * <li>SignalEntry</li>
 * <li>ValuesEntry</li>
 * <li>EventEntry</li>
 * <li>CustomEntry</li>
 * </ul>
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface Entry extends Cloneable{
	
	
	/**
	 * Gets the comment of this Entry.  
	 * 
	 * @return the comment
	 */
	String getComment();
	
	/**
	 * Gets the id of this Entry. The id is unique for all Entries in a unsiens dataset.
	 *  
	 * @return the id of this Entry
	 */
	String getId();
	
	/**
	 * Gets the name of this Entry.
	 *  
	 * @return the name of this Entry
	 */
	String getName();
	
	/**
	 * Gets the source id of this Entry. The source id can be used to describe origin of data contained 
	 * in an Entry. For example a device serial number or MAC adress.
	 * 
	 * @return the source id of this Entry
	 */
	String getSourceId();
	
	/**
	 * Gets the source of this Entry. The source can be used to comment the origin of data contained 
	 * in an Entry. For example the name of the measurement device.
	 * 
	 * @return the source of this Entry
	 */
	String getSource();
	
	/**
	 * Gets an identifier of the content class of the data contained in this Entry. Recommended 
	 * content classes are listed in the unisens documentation.
	 * 
	 * @return the content class of this Entry
	 */
	String getContentClass();
	
	
	/**
	 * Sets  the name of this Entry. 
	 * 
	 * @param name of the entry
	 */
	void setName(String name);
	
	/**
	 * Sets  the content class of the data contained in this Entry. Recommended 
	 * content classes are listed in the unisens documentation.
	 * 
	 * @param theClass the content class
	 */
	void setContentClass(String theClass);
	
	/**
	 * Sets the comment of this Entry.  
	 * 
	 * @param comment the comment
	 */
	void setComment(String comment);
	
	/**
	 * Rename this Entry. Also the data file containing the assiciated data will be renamed.
	 * 
	 * @param newId the new id of this Entry
	 * @throws IOException
	 * @throws DuplicateIdException
	 */
	void rename(String newId)	throws IOException, DuplicateIdException ;
	
	/**
	 * Sets the source id of this Entry. The source id can be used to describe origin of data contained 
	 * in an Entry. For example a device serial number or MAC adress.	 
	 *  
	 * @param sourceId the source id of this Entry
	 */
	void setSourceId(String sourceId);
	
 	/**
 	 * Sets the source of this Entry. The source can be used to comment the origin of data contained 
	 * in an Entry.
	 * 
 	 * @param source the source of this Entry
 	 */
 	void setSource(String source);
 	
 	/**
	 * Returns the custom attributes of this unisens entry. Custom attributes can be used
	 * to add simple context information as key/values pairs.  
	 * 
	 * @return all custom attribues as HashMap
	 */
	HashMap<String, String> getCustomAttributes();
	
	/**
	 * Add a ned custom attributes to this unisens entry. Custom attributes can be used
	 * to add simple context information as key/values pairs.
	 * 
	 * @param key the key of the new attribute
	 * @param value the value of the new attribute
	 */
	void addCustomAttribute(String key, String value);
 	
 	/**
 	 * Gets the file format that is used to represet this Entry in the associated data file
 	 * 
 	 * @return the file format of this Entry
 	 */
 	FileFormat getFileFormat();
 	
 	/**
 	 * Sets the file format that is used to represet this Entry in the associated data file
 	 * 
 	 * @param fileFormat the file format of this Entry
 	 */
 	void setFileFormat(FileFormat fileFormat);
 	
 	/**
 	 * Gets the unisens object which contains this Entry 
 	 * 
 	 * @return the unisend object
 	 */
 	Unisens getUnisens();
 	
 	/**
 	 * Set the unisens object which contains this Entry 
 	 * 
 	 */
 	void setUnisens(Unisens unisens);
 	
 	
 	/**
 	 * Closes the data file associated with this Entry
 	 */
 	void close();
 	
 	Entry clone();
 	
	/**
	 *  
	 * Creates a new BinFileFormat object. 
	 * 
	 * @return new BinFileFormat object
	 * 
	 */
	BinFileFormat createBinFileFormat();

	/**
	 * Creates a new CsvFileFormat object. 
	 * 
	 * @return new CsvFileFormat object
	 */
	CsvFileFormat createCsvFileFormat();

	/**
	 * Creates a new XmlFileFormat object. 
	 * 
	 * @return new XmlFileFormat object
	 */
	XmlFileFormat createXmlFileFormat();

	/**
	 * Creates a new CustomFileFormat object. 
	 * 
	 * @param fileFormatName the name of the CustomFileFormat
	 * @return new CustomFileFormat object
	 */
	CustomFileFormat createCustomFileFormat(String fileFormatName);
 	
}
