package ict_html_parser;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import com.google.gson.*;
 

/*
 * Main class for starting the application
 */
public class Main extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton start, contentTable, noContentTable, collectTables;	
	private JTextArea destinationMetadataPathForContent, destinationMetadataPathForNoContent, sourcePath;
	private JLabel tableNumbersLabel, tableNumbers, remainingTableLabel, remainingTables;
	private ButtonListener listener;
	
	private int currentTableNumber, remainingTableNumber;
	
	private MetaTable[] tables;	
	TableLoader loader = HTMLTableExtractor.getInstance();

	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {		
		initializeGUI();
		this.tables = loadTables();
	}
	

	private void initializeGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.listener = new ButtonListener();
		this.setLayout(new FlowLayout());
		
		JPanel collectPanel = new JPanel();
		collectPanel.setLayout(new FlowLayout());        
        collectTables = new JButton("Collect Tables");
		collectTables.addActionListener(listener); 
		sourcePath = new JTextArea("D:/BusinessInformatics/Large_Scale_Extraction/workshop/HTML_File/");
		tableNumbersLabel = new JLabel("Number of tables: ");
		tableNumbers = new JLabel();
		
		collectPanel.add(collectTables);
		collectPanel.add(sourcePath);
		collectPanel.add(tableNumbersLabel);
		collectPanel.add(tableNumbers);
		
		JPanel saveContentMetadataPanel = new JPanel();
		saveContentMetadataPanel.setLayout(new FlowLayout());
		JLabel dstContentLabel = new JLabel("Save content table metadata file at:");
		saveContentMetadataPanel.add(dstContentLabel);
		destinationMetadataPathForContent = new JTextArea("D:/BusinessInformatics/Large_Scale_Extraction/workshop/");
		saveContentMetadataPanel.add(destinationMetadataPathForContent);
		
		JPanel saveNoContentMetadataPanel = new JPanel();
		saveNoContentMetadataPanel.setLayout(new FlowLayout());
		JLabel dstNoContentLabel = new JLabel("Save non content table metadata file at:");
		saveNoContentMetadataPanel.add(dstNoContentLabel);				
		destinationMetadataPathForNoContent = new JTextArea("D:/BusinessInformatics/Large_Scale_Extraction/workshop/");
		saveNoContentMetadataPanel.add(destinationMetadataPathForNoContent);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		start = new JButton("Start");
		start.addActionListener(listener);
		buttonPanel.add(start);
		contentTable = new JButton("Content Table");
		contentTable.addActionListener(listener);
		buttonPanel.add(contentTable);
		noContentTable = new JButton("No Content Table");
		noContentTable.addActionListener(listener);
		buttonPanel.add(noContentTable);
		remainingTableLabel = new JLabel("Remaining tables: ");
		buttonPanel.add(remainingTableLabel);
		remainingTables = new JLabel();
		buttonPanel.add(remainingTables);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0,1));
					
		mainPanel.add(collectPanel);
		mainPanel.add(saveContentMetadataPanel);
		mainPanel.add(saveNoContentMetadataPanel);
	
		mainPanel.add(buttonPanel);
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.add(mainPanel);
		this.setAlwaysOnTop(true);
        
		this.pack();
		this.setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == start) 
			{
				start.setEnabled(false);
				currentTableNumber = -1;
				try {
					displayNextTable();
					
				}catch(Exception ex) {ex.printStackTrace();}
			}
			else if(e.getSource() == contentTable) {
				try {
					
					tables[currentTableNumber].setContentTable(true);
					saveResult(tables[currentTableNumber]);									
					displayNextTable();	
					
					//display remaining tables
					remainingTableNumber--;
					remainingTables.setText(Integer.toString(remainingTableNumber));
					
					if(remainingTableNumber == 0)
					{
						contentTable.setEnabled(false);
						noContentTable.setEnabled(false);
					}
					
				}catch(Exception ex) {ex.printStackTrace();}
			}
			else if (e.getSource() == noContentTable) {
				try {
					
					tables[currentTableNumber].setContentTable(false);
					saveResult(tables[currentTableNumber]);									
					displayNextTable();	
					
					//display remaining tables
					remainingTableNumber--;
					remainingTables.setText(Integer.toString(remainingTableNumber));
					
					if(remainingTableNumber == 0)
					{
						contentTable.setEnabled(false);
						noContentTable.setEnabled(false);
					}
					
				}catch(Exception ex) {ex.printStackTrace();}
			}
			else if (e.getSource() == collectTables) {
				try 
				{
					long startTime = System.currentTimeMillis();
					//extracts the tables from the HTML files at the given location
					extractTables();
					long stopTime = System.currentTimeMillis();
					
					long elapsedTime = stopTime - startTime;
					System.out.println("Time to extract: " + elapsedTime);
				    					
					int totalTables = HTMLTableExtractor.getInstance().currentTableNumber;
					remainingTableNumber = totalTables;
					tableNumbers.setText(Integer.toString(totalTables));
					remainingTables.setText(Integer.toString(remainingTableNumber));
					collectTables.setEnabled(false);
				}
				catch(Exception ex) 
				{ex.printStackTrace();}
			}
		}
	}
	
	//Gets the path to save the Metadata file for content table
	private String getdestinationMetadataPathForContent() {
		String tmp = destinationMetadataPathForContent.getText();
		tmp = tmp.replace('\\', '/');
		if(tmp.endsWith("/"))
			return tmp;
		return tmp + "/";
	}
	
	//Gets the path to save the Metadata file for non content table
	private String getdestinationMetadataPathForNoContent() {
		String tmp = destinationMetadataPathForNoContent.getText();
		tmp = tmp.replace('\\', '/');
		if(tmp.endsWith("/"))
			return tmp;
		return tmp + "/";
	}
	
	private String getsourcePath() {
		String tmp = sourcePath.getText();
		tmp = tmp.replace('\\', '/');
		if(tmp.endsWith("/"))
			return tmp;
		return tmp + "/";
	}
	
	//reads the HTML file and extracts tables from it
	public void extractTables()
	{
		try 
		{						  
			File directory = new File(getsourcePath());
			File[] allFiles = directory.listFiles();
						
			for (int j = 0; j < allFiles.length; j++)
			{
				File fileHtml = allFiles[j];
				
				if(fileHtml.getName() != null)
					HTMLParser.filename = fileHtml.getName();
				
    		    BufferedReader in = new BufferedReader(new FileReader(fileHtml));
    		    
    		    String str;
    		    StringBuilder contentBuilder = new StringBuilder();
    		    
    		    while ((str = in.readLine()) != null) {
    		        contentBuilder.append(str);
    		    }
    		    in.close();
    		    
    		    Reader reader = new StringReader(contentBuilder.toString());
    		    
    		    //File contents sent to HTML Parser
    		    if(reader != null) 
    			{
					HTMLEditorKit.Parser parser = new ParserDelegator();
	        		HTMLParser callback = new HTMLParser();
	        		parser.parse(reader, callback, true);	        		
	        		reader.close();
    			}
			}			
			
		}		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//Reads the file at the path and returns its content in string format
	public static String readFile(String file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();

		// Get the file's size and then map it into memory
		long sz = fc.size();
		MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
		fc.close();

		// decoder with utf-8 charset
		CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);

		// Decode the file into a char buffer
		CharBuffer cb = decoder.decode(bb);

		String string = cb.toString();
		fis.close();
		
		return string;
	}
	
	//Displays the next table from the collected tables in the browser
	private void displayNextTable() throws URISyntaxException, IOException {
		currentTableNumber++;	
		if(currentTableNumber >= tables.length) {
			File f = new File(getdestinationMetadataPathForContent() + "tmp.html");
			f.delete();
			System.exit(0);
		}
		
		URI path = new URI(getdestinationMetadataPathForContent() + "tmp.html");
		File tmp = new File(getdestinationMetadataPathForContent() + "tmp.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
		if(tables[currentTableNumber] != null)
		{
		bw.append("<html>" + tables[currentTableNumber].getText() + "</html>");
		bw.close();
		Desktop.getDesktop().browse(path);
		}
	}
	
	//Saves the metadata about the table in the JSON file
	private void saveResult(MetaTable objTable) throws IOException {
		
		TableJSON objTableJSON = new TableJSON(objTable.getDocument(), objTable.getFrom()
				, objTable.getTo(), objTable.isContentTable());
		
	
		Gson gson = new Gson();
		String json = gson.toJson(objTableJSON);
		
		try {
			if(objTable.isContentTable())
			{
				//write converted JSON data to a file
				FileWriter writer = new FileWriter(getdestinationMetadataPathForContent() + objTable.getDocument() + UUID.randomUUID() + ".json");
				writer.write(json);
				writer.close();
			}
			else {
				//write converted JSON data to a file
				FileWriter writer = new FileWriter(getdestinationMetadataPathForNoContent() + objTable.getDocument() + UUID.randomUUID() + ".json");
				writer.write(json);
				writer.close();
			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Gets all the tables collected
	private MetaTable[] loadTables() {
		
		return loader.getTables();
	}
}
