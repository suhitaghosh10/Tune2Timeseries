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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.unisens.BinFileFormat;
import org.unisens.CsvFileFormat;
import org.unisens.CustomFileFormat;
import org.unisens.DuplicateIdException;
import org.unisens.Entry;
import org.unisens.FileFormat;
import org.unisens.Unisens;
import org.unisens.XmlFileFormat;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.AbstractReader;
import org.unisens.ri.io.AbstractWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Radoslav Nedkov
 * @email radi.nedkov@gmail.com
 *
 * 25.02.2007
 */
public abstract class EntryImpl implements Entry , Constants{
	protected Unisens unisens = null;
	protected FileFormat fileFormat = null;
	private String id = null;
	private String name = null;
	private String contentClass = null;
	private String source = null;
	private String sourceId = null;
	private String comment = null;
	
	private HashMap<String, String> customAttributes = new HashMap<String, String>();
	
	protected EntryImpl(Unisens unisens , Node entryNode){
		this.parse(entryNode);
		this.unisens = unisens;
	}

	public EntryImpl(Unisens unisens , String id){
		this.unisens = unisens;
		this.id = id;
		String absoluteFileName = unisens.getPath() + id;
	    try
	    {
	    	new FileOutputStream(absoluteFileName).close();
	    }
        catch (IOException e)
        {
        }	    	
	}
	
	protected EntryImpl(Entry entry){
		this.unisens = entry.getUnisens();
		this.id = entry.getId();
		this.contentClass = entry.getContentClass();
		this.source = entry.getSource();
		this.sourceId = entry.getSourceId();
		this.comment = entry.getComment();
		this.fileFormat = entry.getFileFormat().clone();
		HashMap<String, String> tmpCustomAttributes = entry.getCustomAttributes();
		for (Iterator<String> i = tmpCustomAttributes.keySet().iterator(); i.hasNext();)
		{
			String key = i.next();
			this.addCustomAttribute(key, tmpCustomAttributes.get(key));
		}
	}
	
	protected abstract AbstractReader getReader();
	protected abstract AbstractWriter getWriter();
	protected abstract boolean isReaderOpened();
	protected abstract boolean isWriterOpened();
	
	private void parse(Node entryNode){		
		NamedNodeMap attrs = entryNode.getAttributes();
		Node attrNode = attrs.getNamedItem(ENTRY_ID);
		id = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attrs.getNamedItem(ENTRY_NAME);
		name = (attrNode != null) ? attrNode.getNodeValue() : null;
	    attrNode = attrs.getNamedItem(ENTRY_SOURCE);
		source = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attrs.getNamedItem(ENTRY_SOURCE_ID);
		sourceId = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attrs.getNamedItem(ENTRY_CONTENTCLASS);
		contentClass = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attrs.getNamedItem(ENTRY_COMMENT);
		comment = (attrNode != null) ? attrNode.getNodeValue() : null;
		

		NodeList childNodes = entryNode.getChildNodes();
		Node childNode = null;
	
		for(int i = 0 ; i < childNodes.getLength() ; i++){
			childNode = childNodes.item(i);
			if(childNode.getNodeType() == Node.ELEMENT_NODE){
				String nodeName = childNode.getNodeName();
				if(nodeName.equalsIgnoreCase(BINFILEFORMAT))
					this.fileFormat = new BinFileFormatImpl(childNode);
				if(nodeName.equalsIgnoreCase(CSVFILEFORMAT))
					this.fileFormat = new CsvFileFormatImpl(childNode);
				if(nodeName.equalsIgnoreCase(XMLFILEFORMAT))
					this.fileFormat = new XmlFileFormatImpl(childNode);
				if(nodeName.equalsIgnoreCase(CUSTOMFILEFORMAT)){
					attrNode = childNode.getAttributes().getNamedItem(CUSTOMFILEFORMAT_FILEFORMATNAME);
					String fileFormatName =  (attrNode != null) ? attrNode.getNodeValue() : "CST";
					this.fileFormat = new CustomFileFormatImpl(childNode, fileFormatName);
				}
				if(nodeName.equalsIgnoreCase(CUSTOM_ATTRIBUTES)){
					NodeList customAttributeNodes = childNode.getChildNodes();
					for (int j = 0; j < customAttributeNodes.getLength(); j++) {
						Node customAttributeNode = customAttributeNodes.item(j);
						if(customAttributeNode.getNodeName().equalsIgnoreCase(CUSTOM_ATTRIBUTE)){
							attrs = customAttributeNode.getAttributes();
							Node keyNode = attrs.getNamedItem(CUSTOM_ATTRIBUTE_KEY);
							String key = "";
							if(keyNode != null){
								key = keyNode.getNodeValue();	
							}
							Node valueNode = attrs.getNamedItem(CUSTOM_ATTRIBUTE_VALUE);
							String value = "";
							if(valueNode != null){
								value = valueNode.getNodeValue();	
							}
							if(key != "")
								customAttributes.put(key, value);
						}
					}
				}
			}
		}
		return;
	}
	
	public Unisens getUnisens(){
		return unisens;
	}
	
	public void setUnisens(Unisens unisens){
		this.unisens = unisens;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentClass() {
		return contentClass;
	}
	public void setContentClass(String contentClass){
		this.contentClass = contentClass;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public HashMap<String, String> getCustomAttributes() {
		return customAttributes;
	}

	public void addCustomAttribute(String key, String value) {
		this.customAttributes.put(key, value);
	}
	
	public FileFormat getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(FileFormat fileFormat) {	
		this.fileFormat = fileFormat.clone();
	}
	
	public void rename(String newId) throws IOException, DuplicateIdException {	
		if(unisens.getEntry(newId) != null)
			throw new DuplicateIdException("Dublicate group id : " + newId);
		else{
			String fileFrom = unisens.getPath() + this.getId();
			String fileTo = unisens.getPath() + newId;
			
			File file = new File(fileFrom);
			if(file.exists())
				file.renameTo(new File(fileTo));
			this.setId(newId);
		}
	}
	
	public void close(){
		try{
			if(isReaderOpened())
				getReader().close();
			if(isWriterOpened())
				getWriter().close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BinFileFormat createBinFileFormat() {
		return new BinFileFormatImpl();
	}
	
	public CustomFileFormat createCustomFileFormat(String fileFormatName){
		return new CustomFileFormatImpl(fileFormatName);
	}

	public CsvFileFormat createCsvFileFormat() {
		return new CsvFileFormatImpl();
	}
	
	public XmlFileFormat createXmlFileFormat() {
		return new XmlFileFormatImpl();
	}
	
	public abstract Entry clone();
}
