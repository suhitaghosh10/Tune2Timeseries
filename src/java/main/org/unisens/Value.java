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


/**
 * Value represent data values acquired at a certain point in time. Points in time 
 * are given as samplestamps, i.e. sample number using a specific sampleRate that 
 * is set in the {@link ValuesEntry}. 
 *
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public class Value {

	private long sampleStamp;
	private Object data;
	
	/**
	 * Constructor
	 * 
	 * @param sampleStamp point in time determined by sample number
	 * @param data the data as object [channelCount]
	 */
	public Value(long sampleStamp, Object data){
		this.sampleStamp = sampleStamp;
		this.setData(data);
	}

	/**
	 * Gets the sample stamp of this Value.
	 * 
	 * @return the samplestamp
	 * @deprecated use getSampleStamp
	 * @see org.unisens.getSampleStamp
	 */
	@Deprecated
	public long getSamplestamp() {
		return sampleStamp;
	}

	/**
	 * Sets the samplestamp to this Value
	 * 
	 * @param samplestamp
	 * @deprecated use setSampleStamp
	 * @see org.unisens.setSampleStamp
	 */
	@Deprecated
	public void setSamplestamp(long samplestamp) {
		this.sampleStamp = samplestamp;
	}

	/**
	 * Gets the sample stamp of this Value.
	 * 
	 * @return the sample stamp
	 */
	public long getSampleStamp() {
		return sampleStamp;
	}

	/**
	 * Sets the sample stamp to this Value
	 * 
	 * @param sampleStamp
	 */
	public void setSampleStamp(long sampleStamp) {
		this.sampleStamp = sampleStamp;
	}

	/**
	 * Gets the data of this Value as object [channelCount]. Where channelCount is the number of 
	 * channels of this Value
	 * 
	 * @return the data of this value
	 */
	public Object getData() {
		return data;
	}


	/**
	 * Sets the data of this Values. 
	 * 
	 * @param data the data of this Value
	 */
	public void setData(Object data) {
		if(data instanceof Byte){
			this.data = new byte[]{(Byte)data};
			return;
		}
		if(data instanceof Short){
			this.data = new short[]{(Short)data};
			return;
		}
		if(data instanceof Integer){
			this.data = new int[]{(Integer)data};
			return;
		}
		if(data instanceof Long){
			this.data = new long[]{(Long)data};
			return;
		}
		if(data instanceof Float){
			this.data = new float[]{(Float)data};
			return;
		}
		if(data instanceof Double){
			this.data = new double[]{(Double)data};
			return;
		}
		this.data = data;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Value))
			return false;
		Value aValue = (Value)o;
		if(sampleStamp != aValue.getSampleStamp())
			return false;
		if(data instanceof byte[] && aValue.getData() instanceof byte[]){
			byte[] data1 = (byte[])data;
			byte[] data2 = (byte[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		else if(data instanceof short[] && aValue.getData() instanceof short[]){
			short[] data1 = (short[])data;
			short[] data2 = (short[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		else if(data instanceof int[] && aValue.getData() instanceof int[]){
			int[] data1 = (int[])data;
			int[] data2 = (int[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		else if(data instanceof long[] && aValue.getData() instanceof long[]){
			long[] data1 = (long[])data;
			long[] data2 = (long[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		else if(data instanceof double[] && aValue.getData() instanceof double[]){
			double[] data1 = (double[])data;
			double[] data2 = (double[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		else if(data instanceof float[] && aValue.getData() instanceof float[]){
			float[] data1 = (float[])data;
			float[] data2 = (float[])aValue.getData();
			if(data1.length != data2.length)
				return false;
			for (int i = 0; i < data2.length; i++) {
				if(data1[i] != data2[i])
					return false;
			}
			return true;
		}
		return false;
	}

}
