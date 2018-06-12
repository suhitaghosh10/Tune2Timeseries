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

package org.unisens.ri.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.EventWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class EventXmlWriter extends EventWriter implements Constants
{
	private Document document;
	private Element root;

	public EventXmlWriter(EventEntry eventEntry) throws IOException
	{
		super(eventEntry);
		this.open();
	}

	@Override
	public void open() throws IOException
	{
		try
		{
			if (!isOpened)
			{
				DocumentBuilderFactory domFactory = DocumentBuilderFactory
						.newInstance();
				domFactory.setNamespaceAware(true); // never forget this!
				DocumentBuilder builder = domFactory.newDocumentBuilder();

				if (new File(absoluteFileName).length() != 0)
				{
					document = builder.parse(absoluteFileName);
					root = (Element) document.getElementsByTagName(
							EVENT_XML_READER_EVENTS_ELEMENT).item(0);
				}
				else
				{
					document = builder.newDocument();
					root = document
							.createElement(EVENT_XML_READER_EVENTS_ELEMENT);
					document.appendChild(root);
				}
				isOpened = true;
			}
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void append(Event event) throws IOException
	{
		open();
		try
		{
			// Truncate comments and types when necessary
			event.trim(this.eventEntry.getTypeLength(), this.eventEntry
					.getCommentLength());

			Element eventElement = document
					.createElement(EVENT_XML_READER_EVENT_ELEMENT);
			eventElement.setAttribute(EVENT_XML_READER_SAMPLESTAMP_ATTR, ""+
					event.getSampleStamp());
			eventElement.setAttribute(EVENT_XML_READER_TYPE_ATTR, event
					.getType());
			if (event.getComment() != "")
				eventElement.setAttribute(EVENT_XML_READER_COMMENT_ATTR, event
						.getComment());
			root.appendChild(eventElement);

			DOMSource source = new DOMSource(document);

			FileOutputStream out = new FileOutputStream(new File(
					absoluteFileName));
			StreamResult r = new StreamResult(out);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			transformer.transform(source, r);
			out.close();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void append(List<Event> events) throws IOException
	{
		try
		{
			for (Event event : events)
			{
				// Truncate comments and types when necessary
				if (event.getComment().length() > this.eventEntry
						.getCommentLength())
				{
					event.setComment(event.getComment().substring(0,
							this.eventEntry.getCommentLength()));
				}
				if (event.getType().length() > this.eventEntry.getTypeLength())
				{
					event.setType(event.getType().substring(0,
							this.eventEntry.getTypeLength()));
				}

				Element eventElement = document
						.createElement(EVENT_XML_READER_EVENT_ELEMENT);
				eventElement.setAttribute(EVENT_XML_READER_SAMPLESTAMP_ATTR, ""+
						event.getSampleStamp());
				eventElement.setAttribute(EVENT_XML_READER_TYPE_ATTR, event
						.getType());
				if (event.getComment() != "")
					eventElement.setAttribute(EVENT_XML_READER_COMMENT_ATTR,
							event.getComment());
				root.appendChild(eventElement);
			}

			DOMSource source = new DOMSource(document);

			FileOutputStream out = new FileOutputStream(new File(
					absoluteFileName));
			StreamResult r = new StreamResult(out);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			transformer.transform(source, r);
			out.close();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void empty() throws IOException
	{
		new File(absoluteFileName).delete();
		new File(absoluteFileName).createNewFile();
		this.isOpened = false;
	}

	@Override
	public void close() throws IOException
	{
		this.isOpened = false;
		this.document = null;
	}
}
