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
import org.unisens.SignalEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.SignalWriter;
import org.unisens.ri.util.Utilities;

public class SignalCsvWriter extends SignalWriter
{
	private BufferedFileWriter file = null;
	private static String NEWLINE = System.getProperty("line.separator");
	private String separator = null;
	private String decimalSeparator = null;

	public SignalCsvWriter(SignalEntry signal) throws IOException
	{
		super(signal);
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			file = new BufferedFileWriter(new File(this.absoluteFileName));
			isOpened = true;
			separator = ((CsvFileFormat) signalEntry.getFileFormat())
					.getSeparator();
			decimalSeparator = ((CsvFileFormat) signalEntry.getFileFormat())
			.getDecimalSeparator();
		}
	}

	public void close() throws IOException
	{
		if (this.isOpened)
			file.close();
		this.isOpened = false;
	}

	public void append(Object data) throws IOException,
			IllegalArgumentException
	{
		open();
		appendData(data);
		file.flush();
	}

	private void appendData(Object data) throws IOException
	{
		data = Utilities.convertFrom1DimTo2DimArray(data, channelCount);

		if (dataType == DataType.INT8)
		{
			appendByte((byte[][]) data);
			return;
		}
		if (dataType == DataType.INT16)
		{
			appendShort((short[][]) data);
			return;
		}
		if (dataType == DataType.UINT8)
		{
			if (data instanceof byte[][])
				appendShort(Utilities
						.convertUnsignedByteToShort((byte[][]) data));
			else
				appendShort((short[][]) data);
			return;
		}
		if (dataType == DataType.INT32)
		{
			appendInteger((int[][]) data);
			return;
		}
		if (dataType == DataType.UINT16)
		{
			if (data instanceof short[][])
				appendInteger(Utilities
						.convertUnsignedShortToInteger((short[][]) data));
			else
				appendInteger((int[][]) data);
			return;
		}
		if (dataType == DataType.UINT32)
		{
			if (data instanceof int[][])
				appendLong(Utilities
						.convertUnsignedIntegerToLong((int[][]) data));
			else
				appendLong((long[][]) data);
			return;
		}
		if (dataType == DataType.FLOAT)
		{
			appendFloat((float[][]) data);
			return;
		}
		if (dataType == DataType.DOUBLE)
		{
			appendDouble((double[][]) data);
			return;
		}
	}

	private void appendByte(byte[][] data) throws IOException
	{
		String line;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length - 1; j++)
			{
				line += data[i][j] + this.separator;
			}
			file.writeString(line + data[i][data[i].length - 1] + NEWLINE);
		}
	}

	private void appendShort(short[][] data) throws IOException
	{
		String line;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length - 1; j++)
			{
				line += data[i][j] + this.separator;
			}
			file.writeString(line + data[i][data[i].length - 1] + NEWLINE);
		}
	}

	private void appendInteger(int[][] data) throws IOException
	{
		String line;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length - 1; j++)
			{
				line += data[i][j] + this.separator;
			}
			file.writeString(line + data[i][data[i].length - 1] + NEWLINE);
		}
	}

	private void appendLong(long[][] data) throws IOException
	{
		String line;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length - 1; j++)
			{
				line += data[i][j] + this.separator;
			}
			file.writeString(line + data[i][data[i].length - 1] + NEWLINE);
		}
	}

	private void appendFloat(float[][] data) throws IOException
	{
		String line;
		String dataString;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length; j++)
			{
				dataString = data[i][j] + "";
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace('.', decimalSeparator.charAt(0));
				}
				line +=  dataString;
				if (j<(data[0].length-1))
				{
					line += this.separator;
				}
			}
			file.writeString(line + NEWLINE);
		}
	}

	private void appendDouble(double[][] data) throws IOException
	{
		String line;
		String dataString;
		for (int i = 0; i < data.length; i++)
		{
			line = "";
			int j = 0;
			for (; j < data[0].length; j++)
			{
				dataString = data[i][j] + "";
				if (!decimalSeparator.equalsIgnoreCase("."))
				{
					dataString = dataString.replace('.', decimalSeparator.charAt(0));
				}
				line +=  dataString;
				if (j<(data[0].length-1))
				{
					line += this.separator;
				}
			}
			file.writeString(line + NEWLINE);
		}
	}
}
