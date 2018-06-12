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
 * UnisensParseException represents specific Exceptions that may occur while
 * parsing the unisens.xml file of a unisens DataSet.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 * 
 */
public class UnisensParseException extends Exception
{
	private static final long serialVersionUID = 2344359345923985L;
	private UnisensParseExceptionTypeEnum unisensParseExceptionType;

	/**
	 * UnisensParseException represents specific Exceptions that may occur while
	 * parsing the unisens.xml file of a unisens DataSet.
	 * 
	 * @param unisensParseExceptionType
	 *            Exception type (ENUM)
	 */
	public UnisensParseException(
			UnisensParseExceptionTypeEnum unisensParseExceptionType)
	{
		super();
		this.unisensParseExceptionType = unisensParseExceptionType;
	}

	/**
	 * UnisensParseException represents specific Exceptions that may occur while
	 * parsing the unisens.xml file of a unisens DataSet.
	 * 
	 * @param message
	 *            Specific exception message
	 * @param unisensParseExceptionType
	 *            Exception type (ENUM)
	 */
	public UnisensParseException(String message,
			UnisensParseExceptionTypeEnum unisensParseExceptionType)
	{
		super(message);
		this.unisensParseExceptionType = unisensParseExceptionType;
	}

	/**
	 * Gets the {@link UnisensParseExceptionTypeEnum}.
	 */
	public UnisensParseExceptionTypeEnum getUnisensParseExceptionType()
	{
		return unisensParseExceptionType;
	}
}
