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
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.ValuesWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ValuesXmlWriter extends ValuesWriter implements Constants{
	private Document document;
	private Element root;
	
	public ValuesXmlWriter(ValuesEntry valuesEntry) throws IOException{
		super(valuesEntry);
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
			    	root = (Element)document.getElementsByTagName(VALUES_XML_READER_VALUES_ELEMENT).item(0);
			    }
			    else{
			    	document = builder.newDocument();
			    	root = document.createElement(VALUES_XML_READER_VALUES_ELEMENT);
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
	public void append(Value value) throws IOException {
		open();
		try {
			if(dataType == DataType.INT8)
				root.appendChild(createValueElement(value.getSampleStamp(), (byte[])value.getData()));
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
				root.appendChild(createValueElement(value.getSampleStamp(), (short[])value.getData()));
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
				root.appendChild(createValueElement(value.getSampleStamp(), (int[])value.getData()));
			if(dataType == DataType.UINT32)
				root.appendChild(createValueElement(value.getSampleStamp(), (long[])value.getData()));
			if(dataType == DataType.FLOAT)
				root.appendChild(createValueElement(value.getSampleStamp(), (float[])value.getData()));
			if(dataType == DataType.DOUBLE)
				root.appendChild(createValueElement(value.getSampleStamp(), (double[])value.getData()));
			
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

	@Override
	public void append(Value[] values) throws IOException {
		open();
		try {
			if(dataType == DataType.INT8)
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (byte[])values[i].getData()));
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (short[])values[i].getData()));
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (int[])values[i].getData()));
			if(dataType == DataType.UINT32)
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (long[])values[i].getData()));
			if(dataType == DataType.FLOAT)
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (float[])values[i].getData()));
			if(dataType == DataType.DOUBLE)
				for(int i = 0; i < values.length ; i++)
					root.appendChild(createValueElement(values[i].getSampleStamp(), (double[])values[i].getData()));
		    
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

	@Override
	public void appendValuesList(ValueList valueList) throws IOException {
		open();
		try {
			if(dataType == DataType.INT8){
				long[] samplestamps = valueList.getSamplestamps();
				byte[][] valuesDate = (byte[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8)){
				long[] samplestamps = valueList.getSamplestamps();
				short[][] valuesDate = (short[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16)){
				long[] samplestamps = valueList.getSamplestamps();
				int[][] valuesDate = (int[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			if(dataType == DataType.UINT32){
				long[] samplestamps = valueList.getSamplestamps();
				long[][] valuesDate = (long[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			if(dataType == DataType.FLOAT){
				long[] samplestamps = valueList.getSamplestamps();
				float[][] valuesDate = (float[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			if(dataType == DataType.DOUBLE){
				long[] samplestamps = valueList.getSamplestamps();
				double[][] valuesDate = (double[][])valueList.getData();
				for(int i = 0; i < valueList.getSamplestamps().length ; i++)
					root.appendChild(createValueElement(samplestamps[i], valuesDate[i]));
			}
			
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

	@Override
	public void close() throws IOException {
		this.document = null;
		this.isOpened = false;
	}
	
	private Element createValueElement(long samplestamp, byte[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}
	
	private Element createValueElement(long samplestamp, short[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}
	
	private Element createValueElement(long samplestamp, int[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}
	
	private Element createValueElement(long samplestamp, long[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}
	
	private Element createValueElement(long samplestamp, float[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}
	
	private Element createValueElement(long samplestamp, double[] data){
		Element valueElement = document.createElement(VALUES_XML_READER_VALUE_ELEMENT);
		valueElement.setAttribute(VALUES_XML_READER_SAMPLESTAMP_ATTR, "" + samplestamp);
		for(int i = 0 ; i < data.length; i++){
			Node dataNode = valueElement.appendChild(document.createElement(VALUES_XML_READER_DATA_ELEMENT));
			dataNode.setTextContent("" + data[i]);	
		}
		return valueElement;
	}

}
