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
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Unisens is the basic interface to a unisens dataset. It represents its structure. 
 * Each Unisens object is linked to its unisens.xml file, which is contained in every 
 * unisens dataset.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface Unisens {
	
	
	/**
	 * Returns the path of the dataset represented by this Unisens object. The associated 
	 * unisens.xml file is contained in this path.
	 * 
	 * @return the path of this Unisens dataset
	 */
	String getPath();
	
	/**
	 * Gets the comment of this Unisens object. The comment describes the dataset as a whole.
	 * 
	 * @return the comment of this Unisens object
	 */
	String getComment();
	
	/**
	 * Sets the comment of this Unisens object. The comment describes the dataset as a whole.
	 * 
	 * @param comment decription of the whole dataset
	 */
	void setComment(String comment);
	
	/**
	 * Gets the duration of this dataset in seconds. If the dataset contains at least one 
	 * SignalEntry this information is redundant, because the duration can then be 
	 * calculated from the length of the SignalEntry.
	 * 
	 * @return duration of this dataset in seconds. 
	 */
	double getDuration();
	
	
	/**
	 * Sets the duration of this dataset in seconds. If the dataset contains at least one 
	 * SignalEntry this information is not needed, because the duration can then be 
	 * calculated from the length of the SignalEntry.
	 * 
	 * @param duration the duration of this dataset in seconds 
	 * 
	 */
	void setDuration(double duration);	
	
	/**
	 * Sets the duration of this dataset in seconds. If the dataset contains at least one 
	 * SignalEntry this information is not needed, because the duration can then be 
	 * calculated from the length of the SignalEntry.
	 * 
	 * @param duration the duration of this dataset in seconds
	 * @deprecated use double parameter instead of long
	 * @see setDuration(double duration)
	 * 
	 */
	@Deprecated
	void setDuration(long duration);
	
	
	/**
	 * Gets the id of this unisens dataset. The id can be used to identify a dataset 
	 * within a database.
	 * 
	 * @return the Id of this dataset
	 */
	String getMeasurementId();
	
	
	/**
	 * Gets the id of this unisens dataset. The id can be used to identify a dataset within a 
	 * database.
	 * 
	 * @param id the Id of this dataset.
	 */
	void setMeasurementId(String id);
	
	
	/**
	 * Gets the timestamp of the start of the data acquisition 
	 * 
	 * @return the timestamp of the start of the data acquisition
	 */
	Date getTimestampStart();
	
	
	/**
	 * Sets the timestamp of the start of the data acquisition
	 * 
	 * @param timestampStart the timestamp of the start of the data acquisition
	 */
	void setTimestampStart(Date timestampStart);
	
	
	/**
	 * Returns the version of the XML-Schema used in the unisens.xml file 
	 * 
	 * @return the Schema Version of the uisens.xml file
	 */
	String getVersion();
	
	
	/**
	 * Gets the Context of this unisens dataset. The associated information is contained in
	 * the context.xml file. See the documentation for more information.
	 * 
	 * @return the Context of this unisens dataset
	 */
	Context getContext();
	
	/**
	 * Returns a List of all Entries contained in this unisens dataset. The Entries can be 
	 * of the following type:
	 * <ul>
	 * <li>SignalEntry</li>
	 * <li>ValuesEntry</li>
	 * <li>EventEntry</li>
	 * <li>CustomEntry</li>
	 * </ul>
	 * 
	 * @return the List of Entries in this unisens dataset
	 */
	List<Entry> getEntries();
	
	/**
	 * Returns the Entry identified by its id. Within an unisens dataset each entry has a 
	 * unique id.
	 * 
	 * @param id the id of the Entry
	 * @return Entry identified by id
	 */
	Entry getEntry(String id);
	
	/**
	 * Returns a List of alls Groups contained in this unisens dataset. Semantically 
	 * related entries can be grouped in a Group.
	 * 
	 * @return the List of Groupd contained in this unisens dataset.
	 */
	List<Group> getGroups();
	
	/**
	 * Returns the Group identified by its id. Within an unisens dataset each group has 
	 * a unique id.
	 * 
	 * @param id id of the Group.
	 * @return Group identifierd by id
	 */
	Group getGroup(String id);
	
	/**
	 * Removes the context information from this unisens dataset. The corresponding 
	 * context.xml file will not be deleted.
	 */
	void deleteContext();
	
	/**
	 * Deletes a Group from this unisens dataset. The Entries in the group will no 
	 * be deleted.
	 * 
	 * @param group the Group to be deleted
	 */
	void deleteGroup(Group group);
	
	/**
	 * Delete an Entry from this unisens dataset
	 * 
	 * @param entry the Entry to be deleted
	 */
	void deleteEntry(Entry entry);
	
	/**
	 * Saves this unisens dataset. The dataset information is stored in the associated 
	 * unisens.xml file. 
	 * 
	 * @throws IOException
	 */
	void save()	throws IOException;
	
	
	/**
	 * Creates a context information to this uniens dataset. The context information hast 
	 * to be available in the context.xml file. The structure of the context.xml file is 
	 * described by a XML-Schema.
	 * 
	 * @param schemaUrl the URL to the XML schema describing the structure of context.xml
	 * @return the created Context
	 */
	Context createContext(String schemaUrl);
	
	/**
	 * Creates a new Group in this unisens dataset. Within a dataset groups have to have 
	 * a unique id.
	 * 
	 * @param id the id of the Group
	 * @return the created Group
	 * @throws DuplicateIdException 
	 */
	Group createGroup(String id) throws DuplicateIdException;
	
	/**
	 * Creates a new SignalEntry in this unisens dataset. A SignalEntry represents 
	 * continuously sampled data. A file containing the associated data is also created 
	 * if not already present. The filename is the same as the id of the Entry.
	 * 
	 * @param id the id of the Entry. Has to be unique within one unisens dataset
	 * @param channelNames an Array of names of the channels. At least on channel has to be present.
	 * @param dataType the DataType of the data contained in this SignalEntry.
	 * @param sampleRate the sampleRate of the data in samples per second.
	 * @return the created SignalEntry
	 * @throws DuplicateIdException
	 */
	SignalEntry createSignalEntry(String id, String[] channelNames, DataType dataType, double sampleRate)	throws DuplicateIdException ;
	
	
	/**
	 * Creates a new EventEntry in this unisens dataset. An EventEntry represents Events at
	 * certain points in time. Events have no data value. A file containing the associated 
	 * data is also created if not already present. The filename is the same as the id of 
	 * the Entry.
	 * 
	 * @param id the id of the Entry. Has to be unique within one unisens dataset.
	 * @param sampleRate the sampleRate of the data in samples per second. Points if time are 
	 * given as sample counts
	 * @return the created EventEntry
	 * @throws DuplicateIdException
	 */
	EventEntry createEventEntry(String id, double sampleRate)	throws DuplicateIdException ;
	
	/**
	 * Creates a new ValuesEntry in this unisens dataset. A ValuesEntry represents values 
	 * aquired at certain points in time. A file containing the associated data is also 
	 * created if not already present. The filename is the same as the id of the Entry.
	 * 
	 * @param id the id of the Entry. Has to be unique within one unisens dataset.
	 * @param channelNames an Array of names of the channels. At least on channel has to be present.
	 * @param dataType the DataType of the data contained in this SignalEntry.
	 * @param sampleRate the sampleRate of the data in samples per second. Points in time are 
	 * given as sample counts
	 * @return the created ValuesEntry
	 * @throws DuplicateIdException
	 */
	ValuesEntry createValuesEntry(String id, String[] channelNames, DataType dataType, double sampleRate)	throws DuplicateIdException ;

	/**
	 * Creates a new CumstomEntry in this unisens dataset. This method can be used to add 
	 * an Entry to this unsiens dataset that is not specified by the unisens specification.
	 * 
	 * @param id the id of the Entry. Has to be unique within one unisens dataset.
	 * @return the created CustomEntry
	 * @throws DuplicateIdException
	 */
	CustomEntry createCustomEntry(String id) throws DuplicateIdException;
	
	
	/**
	 * Closes all open Entries contained in this unisens dataset
	 */
	void closeAll();
	
	/**
	 * Add an existing Entry to this unisens dataset. This method can be used to copy  
	 * an existing Entry to this unsiens dataset. If deepCopy=true also the 
	 * corresponding data file will be copied.
	 * 
	 * @param entry the entry that should be added.
	 * @param deepCopy flag that indicates to copy the datafile
	 * @return the added Entry 
	 * @throws DuplicateIdException
	 */
	Entry addEntry(Entry entry, boolean deepCopy) throws DuplicateIdException;
	
	/**
	 * Add an existing Group and its Entries to this unisens dataset. This method can 
	 * be used to copy an existing Group and its Entries to this unsiens dataset. If 
	 * deepCopy=true also the corresponding data files will be copied.
	 * 
	 * @param group the group that should be added.
	 * @param deepCopy flag that indicates to also copy the datafiles
	 * @return the added Group 
	 * @throws DuplicateIdException
	 */
	Group addGroup(Group group, boolean deepCopy)throws DuplicateIdException;
	
	/**
	 * Returns the custom attributes of this unisens dataset. Custom attributes can be used
	 * to add simple context information as key/values pairs. For complex context 
	 * information {@link #createContext(String schemaUrl)} should be preferred. 
	 * 
	 * @return all custom attribues as HashMap
	 */
	HashMap<String, String> getCustomAttributes();
	
	/**
	 * Add a ned custom attributes to this unisens dataset. Custom attributes can be used
	 * to add simple context information as key/values pairs. For complex context 
	 * information {@link #createContext(String schemaUrl)} should be preferred. 
	 * 
	 * @param key the key of the new attribute
	 * @param value the value of the new attribute
	 */
	void addCustomAttribute(String key, String value);
}
