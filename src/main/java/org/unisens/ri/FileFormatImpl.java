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

import org.unisens.FileFormat;
import org.unisens.ri.config.Constants;
import org.w3c.dom.Node;

public abstract class FileFormatImpl implements FileFormat, Constants {
	private String comment = null;
	private String fileFormatName = null;
	
	protected FileFormatImpl(Node fileFormatNode , String fileFormatName){
		this.fileFormatName = fileFormatName;
		this.parse(fileFormatNode);
	}
	
	private void parse(Node fileFormatNode){
		Node attrNode = fileFormatNode.getAttributes().getNamedItem(FILEFORMAT_COMMENT);
		this.comment = (attrNode != null) ? attrNode.getNodeValue() : null;
	}
	
	protected FileFormatImpl(String fileFormatName){
		this.fileFormatName = fileFormatName;
	}
	protected FileFormatImpl(FileFormat fileFormat ){
		this.fileFormatName = fileFormat.getFileFormatName();
		this.comment = fileFormat.getComment();
	}
	
	public String getComment() {
		return comment;
	}

	public String getFileFormatName(){
		return this.fileFormatName;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public abstract FileFormat clone();

}
