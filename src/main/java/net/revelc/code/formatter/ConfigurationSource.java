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

import java.io.File;
import java.nio.charset.Charset;

import org.apache.maven.plugin.logging.Log;

/**
 * The Interface ConfigurationSource.
 */
public interface ConfigurationSource {

    /**
     * Gets the log.
     *
     * @return the log
     */
    Log getLog();

    /**
     * Gets the compiler sources.
     *
     * @return the compiler sources
     */
    String getCompilerSources();

    /**
     * Gets the compiler compliance.
     *
     * @return the compiler compliance
     */
    String getCompilerCompliance();

    /**
     * Gets the compiler codegen target platform.
     *
     * @return the compiler codegen target platform
     */
    String getCompilerCodegenTargetPlatform();

    /**
     * Gets the target directory.
     *
     * @return the target directory
     */
    File getTargetDirectory();

    /**
     * Gets the encoding.
     *
     * @return the encoding
     */
    Charset getEncoding();

}
