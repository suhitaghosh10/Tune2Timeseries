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

import org.unisens.Context;
import org.unisens.ri.config.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class ContextImpl implements Context, Constants{
		private String schemaUrl = null;
		
		protected ContextImpl(Node contextNode){
			this.parse(contextNode);
		}
		
		protected ContextImpl(String schemaUrl){
			this.schemaUrl = schemaUrl;
		}
		
		private void parse(Node contextNode){
			NamedNodeMap attrs = contextNode.getAttributes();
			Node attrNode = attrs.getNamedItem(CONTEXT_SCHEMAURL);
			this.schemaUrl = (attrNode != null) ? attrNode.getNodeValue() : null;
		}
		
		protected Element createElement(Document document){
			Element context = document.createElement(CONTEXT);
			if(getSchemaUrl() != null)
				context.setAttribute(CONTEXT_SCHEMAURL, getSchemaUrl());
			
			return context;
		}
		
		public String getSchemaUrl() {
			return schemaUrl;
		}
		public void setSchemaUrl(String schemaUrl) {
			this.schemaUrl = schemaUrl;
		}
		
		
}
