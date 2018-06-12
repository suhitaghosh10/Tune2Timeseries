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

import org.unisens.TimedEntry;
import org.unisens.Unisens;
import org.unisens.ri.config.Constants;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class TimedEntryImpl extends EntryImpl implements TimedEntry, Constants {
	protected long count = -1;
	protected double sampleRate = 0;
	public TimedEntryImpl(Unisens unisens, Node timedEntryNode) {
		super(unisens, timedEntryNode);
		parse(timedEntryNode);
		
	}

	public TimedEntryImpl(Unisens unisens, String id, double sampleRate) {
		super(unisens, id);
		this.sampleRate = sampleRate;
	}
	
	protected TimedEntryImpl(TimedEntry timedEntry){
		super(timedEntry);
		this.sampleRate = timedEntry.getSampleRate();
	}
	
	private void parse(Node timedEntryNode){
		NamedNodeMap attrs = timedEntryNode.getAttributes();
		Node attrNode = attrs.getNamedItem(TIMEDENTRY_SAMPLERATE);
		sampleRate = (attrNode != null) ? Double.parseDouble(attrNode.getNodeValue()): 0;
		return;
	}
	
	public long getCount() {
		if(count == -1)
			calculateCount();
		return count;
	}
	
	protected void calculateCount(){
		count = this.getReader().getSampleCount();
	}
	protected void calculateCount(int increase){
		if(count == -1)
			calculateCount();
		else
			count += increase;
	}

	public double getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}
}
