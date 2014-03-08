package ict_html_parser;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.*;

class HTMLParser extends HTMLEditorKit.ParserCallback {
	
	private boolean encounteredATable = false; 
	private boolean encounteredAHeader = false;
	private boolean encounteredARow = false;
	private boolean encounteredADataCell = false;
	
	public static String filename;
	StringBuilder textBuilder = new StringBuilder();
    
    private int startOfPosition;
    private int endOfPosition;
	private int rows;	
	private int columns;
	private int rowsNull;
	private int columnsWithNonString;
	private int[] dataCellLength;
	private int tagCount;
	private int linkCount;
	
	private int currentDataCell = -1;
 

    public void handleText(char[] data, int pos) 
    {
    	if(encounteredATable) 
    		textBuilder.append(new String(data));   		
    	
    	if(encounteredARow)
    	{
    		if(data == null)
    			rowsNull++;
    		else if(data.length == 0)
    			rowsNull++;
    	}
    	if(encounteredAHeader)
    	{
    		try {
    			//checks if the column name is non string type (contains any integer)
    			if(Integer.valueOf(new String(data)) instanceof Integer)
        			columnsWithNonString++;
				
			} catch (Exception e) {
				// TODO: handle exception
//				e.printStackTrace();
			}
    		
    	}
    	if(encounteredADataCell)
    	{
    		currentDataCell++;
    		if(dataCellLength != null)
    		{
	    		if(currentDataCell < dataCellLength.length)
	    			dataCellLength[currentDataCell] = data.length; //the length of data in the datacell
	    		else {
	    			expandCellArray();		
//	    			dataCellLength[currentDataCell] = data.length;  //the length of data in the datacell
				}
    		}
    		else {
				expandCellArray();
				dataCellLength[currentDataCell] = data.length;
			}
    		
    	}
    }
        
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) 
    {    	   	
        if((t == HTML.Tag.TABLE)) 
    	{
			encounteredATable = true;
			startOfPosition = pos;
			
			//for start of the table make them zero
			rows = 0;
			columns = 0;
			rowsNull = 0;
			columnsWithNonString = 0;
			dataCellLength = new int[0];
			
			//delete the previous table contents
			textBuilder.delete(0, textBuilder.length());
			textBuilder.append("<table>");			
    	} 
        if((t == HTML.Tag.TH)) 
    	{
			encounteredAHeader = true;			
			textBuilder.append("<th>");
    	}
        if((t == HTML.Tag.TR)) 
    	{
			encounteredARow = true;			
			textBuilder.append("<tr>");
    	}
        if((t == HTML.Tag.TD)) 
    	{
			encounteredADataCell = true;			
			textBuilder.append("<td>");
    	}
        
    }

    public void handleEndTag(HTML.Tag t, int pos) 
    {
    	if(t == HTML.Tag.TABLE) 
    	{
    		tagCount++;
    		if(encounteredATable)
    		{
    			if(rows > 2 && columns > 2)
    			{
    			encounteredATable = false;
				endOfPosition = pos;
				textBuilder.append("</table>");
				
				//Save the table to the Metatables
				HTMLTableExtractor.getInstance().currentTableNumber++;
				MetaTable objMetaTable = new MetaTable(filename, new String(textBuilder), 
						startOfPosition, endOfPosition, rows , columns, rowsNull, 
						columnsWithNonString, dataCellLength, tagCount, linkCount);
				
				HTMLTableExtractor.getInstance().setTables(objMetaTable);  
    			}
    		}
    	}
    	if((t == HTML.Tag.TH)) 
    	{
			encounteredAHeader = false;			
			textBuilder.append("</th>");
			if(rows == 0)
				columns++; // increment column numbers
			tagCount++;
    	}
        if((t == HTML.Tag.TR)) 
    	{
			encounteredARow = false;			
			textBuilder.append("</tr>");
				
			rows++; //Increment Rows
			tagCount++;
			
    	}
        if((t == HTML.Tag.TD)) 
    	{
			encounteredADataCell = false;			
			textBuilder.append("</td>");
			tagCount++;
			if(rows == 0)
				columns++; //Increment column because in such tables no header tag <th> was used so the <td> of first rows are columns
    	}
        if(t== HTML.Tag.A)
        {
        	linkCount++;
        }
    }
    
  //Expands the size of the MetaTable array in case more tables are extracted than the size of the current array
  	private void expandCellArray()
  	{
  		try {
	  		//create new array with 100 more places
	  		int length;
	  		if(dataCellLength != null)
	  			length = dataCellLength.length;
	  		else
	  			length = 0;
	  		
	  		int[] temp = new int[length  + 100];
	  		if(dataCellLength != null)
	  		//copy the contents to the temporary array
	  			System.arraycopy(dataCellLength, 0, temp, 0, length);
	  		//assign the old array the contents and dimension of the temporary array to have array with places for more data
	  		dataCellLength = temp;
	  		} catch (Exception e) {
				// TODO: handle exception
	  			e.printStackTrace();
		}
  	}
    
}