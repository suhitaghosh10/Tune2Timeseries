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

package org.unisens.ri.io.bin;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import org.unisens.BinFileFormat;
import org.unisens.DataType;
import org.unisens.Endianess;
import org.unisens.SignalEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.SignalWriter;
import org.unisens.ri.util.Utilities;

public class SignalBinWriter extends SignalWriter {
	private BufferedFileWriter file = null;
	
	public SignalBinWriter(SignalEntry signalEntry) throws IOException{
		super(signalEntry);
		open();
	}
	public void open() throws IOException{
		if(!isOpened){
			this.file = new BufferedFileWriter(new File(absoluteFileName));
			this.isOpened = true;
			this.file.setSigned(! dataType.value().startsWith("u"));
			if(((BinFileFormat)this.signalEntry.getFileFormat()).getEndianess() == Endianess.BIG)
				this.file.setEndian(ByteOrder.BIG_ENDIAN);
			else
				this.file.setEndian(ByteOrder.LITTLE_ENDIAN);
		}
	}
	
	public void close() throws IOException{
		if(this.isOpened)
			file.close();
		this.isOpened = false;
		
	}
	
	public void append(Object data) throws IOException{
		open();
		
		data = Utilities.convertFrom1DimTo2DimArray(data, channelCount);
		
		if((dataType == DataType.INT8)){
			append8((byte[][])data);
		}
		else if((dataType == DataType.UINT8)){
			if(data instanceof byte[][])
				append8(Utilities.convertUnsignedByteToShort((byte[][])data));
			else
				append8((short[][])data);
		}
		else if((dataType == DataType.INT16)){
			append16((short[][])data);
		}
		else if((dataType == DataType.UINT16)){
			if(data instanceof short[][])
				append16(Utilities.convertUnsignedShortToInteger((short[][])data));
			else
				append16((int[][])data);
		}
		else if((dataType == DataType.INT32)){
			append32((int[][])data);
		}
		else if((dataType == DataType.UINT32)){
			if(data instanceof int[][])
				append32(Utilities.convertUnsignedIntegerToLong((int[][])data));
			else
				append32((long[][])data);
		}
		else if((dataType == DataType.FLOAT)){
			appendFloat((float[][])data);
		}
		else if((dataType == DataType.DOUBLE)){
			appendDouble((double[][])data);
		}
		file.flush();
	}
	
	private void append8(byte[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.write8(data[i][j]);
			}
		}
	}
	
	private void append8(short[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.write8(data[i][j]);
			}
		}
	}
	
	private void append16(short[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.write16(data[i][j]);
			}
		}
	}
	
	private void append16( int[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.write16(data[i][j]);
			}
		}
	}
	private void append32(int[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
					file.write32(data[i][j]);
			}
		}
	}
	
	private void append32(long[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.write32(data[i][j]);
			}
		}
	}
	private void appendFloat(float[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.writeFloat(data[i][j]);
			}
		}
	}
	private void appendDouble(double[][] data)throws IOException{
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[0].length ; j ++){
				file.writeDouble(data[i][j]);
			}
		}
	}
}
