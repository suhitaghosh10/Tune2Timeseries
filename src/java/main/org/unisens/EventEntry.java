/*
Unisens Interface - interface for a universal sensor data format
Copyright 2008-2010 FZI Research Center for Information Technology, Germany
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
import java.util.List;


/**
 * EventEntry represents an Entry containing a list of Events. Events have no
 * signal or value data. Each Event has a timstamp and a type and can have a
 * comment. Type and comment are strings.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 * 
 */
public interface EventEntry extends AnnotationEntry
{
	/**
	 * Appends an Event to this EventEntry
	 * 
	 * @param event
	 *            the Event to append
	 * @throws IOException
	 */
	void append(Event event) throws IOException;

	/**
	 * Reads Events from a data file beginning at a given postion
	 * 
	 * @param position
	 *            the position to start from
	 * @param length
	 *            the number of Events to read
	 * @return a List of Events
	 * @throws IOException
	 */
	List<Event> read(long position, int length) throws IOException;

	/**
	 * Reads Events from a data file beginning at the current position of the
	 * file pointer
	 * 
	 * @param length
	 *            the numer of Events to read
	 * @return a List of Events
	 * @throws IOException
	 */
	List<Event> read(int length) throws IOException;

	/**
	 * Appends a List of Events to this EventEntry
	 * 
	 * @param events
	 * @throws IOException
	 */
	void append(List<Event> events) throws IOException;
}
