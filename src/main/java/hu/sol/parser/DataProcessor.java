package hu.sol.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.sol.parser.bean.*;

public class DataProcessor {

	public List<Table> parseFilesToTables(List<File> files) throws IOException {
		List<Table> tables = new ArrayList<Table>();

		for (File file : files) {
			System.out.println("Új fájl.");
			tables.add(parseFileIntoTable(file));
		}

		return tables;

	}

	private Table parseFileIntoTable(File file) throws IOException {
		Table table = new Table();
		
		FileReader fileReader = new FileReader(file);
		BufferedReader buffReader = new BufferedReader(fileReader);

		String line;		
		while ((line = buffReader.readLine()) != null) {
			Row row = parseLineToRow(line);
			
			if(row != null) {
				table.addRow(row); 
			}
		}

		buffReader.close();
		fileReader.close();
		
		return table;
	}

	public Row parseLineToRow(String line) {
		if(line.lastIndexOf("INDEX") >= 0 || line.lastIndexOf("CONSTRAINT") >= 0) {
			return null;
		}  
		
		Row row = new Row();
		
		if (line.lastIndexOf("TABLE") >= 0) {			
			line = line.substring(5);
			System.out.println(line);			
			System.out.println((line.split("\\p{javaUpperCase}"))[0]);
			
		}  else 
			if (line.lastIndexOf("FIELD") >= 0) {
		
			}				
				
		return row;
	}
}