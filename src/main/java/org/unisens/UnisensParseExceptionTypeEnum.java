/*
Unisens Interface - interface for a universal sensor data format
Copyright (C) 2008 FZI Research Center for Information Technology, Germany
                   Institute for Information Processing Technology (ITIV),
				   KIT, Germany

This file is part of the Unisens Interface. For more information, see
<http://www.unisens.org>

The Unisens Interface is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Unisens Interface is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the Unisens Interface. If not, see <http://www.gnu.org/licenses/>. 
*/

package org.unisens;

/**
 * UnisensParseExceptionTypeEnum is an enum of UnisensParseException types that can occur while
 * parsing an unisens.xml file.  
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public enum UnisensParseExceptionTypeEnum {
	UNISENS_GROUP_ENTRY_NOT_EXIST,
	UNISENS_INVALID
}
