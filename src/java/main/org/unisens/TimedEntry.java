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
 * TimedEntry is the superclass of MeasurementEntry and EventEntry and defines 
 * their common methods.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public interface TimedEntry extends Entry {
	
	/**
	 * Sets the sample rate of this Entry in samples per second.
	 * 
	 * @param sampleRate the sample rate
	 */
	void setSampleRate(double sampleRate);
	
	/**
	 * Gets the sample rate of this Entry in samples per second.
	 * 
	 * @return the sample rate
	 */
	double getSampleRate();
	
	/**
	 * Return the number of items or rows in this Entry.
	 * 
	 * @return the number of items or rows in this Entry
	 */
	long getCount();
	

	/**
	 * Reset the file pointer of this Entry. The next read will read from the
	 * beginning of the data file
	 */
	void resetPos();
}
