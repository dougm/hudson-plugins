package org.jvnet.hudson.plugins.fit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public final class HtmlContentHandler implements ContentHandler {
	public static final class FitResult {
		private int expectationsNumber;
		private int errorsNumber;

		void addExpectation() {
			expectationsNumber++;
		}

		public int getExpectationsNumber() {
			return expectationsNumber;
		}

		void addError() {
			errorsNumber++;
		}

		public int getErrorsNumber() {
			return errorsNumber;
		}
	}

	private class TdAttributes {
		boolean yellowBackground = false;
		boolean containsHorizontalLine = false;
		boolean containsExpected = false;
		boolean italicsDetected = false;
	}

	private TdAttributes tdAttributes;

	private FitResult fitResult = new FitResult();

	// private boolean italicsDetected = false;

	private boolean inTable = false;

	private boolean inItalics = false;

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String content = new String(ch, start, length);
		if (tdAttributes != null && tdAttributes.italicsDetected) {
			if (!tdAttributes.containsExpected) {
				tdAttributes.containsExpected = containsExpected(content);
			}
			if (tdAttributes.containsExpected && containsActual(content)
					&& inItalics) {
				fitResult.addExpectation();
			}
		}
	}

	private boolean containsExpected(String content) {
		return content.startsWith("expected");
	}

	private boolean containsActual(String content) {
		return content.startsWith("actual");
	}

	public void endDocument() throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (isElementItalics(localName)) {
			inItalics = true;
		}
		if (isElementTable(localName)) {
			inTable = true;
		} else if (isElementTd(localName) && inTable) {
			tdAttributes = new TdAttributes();
			tdAttributes.yellowBackground = isBackgroundColorYellow(atts);
		} else if (tdAttributes != null) {
			if (isElementItalics(localName)) {
				tdAttributes.italicsDetected = true;
			}
			if (tdAttributes.yellowBackground
					&& !tdAttributes.containsHorizontalLine) {
				tdAttributes.containsHorizontalLine = isElementHr(localName);
				if (tdAttributes.containsHorizontalLine) {
					fitResult.addError();
				}
			}
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (isElementItalics(localName)) {
			inItalics = false;
		}
		if (isElementTable(localName)) {
			inTable = false;
			tdAttributes = null;
		} else if (isElementTd(localName)) {
			tdAttributes = null;
		}

	}

	private boolean isElementHr(String localName) {
		return StringUtils.equalsIgnoreCase(localName, "hr");
	}

	private boolean isBackgroundColorYellow(Attributes atts) {
		// the pink background For fit results is #ffcfcf
		return StringUtils
				.equalsIgnoreCase("#ffffcf", atts.getValue("bgcolor"));
	}

	private boolean isElementTd(String localName) {
		return StringUtils.equalsIgnoreCase(localName, "td");
	}

	private boolean isElementItalics(String localName) {
		return StringUtils.equalsIgnoreCase(localName, "i");
	}

	private boolean isElementTable(String localName) {
		return StringUtils.equalsIgnoreCase(localName, "table");
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

	protected static HtmlContentHandler.FitResult parse(String filename)
			throws IOException {
		return new HtmlContentHandler().parse(filename, null);
	}

	protected static HtmlContentHandler.FitResult parse(InputSource input)
			throws IOException {
		return new HtmlContentHandler().parse(null, input);
	}

	protected static HtmlContentHandler.FitResult parse(File file)
			throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return parse(new InputSource(fileInputStream));
	}

	private HtmlContentHandler.FitResult parse(String filename,
			InputSource inputSource) throws IOException {
		try {
			Parser parser = new Parser();
			parser.setContentHandler(this);
			if (filename != null) {
				parser.parse(filename);
			} else {
				parser.parse(inputSource);
			}
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		return fitResult;
	}
}