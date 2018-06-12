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

/**
 * A ValuesEntry represents data acquired at certain points in time. Points in
 * time are determined by sample number using a specific sampleRate.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 * 
 */

public interface ValuesEntry extends MeasurementEntry
{

	/**
	 * Reads from the data file beginning at a given postion and presents the
	 * data as Array of Value
	 * 
	 * @param pos
	 *            the position/row to start from
	 * @param length
	 *            number of rows to read
	 * @return an Array of Value objects
	 * @throws IOException
	 */
	Value[] read(long pos, int length) throws IOException;

	/**
	 * Reads from the data file beginning at the current postion of the file
	 * pointer and presents the data as Array of Value
	 * 
	 * @param length
	 *            number of rows to read
	 * @return an Array of Value objects
	 * @throws IOException
	 */
	Value[] read(int length) throws IOException;

	Value[] readScaled(long pos, int length) throws IOException;

	Value[] readScaled(int length) throws IOException;

	/**
	 * Appends a Value object to this ValueEntry.
	 * 
	 * @param data
	 *            the Value to append
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	void append(Value data) throws IllegalArgumentException, IOException;


	/**
	 * Appends an Array of Values object to this ValueEntry
	 * 
	 * @param data
	 *            the Values to append
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	void append(Value[] data) throws IllegalArgumentException, IOException;

	/**
	 * Appends the Values contained in a ValuesList object to this ValueEntry
	 * 
	 * @param valueList
	 *            the ValuesList to append
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	void appendValuesList(ValueList valueList) throws IOException,
			IllegalArgumentException;

	/**
	 * Reads from the data file beginning at the current position of the file
	 * pointer and presents the data as a ValuesList object. 
	 * 
	 * @param length
	 *            number of rows to read
	 * @return a ValuesList
	 * @throws IOException
	 */
	ValueList readValuesList(int length) throws IOException;

	/**
	 * Reads from the data file beginning at a given position and presents the
	 * data as a ValuesList object. 
	 * 
	 * @param pos
	 *            the position/row to start from
	 * @param length
	 *            number of rows to read
	 * @return a ValuesList
	 * @throws IOException
	 */
	ValueList readValuesList(long pos, int length) throws IOException;

	/**
	 * Reads from the data file beginning at the current position of the file
	 * pointer and presents the data as a ValuesList object. The data values are
	 * scaled according to baseline and lsbValue.
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @param length
	 *            number of rows to read
	 * @return a ValuesList
	 * @throws IOException
	 */
	ValueList readValuesListScaled(int length) throws IOException;

	/**
	 * Reads from the data file beginning at a given position and presents the
	 * data as a ValuesList object. The data values are scaled according to
	 * baseline and lsbValue.
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @param pos
	 *            the position/row to start from
	 * @param length
	 *            number of rows to read
	 * @return a ValuesList
	 * @throws IOException
	 */
	ValueList readValuesListScaled(long pos, int length) throws IOException;
}
