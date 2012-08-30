package com.relativitas.maven.plugins.formatter;

/**
 * @author marvin.froeder
 */
public enum LineEnding {

	AUTO(System.getProperty("line.separator")), KEEP(null), LF("\n"), CRLF(
			"\r\n"), CR("\r");

	private final String chars;

	LineEnding(String chars) {
		this.chars = chars;
	}

	public String getChars() {
		return chars;
	}

	/**
	 * Returns the most occurring line-ending characters in the file text or null if no line-ending occurs the most.
	 * 
	 * @return
	 */
	public static LineEnding determineLineEnding(String fileDataString) {
		int lfCount = 0;
		int crCount = 0;
		int crlfCount = 0;

		for (int i = 0; i < fileDataString.length(); i++) {
			char c = fileDataString.charAt(i);
			if (c == '\r') {
				if ((i + 1) < fileDataString.length()
						&& fileDataString.charAt(i + 1) == '\n') {
					crlfCount++;
					i++;
				} else {
					crCount++;
				}
			} else if (c == '\n') {
				lfCount++;
			}
		}

		if (lfCount > crCount && lfCount > crlfCount) {
			return LF;
		} else if (crlfCount > lfCount && crlfCount > crCount) {
			return CRLF;
		} else if (crCount > lfCount && crCount > crlfCount) {
			return CR;
		}
		return null;
	}

}
