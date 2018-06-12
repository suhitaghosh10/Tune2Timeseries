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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.unisens.DataType;
import org.unisens.SignalEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.SignalWriter;
import org.unisens.ri.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class SignalXmlWriter extends SignalWriter implements Constants{
	private Document document;
	private Element root;
	//private DecimalFormat decimalFormat;
	
	public SignalXmlWriter(SignalEntry signalEntry) throws IOException{
		super(signalEntry);
		this.open();
	}
	
	@Override
	public void open() throws IOException {
		try {
			if(!isOpened){
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			    domFactory.setNamespaceAware(true); // never forget this!
			    DocumentBuilder builder = domFactory.newDocumentBuilder();
				
			    if(new File(absoluteFileName).length() != 0){
			    	document = builder.parse(absoluteFileName);
			    	root = (Element)document.getElementsByTagName(SIGNAL_XML_READER_SIGNAL_ELEMENT).item(0);
				}else{
			    	document = builder.newDocument();
			    	root = document.createElement(SIGNAL_XML_READER_SIGNAL_ELEMENT);
			    	document.appendChild(root);
			    }
			    isOpened = true;
			}
		}catch(ParserConfigurationException e) {
			e.printStackTrace();
		}catch (SAXException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void append(Object data) throws IOException {
		open();
		
		data = Utilities.convertFrom1DimTo2DimArray(data, channelCount);
		
		try {
			if(dataType == DataType.INT8)
				appendData((byte[][])data);
			
			if(dataType == DataType.INT16)
				appendData((short[][])data);
			
			if(dataType == DataType.UINT8){
				if(data instanceof byte[][])
					appendData(Utilities.convertUnsignedByteToShort((byte[][])data));
				else
					appendData((short[][])data);
			}
			
			if(dataType == DataType.INT32)
				appendData((int[][])data);
			
			if(dataType == DataType.UINT16){
				if(data instanceof short[][])
					appendData(Utilities.convertUnsignedShortToInteger((short[][])data));
				else
					appendData((int[][])data);
			}
			
			if(dataType == DataType.UINT32){
				if(data instanceof int[][])
					appendData(Utilities.convertUnsignedIntegerToLong((int[][])data));
				else
					appendData((long[][])data);
			}
			
			if(dataType == DataType.FLOAT)
				appendData((float[][])data);
			
			if(dataType == DataType.DOUBLE)
				appendData((double[][])data);
			
		    DOMSource source = new DOMSource(document);
		    
			FileOutputStream out = new FileOutputStream(new File(absoluteFileName));
			StreamResult r = new StreamResult(out);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			transformer.transform(source, r);
			out.close();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	private void appendData(byte[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}
	
	private void appendData(short[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}
	
	private void appendData(int[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}
	
	private void appendData(long[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}
	
	private void appendData(float[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}
	
	private void appendData(double[][] data){
		Element sampleElement;
		Node dataNode;
		for(int i = 0 ; i < data.length; i++){
			sampleElement = document.createElement(SIGNAL_XML_READER_SAMPLE_ELEMENT);
			for (int j = 0; j < data[0].length; j++) {
				dataNode = sampleElement.appendChild(document.createElement(SIGNAL_XML_READER_DATA_ELEMENT));
				dataNode.setTextContent("" + data[i][j]);
			}
			root.appendChild(sampleElement);
		}
	}

	@Override
	public void close() throws IOException {
		this.isOpened = false;
		this.document = null;
	}

}
