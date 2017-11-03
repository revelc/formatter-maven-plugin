/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
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
/**
 * 
 */
package com.marvinformatics.formatter.support.io;

import static com.marvinformatics.formatter.support.io.Resource.forPath;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import com.marvinformatics.formatter.support.io.Resource.UnknownResourceException;

/**
 * Test method for {@link Resource}.
 */
public class ResourceTest {

	/**
	 * File in the class-path under a layer of packages.
	 */
	private static final String SUBPACKAGE_TEST_FILE = "classpath:/com/marvinformatics/formatter/support/io/other_test_file";

	/**
	 * Path to a file that does not exist.
	 */
	private static final String INVALID_PACKAGE_TEST_FILE = "classpath:/this/is/intentionally/invalid/test_file";

	/**
	 * File in the class-path under a the default package.
	 */
	private static final String ROOT_TEST_FILE = "classpath:/test_file";

	/**
	 * File in the file-system under a the default package.
	 */
	private static final String FILE_ROOT_TEST_FILE = "file:src/test/resources/test_file";

	/**
	 * Test method for {@link Resource#asInputStream()}.
	 * <p />
	 * This test case assumes a valid resource path in the default package.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@SuppressWarnings("resource")
	@Test
	public void testAsInputStreamValidDefaultPackageResource() throws UnknownResourceException {
		InputStream stream = forPath(ROOT_TEST_FILE).asInputStream();
		String result = new Scanner(stream, UTF_8.name()).useDelimiter("\\A").next();

		assertEquals("alpha\nbeta", result);
	}

	/**
	 * Test method for {@link Resource#asInputStream()}.
	 * <p />
	 * This test case assumes a valid resource path in a sub-package.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@SuppressWarnings("resource")
	@Test
	public void testAsInputStreamValidSubpackageResource() throws UnknownResourceException {
		InputStream stream = forPath(SUBPACKAGE_TEST_FILE).asInputStream();
		String result = new Scanner(stream, UTF_8.name()).useDelimiter("\\A").next();

		assertEquals("alpha subpackage\nbeta subpackage", result);
	}

	/**
	 * Test method for {@link Resource#asInputStream()}.
	 * <p />
	 * This test case assumes an invalid resource path that will induce failure.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@Test(expected = UnknownResourceException.class)
	public void testAsInputStreamInvalidResource() throws UnknownResourceException {
		forPath(INVALID_PACKAGE_TEST_FILE).asInputStream();
	}

	/**
	 * Test method for {@link Resource#asString()}.
	 * <p />
	 * This test case assumes a valid resource path.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testAsString() throws UnknownResourceException {
		String result = forPath(SUBPACKAGE_TEST_FILE).asString();
		assertEquals("alpha subpackage\nbeta subpackage", result);
	}

	/**
	 * Test method for {@link Resource#asInputStream()}.
	 * <p />
	 * This test case assumes a valid resource path.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@SuppressWarnings("resource")
	@Test
	public void testLoadInputStreamClasspath() throws UnknownResourceException {
		InputStream stream = forPath(SUBPACKAGE_TEST_FILE).asInputStream();
		String result = new Scanner(stream, UTF_8.name()).useDelimiter("\\A").next();

		assertEquals("alpha subpackage\nbeta subpackage", result);
	}

	/**
	 * Test method for {@link Resource#asInputStream()}.
	 * <p />
	 * This test case assumes a valid resource path.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@SuppressWarnings("resource")
	@Test
	public void testLoadInputStreamFile() throws UnknownResourceException {
		InputStream stream = forPath(FILE_ROOT_TEST_FILE).asInputStream();
		String result = new Scanner(stream, UTF_8.name()).useDelimiter("\\A").next();

		assertEquals("alpha\nbeta", result);
	}

	/**
	 * Test method for {@link Resource#getPrefix()}.
	 * <p />
	 * This test case assumes a classpath resource
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetPrefixClasspath() throws UnknownResourceException {
		String result = forPath(ROOT_TEST_FILE).getPrefix();
		assertEquals("classpath", result);
	}

	/**
	 * Test method for {@link Resource#getPrefix()}.
	 * <p />
	 * This test case assumes a file resource
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetPrefixFile() throws UnknownResourceException {
		String result = forPath(FILE_ROOT_TEST_FILE).getPrefix();
		assertEquals("file", result);
	}

	/**
	 * Test method for {@link Resource#getPath()}.
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetPathClasspath() throws UnknownResourceException {
		String result = forPath(ROOT_TEST_FILE).getPath();
		assertEquals("classpath:/test_file", result);
	}

	/**
	 * Test method for {@link Resource#getPath()}.
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetPathFile() throws UnknownResourceException {
		String result = forPath(FILE_ROOT_TEST_FILE).getPath();
		assertEquals("file:src/test/resources/test_file", result);
	}

	/**
	 * Test method for {@link Resource#getNativePath()}.
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetNativePathClasspath() throws UnknownResourceException {
		String result = forPath(ROOT_TEST_FILE).getNativePath();
		assertEquals("/test_file", result);
	}

	/**
	 * Test method for {@link Resource#getNativePath()}.
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testGetNativePathFile() throws UnknownResourceException {
		String result = forPath(FILE_ROOT_TEST_FILE).getNativePath();
		assertEquals("src/test/resources/test_file", result);
	}

	/**
	 * Test method for {@link Resource#forPath(java.lang.String)}.
	 * @throws IOException 
	 * @throws UnknownResourceException 
	 */
	@Test
	public void testForPath() throws UnknownResourceException {
		String result = forPath(ROOT_TEST_FILE).asString();
		assertEquals("alpha\nbeta", result);
	}
}
