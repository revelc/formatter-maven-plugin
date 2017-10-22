/**
 * Copyright 2010-2017. All work is copyrighted to their respective
 * author(s), unless otherwise stated.
 *
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.revelc.code.formatter.ConfigurationSource;
import net.revelc.code.formatter.Formatter;
import net.revelc.code.formatter.LineEnding;
import net.revelc.code.formatter.Result;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public abstract class AbstractFormatterTest {

    public static class TestConfigurationSource implements ConfigurationSource {

        private final File targetDir;

        public TestConfigurationSource(File targetDir) {
            this.targetDir = targetDir;
        }

        @Override
        public File getTargetDirectory() {
            return this.targetDir;
        }

        @Override
        public Log getLog() {
            return new SystemStreamLog();
        }

        @Override
        public Charset getEncoding() {
            return StandardCharsets.UTF_8;
        }

        @Override
        public String getCompilerSources() {
            return "1.8";
        }

        @Override
        public String getCompilerCompliance() {
            return "1.8";
        }

        @Override
        public String getCompilerCodegenTargetPlatform() {
            return "1.8";
        }
    }

    protected void doTestFormat(Formatter formatter, String fileUnderTest, String expectedSha512) throws IOException {
        File originalSourceFile = new File("src/test/resources/", fileUnderTest);
        File sourceFile = new File("target/testoutput/", fileUnderTest);

        Map<String, String> options = new HashMap<>();
        final File targetDir = new File("target/testoutput");
        targetDir.mkdirs();

        Files.copy(originalSourceFile, sourceFile);

        formatter.init(options, new TestConfigurationSource(targetDir));
        Result result = formatter.formatFile(sourceFile, LineEnding.CRLF, false);
        Assert.assertEquals(Result.SUCCESS, result);

        // We are hashing this as set in stone in case for some reason our source file changes unexpectedly.
        byte[] sha512 = Files.asByteSource(sourceFile).hash(Hashing.sha512()).asBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sha512.length; i++) {
            sb.append(Integer.toString((sha512[i] & 0xff) + 0x100, 16).substring(1));
        }

        Assert.assertEquals(expectedSha512, sb.toString());
    }

}
