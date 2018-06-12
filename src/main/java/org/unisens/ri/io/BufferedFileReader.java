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

package org.unisens.ri.io;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class BufferedFileReader
{
	protected File file;
	protected String absoluteFileName;
    protected BufferedInputStream in;
    protected ByteBuffer buf1;
    protected ByteBuffer buf2;
    protected ByteBuffer buf4;
    protected ByteBuffer buf8;
    protected byte[] byteBuf1;
    protected byte[] byteBuf2;
    protected byte[] byteBuf4;
    protected byte[] byteBuf8;

    protected ByteOrder endian;

    protected boolean signed;
    
    protected long currentPosition;

    public BufferedFileReader(File file) throws IOException{
    	if(!file.exists())
    		file.createNewFile();
    	this.file = file;
    	this.absoluteFileName = this.file.getAbsolutePath();
        this.in = new BufferedInputStream(new FileInputStream(file));
        this.buf1 = ByteBuffer.allocate(1);
        this.buf2 = ByteBuffer.allocate(2);
        this.buf4 = ByteBuffer.allocate(4);
        this.buf8 = ByteBuffer.allocate(8);
        this.buf1.order(ByteOrder.LITTLE_ENDIAN);
        this.buf2.order(ByteOrder.LITTLE_ENDIAN);
        this.buf4.order(ByteOrder.LITTLE_ENDIAN);
        this.buf8.order(ByteOrder.LITTLE_ENDIAN);
        this.byteBuf1 = new byte[1];
        this.byteBuf2 = new byte[2];
        this.byteBuf4 = new byte[4];
        this.byteBuf8 = new byte[8];
        signed = true;
        currentPosition = 0;
    }
    
    public void setEndian(ByteOrder byteOrder){
       this.buf1.order(byteOrder);
       this.buf2.order(byteOrder);
       this.buf4.order(byteOrder);
       this.buf8.order(byteOrder);
    }
    
    public ByteOrder getEndian(){
        return this.buf1.order();
    }
    
    public void setSigned(boolean b){
    	signed = b;
    }

    public boolean getSigned(){
    	return signed;
    }
  
    public short read8()throws java.io.IOException{	
    	int readed = in.read(byteBuf1);
    	currentPosition += readed;
        if(signed)
            return byteBuf1[0];
        else
            return (short)(byteBuf1[0] & 0xff);
    }

    public int read16() throws java.io.IOException{
    	int readed = in.read(byteBuf2);
    	currentPosition += readed;
        this.buf2.clear();
    	this.buf2.put(byteBuf2);
    	this.buf2.rewind();
    	
    	int result = this.buf2.getShort();
    	
        if(signed)
        	if( (result&0x8000) == 0x8000 )
        		result = -(0x10000 - result);

        return result & 0xffff;
    }
    
    public long read32()throws java.io.IOException{
    	int readed = in.read(byteBuf4);
    	currentPosition += readed;
        this.buf4.clear();
    	this.buf4.put(byteBuf4);
    	this.buf4.rewind();
    	
    	long result = this.buf4.getInt();
        if(signed)
        	if( (result&0x80000000L) == 0x80000000L )
        		result = -(0x100000000L - result);

        return result & 0xffffffffL;
    }
    
    public long read64()throws java.io.IOException{
    	int readed = in.read(byteBuf8);
    	currentPosition += readed;
        this.buf8.clear();
    	this.buf8.put(byteBuf8);
    	this.buf8.rewind();
    	
    	long result = this.buf8.getLong();
    	
        if(signed)
        	if( (result&0x800000000000000L) == 0x800000000000000L )
        		result = -(0x1000000000000000L - result);

        return result & 0xffffffffffffffffL;
    }
    
    public float readFloat() throws IOException{
    	int readed = in.read(byteBuf4);
    	currentPosition += readed;
        this.buf4.clear();
    	this.buf4.put(byteBuf4);
    	this.buf4.rewind();
    	
    	float result = this.buf4.getFloat();
      
        return result;
    }
     
    public double readDouble() throws IOException{
    	int readed = in.read(byteBuf8);
    	currentPosition += readed;
        this.buf8.clear();
    	this.buf8.put(byteBuf8);
    	this.buf8.rewind();
    	
    	double result = this.buf8.getDouble();
      
        return result;
    }
    
    public String readString(int byteLength) throws IOException{
    	byte[] buff = new byte[byteLength];
    	int readed = in.read(buff);
        currentPosition += readed;
        return new String(buff);	
    }
    
    public byte[] readBytes(int length) throws IOException{
    	byte[] buf = new byte[length];
    	int readed = in.read(buf);
    	currentPosition += readed;
        return buf;
    }
  
	public long length() throws IOException{
		return file.length();  
	}
	public void seek(long pos) throws IOException{
		if(currentPosition <= pos){
			while(currentPosition != pos){
				long skipped = in.skip(pos - currentPosition);
				currentPosition += skipped;
			}	
		}
		else{
			in.close();
			in = new BufferedInputStream(new FileInputStream(file));
			currentPosition = 0;
			seek(pos);
		}
			
	}
	
	public void close() throws IOException{
		in.close();
	}
	
	public void empty() throws IOException{
		in.close();
		file.delete();
		file = new File(absoluteFileName);
		file.createNewFile();
		in = new BufferedInputStream(new FileInputStream(file));
		currentPosition = 0;
	}
}



