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
		HSSFSheet sheet = workbook.createSheet(sheetName) ;
		HSSFRow hssfRow = sheet.createRow(sheet.getLastRowNum());
		HSSFCell hssfCell = hssfRow.createCell(hssfRow.getLastCellNum() + 1);		
		
		hssfCell.setCellValue("Tábla");
		hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());		
		hssfCell.setCellValue("Mező Neve");
		hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());		
		hssfCell.setCellValue("Mező Típusa");
		hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());		
		hssfCell.setCellValue("Mező Leírása");
		hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());		
		hssfCell.setCellValue("ÜOM");
		System.out.println(hssfCell.getStringCellValue() + hssfCell.getRowIndex()+hssfCell.getColumnIndex());
		//hssfRow = sheet.createRow(sheet.getLastRowNum());
		
		for(Table table : tables) {
			hssfRow = sheet.createRow(sheet.getLastRowNum() + 1);
			hssfCell = hssfRow.createCell(hssfRow.getLastCellNum() + 1);
			hssfCell.setCellValue(table.getTableName());
			hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
			hssfCell.setCellValue(table.getTableDescription());
			hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
			hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
			hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
			hssfCell.setCellValue(table.getTableUOM());

			for(Row row : table.getRows()) {
				hssfRow = sheet.createRow(sheet.getLastRowNum() + 1);
								
				hssfCell = hssfRow.createCell(hssfRow.getLastCellNum() + 1);
				hssfCell.setCellValue(table.getTableName());
				hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
				hssfCell.setCellValue(row.getField().getFieldName());
				hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
				hssfCell.setCellValue(row.getField().getDataType());
				hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
				hssfCell.setCellValue(row.getDescription());
				hssfCell = hssfRow.createCell((int)hssfRow.getLastCellNum());
				hssfCell.setCellValue(row.getUOM());
			}
			sheet.createRow(sheet.getLastRowNum() + 1);
		}
		
		FileOutputStream fileOutStream = new FileOutputStream(outputFileName);
		
		workbook.write(fileOutStream);		
		fileOutStream.flush();
		fileOutStream.close();
		workbook.close();
	}	
}