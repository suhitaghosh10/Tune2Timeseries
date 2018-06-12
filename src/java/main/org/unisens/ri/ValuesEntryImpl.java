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

import java.io.File;
import java.io.IOException;

import org.unisens.DataType;
import org.unisens.Unisens;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.io.ValuesIoFactory;
import org.unisens.ri.io.ValuesReader;
import org.unisens.ri.io.ValuesWriter;
import org.w3c.dom.Node;

public class ValuesEntryImpl extends MeasurementEntryImpl implements ValuesEntry{
	private ValuesReader valuesReader = null;
	private ValuesWriter valuesWriter = null;
	
	protected ValuesEntryImpl(Unisens unisens , Node entryNode){
		super(unisens , entryNode);
	}
	
	protected ValuesEntryImpl(Unisens unisens , String id , String[] channelNames, DataType dataType, double sampleRate){
		super(unisens ,id , channelNames , dataType , sampleRate);
		this.fileFormat = new CsvFileFormatImpl();
		try {
			File file = new File(this.unisens.getPath() + File.separator + this.getId());
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected ValuesEntryImpl(ValuesEntry valuesEntry){
		super(valuesEntry);
	}
	
	public void resetPos() {
		getReader().resetPos();
	}
	
	public void append(Value data) throws IllegalArgumentException, IOException {
		this.getWriter().append(data);
		this.calculateCount(1);
	}

	public void append(Value[] data) throws IllegalArgumentException, IOException {
		this.getWriter().append(data);
		this.calculateCount(data.length);
	}

	public void appendValuesList(ValueList valueList) throws IOException, IllegalArgumentException {
		this.getWriter().appendValuesList(valueList);
		this.calculateCount(valueList.getSamplestamps().length);
	}


	public Value[] read(int length) throws IOException {
		return this.getReader().read(length);
	}

	public Value[] read(long pos, int length) throws IOException {
		return this.getReader().read(pos , length);
	}
	
	public Value[] readScaled(int length) throws IOException {
		return this.getReader().readScaled(length);
	}

	public Value[] readScaled(long pos, int length) throws IOException {
		return this.getReader().readScaled(pos , length);
	}

	public ValueList readValuesList(int length) throws IOException {
		return this.getReader().readValuesList(length);
	}

	public ValueList readValuesList(long pos, int length) throws IOException {
		return this.getReader().readValuesList(pos , length);
	}

	public ValueList readValuesListScaled(int length) throws IOException {
		return this.getReader().readValuesListScaled(length);
	}

	public ValueList readValuesListScaled(long pos, int length) throws IOException {
		return this.getReader().readValuesListScaled(pos , length);
	}
	
	protected ValuesReader getReader(){
		if(valuesReader == null)
			valuesReader = ValuesIoFactory.createValuesReader(this);
		return valuesReader;
	}
	
	protected ValuesWriter getWriter(){
		if(valuesWriter == null)
			valuesWriter = ValuesIoFactory.createValuesWriter(this);
		return valuesWriter;
	}
	
	@Override
	protected boolean isReaderOpened() {
		return valuesReader != null;
	}

	@Override
	protected boolean isWriterOpened() {
		return valuesWriter != null;
	}
	@Override
	public ValuesEntry clone() {
		return new ValuesEntryImpl(this);
	}
}
