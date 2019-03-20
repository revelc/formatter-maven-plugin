/*******************************************************************************
 * Copyright (c) 2004, 2011 John-Mason P. Shackelford and others.,
 *               2009 Jose Montoya
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Contributors:
 *     John-Mason P. Shackelford - initial API and implementation
 * 	   IBM Corporation - bug fixes
 * 	   Jose Montoya - Modified implementation outside Eclipse Platform
 *******************************************************************************/
package net.revelc.code.formatter.xml.eclipse;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Based on Eclipse Ant Formatter and distributed under original Eclipse 2.0 license and compatible Apache 2.0 See:
 * https://github.com/eclipse/eclipse.platform/tree/master/ant/org.eclipse.ant.ui/Ant%20Editor/org/eclipse/ant/internal/ui/editor/formatter
 */
public class XMLTagFormatter {

    public String format(String tagText, String indent, String lineDelimiter, FormattingPreferences prefs) {
        Tag tag;

        if (tagText.startsWith("</") || tagText.startsWith("<%") //$NON-NLS-1$ //$NON-NLS-2$
                || tagText.startsWith("<?") || tagText.startsWith("<[")) { //$NON-NLS-1$ //$NON-NLS-2$
            return tagText;
        }

        try {
            tag = new TagParser().parse(tagText);
        } catch (ParseException e) {
            // if we can't parse the tag, give up and leave the text as is.
            return tagText;
        }

        return new TagFormatter(prefs).format(tag, indent, lineDelimiter);
    }

    public static class AttributePair {

        private String fAttribute;
        private String fValue;
        private char fQuote;

        public AttributePair(String attribute, String value, char attributeQuote) {
            fAttribute = attribute;
            fValue = value;
            fQuote = attributeQuote;
        }

        public String getAttribute() {
            return fAttribute;
        }

        public String getValue() {
            return fValue;
        }

        public char getQuote() {
            return fQuote;
        }
    }

    protected static class ParseException extends Exception {

        private static final long serialVersionUID = 1L;

        public ParseException(String message) {
            super(message);
        }
    }

    protected static class Tag {

        private List<AttributePair> fAttributes = new ArrayList<>();

        private boolean fClosed;

        private String fElementName;

        public void addAttribute(String attribute, String value, char quote) {
            fAttributes.add(new AttributePair(attribute, value, quote));
        }

        public int attributeCount() {
            return fAttributes.size();
        }

        public AttributePair getAttributePair(int i) {
            return fAttributes.get(i);
        }

        public String getElementName() {
            return this.fElementName;
        }

        public void setElementName(String elementName) {
            fElementName = elementName;
        }

        public boolean isClosed() {
            return fClosed;
        }

        public void setClosed(boolean closed) {
            fClosed = closed;
        }

        public int minimumLength() {
            int length = 2; // for the < >
            if (this.isClosed())
                length++; // if we need to add an />
            length += this.getElementName().length();
            if (this.attributeCount() > 0 || this.isClosed())
                length++;
            for (int i = 0; i < this.attributeCount(); i++) {
                AttributePair attributePair = this.getAttributePair(i);
                length += attributePair.getAttribute().length();
                length += attributePair.getValue().length();
                length += 4; // equals sign, quote characters & trailing space
            }
            if (this.attributeCount() > 0 && !this.isClosed())
                length--;
            return length;
        }

        public void setAttributes(List<AttributePair> attributePair) {
            fAttributes.clear();
            fAttributes.addAll(attributePair);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(500);
            sb.append('<');
            sb.append(this.getElementName());
            if (this.attributeCount() > 0 || this.isClosed())
                sb.append(' ');

            for (int i = 0; i < this.attributeCount(); i++) {
                AttributePair attributePair = this.getAttributePair(i);
                sb.append(attributePair.getAttribute());
                sb.append('=');
                sb.append(attributePair.getQuote());
                sb.append(attributePair.getValue());
                sb.append(attributePair.getQuote());
                if (this.isClosed() || i != this.attributeCount() - 1)
                    sb.append(' ');
            }
            if (this.isClosed())
                sb.append('/');
            sb.append('>');
            return sb.toString();
        }
    }

    // if object creation is an issue, use static methods or a flyweight
    // pattern
    protected static class TagParser {

        private String fElementName;

        private String fParseText;

