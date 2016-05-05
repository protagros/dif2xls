package hu.sol.main;

import java.io.File;
import java.util.List;

import hu.sol.parser.DataProcessor;
import hu.sol.parser.FileHandler;

public class Main {

	public static void main(String[] args) throws Exception {
		FileHandler fileHandler = new FileHandler();
		String[] extensions = new String[1];
		extensions[0] = "dif";
		List<File> fileList = fileHandler.getFilesByExtension(args[0], extensions);
		new DataProcessor().parseFilesToTables(fileList);
	}
}