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

import java.util.ArrayList;
import java.util.List;

import org.unisens.Entry;
import org.unisens.Group;
import org.unisens.Unisens;
import org.unisens.UnisensParseException;
import org.unisens.UnisensParseExceptionTypeEnum;
import org.unisens.ri.config.Constants;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GroupImpl implements Group, Constants {
	private String id = null;
	private String comment = null;
	
	private List<Entry> entries  = new ArrayList<Entry>();
	private Unisens unisens = null;
	
	public GroupImpl(Unisens unisens ,Node groupNode)throws UnisensParseException{
		this.unisens = unisens;
		this.parse(groupNode);
	}
	
	public GroupImpl(Unisens unisens , String id , String comment){
		this.id = id;
		this.comment = comment;
		this.unisens = unisens;
	}
	
	
	private void parse(Node groupNode) throws UnisensParseException{
		NamedNodeMap attr = groupNode.getAttributes();
		Node attrNode = attr.getNamedItem(GROUP_ID);
		id = (attrNode != null) ? attrNode.getNodeValue():"" ;
		attrNode = attr.getNamedItem(GROUP_COMMENT);
	    comment = (attrNode != null) ? attrNode.getNodeValue() : "";
	    
	    NodeList groupEntryNodes = groupNode.getChildNodes();
	    Node groupEntryNode;
	    int length = groupEntryNodes.getLength();
	    for(int i = 0 ; i < length ; i++){
	    	groupEntryNode = groupEntryNodes.item(i);
	    	if(groupEntryNode.getNodeType() == Node.ELEMENT_NODE){
	    		String entryId = groupEntryNode.getAttributes().getNamedItem(GROUPENTRY_REF).getNodeValue();
	    		Entry entry = unisens.getEntry(entryId);
	    		if(entry != null)
	    			entries.add(entry);
	    		else
	    			throw new UnisensParseException(String.format("Unvalid unisens.xml : the entry with id %s in group with id %s not exist!", entryId, getId()), UnisensParseExceptionTypeEnum.UNISENS_GROUP_ENTRY_NOT_EXIST);
	    	}
	    }
	}
	
	public void addEntry(Entry entry) {
		this.entries.add(entry);
	}

	
	public List<Entry> getEntries() {
		return entries;
	}

	public void removeEntry(Entry entry) {
		this.entries.remove(entry);
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
}
