package org.jvnet.hudson.plugins.fit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import fit.Fixture;
import fit.Parse;

public class FitFileRunner {
	public static File process(String filename, String outputFilename)
			throws IOException {
		String input = convertStreamToString(getInputStream(filename));
		Fixture fixture = new Fixture();
		Parse tables;
		try {
			if (StringUtils.contains(input, "<wiki>")) {
				tables = new Parse(input, new String[] { "wiki", "table", "tr",
						"td" }).parts;
			} else {
				tables = new Parse(input, new String[] { "table", "tr", "td" });
			}
			fixture.doTables(tables);
		} catch (Exception e) {
			tables = new Parse("body", "Unable to parse input. Input ignored.",
					null, null);
			fixture.exception(tables, e);
		}
		return generateOutputFile(outputFilename, tables);
	}

	private static File generateOutputFile(String outputFilename, Parse tables)
			throws IOException {
		File outputFile = new File(outputFilename);
		PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(
				outputFile)));
		tables.print(output);
		output.close();
		return outputFile;
	}

	private static InputStream getInputStream(String filename)
			throws FileNotFoundException {
		InputStream is = FitFileRunner.class.getResourceAsStream(filename);
		if (is == null) {
			throw new FileNotFoundException(filename
					+ " cannot be opened because it does not exist");
		}
		return is;
	}

	private static String convertStreamToString(InputStream inputStream)
			throws FileNotFoundException, IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);

		int numberElementsRead;
		StringBuffer stringBuffer = new StringBuffer();
		final int MAX_CHUNK_SIZE = 1000;
		do {
			char[] cs = new char[MAX_CHUNK_SIZE];
			numberElementsRead = reader.read(cs);
			stringBuffer.append(cs, 0, numberElementsRead);
		} while (numberElementsRead == MAX_CHUNK_SIZE);
		return stringBuffer.toString();
	}

}
