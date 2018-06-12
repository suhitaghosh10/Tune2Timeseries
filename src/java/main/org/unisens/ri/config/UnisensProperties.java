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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UnisensProperties {
	private static Properties properties = null;
	private static UnisensProperties unisensProiperties = null;

	private UnisensProperties() {
		try {
			properties = new Properties();
			InputStream i = UnisensProperties.class.getClassLoader().getResourceAsStream(Constants.PATH_CONFIG);

			properties.load(i);
			

			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static UnisensProperties getInstance() {
		if (unisensProiperties == null)
			unisensProiperties = new UnisensProperties();

		return new UnisensProperties();
	}

	public Properties getProperties() {
		return properties;
	}
}
