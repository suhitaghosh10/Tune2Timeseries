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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class BufferedFileWriter
{
	protected File file;
	protected String absoluteFileName;
    protected BufferedOutputStream out;
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

    public BufferedFileWriter(File file) throws IOException{
    	if(!file.exists())
    		file.createNewFile();
    	this.file = file;
    	this.absoluteFileName = this.file.getAbsolutePath();
    	this.out = new BufferedOutputStream(new FileOutputStream(file , true));
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

    public void write8(short b) throws java.io.IOException{
    	buf1.clear();
    	buf1.put((byte)(b&0xff));
    	buf1.rewind();
    	out.write(buf1.array());
    }
   
    public void write16(int i) throws java.io.IOException{
    	buf2.clear();
    	buf2.putShort((short)(i&0xffff));
    	buf2.rewind();
    	out.write(buf2.array());
    }

    public void write32(long l)throws java.io.IOException{
    	buf4.clear();
    	buf4.putInt((int)(l&0xffffffff));
    	buf4.rewind();
    	out.write(buf4.array());
    }
    
    public void write64(long l) throws java.io.IOException{
    	buf8.clear();
    	buf8.putLong(l);
    	buf8.rewind();
    	out.write(buf8.array());
    }
    
    public void writeFloat(float f) throws IOException{
    	buf4.clear();
    	buf4.putFloat(f);
    	buf4.rewind();
    	out.write(buf4.array());
    }
     
    public void writeDouble(double d) throws IOException{
    	buf8.clear();
    	buf8.putDouble(d);
    	buf8.rewind();
    	out.write(buf8.array());
    }
    
    public void writeString(String str) throws IOException{
    	out.write(str.getBytes());
    }
   
    public void writeBytes(byte[] bytes) throws IOException{
    	out.write(bytes);
    }
    
	public long length() throws IOException{
		return file.length();  
	}
	
	public void flush()throws IOException{
		out.flush();
	}
	public void close() throws IOException{
		out.close();
	}
	
	public void empty() throws IOException{
		out.close();
		file.delete();
		file = new File(absoluteFileName);
		file.createNewFile();
		out = new BufferedOutputStream(new FileOutputStream(file , true));
	}
}




