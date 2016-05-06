package hu.sol.parser;

import hu.sol.parser.bean.Row;
import hu.sol.parser.bean.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FileHandler {

	public List<File> getFilesByExtension(String dir, String[] extensions) throws Exception {		
		File baseDir = new File(dir);

		if (!baseDir.exists() || !baseDir.isDirectory()) {
			throw new FileNotFoundException("Input is expected to be path of a directory.");
		} else return (List<File>)(FileUtils.listFiles(baseDir, extensions, true));
	}
	
	public List<String> loadDataTypesFromFile(String file) throws IOException {
		File dataTypesFile = new File(file);
		List<String> dataTypes = new ArrayList<String>();
		if (!dataTypesFile.exists() || dataTypesFile.isDirectory()) {
			throw new FileNotFoundException("Input is expected to be a file in a directory.");
		} else {
			FileReader fileReader = new FileReader(file);
			BufferedReader buffReader = new BufferedReader(fileReader);

			String line;
			while ((line = buffReader.readLine()) != null) {
				dataTypes.add(line);
			}

			buffReader.close();
			fileReader.close();
		}
		
		return dataTypes;
	}		
	
	public void writeXLSFile(String outputFileName, String sheetName, List<Table> tables) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		List<String> fieldNames, headerNames, cellValues;
		
		fieldNames = new ArrayList<String>(0);
		fieldNames.add("Tábla");
		fieldNames.add("Mező Neve");
		fieldNames.add("Mező Típusa");
		fieldNames.add("Mező Leírása");
		fieldNames.add("ÜOM");
		
		createHSSFRow(sheet, fieldNames);
		
		for(Table table : tables) {
			headerNames = new ArrayList<String>(0);
			
			headerNames.add(table.getTableName());
			headerNames.add(table.getTableDescription());
			headerNames.add("");
			headerNames.add("");
			headerNames.add(table.getTableUOM());

			createHSSFRow(sheet, headerNames);
			
			for(Row row : table.getRows()) {
				cellValues = new ArrayList<String>(0);
				
				cellValues.add(table.getTableName());
				cellValues.add(row.getField().getFieldName());
				cellValues.add(row.getField().getDataType());
				if(row.getUOM() != null) {
					cellValues.add(row.getDescription() + row.getUOM());
				} else cellValues.add(row.getDescription());				
				cellValues.add(row.getUOM());
				
				createHSSFRow(sheet, cellValues);
			}
		}
		
		FileOutputStream fileOutStream = new FileOutputStream(outputFileName);
		
		workbook.write(fileOutStream);	
		fileOutStream.flush();
		fileOutStream.close();
		workbook.close();
	}
	
	private HSSFRow createHSSFRow(HSSFSheet sheet, List<String> cellValues) {
		HSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
		HSSFCell nextCell = newRow.createCell(newRow.getLastCellNum() + 1);
		
		for(String value : cellValues) {								
			nextCell.setCellValue(value);
			nextCell = newRow.createCell((int)newRow.getLastCellNum());
		}
		
		return newRow;
	}
}