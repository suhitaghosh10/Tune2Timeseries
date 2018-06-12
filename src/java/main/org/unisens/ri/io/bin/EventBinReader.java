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
import java.util.ArrayList;
import java.util.List;

import org.unisens.BinFileFormat;
import org.unisens.Endianess;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.io.BufferedFileReader;
import org.unisens.ri.io.EventReader;

public class EventBinReader extends EventReader
{
	private BufferedFileReader file = null;
	protected int commentLength = -1;
	protected int typeLength = -1;
	protected int bytePerEvent = 0;
	protected int timestampTypeLength = 8;

	public EventBinReader(EventEntry eventEntry) throws IOException
	{
		super(eventEntry);
		this.commentLength = this.eventEntry.getCommentLength();
		this.typeLength = this.eventEntry.getTypeLength();
		this.bytePerEvent = this.commentLength + this.typeLength
				+ this.timestampTypeLength;
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			this.file = new BufferedFileReader(new File(absoluteFileName));
			if (((BinFileFormat) this.eventEntry.getFileFormat())
					.getEndianess() == Endianess.BIG)
				this.file.setEndian(ByteOrder.BIG_ENDIAN);
			else
				this.file.setEndian(ByteOrder.LITTLE_ENDIAN);
			this.file.setSigned(true);
			this.isOpened = true;
			this.currentSample = 0;
		}
	}

	public void close() throws IOException
	{
		if (this.isOpened)
			file.close();
		this.isOpened = false;
	}

	public List<Event> read(int length) throws IOException
	{
		return this.read(currentSample, length);
	}


	public List<Event> read(long pos, int length) throws IOException
	{
		open();
		if (pos != currentSample)
		{
			if (pos > eventEntry.getCount())
				return null;
			if (pos < 0)
				pos = 0;
			currentSample = pos;
			file.seek(currentSample * bytePerEvent);
		}

		if (currentSample + length > eventEntry.getCount())
			length = (int) (eventEntry.getCount() - currentSample);

		return this.readEvents(length);

	}

	private List<Event> readEvents(int length) throws IOException
	{
		List<Event> events = new ArrayList<Event>();
		Event event;
		long samplestamp;
		String type;
		String comment;
		for (int i = 0; i < length; i++)
		{
			samplestamp = (long) file.read64();
			type = file.readString(typeLength).trim();
			comment = file.readString(commentLength).trim();
			event = new Event(samplestamp, type, comment);
			events.add(event);
		}
		close();
		return events;
	}

	public long getSampleCount()
	{
		try
		{
			open();
			return this.file.length() / (bytePerEvent);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return -1;
		}
	}

	@Override
	public void resetPos()
	{
		try
		{
			open();
			file.seek(0);
			currentSample = 0;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
