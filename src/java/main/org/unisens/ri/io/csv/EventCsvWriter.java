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
import java.util.List;

import org.unisens.CsvFileFormat;
import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.io.BufferedFileWriter;
import org.unisens.ri.io.EventWriter;

public class EventCsvWriter extends EventWriter
{
	private BufferedFileWriter file = null;
	private String separator = null;
	private static String NEWLINE = System.getProperty("line.separator");


	public EventCsvWriter(EventEntry eventEntry) throws IOException
	{
		super(eventEntry);
		this.open();
	}

	public void open() throws IOException
	{
		if (!isOpened)
		{
			file = new BufferedFileWriter(new File(this.absoluteFileName));
			this.isOpened = true;
			separator = ((CsvFileFormat) eventEntry.getFileFormat())
					.getSeparator();
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

		// Truncate comments and types when necessary
		event.trim(this.eventEntry.getTypeLength(), this.eventEntry.getCommentLength());

		file.writeString(event.getSampleStamp() + this.separator
				+ event.getType() + this.separator + event.getComment()
				+ NEWLINE);
		file.flush();
	}

	public void append(List<Event> events) throws IOException
	{
		open();
		for (Event event : events)
		{
			// Truncate comments and types when necessary
			event.trim(this.eventEntry.getTypeLength(), this.eventEntry.getCommentLength());

			file.writeString(event.getSampleStamp() + this.separator
					+ event.getType() + this.separator + event.getComment()
					+ NEWLINE);
		}
		file.flush();
	}
}
