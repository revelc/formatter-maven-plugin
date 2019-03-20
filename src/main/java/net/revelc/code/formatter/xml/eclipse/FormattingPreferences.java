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

/**
 * Based on Eclipse Ant Formatter and distributed under original Eclipse 2.0 license and compatible Apache 2.0 See:
 * https://github.com/eclipse/eclipse.platform/tree/master/ant/org.eclipse.ant.ui/Ant%20Editor/org/eclipse/ant/internal/ui/editor/formatter
 */
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