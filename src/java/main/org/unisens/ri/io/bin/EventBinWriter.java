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
import java.util.List;

import org.unisens.BinFileFormat;
import org.unisens.Endianess;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.EventWriter;

public class EventBinWriter extends EventWriter
{
	private BufferedFileWriter file = null;
	private int commentLength = -1;
	private int typeLength = -1;

	public EventBinWriter(EventEntry eventEntry) throws IOException
	{
		super(eventEntry);
		this.commentLength = this.eventEntry.getCommentLength();
		this.typeLength = this.eventEntry.getTypeLength();
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			this.file = new BufferedFileWriter(new File(absoluteFileName));
			this.isOpened = true;
			if (((BinFileFormat) this.eventEntry.getFileFormat())
					.getEndianess() == Endianess.BIG)
				this.file.setEndian(ByteOrder.BIG_ENDIAN);
			else
				this.file.setEndian(ByteOrder.LITTLE_ENDIAN);
			this.file.setSigned(true);
		}
	}

	public void close() throws IOException
	{
		if (this.isOpened)
			file.close();
		this.isOpened = false;
	}

	public void empty() throws IOException
	{
		open();
		file.empty();
	}

	public void append(Event event) throws IOException
	{
		open();
		this.appendT(event);
		file.flush();
	}


	public void append(List<Event> events) throws IOException
	{
		open();
		for (Event event : events)
			this.appendT(event);
		file.flush();
	}


	private void appendT(Event event) throws IOException
	{
		event.trim(this.eventEntry.getTypeLength(), this.eventEntry
				.getCommentLength());

		byte[] type = event.getType().getBytes();
		if (type.length < this.typeLength)
		{
			byte[] temp = new byte[typeLength];
			System.arraycopy(type, 0, temp, 0, type.length);
			for (int i = type.length; i < temp.length; i++)
				temp[i] = 0;
			type = temp;
		}
		else
		{
			if (type.length > this.typeLength)
			{
				byte[] temp = new byte[typeLength];
				System.arraycopy(type, 0, temp, 0, type.length);
				type = temp;
			}
		}

		byte[] comment = event.getComment().getBytes();
		if (comment.length < commentLength)
		{
			byte[] temp = new byte[commentLength];
			System.arraycopy(comment, 0, temp, 0, comment.length);
			for (int i = comment.length; i < temp.length; i++)
				temp[i] = 0;
			comment = temp;
		}
		else
		{
			if (comment.length > commentLength)
			{
				byte[] temp = new byte[commentLength];
				System.arraycopy(comment, 0, temp, 0, commentLength);
				comment = temp;
			}
		}
		long value = event.getSampleStamp();
		this.file.write64(value);
		this.file.writeBytes(type);
		this.file.writeBytes(comment);
		return;
	}

}
