/*
Unisens Library - library for a universal sensor data format
Copyright 2008-2010 FZI Research Center for Information Technology, Germany
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

import org.unisens.CsvFileFormat;
import org.unisens.FileFormat;
import org.w3c.dom.Node;

public class CsvFileFormatImpl extends FileFormatImpl implements CsvFileFormat
{
	private String separator = ";";
	private String decimalSeparator = ".";

	protected CsvFileFormatImpl(Node csvFileFormatNode)
	{
		super(csvFileFormatNode, "CSV");
		this.parse(csvFileFormatNode);
	}

	protected CsvFileFormatImpl(CsvFileFormatImpl csvFileFormat)
	{
		super(csvFileFormat);
		this.separator = csvFileFormat.getSeparator();
		this.decimalSeparator = csvFileFormat.getDecimalSeparator();
	}

	public CsvFileFormatImpl()
	{
		super("CSV");
	}

	private void parse(Node csvFileFormatNode)
	{
		Node attrNode = csvFileFormatNode.getAttributes().getNamedItem(
				CSVFILEFORMAT_SEPARATOR);
		this.separator = (attrNode != null) ? attrNode.getNodeValue() : ";";
		if (separator.equalsIgnoreCase("\\t"))
		{
			separator = "\t";
		}
		attrNode = csvFileFormatNode.getAttributes().getNamedItem(
				CSVFILEFORMAT_DECIMAL_SEPARATOR);
		this.decimalSeparator = (attrNode != null) ? attrNode.getNodeValue()
				: ".";
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	@Override
	public FileFormat clone()
	{
		return new CsvFileFormatImpl(this);
	}

	public String getDecimalSeparator()
	{
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator)
	{
		this.decimalSeparator = decimalSeparator;
	}


}
