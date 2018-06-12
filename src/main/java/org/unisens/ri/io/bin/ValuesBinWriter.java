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
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.ValuesWriter;
import org.unisens.ri.util.Utilities;

public class ValuesBinWriter extends ValuesWriter {
	private BufferedFileWriter file = null;
	
	public ValuesBinWriter(ValuesEntry valuesEntry) throws IOException{
		super(valuesEntry);
		this.valuesEntry = valuesEntry;
		this.open();
	}
	
	public void open() throws IOException{
		if(!isOpened){
			this.file = new BufferedFileWriter(new File(absoluteFileName));
			this.isOpened = true;
			this.file.setSigned(! dataType.value().startsWith("u"));
			if(((BinFileFormat)this.valuesEntry.getFileFormat()).getEndianess() == Endianess.BIG)
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

	public void append(Value value) throws IOException {
		open();
		file.write64(value.getSampleStamp());
		Object data = value.getData();
		
		if(dataType == DataType.INT8 ){
			append8((byte[])data);
		}
		if(dataType == DataType.UINT8 ){
			append8((short[])data);
		}
		
		if(dataType == DataType.INT16){
			append16((short[])data);
		}
		if(dataType == DataType.UINT16){
			append16((int[])data);
		}
		if(dataType == DataType.INT32){
			append32((int[])data);
		}
		if(dataType == DataType.UINT32){
			append32((long[])data);
		}
		
		file.flush();
		
	}

	public void append(Value[] values) throws IOException {
		open();
		Value value;
		if(dataType == DataType.INT8){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append8((byte[])value.getData());
			}
			return;
		}
		
		if(dataType == DataType.UINT8){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append8((short[])value.getData());
			}
		}
		if(dataType == DataType.INT16){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append16((short[])value.getData());
			}
		}
		if(dataType == DataType.UINT16){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append16((int[])value.getData());
			}
		}
		if(dataType == DataType.INT32){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append32((int[])value.getData());	
			}
		}
		if(dataType == DataType.UINT32){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				append32((long[])value.getData());
			}
		}
		if(dataType == DataType.FLOAT){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				appendFloat((float[])value.getData());
			}
		}
		if(dataType == DataType.DOUBLE){
			for(int i = 0 ; i < values.length ; i++){
				value = values[i];
				file.write64(value.getSampleStamp());
				appendDouble((double[])value.getData());
			}
		}
		
		file.flush();
	}

	public void appendValuesList(ValueList valueList) throws IOException {
		open();
		long[] timestamps = valueList.getSamplestamps();
		Object data = valueList.getData();
		data = Utilities.convertFrom1DimTo2DimArray(data, channelCount);
		if(dataType == DataType.INT8){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append8(((byte[][])data)[i]);
			}
		}
		if(dataType == DataType.UINT8 ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append8(((short[][])data)[i]);
			}
		}
		if(dataType == DataType.INT16 ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append8(((short[][])data)[i]);
			}
		}
		if(dataType == DataType.UINT16 ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append16(((int[][])data)[i]);
			}
		}
		if(dataType == DataType.INT32 ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append16(((int[][])data)[i]);
			}
		}
		if(dataType == DataType.UINT32 ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				append32(((long[][])data)[i]);
			}
		}
		if(dataType == DataType.FLOAT ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				appendFloat(((float[][])data)[i]);
			}
		}
		if(dataType == DataType.DOUBLE ){
			for(int i = 0 ; i < timestamps.length ; i++){
				file.write64(timestamps[i]);
				appendDouble(((double[][])data)[i]);
			}
		}
		
		file.flush();
	}
	private void append8(byte[] data) throws IOException{
		file.writeBytes(data);
	}
	
	private void append8(short[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.write8(data[i]);
	}
	private void append16(short[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.write16(data[i]);
	}
	private void append16(int[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.write16(data[i]);
	}
	private void append32(int[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.write32(data[i]);
	}
	private void append32(long[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.write32(data[i]);
	}
	private void appendFloat(float[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.writeFloat(data[i]);
	}
	private void appendDouble(double[] data) throws IOException{
		for(int i = 0; i < data.length ; i++)
			file.writeDouble(data[i]);
	}
}
