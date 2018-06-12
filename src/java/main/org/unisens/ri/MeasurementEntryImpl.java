/*
Unisens Library - library for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV),
				   KIT, Germany

This file is part of the Unisens Library. For more information, see
<http://www.unisens.org>

The Unisens Library is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Library. If not, see <http://www.gnu.org/licenses/>. 
*/

package org.unisens.ri;

import java.util.Vector;

import org.unisens.DataType;
import org.unisens.MeasurementEntry;
import org.unisens.Unisens;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class MeasurementEntryImpl extends TimedEntryImpl implements MeasurementEntry {
	protected int adcResolution = 0;
	protected int adcZero = 0;
	protected int baseline = 0;
	protected String[] channelNames = null;
	protected DataType dataType = null;
	protected double lsbValue = 0;
	protected String unit = null;
	
	protected MeasurementEntryImpl(Unisens unisens , Node entryNode) {
		super(unisens , entryNode);
		this.parse(entryNode);
	}
	
	public MeasurementEntryImpl(Unisens unisens , String id , String[] channelNames, DataType dataType, double sampleRate) {
		super(unisens , id , sampleRate);
		this.channelNames = channelNames;
		this.dataType = dataType;
	}
	
	protected MeasurementEntryImpl(MeasurementEntry measurementEntry){
		super(measurementEntry);
		this.adcResolution = measurementEntry.getAdcResolution();
		this.adcZero = measurementEntry.getAdcZero();
		this.baseline = measurementEntry.getBaseline();
		this.channelNames = measurementEntry.getChannelNames().clone();
		this.dataType = measurementEntry.getDataType();
		this.lsbValue = measurementEntry.getLsbValue();
		this.unit = measurementEntry.getUnit();
	}
	
	
	private void parse(Node measurementNode){
		NamedNodeMap attrs = measurementNode.getAttributes();
		Node attrNode = attrs.getNamedItem(MEASUREMENTENTRY_ADCRESOLUTION);
		adcResolution = (attrNode != null) ? Integer.parseInt(attrNode.getNodeValue()): 0;
		attrNode = attrs.getNamedItem(MEASUREMENTENTRY_ADCZERO);
		adcZero = (attrNode != null) ? Integer.parseInt(attrNode.getNodeValue()): 0;
		attrNode = attrs.getNamedItem(MEASUREMENTENTRY_BASELINE);
		baseline = (attrNode != null) ? Integer.parseInt(attrNode.getNodeValue()): 0;
		attrNode = attrs.getNamedItem(MEASUREMENTENTRY_LSBVALUE);
		lsbValue = (attrNode != null) ? Double.parseDouble(attrNode.getNodeValue()): 0;
		
	    attrNode = attrs.getNamedItem(MEASUREMENTENTRY_DATATYPE);
		dataType = (attrNode != null) ? DataType.fromValue(attrNode.getNodeValue()) : null;
		attrNode = attrs.getNamedItem(MEASUREMENTENTRY_UNIT);
		unit = (attrNode != null) ? attrNode.getNodeValue() : null;
		
		NodeList childNodes = measurementNode.getChildNodes();
		Node childNode = null;
		Vector<String> channelNames = new Vector<String>();
		for(int i = 0 ; i < childNodes.getLength() ; i++){
			childNode = childNodes.item(i);
			if((childNode.getNodeType() == Node.ELEMENT_NODE) && childNode.getNodeName().equalsIgnoreCase(CHANNEL))
				channelNames.add(childNode.getAttributes().getNamedItem(CHANNEL_NAME).getNodeValue());
		}

		Object[] o = channelNames.toArray();
		this.channelNames = new String[o.length];
		for(int i = 0 ; i < o.length ; i++)
			this.channelNames[i] = (String)o[i];

		
		return;
	}

	public int getAdcResolution() {
		return this.adcResolution;
	}

	public int getAdcZero() {
		return adcZero;
	}

	public int getBaseline() {
		return baseline;
	}

	public int getChannelCount() {
		return channelNames.length;
	}

	public String[] getChannelNames() {
		return channelNames;
	}

	public DataType getDataType() {
		return dataType;
	}

	public double getLsbValue() {
		if (lsbValue == 0)
		{
			// returns 1.0 when no LSB value is defined in the XML file
			return 1.0;
		}
		return lsbValue;
	}

	public String getUnit() {
		return this.unit;
	}

	public abstract void resetPos();
	

	public void setAdcResolution(int adcResolution) {
		this.adcResolution = adcResolution;
	}

	public void setAdcZero(int adcZero) {
		this.adcZero = adcZero;
	}

	public void setBaseline(int baseline) {
		this.baseline = baseline;
	}

	public void setChannelNames(String[] channelNames) {
		this.channelNames = channelNames;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setLsbValue(double lsbValue){
		this.lsbValue = lsbValue;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setAdcProperties(int adcZero, int adcResolution, int baseline, double lsbValue) {
		this.adcZero = adcZero;
		this.adcResolution = adcResolution;
		this.baseline = baseline;
		this.lsbValue = lsbValue;
	}
}
