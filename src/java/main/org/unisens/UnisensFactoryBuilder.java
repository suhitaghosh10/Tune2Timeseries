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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * UnisensFactoryBuilder creates a UnisensFactory of the used unisens implementation. The
 * standard unisens implementation can be set by the system property 
 * "org.unisens.StandardUnisensFactoryClass". If no system property is set the 
 * reference implementation in the package "org.unisens.ri" is used as standard 
 * implementation.
 * 
 * @author Joerg Ottenbacher
 * @author Radi Nedkov
 * @author Malte Kirst
 *
 */
public class UnisensFactoryBuilder {
	
	/**
	 * Creates a UnisensFactory from the standard implementation. 
	 * 
	 * @return UnisensFactory
	 */
	public static UnisensFactory createFactory(){
		String factoryClass = System.getProperty("org.unisens.StandardUnisensFactoryClass");
		if (factoryClass == null)
	        factoryClass ="org.unisens.ri.UnisensFactoryImpl";
		try{
		    
		    Class<UnisensFactory> readerClass = (Class<UnisensFactory>)Class.forName(factoryClass);
			Constructor<UnisensFactory> readerConstructor = readerClass.getConstructor();
			return (UnisensFactory)readerConstructor.newInstance();
		} catch (ClassNotFoundException e) {
	        System.out.println("Class (" + factoryClass + ") could not be found!");
	        e.printStackTrace();
	    } catch (InstantiationException e) {
	        System.out.println("Class (" + factoryClass + ") could not be instantiated!");
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        System.out.println("Class (" + factoryClass + ") could not be accessed!");
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
	    return null;
	}

	/**
	 * Creates a UnisensFactory specified by a given UnisensFactory class name. 
	 * 
	 * @param factoryClass name of the factory class
	 * @return UnisensFactory
	 */
	public static UnisensFactory createFactory(String factoryClass){
		try{
		    
		    Class<UnisensFactory> readerClass = (Class<UnisensFactory>)Class.forName(factoryClass);
			Constructor<UnisensFactory> readerConstructor = readerClass.getConstructor();
			return (UnisensFactory)readerConstructor.newInstance();
		} catch (ClassNotFoundException e) {
	        System.out.println("Class (" + factoryClass + ") could not be found!");
	        e.printStackTrace();
	    } catch (InstantiationException e) {
	        System.out.println("Class (" + factoryClass + ") could not be instantiated!");
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        System.out.println("Class (" + factoryClass + ") could not be accessed!");
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
	    return null;
	}
}
