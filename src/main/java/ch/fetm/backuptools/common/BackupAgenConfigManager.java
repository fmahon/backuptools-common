/*	Copyright 2013 Florian Mahon <florian@faivre-et-mahon.ch>
 * 
 *    This file is part of backuptools.
 *    
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.fetm.backuptools.common;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public abstract class BackupAgenConfigManager {

	public final static BackupAgentConfig readConfigurationFile() {
		BackupAgentConfig config = new BackupAgentConfig();
    	
		Path configfile = getConfigFile();
		if(!Files.exists(configfile))
			return null;
		
		try {
			XMLDecoder in = new XMLDecoder( new BufferedInputStream( 
												new FileInputStream(configfile.toFile())));
			config = (BackupAgentConfig) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return config;
	}

	public static void writeConfigurationInFile(BackupAgentConfig config) {
		Path configfile = getConfigFile();
		try {	
			if(Files.exists(configfile)){
					Files.delete(configfile);
				
			}
			
			Files.createFile(configfile);
			XMLEncoder out = new XMLEncoder( new FileOutputStream(configfile.toFile()));
			out.writeObject(config);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
    }

	private static Path getConfigFile() {
		String home = System.getProperty("user.home");
		
    	Path path = Paths.get( home
    						   + FileSystems.getDefault().getSeparator() 
    						   + ".backuptools");
    	
    	if(!path.toFile().exists()){
    		try {
				Files.createDirectory(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	Path configfile = Paths.get( path.toAbsolutePath().toString() 
    								 + FileSystems.getDefault().getSeparator()
    								 + "config.xml");
   
		return configfile;
	}
}
