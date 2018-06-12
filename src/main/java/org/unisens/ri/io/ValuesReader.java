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

package org.unisens.ri.io;

import java.io.IOException;

import org.unisens.DataType;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;


public abstract class ValuesReader extends AbstractReader{
	protected ValuesEntry valuesEntry = null;
	protected DataType dataType;
	protected int channelCount = 0;
	protected double lsbValue;
	protected int baseline;

	public ValuesReader(ValuesEntry valuesEntry){
		super(valuesEntry);
		this.valuesEntry = valuesEntry;
		this.dataType = valuesEntry.getDataType();
		this.channelCount = valuesEntry.getChannelCount();
		this.lsbValue = valuesEntry.getLsbValue();
		this.baseline = valuesEntry.getBaseline();
	}
	public abstract Value[] read(int length) throws IOException;
	public abstract Value[] read(long pos , int length) throws IOException;
	public abstract Value[] readScaled(int length) throws IOException;
	public abstract Value[] readScaled(long pos , int length) throws IOException;
	public abstract ValueList readValuesList(int length) throws IOException;
	public abstract ValueList readValuesList(long pos , int length) throws IOException;
	public abstract ValueList readValuesListScaled(int length) throws IOException;
	public abstract ValueList readValuesListScaled(long pos , int length) throws IOException;
	public abstract void resetPos();
}
