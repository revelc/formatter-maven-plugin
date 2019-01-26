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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is very similar to Formatter, but it is focused on CI servers.
 *
 * If the code ain't formatted as expected this will fail the build
 *
 * @author marvin.froeder
 */
public class ValidateTask extends FormatterTask implements ConfigurationSource {

    private Logger logger = LoggerFactory.getLogger(ValidateTask.class);

    // @Parameter(defaultValue = "false", property = "aggregator", required = true)
    private boolean aggregator;

    // @Parameter(defaultValue = "${project.executionRoot}", required = true)
    private boolean executionRoot;

    @Override
    public void execute() {
        if (this.aggregator && !this.executionRoot) {
            return;
        }

        super.execute();
    }

    @Override
    protected void doFormatFile(File file, ResultCollector rc, Properties hashCache, String basedirPath, boolean dryRun)
            throws IOException {
        super.doFormatFile(file, rc, hashCache, basedirPath, true);

        if (rc.successCount != 0) {
            throw new RuntimeException("File '" + file
                    + "' has not been previously formatted.  Please format file and commit before running validation!");
        }
        if (rc.failCount != 0) {
            throw new RuntimeException("Error formating '" + file + "' ");
        }
    }

}
