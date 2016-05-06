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
			throw new FileNotFoundException("Input is expected to be absolute path to directory.");
		} else return (List<File>)(FileUtils.listFiles(baseDir, extensions, true));
	}
	
	public List<String> loadDataTypesFromFile(String file) throws IOException {
		File dataTypesFile = new File(file);
		List<String> dataTypes = new ArrayList<String>();
		if (!dataTypesFile.exists() || dataTypesFile.isDirectory()) {
			throw new FileNotFoundException("Input is expected to be absolute path to file.");
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
	
	public void writeXLSFile(String outDirName, String outFileName, String sheetName, List<Table> tables) throws IOException {
		HSSFWorkbook workbook;
		File file = new File(outDirName);

		if(file.isAbsolute()) {
			if(!file.exists()) {
				file.mkdirs();
				if(!file.exists()) {
					throw new IOException("Output directory couldn't be created.");
				}
			}
			
			String outputFileName = outDirName + File.separator + outFileName;
			FileOutputStream fileOutStream = new FileOutputStream(outputFileName);
			workbook = new HSSFWorkbook();
			
			createHSSFWorkbook(workbook, sheetName, tables);
			
			workbook.write(fileOutStream);	
			fileOutStream.flush();
			fileOutStream.close();
			workbook.close();					
		} else throw new IOException("Output path needs to be absolute.");
	}
	
	private void createHSSFWorkbook(HSSFWorkbook workbook, String sheetName, List<Table> tables) throws NullPointerException {
		if (workbook != null) {
			HSSFSheet sheet = workbook.createSheet(sheetName);
			List<String> fieldNames, cellValues;

			fieldNames = new ArrayList<String>(0);
			fieldNames.add("Tábla");
			fieldNames.add("Mező Neve");
			fieldNames.add("Mező Típusa");
			fieldNames.add("Mező Leírása");
			fieldNames.add("ÜOM");

			createHSSFRow(sheet, fieldNames);

			for (Table table : tables) {
				for (Row row : table.getRows()) {
					cellValues = new ArrayList<String>(0);

					cellValues.add(table.getTableName());
					cellValues.add(row.getField().getFieldName());
					cellValues.add(row.getField().getDataType());
					if (row.getUOM() != null) {
						cellValues.add(row.getDescription() + row.getUOM());
					} else
						cellValues.add(row.getDescription());
					cellValues.add(row.getUOM());

					createHSSFRow(sheet, cellValues);
				}
			}
		} else throw new NullPointerException("Excel workbook was null.");
	}
	
	private void createHSSFRow(HSSFSheet sheet, List<String> cellValues) {
		HSSFRow newRow = sheet.createRow(sheet.getLastRowNum() + 1);
		HSSFCell nextCell = newRow.createCell(newRow.getLastCellNum() + 1);
		
		for(String value : cellValues) {								
			nextCell.setCellValue(value);
			nextCell = newRow.createCell((int)newRow.getLastCellNum());
		}
	}
}