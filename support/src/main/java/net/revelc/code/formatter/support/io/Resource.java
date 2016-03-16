/**
 * Copyright 2010-2016. All work is copyrighted to their respective
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
package net.revelc.code.formatter.support.io;

import static net.revelc.code.formatter.support.text.Template.as;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import net.revelc.code.formatter.support.text.Template;

/**
 * Abstraction layer for resources file and class-path resources.
 */
public abstract class Resource {

    private final String path;

    private final String nativePath;

    protected Resource(final String newPath) {
        this.path = requireNonNull(newPath, "path shall not be null");
        this.nativePath = newPath.substring(getPrefix().length() + 1);
    }

    /**
     * Loads the resource and returns an InputStream from which to load it.
     * @return Input stream for the resource
     * @throws UnknownResourceException If the resource is not accessible
     */
    public final InputStream asInputStream() throws UnknownResourceException {
        return toInputStream();
    }

    /**
     * Loads the resource and returns it as a string.
     * @return String obtained from the resource
     * @throws UnknownResourceException If the resource is not accessible
     */
    public final String asString() throws UnknownResourceException {
        String result = null;

        try (final InputStream stream = asInputStream(); Scanner scanner = new Scanner(stream, UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new UnknownResourceException("Error loading resource", e);
        }

        return result;
    }

    protected abstract InputStream toInputStream() throws UnknownResourceException;

    protected abstract String getPrefix();

    /**
     * Gets the path to the resource.
     * @return Resource path
     */
    public final String getPath() {
        return this.path;
    }

    /**
     * Gets the path to the resource in terms native to the resource type.
     * @return Native path
     */
    public final String getNativePath() {
        return this.nativePath;
    }

    /**
     * Creates an instance for a path prefixed by either classpath: or file:.
     * @param path Path to the resource
     * @return Resource instance
     * @throws UnknownResourceException If the supplied path can not be resolved
     *         to a resource
     */
    public static Resource forPath(final String path) throws UnknownResourceException {

        Resource resource = null;

        if (path.startsWith(ClasspathResource.PREFIX + ":")) {
            resource = new ClasspathResource(path);
        } else if (path.startsWith(FileResource.PREFIX + ":")) {
            resource = new FileResource(path);
        } else {
            resource = forPath(FileResource.PREFIX + ":" + path);
        }

        return resource;
    }

    /**
     * Class-path based implementation of the {@link Resource} class.
     */
    static class ClasspathResource extends Resource {

        public static final String PREFIX = "classpath";

        ClasspathResource(String path) {
            super(path);
        }

        @Override
        public final String getPrefix() {
            return PREFIX;
        }

        @Override
        protected InputStream toInputStream() throws UnknownResourceException {
            final InputStream is = getClass().getResourceAsStream(getNativePath());

            if (is == null) {
                throw new UnknownResourceException("Unknown resource at " + getPath());
            }

            return is;
        }
    }

    /**
     * File-system based implementation of the {@link Resource} class.
     */
    static class FileResource extends Resource {

        private static final Template UNKNOWN = as("Unknown resource %s");

        private static final Template NOT_FILE = as("Resource not a file %s");

        private static final Template NOT_READ = as("Resource not readable %s");

        public static final String PREFIX = "file";

        protected FileResource(final String path) {
            super(path);
        }

        @Override
        public final String getPrefix() {
            return PREFIX;
        }

        @Override
        protected InputStream toInputStream() throws UnknownResourceException {
            final File file = new File(getNativePath());

            if (!file.exists()) {
                throw new UnknownResourceException(UNKNOWN.format(getPath()));
            }

            if (!file.isFile()) {
                throw new UnknownResourceException(NOT_FILE.format(getPath()));
            }

            if (!file.canRead()) {
                throw new UnknownResourceException(NOT_READ.format(getPath()));
            }

            InputStream stream;

            try {
                stream = new FileInputStream(file);
            } catch (final FileNotFoundException e) {
                throw new UnknownResourceException(NOT_READ.format(getPath()));
            }

            return stream;
        }
    }

    /**
     * Exception to indicate that a {@link Resource} can not be loaded.
     */
    @SuppressWarnings("serial")
    public static class UnknownResourceException extends Exception {

        /**
         * Constructs a new UnknownResourceException instance.
         * @param message Error message
         * @param cause underlying cause
         */
        public UnknownResourceException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new UnknownResourceException instance.
         * @param message Error message
         */
        public UnknownResourceException(String message) {
            super(message);
        }
    }
}
