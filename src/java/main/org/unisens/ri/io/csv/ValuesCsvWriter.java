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

package org.unisens.ri.io.csv;

import java.io.File;
import java.io.IOException;

import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.ValuesWriter;
import org.unisens.ri.util.Utilities;

public class ValuesCsvWriter extends ValuesWriter {
	private BufferedFileWriter file = null;
	private static String NEWLINE = System.getProperty("line.separator");
	private String separator = null;
	private String decimalSeparator = null;
	
	public ValuesCsvWriter(ValuesEntry valuesEntry) throws IOException{
		super(valuesEntry);
		this.open();
	}
	
	public void open() throws IOException{
		if(!isOpened){
			file = new BufferedFileWriter(new File(this.absoluteFileName));
			this.isOpened = true;
			this.separator = ((CsvFileFormat)valuesEntry.getFileFormat()).getSeparator();
			this.decimalSeparator = ((CsvFileFormat)valuesEntry.getFileFormat()).getDecimalSeparator();
		}
	}
	
	public void close()throws IOException{
		if(this.isOpened)
			file.close();
		this.isOpened = false;
	}

	public void append(Value value) throws IllegalArgumentException, IOException {
		open();
		file.writeString(""+value.getSampleStamp() + this.separator);
		if((dataType == DataType.INT16)|| (dataType == DataType.UINT8)){
			appendShort((short[]) value.getData());
		}
		if((dataType == DataType.INT32) || (dataType == DataType.UINT16)){
			appendInt((int [])value.getData());
		}
		if(dataType == DataType.UINT32){
			appendLong((long[])value.getData());
		}
		if(dataType == DataType.FLOAT){
			appendFloat((float[])value.getData());
		}
		if(dataType == DataType.DOUBLE){
			appendDouble((double[])value.getData());
		}
		file.flush();
	}

	public void append(Value[] values) throws IOException {
		open();
		if((dataType == DataType.INT16)|| (dataType == DataType.UINT8)){
			for(int i = 0 ; i < values.length ; i++ ){
				file.writeString(""+values[i].getSampleStamp() + this.separator);
				appendShort((short[]) values[i].getData());
			}
		}
		if((dataType == DataType.INT32) || (dataType == DataType.UINT16)){
			for(int i = 0 ; i < values.length ; i++ ){
				file.writeString(""+values[i].getSampleStamp() + this.separator);
				appendInt((int[]) values[i].getData());
			}
		}
		if(dataType == DataType.UINT32){
			for(int i = 0 ; i < values.length ; i++ ){
				file.writeString(""+values[i].getSampleStamp() + this.separator);
				appendLong((long[]) values[i].getData());
			}
		}
		if(dataType == DataType.FLOAT){
			for(int i = 0 ; i < values.length ; i++ ){
				file.writeString(""+values[i].getSampleStamp() + this.separator);
				appendFloat((float[]) values[i].getData());
			}
			return;
		}
		if(dataType == DataType.DOUBLE){
			for(int i = 0 ; i < values.length ; i++ ){
				file.writeString(""+values[i].getSampleStamp() + this.separator);
				appendDouble((double[]) values[i].getData());
			}
		}
		file.flush();
	}

	public void appendValuesList(ValueList valueList) throws IOException, IllegalArgumentException {
		open();
		long[] timestamps = valueList.getSamplestamps();
		Object data = valueList.getData();
		data = Utilities.convertFrom1DimTo2DimArray(data, channelCount);
		if((dataType == DataType.INT16)|| (dataType == DataType.UINT8)){
			for(int i = 0 ; i < timestamps.length ; i++ ){
				file.writeString(""+timestamps[i] + this.separator);
				appendShort(((short[][])data)[i]);
			}
		}
		if((dataType == DataType.INT32) || (dataType == DataType.UINT16)){
			for(int i = 0 ; i < timestamps.length ; i++ ){
				file.writeString(""+timestamps[i] + this.separator);
				appendInt(((int[][])data)[i]);
			}
		}
		if(dataType == DataType.UINT32){
			for(int i = 0 ; i < timestamps.length ; i++ ){
				file.writeString(""+timestamps[i] + this.separator);
				appendLong(((long[][])data)[i]);
			}
		}
		if(dataType == DataType.FLOAT){
			for(int i = 0 ; i < timestamps.length ; i++ ){
				file.writeString(""+timestamps[i] + this.separator);
				appendFloat(((float[][])data)[i]);
			}
		}
		if(dataType == DataType.DOUBLE){
			for(int i = 0 ; i < timestamps.length ; i++ ){
				file.writeString(""+timestamps[i] + this.separator);
				appendDouble(((double[][])data)[i]);
			}
		}
		file.flush();
	}
	
	private void appendShort(short[] data)throws IOException{
		String line = "";
		for(int i = 0 ; i < data.length - 1 ; i++)
			line +=  data[i] + this.separator;
		file.writeString( line + data[data.length - 1] + NEWLINE);
	}
	
	private void appendInt(int[] data)throws IOException{
		String line = "";
		for(int i = 0 ; i < data.length - 1 ; i++)
			line +=  data[i] + this.separator;
		file.writeString( line + data[data.length - 1] + NEWLINE);
	}
	
	private void appendLong(long[] data)throws IOException{
		String line = "";
		for(int i = 0 ; i < data.length - 1 ; i++)
			line +=  data[i] + this.separator;
		file.writeString( line + data[data.length - 1] + NEWLINE);
	}
	
	private void appendFloat(float[] data)throws IOException{
		String line = "";
		String dataString = null;
		for(int i = 0 ; i < data.length ; i++)
		{
			dataString = "" + data[i]; 
			if (!decimalSeparator.equalsIgnoreCase("."))
			{
				dataString = dataString.replace('.', decimalSeparator.charAt(0));
			}
			line +=  dataString;
			if (i<(data.length-1))
			{
				line += this.separator;
			}
		}
		file.writeString( line + NEWLINE);
	}
	
	private void appendDouble(double[] data)throws IOException{
		String line = "";
		String dataString = null;
		for(int i = 0 ; i < data.length ; i++)
		{
			dataString = "" + data[i]; 
			if (!decimalSeparator.equalsIgnoreCase("."))
			{
				dataString = dataString.replace('.', decimalSeparator.charAt(0));
			}
			line +=  dataString;
			if (i<(data.length-1))
			{
				line += this.separator;
			}
		}
		file.writeString( line + NEWLINE);
	}

}
