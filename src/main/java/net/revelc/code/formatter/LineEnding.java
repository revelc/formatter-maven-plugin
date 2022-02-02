/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter;

/**
 * The Enum LineEnding.
 */
public enum LineEnding {

    /** The auto. */
    AUTO(System.lineSeparator()),
    /** The keep. */
    KEEP(null),
    /** The lf. */
    LF("\n"),
    /** The crlf. */
    CRLF("\r\n"),
    /** The cr. */
    CR("\r"),
    /** The unknown. */
    UNKNOWN(null);

    /** The chars. */
    private final String chars;

    /**
     * Instantiates a new line ending.
     *
     * @param value
     *            the value
     */
    LineEnding(final String value) {
        this.chars = value;
    }

    /**
     * Gets the chars.
     *
     * @return the chars
     */
    public String getChars() {
        return this.chars;
    }

    /**
     * Checks if is system.
     *
     * @return true, if is system
     */
    public boolean isSystem() {
        return System.lineSeparator().equals(this.getChars());
    }

    /**
     * Returns the most occurring line-ending characters in the file text or null if no line-ending occurs the most.
     *
     * @param fileDataString
     *            the file data string
     * 
     * @return the line ending
     */
    public static LineEnding determineLineEnding(final String fileDataString) {
        var lfCount = 0;
        var crCount = 0;
        var crlfCount = 0;

        for (var i = 0; i < fileDataString.length(); i++) {
            final var c = fileDataString.charAt(i);
            if (c == '\r') {
                if ((i + 1) < fileDataString.length() && fileDataString.charAt(i + 1) == '\n') {
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
        return UNKNOWN;
    }

}
