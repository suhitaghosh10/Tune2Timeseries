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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.unisens.Event;
import org.unisens.EventEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.EventReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EventXmlReader extends EventReader implements Constants{
	private Document document;
	private XPath xpath;
	private XPathExpression expr;
	private NodeList eventNodes;
	
	public EventXmlReader(EventEntry eventEntry) throws IOException{
		super(eventEntry);
		this.open();
	}
	
	@Override
	public void open() throws IOException {
		try {
			if(!isOpened){
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			    domFactory.setNamespaceAware(true); // never forget this!
			    DocumentBuilder builder = domFactory.newDocumentBuilder();
			    document = builder.parse(absoluteFileName);

			    XPathFactory factory = XPathFactory.newInstance();
			    xpath = factory.newXPath();
			}
		}catch(ParserConfigurationException e) {
			e.printStackTrace();
		}catch (SAXException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public long getSampleCount() {
		try{
			expr = xpath.compile(String.format(EVENT_XML_READER_EVENTS_PATH, 0, Long.MAX_VALUE));
			eventNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			return eventNodes.getLength();
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void close() throws IOException {
		this.isOpened = false;
	}

	@Override
	public List<Event> read(int length) throws IOException {
		return read(currentSample, length);
	}

	@Override
	public List<Event> read(long position, int length) throws IOException {
		try{
			List<Event> events = new ArrayList<Event>();
			expr = xpath.compile(String.format(EVENT_XML_READER_EVENTS_PATH, position, position + length + 1));
			eventNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			for(int i = 0 ; i < eventNodes.getLength() ; i++){
				Node eventNode = eventNodes.item(i);
				NamedNodeMap attrs = eventNode.getAttributes();
				Node attrNode = attrs.getNamedItem(EVENT_XML_READER_SAMPLESTAMP_ATTR);
				long samplestamp = Long.parseLong((attrNode != null) ? attrNode.getNodeValue() : "0");
				attrNode = attrs.getNamedItem(EVENT_XML_READER_TYPE_ATTR);
				String type = (attrNode != null) ? attrNode.getNodeValue() : "";
				attrNode = attrs.getNamedItem(EVENT_XML_READER_COMMENT_ATTR);
				String comment = (attrNode != null) ? attrNode.getNodeValue() : "";

				Event event = new Event(samplestamp, type, comment);
				//event.trim(this.eventEntry.getTypeLength(), this.eventEntry.getCommentLength());
				events.add(event);
			}
			
			
			return events;
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void resetPos()
	{
		this.currentSample = 0;
	}

}
