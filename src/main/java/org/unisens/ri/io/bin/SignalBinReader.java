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
import org.unisens.ri.io.BufferedFileReader;
import org.unisens.ri.io.SignalReader;

public class SignalBinReader extends SignalReader{
	private BufferedFileReader file = null;
	private int bytePerSample = 2;
	
	public SignalBinReader(SignalEntry signalEntry) throws IOException{
		super(signalEntry);
		this.open();
	}
	
	public void open() throws IOException{
		if(!isOpened){
			this.file = new BufferedFileReader(new File(absoluteFileName));
			this.isOpened = true;
			this.file.setSigned(!dataType.value().startsWith("u"));
		
			if(((BinFileFormat)this.signalEntry.getFileFormat()).getEndianess() == Endianess.BIG)
				this.file.setEndian(ByteOrder.BIG_ENDIAN);
			else
				this.file.setEndian(ByteOrder.LITTLE_ENDIAN);
			
			if( dataType == DataType.INT8 || dataType == DataType.UINT8 ){
				this.bytePerSample = 1 * channelCount;
				return;
			}
			if( dataType == DataType.INT16 || dataType == DataType.UINT16 ){
				this.bytePerSample = 2 * channelCount;
				return;
			}
			if( dataType == DataType.INT32 || dataType == DataType.UINT32 || dataType == DataType.FLOAT ){
				this.bytePerSample = 4 * channelCount;
				return;
			}
			if( dataType == DataType.DOUBLE ){
				this.bytePerSample = 8 * channelCount;
				return;
			}
		}
	}

	public void close() throws IOException{
		if(isOpened)
			file.close();
		this.isOpened = false;
	}
	
	
	@Override
	public double[][] readScaled(int length) throws IOException {
		return (double[][])read(currentSample, length, true);
	}

	@Override
	public double[][] readScaled(long pos, int length) throws IOException {
		return (double[][])read(pos, length, true);
	}

	@Override
	public Object read(int length) throws IOException {
		return read(currentSample, length, false);
	}

	@Override
	public Object read(long pos, int length) throws IOException {
		return read(pos, length, false) ;
	}

	private Object read(long pos , int length, boolean scaled) throws IOException{
		open();
		if(currentSample != pos){
			if(pos > signalEntry.getCount())
				return null;
			if(pos < 0)
				pos = 0;
			currentSample = pos;
			file.seek(currentSample*bytePerSample);
		}
		if(currentSample + length > signalEntry.getCount())
			length = (int)(signalEntry.getCount() - currentSample);
		
		if(scaled){
			if(dataType == DataType.INT8)
				return readByteScaled(length);
			if(dataType == DataType.UINT8)
				return readShort8Scaled(length);
			if(dataType == DataType.INT16)
				return readShort16Scaled(length);
			if(dataType == DataType.UINT16)
				return readInt16Scaled(length);
			if(dataType == DataType.INT32)
				return readInt32Scaled(length);
			if(dataType == DataType.UINT32)
				return readLong32Scaled(length);
			if(dataType == DataType.FLOAT)
				return readFloatScaled(length);
			if(dataType == DataType.DOUBLE)
				return readDoubleScaled(length);
		}else{
			if(dataType == DataType.INT8)
				return readByte(length);
			if(dataType == DataType.UINT8)
				return readShort8(length);
			if(dataType == DataType.INT16)
				return readShort16(length);
			if(dataType == DataType.UINT16)
				return readInt16(length);
			if(dataType == DataType.INT32)
				return readInt32(length);
			if(dataType == DataType.UINT32)
				return readLong32(length);
			if(dataType == DataType.FLOAT)
				return readFloat(length);
			if(dataType == DataType.DOUBLE)
				return readDouble(length);
		}
		
		return null;
	}
	
	private byte[][] readByte(int length) throws IOException{
		byte[][] data = new byte[length][channelCount];
		byte currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (byte)file.read8();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private short[][] readShort8(int length) throws IOException{
		short[][] data = new short[length][channelCount];
		short currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (short)file.read8();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private short[][] readShort16(int length) throws IOException{
		short[][] data = new short[length][channelCount];
		short currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (short)file.read16();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private int[][] readInt16(int length)throws IOException{
		int[][] data = new int[length][channelCount];
		int currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.read16();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private int[][] readInt32(int length)throws IOException{
		int[][] data = new int[length][channelCount];
		int currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (int)file.read32();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	private long[][] readLong32( int length) throws IOException{
		long[][] data = new long[length][channelCount];
		long currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.read32();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private float[][] readFloat( int length) throws IOException{
		float[][] data = new float[length][channelCount];
		float currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.readFloat();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readDouble( int length) throws IOException{
		double[][] data = new double[length][channelCount];
		double currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.readDouble();
				data[sampleNumber][i] = currentValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readByteScaled(int length) throws IOException{
		double[][] data = new double[length][channelCount];
		byte currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (byte)file.read8();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readShort8Scaled(int length) throws IOException{
		double[][] data = new double[length][channelCount];
		short currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (short)file.read8();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readShort16Scaled(int length) throws IOException{
		double[][] data = new double[length][channelCount];
		short currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (short)file.read16();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readInt16Scaled(int length)throws IOException{
		double[][] data = new double[length][channelCount];
		int currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.read16();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readInt32Scaled(int length)throws IOException{
		double[][] data = new double[length][channelCount];
		int currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = (int)file.read32();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	private double[][] readLong32Scaled( int length) throws IOException{
		double[][] data = new double[length][channelCount];
		long currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.read32();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readFloatScaled( int length) throws IOException{
		double[][] data = new double[length][channelCount];
		float currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.readFloat();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
	}
	
	private double[][] readDoubleScaled( int length) throws IOException{
		double[][] data = new double[length][channelCount];
		double currentValue = 0;
		for(int sampleNumber = 0; sampleNumber < length ; sampleNumber++){
			for(int i = 0 ; i < channelCount  ; i++){
				currentValue = file.readDouble();
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}	
			this.currentSample++;
		}
		return data;
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
}
