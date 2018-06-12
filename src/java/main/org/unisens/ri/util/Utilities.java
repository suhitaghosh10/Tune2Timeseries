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

package org.unisens.ri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.unisens.DataType;

public class Utilities
{

	public static Object convertFrom1DimTo2DimArray(Object data,
			int channelCount)
	{
		boolean isNotArray = !data.getClass().isArray();
		boolean isOneDimensinal = false;
		if (isNotArray)
		{
			isOneDimensinal = true;
		}
		else
		{
			if (!Array.get(data, 0).getClass().isArray())
			{
				isOneDimensinal = true;
			}
		}

		if (isOneDimensinal)
		{
			Class type = data.getClass().getComponentType();
			if (isNotArray)
				type = data.getClass();
			if (type == Byte.TYPE || type == Byte.class)
			{
				if (isNotArray)
				{
					return new byte[][] { { (Byte) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						byte[] data1 = (byte[]) data;
						byte[][] result = new byte[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new byte[][] { (byte[]) data };
					}
				}
			}
			if (type == Short.TYPE || type == Short.class)
			{
				if (isNotArray)
				{
					return new short[][] { { (Short) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						short[] data1 = (short[]) data;
						short[][] result = new short[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new short[][] { (short[]) data };
					}
				}
			}
			if (type == Integer.TYPE || type == Integer.class)
			{
				if (isNotArray)
				{
					return new int[][] { { (Integer) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						int[] data1 = (int[]) data;
						int[][] result = new int[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new int[][] { (int[]) data };
					}
				}
			}
			if (type == Long.TYPE || type == Long.class)
			{
				if (isNotArray)
				{
					return new long[][] { { (Long) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						long[] data1 = (long[]) data;
						long[][] result = new long[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new long[][] { (long[]) data };
					}
				}
			}
			if (type == Double.TYPE || type == Double.class)
			{
				if (isNotArray)
				{
					return new double[][] { { (Double) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						double[] data1 = (double[]) data;
						double[][] result = new double[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new double[][] { (double[]) data };
					}
				}
			}
			if (type == Float.TYPE || type == Float.class)
			{
				if (isNotArray)
				{
					return new float[][] { { (Float) data } };
				}
				else
				{
					if (channelCount == 1)
					{
						float[] data1 = (float[]) data;
						float[][] result = new float[data1.length][1];
						for (int i = 0; i < data1.length; i++)
							result[i][0] = data1[i];
						return result;
					}
					else
					{
						return new float[][] { (float[]) data };
					}
				}
			}
		}

		return data;

	}

	public static Date convertStringToDate(String date)
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat simpleDateFormatWithMs = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS");
			String pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}";
			
			if (Pattern.matches(pattern, date))
			{
				return simpleDateFormatWithMs.parse(date);
			}
			return simpleDateFormat.parse(date);
		}
		catch (ParseException pe)
		{
			pe.printStackTrace();
			return null;
		}
	}

	public static String convertDateToString(Date date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS");
		return simpleDateFormat.format(date);
	}

	public static void copyFile(File in, File out)
	{
		try
		{
			FileInputStream fis = new FileInputStream(in);
			FileOutputStream fos = new FileOutputStream(out);
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1)
			{
				fos.write(buf, 0, i);
			}
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static int getSampleCount(Object data, DataType dataType)
	{
		int length = 1;

		switch (dataType.ordinal())
		{
			case 0:
			{
				if (data instanceof byte[][])
					length = ((byte[][]) data).length;
				else if (data instanceof byte[])
					length = ((byte[]) data).length;
				break;
			}
			case 1:
			case 2:
			{
				if (data instanceof short[][])
					length = ((short[][]) data).length;
				else if (data instanceof short[])
					length = ((short[]) data).length;
				break;
			}
			case 3:
			case 4:
			{
				if (data instanceof int[][])
					length = ((int[][]) data).length;
				else if (data instanceof int[])
					length = ((int[]) data).length;
				break;
			}
			case 5:
			{
				if (data instanceof long[][])
					length = ((long[][]) data).length;
				else if (data instanceof long[])
					length = ((long[]) data).length;
				break;
			}
			case 6:
			{
				if (data instanceof float[][])
					length = ((float[][]) data).length;
				else if (data instanceof float[])
					length = ((float[]) data).length;
				break;
			}
			case 7:
			{
				if (data instanceof double[][])
					length = ((double[][]) data).length;
				else if (data instanceof double[])
					length = ((double[]) data).length;
				break;
			}
		}
		return length;
	}

	// Convert 8 bit unsigned byte to 16 bit signed short. Values > 127 will
	// become negative
	public static short[][] convertUnsignedByteToShort(byte[][] unsignedByteData)
	{
		short[][] convertedShortData = new short[unsignedByteData.length][unsignedByteData[0].length];
		// Add 256 (= 2^8) to all negative values to get the correct value.
		for (int i = 0; i < unsignedByteData.length; i++)
		{
			for (int j = 0; j < unsignedByteData[i].length; j++)
			{
				if (unsignedByteData[i][j] < 0)
					convertedShortData[i][j] = (short) (unsignedByteData[i][j] + 256);
				else
					convertedShortData[i][j] = (short) (unsignedByteData[i][j]);
			}
		}
		return convertedShortData;
	}

	// Convert 16 bit unsigned short to 32 bit signed integer. Values > 32767
	// will become negative
	public static int[][] convertUnsignedShortToInteger(
			short[][] unsignedShortData)
	{
		int[][] convertedIntegerData = new int[unsignedShortData.length][unsignedShortData[0].length];
		// Add 65536 (= 2^16) to all negative values to get the correct value.
		for (int i = 0; i < unsignedShortData.length; i++)
		{
			for (int j = 0; j < unsignedShortData[i].length; j++)
			{
				if (unsignedShortData[i][j] < 0)
					convertedIntegerData[i][j] = (int) (unsignedShortData[i][j] + 65536);
				else
					convertedIntegerData[i][j] = (int) (unsignedShortData[i][j]);
			}
		}
		return convertedIntegerData;
	}

	// Convert 32 bit unsigned integer to 64 bit signed long. Values >
	// 2147483647 will become negative
	public static long[][] convertUnsignedIntegerToLong(
			int[][] unsignedIntegerData)
	{
		long[][] convertedLongData = new long[unsignedIntegerData.length][unsignedIntegerData[0].length];
		// Add 4294967296 (= 2^32) to all negative values to get the correct
		// value.
		for (int i = 0; i < unsignedIntegerData.length; i++)
		{
			for (int j = 0; j < unsignedIntegerData[i].length; j++)
			{
				if (unsignedIntegerData[i][j] < 0)
					convertedLongData[i][j] = (long) (unsignedIntegerData[i][j] + 4294967296L);
				else
					convertedLongData[i][j] = (long) (unsignedIntegerData[i][j]);
			}
		}
		return convertedLongData;
	}
}
