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

import java.util.HashMap;

import org.unisens.CustomFileFormat;
import org.unisens.FileFormat;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CustomFileFormatImpl extends FileFormatImpl implements CustomFileFormat {
	private HashMap<String, String> attributes = new HashMap<String, String>();
	
	protected CustomFileFormatImpl(Node customFileFormatNode, String fileFormatName) {
		super(customFileFormatNode, fileFormatName);
		parse(customFileFormatNode);
	}
	
	protected CustomFileFormatImpl(CustomFileFormat customFileFormat){
		super(customFileFormat);
		attributes = (HashMap<String, String>)customFileFormat.getAttributes().clone();
	}

	public CustomFileFormatImpl(String fileFormatName) {
		super(fileFormatName);
	}
	
	private void parse(Node customFileFormatNode){
		NamedNodeMap attrs = customFileFormatNode.getAttributes();
		int length = attrs.getLength();
		for (int i = 0; i < length; i++){
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			if(!name.equalsIgnoreCase(FILEFORMAT_COMMENT) && !name.equalsIgnoreCase(CUSTOMFILEFORMAT_FILEFORMATNAME))
				this.attributes.put(name, value);
		}
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public FileFormat clone() {
		return new CustomFileFormatImpl(this);
	}

}
