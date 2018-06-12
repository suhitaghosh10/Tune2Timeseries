/*
Unisens Interface - interface for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV),
				   KIT, Germany

This file is part of the Unisens Interface. For more information, see
<http://www.unisens.org>

The Unisens Interface is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Interface is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Interface. If not, see <http://www.gnu.org/licenses/>. 
*/

package org.unisens;

import java.lang.reflect.Array;

/**
 * ValueList is a set of Values that can be accessed as Array of samplestamps and object [length][channelCount].
 * ValueList can for example be used from Matlab for a easier access.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public class ValueList {


	private long [] samplestamps;
	private Object data;

	public ValueList(){}
	
	/**
	 * Constructor
	 * 
	 * @param samplestamps the Array of samplestamps
	 * @param data of this ValueList as object [length][channelCount]
	 */
	public ValueList(long[] samplestamps, Object data){
		this.samplestamps = samplestamps;
		setData(data);
	}
	
	/**
	 * Gets the samplestamps of this list of values.
	 * 
	 * @return the Array of samplestamps 
	 */
	public long [] getSamplestamps(){
		return samplestamps;
	}

	/**
	 * Sets the samplestamps of this list of values
	 * 
	 * @param samplestamps the Array of samplestamps
	 */
	public void setSamplestamps(long[] samplestamps){
		this.samplestamps = samplestamps;
	}

	/**
	 * Gets the rows of data of this ValueList as object [length][channelCount]
	 * 
	 * @return the rows of data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the rows of data of this ValueList as object [length][channelCount]
	 * 
	 * @param data the rows of data
	 */
	public void setData(Object data) {
		if(data.getClass().isArray()){
			if(!Array.get(data, 0).getClass().isArray()){
				this.data = vectorToMatrix(data);
			}else{
				this.data = data;
			}
		}else{
			if(data instanceof Byte){
				this.data = new byte[][]{{(Byte)data}};
			}
			else if(data instanceof Short){
				this.data = new short[][]{{(Short)data}};
			}
			else if(data instanceof Integer){
				this.data = new int[][]{{(Integer)data}};
			}
			else if(data instanceof Long){
				this.data = new long[][]{{(Long)data}};
			}
			else if(data instanceof Double){
				this.data = new double[][]{{(Double)data}};
			}
			else if(data instanceof Float){
				this.data = new float[][]{{(Float)data}};
			}
		}
	}
	
	private static Object vectorToMatrix(Object data)
	{
		if(data instanceof byte[])
		{
			byte[] data1 = (byte[]) data;
			byte[][] result = new byte[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		else if(data instanceof short[])
		{
			short[] data1 = (short[]) data;
			short[][] result = new short[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		else if(data instanceof int[])
		{
			int[] data1 = (int[]) data;
			int[][] result = new int[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		else if(data instanceof long[])
		{
			long[] data1 = (long[]) data;
			long[][] result = new long[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		else if(data instanceof double[])
		{
			double[] data1 = (double[]) data;
			double[][] result = new double[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		else if(data instanceof float[])
		{
			float[] data1 = (float[]) data;
			float[][] result = new float[data1.length][1];
			for(int i = 0; i < data1.length ; i++)
				result[i][0] = data1[i];
			data =  result;
		}
		
		return data;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ValueList))
			return false;
		ValueList valueList = (ValueList)o;
		long[] valueListSamplestamps = valueList.getSamplestamps();
		if(this.samplestamps.length != valueListSamplestamps.length)
			return false;
		for (int i = 0; i < valueListSamplestamps.length; i++)
			if(this.samplestamps[i] != valueListSamplestamps[i])
				return false;
		
		if(data instanceof byte[][] && valueList.getData() instanceof byte[][]){
			byte[][] data1 = (byte[][])data;
			byte[][] data2 = (byte[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		else if(data instanceof short[][] && valueList.getData() instanceof short[][]){
			short[][] data1 = (short[][])data;
			short[][] data2 = (short[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		else if(data instanceof int[][] && valueList.getData() instanceof int[][]){
			int[][] data1 = (int[][])data;
			int[][] data2 = (int[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		else if(data instanceof long[][] && valueList.getData() instanceof long[][]){
			long[][] data1 = (long[][])data;
			long[][] data2 = (long[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		else if(data instanceof float[][] && valueList.getData() instanceof float[][]){
			float[][] data1 = (float[][])data;
			float[][] data2 = (float[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		else if(data instanceof double[][] && valueList.getData() instanceof double[][]){
			double[][] data1 = (double[][])data;
			double[][] data2 = (double[][])valueList.getData();
			if(data1.length != data2.length)
				return false;
			if(data1[0].length != data2[0].length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				for (int j = 0; j < data2[0].length; j++) {
					if(data1[i][j] != data2[i][j])
						return false;
				}
				
			}
			return true;
		}
		return false;
	}

}
