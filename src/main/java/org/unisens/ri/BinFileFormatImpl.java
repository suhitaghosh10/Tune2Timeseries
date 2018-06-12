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

import org.unisens.BinFileFormat;
import org.unisens.Endianess;
import org.unisens.FileFormat;
import org.w3c.dom.Node;


public class BinFileFormatImpl extends FileFormatImpl implements BinFileFormat {
	private Endianess endianess = Endianess.LITTLE;
	
	protected BinFileFormatImpl(Node binFileFormatNode){
		super(binFileFormatNode, "BIN");
		this.parse(binFileFormatNode);
	}
	
	protected BinFileFormatImpl(BinFileFormat binFileFormat){
		super(binFileFormat);
		this.endianess = binFileFormat.getEndianess();
	}
	
	public BinFileFormatImpl() {
		super("BIN");
	}
	
	private void parse(Node binFileFormatNode){
		Node attrNode = binFileFormatNode.getAttributes().getNamedItem(BINFILEFORMAT_ENDIANESS);
		this.endianess = (attrNode != null) ? Endianess.valueOf(attrNode.getNodeValue()) : Endianess.LITTLE;
	}
	
	public Endianess getEndianess() {
		return endianess;
	}

	public void setEndianess(Endianess endianess){
		this.endianess = endianess;		
	}

	@Override
	public FileFormat clone() {
		return new BinFileFormatImpl(this);
	}
	
	

}
