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
package net.revelc.code.formatter.xml.eclipse;

public class FormattingPreferences {
    private int maxLineLength = 115;
    private boolean wrapLongLines = true;
    private boolean tabInsteadOfSpaces = true;
    private int tabWidth = 4;
    private boolean setSplitMultiAttrs = false;

    public void setMaxLineLength(Integer maxLineLength) {
        if (maxLineLength != null)
            this.maxLineLength = maxLineLength;
    }

    public void setWrapLongLines(Boolean wrapLongLines) {
        if (wrapLongLines != null)
            this.wrapLongLines = wrapLongLines;
    }

    public void setTabInsteadOfSpaces(Boolean tabInsteadOfSpaces) {
        if (tabInsteadOfSpaces != null)
            this.tabInsteadOfSpaces = tabInsteadOfSpaces;
    }

    public String getCanonicalIndent() {
        String canonicalIndent;
        if (useTabInsteadOfSpaces()) {
            canonicalIndent = "\t"; //$NON-NLS-1$
        } else {
            String tab = "";
            for (int i = 0; i < getTabWidth(); i++) {
                tab = tab.concat(" "); //$NON-NLS-1$
            }
            canonicalIndent = tab;
        }

        return canonicalIndent;
    }

    public int getMaximumLineWidth() {
        return maxLineLength;
    }

    public boolean wrapLongTags() {
        return wrapLongLines;
    }

    public int getTabWidth() {
        return tabWidth;
    }

    public void setTabWidth(Integer tabWidth) {
        if (tabWidth != null)
            this.tabWidth = tabWidth;
    }

    public boolean useTabInsteadOfSpaces() {
        return tabInsteadOfSpaces;
    }

    public boolean isSetSplitMultiAttrs() {
        return setSplitMultiAttrs;
    }

    public void setSetSplitMultiAttrs(Boolean setSplitMultiAttrs) {
        if (setSplitMultiAttrs != null)
            this.setSplitMultiAttrs = setSplitMultiAttrs;
    }
}