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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.unisens.AnnotationEntry;
import org.unisens.BinFileFormat;
import org.unisens.Context;
import org.unisens.CsvFileFormat;
import org.unisens.CustomEntry;
import org.unisens.CustomFileFormat;
import org.unisens.DataType;
import org.unisens.DuplicateIdException;
import org.unisens.Entry;
import org.unisens.EventEntry;
import org.unisens.FileFormat;
import org.unisens.Group;
import org.unisens.MeasurementEntry;
import org.unisens.SignalEntry;
import org.unisens.Unisens;
import org.unisens.UnisensParseException;
import org.unisens.UnisensParseExceptionTypeEnum;
import org.unisens.ValuesEntry;
import org.unisens.XmlFileFormat;
import org.unisens.ri.config.Constants;
import org.unisens.ri.config.UnisensErrorHandler;
import org.unisens.ri.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Radoslav Nedkov
 * @email radi.nedkov@gmail.com
 * 
 *        25.02.2007
 */
public class UnisensImpl implements Unisens, Constants
{
	private String absolutePath = null;
	private String absoluteFileName = null;
	private String version = null;
	private String measurementId = null;
	private Date timestampStart = null;
	private double duration = 0.0;
	private String comment = null;
	private DecimalFormat decimalFormat = null;

	private HashMap<String, String> customAttributes = new HashMap<String, String>();

	private List<Entry> entries = new ArrayList<Entry>();
	private Context context = null;
	private List<Group> groups = new ArrayList<Group>();

	public UnisensImpl(String absolutePath) throws UnisensParseException
	{
		this.absolutePath = absolutePath;
		if (!this.absolutePath.endsWith(System.getProperty("file.separator")))
			this.absolutePath += System.getProperty("file.separator");
		File dir = new File(absolutePath);
		if (!dir.exists())
			dir.mkdirs();
		this.absoluteFileName = this.absolutePath + "unisens.xml";
		File file = new File(absoluteFileName);
		this.decimalFormat = new DecimalFormat();
		decimalFormat.setDecimalSeparatorAlwaysShown(false);
		decimalFormat.setGroupingSize(0);
		decimalFormat.setMaximumFractionDigits(100);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(dfs);
		if (file.exists())
			this.parse();
		if (this.getVersion() == null)
			this.version = "2.0";
	}

	public HashMap<String, String> getCustomAttributes()
	{
		return customAttributes;
	}

	public void addCustomAttribute(String key, String value)
	{
		this.customAttributes.put(key, value);
	}

	public SignalEntry createSignalEntry(String id, String[] channelNames,
			DataType dataType, double sampleRate) throws DuplicateIdException
	{
		if (this.getEntry(id) != null)
			throw new DuplicateIdException("The entry with " + id + " exist!");
		SignalEntry signalEntry = new SignalEntryImpl(this, id, channelNames,
				dataType, sampleRate);
		this.entries.add(signalEntry);
		return signalEntry;
	}

	public Group createGroup(String id) throws DuplicateIdException
	{
		if (this.getGroup(id) != null)
			throw new DuplicateIdException("The group with " + id + " exist!");
		Group group = new GroupImpl(this, id, null);
		this.groups.add(group);
		return group;
	}

	public Context createContext(String schemaUrl)
	{
		Context context = new ContextImpl(schemaUrl);
		this.context = context;
		return context;
	}


	public EventEntry createEventEntry(String id, double sampleRate)
			throws DuplicateIdException
	{
		if (this.getEntry(id) != null)
			throw new DuplicateIdException("The entry with " + id + " exist!");
		EventEntry eventEntry = new EventEntryImpl(this, id, sampleRate);
		this.entries.add(eventEntry);
		return eventEntry;
	}

