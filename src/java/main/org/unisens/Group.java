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

import java.util.List;


/**
 * A Group can be used to semantically group related Entries. A unisens dataset can have multiple Groups.
 * Within a dataset groups have to have a unique id.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface Group {
	
	/**
	 * Sets this comment for this Group
	 * 
	 * @param comment the comment
	 */
	void setComment(String comment);
	
	/**
	 * Gets the comment for this Group
	 * 
	 * @return the comment
	 */
	String getComment();
	
	/**
	 * Gets the id of this group. Within a dataset groups have to have a unique id.
	 * 
	 * @return the id of this Group
	 */
	String getId();
	
	/**
	 * Sets the id of this group. Within a dataset groups have to have a unique id.
	 * 
	 * @param id the new id of this Group
	 */
	void setId(String id);
	
	/**
	 * Gets all Entries of this Group.
	 * 
	 * @return the List of all Entries in this Group
	 */
	List<Entry> getEntries();
	
	/**
	 * Adds an Entry to this Group.
	 * 
	 * @param entry the Entry to be added
	 */
	void addEntry(Entry entry);
	
	/**
	 * Removes an Entry from this Group
	 * 
	 * @param entry the Entry to be removed
	 */
	void removeEntry(Entry entry);
}
