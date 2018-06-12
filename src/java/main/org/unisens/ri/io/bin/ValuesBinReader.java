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
import org.unisens.ri.io.BufferedFileReader;
import org.unisens.ri.io.ValuesReader;

public class ValuesBinReader extends ValuesReader {
	private BufferedFileReader file = null;
	private int bytePerSample = 2;
	
	public ValuesBinReader(ValuesEntry valuesEntry) throws IOException{
		super(valuesEntry);
		this.open();
	}
	
	public void open() throws IOException{
		if(!isOpened){
			this.file = new BufferedFileReader(new File(absoluteFileName));
			this.file.setSigned(!dataType.value().startsWith("u"));
			this.isOpened = true;
			
			if(((BinFileFormat)this.valuesEntry.getFileFormat()).getEndianess() == Endianess.BIG)
				this.file.setEndian(ByteOrder.BIG_ENDIAN);
			else
				this.file.setEndian(ByteOrder.LITTLE_ENDIAN);
			
			if(dataType == DataType.INT8||dataType == DataType.UINT8){
				this.bytePerSample = 1 * channelCount + 8;
				return;
			}
			if(dataType == DataType.INT16||dataType == DataType.UINT16){
				this.bytePerSample = 2 * channelCount + 8;
				return;
			}
			if(dataType == DataType.INT32||dataType == DataType.UINT32||dataType == DataType.FLOAT){
				this.bytePerSample = 4 * channelCount + 8;
				return;
			}
			if(dataType == DataType.DOUBLE){
				this.bytePerSample = 8 * channelCount + 8;
			}
		}
	}
	
