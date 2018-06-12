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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.unisens.CsvFileFormat;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.io.EventReader;

public class EventCsvReader extends EventReader
{
	private BufferedReader file = null;
	private String separator;


	public EventCsvReader(EventEntry eventEntry) throws IOException
	{
		super(eventEntry);
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			file = new BufferedReader(new FileReader(this.absoluteFileName));
			this.separator = ((CsvFileFormat) eventEntry.getFileFormat())
					.getSeparator();
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

	public List<Event> read(int length) throws IOException
	{
		return this.read(currentSample, length);
	}


	public List<Event> read(long pos, int length) throws IOException
	{
		open();
		long sampleCount = eventEntry.getCount();
		if (pos > sampleCount)
			return null;

		if (pos != currentSample)
			seek(pos);

		if (pos + length > sampleCount)
			length = (int) (sampleCount - pos);

		return this.readLong(length);
	}


	private List<Event> readLong(int numberOfEvents) throws IOException
	{
		List<Event> events = new ArrayList<Event>();
		String currentEvent;
		StringTokenizer tokenizer;
		long timestamp = -1;
		String type = "";
		String comment = "";
		while (numberOfEvents > 0)
		{
			currentEvent = file.readLine();
			tokenizer = new StringTokenizer(currentEvent, separator);
			if (tokenizer.hasMoreTokens())
				timestamp = Long.valueOf(tokenizer.nextToken()).longValue();
			if (tokenizer.hasMoreTokens())
				type = tokenizer.nextToken();
			if (tokenizer.hasMoreTokens())
				comment = tokenizer.nextToken();
			
			Event event = new Event(timestamp, type, comment);
			//event.trim(this.eventEntry.getTypeLength(), this.eventEntry.getCommentLength());
			events.add(event);

			type = "";
			comment = "";
			timestamp = -1;
			numberOfEvents--;
			currentSample++;
		}
		close();
		return events;
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

	public void resetPos()
	{
		currentSample = 0;

		try
		{
			close();
			open();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			return 0;
		}
	}
}
