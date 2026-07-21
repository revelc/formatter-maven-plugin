/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.revelc.code.formatter;

import java.nio.file.Path;
import java.util.Map;

/**
 * The Interface Formatter.
 */
public interface Formatter {

    /**
     * Initialize the {@link org.eclipse.jdt.core.formatter.CodeFormatter} instance to be used by this component.
     *
     * @param options
     *            the options
     * @param cfg
     *            the cfg
     */
    void init(Map<String, String> options, ConfigurationSource cfg);

    /**
     * Format individual file.
     *
     * @param file
     *            the file
     * @param originalCode
     *            the original code
     * @param ending
     *            the ending
     *
     * @return the string
     */
    String formatFile(Path file, String originalCode, LineEnding ending);

    /**
     * Return true if this formatter have been initialized.
     *
     * @return true, if is initialized
     */
    boolean isInitialized();

}
