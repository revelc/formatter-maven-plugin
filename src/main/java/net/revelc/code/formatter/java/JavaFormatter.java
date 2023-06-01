/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.text.edits.TextEdit;

import net.revelc.code.formatter.AbstractCacheableFormatter;
import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;

/**
 * The Class JavaFormatter.
 */
public class JavaFormatter extends AbstractCacheableFormatter implements Formatter {

    /** The formatter. */
    private CodeFormatter formatter;

    /** The exclusion pattern. */
    private Pattern exclusionPattern;

    /** The configuration options */
    private Map<String, String> options;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JavaFormatter that = (JavaFormatter) o;
        return Objects.equals(formatter, that.formatter) && Objects.equals(exclusionPattern, that.exclusionPattern)
                && Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatter, exclusionPattern, options);
    }

    @Override
    public void init(final Map<String, String> options, final ConfigurationSource cfg) {
        super.initCfg(cfg);

        this.formatter = ToolFactory.createCodeFormatter(options, ToolFactory.M_FORMAT_EXISTING);
        this.options = options;
    }

    @Override
    public String doFormat(final String code, final LineEnding ending) throws BadLocationException {
        TextEdit te;
        try {
            final var regions = JavaFormatter.getRegions(code, this.exclusionPattern);
            te = this.formatter.format(CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS, code,
                    regions, 0, ending.getChars());
            if (te == null) {
                this.log.debug(
                        "Code cannot be formatted. Possible cause is unmatched source/target/compliance version.");
                return null;
            }
        } catch (final IndexOutOfBoundsException e) {
            this.log.debug("Code cannot be formatted for text -->" + code + "<--", e);
            return null;
        }

        final IDocument doc = new Document(code);
        te.apply(doc);
        final var formattedCode = doc.get();

        if (code.equals(formattedCode)) {
            return null;
        }
        return formattedCode;
    }

    @Override
    public boolean isInitialized() {
        return this.formatter != null;
    }

    /**
     * Sets the exclusion pattern.
     *
     * @param ep
     *            the new exclusion pattern
     */
    public void setExclusionPattern(final String ep) {
        this.exclusionPattern = Pattern.compile(ep, Pattern.MULTILINE);
    }

    /**
     * Gets the regions.
     *
     * @param code
     *            the code
     * @param exclusionPattern
     *            the exclusion pattern
     *
     * @return the regions
     */
    protected static IRegion[] getRegions(final String code, final Pattern exclusionPattern) {
        final List<IRegion> regions = new ArrayList<>();
        var start = 0;
        if (exclusionPattern != null) {
            final var matcher = exclusionPattern.matcher(code);
            while (matcher.find()) {
                final var s = matcher.start();
                final var e = matcher.end();
                regions.add(new Region(start, s - start));
                start = e;
            }
        }
        regions.add(new Region(start, code.length() - start));
        return regions.toArray(new IRegion[0]);
    }

}
