package ict_html_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

;

public class HTMLTableExtractor implements TableLoader
{
	private static HTMLTableExtractor instance;
	public int currentTableNumber = -1;
	MetaTable[] tables;

    static {
        instance = new HTMLTableExtractor();
    }

    private HTMLTableExtractor() { 
        // hidden constructor
    }    

    public static HTMLTableExtractor getInstance() {
        return instance;
    }
    
    	
	//gets all the tables stored...may be content or non-content
	public MetaTable[] getTables()
	{		
		return tables;
	}
	
	//adds the tables to the array
	public void setTables(MetaTable metadata)
	{
		try 
		{
			if(HTMLTableExtractor.getInstance().tables != null)
			{
				if(currentTableNumber < tables.length)
				{
					HTMLTableExtractor.getInstance().tables[currentTableNumber] = metadata;
				}
				else 
				{
					expandTable();					
					HTMLTableExtractor.getInstance().tables[currentTableNumber] = metadata;  //the length of data in the datacell
				}
			}
			else {
				expandTable();
				HTMLTableExtractor.getInstance().tables[currentTableNumber] = metadata;
			}
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//Expands the size of the MetaTable array in case more tables are extracted than the size of the current array
	private void expandTable()
	{
		//create new array with 100 more places
		int length;
		if(HTMLTableExtractor.getInstance().tables != null)
			length = HTMLTableExtractor.getInstance().tables.length;
		else
			length = 0;
		
		MetaTable[] temp = new MetaTable[length  + 100];
		if(HTMLTableExtractor.getInstance().tables != null)
		//copy the contents to the temporary array
			System.arraycopy(HTMLTableExtractor.getInstance().tables, 0, temp, 0, length);
		//assign the old array the contents and dimension of the temporary array to have array with places for more data
		HTMLTableExtractor.getInstance().tables = temp;
	}
	
		
}