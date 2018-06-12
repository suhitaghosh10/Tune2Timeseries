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

package org.unisens.ri.config;

public interface Constants {
	public static String UNISENS = "unisens";
	public static String CUSTOM_ATTRIBUTES = "customAttributes";
	public static String CUSTOM_ATTRIBUTE = "customAttribute";
	public static String CUSTOM_ATTRIBUTE_KEY = "key";
	public static String CUSTOM_ATTRIBUTE_VALUE = "value";
	public static String CONTEXT = "context";
	public static String ENTRY = "entry";
	public static String SIGNALENTRY = "signalEntry";
	public static String VALUESENTRY = "valuesEntry";
	public static String EVENTENTRY = "eventEntry";
	public static String SPANENTRY = "spanEntry";
	public static String CUSTOMENTRY = "customEntry";
	public static String CHANNEL = "channel";
	public static String GROUP = "group";
	public static String GROUPENTRY = "groupEntry";
	public static String XMLFILEFORMAT = "xmlFileFormat";
	public static String BINFILEFORMAT = "binFileFormat";
	public static String CSVFILEFORMAT = "csvFileFormat";
	public static String CUSTOMFILEFORMAT = "customFileFormat";

	public static String UNISENS_VERSION = "version";
	public static String UNISENS_MEASUREMENT_ID = "measurementId";
	public static String UNISENS_TIMESTAMP_START = "timestampStart";
	public static String UNISENS_DURATION = "duration";
	public static String UNISENS_COMMENT = "comment";
	
	public static String CONTEXT_SCHEMAURL = "schemaUrl";
		
	public static String ENTRY_ID = "id";
	public static String ENTRY_NAME = "name";
	public static String ENTRY_CONTENTCLASS = "contentClass";
	public static String ENTRY_SOURCE = "source";
	public static String ENTRY_SOURCE_ID = "sourceId";
	public static String ENTRY_COMMENT = "comment";
	
	public static String MEASUREMENTENTRY_ADCRESOLUTION = "adcResolution";
	public static String MEASUREMENTENTRY_UNIT = "unit";
	public static String MEASUREMENTENTRY_LSBVALUE = "lsbValue";
	public static String MEASUREMENTENTRY_ADCZERO = "adcZero";
	public static String MEASUREMENTENTRY_BASELINE = "baseline";
	public static String MEASUREMENTENTRY_DATATYPE = "dataType";
	
	public static String TIMEDENTRY_SAMPLERATE = "sampleRate";
	
	public static String ANNOTATIONENTRY_COMMENT_LENGTH = "commentLength";
	public static String ANNOTATIONENTRY_TYPE_LENGTH = "typeLength";
	
	public static String GROUP_ID = "id";
	public static String GROUP_COMMENT = "comment";
	
	public static String GROUPENTRY_REF = "ref";
	
	public static String CHANNEL_NAME = "name";
	
	public static String FILEFORMAT_COMMENT = "comment";
	
	public static String CSVFILEFORMAT_SEPARATOR = "separator";
	public static String CSVFILEFORMAT_DECIMAL_SEPARATOR = "decimalSeparator";
	
	public static String BINFILEFORMAT_ENDIANESS = "endianess";
	
	public static String CUSTOMFILEFORMAT_FILEFORMATNAME = "fileFormatName"; 
	
	public static String PATH_CONFIG = "unisens.cfg";
	public static String PATH_UNISENS_SCHEMA = "unisens.xsd";
	
	public static String SIGNAL_READER = "signal_format_reader";
	public static String SIGNAL_WRITER = "signal_format_writer";
	
	public static String EVENT_READER = "event_format_reader";
	public static String EVENT_WRITER = "event_format_writer";
	
	public static String SPAN_READER = "span_format_reader";
	public static String SPAN_WRITER = "span_format_writer";
	
	public static String VALUES_READER = "values_format_reader";
	public static String VALUES_WRITER = "values_format_writer";
	
	public static String CUSTOM_READER = "custom_format_reader";
	public static String CUSTOM_WRITER = "custom_format_writer";
	
	public static String PROPERTIE_VALIDATION = "org.unisens.validation";
	
	public static String SIGNAL_XML_READER_SAMPLES_PATH = "/signal/sample[ position() > %1$d and position() < %2$d]";
	public static String SIGNAL_XML_READER_SAMPLES_DATA_PATH = "./data/text()";
	public static String SIGNAL_XML_READER_SIGNAL_ELEMENT = "signal";
	public static String SIGNAL_XML_READER_SAMPLE_ELEMENT = "sample";
	public static String SIGNAL_XML_READER_DATA_ELEMENT = "data";
	
	public static String EVENT_XML_READER_EVENTS_PATH = "/events/event[ position() > %1$d and position() < %2$d]";
	public static String EVENT_XML_READER_EVENTS_ELEMENT = "events";
	public static String EVENT_XML_READER_EVENT_ELEMENT = "event";
	public static String EVENT_XML_READER_SAMPLESTAMP_ATTR = "sampleStamp";
	public static String EVENT_XML_READER_TYPE_ATTR = "type";
	public static String EVENT_XML_READER_COMMENT_ATTR = "comment";
	
	public static String SPAN_XML_READER_SPANS_PATH = "/spans/span[ position() > %1$d and position() < %2$d]";
	public static String SPAN_XML_READER_SPANS_ELEMENT = "spans";
	public static String SPAN_XML_READER_SPAN_ELEMENT = "span";
	public static String SPAN_XML_READER_SAMPLESTAMPSTART_ATTR = "sampleStampStart";
	public static String SPAN_XML_READER_SAMPLESTAMPEND_ATTR = "sampleStampEnd";
	public static String SPAN_XML_READER_TYPE_ATTR = "type";
	public static String SPAN_XML_READER_COMMENT_ATTR = "comment";
	
	public static String VALUES_XML_READER_VALUES_PATH = "/values/value[ position() > %1$d and position() < %2$d]";
	public static String VALUES_XML_READER_VALUES_DATA_PATH = "./data/text()";
	public static String VALUES_XML_READER_VALUES_ELEMENT = "values";
	public static String VALUES_XML_READER_VALUE_ELEMENT = "value";
	public static String VALUES_XML_READER_DATA_ELEMENT = "data";
	public static String VALUES_XML_READER_SAMPLESTAMP_ATTR = "sampleStamp";
	
	public static int DEFAULT_EVENT_ENTRY_COMMENT_LENGTH = 10;
	public static int DEFAULT_EVENT_ENTRY_TYPE_LENGTH = 1;
}
