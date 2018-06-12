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

import org.unisens.ValuesEntry;
import org.unisens.ri.config.Constants;
import org.unisens.ri.config.UnisensProperties;

public class ValuesIoFactory {

	@SuppressWarnings("unchecked")
	public static ValuesReader createValuesReader(ValuesEntry valuesEntry){
		Properties unisensProperties = UnisensProperties.getInstance().getProperties();
		String readerClassName = unisensProperties.getProperty(Constants.VALUES_READER.replaceAll("format", valuesEntry.getFileFormat().getFileFormatName().toLowerCase()));
		if(readerClassName != null){
			try{
				Class<ValuesReader> readerClass = (Class<ValuesReader>)Class.forName(readerClassName);
				Constructor<ValuesReader> readerConstructor = readerClass.getConstructor(ValuesEntry.class);
				return readerConstructor.newInstance(valuesEntry);
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
	public static ValuesWriter createValuesWriter(ValuesEntry valuesEntry){
		Properties unisensProperties = UnisensProperties.getInstance().getProperties();
		String readerClassName = unisensProperties.getProperty(Constants.VALUES_WRITER.replaceAll("format", valuesEntry.getFileFormat().getFileFormatName().toLowerCase()));
		if(readerClassName != null){
			try{
				Class<ValuesWriter> readerClass = (Class<ValuesWriter>)Class.forName(readerClassName);
				Constructor<ValuesWriter> readerConstructor = readerClass.getConstructor(ValuesEntry.class);
				return readerConstructor.newInstance(valuesEntry);
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
