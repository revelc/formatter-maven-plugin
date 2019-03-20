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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Based on Eclipse Ant Formatter and distributed under original Eclipse 2.0 license and compatible Apache 2.0 See:
 * https://github.com/eclipse/eclipse.platform/tree/master/ant/org.eclipse.ant.ui/Ant%20Editor/org/eclipse/ant/internal/ui/editor/formatter
 */
public class XmlDocumentFormatter {

    private int depth;
    private final String fDefaultLineDelimiter;
    private final FormattingPreferences prefs;
    private boolean lastNodeWasText;
    private StringBuilder formattedXml;

    public XmlDocumentFormatter() {
        this(System.lineSeparator(), new FormattingPreferences());
    }

    public XmlDocumentFormatter(FormattingPreferences prefs) {
        this(System.lineSeparator(), prefs);
    }

    public XmlDocumentFormatter(String defaultLineDelimiter, FormattingPreferences prefs) {
        this.depth = -1;
        this.fDefaultLineDelimiter = defaultLineDelimiter;
        this.prefs = prefs;
    }

    private void copyNode(Reader reader, StringBuilder out) throws IOException {
        TagReader tag = TagReaderFactory.createTagReaderFor(reader);
        depth = depth + tag.getPreTagDepthModifier();

        if (!lastNodeWasText) {

            if (tag.startsOnNewline() && !hasNewlineAlready(out)) {
                out.append(fDefaultLineDelimiter);
            }

            if (tag.requiresInitialIndent()) {
                out.append(indent(prefs.getCanonicalIndent()));
            }
        }

        if (tag instanceof XmlElementReader) {
            out.append(new XMLTagFormatter().format(tag.getTagText(), indent(prefs.getCanonicalIndent()),
                    fDefaultLineDelimiter, prefs));
        } else {
            out.append(tag.getTagText());
        }

        depth = depth + tag.getPostTagDepthModifier();
        lastNodeWasText = tag.isTextNode();

    }

