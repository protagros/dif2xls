package hu.sol.main;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.util.StringUtil;

import hu.sol.parser.DataProcessor;
import hu.sol.parser.FileHandler;

public class Main {

	private static Options options = new Options();
	
	public static void main(String[] args) throws Exception {		
		System.setProperty("file.encoding", "UTF-8");
		setOptions();
		if(args.length != 0) {			
			CommanderKeen(createParser(args));
		} else printHelp();
		System.out.println(Charset.defaultCharset());
	}
	
	@SuppressWarnings("all")
	private static void setOptions() {
		options.addOption(OptionBuilder.withLongOpt("help").
										withDescription("prints commands with their descriptions").
										create("h"));
		options.addOption(OptionBuilder.withDescription("root directory of scannable files (scan is recursive)").
										isRequired().
										hasArg().withArgName("PATH").
										create("indir"));
		options.addOption(OptionBuilder.withDescription("directory of output files (created if missing)").
										isRequired().
										hasArg().withArgName("PATH").
										create("outdir"));
		options.addOption(OptionBuilder.withDescription("output filename with extension").
										isRequired().
										hasArg().withArgName("FILENAME").
										create("outfile"));
		options.addOption(OptionBuilder.withLongOpt("filter").
										withDescription("output filename with extension").
										isRequired().
										hasArg().withArgName("FILENAME").
										create("f"));		
		options.addOption(OptionBuilder.withLongOpt("extensions").
										withDescription("comma separated list of extensions to look for under root directory").
										isRequired().
										hasArgs().withArgName("LIST-EXTENSIONS").
										withValueSeparator(',').
										create("exts"));
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
				 DataProcessor dp = new DataProcessor(dataTypes);
				
				 dp.parseFilesToTables(fileList);
				 fileHandler.writeXLSFile(cmd.getOptionValue("outdir")+cmd.getOptionValue("outfile"), "teszt_2", dp.parseFilesToTables(fileList));
			}
		}
	}
}