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

package org.unisens.ri;

import java.io.IOException;
import java.util.List;

import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.Unisens;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.EventIoFactory;
import org.unisens.ri.io.EventReader;
import org.unisens.ri.io.EventWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class EventEntryImpl extends TimedEntryImpl implements EventEntry,
		Constants
{
	private int typeLength = 0;
	private int commentLength = 0;

	private EventReader eventReader = null;
	private EventWriter eventWriter = null;


	protected EventEntryImpl(Unisens unisens, Node eventNode)
	{
		super(unisens, eventNode);
		this.parse(eventNode);
	}

	protected EventEntryImpl(Unisens unisens, String id, double sampleRate)
	{
		super(unisens, id, sampleRate);
		this.fileFormat = new CsvFileFormatImpl();
		this.typeLength = Constants.DEFAULT_EVENT_ENTRY_TYPE_LENGTH;
		this.commentLength = Constants.DEFAULT_EVENT_ENTRY_COMMENT_LENGTH;
	}

	protected EventEntryImpl(EventEntry eventEntry)
	{
		super(eventEntry);
		this.typeLength = eventEntry.getTypeLength();
		this.commentLength = eventEntry.getCommentLength();
	}

	private void parse(Node eventNode)
	{
		NamedNodeMap attrs = eventNode.getAttributes();
		Node attrNode = attrs.getNamedItem(ANNOTATIONENTRY_COMMENT_LENGTH);
		commentLength = (attrNode != null) ? Integer.parseInt(attrNode
				.getNodeValue()) : Constants.DEFAULT_EVENT_ENTRY_COMMENT_LENGTH;
		attrNode = attrs.getNamedItem(ANNOTATIONENTRY_TYPE_LENGTH);
		typeLength = (attrNode != null) ? Integer.parseInt(attrNode
				.getNodeValue()) : Constants.DEFAULT_EVENT_ENTRY_TYPE_LENGTH;
	}

	public void empty() throws IOException
	{
		this.close();
		this.getWriter().empty();
		this.count = 0;
	}

	public int getCommentLength()
	{
		return commentLength;
	}

	public int getTypeLength()
	{
		return typeLength;
	}

	public void setCommentLength(int commentLength)
	{
		this.commentLength = commentLength;
	}

	public void setTypeLength(int typeLength)
	{
		this.typeLength = typeLength;

	}

	public List<Event> read(int length) throws IOException
	{
		List<Event> result = this.getReader().read(length);
		this.getReader().close();
		return result;
	}


	public List<Event> read(long position, int length) throws IOException
	{
		List<Event> result = this.getReader().read(position, length);
		this.getReader().close();
		return result;
	}

	public void append(Event event) throws IOException
	{
		this.getWriter().append(event);
		this.getWriter().close();
		this.calculateCount(1);
	}


	public void append(List<Event> events) throws IOException,
			IllegalArgumentException
	{
		this.getWriter().append(events);
		this.getWriter().close();
		this.calculateCount(events.size());
	}

	@Override
	protected EventReader getReader()
	{
		if (this.eventReader == null)
			eventReader = EventIoFactory.createEventReader(this);
		return eventReader;
	}

	@Override
	protected EventWriter getWriter()
	{
		if (this.eventWriter == null)
			eventWriter = EventIoFactory.createEventWriter(this);
		return eventWriter;
	}

	@Override
	protected boolean isReaderOpened()
	{
		return eventReader != null;
	}

	@Override
	protected boolean isWriterOpened()
	{
		return eventWriter != null;
	}

	@Override
	public EventEntry clone()
	{
		return new EventEntryImpl(this);
	}

	public void resetPos()
	{
		getReader().resetPos();
	}
}
