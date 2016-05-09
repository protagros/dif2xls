package hu.sol.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hu.sol.parser.DataProcessor;
import hu.sol.parser.FileHandler;

@SuppressWarnings("deprecation")
public class Main {

	private static final String DEFAULT_OUTPUT_FILENAME = "bi_report.xls";
	private static final String DEFAULT_INPUT_FILTER_FILENAME = "datatypes.lst";
	private static final String DEFAULT_OUTPUT_SHEETNAME = "bi_report_sheet";
	private static final String DEFAULT_INPUT_EXTENSION = "dif";
	private static final String HELP_HEADER = "\n\nThe list of commands available\n\n";
	private static final String HELP_FOOTER = "\nSources can be found @ https://github.com/protagros/dif2xls.git";
	
	private static Options options = new Options();
	
	public static void main(String[] args) throws Exception {		
		String appRootDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();

		setOptions();
		
		if(args.length != 0) {			
			CommanderKeen(createParser(args), appRootDir);
		} else printHelp();
	}
	
	@SuppressWarnings("all")
	private static void setOptions() {
		OptionGroup dataTypesOptionGroup = new OptionGroup();
		
		dataTypesOptionGroup.addOption(OptionBuilder.withLongOpt("filter").
													withDescription("path to file with all known datatypes (uses datatypes.lst if no argument received)").
													hasOptionalArg().withArgName("FILENAME").
													create("f"));
		dataTypesOptionGroup.addOption(OptionBuilder.withLongOpt("regex").
													withDescription("regular expression to match datatypes with (uses default regex if no argument received)").
													hasOptionalArg().withArgName("REGEX").
													create("r"));
				
		options.addOption(OptionBuilder.withLongOpt("help").
										withDescription("prints help").
										create("h"));
		options.addOption(OptionBuilder.withLongOpt("verbose").
										withDescription("tells user how command line arguments have been interpreted").
										create("v"));		
		options.addOption(OptionBuilder.withDescription("root directory of files to be scanned (recursively)").
										isRequired().
										hasArg().withArgName("PATH").
										create("indir"));
		options.addOption(OptionBuilder.withDescription("directory of output file").
										hasArg().withArgName("PATH").
										create("outdir"));
		options.addOption(OptionBuilder.withDescription("output filename").
										hasArg().withArgName("FILENAME").
										create("outfile"));
		options.addOption(OptionBuilder.withLongOpt("sheet").
										withDescription("output excel sheet name").
										hasArg().withArgName("SHEETNAME").
										create("s"));		
		options.addOption(OptionBuilder.withLongOpt("extensions").
										withDescription("comma separated list of extensions to look for under root directory").
										hasArgs().withArgName("file1,file2,..fn").
										withValueSeparator(',').
										create("exts"));
		
		options.addOptionGroup(dataTypesOptionGroup);
	}
	
	private static void printHelp() {
		 HelpFormatter formatter = new HelpFormatter();
		 formatter.printHelp("exts2xls", HELP_HEADER, options, HELP_FOOTER, true);
	}
	
	private static CommandLine createParser(String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println("Commandline parser failed to load due to: " + e.getMessage());
			return null;
		}
	}
	
	private static void CommanderKeen(CommandLine cmd, String appRootDir) throws Exception {
		String filterFilePath = null, regex = null, inDir = null, outDir = null, outFile = null, sheetName = null;
		String[] extensions = null;
		List<String> dataTypes = null;
		List<File> fileList = null;		
		DataProcessor dataProcessor = new DataProcessor();
		FileHandler fileHandler = new FileHandler();
		
		if (cmd != null) {
			if(cmd.hasOption('h')) {
				printHelp();
			} else {
				if (cmd.hasOption('f')) {
					if (cmd.getOptionValue('f') != null) {
						filterFilePath = cmd.getOptionValue('f');						
					} else {
						filterFilePath = appRootDir + "/" + DEFAULT_INPUT_FILTER_FILENAME;						
					}
					dataTypes = fileHandler.loadDataTypesFromFile(filterFilePath);
					dataProcessor = new DataProcessor(dataTypes);
				} else {
					if (cmd.hasOption('r')) {
						System.out.println("regex");
						if (cmd.getOptionValue('r') != null) {
							System.out.println("van érték");
							regex = cmd.getOptionValue('r');
							dataProcessor = new DataProcessor(regex);
						}
					}
				}
				
				if(cmd.hasOption("indir")) {
					inDir = cmd.getOptionValue("indir");
				} else {
					inDir = appRootDir;
				}
				
				if(cmd.hasOption("outdir")) {
					outDir = cmd.getOptionValue("outdir");
				} else {
					outDir = appRootDir;
				}

				if(cmd.hasOption("outfile")) {
					outFile = cmd.getOptionValue("outfile");
				} else {
					outFile = DEFAULT_OUTPUT_FILENAME;
				}
				
				if(cmd.hasOption('s')) {
					sheetName = cmd.getOptionValue('s');
				} else sheetName = DEFAULT_OUTPUT_SHEETNAME;
				
				if(cmd.hasOption("exts")) {
					extensions = cmd.getOptionValues("exts");
				} else extensions = new String[]{DEFAULT_INPUT_EXTENSION}; 

				if(filterFilePath != null) {
					dataTypes = fileHandler.loadDataTypesFromFile(filterFilePath);
				}
								 
				fileList = fileHandler.getFilesByExtension(inDir, extensions);											
				
				if (cmd.hasOption('v')) {
					getInfo(regex, inDir, outDir, outFile, sheetName, extensions, dataTypes, fileList);
				}
				
				fileHandler.writeXLSFile(outDir, outFile, sheetName, dataProcessor.parseFilesToTables(fileList));
			}
		}
	}

	private static void getInfo(String regex, String inDir, 
			String outDir, String outFile, String sheetName, 
			String[] extensions, List<String> dataTypes,
			List<File> fileList) throws IOException {
		
		System.out.println("input directory: " + inDir);
		
		System.out.println("using extensions: ");
		for(String ext : extensions) {
			System.out.println("\t" + ext);
		}
		
		System.out.println("processing files: " + fileList.size());
		
		System.out.println("output file: " + outDir + "\\" + outFile);
		System.out.println("output sheetname: " + sheetName);
		
		if(dataTypes != null) {
			System.out.println("data types checked:");
			for(String type : dataTypes) {
				System.out.println(type);
			}
		} else if(regex != null) {
			System.out.println("regex:\n" + regex);
		} else System.out.println("regex: SYSTEM DEFAULT\n\t[A-Z]+\\d*\\(?\\d*,?\\d*\\)?");
	}
}