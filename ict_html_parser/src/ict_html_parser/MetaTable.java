package ict_html_parser;

/*
 * This is a class which provides data about a table and the table itself.
 * You can extend this class with any necessary attributes you would like to save in the output file, 
 * but remember to also adapt the toString() method in order to have a proper representation of the table in the output file.
 */
public class MetaTable {

	//html contents
	private String text;
	//HTML file name
	private String document;
	//character position
	private int from, to;
	private int rows;
	private int rowsNull;
	private int columns;
	private int columnsWithNonString;
	private int[] dataCellLength;
	private int tagCount;
	private int linkCount;
	
	private boolean contentTable;
	//Constructor for less information
//	public MetaTable(String document, String text, int from, int to){
//		this.document = document;
//		this.text = text;
//		this.from = from;
//		this.to = to;
//	}
	
	//Constructor for more information
	public MetaTable(String document, String text, int from, int to, int rows, int columns, 
			int rowsNull, int columnsWithNonString, int[] dataCellLength, int tag, int link){
		this.document = document;
		this.text = text;
		this.from = from;
		this.to = to;
		this.rows = rows;
		this.columns = columns;
		this.rowsNull = rowsNull;
		this.columnsWithNonString = columnsWithNonString;
		this.dataCellLength = dataCellLength;
		this.tagCount = tag;
		this.linkCount = link;		
	}
	
	@Override
	public String toString() {
		int dataCellsAvg = calculateAverage(dataCellLength);
		double variance = calculateVariance(dataCellsAvg, dataCellLength);
		double stddev = Math.sqrt(variance);
	
		return "FileName: " + document + ";" + "StartPos: " + from + ";" + "EndPos" 
					+  to + ";" + "nor: " + rows + ";" + "noc: " + columns + ";" + "nullrows: " + rowsNull + ";" + 
					"columnsWithNoString: " + columnsWithNonString + ";" + "dataCellsAvg: " + dataCellsAvg + ";" + "StdDev: " + Double.toString(stddev);
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRowsNull() {
		return rowsNull;
	}

	public void setRowsNull(int rowsNull) {
		this.rowsNull = rowsNull;
	}

	public int getColumnsWithNonString() {
		return columnsWithNonString;
	}

	public void setColumnsWithNonString(int columnsWithNonString) {
		this.columnsWithNonString = columnsWithNonString;
	}

	public int[] getDataCellLength() {
		return dataCellLength;
	}

	public void setDataCellLength(int dataCellLength[]) {
		this.dataCellLength = dataCellLength;
	}
	
	//Calculate the average of the data cells
	private int calculateAverage(int[] dataCells)
	{
		int elements = 0;
		int sum = 0;
		try 
		{			
			for (int i = 0; i < dataCells.length; i++)
			{
				if(dataCells[i] != 0)
					elements++;
				sum = sum + dataCells[i];			
			}
			if(elements != 0)
				return sum/elements;
			else 
				return 0;
		} 
		catch (Exception e) {
			// TODO: handle exception
			if(elements != 0)
				return sum/elements;
			else 
				return 0;
		}
	}
	
	//Calculate the variance of the data cells
	private int calculateVariance(int avg, int[] dataCells)
	{
		int elements = 0;
		int varianceSum = 0;
		try 
		{			
			for (int i = 0; i < dataCells.length; i++)
			{
				if(dataCells[i] != 0)
				{
					elements++;
					varianceSum = varianceSum + (dataCells[i] - avg) * (dataCells[i] - avg);
				}							
			}
			if(elements != 0)
				return varianceSum/elements;
			else 
				return 0;
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			if(elements != 0)
				return varianceSum/elements;
			else 
				return 0;
		}
	}

	public boolean isContentTable() {
		return contentTable;
	}

	public void setContentTable(boolean contentTable) {
		this.contentTable = contentTable;
	}

	public int getTagCount() {
		return tagCount;
	}

	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}

	public int getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(int linkCount) {
		this.linkCount = linkCount;
	}
	
}
