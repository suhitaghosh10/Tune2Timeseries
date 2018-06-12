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

import java.io.IOException;

import org.unisens.DataType;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.ri.io.SignalIoFactory;
import org.unisens.ri.io.SignalReader;
import org.unisens.ri.io.SignalWriter;
import org.unisens.ri.util.Utilities;
import org.w3c.dom.Node;


public class SignalEntryImpl extends MeasurementEntryImpl implements SignalEntry{
	private SignalReader signalReader = null;
	private SignalWriter signalWriter = null;

	protected SignalEntryImpl(Unisens unisens , Node entryNode){
		super(unisens , entryNode);
	}
	
	protected SignalEntryImpl(Unisens unisens , String id , String[] channelNames, DataType dataType, double sampleRate){
		super(unisens , id , channelNames, dataType, sampleRate);
		this.fileFormat = new BinFileFormatImpl();
	}
	
	protected SignalEntryImpl(SignalEntry signalEntry){
		super(signalEntry);
	}

	public void resetPos() {
		getReader().resetPos();
	}
	
	public Object read(int length) throws IOException{
		return this.getReader().read(length);
	}
	
	public Object read(long pos , int length) throws IOException{
		return this.getReader().read(pos, length);
	}
	
	public double[][] readScaled(long pos, int length)throws IOException {
		return this.getReader().readScaled(pos, length);
	}
	
	public double[][] readScaled(int length)throws IOException {
		return this.getReader().readScaled(length);
	}

	public void append(Object data) throws IOException , IllegalArgumentException{
		this.getWriter().append(data);
		this.calculateCount(Utilities.getSampleCount(data, dataType));
	}
	
	protected SignalReader getReader(){
		if(signalReader == null)
			signalReader = SignalIoFactory.createSignalReader(this);
		return signalReader;
	}
	
	protected SignalWriter getWriter(){
		if(signalWriter == null)
			signalWriter = SignalIoFactory.createSignalWriter(this);
		return signalWriter;
	}
	
	@Override
	protected boolean isReaderOpened() {
		return signalReader != null;
	}

	@Override
	protected boolean isWriterOpened() {
		return signalWriter != null;
	}
	
	@Override
	public SignalEntry clone() {
		return new SignalEntryImpl(this);
	}
}