    public String format(String documentText) {
        Reader reader = new StringReader(documentText);
        formattedXml = new StringBuilder();

        if (depth == -1) {
            depth = 0;
        }
        lastNodeWasText = false;
        try {
            while (true) {
                reader.mark(1);
                int intChar = reader.read();
                reader.reset();

                if (intChar != -1) {
                    copyNode(reader, formattedXml);
                } else {
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return formattedXml.toString();
    }

    private boolean hasNewlineAlready(StringBuilder out) {
        return out.lastIndexOf("\n") == formattedXml.length() - 1 //$NON-NLS-1$
                || out.lastIndexOf("\r") == formattedXml.length() - 1; //$NON-NLS-1$
    }

    private String indent(String canonicalIndent) {
        StringBuilder indent = new StringBuilder(30);
        for (int i = 0; i < depth; i++) {
            indent.append(canonicalIndent);
        }
        return indent.toString();
    }

    private static class CommentReader extends TagReader {

        private boolean complete = false;

        @Override
        protected void clear() {
            this.complete = false;
        }

        @Override
        public String getStartOfTag() {
            return "<!--"; //$NON-NLS-1$
        }

        @Override
        protected String readTag() throws IOException {
            int intChar;
            char c;
            StringBuilder node = new StringBuilder();

            while (!complete && (intChar = reader.read()) != -1) {
                c = (char) intChar;

                node.append(c);

                if (c == '>' && node.toString().endsWith("-->")) { //$NON-NLS-1$
                    complete = true;
                }
            }
            return node.toString();
        }
    }

    private static class DoctypeDeclarationReader extends TagReader {

        private boolean complete = false;

        @Override
        protected void clear() {
            this.complete = false;
        }

        @Override
        public String getStartOfTag() {
            return "<!"; //$NON-NLS-1$
        }

        @Override
        protected String readTag() throws IOException {
            int intChar;
            char c;
            StringBuilder node = new StringBuilder();

            while (!complete && (intChar = reader.read()) != -1) {
                c = (char) intChar;

                node.append(c);

                if (c == '>') {
                    complete = true;
                }
            }
            return node.toString();
        }

    }

    private static class ProcessingInstructionReader extends TagReader {

        private boolean complete = false;

        @Override
        protected void clear() {
            this.complete = false;
        }

        @Override
        public String getStartOfTag() {
            return "<?"; //$NON-NLS-1$
        }

        @Override
        protected String readTag() throws IOException {
            int intChar;
            char c;
            StringBuilder node = new StringBuilder();

            while (!complete && (intChar = reader.read()) != -1) {
                c = (char) intChar;

                node.append(c);

                if (c == '>' && node.toString().endsWith("?>")) { //$NON-NLS-1$
                    complete = true;
                }
            }
            return node.toString();
        }
    }

    private static abstract class TagReader {

        protected Reader reader;

        private String tagText;

        protected abstract void clear();

        public int getPostTagDepthModifier() {
            return 0;
        }

        public int getPreTagDepthModifier() {
            return 0;
        }

        abstract public String getStartOfTag();

        public String getTagText() {
            return this.tagText;
        }

        public boolean isTextNode() {
            return false;
        }

        protected abstract String readTag() throws IOException;

        public boolean requiresInitialIndent() {
            return true;
        }

        public void setReader(Reader reader) throws IOException {
            this.reader = reader;
            this.clear();
            this.tagText = readTag();
        }

        public boolean startsOnNewline() {
            return true;
        }
    }

    private static class TagReaderFactory {

        // Warning: the order of the Array is important!
        private static TagReader[] tagReaders = new TagReader[] { new CommentReader(), new DoctypeDeclarationReader(),
                new ProcessingInstructionReader(), new XmlElementReader() };

        private static TagReader textNodeReader = new TextReader();

        public static TagReader createTagReaderFor(Reader reader) throws IOException {

            char[] buf = new char[10];
            reader.mark(10);
            reader.read(buf, 0, 10);
            reader.reset();

            String startOfTag = String.valueOf(buf);

            for (int i = 0; i < tagReaders.length; i++) {
                if (startOfTag.startsWith(tagReaders[i].getStartOfTag())) {
                    tagReaders[i].setReader(reader);
                    return tagReaders[i];
                }
            }
            // else
            textNodeReader.setReader(reader);
            return textNodeReader;
        }
    }

    private static class TextReader extends TagReader {

        private boolean complete;

        private boolean isTextNode;

        @Override
        protected void clear() {
            this.complete = false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.ant.internal.ui.editor.formatter.XmlDocumentFormatter.TagReader# getStartOfTag()
         */
        @Override
        public String getStartOfTag() {
            return "";
        }

        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.ant.internal.ui.editor.formatter.XmlDocumentFormatter.TagReader# isTextNode()
         */
        @Override
        public boolean isTextNode() {
            return this.isTextNode;
        }

        @Override
        protected String readTag() throws IOException {

            StringBuilder node = new StringBuilder();

            while (!complete) {

                reader.mark(1);
                int intChar = reader.read();
                if (intChar == -1)
                    break;

                char c = (char) intChar;
                if (c == '<') {
                    reader.reset();
                    complete = true;
                } else {
                    node.append(c);
                }
            }

            // if this text node is just whitespace
            // remove it, except for the newlines.
            if (node.length() < 1) {
                this.isTextNode = false;

            } else if (node.toString().trim().length() == 0) {
                String whitespace = node.toString();
                node = new StringBuilder();
                for (int i = 0; i < whitespace.length(); i++) {
                    char whitespaceCharacter = whitespace.charAt(i);
                    if (whitespaceCharacter == '\n' || whitespaceCharacter == '\r') {
                        node.append(whitespaceCharacter);
                    }
                }
                this.isTextNode = false;

            } else {
                this.isTextNode = true;
            }
            return node.toString();
        }

        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.ant.internal.ui.editor.formatter.XmlDocumentFormatter.TagReader# requiresInitialIndent()
         */
        @Override
        public boolean requiresInitialIndent() {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.eclipse.ant.internal.ui.editor.formatter.XmlDocumentFormatter.TagReader# startsOnNewline()
         */
        @Override
        public boolean startsOnNewline() {
            return false;
        }
    }

    private static class XmlElementReader extends TagReader {

        private boolean complete = false;

        @Override
        protected void clear() {
            this.complete = false;
        }

        @Override
        public int getPostTagDepthModifier() {
            if (getTagText().endsWith("/>") || getTagText().endsWith("/ >")) { //$NON-NLS-1$ //$NON-NLS-2$
                return 0;
            } else if (getTagText().startsWith("</")) { //$NON-NLS-1$
                return 0;
            } else {
                return +1;
            }
        }

        @Override
        public int getPreTagDepthModifier() {
            if (getTagText().startsWith("</")) { //$NON-NLS-1$
                return -1;
            }
            return 0;
        }

        @Override
        public String getStartOfTag() {
            return "<"; //$NON-NLS-1$
        }

        @Override
        protected String readTag() throws IOException {

            StringBuilder node = new StringBuilder();

            boolean insideQuote = false;
            int intChar;

            while (!complete && (intChar = reader.read()) != -1) {
                char c = (char) intChar;

                node.append(c);
                // TODO logic incorrectly assumes that " is quote character
                // when it could also be '
                if (c == '"') {
                    insideQuote = !insideQuote;
                }
                if (c == '>' && !insideQuote) {
                    complete = true;
                }
            }
            return node.toString();
        }
    }

}