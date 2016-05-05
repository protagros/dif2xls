package hu.sol.parser.helper;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter {

	private String extension;

	public ExtensionFilter(String extension) {
		this.extension = extension;
	}

	public boolean accept(File dir, String name) {
		if (name.lastIndexOf('.') > 0) {
			int lastIndex = name.lastIndexOf('.');
			String str = name.substring(lastIndex);
			if (str.equals("." + extension)) {
				return true;
			}
		}
		return false;
	}
}