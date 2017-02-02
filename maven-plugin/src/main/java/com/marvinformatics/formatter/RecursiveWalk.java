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
package com.marvinformatics.formatter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Stream;

import org.codehaus.plexus.util.MatchPatterns;

import com.marvinformatics.formatter.java.JavaFormatter;
import com.marvinformatics.formatter.javascript.JavascriptFormatter;

public class RecursiveWalk extends RecursiveAction {
	private static final long serialVersionUID = 6913234076030245489L;
	private final Stream<Path> paths;
	private final ThreadLocal<JavaFormatter> javaFormatter;
	private final ResultCollector resultCollector;
	private final ThreadLocal<JavascriptFormatter> jsFormatter;
	private final MatchPatterns excludes;

	public RecursiveWalk(ThreadLocal<JavaFormatter> javaFormatter, ThreadLocal<JavascriptFormatter> jsFormatter,
			ResultCollector resultCollector, Stream<Path> stream, MatchPatterns excludes) {
		super();
		this.paths = stream;
		this.javaFormatter = javaFormatter;
		this.jsFormatter = jsFormatter;
		this.resultCollector = resultCollector;
		this.excludes = excludes;
	}

	public RecursiveWalk(ThreadLocal<JavaFormatter> javaFormatter, ThreadLocal<JavascriptFormatter> jsFormatter,
			ResultCollector resultCollector, Path path, MatchPatterns excludes) {
		this(javaFormatter, jsFormatter, resultCollector, Collections.singletonList(path).stream(), excludes);
	}

	@Override
	protected void compute() {
		final List<RecursiveWalk> walks = new ArrayList<>();
		paths.parallel().forEach(path -> {
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						if (matchExclusions(dir))
							return FileVisitResult.SKIP_SUBTREE;

						if (!dir.equals(path)) {
							RecursiveWalk w = new RecursiveWalk(javaFormatter, jsFormatter, resultCollector, dir,
									excludes);
							w.fork();
							walks.add(w);

							return FileVisitResult.SKIP_SUBTREE;
						} else {
							return FileVisitResult.CONTINUE;
						}
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						String name = file.getName(file.getNameCount() - 1).toString();

						if (matchExclusions(file))
							return FileVisitResult.CONTINUE;

						if (name.endsWith(".java"))
							resultCollector.collect(javaFormatter.get().formatFile(file));
						if (name.endsWith(".js"))
							resultCollector.collect(jsFormatter.get().formatFile(file));

						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				completeExceptionally(e);
			}
		});

		for (RecursiveWalk w : walks) {
			w.join();
		}
	}

	private boolean matchExclusions(Path path) {
		return excludes.matches(path.toAbsolutePath().toString(), true);
	}
}