        protected List<AttributePair> getAttibutes(String elementText) throws ParseException {

            class Mode {
                private int mode;

                public void setAttributeNameSearching() {
                    mode = 0;
                }

                public void setAttributeNameFound() {
                    mode = 1;
                }

                public void setAttributeValueSearching() {
                    mode = 2;
                }

                public void setAttributeValueFound() {
                    mode = 3;
                }

                public void setFinished() {
                    mode = 4;
                }

                public boolean isAttributeNameSearching() {
                    return mode == 0;
                }

                public boolean isAttributeNameFound() {
                    return mode == 1;
                }

                public boolean isAttributeValueSearching() {
                    return mode == 2;
                }

                public boolean isAttributeValueFound() {
                    return mode == 3;
                }

                public boolean isFinished() {
                    return mode == 4;
                }
            }

            List<AttributePair> attributePairs = new ArrayList<>();

            CharacterIterator iter = new StringCharacterIterator(
                    elementText.substring(getElementName(elementText).length() + 2));

            // state for finding attributes
            Mode mode = new Mode();
            mode.setAttributeNameSearching();
            char attributeQuote = '"';
            StringBuilder currentAttributeName = null;
            StringBuilder currentAttributeValue = null;

            char c = iter.first();
            while (iter.getIndex() < iter.getEndIndex()) {

                switch (c) {

                case '"':
                case '\'':

                    if (mode.isAttributeValueSearching()) {

                        // start of an attribute value
                        attributeQuote = c;
                        mode.setAttributeValueFound();
                        currentAttributeValue = new StringBuilder(1024);

                    } else if (mode.isAttributeValueFound() && attributeQuote == c) {

                        // we've completed a pair!
                        AttributePair pair = new AttributePair(currentAttributeName.toString(),
                                currentAttributeValue.toString(), attributeQuote);

                        attributePairs.add(pair);

                        // start looking for another attribute
                        mode.setAttributeNameSearching();

                    } else if (mode.isAttributeValueFound() && attributeQuote != c) {

                        // this quote character is part of the attribute value
                        currentAttributeValue.append(c);

                    } else {
                        // this is no place for a quote!
                        throw new ParseException("Unexpected '" + c //$NON-NLS-1$
                                + "' when parsing:\n\t" + elementText); //$NON-NLS-1$
                    }
                    break;

                case '=':

                    if (mode.isAttributeValueFound()) {

                        // this character is part of the attribute value
                        currentAttributeValue.append(c);

                    } else if (mode.isAttributeNameFound()) {

                        // end of the name, now start looking for the value
                        mode.setAttributeValueSearching();

                    } else {
                        // this is no place for an equals sign!
                        throw new ParseException("Unexpected '" + c //$NON-NLS-1$
                                + "' when parsing:\n\t" + elementText); //$NON-NLS-1$
                    }
                    break;

                case '/':
                case '>':
                    if (mode.isAttributeValueFound()) {
                        // attribute values are CDATA, add it all
                        currentAttributeValue.append(c);
                    } else if (mode.isAttributeNameSearching()) {
                        mode.setFinished();
                    } else if (mode.isFinished()) {
                        // consume the remaining characters
                    } else {
                        // we aren't ready to be done!
                        throw new ParseException("Unexpected '" + c //$NON-NLS-1$
                                + "' when parsing:\n\t" + elementText); //$NON-NLS-1$
                    }
                    break;

                default:

                    if (mode.isAttributeValueFound()) {
                        // attribute values are CDATA, add it all
                        currentAttributeValue.append(c);

                    } else if (mode.isFinished()) {
                        if (!Character.isWhitespace(c)) {
                            throw new ParseException("Unexpected '" + c //$NON-NLS-1$
                                    + "' when parsing:\n\t" + elementText); //$NON-NLS-1$
                        }
                    } else {
                        if (!Character.isWhitespace(c)) {
                            if (mode.isAttributeNameSearching()) {
                                // we found the start of an attribute name
                                mode.setAttributeNameFound();
                                currentAttributeName = new StringBuilder(255);
                                currentAttributeName.append(c);
                            } else if (mode.isAttributeNameFound()) {
                                currentAttributeName.append(c);
                            }
                        }
                    }
                    break;
                }

                c = iter.next();
            }
            if (!mode.isFinished()) {
                throw new ParseException("Element did not complete normally."); //$NON-NLS-1$
            }
            return attributePairs;
        }

        /**
         * @param tagText
         *            text of an XML tag
         * @return extracted XML element name
         */
        protected String getElementName(String tagText) throws ParseException {
            if (!tagText.equals(this.fParseText) || this.fElementName == null) {
                int endOfTag = tagEnd(tagText);
                if ((tagText.length() > 2) && (endOfTag > 1)) {
                    this.fParseText = tagText;
                    this.fElementName = tagText.substring(1, endOfTag);
                } else {
                    throw new ParseException("No element name for the tag:\n\t" //$NON-NLS-1$
                            + tagText);
                }
            }
            return fElementName;
        }

