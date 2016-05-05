package hu.sol.main;

import java.io.File;
import java.util.List;

import hu.sol.parser.DataProcessor;
import hu.sol.parser.FileHandler;

public class Main {

	public static void main(String[] args) throws Exception {
		FileHandler fileHandler = new FileHandler();
		String[] extensions = new String[1];
		List<String> dataTypes = fileHandler.loadDataTypesFromFile(args[1]); 
		extensions[0] = "dif";
		List<File> fileList = fileHandler.getFilesByExtension(args[0], extensions);		
		DataProcessor dp = new DataProcessor(dataTypes);
		
		dp.parseFilesToTables(fileList);
		fileHandler.writeXLSFile("e://suIT//tabla_teszt_3.xls", "teszt_2", dp.parseFilesToTables(fileList));
	}
}