	public ValuesEntry createValuesEntry(String id, String[] channelNames,
			DataType dataType, double sampleRate) throws DuplicateIdException
	{
		if (this.getEntry(id) != null)
			throw new DuplicateIdException("The entry with " + id + " exist!");
		ValuesEntry valuesEntry = new ValuesEntryImpl(this, id, channelNames,
				dataType, sampleRate);
		this.entries.add(valuesEntry);
		return valuesEntry;
	}

	public CustomEntry createCustomEntry(String id) throws DuplicateIdException
	{
		if (this.getEntry(id) != null)
			throw new DuplicateIdException("The entry with " + id + " exist!");
		CustomEntry customEntry = new CustomEntryImpl(this, id);
		this.entries.add(customEntry);
		return customEntry;
	}

	public Group addGroup(Group group, boolean deepCopy)
			throws DuplicateIdException
	{
		if (this.getGroup(group.getId()) != null)
			throw new DuplicateIdException("The group with " + group.getId()
					+ " exist!");
		Group myGroup = new GroupImpl(this, group.getId(), group.getComment());
		List<Entry> groupEntries = group.getEntries();
		for (Entry groupEntry : groupEntries)
		{
			Entry myEntry = this.getEntry(groupEntry.getId());
			if (myEntry != null)
			{
				myGroup.addEntry(myEntry);
			}
			else
			{
				myGroup.addEntry(this.addEntry(groupEntry, deepCopy));
			}
		}
		this.groups.add(myGroup);
		return myGroup;
	}

	public void deleteGroup(Group group)
	{
		this.groups.remove(group);
	}


	public List<Entry> getEntries()
	{
		return entries;
	}

	public Entry addEntry(Entry entry, boolean deepCopy)
			throws DuplicateIdException
	{
		if (this.getEntry(entry.getId()) != null)
			throw new DuplicateIdException("The entry with " + entry.getId()
					+ " exist!");
		Entry myEntry = entry.clone();
		myEntry.setUnisens(this);
		entries.add(myEntry);
		if (deepCopy)
		{
			File in = new File(entry.getUnisens().getPath() + entry.getId());
			File out = new File(getPath() + entry.getId());
			if (in.exists())
				Utilities.copyFile(in, out);
		}
		return myEntry;
	}

	public Entry getEntry(String id)
	{
		for (Entry entry : entries)
			if (entry.getId().equalsIgnoreCase(id))
				return entry;
		return null;
	}

	public void deleteEntry(Entry entry)
	{
		entry.close();
		this.deleteFile(absolutePath + entry.getId());
		this.entries.remove(entry);
		for (Group group : groups)
			group.getEntries().remove(entry);

	}


	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public double getDuration()
	{
		return duration;
	}

	public void setDuration(double duration)
	{
		this.duration = duration;
	}

	/**
	 * @deprecated
	 */
	public void setDuration(long duration)
	{
		this.duration = (double) duration;
	}

	public void setTimestampStart(Date timestampStart)
	{
		this.timestampStart = timestampStart;
	}

	public String getMeasurementId()
	{
		return measurementId;
	}

	public void setMeasurementId(String measurementId)
	{
		this.measurementId = measurementId;
	}

	public Date getTimestampStart()
	{
		return timestampStart;
	}

	public String getVersion()
	{
		return version;
	}


	public Context getContext()
	{
		return context;
	}

	public void deleteContext()
	{
		this.context = null;
	}

	public List<Group> getGroups()
	{
		return groups;
	}

	public Group getGroup(String id)
	{
		for (Group group : groups)
			if (group.getId().equalsIgnoreCase(id))
				return group;
		return null;
	}


	public String getPath()
	{
		return this.absolutePath;
	}

