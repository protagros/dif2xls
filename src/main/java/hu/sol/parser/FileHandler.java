package hu.sol.parser;

import hu.sol.parser.bean.Row;
import hu.sol.parser.bean.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	public void writeXLSFile(String outputFileName, String sheetName, List<Table> tables) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName) ;

		for(Table table : tables) {
			for(Row row : table.getRows()) {
				HSSFRow hssfRow = sheet.createRow(sheet.getLastRowNum() + 1);
								
				HSSFCell hssfCell = hssfRow.createCell(hssfRow.getLastCellNum() + 1);
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