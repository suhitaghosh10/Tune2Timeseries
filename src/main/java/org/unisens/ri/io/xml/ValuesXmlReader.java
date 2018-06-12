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
import org.unisens.Value;
import org.unisens.ValueList;
import org.unisens.ValuesEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.ValuesReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ValuesXmlReader extends ValuesReader implements Constants{
	private Document document;
	private XPath xpath;
	private XPathExpression expr;
	private NodeList valueNodes;
	
	public ValuesXmlReader(ValuesEntry valuesEntry) throws IOException{
		super(valuesEntry);
		open();
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
	public Value[] read(int length) throws IOException {
		return read(currentSample, length, false);
	}
	
	@Override
	public Value[] read(long pos, int length) throws IOException{
		return read(pos, length, false);
	}
	
	@Override
	public Value[] readScaled(int length) throws IOException {
		return read(currentSample, length, true);
	}

	@Override
	public Value[] readScaled(long pos, int length) throws IOException {
		return read(pos, length, true);
	}

	private Value[] read(long pos, int length, boolean scaled) throws IOException {
		try{
			expr = xpath.compile(String.format(VALUES_XML_READER_VALUES_PATH, pos, pos + length + 1));
			valueNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			if(scaled){
				if(dataType == DataType.INT8)
					return convertValueNodeListToValueByteArrayScaled(valueNodes);
				if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
					return convertValueNodeListToValueShortArrayScaled(valueNodes);
				if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
					return convertValueNodeListToValueIntArrayScaled(valueNodes);
				if(dataType == DataType.UINT32)
					return convertValueNodeListToValueLongArrayScaled(valueNodes);
				if(dataType == DataType.FLOAT)
					return convertValueNodeListToValueFloatArrayScaled(valueNodes);
				if(dataType == DataType.DOUBLE)
					return convertValueNodeListToValueDoubleArrayScaled(valueNodes);
			}else{
				if(dataType == DataType.INT8)
					return convertValueNodeListToValueByteArray(valueNodes);
				if(dataType == DataType.INT16 || (dataType == DataType.UINT8))
					return convertValueNodeListToValueShortArray(valueNodes);
				if(dataType == DataType.INT32 || (dataType == DataType.UINT16))
					return convertValueNodeListToValueIntArray(valueNodes);
				if(dataType == DataType.UINT32)
					return convertValueNodeListToValueLongArray(valueNodes);
				if(dataType == DataType.FLOAT)
					return convertValueNodeListToValueFloatArray(valueNodes);
				if(dataType == DataType.DOUBLE)
					return convertValueNodeListToValueDoubleArray(valueNodes);
			}
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ValueList readValuesList(int length) throws IOException {
		return readValuesList(currentSample, length);
	}

	@Override
	public ValueList readValuesList(long pos, int length) throws IOException {
		Value[] values = read(pos, length);
		return convertValueArrayToValueList(values, false);
	}

	@Override
	public ValueList readValuesListScaled(int length) throws IOException {
		return readValuesListScaled(currentSample, length);
	}

	@Override
	public ValueList readValuesListScaled(long pos, int length) throws IOException {
		Value[] values = readScaled(pos, length);
		return convertValueArrayToValueList(values, true);
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
			expr = xpath.compile(String.format(VALUES_XML_READER_VALUES_PATH, 0, Long.MAX_VALUE));
			valueNodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			return valueNodes.getLength();
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private Value[] convertValueNodeListToValueByteArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			byte[] data = new byte[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Byte.parseByte(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueShortArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			short[] data = new short[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Short.parseShort(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueIntArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			int[] data = new int[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Integer.parseInt(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueLongArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			long[] data = new long[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Long.parseLong(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueFloatArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			float[] data = new float[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Float.parseFloat(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueDoubleArray(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = Double.parseDouble(datas.item(j).getTextContent());
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueByteArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Byte.parseByte(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueShortArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Short.parseShort(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueIntArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Integer.parseInt(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueLongArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Long.parseLong(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueFloatArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Float.parseFloat(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private Value[] convertValueNodeListToValueDoubleArrayScaled(NodeList valueNodes) throws XPathExpressionException{
		Value[] values = new Value[valueNodes.getLength()];
		expr = xpath.compile(VALUES_XML_READER_VALUES_DATA_PATH);
		for (int i = 0; i < valueNodes.getLength(); i++) {
			NodeList datas = (NodeList)expr.evaluate(valueNodes.item(i), XPathConstants.NODESET);
			double[] data = new double[datas.getLength()];
			for (int j = 0; j < datas.getLength(); j++)
				data[j] = (Double.parseDouble(datas.item(j).getTextContent()) - baseline) * lsbValue;
			values[i] = new Value(Long.parseLong(valueNodes.item(i).getAttributes().getNamedItem(VALUES_XML_READER_SAMPLESTAMP_ATTR).getNodeValue()), data);
		}
		return values;
	}
	
	private ValueList convertValueArrayToValueList(Value[] values, boolean scaled){
		ValueList valueList = new ValueList();
		long[] samplestamp = new long[values.length];
		if(scaled){
			double[][] valuesData = new double[values.length][((double[])values[0].getData()).length];
			for (int i = 0; i < values.length; i++) {
				samplestamp[i] = values[i].getSampleStamp();
				double[] valueData = (double[])values[i].getData();
				System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
			}
			valueList.setSamplestamps(samplestamp);
			valueList.setData(valuesData);
			return valueList;
		}else{
			if(dataType == DataType.INT8){
				byte[][] valuesData = new byte[values.length][((byte[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					byte[] valueData = (byte[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
			if(dataType == DataType.INT16 || (dataType == DataType.UINT8)){
				short[][] valuesData = new short[values.length][((short[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					short[] valueData = (short[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
			if(dataType == DataType.INT32 || (dataType == DataType.UINT16)){
				int[][] valuesData = new int[values.length][((int[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					int[] valueData = (int[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
			if(dataType == DataType.UINT32){
				long[][] valuesData = new long[values.length][((long[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					long[] valueData = (long[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
			if(dataType == DataType.FLOAT){
				float[][] valuesData = new float[values.length][((float[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					float[] valueData = (float[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
			if(dataType == DataType.DOUBLE){
				double[][] valuesData = new double[values.length][((double[])values[0].getData()).length];
				for (int i = 0; i < values.length; i++) {
					samplestamp[i] = values[i].getSampleStamp();
					double[] valueData = (double[])values[i].getData();
					System.arraycopy(valueData, 0, valuesData[i], 0, valueData.length);
				}
				valueList.setSamplestamps(samplestamp);
				valueList.setData(valuesData);
				return valueList;
			}
		}
		return null;
	}
}
