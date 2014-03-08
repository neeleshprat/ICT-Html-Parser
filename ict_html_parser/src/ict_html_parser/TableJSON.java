package ict_html_parser;

public class TableJSON
{
		//HTML file name
		private String document;
		//character position
		private int from, to;
		private boolean contentTable = false;
		
//		private int rows;
//		private int rowsNull;
//		private int columns;
//		private int columnsWithNonString;		
//		private int dataCellsAvg;
//		private double variance;
//		private double stddev;
		
		
		public TableJSON(String doc, int fr, int t, boolean ct)
		{
			this.document = doc;
			this.from = fr;
			this.to = t;
			this.contentTable = ct;
		}
		
//		public TableJSON(String doc, int fr, int t, int r, int rn, int c, int cs, int da, double var, double sd, boolean ct)
//		{
//			this.document = doc;
//			this.from = fr;
//			this.to = t;
//			this.rows = r;
//			this.rowsNull = rn;
//			this.columns = c;
//			this.columnsWithNonString = cs;
//			this.dataCellsAvg = da;
//			this.variance = var;
//			this.contentTable = ct;
//			this.stddev = sd;
//		}
}