	private void parse() throws UnisensParseException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);

			FileInputStream fis = new FileInputStream(
					new File(absoluteFileName));
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(fis);

			String validate = System.getProperty(PROPERTIE_VALIDATION);
			if (validate != null && validate.equalsIgnoreCase("true"))
				validate(document);

			Node unisensNode = document.getDocumentElement();
			NodeList unisensChildNodes = unisensNode.getChildNodes();
			this.parse(unisensNode);

			Node currentNode;

			EntryImpl entry = null;
			ContextImpl context = null;
			GroupImpl group = null;

			for (int i = 0; i < unisensChildNodes.getLength(); i++)
			{
				currentNode = unisensChildNodes.item(i);
				if (currentNode.getNodeType() == Node.ELEMENT_NODE)
				{

					if (currentNode.getNodeName().equalsIgnoreCase(SIGNALENTRY))
					{
						entry = new SignalEntryImpl(this, currentNode);
						this.entries.add(entry);
						continue;
					}

					if (currentNode.getNodeName().equalsIgnoreCase(EVENTENTRY))
					{
						entry = new EventEntryImpl(this, currentNode);
						this.entries.add(entry);
						continue;
					}

					if (currentNode.getNodeName().equalsIgnoreCase(CUSTOMENTRY))
					{
						entry = new CustomEntryImpl(this, currentNode);
						this.entries.add(entry);
						continue;
					}

					if (currentNode.getNodeName().equalsIgnoreCase(VALUESENTRY))
					{
						entry = new ValuesEntryImpl(this, currentNode);
						this.entries.add(entry);
						continue;
					}

					if (currentNode.getNodeName().equalsIgnoreCase(CONTEXT))
					{
						context = new ContextImpl(currentNode);
						this.context = context;
						continue;
					}
					if (currentNode.getNodeName().equalsIgnoreCase(GROUP))
					{
						group = new GroupImpl(this, currentNode);
						this.groups.add(group);
						continue;
					}

					if (currentNode.getNodeName().equalsIgnoreCase(
							CUSTOM_ATTRIBUTES))
					{
						NodeList customAttributeNodes = currentNode
								.getChildNodes();
						for (int j = 0; j < customAttributeNodes.getLength(); j++)
						{
							Node customAttributeNode = customAttributeNodes
									.item(j);
							if (customAttributeNode.getNodeName()
									.equalsIgnoreCase(CUSTOM_ATTRIBUTE))
							{
								NamedNodeMap attrs = customAttributeNode
										.getAttributes();
								Node keyNode = attrs
										.getNamedItem(CUSTOM_ATTRIBUTE_KEY);
								String key = "";
								if (keyNode != null)
								{
									key = keyNode.getNodeValue();
								}
								Node valueNode = attrs
										.getNamedItem(CUSTOM_ATTRIBUTE_VALUE);
								String value = "";
								if (valueNode != null)
								{
									value = valueNode.getNodeValue();
								}
								if (key != "")
									customAttributes.put(key, value);
							}
						}
					}
				}

			}
			fis.close();
		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch (SAXException saxe)
		{
			saxe.printStackTrace();
			throw new UnisensParseException(saxe.getMessage(), UnisensParseExceptionTypeEnum.UNISENS_INVALID);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private void parse(Node unisensNode)
	{
		NamedNodeMap attr = unisensNode.getAttributes();
		Node attrNode = attr.getNamedItem(Constants.UNISENS_VERSION);
		version = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attr.getNamedItem(Constants.UNISENS_MEASUREMENT_ID);
		measurementId = (attrNode != null) ? attrNode.getNodeValue() : null;
		attrNode = attr.getNamedItem(Constants.UNISENS_TIMESTAMP_START);
		timestampStart = (attrNode != null) ? Utilities
				.convertStringToDate(attrNode.getNodeValue()) : null;
		attrNode = attr.getNamedItem(Constants.UNISENS_DURATION);
		duration = (attrNode != null) ? Double.parseDouble(attrNode
				.getNodeValue()) : 0;
		attrNode = attr.getNamedItem(Constants.UNISENS_COMMENT);
		comment = (attrNode != null) ? attrNode.getNodeValue() : null;

		return;
	}

	/**
	 * This method writes the XML tree of unisens.xml
	 * @param document
	 * @return XML root element
	 */
	protected Element createElement(Document document)
	{
		Element unisensElement = null;

		unisensElement = document.createElementNS(
				"http://www.unisens.org/unisens2.0", UNISENS);
		unisensElement
				.setAttributeNS(
						"http://www.w3.org/2001/XMLSchema-instance",
						"xsi:schemaLocation",
						"http://www.unisens.org/unisens2.0 http://www.unisens.org/unisens2.0/unisens.xsd");

		if (getComment() != null)
			unisensElement.setAttribute(UNISENS_COMMENT, getComment());
		if (getDuration() != 0)
			unisensElement.setAttribute(UNISENS_DURATION, "" + getDuration());
		if (getTimestampStart() != null)
			unisensElement.setAttribute(UNISENS_TIMESTAMP_START,
					Utilities.convertDateToString(timestampStart));
		if (getMeasurementId() != null)
			unisensElement.setAttribute(UNISENS_MEASUREMENT_ID,
					getMeasurementId());
		if (getVersion() != null)
			unisensElement.setAttribute(UNISENS_VERSION, getVersion());

		if (this.customAttributes.size() > 0)
		{
			Element customAttributesElement = document.createElement(CUSTOM_ATTRIBUTES);
			for (String key : customAttributes.keySet())
			{
				Element customAttributeElement = document.createElement(CUSTOM_ATTRIBUTE);
				customAttributeElement.setAttribute(CUSTOM_ATTRIBUTE_KEY, key);
				customAttributeElement.setAttribute(CUSTOM_ATTRIBUTE_VALUE, customAttributes.get(key));
				customAttributesElement.appendChild(customAttributeElement);
			}
			unisensElement.appendChild(customAttributesElement);
		}

		if (this.context != null)
		{
			Element contextElement = document.createElement(CONTEXT);
			if (context.getSchemaUrl() != null)
				contextElement.setAttribute(CONTEXT_SCHEMAURL,
						context.getSchemaUrl());
			unisensElement.appendChild(contextElement);
		}

		Entry entry;

		for (int i = 0; i < entries.size(); i++)
		{
			entry = (Entry) entries.get(i);
			Element entryElement = null;

			if (entry instanceof SignalEntry)
			{
				entryElement = document.createElement(SIGNALENTRY);
			}
			else if (entry instanceof ValuesEntry)
			{
				entryElement = document.createElement(VALUESENTRY);
			}
			else if (entry instanceof EventEntry)
			{
				entryElement = document.createElement(EVENTENTRY);
			}
			else if (entry instanceof CustomEntry)
			{
				entryElement = document.createElement(CUSTOMENTRY);
			}

			if (entry.getComment() != null)
			{
				entryElement.setAttribute(ENTRY_COMMENT, entry.getComment());
			}
			
			if (entry.getContentClass() != null)
			{
				entryElement.setAttribute(ENTRY_CONTENTCLASS, entry.getContentClass());
			}
			
			if (entry.getSource() != null)
			{
				entryElement.setAttribute(ENTRY_SOURCE, entry.getSource());
			}
			
			if (entry.getSourceId() != null)
			{
				entryElement.setAttribute(ENTRY_SOURCE_ID, entry.getSourceId());
			}
			
			if (entry.getName() != null)
			{
				entryElement.setAttribute(ENTRY_NAME, entry.getId());
			}
			
			if (entry.getId() != null)
			{
				entryElement.setAttribute(ENTRY_ID, entry.getId());
			}

			if (entry.getCustomAttributes().size() > 0)
			{
				Element customAttributesElement = document.createElement(CUSTOM_ATTRIBUTES);
				HashMap<String, String> customAttributes = entry.getCustomAttributes();
				
				for (String key : customAttributes.keySet())
				{
					Element customAttributeElement = document.createElement(CUSTOM_ATTRIBUTE);
					customAttributeElement.setAttribute(CUSTOM_ATTRIBUTE_KEY, key);
					customAttributeElement.setAttribute(CUSTOM_ATTRIBUTE_VALUE, customAttributes.get(key));
					customAttributesElement.appendChild(customAttributeElement);
				}
				entryElement.appendChild(customAttributesElement);
			}

			FileFormat fileFormat = entry.getFileFormat();
			Element fileFormatElement = null;

			if (fileFormat instanceof BinFileFormat)
			{
				BinFileFormat binFileFormat = (BinFileFormat) fileFormat;
				fileFormatElement = document.createElement(BINFILEFORMAT);
				fileFormatElement.setAttribute(BINFILEFORMAT_ENDIANESS, binFileFormat.getEndianess().name());
			}
			else if (fileFormat instanceof CsvFileFormat)
			{
				CsvFileFormat csvFileFormat = (CsvFileFormat) fileFormat;
				fileFormatElement = document.createElement(CSVFILEFORMAT);
				fileFormatElement.setAttribute(CSVFILEFORMAT_SEPARATOR, csvFileFormat.getSeparator() == "\t" ? "\\t" : csvFileFormat.getSeparator());
				fileFormatElement.setAttribute(CSVFILEFORMAT_DECIMAL_SEPARATOR, csvFileFormat.getDecimalSeparator());
			}
			else if (fileFormat instanceof XmlFileFormat)
			{
				fileFormatElement = document.createElement(XMLFILEFORMAT);
			}
			else if (fileFormat instanceof CustomFileFormat)
			{
				CustomFileFormat customFileFormat = (CustomFileFormat) fileFormat;
				fileFormatElement = document.createElement(CUSTOMFILEFORMAT);
				fileFormatElement.setAttribute(CUSTOMFILEFORMAT_FILEFORMATNAME, customFileFormat.getFileFormatName());
				HashMap<String, String> attributes = customFileFormat.getAttributes();
				Set<String> attrNames = attributes.keySet();
				
				for (String attrName : attrNames)
				{
					fileFormatElement.setAttribute(attrName, attributes.get(attrName));
				}
			}
			
			if (fileFormat.getComment() != null)
			{
				fileFormatElement.setAttribute(FILEFORMAT_COMMENT, fileFormat.getComment());
			}
			
			entryElement.appendChild(fileFormatElement);

			if (entry instanceof MeasurementEntry)
			{
				MeasurementEntry measurementEntry = (MeasurementEntry) entry;
				if (measurementEntry.getAdcResolution() != 0)
					entryElement.setAttribute(MEASUREMENTENTRY_ADCRESOLUTION,
							"" + measurementEntry.getAdcResolution());
				if (measurementEntry.getAdcZero() != 0)
					entryElement.setAttribute(MEASUREMENTENTRY_ADCZERO, ""
							+ measurementEntry.getAdcZero());
				if (measurementEntry.getBaseline() != 0)
					entryElement.setAttribute(MEASUREMENTENTRY_BASELINE, ""
							+ measurementEntry.getBaseline());
				if (measurementEntry.getDataType() != null)
					entryElement.setAttribute(MEASUREMENTENTRY_DATATYPE,
							measurementEntry.getDataType().value());
				if (measurementEntry.getLsbValue() != 0)
					entryElement
							.setAttribute(MEASUREMENTENTRY_LSBVALUE,
									decimalFormat.format(measurementEntry
											.getLsbValue()));
				if (measurementEntry.getSampleRate() != 0)
					entryElement.setAttribute(TIMEDENTRY_SAMPLERATE,
							decimalFormat.format(measurementEntry
									.getSampleRate()));
				if (measurementEntry.getUnit() != null)
					entryElement.setAttribute(MEASUREMENTENTRY_UNIT,
							measurementEntry.getUnit());

				String[] channelNames = measurementEntry.getChannelNames();

				for (int j = 0; j < channelNames.length; j++)
				{
					Element channelElement = document.createElement(CHANNEL);
					channelElement.setAttribute(CHANNEL_NAME, channelNames[j]);
					entryElement.appendChild(channelElement);
				}
			}
			else if (entry instanceof AnnotationEntry)
			{
				AnnotationEntry annotationEntry = (AnnotationEntry) entry;
				if (annotationEntry.getSampleRate() != 0)
					entryElement.setAttribute(TIMEDENTRY_SAMPLERATE, decimalFormat.format(annotationEntry.getSampleRate()));
				if (annotationEntry.getTypeLength() != 0)
					entryElement.setAttribute(ANNOTATIONENTRY_TYPE_LENGTH, "" + annotationEntry.getTypeLength());
				if (annotationEntry.getCommentLength() != 0)
					entryElement.setAttribute(ANNOTATIONENTRY_COMMENT_LENGTH, "" + annotationEntry.getCommentLength());
			}
			else if (entry instanceof CustomEntry)
			{
				CustomEntry customEntry = (CustomEntry) entry;
				HashMap<String, String> attributes = customEntry
						.getAttributes();
				Set<String> keySet = attributes.keySet();
				String[] keys = new String[keySet.size()];
				keys = (String[]) keySet.toArray(keys);
				
				for (int j = 0; j < keys.length; j++)
				{
					entryElement.setAttribute(keys[j], (String) attributes.get(keys[j]));
				}
			}

			unisensElement.appendChild(entryElement);
		}
		Group group;
		List<Entry> entries;
		Element groupElement;
		Element groupEntryElement;

		for (int i = 0; i < groups.size(); i++)
		{
			group = (Group) groups.get(i);
			groupElement = document.createElement(GROUP);

			if (group.getComment() != null)
				groupElement.setAttribute(GROUP_COMMENT, group.getComment());
			if (group.getId() != null)
				groupElement.setAttribute(GROUP_ID, group.getId());

			entries = group.getEntries();
			for (int j = 0; j < entries.size(); j++)
			{
				groupEntryElement = document.createElement(GROUPENTRY);
				groupEntryElement.setAttribute(GROUPENTRY_REF,
						((Entry) entries.get(j)).getId());
				groupElement.appendChild(groupEntryElement);
			}
			unisensElement.appendChild(groupElement);
		}
		return unisensElement;
	}


	public void save()
	{
		try
		{
			File file = new File(this.absoluteFileName);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			Document document = factory.newDocumentBuilder().newDocument();

			Element unisensElement = this.createElement(document);

			document.appendChild(unisensElement);
			DOMSource source = new DOMSource(document);

			String validate = System.getProperty(PROPERTIE_VALIDATION);
			if (validate != null && validate.equalsIgnoreCase("true"))
				validate(document);

			FileOutputStream out = new FileOutputStream(file);
			StreamResult result = new StreamResult(out);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(source, result);

			out.flush();
			out.close();

		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch (TransformerConfigurationException tce)
		{
			tce.printStackTrace();
		}
		catch (TransformerException te)
		{
			te.printStackTrace();
		}
		catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public void closeAll()
	{
		for (Entry entry : entries)
			entry.close();
	}

	protected void finalize() throws Throwable
	{
		try
		{
			closeAll(); // close open files
		}
		finally
		{
			super.finalize();
		}
	}

	private void deleteFile(String absoluteFileName)
	{
		File file = new File(absoluteFileName);
		if (file.exists())
			file.delete();
	}

	private void validate(Document document)
	{
		try
		{
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL schemaUrl = Unisens.class.getClassLoader().getResource(
					"unisens.xsd");
			Schema schema = schemaFactory.newSchema(schemaUrl);
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new UnisensErrorHandler());
			validator.validate(new DOMSource(document));
		}
		catch (SAXException saxe)
		{
			saxe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

}
