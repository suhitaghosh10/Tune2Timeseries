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
 * MeasurementEntry is the superclass of SignalEntry and ValuesEntry and defines
 * the common methods. Direct instances of MeasurementEntry are normally not
 * used.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 * 
 */
public interface MeasurementEntry extends TimedEntry
{

	/**
	 * Gets the names of all channels as Array of String
	 * 
	 * @return the names of all channels
	 */
	String[] getChannelNames();

	/**
	 * Gets the resolution in bits of the ADC used to acquire the data contained
	 * in this Entry
	 * 
	 * @return the resolution in bits
	 */
	int getAdcResolution();

	/**
	 * Sets the resolution in bits of the ADC used to acquire the data contained
	 * in this Entry.
	 * 
	 * @param adcResolution
	 *            the resolution in bits
	 */
	void setAdcResolution(int adcResolution);

	/**
	 * Gets the output given by the ADC, when the input falls exactly at the
	 * center of the ADC range. For bipolar ADCs with two's complement output
	 * adcZero is usually zero.
	 * 
	 * adcZero is just an additional information for characterizing the used
	 * ADC. adcZero is not necessary for the calculation of physical values. You
	 * can use this information for the calculation of the ADC's physical range.
	 * 
	 * @return the output given by the ADC, when the input falls exactly at the
	 *         center of the ADC range
	 */
	int getAdcZero();

	/**
	 * Sets the output given by the ADC, when the input falls exactly at the
	 * center of the ADC range. For bipolar ADCs with two's complement output
	 * adcZero is usually zero.
	 * 
	 * @param adcZero
	 *            the output given by the ADC, when the input falls exactly at
	 *            the center of the ADC range
	 */
	void setAdcZero(int adcZero);

	/**
	 * Gets the value of ADC output that would map to 0 physical units input.
	 * This value can be beyond the ADC output range. The value of the physical
	 * variable is calculated by:
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @return the value of ADC output that would map to the value of 0 of the
	 *         physical variable
	 */
	int getBaseline();

	/**
	 * Sets the value of ADC output that would map to 0 physical units input.
	 * This value can be beyond the ADC output range. The value of the physical
	 * variable is calculated by:
	 * 
	 * value = (ADCout - baseline) * lsbValue.
	 * 
	 * @param baseline
	 *            the value of ADC output that would map to the value of 0 of
	 *            the physical variable
	 */
	void setBaseline(int baseline);

	/**
	 * Gets the equivalent value of the physical variable represented by the
	 * least significant bit of the ADC used to acquire the data contained in
	 * this Entry
	 * 
	 * @return the value of the physical variable represented by the least
	 *         significant bit of the ADC
	 */
	double getLsbValue();

	/**
	 * Sets the equivalent value of the physical variable represented by the
	 * least significant bit of the ADC used to acquire the data contained in
	 * this Entry
	 * 
	 * @param lsbValue
	 *            the value of the physical variable represented by the least
	 *            significant bit of the ADC
	 */
	void setLsbValue(double lsbValue);

	/**
	 * Gets the string that specifies the physical unit of the acquired
	 * variable(s)
	 * 
	 * @return the unit
	 */
	String getUnit();

	/**
	 * Sets the string that specifies the physical unit of the acquired
	 * variable(s)
	 * 
	 * @param unit
	 *            the unit
	 */
	void setUnit(String unit);

	/**
	 * Gets the number of channels in this Entry
	 * 
	 * @return the number of Channels
	 */
	int getChannelCount();

	/**
	 * Sets the names of the channels in this Entry. The number of channels
	 * cannot be changed after a channel is created
	 * 
	 * @param channelNames
	 *            the names of the channels as Array of Strings
	 */
	void setChannelNames(String[] channelNames);

	/**
	 * Gets the DataType of the data contained in this Entry
	 * 
	 * @return the DataType
	 */
	DataType getDataType();

	/**
	 * Sets the DataType of the data contained in this Entry
	 * 
	 * @param dataType
	 *            the DataType
	 */
	void setDataType(DataType dataType);

	/**
	 * Sets the properties of the ADC used to acquire the data contained in this
	 * Entry.
	 * 
	 * @param adcZero
	 *            the output given by the ADC, when the input falls exactly at
	 *            the center of the ADC range
	 * @param adcResolution
	 *            the resolution in bits of the ADC
	 * @param baseline
	 *            the value of ADC output that would map to 0 physical units
	 *            input
	 * @param lsbValue
	 *            the equivalent value of the physical variable represented by
	 *            the least significant bit of the ADC
	 *            
	 * @see setAdcZero
	 * @see setAdcResolution
	 * @see setBaseline
	 * @see setLsbValue
	 */
	void setAdcProperties(int adcZero, int adcResolution, int baseline,
			double lsbValue);
}