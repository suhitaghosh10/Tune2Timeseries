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

package org.unisens.ri.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.unisens.EventEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.config.UnisensProperties;

public class EventIoFactory {

	@SuppressWarnings("unchecked")
	public static EventReader createEventReader(EventEntry eventEntry){
		Properties unisensProperties = UnisensProperties.getInstance().getProperties();
		String readerClassName = unisensProperties.getProperty(Constants.EVENT_READER.replaceAll("format", eventEntry.getFileFormat().getFileFormatName().toLowerCase()));
		if(readerClassName != null){
			try{
				Class<EventReader> readerClass = (Class<EventReader>)Class.forName(readerClassName);
				Constructor<EventReader> readerConstructor = readerClass.getConstructor(EventEntry.class);
				return (EventReader)readerConstructor.newInstance(eventEntry);
			} catch (ClassNotFoundException e) {
	            System.out.println("Class (" + readerClassName + ") could not be found!");
	            e.printStackTrace();
	        } catch (InstantiationException e) {
	            System.out.println("Class (" + readerClassName + ") could not be instantiated!");
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            System.out.println("Class (" + readerClassName + ") could not be accessed!");
	            e.printStackTrace();
	        } catch (ClassCastException ec) {
	            ec.printStackTrace();
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static EventWriter createEventWriter(EventEntry eventEntry){
		Properties unisensProperties = UnisensProperties.getInstance().getProperties();
		String readerClassName = unisensProperties.getProperty(Constants.EVENT_WRITER.replaceAll("format", eventEntry.getFileFormat().getFileFormatName().toLowerCase()));
		if(readerClassName != null){
			try{
				Class<EventWriter> readerClass = (Class<EventWriter>)Class.forName(readerClassName);
				Constructor<EventWriter> readerConstructor = readerClass.getConstructor(EventEntry.class);
				return (EventWriter)readerConstructor.newInstance(eventEntry);
			} catch (ClassNotFoundException e) {
	            System.out.println("Class (" + readerClassName + ") could not be found!");
	            e.printStackTrace();
	        } catch (InstantiationException e) {
	            System.out.println("Class (" + readerClassName + ") could not be instantiated!");
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            System.out.println("Class (" + readerClassName + ") could not be accessed!");
	            e.printStackTrace();
	        } catch (ClassCastException ec) {
	            ec.printStackTrace();
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
		}
		return null;
	}
}
