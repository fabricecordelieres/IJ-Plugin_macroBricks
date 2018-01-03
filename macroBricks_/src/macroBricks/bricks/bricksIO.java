package macroBricks.bricks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
*
*  bricksIO.java, 23 nov. 2017
   Fabrice P Cordelieres, fabrice.cordelieres at gmail.com

   Copyright (C) 2017 Fabrice P. Cordelieres

   License:
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/

/**
 * This class allows input and output of the plugin: importing bricks from a file saved on the disk/output the bricks to this file.
 * The bricks file is a markdown file: it both allows use in the plugin and further export as a formatted document.
 * @author fab
 *
 */
public class bricksIO {
	/** Default macroBricks file's name **/
	public static final String MACRO_BRICKS_FILE="macroBricks.md";
	
	/**
	 * Reads the macroBricks.md file, saved in the default folder
	 * @return and arrayList containing all the bricks, parsed from the file
	 */
	public static ArrayList<brick> readBrickFile(){
		ArrayList<brick> out=new ArrayList<brick>();
		String fileContent="";
		
		if(new File(MACRO_BRICKS_FILE).exists()) {
			try {
				BufferedReader br=new BufferedReader(new FileReader(MACRO_BRICKS_FILE));
				String line=br.readLine();
				while(line!=null) {
					fileContent+=line+"\n";
					line=br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(fileContent.length()>0) {
				String[] stringBricks=fileContent.split(brick.BRICK_SEPARATOR_STOP);
				for(String s:stringBricks) out.add(new brick(s));
			}
			
			out.sort(null);
			
		}
		
		return out;
	}
	
	/**
	 * Appends the provided brick to the macroBricks.md file, saved in the default folder.
	 * In case none has been saved yet, the file is created.
	 * @param brick the brick object to save to the macroBricks.md file.
	 */
	public static void appendBrickToFile(brick brick) {
		FileWriter fw=null;
		try {
			fw=new FileWriter(MACRO_BRICKS_FILE, true);
			fw.write(brick.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the provided brick to the macroBricks.md file, saved in the default folder.
	 * In case a file already exists, this method overwrites it without warning.
	 * @param bricks an ArrayList of brick objects to save to a new macroBricks.md file.
	 */
	public static void writeBricksFile(ArrayList<brick> bricks) {
		bricks.sort(null);
		FileWriter fw=null;
		try {
			fw=new FileWriter(MACRO_BRICKS_FILE, false);
			for(int i=0; i<bricks.size(); i++) fw.write(bricks.get(i).toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the macroBricks.md file, extracts the bricks and returns the bricks' names
	 * as a String array. In the absence of the file or in case no bricks were found, the 
	 * array only contains the "None" String
	 * @return the names of the bricks, as a String array
	 */
	public static String[] getBricksNames() {
		ArrayList<brick> bricks=readBrickFile();
		
		if(bricks.size()==0) return new String[] {"None"};
		String[] out=new String[bricks.size()];
		for(int i=0; i<bricks.size(); i++) out[i]=(i+1)+"-"+bricks.get(i).getName();
		
		return out;
	}
}