        protected boolean isClosed(String tagText) {
            return tagText.charAt(tagText.lastIndexOf('>') - 1) == '/';
        }

        /**
         * @param tagText
         * @return a fully populated tag
         */
        public Tag parse(String tagText) throws ParseException {
            Tag tag = new Tag();
            tag.setElementName(getElementName(tagText));
            tag.setAttributes(getAttibutes(tagText));
            tag.setClosed(isClosed(tagText));
            return tag;
        }

        private int tagEnd(String text) {
            // This is admittedly a little loose, but we don't want the
            // formatter to be too strict...
            // http://www.w3.org/TR/2000/REC-xml-20001006#NT-Name
            for (int i = 1; i < text.length(); i++) {
                char c = text.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != ':' && c != '.' && c != '-' && c != '_') {
                    return i;
                }
            }
            return -1;
        }
    }

    protected class TagFormatter {

        private final FormattingPreferences prefs;

        public TagFormatter(FormattingPreferences prefs) {
            this.prefs = prefs;
        }

        private int countChar(char searchChar, String inTargetString) {
            StringCharacterIterator iter = new StringCharacterIterator(inTargetString);
            int i = 0;
            if (iter.first() == searchChar)
                i++;
            while (iter.getIndex() < iter.getEndIndex()) {
                if (iter.next() == searchChar) {
                    i++;
                }
            }
            return i;
        }

        protected void openTag(StringBuilder sb, Tag tag) {
            sb.append('<');
            sb.append(tag.getElementName());
        }

        protected void closeTag(StringBuilder sb, Tag tag) {
            if (tag.isClosed()) {
                sb.append(' ');
                sb.append('/');
            }
            sb.append('>');
        }

        public String format(Tag tag, String indent, String lineDelimiter) {
            if (prefs.isSetSplitMultiAttrs()) {
                StringBuilder sb = new StringBuilder(1024);
                openTag(sb, tag);

                String extraIndent = indent + prefs.getCanonicalIndent();

                for (int i = 0; i < tag.attributeCount(); i++) {
                    AttributePair pair = tag.getAttributePair(i);

                    sb.append(lineDelimiter);
                    sb.append(extraIndent);
                    sb.append(pair.getAttribute());
                    sb.append('=');
                    sb.append(pair.getQuote());
                    sb.append(pair.getValue());
                    sb.append(pair.getQuote());
                }

                closeTag(sb, tag);

                return sb.toString();

            } else if (prefs.wrapLongTags()
                    && lineRequiresWrap(indent + tag.toString(), prefs.getMaximumLineWidth(), prefs.getTabWidth())) {
                return wrapTag(tag, indent, lineDelimiter);
            }

            return tag.toString();
        }

        protected boolean lineRequiresWrap(String line, int lineWidth, int tabWidth) {
            return tabExpandedLineWidth(line, tabWidth) > lineWidth;
        }

        /**
         * @param line
         *            the line in which spaces are to be expanded
         * @param tabWidth
         *            number of spaces to substitute for a tab
         * @return length of the line with tabs expanded to spaces
         */
        protected int tabExpandedLineWidth(String line, int tabWidth) {
            int tabCount = countChar('\t', line);
            return (line.length() - tabCount) + (tabCount * tabWidth);
        }

        protected String wrapTag(Tag tag, String indent, String lineDelimiter) {
            StringBuilder sb = new StringBuilder(1024);
            StringBuilder pairBuffer;
            openTag(sb, tag);

            String extraIndent = indent + prefs.getCanonicalIndent();
            int currLineLength = tabExpandedLineWidth(indent, prefs.getTabWidth()) + sb.length();

            // If each attribute fits on the line append it, otherwise start a new line and continue
            for (int i = 0; i < tag.attributeCount(); i++) {
                AttributePair pair = tag.getAttributePair(i);
                pairBuffer = new StringBuilder(1024);

                pairBuffer.append(pair.getAttribute());
                pairBuffer.append('=');
                pairBuffer.append(pair.getQuote());
                pairBuffer.append(pair.getValue());
                pairBuffer.append(pair.getQuote());

                if (currLineLength + pairBuffer.length() < prefs.getMaximumLineWidth()) {
                    sb.append(' ');
                    sb.append(pairBuffer);

                    currLineLength += pairBuffer.length() + 1;
                } else {
                    sb.append(lineDelimiter);
                    sb.append(extraIndent);
                    sb.append(pairBuffer);

                    currLineLength = extraIndent.length() + pairBuffer.length();
                }
            }

            closeTag(sb, tag);

            return sb.toString();
        }
    }
}
