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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.unisens.DataType;
import org.unisens.SignalEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.SignalReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SignalXmlReader extends SignalReader implements Constants{
	private Document document;
	private XPath xpath;
	private XPathExpression expr;
	private NodeList sampleNodes;
	
	public SignalXmlReader(SignalEntry signalEntry) throws IOException{
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
	public Object read(int length) throws IOException {
		return read(length, false);
	}

	@Override
	public Object read(long pos, int length) throws IOException {
		return read(pos, length, false);
	}

	@Override
	public double[][] readScaled(int length) throws IOException {
		return (double[][])read(length, true);
	}

	@Override
	public double[][] readScaled(long pos, int length) throws IOException {
		return (double[][])read(pos, length, true);
	}

	private Object read(int length, boolean scaled) throws IOException {
		return read(currentSample, length, scaled);
	}
	
	
	private Object read(long pos, int length, boolean scaled) throws IOException {
		try{
			expr = xpath.compile(String.format(SIGNAL_XML_READER_SAMPLES_PATH, pos, pos + length + 1));
			sampleNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			return convertSampleNodeListToArray(sampleNodes, scaled);
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public void resetPos() {
		this.currentSample = 0;
	}

	@Override
	public void close() throws IOException {
		this.isOpened = false;
	}

	@Override
	public long getSampleCount() {
		try{
			expr = xpath.compile(String.format(SIGNAL_XML_READER_SAMPLES_PATH, 0, Long.MAX_VALUE));
			sampleNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			return sampleNodes.getLength();
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private Object convertSampleNodeListToArray(NodeList sampleNodes, boolean scaled) throws XPathExpressionException{
		if(scaled){
			if(dataType == DataType.INT8)
				return convertSampleNodeListToByteArrayScaled(sampleNodes);
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
				return convertSampleNodeListToShortArrayScaled(sampleNodes);
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
				return convertSampleNodeListToIntArrayScaled(sampleNodes);
			if(dataType == DataType.UINT32)
				return convertSampleNodeListToLongArrayScaled(sampleNodes);
			if(dataType == DataType.FLOAT)
				return convertSampleNodeListToFloatArrayScaled(sampleNodes);
			if(dataType == DataType.DOUBLE)
				return convertSampleNodeListToDoubleArrayScaled(sampleNodes);
		}else{
			if(dataType == DataType.INT8)
				return convertSampleNodeListToByteArray(sampleNodes);
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
				return convertSampleNodeListToShortArray(sampleNodes);
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
				return convertSampleNodeListToIntArray(sampleNodes);
			if(dataType == DataType.UINT32)
				return convertSampleNodeListToLongArray(sampleNodes);
			if(dataType == DataType.FLOAT)
				return convertSampleNodeListToFloatArray(sampleNodes);
			if(dataType == DataType.DOUBLE)
				return convertSampleNodeListToDoubleArray(sampleNodes);
		}
		return null;
	}
	
	private Object convertSampleNodeListToByteArray(NodeList sampleNodes)throws XPathExpressionException{
		byte[][] result = new byte[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Byte.parseByte(datas.item(j).getTextContent());
		}
		return result;	}
	
	private Object convertSampleNodeListToShortArray(NodeList sampleNodes) throws XPathExpressionException{
		short[][] result = new short[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Short.parseShort(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private Object convertSampleNodeListToIntArray(NodeList sampleNodes) throws XPathExpressionException{
		int[][] result = new int[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Integer.parseInt(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private Object convertSampleNodeListToLongArray(NodeList sampleNodes) throws XPathExpressionException{
		long[][] result = new long[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Long.parseLong(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private Object convertSampleNodeListToFloatArray(NodeList sampleNodes) throws XPathExpressionException{
		float[][] result = new float[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Float.parseFloat(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private Object convertSampleNodeListToDoubleArray(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Double.parseDouble(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private double[][] convertSampleNodeListToByteArrayScaled(NodeList sampleNodes)throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = (Byte.parseByte(datas.item(j).getTextContent()) - baseline) * lsbValue;
		}
		return result;	}
	
	private double[][] convertSampleNodeListToShortArrayScaled(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = (Short.parseShort(datas.item(j).getTextContent()) - baseline) * lsbValue;
		}
		return result;
	}
	
	private double[][] convertSampleNodeListToIntArrayScaled(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = Integer.parseInt(datas.item(j).getTextContent());
		}
		return result;
	}
	
	private double[][] convertSampleNodeListToLongArrayScaled(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = (Long.parseLong(datas.item(j).getTextContent()) - baseline) * lsbValue;
		}
		return result;
	}
	
	private double[][] convertSampleNodeListToFloatArrayScaled(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = (Float.parseFloat(datas.item(j).getTextContent()) - baseline) * lsbValue;
		}
		return result;
	}
	
	private double[][] convertSampleNodeListToDoubleArrayScaled(NodeList sampleNodes) throws XPathExpressionException{
		double[][] result = new double[sampleNodes.getLength()][channelCount];
		expr = xpath.compile(SIGNAL_XML_READER_SAMPLES_DATA_PATH);
		for (int i = 0; i < sampleNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(sampleNodes.item(i), XPathConstants.NODESET);
			for (int j = 0; j < datas.getLength(); j++)
				result[i][j] = (Double.parseDouble(datas.item(j).getTextContent()) - baseline) * lsbValue;
		}
		return result;
	}

}
