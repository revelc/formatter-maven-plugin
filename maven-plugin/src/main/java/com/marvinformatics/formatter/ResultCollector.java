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

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Stopwatch;

public class ResultCollector {

	private final AtomicInteger successCount = new AtomicInteger();

	private final AtomicInteger failCount = new AtomicInteger();

	private final AtomicInteger skippedCount = new AtomicInteger();

	private final Stopwatch watch;

	public ResultCollector(Stopwatch watch) {
		super();
		this.watch = watch;
	}

	public void collect(Result result) {
		switch (result) {
		case SUCCESS:
			successCount.incrementAndGet();
			break;
		case FAIL:
			failCount.incrementAndGet();
			break;
		case SKIPPED:
			skippedCount.incrementAndGet();
			break;
		}
	}

	public int successCount() {
		return successCount.get();
	}

	public int failCount() {
		return failCount.get();
	}

	public int skippedCount() {
		return skippedCount.get();
	}

	public Stopwatch getWatch() {
		return watch;
	}

}
