package macroBricks.bricks;

/**
*
*  brick.java, 23 nov. 2017
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
 * This class is aimed at generating and storing macro bricks, which are user-defined ImageJ macro functions,
 * their documentation, dependencies and code
 * @author fab
 *
 */
public class brick implements Comparable<brick>{
	
	
	/** The brick's name **/
	String name="";
	
	/** The brick's documentation **/
	String documentation="";
	
	/** The brick's function code **/
	String function="";
	
	/** Tag used at start of the brick block **/
	public static final String BRICK_SEPARATOR_START="***\n# ";
	
	/** Tag used at the end of the brick block **/
	public static final String BRICK_SEPARATOR_STOP="\n```\n---\n";
	
	/** Tag used before the documentation **/
	public static final String NAME_DOCUMENTATION_SEPARATOR="\n## Documentation\n";
	
	/** Tag used before the code **/
	public static final String DOCUMENTATION_FUNCTION_SEPARATOR="\n## Code\n```java\n";
	
	
	
	/**
	 * Creates a new empty brick
	 */
	public brick() {
	}
	
	/**
	 * Creates a new empty brick based on the provided content
	 * @param content the content to parse as a brick
	 */
	public brick(String content) {
		parseContent(content);
		checkFields();
	}
	
	/**
	 * Creates a new brick object based on individual arguments
	 * @param name brick's name
	 * @param documentation brick's documentation
	 * @param function brick's function code
	 */
	public brick(String name, String documentation, String function) {
		this.name=name;
		this.documentation=documentation;
		this.function=function;
		checkFields();
	}
	
	/**
	 * Checks all the fields are non null or replaces them with empty values
	 */
	void checkFields() {
		if(name==null) name="";
		if(documentation==null) documentation="";
		if(function==null) function="";
	}
	
	/**
	 * Parses a String into a brick, supposing the provided String is field delimited
	 * @param content the String to parse
	 */
	void parseContent(String content) {
		name=getField(BRICK_SEPARATOR_START, NAME_DOCUMENTATION_SEPARATOR, content);
		documentation=getField(NAME_DOCUMENTATION_SEPARATOR, DOCUMENTATION_FUNCTION_SEPARATOR, content);
		function=getField(DOCUMENTATION_FUNCTION_SEPARATOR, BRICK_SEPARATOR_STOP, content);
	}
	
	/**
	 * Parses the input content using the provided delimiters and returns the portion of text in beetwen.
	 * In case the start delimiter is not found, returns an empty String.
	 * In case the start delimiter is found but the stop delimiter is not found, returns a String extending from the start to the end of the content.
	 * @param delimiterStart the start delimiter
	 * * @param delimiterStop the stop delimiter
	 * @param content the content to parse
	 * @return the text encapsulated between the delimiters. In case the start delimiter is not found, returns an empty String. In case the start delimiter is found but the stop delimiter is not found, returns a String extending from the start to the end of the content.
	 */
	public static String getField(String delimiterStart, String delimiterStop, String content) {
		if(!content.isEmpty()) {
			int start=content.indexOf(delimiterStart)+delimiterStart.length();
			int stop=content.indexOf(delimiterStop, start!=-1?start:0);
			if(start>=0 && stop<0) return content.substring(start);
			if(start>=0 && stop>=0) return content.substring(start, stop);
		}
		return "";
	}
	
	@Override
	public String toString() {
		return	BRICK_SEPARATOR_START
				+name
				+NAME_DOCUMENTATION_SEPARATOR
				+documentation
				+DOCUMENTATION_FUNCTION_SEPARATOR
				+function
				+BRICK_SEPARATOR_STOP;
	}

	/**
	 * Returns the name of the current brick
	 * @return the name of the current brick, as a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Replaces the name of the current brick by the provided String
	 * @param name the new name of the element, as a String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the documentation of the current brick
	 * @return the documentation of the current brick, as a String
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * Replaces the documentation of the current brick by the provided String
	 * @param documentation the new documentation of the element, as a String
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Returns the function code of the current brick
	 * @return the function code of the current brick, as a String
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Replaces the function code of the current brick by the provided String
	 * @param function the new function code of the element, as a String
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public int compareTo(brick b) {
		return getName().compareToIgnoreCase(b.getName());
	}
}
