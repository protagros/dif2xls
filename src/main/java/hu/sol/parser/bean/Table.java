package hu.sol.parser.bean;

import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private String tableName, tableDescription;
	
	private List<Row> rows;
	
	public Table() {		
	}
	
	public Table(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableDescription() {
		return tableDescription;
	}

	public void setTableDescription(String tableDescription) {
		this.tableDescription = tableDescription;
	}

	
	public List<Row> getRows() {
		return rows;
	}
	
	public void addRow(Row newRow) {
		if( rows == null ) {
			rows = new ArrayList<Row>(0);			
		} 
		
		rows.add(newRow); 		
	}
	
	@Override
	public String toString() {		
		StringBuilder strBuilder;
		
		if(tableName != null) {
			strBuilder = new StringBuilder("Tabla: ").append(tableName).append("\n");
			if(rows != null) {
				for(Row row : rows) {
					strBuilder.append(row).append("\n");
				}
			} else strBuilder.append("\tA tablaban nem szerepelnek sorok.\n");
		} else return "Ures tabla.\n";
		
		return strBuilder.toString();
	}
}