	public void close() throws IOException{
		if(isOpened)
			file.close();
		this.isOpened = false;
	}
	public void resetPos(){
		try{
			open();
			file.seek(0);
			currentSample = 0;
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public long getSampleCount(){
		try{
			open();
			return file.length()/(bytePerSample) ;
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return -1;
	}


	public Value[] read(int length) throws IOException {
		return this.read(currentSample , length, false);
	}

	@Override
	public Value[] read(long pos, int length) throws IOException {
		return this.read(pos , length, false);
	}

	@Override
	public Value[] readScaled(int length) throws IOException {
		return this.read(currentSample , length, true);
	}

	@Override
	public Value[] readScaled(long pos, int length) throws IOException {
		return this.read(pos , length, true);
	}

	private Value[] read(long pos, int length, boolean scaled) throws IOException {
		open();
		if(pos != currentSample){
			if(pos > valuesEntry.getCount())
				return null;
			if(pos < 0)
				pos = 0;
			currentSample = pos;
			file.seek(currentSample*bytePerSample);
		}
		
		if(currentSample + length > valuesEntry.getCount())
			length = (int)(valuesEntry.getCount() - currentSample);
		
		if(scaled){
			if((dataType == DataType.INT8))
				return readByte8AsArrayScaled(length);
			if((dataType == DataType.UINT8))
				return readShort8AsArrayScaled(length);
			if((dataType == DataType.INT16))
				return readShort16AsArrayScaled(length);
			if((dataType == DataType.UINT16))
				return readInt16AsArrayScaled(length);
			if((dataType == DataType.INT32))
				return readInt32AsArrayScaled(length);
			if((dataType == DataType.UINT32))
				return readLong32AsArrayScaled(length);
			if((dataType == DataType.FLOAT))
				return readFloatAsArrayScaled(length);
			if((dataType == DataType.DOUBLE))
				return readDoubleAsArrayScaled(length);
		}else{
			if((dataType == DataType.INT8))
				return readByte8AsArray(length);
			if((dataType == DataType.UINT8))
				return readShort8AsArray(length);
			if((dataType == DataType.INT16))
				return readShort16AsArray(length);
			if((dataType == DataType.UINT16))
				return readInt16AsArray(length);
			if((dataType == DataType.INT32))
				return readInt32AsArray(length);
			if((dataType == DataType.UINT32))
				return readLong32AsArray(length);
			if((dataType == DataType.FLOAT))
				return readFloatAsArray(length);
			if((dataType == DataType.DOUBLE))
				return readDoubleAsArray(length);
		}
		return null;
	}
	
	@Override
	public ValueList readValuesListScaled(int length) throws IOException {
		return readValuesList(currentSample, length, true);
	}

	@Override
	public ValueList readValuesListScaled(long pos, int length) throws IOException {
		return readValuesList(pos, length, true);
	}

	public ValueList readValuesList(int length) throws IOException {
		return readValuesList(currentSample, length, false);
	}
	
	public ValueList readValuesList(long pos, int length) throws IOException{
		return readValuesList(pos, length, false);
	}
	
	private ValueList readValuesList(long pos, int length, boolean scaled) throws IOException {
		open();
		if(pos != currentSample){
			if(pos > valuesEntry.getCount())
				return null;
			if(pos < 0)
				pos = 0;
			currentSample = pos;
			file.seek(currentSample*bytePerSample);
		}
		
		if(currentSample + length > valuesEntry.getCount())
			length = (int)(valuesEntry.getCount() - currentSample);
		
		if(scaled){
			if(dataType == DataType.INT8)
				return readByte8AsListScaled(length);
			if(dataType == DataType.UINT8)
				return readShort8AsListScaled(length);
			if(dataType == DataType.INT16)
				return readShort16AsListScaled(length);
			if(dataType == DataType.UINT16)
				return readInt16AsListScaled(length);
			if(dataType == DataType.INT32)
				return readInt32AsListScaled(length);
			if(dataType == DataType.UINT32)
				return readLong32AsListScaled(length);
			if((dataType == DataType.FLOAT))
				return readFloatAsListScaled(length);
			if((dataType == DataType.DOUBLE))
				return readDoubleAsListScaled(length);
		}else{
			if(dataType == DataType.INT8)
				return readByte8AsList(length);
			if(dataType == DataType.UINT8)
				return readShort8AsList(length);
			if(dataType == DataType.INT16)
				return readShort16AsList(length);
			if(dataType == DataType.UINT16)
				return readInt16AsList(length);
			if(dataType == DataType.INT32)
				return readInt32AsList(length);
			if(dataType == DataType.UINT32)
				return readLong32AsList(length);
			if((dataType == DataType.FLOAT))
				return readFloatAsList(length);
			if((dataType == DataType.DOUBLE))
				return readDoubleAsList(length);
		}
		return null;
	}
	
	private Value[] readByte8AsArray(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			byte[] data = new byte[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (byte)file.read8();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readShort8AsArray(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			short[] data = new short[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = file.read8();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readShort16AsArray(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			short[] data = new short[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (short)file.read16();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readInt16AsArray(int length)throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			int[] data = new int[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = file.read16();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readInt32AsArray(int length)throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			int[] data = new int[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (int)file.read32();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readLong32AsArray( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			long[] data = new long[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = file.read32();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readFloatAsArray( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			float[] data = new float[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = file.readFloat();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readDoubleAsArray( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = file.readDouble();
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[] readByte8AsArrayScaled(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = ((byte)file.read8() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readShort8AsArrayScaled(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (file.read8() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readShort16AsArrayScaled(int length) throws IOException{
		Value[] values = new Value[length];
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = ((short)file.read16() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readInt16AsArrayScaled(int length)throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (file.read16() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private Value[]  readInt32AsArrayScaled(int length)throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = ((int)file.read32() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readLong32AsArrayScaled( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (file.read32() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readFloatAsArrayScaled( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (file.readFloat() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	private Value[]  readDoubleAsArrayScaled( int length) throws IOException{
		Value[] values = new Value[length];

		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			double[] data = new double[channelCount];
			long samplestamp = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[i] = (file.readDouble() - baseline) * lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
	
		return values;
	}
	
	private ValueList readByte8AsList(int length) throws IOException{
		ValueList valueList = new ValueList();
		byte[][] data = new byte[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (byte)file.read8();	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readShort8AsList(int length) throws IOException{
		ValueList valueList = new ValueList();
		short[][] data = new short[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = file.read8();
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readShort16AsList(int length) throws IOException{
		ValueList valueList = new ValueList();
		short[][] data = new short[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (short)file.read16();	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readInt16AsList(int length)throws IOException{
		ValueList valueList = new ValueList();
		int[][] data = new int[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = file.read16();	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readInt32AsList(int length)throws IOException{
		ValueList valueList = new ValueList();
		int[][] data = new int[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (int)file.read32();	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readLong32AsList( int length) throws IOException{
		ValueList valueList = new ValueList();
		long[][] data = new long[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = file.read32();	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readFloatAsList( int length) throws IOException{
		ValueList valueList = new ValueList();
		float[][] data = new float[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = file.readFloat();	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readDoubleAsList( int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = file.readDouble();	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readByte8AsListScaled(int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = ((byte)file.read8() - baseline) * lsbValue;	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readShort8AsListScaled(int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (file.read8() - baseline) * lsbValue;
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readShort16AsListScaled(int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = ((short)file.read16() - baseline) * lsbValue;	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readInt16AsListScaled(int length)throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (file.read16() - baseline) * lsbValue;	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	
	private ValueList readInt32AsListScaled(int length)throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = ((int)file.read32() - baseline) * lsbValue;	
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readLong32AsListScaled( int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (file.read32() - baseline) * lsbValue;	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readFloatAsListScaled( int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (file.readFloat() - baseline) * lsbValue;	
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
	private ValueList readDoubleAsListScaled( int length) throws IOException{
		ValueList valueList = new ValueList();
		double[][] data = new double[length][channelCount];
		long[] timestamps = new long[length];
		
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			timestamps[sampleNumber] = file.read64();
			for(int i = 0 ; i < channelCount  ; i++)
				data[sampleNumber][i] = (file.readDouble() - baseline) * lsbValue;
			this.currentSample++;
		}
		
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
}
