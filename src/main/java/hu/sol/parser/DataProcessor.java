package hu.sol.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.sol.parser.bean.*;

public class DataProcessor {

	private String tableName = null, 
			tableDescription = null,	
					tableUOM = null;
					
	private List<String> dataTypes;

	public DataProcessor(List<String> dataTypes) {
		this.dataTypes = dataTypes;
	}

	public List<Table> parseFilesToTables(List<File> files) throws IOException {
		List<Table> tables = new ArrayList<Table>();

		for (File file : files) {
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

			if (row != null) {
				table.addRow(row);
			} else if (table.getTableName() == null) {
				table.setTableName(this.tableName);
				if(this.tableUOM != null) {
					table.setTableUOM(this.tableUOM);
				}
				if(this.tableDescription != null) {
					table.setTableDescription(this.tableDescription);
				}
			}
		}

		buffReader.close();
		fileReader.close();

		return table;
	}

	public Row parseLineToRow(String line) {
		Row row = null;
		int uomIndex = -1;
				
		if (line.lastIndexOf("TABLE") >= 0) {
			tableUOM = null;
			line = line.substring(5);
			this.tableName = (line.split("\\p{javaUpperCase}"))[0];			
			uomIndex = line.lastIndexOf("ÜOM");
			
			if(uomIndex >= 0) {
				this.tableUOM = line.substring(uomIndex);
				this.tableDescription = line.substring(this.tableName.length(), uomIndex);
			} else this.tableDescription = line.substring(this.tableName.length());
				
			return null;
		}
		
		if (line.lastIndexOf("FIELD") >= 0) {
			row = new Row();
			Field field = new Field();

			line = line.substring(5);

			field.setFieldName((line.split("\\p{javaUpperCase}")[0]));
			field.setDataType(searchForDataType(line));

			row.setField(field);
			uomIndex = line.lastIndexOf("ÜOM");						
			int	dataTypeEnd = line.lastIndexOf((field.getDataType())) + field.getDataType().length();
			if(uomIndex >= 0) { 				
				row.setUOM(line.substring(uomIndex));
				row.setDescription(line.substring(dataTypeEnd, uomIndex));
			} else row.setDescription(line.substring(dataTypeEnd));
		}

		return row;
	}

	private String searchForDataType(String line) {
		for (String dataType : dataTypes) {
			if (line.lastIndexOf(dataType) >= 0)
				return dataType;
		} 
		
		String message = "Unknown Datatype in line: " + line;
		System.out.println(message);
		
		return message;
	}
}