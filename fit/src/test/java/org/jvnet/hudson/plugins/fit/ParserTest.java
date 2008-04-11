package org.jvnet.hudson.plugins.fit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.jvnet.hudson.plugins.fit.HtmlContentHandler.FitResult;
import org.xml.sax.InputSource;

public class ParserTest extends TestCase {

	public void testCountExpectationProblems() throws IOException {
		assertEquals(0, parseExtensionsFileFromStandardFitDistro()
				.getExpectationsNumber());
		assertEquals(2, parseFixturesFileFromStandardFitDistro()
				.getExpectationsNumber());
		assertEquals(4, parseAnnotationsFileFromStandardFitDistro()
				.getExpectationsNumber());
	}

	public void testCountExceptionsErrors() throws IOException {
		assertEquals(14, parseFixturesFileFromStandardFitDistro()
				.getErrorsNumber());
		assertEquals(0, parseExtensionsFileFromStandardFitDistro()
				.getErrorsNumber());
		assertEquals(4, parseAnnotationsFileFromStandardFitDistro()
				.getErrorsNumber());
	}

	private HtmlContentHandler.FitResult parseAnnotationsFileFromStandardFitDistro()
			throws IOException {
		return parse("annotation.html");
	}

	private HtmlContentHandler.FitResult parseFixturesFileFromStandardFitDistro()
			throws IOException {
		return parse("fixtures.html");
	}

	private HtmlContentHandler.FitResult parseExtensionsFileFromStandardFitDistro()
			throws IOException {
		return parse("extensions.html");
	}

	public void testFitAllSuccess() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciAllSuccess.html");
		assertEquals(0, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	public void testFitFixtureNotFound() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciFixtureNotFound.html");
		assertEquals(1, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	public void testFitMethodNotFound() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciMethodNotFound.html");
		assertEquals(1, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	public void testFitAttributeNotFound() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciAttributeNotFound.html");
		assertEquals(1, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	public void testFitExpectationFailure() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciExpectationFailure.html");
		assertEquals(0, allSuccess.getErrorsNumber());
		assertEquals(1, allSuccess.getExpectationsNumber());
	}

	public void testFitNoContent() throws IOException {
		FitResult allSuccess = runFitAndParseResult("FibonacciNoFixture.html");
		assertEquals(0, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	public void testFitNoContent_TextContainsExpected() throws IOException {
		FitResult allSuccess = parse("FibonacciTextExpected.html");
		assertEquals(0, allSuccess.getErrorsNumber());
		assertEquals(0, allSuccess.getExpectationsNumber());
	}

	private FitResult runFitAndParseResult(String inputFilename)
			throws IOException, FileNotFoundException {
		File outpudirectory = new File("target" + File.separator
				+ "fit-test-results");
		if (!outpudirectory.exists()) {
			outpudirectory.mkdir();
		}
		String outputFilename = outpudirectory.getAbsolutePath()
				+ File.separator + "output_" + inputFilename;
		File file = FitFileRunner.process(inputFilename, outputFilename);
		return HtmlContentHandler.parse(file);
	}

	private FitResult parse(String filename) throws IOException {
		InputStream is = getInputStream(filename);
		return HtmlContentHandler.parse(new InputSource(is));
	}

	private InputStream getInputStream(String filename)
			throws FileNotFoundException {
		InputStream is = getClass().getResourceAsStream(filename);
		if (is == null) {
			throw new FileNotFoundException(filename
					+ " cannot be opened because it does not exist");
		}
		return is;
	}

}
