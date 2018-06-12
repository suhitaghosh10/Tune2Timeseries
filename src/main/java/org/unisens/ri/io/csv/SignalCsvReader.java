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
import org.unisens.SignalEntry;
import org.unisens.ri.io.SignalReader;

public class SignalCsvReader extends SignalReader
{
	private BufferedReader file = null;
	private String separator = null;
	private String decimalSeparator = null;

	public SignalCsvReader(SignalEntry signalEntry) throws IOException
	{
		super(signalEntry);
		open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			file = new BufferedReader(new FileReader(this.absoluteFileName));
			this.separator = ((CsvFileFormat) signalEntry.getFileFormat())
					.getSeparator();
			this.decimalSeparator = ((CsvFileFormat) signalEntry.getFileFormat())
			.getDecimalSeparator();
			this.isOpened = true;
			this.currentSample = 0;
		}
	}

	@Override
	public void close() throws IOException
	{
		if (isOpened)
			file.close();
		this.isOpened = false;
	}

	@Override
	public Object read(int length) throws IOException
	{
		return read(length, false);
	}

	@Override
	public Object read(long pos, int length) throws IOException
	{
		return read(pos, length, false);
	}

	@Override
	public double[][] readScaled(int length) throws IOException
	{
		return (double[][]) read(length, true);
	}

	@Override
	public double[][] readScaled(long pos, int length) throws IOException
	{
		return (double[][]) read(pos, length, true);
	}

	private Object read(int length, boolean scaled) throws IOException
	{
		return this.read(currentSample, length, scaled);
	}

	private Object read(long pos, int length, boolean scaled)
			throws IOException
	{
		open();
		long sampleCount = signalEntry.getCount();

		if (pos > sampleCount)
			return null;
		if (pos != currentSample)
			seek(pos);
		if (currentSample + length > sampleCount)
			length = (int) (sampleCount - currentSample);

		if (scaled)
		{
			if (dataType == DataType.INT8)
				return readByteScaled(length);
			if ((dataType == DataType.INT16) || (dataType == DataType.UINT8))
				return readShortScaled(length);
			if ((dataType == DataType.INT32) || (dataType == DataType.UINT16))
				return readIntScaled(length);
			if (dataType == DataType.UINT32)
				return readLongScaled(length);
			if (dataType == DataType.FLOAT)
				return readFloatScaled(length);
			if (dataType == DataType.DOUBLE)
				return readDoubleScaled(length);
		}
		else
		{
			if (dataType == DataType.INT8)
				return readByte(length);
			if ((dataType == DataType.INT16) || (dataType == DataType.UINT8))
				return readShort(length);
			if ((dataType == DataType.INT32) || (dataType == DataType.UINT16))
				return readInt(length);
			if (dataType == DataType.UINT32)
				return readLong(length);
			if (dataType == DataType.FLOAT)
				return readFloat(length);
			if (dataType == DataType.DOUBLE)
				return readDouble(length);
		}

		return null;
	}

	private byte[][] readByte(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		byte[][] data = new byte[length][channelCount];
		byte currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Byte.parseByte(tokenizer.nextToken().trim());
				data[sampleNumber][i] = currentValue;
			}
			currentSample++;
		}

		return data;
	}


	private short[][] readShort(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		short[][] data = new short[length][channelCount];
		short currentValue = 0;

		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Short.parseShort(tokenizer.nextToken().trim());
				data[sampleNumber][i] = currentValue;
			}
			currentSample++;
		}
		return data;
	}

	private int[][] readInt(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		int[][] data = new int[length][channelCount];
		int currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Integer.parseInt(tokenizer.nextToken().trim());
				data[sampleNumber][i] = currentValue;
			}
			currentSample++;
		}

		return data;
	}

	private long[][] readLong(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		long[][] data = new long[length][channelCount];
		long currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Long.parseLong(tokenizer.nextToken().trim());
				data[sampleNumber][i] = currentValue;
			}
			currentSample++;
		}
		return data;
	}

	private float[][] readFloat(int length) throws IOException
	{
		try
		{
			String line = "";
			StringTokenizer tokenizer;
			String dataString;
			float[][] data = new float[length][channelCount];
			for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
			{
				line = file.readLine();
				tokenizer = new StringTokenizer(line, separator);
				for (int i = 0; i < this.channelCount; i++)
				{
					dataString = tokenizer.nextToken().trim();
					if (!decimalSeparator.equalsIgnoreCase("."))
					{
						dataString = dataString.replace(
								decimalSeparator.charAt(0), '.');
					}
					data[sampleNumber][i] = Float.parseFloat(dataString);
				}
				currentSample++;
			}
			return data;
		}
		catch (NumberFormatException pe)
		{
			pe.printStackTrace();
			return null;
		}
	}

	private double[][] readDouble(int length) throws IOException
	{
		try
		{
			String line = "";
			StringTokenizer tokenizer;
			String dataString;
			double[][] data = new double[length][channelCount];
			for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
			{
				line = file.readLine();
				tokenizer = new StringTokenizer(line, separator);
				for (int i = 0; i < this.channelCount; i++)
				{
					dataString = tokenizer.nextToken().trim();
					if (!decimalSeparator.equalsIgnoreCase("."))
					{
						dataString = dataString.replace(
								decimalSeparator.charAt(0), '.');
					}
					data[sampleNumber][i] = Double.parseDouble(dataString);
				}
				currentSample++;
			}
			return data;
		}
		catch (NumberFormatException pe)
		{
			pe.printStackTrace();
			return null;
		}
	}

	private double[][] readByteScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		double[][] data = new double[length][channelCount];
		byte currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Byte.parseByte(tokenizer.nextToken().trim());
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}
			currentSample++;
		}

		return data;
	}


	private double[][] readShortScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		double[][] data = new double[length][channelCount];
		short currentValue = 0;

		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Short.parseShort(tokenizer.nextToken().trim());
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}
			currentSample++;
		}
		return data;
	}

	private double[][] readIntScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		double[][] data = new double[length][channelCount];
		int currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Integer.parseInt(tokenizer.nextToken().trim());
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}
			currentSample++;
		}

		return data;
	}

	private double[][] readLongScaled(int length) throws IOException
	{
		String line = "";
		StringTokenizer tokenizer;
		double[][] data = new double[length][channelCount];
		long currentValue = 0;
		for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
		{
			line = file.readLine();
			tokenizer = new StringTokenizer(line, separator);
			for (int i = 0; i < this.channelCount; i++)
			{
				currentValue = Long.parseLong(tokenizer.nextToken().trim());
				data[sampleNumber][i] = (currentValue - baseline) * lsbValue;
			}
			currentSample++;
		}
		return data;
	}

	private double[][] readFloatScaled(int length) throws IOException
	{
		try
		{
			String line = "";
			StringTokenizer tokenizer;
			String dataString;
			double[][] data = new double[length][channelCount];
			for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
			{
				line = file.readLine();
				tokenizer = new StringTokenizer(line, separator);
				for (int i = 0; i < this.channelCount; i++)
				{
					dataString = tokenizer.nextToken().trim();
					if (!decimalSeparator.equalsIgnoreCase("."))
					{
						dataString = dataString.replace(
								decimalSeparator.charAt(0), '.');
					}
					data[sampleNumber][i] = (Float.parseFloat(dataString) - baseline) * lsbValue;
				}
				currentSample++;
			}
			return data;
		}
		catch (NumberFormatException pe)
		{
			pe.printStackTrace();
			return null;
		}
	}

	private double[][] readDoubleScaled(int length) throws IOException
	{
		try
		{
			String line = "";
			StringTokenizer tokenizer;
			String dataString;
			double[][] data = new double[length][channelCount];
			for (int sampleNumber = 0; sampleNumber < length; sampleNumber++)
			{
				line = file.readLine();
				tokenizer = new StringTokenizer(line, separator);
				for (int i = 0; i < this.channelCount; i++)
				{
					dataString = tokenizer.nextToken().trim();
					if (!decimalSeparator.equalsIgnoreCase("."))
					{
						dataString = dataString.replace(
								decimalSeparator.charAt(0), '.');
					}
					data[sampleNumber][i] = (Double.parseDouble(dataString) - baseline) * lsbValue;
				}
				currentSample++;
			}
			return data;
		}
		catch (NumberFormatException pe)
		{
			pe.printStackTrace();
			return null;
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


}
