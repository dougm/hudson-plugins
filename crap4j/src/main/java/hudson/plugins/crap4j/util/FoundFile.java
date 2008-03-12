package hudson.plugins.crap4j.util;

import java.io.File;

public class FoundFile {
	
	private final File file;
	private final String relativePath;

	public FoundFile(File file, String relativePath) {
		super();
		this.file = file;
		this.relativePath = relativePath;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String getRelativePath() {
		return this.relativePath;
	}
}
