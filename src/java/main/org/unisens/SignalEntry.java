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
 * A SignalEntry represents continuously sampled data with a fixed sample rate.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 * 
 */
public interface SignalEntry extends MeasurementEntry
{

	/**
	 * Reads rows of data from a data file beginning at a given position. The
	 * data is returned as object [length][channelCount], where channelCount is
	 * the number of channels in this SignalEntry
	 * 
	 * @param pos
	 *            the position to start from
	 * @param length
	 *            the number of rows to read
	 * @return the data as object [length][channelCount]
	 * @throws IOException
	 */
	Object read(long pos, int length) throws IOException;

	/**
	 * Reads data from a data file beginning at the current position of the file
	 * pointer. The data is returned as object [length][channelCount], where
	 * channelCount is the number of channels in this SignalEntry
	 * 
	 * @param length
	 *            the number of rows to read
	 * @return the data as object [length][channelCount]
	 * @throws IOException
	 */
	Object read(int length) throws IOException;

	/**
	 * Reads data from a data file beginning at a given position. The data is
	 * returned as double [length][channelCount], where channelCount is the
	 * number of channels in this SignalEntry. The data values are scaled
	 * according to baseline and lsbValue.
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @param pos
	 *            the position to start from
	 * @param length
	 *            the number of rows to read
	 * @return the scaled data as double [length][channelCount]
	 * @throws IOException
	 */
	double[][] readScaled(long pos, int length) throws IOException;

	/**
	 * Reads data from a data file beginning at the current position of the file
	 * pointer. The data is returned as double [length][channelCount], where
	 * channelCount is the number of channels in this SignalEntry. The data
	 * values are scaled according to baseline and lsbValue.
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @param length
	 *            the number of rows to read
	 * @return the scaled data as double [length][channelCount]
	 * @throws IOException
	 */
	double[][] readScaled(int length) throws IOException;

	/**
	 * Appends rows of data at the end of this SignalEntry.
	 * 
	 * @param data
	 *            the data to add as object [length][channelCount]
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	void append(Object data) throws IOException, IllegalArgumentException;
}
