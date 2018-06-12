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
import java.io.IOException;
import java.util.HashMap;

import org.unisens.CustomEntry;
import org.unisens.Unisens;
import org.unisens.ri.config.Constants;
import org.unisens.ri.io.AbstractReader;
import org.unisens.ri.io.AbstractWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CustomEntryImpl extends EntryImpl implements CustomEntry, Constants{
    private HashMap<String, String> attributes = new HashMap<String, String>();
	
    protected CustomEntryImpl(Unisens unisens , Node entryNode){
		super(unisens , entryNode);
		this.parse(entryNode);
		try {
			File file = new File(this.unisens.getPath() + File.separator + this.getId());
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    protected CustomEntryImpl(Unisens unisens , String id){
		super(unisens , id);
		this.fileFormat = new CustomFileFormatImpl("custom");
	}
    
    protected CustomEntryImpl(CustomEntry customEntry){
    	super(customEntry);
    	this.attributes = new HashMap<String, String>(customEntry.getAttributes());
    }
    
	private void parse(Node undefNode){
		NamedNodeMap attrs = undefNode.getAttributes();
		int lenght = attrs.getLength();
		Node attr;
		String name;
		String value;
		for(int i = 0 ; i < lenght ; i++){
			attr = attrs.item(i);
			name = attr.getNodeName();
			value = attr.getNodeValue();
			if(!name.equalsIgnoreCase(ENTRY_ID) && !name.equalsIgnoreCase(ENTRY_COMMENT)&& !name.equalsIgnoreCase(ENTRY_SOURCE)&& !name.equalsIgnoreCase(ENTRY_SOURCE_ID)&& !name.equalsIgnoreCase(ENTRY_CONTENTCLASS))
				attributes.put(name, value);
		}
	}

	
	public String getAttribute(String attributeName) {
		return (String)attributes.get(attributeName);
	}
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	protected AbstractReader getReader(){
		return null;
	}
	
	protected AbstractWriter getWriter(){
		return null;
	}

	@Override
	protected boolean isReaderOpened() {
		return false;
	}

	@Override
	protected boolean isWriterOpened() {
		return false;
	}
	@Override
	public CustomEntry clone() {
		return new CustomEntryImpl(this);
	}
}
