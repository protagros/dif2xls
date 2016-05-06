package hu.sol.main;

import java.io.File;
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

	private static final String DEFAULT_FILENAME = "bi_report.xls";
	private static final String DEFAULT_SHEETNAME = "bi_report_sheet";
	
	private static Options options = new Options();
	
	public static void main(String[] args) throws Exception {		
		String defaultOutputDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();

		setOptions();
		if(args.length != 0) {			
			CommanderKeen(createParser(args));
		} else printHelp();
	}
	
	@SuppressWarnings("all")
	private static void setOptions() {
		OptionGroup dataTypesOptionGroup = new OptionGroup();
		
		dataTypesOptionGroup.addOption(OptionBuilder.withLongOpt("filter").
													withDescription("path to file with all known datatypes").
													hasOptionalArg().withArgName("FILENAME").
													create("f"));
		dataTypesOptionGroup.addOption(OptionBuilder.withLongOpt("regex").
													withDescription("regular expression to match datatypes with").
													hasOptionalArg().withArgName("REGULAR EXPRESSION").
													create("r"));
				
		options.addOption(OptionBuilder.withLongOpt("help").
										withDescription("prints commands with their descriptions").
										create("h"));
		options.addOption(OptionBuilder.withDescription("root directory of scannable files (scan is recursive)").
										isRequired().
										hasArg().withArgName("PATH").
										create("indir"));
		options.addOption(OptionBuilder.withDescription("directory of output files (gets created if missing)").
										hasArg().withArgName("PATH").
										create("outdir"));
		options.addOption(OptionBuilder.withDescription("output filename with extension").
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
		 String header = "\n\nThe list of commands available\n\n";
		 String footer = "\nSources can be found @ https://github.com/protagros/dif2xls.git";
		 
		 HelpFormatter formatter = new HelpFormatter();
		 formatter.printHelp("exts2xls", header, options, footer, true);
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
	
	private static void CommanderKeen(CommandLine cmd) throws Exception {
		if (cmd != null) {
			if(cmd.hasOption('h')) {
				printHelp();
			} else {
				 FileHandler fileHandler = new FileHandler();
				 String[] extensions = cmd.getOptionValues("extensions");
				 List<String> dataTypes = fileHandler.loadDataTypesFromFile(cmd.getOptionValue("filter"));
				 List<File> fileList = fileHandler.getFilesByExtension(cmd.getOptionValue("indir"), extensions);
				 DataProcessor dp = new DataProcessor();
				
				 fileHandler.writeXLSFile(cmd.getOptionValue("outdir"), cmd.getOptionValue("outfile"), cmd.getOptionValue("sheet"), dp.parseFilesToTables(fileList));
			}
		}
	}
}