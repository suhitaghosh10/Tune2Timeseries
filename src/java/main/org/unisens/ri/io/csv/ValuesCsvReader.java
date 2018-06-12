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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.unisens.CsvFileFormat;
import org.unisens.DataType;
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.io.ValuesReader;

public class ValuesCsvReader extends ValuesReader
{
	private BufferedReader file = null;
	private String separator = null;
	private String decimalSeparator = null;

	public ValuesCsvReader(ValuesEntry valuesEntry) throws IOException
	{
		super(valuesEntry);
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			file = new BufferedReader(new FileReader(this.absoluteFileName));
			this.separator = ((CsvFileFormat) valuesEntry.getFileFormat())
					.getSeparator();
			this.decimalSeparator = ((CsvFileFormat) valuesEntry.getFileFormat())
			.getDecimalSeparator();
			this.isOpened = true;
		}
	}

	@Override
	public void close() throws IOException
	{
		if (isOpened)
			file.close();
		this.isOpened = false;
	}

	public Value[] read(int length) throws IOException
	{
		return read(currentSample, length, false);
	}


	public Value[] read(long pos, int length) throws IOException
	{
		return read(pos, length, false);
	}


	@Override
	public Value[] readScaled(int length) throws IOException
	{
		return read(currentSample, length, true);
	}

	@Override
	public Value[] readScaled(long pos, int length) throws IOException
	{
		return read(pos, length, true);
	}

	private Value[] read(long pos, int length, boolean scaled)
			throws IOException
	{
		open();
		long sampleCount = valuesEntry.getCount();
		if (pos > sampleCount)
			return null;
		if (pos + length > sampleCount)
			length = (int) (sampleCount - pos);
		if (pos != currentSample)
			seek(pos);

		if (scaled)
		{
			if (dataType == DataType.INT8)
				return readByteAsArrayScaled(length);
			if ((dataType == DataType.INT16) || (dataType == DataType.UINT8))
				return readShortAsArrayScaled(length);
			if ((dataType == DataType.INT32) || (dataType == DataType.UINT16))
				return readIntAsArrayScaled(length);
			if (dataType == DataType.UINT32)
				return readLongAsArrayScaled(length);
			if (dataType == DataType.FLOAT)
				return readFloatAsArrayScaled(length);
			if (dataType == DataType.DOUBLE)
				return readDoubleAsArrayScaled(length);
		}
		else
		{
			if (dataType == DataType.INT8)
				return readByteAsArray(length);
			if ((dataType == DataType.INT16) || (dataType == DataType.UINT8))
				return readShortAsArray(length);
			if ((dataType == DataType.INT32) || (dataType == DataType.UINT16))
				return readIntAsArray(length);
			if (dataType == DataType.UINT32)
				return readLongAsArray(length);
			if (dataType == DataType.FLOAT)
				return readFloatAsArray(length);
			if (dataType == DataType.DOUBLE)
				return readDoubleAsArray(length);
		}

		return null;
	}

	@Override
	public ValueList readValuesListScaled(int length) throws IOException
	{
		return readValuesList(currentSample, length, true);
	}

	@Override
	public ValueList readValuesListScaled(long pos, int length)
			throws IOException
	{
		return readValuesList(pos, length, true);
	}

	public ValueList readValuesList(int length) throws IOException
	{
		return readValuesList(currentSample, length, false);
	}

	public ValueList readValuesList(long pos, int length) throws IOException
	{
		return readValuesList(pos, length, false);
	}

	private ValueList readValuesList(long pos, int length, boolean scaled)
			throws IOException
	{
		open();
		if (pos > valuesEntry.getCount())
			return null;
		if (pos < 0)
			pos = 0;
		if (pos != currentSample)
			seek(pos);
		if (pos + length > valuesEntry.getCount())
			length = (int) (valuesEntry.getCount() - pos);

		if (scaled)
		{
			if (dataType == DataType.INT8)
				return readByteAsListScaled(length);
			if (dataType == DataType.INT16 || dataType == DataType.UINT8)
				return readShortAsListScaled(length);
			if (dataType == DataType.INT32 || dataType == DataType.UINT16)
				return readIntAsListScaled(length);
			if (dataType == DataType.UINT32)
				return readLongAsListScaled(length);
			if ((dataType == DataType.FLOAT))
				return readFloatAsListScaled(length);
			if (dataType == DataType.DOUBLE)
				return readDoubleAsListScaled(length);
		}
		else
		{
			if (dataType == DataType.INT8)
				return readByteAsList(length);
			if (dataType == DataType.INT16 || dataType == DataType.UINT8)
				return readShortAsList(length);
			if (dataType == DataType.INT32 || dataType == DataType.UINT16)
				return readIntAsList(length);
			if (dataType == DataType.UINT32)
				return readLongAsList(length);
			if ((dataType == DataType.FLOAT))
				return readFloatAsList(length);
			if (dataType == DataType.DOUBLE)
				return readDoubleAsList(length);
		}
		return null;
	}


	public long getSampleCount()
	{
		try
		{
			open();
			long sampleCount = 0;
			while (file.readLine() != null)
				sampleCount++;
			resetPos();
			return sampleCount;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return -1;
		}
	}

	public void resetPos()
	{
		try
		{
			currentSample = 0;
			close();
			open();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private void seek(long pos)
	{
		try
		{
			while (currentSample < pos)
			{
				file.readLine();
				currentSample++;
			}
			if (currentSample > pos)
			{
				resetPos();
				seek(pos);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private Value[] readByteAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			byte[] data = new byte[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = Byte.parseByte(tokenizer.nextToken().trim());
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readShortAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			short[] data = new short[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = Short.parseShort(tokenizer.nextToken().trim());
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readIntAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			int[] data = new int[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = Integer.parseInt(tokenizer.nextToken().trim());
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readLongAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			long[] data = new long[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = Long.parseLong(tokenizer.nextToken().trim());
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readFloatAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		String dataString;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			float[] data = new float[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);

			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[i] = Float.parseFloat(dataString);
			}
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readDoubleAsArray(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[i] = Double.parseDouble(dataString);
			}
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readByteAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = (Byte.parseByte(tokenizer.nextToken().trim()) - baseline)
						* lsbValue;
			;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readShortAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = (Short.parseShort(tokenizer.nextToken().trim()) - baseline)
						* lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readIntAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = (Integer.parseInt(tokenizer.nextToken().trim()) - baseline)
						* lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readLongAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			for (int i = 0; i < channelCount; i++)
				data[i] = (Long.parseLong(tokenizer.nextToken().trim()) - baseline)
						* lsbValue;
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readFloatAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[i] = (Float.parseFloat(dataString) - baseline)
				* lsbValue;
			}
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}

	private Value[] readDoubleAsArrayScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		Value[] values = new Value[length];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			double[] data = new double[channelCount];
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			long samplestamp = Long.parseLong(tokenizer.nextToken().trim());
			
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[i] = (Double.parseDouble(dataString) - baseline)
				* lsbValue;
			}
			values[sampleNumber] = new Value(samplestamp, data);
			this.currentSample++;
		}
		return values;
	}


	private ValueList readByteAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		byte[][] data = new byte[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = Byte.parseByte(tokenizer.nextToken()
						.trim());
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readShortAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		short[][] data = new short[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = Short.parseShort(tokenizer.nextToken()
						.trim());
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readIntAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		int[][] data = new int[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Integer.parseInt(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = Integer.parseInt(tokenizer.nextToken()
						.trim());
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readLongAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		long[][] data = new long[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = Long.parseLong(tokenizer.nextToken()
						.trim());
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readFloatAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		float[][] data = new float[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[sampleNumber][i] = Float.parseFloat(dataString);
			}
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readDoubleAsList(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[sampleNumber][i] = Double.parseDouble(dataString);
			}
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readByteAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = (Byte.parseByte(tokenizer.nextToken()
						.trim()) - baseline)
						* lsbValue;
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readShortAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = (Short.parseShort(tokenizer.nextToken()
						.trim()) - baseline)
						* lsbValue;
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readIntAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Integer.parseInt(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = (Integer.parseInt(tokenizer.nextToken()
						.trim()) - baseline)
						* lsbValue;
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readLongAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
				data[sampleNumber][i] = (Long.parseLong(tokenizer.nextToken()
						.trim()) - baseline)
						* lsbValue;
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readFloatAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[sampleNumber][i] = (Float.parseFloat(dataString) - baseline)
						* lsbValue;
			}
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}

	private ValueList readDoubleAsListScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		String dataString;
		ValueList valueList = new ValueList();
		long[] timestamps = new long[length];
		double[][] data = new double[length][channelCount];
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			timestamps[sampleNumber] = Long.parseLong(tokenizer.nextToken()
					.trim());
			for (int i = 0; i < channelCount; i++)
			{
				dataString = tokenizer.nextToken().trim();
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace(
							decimalSeparator.charAt(0), '.');
				}
				data[sampleNumber][i] = (Double.parseDouble(dataString) - baseline)
						* lsbValue;
			}
			this.currentSample++;
		}
		valueList.setSamplestamps(timestamps);
		valueList.setData(data);
		return valueList;
	}
}
